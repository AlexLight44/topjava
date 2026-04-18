<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<html>
<head>
    <title>Список еды</title>
    <style>
        .excess {
            color: red;
        }

        .normal {
            color: green;
        }
    </style>
</head>
<body>
<h2>Список еды</h2>
<form method="post">
    <input type="hidden" name="id" value="${meal.id}">

    <label>Дата и время:
        <input type="datetime-local" name="dateTime"
               value="${meal.dateTime != null ? meal.dateTime.toString().replace('T', ' ') : ''}" required>
    </label><br><br>

    <label>Описание:
        <input type="text" name="description" value="${meal.description}" required>
    </label><br><br>

    <label>Калории:
        <input type="number" name="calories" value="${meal.calories}" required>
    </label><br><br>

    <button type="submit">Сохранить</button>
</form>
<hr>
<table border="1" cellpadding="8">
    <tr>
        <th>Дата и время</th>
        <th>Описание</th>
        <th>Калории</th>
        <th>Действия</th>
    </tr>
    <c:forEach items="${meals}" var="meal">
        <tr class="${meal.excess ? 'excess' : 'normal'}">
            <td>${meal.dateTime.toString().replace('T', ' ')}</td>
            <td>${meal.description}</td>
            <td>${meal.calories}</td>
            <td>
                <a href="meals?action=edit&id=${meal.id}">Edit</a> |
                <a href="meals?action=delete&id=${meal.id}" onclick="return confirm('Удалить?')">Delete</a>
            </td>
        </tr>
    </c:forEach>
</table>
</body>
</html>
