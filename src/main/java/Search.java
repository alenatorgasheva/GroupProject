import lombok.Data;
import org.hibernate.validator.constraints.NotEmpty;

import java.util.ArrayList;

@Data
public class Search {

    @NotEmpty(message = "Enter the city you want to fly from.")
    private String cityFrom;

    @NotEmpty(message = "Enter the city you want to fly to.")
    private String cityTo;

    @NotEmpty(message = "Choose the date.")
    private String date;

    private int passengersCount = 1;

    private ArrayList<Flight> result;

    public ArrayList<Flight> getInputResult() {
        result = FlightService.getInstance().findFlightsInDB(cityFrom, cityTo, date, passengersCount);
        return result;
    }
}
