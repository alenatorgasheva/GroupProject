import lombok.Data;
import java.util.ArrayList;

@Data
public class Flight {

    private Long id;

    private String flightNumber;

    private String cityFrom;

    private String cityTo;

    private String timeFrom;

    private String timeTo;

    private double price;

    private int passengersCount;

    private ArrayList<String> days = new ArrayList<String>();

    public Flight(String flightNumber, String cityFrom, String cityTo, String timeFrom, String timeTo, double price, int passengersCount) {
        this.flightNumber = flightNumber;
        this.cityFrom = cityFrom;
        this.cityTo = cityTo;
        this.timeFrom = timeFrom;
        this.timeTo = timeTo;
        this.passengersCount = passengersCount;
        this.price = price;
    }

}
