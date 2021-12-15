<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>

<!DOCTYPE html>

<html lang="en" dir="ltr"
      xmlns:th="http://www.thymeleaf.org">
<head>
    <meta charset="UTF-8"/>
    <meta charset="UTF-8"/>
    <meta name="viewport" content="width=device-width, initial-scale=1.0"/>
    <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.4.1/css/bootstrap.min.css" integrity="sha384-Vkoo8x4CGsO3+Hhxv8T/Q5PaXtkKtu6ug5TOeNV6gBiFeWPGFN9MuhOf23Q9Ifjh" crossorigin="anonymous"/>


    <link rel="stylesheet" href="../../css/style.css"/>

    <link href='https://unpkg.com/boxicons@2.0.7/css/boxicons.min.css' rel='stylesheet'/>
    <meta name="viewport" content="width=device-width, initial-scale=1.0"/>
</head>
<body>

<div class="sidebar">
    <div class="logo-details">

        <span class="logo_name"></span>
    </div>
    <ul class="nav-links">
        <li>
            <a href="#" class="active">
                <i class='bx bx-grid-alt' ></i>
                <span class="links_name">Dashboard</span>
            </a>
        </li>

        <li class="log_out">
            <a href="login.html">
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
        <div>
            <a>example@mail.ru</a>
        </div>
    </nav>

    <div class="home-content">
        <div class="sales-boxes">
            <div class="recent-sales box">
                <div class="table-responsive">
                    <main class="py-4">
                        <div class="container">
                            <div class="row justify-content-center">
                                <div class="col-md-12">
                                    <div class="card">
                                        <div class="card-body">
                                            <div class="page-container">
                                                <form id="searchForm"
                                                      th:action="@{/flights(search)}"
                                                      th:object="${search}"
                                                      action="#"
                                                      method="post">
                                                    <label for="cityFrom">
                                                        <input type="text" placeholder="City From"
                                                               th:field="*{cityFrom}"
                                                               th:class="${#fields.hasErrors('cityFrom')} ? 'field-error'"/>
                                                    </label>

                                                    <label for="cityTo">
                                                        <input type="text" placeholder="City To"
                                                               th:field="*{cityTo}"
                                                               th:class="${#fields.hasErrors('cityTo')} ? 'field-error'"/>
                                                    </label>

                                                    <label for="date">
                                                        <input type="date" th:min="${#calendars.format(#calendars.createNow(), 'yyyy-MM-dd')}"
                                                               th:field="*{date}"
                                                               th:class="${#fields.hasErrors('date')} ? 'field-error'"/>
                                                    </label>

                                                    <label for="passengersCount">
                                                        <input type="number" value="1"
                                                               th:field="*{passengersCount}"/>
                                                    </label>

                                                    <input type="submit" value="Search"/>

                                                    <div th:if="${#fields.hasErrors('*')}"
                                                         class="alert alert-error">
                                                        <p th:each="error : ${#fields.errors('*')}"
                                                           th:text="${error}">
                                                            Validation error
                                                        </p>
                                                    </div>
                                                    <div th:if="${!#fields.hasErrors('*')}"
                                                         class="alert alert-error">
                                                        <p th:if="${flights == null or flights.isEmpty()}">
                                                            Sorry: there are no suitable flights.
                                                        </p>
                                                    </div>
                                                </form>
                                            </div>

                                            <table id="filter" class="table"
                                                   th:if="${flights != null and !flights.isEmpty()}">
                                                <thead>
                                                <tr class="header">
                                                    <th>From</th>
                                                    <th>To</th>
                                                    <th>Flight Name</th>
                                                    <th>Departure time</th>
                                                    <th>Arrival time</th>
                                                    <th>Price</th>
                                                    <th> </th>
                                                </tr>
                                                </thead>
                                                <tbody>
                                                <tr th:each="flight : ${flights}">
                                                    <form id="buy"
                                                          th:action="@{/flights(buy=${flight.id}, passengers=${search.passengersCount})}"
                                                          action="#"
                                                          method="post">
                                                        <td th:text="${flight.cityFrom}">1</td>
                                                        <td th:text="${flight.cityTo}">1</td>
                                                        <td>
                                                            <small th:text="${flight.flightNumber}">1</small>
                                                        </td>
                                                        <td th:text="${flight.timeFrom}">1</td>
                                                        <td th:text="${flight.timeTo}">1</td>
                                                        <td th:text="${flight.price}">1</td>
                                                        <td><input type="submit" value="Buy"/></td>
                                                    </form>
                                                </tr>
                                                </tbody>
                                            </table>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </main>
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
        }else
            sidebarBtn.classList.replace("bx-menu-alt-right", "bx-menu");
    }
</script>
</body>
</html>