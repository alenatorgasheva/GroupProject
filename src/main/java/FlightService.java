import java.io.FileReader;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.text.SimpleDateFormat;
import java.util.Date;

public class FlightService {

    private static FlightService instance;

    private Calendar calendar = Calendar.getInstance();

    public static synchronized FlightService getInstance() {
        if (instance == null) {
            instance = new FlightService();
        }
        return instance;
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
        ArrayList<Flight> flights = new ArrayList<Flight>();
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


            ArrayList<String> days = new ArrayList<String>(Arrays.asList(infoFlight).subList(7, infoFlight.length));
            newFlight.setDays(days);

            flights.add(newFlight);
        }
        return flights;
    }

    public void saveToDB(Flight flight) {
        String url = "jdbc:mysql://localhost:3306/group_project";
        String user = "root";
        String password = "password";

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
                            query = "INSERT INTO `group_project`.`flights` (`date`, `flight_number`, `city_from`, `city_to`, `time_from`, `time_to`, `price`, `passengers_count`) VALUES ('" + dateFormat + "', '" + flight.getFlightNumber() + "', '" + flight.getCityFrom() + "', '" + flight.getCityTo() + "', '" + flight.getTimeFrom() + "', '" + flight.getTimeTo() + "', '" + flight.getPrice() + "', '" + flight.getPassengersCount() + "');";
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
}
