<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html lang="ru">
<head>
    <title>Meals</title>
    <link rel="stylesheet" href="css/style.css">
</head>
<body>
<h3><a href="index.html">Home</a></h3>
<hr>
<h2>Meals</h2>
<table>
    <tr>
        <th>Date</th>
        <th>Description</th>
        <th>Calories</th>
    </tr>
    <jsp:useBean id="mealsTo" scope="request" type="java.util.List"/>
    <jsp:useBean id="TimeUtil" scope="request" class="ru.javawebinar.topjava.util.TimeUtil"/>
    <c:forEach var="mealTo" items="${mealsTo}">
        <c:set var="excessClass" value="${mealTo.excess ? 'excess' : 'no-excess'}"/>
        <tr class="${excessClass}">
            <td>${TimeUtil.formatDate(mealTo.dateTime)}</td>
            <td>${mealTo.description}</td>
            <td>${mealTo.calories}</td>
        </tr>
    </c:forEach>
</table>
</body>
</html>
