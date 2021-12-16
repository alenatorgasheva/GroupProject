import lombok.Data;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicLong;

@Data
public class InMemoryFlightRepository {
    private static final AtomicLong counterFlights = new AtomicLong();

    private final ConcurrentMap<Long, Flight> flights = new ConcurrentHashMap<>();

    private static InMemoryFlightRepository instance;

    private final User user = new User();

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
        return this.flights.get(id);
    }

    public User getFlight() {
        return this.user;
    }

    public Flight saveFlight(Flight flight) {
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
        FlightService.getInstance().saveFlightToDB(flight);
        return flight;
    }

    public User saveUser(User newUser) {
        user.setId(newUser.getId());
        user.setLogin(newUser.getLogin());
        user.setPassword(newUser.getPassword());
        user.setRole(newUser.getRole());
        return this.user;
    }

    public void downloadData(String path) {
        for (Flight flight : FlightService.getInstance().readFromCSV(path)) {
            saveFlight(flight);
        }
    }

    public InMemoryFlightRepository removeAll() {
        instance = new InMemoryFlightRepository();
        return instance;
    }
}
