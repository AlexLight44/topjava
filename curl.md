# 1. Получить все еды GET
http://localhost:8080/topjava/rest/meals

# 2. Получить одну еду GET
http://localhost:8080/topjava/rest/meals/100003

# 3. Фильтрация (getBetween) GET
http://localhost:8080/topjava/rest/meals/between?startDate=2020-01-30&startTime=10:00:00&endDate=2020-01-31&endTime=13:00:00"

# 4. Создать еду POST
Basic Auth:
Username user@yandex.ru
Password password
Headers:
Key Content-Type
Value application/json
Body:
{
"dateTime": "2020-02-01T18:00:00",
"description": "Тестовая еда",
"calories": 300
}
http://localhost:8080/topjava/rest/meals

# 5. Проверить созданную еду GET

http://localhost:8080/topjava/rest/meals/100012

# 6. Удалить еду DELETE
http://localhost:8080/topjava/rest/meals/100003