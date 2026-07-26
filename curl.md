# Примеры запросов к MealRestController (curl)

Все запросы выполняются от пользователя:
- **Email:** `user@yandex.ru`
- **Password:** `password`

---
*  curl.exe для windows

### 1. Получить всю еду

curl -u user@yandex.ru:password http://localhost:8080/topjava/rest/meals

### 2. Получить одну еду

curl -u user@yandex.ru:password http://localhost:8080/topjava/rest/meals/100003

### 3. Фильтрация (getBetween)

curl -u user@yandex.ru:password "http://localhost:8080/topjava/rest/meals/between?startDate=2020-01-30&startTime=10:00:00&endDate=2020-01-31&endTime=13:00:00"

### 4. Создать еду

curl -u user@yandex.ru:password -H "Content-Type: application/json" -X POST \
  -d "{\"dateTime\":\"2020-02-01T18:00:00\",\"description\":\"Тестовая еда\",\"calories\":300}" \
  http://localhost:8080/topjava/rest/meals

### 5. Проверить созданную еду

После создания смотри заголовок Location в ответе id может отличатся.

curl -u user@yandex.ru:password http://localhost:8080/topjava/rest/meals/100012

### 6. Обновить еду

curl -u user@yandex.ru:password -H "Content-Type: application/json" -X PUT \
  -d "{\"dateTime\":\"2020-01-30T10:00:00\",\"description\":\"Обновленный завтрак\",\"calories\":200}" \
  http://localhost:8080/topjava/rest/meals/100003

### 7. Удалить еду

curl -u user@yandex.ru:password -X DELETE http://localhost:8080/topjava/rest/meals/100003