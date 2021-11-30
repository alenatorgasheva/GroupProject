import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicLong;

public class InMemoryFlightRepository {
    private static AtomicLong counterFlights = new AtomicLong();

    private final ConcurrentMap<Long, Flight> flights = new ConcurrentHashMap<Long, Flight>();

    private static InMemoryFlightRepository instance;

    public static synchronized InMemoryFlightRepository getInstance() {
        if (instance == null) {
            instance = new InMemoryFlightRepository();
        }
        return instance;
    }

    public Iterable<Flight> findAllFlights(String path) {
        if (this.flights.isEmpty()) {
            for (Flight flight : FlightService.getInstance().readFromCSV(path)) {
                save(flight);
            }
        }
        return this.flights.values();

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
        this.flights.put(flightId, flight);
        return flight;
    }

}
