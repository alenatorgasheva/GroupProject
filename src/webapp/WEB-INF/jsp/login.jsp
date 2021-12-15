<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8"/>
    <link rel="stylesheet" href="../../css/style_1.css"/>

    <title>Flights : Login</title>
</head>
<body>
<h2>Airline Reservation System</h2>
<div class="container" id="container">

    <div class="form-container sign-in-container">
        <form action="flights">
            <h1>Sign in</h1>

            <input type="email" placeholder="Email" />
            <input type="password" placeholder="Password" />
            <a href="#">Forgot your password?</a>

            <button type="submit" class="btn btn-primary btn-block">Sign In</button>
        </form>
    </div>
    <div class="overlay-container">
        <div class="overlay">
            <div class="overlay-panel overlay-right">
                <h1>Hello!</h1>
                <p>Enter your username and password</p>
            </div>
        </div>
    </div>
</div>


</body>
</html>