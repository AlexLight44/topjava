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
<table border="1" cellpadding="8">
    <tr>
        <th>Дата и время</th>
        <th>Описание</th>
        <th>Калории</th>
    </tr>
    <c:forEach items="${meals}" var="meal">
        <tr class="${meal.excess ? 'excess' : 'normal'}">
            <td>${meal.dateTime.toString().replace('T', ' ')}</td>
            <td>${meal.description}</td>
            <td>${meal.calories}</td>
        </tr>
    </c:forEach>
</table>
</body>
</html>
