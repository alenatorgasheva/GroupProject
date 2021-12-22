import java.io.FileReader;
import java.io.IOException;
import java.sql.*;
import java.util.*;
import java.text.SimpleDateFormat;
import java.util.Date;

public class FlightService {

    private final String url = "jdbc:mysql://localhost:3306/group_project";
    private final String user = "root";
    private final String password = "password";

    private static FlightService instance;

    private Calendar calendar = Calendar.getInstance();

    public static synchronized FlightService getInstance() {
        if (instance == null) {
            instance = new FlightService();
        }
        return instance;
    }

    public User findUser(User userFlight) {
        try {
            Connection connection = DriverManager.getConnection(url, user, password);
            Statement statement = connection.createStatement();

            String query = "SELECT * FROM `users` WHERE (`login` = '" + userFlight.getLogin() + "') AND (`password` = '" + userFlight.getPassword() + "');";
            ResultSet resultSet = statement.executeQuery(query);
            while (resultSet.next()) {
                userFlight.setId(resultSet.getLong("id"));
                String role = resultSet.getString("role");
                if (role.equals("admin")) {
                    userFlight.setRole(User.ROLE.ADMIN);
                } else {
                    userFlight.setRole(User.ROLE.USER);
                }
            }

            statement.close();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return userFlight;
    }

    public ArrayList<Flight> readFromCSV(String path) {
        FileReader fileIn = null;
        StringBuilder data = new StringBuilder();

        try {
            fileIn = new FileReader(path);
            int a;
            while ((a = fileIn.read()) != -1) {
                data.append((char) a);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (fileIn != null) {
                try {
                    fileIn.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return decodeData(data.toString());
    }

    public ArrayList<Flight> decodeData(String data) {
        // конвертирование строки в коллекцию рейсов
        ArrayList<Flight> flights = new ArrayList<>();
        String[] rawFlights;
        String[] infoFlight;

        rawFlights = data.split("\n");
        for (String s : rawFlights) {
            infoFlight = s.split(",");
            infoFlight[infoFlight.length - 1] = infoFlight[infoFlight.length - 1].substring(0, 2);
            String flightNumber = infoFlight[0];
            String cityFrom = infoFlight[1];
            String cityTo = infoFlight[2];
            String timeFrom = infoFlight[3];
            String timeTo = infoFlight[4];
            double price = new Double(infoFlight[5]);
            int passengersCount = Integer.parseInt(infoFlight[6]);

            Flight newFlight = new Flight(flightNumber, cityFrom, cityTo, timeFrom, timeTo, price, passengersCount);


            ArrayList<String> days = new ArrayList<>(Arrays.asList(infoFlight).subList(7, infoFlight.length));
            newFlight.setDays(days);

            flights.add(newFlight);
        }
        return flights;
    }

    public void saveFlightToDB(Flight flight) {
        try {
            Connection connection = DriverManager.getConnection(url, user, password);
            Statement statement = connection.createStatement();
            for (int i = 0; i <= 30; i += 1) {
                calendar.add(Calendar.DAY_OF_YEAR, i);
                Date date = calendar.getTime();
                String dateWD = new SimpleDateFormat("EE").format(date);
                String dateFormat = new SimpleDateFormat("yyyy-MM-dd").format(date);
                for (String day : flight.getDaysArrayList()) {
                    if (day.equals(dateWD)) {
                        // проверяем, есть ли рейс в бд
                        boolean isFlightExist = false;
                        String query = "SELECT * FROM `flights` WHERE (`date` = '" + dateFormat + "') AND (`flight_number` = '" + flight.getFlightNumber() + "');";
                        ResultSet resultSet = statement.executeQuery(query);
                        while (resultSet.next()) {
                            isFlightExist = true;
                        }
                        if (!isFlightExist) {
                            // добавляем рейс в бд
                            query = "INSERT INTO `group_project`.`flights` (`date`, `flight_number`, `city_from`, `city_to`, `time_from`, `time_to`, `price`, `passengers_available`) VALUES ('" + dateFormat + "', '" + flight.getFlightNumber() + "', '" + flight.getCityFrom() + "', '" + flight.getCityTo() + "', '" + flight.getTimeFrom() + "', '" + flight.getTimeTo() + "', '" + flight.getPrice() + "', '" + flight.getPassengersCount() + "');";
                            statement.executeUpdate(query);
                        }
                    }
                }
                calendar = Calendar.getInstance();
            }

            statement.close();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void savePassengerToDB(Passenger passenger, Long orderId) {
        try {
            Connection connection = DriverManager.getConnection(url, user, password);
            Statement statement = connection.createStatement();

            String query = "INSERT INTO `group_project`.`passengers` (`order_id`, `last_name`, `first_name`, `passport`, `birthday_date`, `luggage`, `insurance`, `autoregistration`) " +
                    "VALUES ('" + orderId + "', '" + passenger.getLastName() + "', '" + passenger.getFirstName() + "', '" + passenger.getPassport() + "', '" + passenger.getBirthdayDate() + "', '" + passenger.getLuggage() + "', '" + passenger.getInsurance() + "', '" + passenger.getAutoregistration() + "');";
            statement.executeUpdate(query);

            statement.close();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Flight findFlight(Long flightId) {
        Flight flight = null;
        try {
            Connection connection = DriverManager.getConnection(url, user, password);
            Statement statement = connection.createStatement();

            String query = "SELECT * FROM `flights` WHERE (`id` = '" + flightId + "');";
            ResultSet resultSet = statement.executeQuery(query);
            while (resultSet.next()) {
                flight = new Flight(resultSet.getString("flight_number"), resultSet.getString("city_from"),
                        resultSet.getString("city_to"), resultSet.getString("time_from"),
                        resultSet.getString("time_to"), resultSet.getDouble("price"),
                        resultSet.getInt("passengers_available"));
                flight.setId(flightId);
            }

            statement.close();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return flight;
    }

    public ArrayList<Flight> findFlightsInDB(String cityFrom, String cityTo, String date, int passengersCount) {
        ArrayList<Flight> flights = new ArrayList<>();
        try {
            Connection connection = DriverManager.getConnection(url, user, password);
            Statement statement = connection.createStatement();

            String query = "SELECT * FROM `flights` WHERE (`date` = '" + date + "') AND (`city_from` = '" + cityFrom + "') AND (`city_to` = '" + cityTo + "');";
            System.out.println(query);
            ResultSet resultSet = statement.executeQuery(query);
            while (resultSet.next()) {
                if (resultSet.getInt("passengers_available") >= passengersCount) {
                    Flight flight = new Flight(resultSet.getString("flight_number"), resultSet.getString("city_from"),
                            resultSet.getString("city_to"), resultSet.getString("time_from"),
                            resultSet.getString("time_to"), resultSet.getDouble("price"),
                            resultSet.getInt("passengers_available"));
                    flight.setId(resultSet.getLong("id"));
                    addSort(flight, flights);
                }
            }

            statement.close();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return flights;
    }

    private void addSort(Flight newFlight, ArrayList<Flight> flights) {
        if (flights.isEmpty()) {
            flights.add(newFlight);
        } else {
            for (Flight existedFlight : flights) {
                if (existedFlight.getPrice() > newFlight.getPrice()) {
                    int k = flights.indexOf(existedFlight);
                    Flight flightSave;
                    Flight savedFlightSave = existedFlight;
                    flights.set(k, newFlight);
                    for (int j = k + 1; j < flights.size(); j++) {
                        flightSave = flights.get(j);
                        flights.set(j, savedFlightSave);
                        savedFlightSave = flightSave;
                    }
                    flights.add(savedFlightSave);
                    break;
                }
            }
        }
    }

    public Long buy(Long userId, Long flightId, int passengersCount) {
        Long orderId = null;

        try {
            Connection connection = DriverManager.getConnection(url, user, password);
            Statement statement = connection.createStatement();

            String query = "INSERT INTO `group_project`.`orders` (`user_id`, `flight_id`, `passengers_count`, `total_price`) VALUES ('" + userId + "', '" + flightId + "', '" + passengersCount + "', '" + 0 + "');";
            statement.executeUpdate(query);
            query = "SELECT * FROM `group_project`.`orders` WHERE (`user_id` = '" + userId + "') AND (`flight_id` = '" + flightId + "') AND (`passengers_count` = '" + passengersCount + "');";
            ResultSet resultSet = statement.executeQuery(query);
            while (resultSet.next()) {
                orderId = resultSet.getLong("id");
            }

            int passengersAvailable = 0;
            query = "SELECT * FROM `group_project`.`flights` WHERE (`id` = '" + flightId + "');";
            resultSet = statement.executeQuery(query);
            while (resultSet.next()) {
                passengersAvailable = resultSet.getInt("passengers_available") - passengersCount;
            }

            query = "UPDATE `group_project`.`flights` SET `passengers_available` = '" + passengersAvailable + "' WHERE (`id` = '" + flightId + "');";
            statement.executeUpdate(query);

            statement.close();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return orderId;
    }

    public void updateOrderPrice(Long orderId, double totalPrice) {
        try {
            Connection connection = DriverManager.getConnection(url, user, password);
            Statement statement = connection.createStatement();

            String query = "UPDATE `group_project`.`orders` SET `total_price` = '" + totalPrice + "' WHERE (`id` = '" + orderId + "');";
            statement.executeUpdate(query);

            statement.close();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Map<String, Object> getOrderInfo(Long orderId) {
        HashMap<String, Object> info = new HashMap<>();

        try {
            Connection connection = DriverManager.getConnection(url, user, password);
            Statement statement = connection.createStatement();

            String query = "SELECT * FROM `group_project`.`orders` WHERE (`id` = '" + orderId + "');";
            ResultSet resultSet = statement.executeQuery(query);
            long flightId = 0;
            double totalPrice = 0;
            while (resultSet.next()) {
                flightId = resultSet.getLong("flight_id");
            }

            query = "SELECT * FROM `group_project`.`flights` WHERE (`id` = '" + flightId + "');";
            resultSet = statement.executeQuery(query);
            Flight flight = new Flight();
            while (resultSet.next()) {
                flight.setId(flightId);
                flight.setFlightNumber(resultSet.getString("flight_number"));
                flight.setCityFrom(resultSet.getString("city_from"));
                flight.setCityTo(resultSet.getString("city_to"));
                flight.setTimeFrom(resultSet.getString("time_from"));
                flight.setTimeTo(resultSet.getString("time_to"));
                flight.setPrice(resultSet.getDouble("price"));
                flight.setPassengersCount(resultSet.getInt("passengers_available"));
            }
            info.put("flight", flight);

            ArrayList<Passenger> passengers = new ArrayList<>();
            Price price = new Price(flight.getPrice(), 2500.0, 600.0, 50.0);
            query = "SELECT * FROM `group_project`.`passengers` WHERE (`order_id` = '" + orderId + "');";
            resultSet = statement.executeQuery(query);
            while (resultSet.next()) {
                Passenger passenger = new Passenger(resultSet.getString("last_name"),
                        resultSet.getString("first_name"), resultSet.getString("passport"),
                        resultSet.getString("birthday_date"), resultSet.getString("luggage"),
                        resultSet.getString("insurance"), resultSet.getString("autoregistration"));
                passenger.setId(resultSet.getLong("id"));
                passengers.add(passenger);

                price.addTicketsCount();
                price.addLuggageCount(passenger.getLuggage());
                price.addInsuranceCount(passenger.getInsurance());
                price.addAutoregistrationCount(passenger.getAutoregistration());
            }
            price.setTicketCount(passengers.size());

            info.put("passengers", passengers);

            info.put("price", price);

            statement.close();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return info;
    }

    public Long getUserId(Long orderId) {
        Long userId = null;

        try {
            Connection connection = DriverManager.getConnection(url, user, password);
            Statement statement = connection.createStatement();

            String query = "SELECT * FROM `group_project`.`orders` WHERE (`id` = '" + orderId + "');";
            ResultSet resultSet = statement.executeQuery(query);
            while (resultSet.next()) {
                userId = resultSet.getLong("user_id");
            }

            statement.close();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return userId;
    }
}
