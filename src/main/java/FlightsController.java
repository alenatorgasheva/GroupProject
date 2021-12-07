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

	@RequestMapping("flights")
	public ModelAndView flights() {
		Iterable<Flight> flights = this.flightRepository.findAllFlights();
		return new ModelAndView("flights", "flights", flights);
	}

	@RequestMapping(value = "flights", params = "download")
	public ModelAndView flightsDownload() {
		String path = "src/main/resources/flights.csv";
		this.flightRepository.downloadData(path);
		Iterable<Flight> flights = this.flightRepository.findAllFlights();
		return new ModelAndView("flights", "flights", flights);
	}

	@RequestMapping("login")
	public ModelAndView login() {
		return new ModelAndView("login");
	}

	@RequestMapping("foo")
	public String foo() {
		throw new RuntimeException("Expected exception in controller");
	}

}
