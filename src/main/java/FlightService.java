
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

public class FlightService {

    private static FlightService instance;

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
        } catch (FileNotFoundException e) {
            e.printStackTrace();
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
}
