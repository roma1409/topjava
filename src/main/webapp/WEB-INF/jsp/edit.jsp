<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html lang="ru">
<head>
    <title>Meal</title>
    <link rel="stylesheet" href="css/style.css">
</head>
<body>
<jsp:include page="header.jsp"/>
<h2>Meal</h2>
<form action="meals" method="post">
    <input type="hidden" value="${meal.id}" name="id">
    <table class="edit-table">
        <tr>
            <td>Date</td>
            <td><input name="datetime" type="datetime-local" value="${meal.dateTime}" required></td>
        </tr>
        <tr>
            <td>Description</td>
            <td><textarea name="description" cols="26" required>${meal.description}</textarea></td>
        </tr>
        <tr>
            <td>Calories</td>
            <td><input name="calories" type="number" value="${meal.calories}" min="0" required></td>
        </tr>
    </table>
    <button type="reset" onclick="history.back()">Cancel</button>
    <button type="submit">Save</button>
</form>
</body>
</html>
