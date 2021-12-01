import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;


@Controller
@RequestMapping("/")
public class FlightsController {
	private final InMemoryFlightRepository flightRepository;

	@Autowired
	public FlightsController(InMemoryFlightRepository flightRepository) {
		this.flightRepository = flightRepository;
	}

	@RequestMapping(params = "flights")
	public ModelAndView list() {
		String path = "src/main/resources/flights.csv";
		Iterable<Flight> flights = this.flightRepository.findAllFlights(path);
		return new ModelAndView("admin", "flights", flights);
	}


	@RequestMapping("foo")
	public String foo() {
		throw new RuntimeException("Expected exception in controller");
	}

}
