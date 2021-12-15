<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>

<!DOCTYPE html>

<html lang="en"
      xmlns:th="http://www.thymeleaf.org">
<head>
    <meta charset="UTF-8"/>
    <link rel="stylesheet" href="../../css/style.css"/>
    <link href='https://unpkg.com/boxicons@2.0.7/css/boxicons.min.css' rel='stylesheet'/>
    <meta name="viewport" content="width=device-width, initial-scale=1.0"/>

    <title>Flights : Admin Page</title>
</head>
<body>
<form method="post" action="admin">
    <div class="sidebar">
        <div class="logo-details">
            <span class="logo_name">Air</span>
        </div>
        <ul class="nav-links">
            <li>
                <a href="#" class="active">
                    <i class='bx bx-grid-alt' ></i>
                    <span class="links_name">Dashboard</span>
                </a>
            </li>

            <li class="log_out">
                <a href="login">
                    <i class='bx bx-log-out'></i>
                    <span class="links_name">Log out</span>
                </a>
            </li>
        </ul>
    </div>
    <section class="home-section">
        <nav>
            <div class="sidebar-button">
                <i class='bx bx-menu sidebarBtn'></i>
                <span class="dashboard">Airline reservation system</span>
            </div>
            <div class="search-box">
                <input type="text" placeholder="Search..."/>
                <i class='bx bx-search' ></i>
            </div>
        </nav>
        <div class="home-content">
            <div class="sales-boxes">
                <div class="recent-sales box">
                    <div style="margin-bottom: 20px;">
                        <div class="button" >
                            <input type="submit" value="Download flight data"/>
                        </div>
                    </div>
                    <div class="table-responsive">
                        <table class="table table-hover table-sm">
                            <thead>
                            <tr th:if="${flights.empty}">
                                <td colspan="9">
                                    Flight data was not found.
                                </td>
                            </tr>
                            </thead>
                            <thead th:if="${!flights.empty}">
                            <tr class="header">
                                <th colspan="2" class="pl-4">Flight Name</th>
                                <th>From</th>
                                <th>-></th>
                                <th>To</th>
                                <th>Departure time</th>
                                <th>Arrival time</th>
                                <th>Price</th>
                                <th>Weekday</th>
                            </tr>
                            </thead>
                            <tbody>
                            <tr th:each="flight : ${flights}">
                                <td class="pr-0 pl-4"></td>
                                <td>
                                    <small th:text="${flight.flightNumber}">1</small>
                                </td>
                                <td th:text="${flight.cityFrom}">1</td>
                                <td>    </td>
                                <td th:text="${flight.cityTo}">1</td>
                                <td th:text="${flight.timeFrom}">1</td>
                                <td th:text="${flight.timeTo}">1</td>
                                <td th:text="${flight.price}">1</td>
                                <td th:text="${flight.days}">1</td>
                            </tr>
                            </tbody>
                        </table>
                    </div>
                </div>
            </div>
        </div>
    </section>

    <script>
        let sidebar = document.querySelector(".sidebar");
        let sidebarBtn = document.querySelector(".sidebarBtn");
        sidebarBtn.onclick = function() {
            sidebar.classList.toggle("active");
            if(sidebar.classList.contains("active")){
                sidebarBtn.classList.replace("bx-menu" ,"bx-menu-alt-right");
            } else {
                sidebarBtn.classList.replace("bx-menu-alt-right", "bx-menu");
            }
        }
    </script>
</form>
</body>
</html>

