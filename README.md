# ☕ Coffee Order API

> 다수 서버 환경에서도 안정적으로 동작하는 커피숍 주문 시스템

---

## 설계 내용

### ERD

```
users
├── id          BIGINT PK AUTO_INCREMENT
└── point       INT

menus
├── id          BIGINT PK AUTO_INCREMENT
├── name        VARCHAR
└── price       INT

orders
├── id          BIGINT PK AUTO_INCREMENT
├── user_id     BIGINT FK → users.id
├── total_price INT
└── created_at  DATETIME

order_items
├── id          BIGINT PK AUTO_INCREMENT
├── order_id    BIGINT FK → orders.id
├── menu_id     BIGINT FK → menus.id
└── price       INT
```

### API 명세

| 기능 | Method | URL |
|------|--------|-----|
| 커피 메뉴 목록 조회 | GET | /coffee/menus |
| 포인트 충전 | POST | /coffee/points/charge |
| 커피 주문/결제 | POST | /coffee/orders |
| 인기 메뉴 조회 | GET | /coffee/menus/popular |

---

#### 1. 커피 메뉴 목록 조회

**Request**
```
GET /coffee/menus
```

**Response 200**
```json
[
  { "menuId": 1, "name": "아메리카노", "price": 3000 },
  { "menuId": 2, "name": "카페라떼",   "price": 4000 }
]
```

---

#### 2. 포인트 충전

**Request**
```
POST /coffee/points/charge
```
```json
{
  "userId": 1,
  "amount": 10000
}
```

**Response 200**
```json
{
  "userId": 1,
  "point": 10000
}
```

**에러**

| 상황 | 메시지 |
|------|--------|
| 존재하지 않는 사용자 | 사용자를 찾을 수 없습니다. |
| amount < 1 | 충전 금액은 1 이상이어야 합니다. |

---

#### 3. 커피 주문/결제

**Request**
```
POST /coffee/orders
```
```json
{
  "userId": 1,
  "menuIds": [1, 2]
}
```

**Response 200**
```json
{
  "orderId": 1,
  "totalPrice": 7000
}
```

**에러**

| 상황 | 메시지 |
|------|--------|
| 존재하지 않는 사용자 | 사용자를 찾을 수 없습니다. |
| 존재하지 않는 메뉴 포함 | 존재하지 않는 메뉴가 포함되어 있습니다. |
| 포인트 부족 | 포인트가 부족합니다. |
| 메뉴 미선택 | 메뉴는 최소 1개 이상 선택해야 합니다. |

주문 완료 시 userId, menuIds, totalPrice를 데이터 수집 플랫폼으로 실시간 전송합니다 (Mock 로그 방식).

---

#### 4. 인기 메뉴 조회

**Request**
```
GET /coffee/menus/popular
```

**Response 200**
```json
[
  { "menuId": 1, "name": "아메리카노", "orderCount": 4 },
  { "menuId": 2, "name": "카페라떼",   "orderCount": 3 }
]
```

최근 7일 기준 주문 횟수 상위 3개 메뉴를 반환합니다.

---

## 설계의 의도

### 도메인 단위 패키지 분리

```
coffeeapi/
├── common/     # 전역 예외 처리
├── menu/       # 메뉴 조회, 인기 메뉴
├── order/      # 주문 생성, 결제
├── point/      # 포인트 충전
└── user/       # 사용자 도메인
```

각 도메인이 자신의 Controller / Service / Repository / DTO / Domain을 갖도록 구성했습니다.
도메인 간 의존은 서비스 레이어에서만 발생하도록 제한했습니다.

### 주문 당시 가격 스냅샷 저장

`order_items.price` 컬럼에 주문 시점의 가격을 별도로 저장합니다.
메뉴 가격이 나중에 변경되어도 과거 주문 금액이 달라지지 않도록 무결성을 보장합니다.

### 전역 예외 처리

`GlobalExceptionHandler`에서 `IllegalArgumentException`과 `MethodArgumentNotValidException`을
일관된 형식(`{ "message": "..." }`)으로 응답합니다.
컨트롤러에서 예외 처리 코드를 분리하여 비즈니스 로직에만 집중할 수 있도록 했습니다.

### 실시간 주문 데이터 전송

`RealTimeOrderSender` 컴포넌트가 주문 완료 직후 외부 데이터 수집 플랫폼 전송을 담당합니다.
현재는 Mock(로그 출력)으로 구현되어 있으며, 실제 환경에서는 HTTP Client 또는 Kafka 등으로 교체할 수 있도록 역할을 분리했습니다.

---

## 선택한 문제 해결 전략

### 인기 메뉴 집계

`OrderItem`과 `CoffeeOrder`를 JOIN하고 `createdAt >= 7일 전` 조건으로 필터링한 뒤,
메뉴 기준으로 GROUP BY하여 주문 횟수를 COUNT합니다.
JPQL에서 `Pageable`을 활용해 상위 3개만 가져옵니다.

```sql
SELECT m.id, m.name, COUNT(oi.id)
FROM order_items oi
JOIN menus m ON oi.menu_id = m.id
JOIN orders o ON oi.order_id = o.id
WHERE o.created_at >= (NOW() - INTERVAL 7 DAY)
GROUP BY m.id, m.name
ORDER BY COUNT(oi.id) DESC
LIMIT 3;
```

별도 집계 테이블 없이 주문 이력 데이터만으로 정확한 횟수를 계산할 수 있어 데이터 일관성이 보장됩니다.

### 포인트 차감 트랜잭션

포인트 부족 여부 검증 → 포인트 차감 → 주문 생성 → 저장을 하나의 `@Transactional` 안에서 처리합니다.
중간에 예외가 발생하면 전체가 롤백되어 포인트만 차감되고 주문이 누락되는 상황을 방지합니다.

### 예외 처리 전략

비즈니스 규칙 위반은 `IllegalArgumentException`으로 던지고, 입력값 검증은 `@Valid`와 Bean Validation으로 처리합니다.
두 경우 모두 `GlobalExceptionHandler`에서 받아 동일한 에러 응답 형식으로 반환합니다.

---

## 기술적 선택 이유

| 선택 | 이유 |
|------|------|
| Spring Boot | 빠른 설정, 내장 서버, Spring Data JPA와의 높은 호환성 |
| MySQL | 트랜잭션 지원(InnoDB), GROUP BY 집계 쿼리에 적합 |
| Spring Data JPA | 반복 SQL 감소, JPQL로 복잡한 집계 쿼리도 객체지향적으로 표현 가능 |
| Record DTO | 불변 객체로 DTO를 간결하게 표현, 불필요한 setter 제거 |
| `@RestControllerAdvice` | 예외 처리를 한 곳에서 관리하여 일관된 에러 응답 보장 |
| `org.springframework.transaction.annotation.Transactional` | Spring 트랜잭션 관리와 완전히 연동되어 롤백 규칙을 정확히 제어 |

---

## 실행 방법

### 1. DB 생성

```sql
CREATE DATABASE coffee;
```

### 2. application.yml 설정

```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/coffee
    username: root
    password: 비밀번호
  sql:
    init:
      mode: always
  jpa:
    hibernate:
      ddl-auto: update
```

### 3. 애플리케이션 실행

```bash
./gradlew bootRun
```

애플리케이션 시작 시 `data.sql`이 자동 실행되어 초기 메뉴·사용자 데이터가 삽입됩니다.

기본 포트: `http://localhost:8080`
