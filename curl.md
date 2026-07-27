# Examples of requests to MealRestController (curl)

All requests are executed from the user:

- **Email:** `user@yandex.ru`
- **Password:** `password`

---

* curl.exe for windows

### 1. Get all the food

```shell
curl -u user@yandex.ru:password http://localhost:8080/topjava/rest/meals
```

### 2. Get one food

```shell
curl -u user@yandex.ru:password http://localhost:8080/topjava/rest/meals/100003
```

### 3. Filtering (getBetween)

```shell
curl -u user@yandex.ru:password "http://localhost:8080/topjava/rest/meals/between?startDate=2020-01-30&startTime=10:00:00&endDate=2020-01-31&endTime=13:00:00"
```

### 4. Create food

```shell
curl -u user@yandex.ru:password -H "Content-Type: application/json" -X POST \
  -d "{\"dateTime\":\"2020-02-01T18:00:00\",\"description\":\"Тестовая еда\",\"calories\":300}" \
  http://localhost:8080/topjava/rest/meals
```

### 5. Check the created food

After creation, see the Location header in the response, the id may differ.

```shell
curl -u user@yandex.ru:password http://localhost:8080/topjava/rest/meals/100012
```

### 6. Update food

```shell
curl -u user@yandex.ru:password -H "Content-Type: application/json" -X PUT \
  -d "{\"dateTime\":\"2020-01-30T10:00:00\",\"description\":\"Обновленный завтрак\",\"calories\":200}" \
  http://localhost:8080/topjava/rest/meals/100003
```

### 7. Remove food

```shell
curl -u user@yandex.ru:password -X DELETE http://localhost:8080/topjava/rest/meals/100003
```