import lombok.Data;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicLong;

@Data
public class InMemoryFlightRepository {
    private static final AtomicLong counterFlights = new AtomicLong();

    private final ConcurrentMap<Long, Flight> flights = new ConcurrentHashMap<Long, Flight>();

    private static InMemoryFlightRepository instance;

    private Long userId = 2L;

    public static synchronized InMemoryFlightRepository getInstance() {
        if (instance == null) {
            instance = new InMemoryFlightRepository();
        }
        return instance;
    }

    public Iterable<Flight> findAllFlights() {
        return this.flights.values();
    }

    public Flight findFlight(Long id) {
        System.out.println(this.flights.get(id).getFlightNumber());
        return this.flights.get(id);
    }

    public Flight save(Flight flight) {
        Long flightId = flight.getId();
        if (flightId == null) {
            flightId = counterFlights.incrementAndGet();
            flight.setId(flightId);
        } else {
            if (counterFlights.longValue() < flightId) {
                counterFlights.set(flightId);
            }
        }
        boolean isFlightSaved = false;
        for (Flight savedFlight : this.flights.values()) {
            if (savedFlight.getFlightNumber().equals(flight.getFlightNumber())) {
                isFlightSaved = true;
                break;
            }
        }
        if (!isFlightSaved) {
            this.flights.put(flightId, flight);
        }
        FlightService.getInstance().saveToDB(flight);
        return flight;
    }


    public void downloadData(String path) {
        for (Flight flight : FlightService.getInstance().readFromCSV(path)) {
            save(flight);
        }
    }
}
