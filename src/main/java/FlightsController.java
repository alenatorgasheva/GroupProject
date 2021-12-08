import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;


@Controller
@RequestMapping("/")
public class FlightsController {

	private final InMemoryFlightRepository flightRepository;

	private final FlightService flightService;

	@Autowired
	public FlightsController(InMemoryFlightRepository flightRepository) {
		this.flightRepository = flightRepository;
		this.flightService = FlightService.getInstance();
	}

	@RequestMapping("login")
	public ModelAndView login() {
		return new ModelAndView("login");
	}

	@RequestMapping("admin")
	public ModelAndView admin() {
		Iterable<Flight> flights = this.flightRepository.findAllFlights();
		return new ModelAndView("admin", "flights", flights);
	}

	@RequestMapping(value = "admin", method = RequestMethod.POST)
	public ModelAndView download() {
		String path = "src/main/resources/flights.csv";
		this.flightRepository.downloadData(path);
		Iterable<Flight> flights = this.flightRepository.findAllFlights();
		return new ModelAndView("admin", "flights", flights);
	}

	@RequestMapping(value = "flights", method = RequestMethod.GET)
	public ModelAndView flights(@ModelAttribute Search search) {
		Iterable<Flight> flights = search.getResult();
		System.out.println(flights);
		return new ModelAndView("flights", "flights", flights);
	}

	@RequestMapping(value = "flights", params = "search", method = RequestMethod.POST)
	public ModelAndView find(@Valid Search search, BindingResult result) {
		if (result.hasErrors()) {
			return new ModelAndView("flights", "flightsErrors", result.getAllErrors());
		}
		Iterable<Flight> flights = search.getInputResult();
		return new ModelAndView("flights", "flights", flights);
	}

	@RequestMapping(value = "flights", params = "buy", method = RequestMethod.POST)
	public ModelAndView buy(@RequestParam("buy") Long flightId, @RequestParam("passengers") int passengersCount,
							@Valid Search search, BindingResult result, RedirectAttributes redirect) {
		flightService.buy(flightRepository.getUserId(), flightId, passengersCount);
		Iterable<Flight> flights = search.getInputResult();
		return new ModelAndView("redirect:/flights/", "flights", flights);
	}

}
