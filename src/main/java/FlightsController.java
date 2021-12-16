import model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;


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
	public ModelAndView download(@RequestParam("path") String path) {
		System.out.println(path);
		this.flightRepository.downloadData(path);
		Iterable<Flight> flights = this.flightRepository.findAllFlights();
		return new ModelAndView("admin", "flights", flights);
	}

	@RequestMapping(value = "flights", method = RequestMethod.GET)
	public ModelAndView flights(@ModelAttribute Search search) {
		Iterable<Flight> flights = search.getResult();
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

	@RequestMapping(value = "login", params = "sing_in", method = RequestMethod.POST)
	public ModelAndView sing_in(@Valid Search search, BindingResult result,final HttpServletRequest req,
								final HttpServletResponse res,
								final User.ROLE role) {
		if (role.equals(User.ROLE.ADMIN)) {
			return new ModelAndView("admin", "admin", admin());
		}
		Iterable<Flight> flights = search.getInputResult();
		return new ModelAndView("flights", "flights", flights);
	}

	@RequestMapping(value = "order", params = "flight", method = RequestMethod.POST)
	public ModelAndView book(@RequestParam("flight") Long flightId, @RequestParam("count") int passengersCount,
							@Valid Search search, BindingResult result, RedirectAttributes redirect) {
		ModelAndView modelAndView = new ModelAndView();

		Flight flight = flightService.findFlight(flightId);
		modelAndView.addObject("flight", flight);

		modelAndView.addObject("flightId", flightId);
		modelAndView.addObject("count", passengersCount);
		modelAndView.addObject("insurance", "false");
		modelAndView.addObject("autoregistration", "false");

		modelAndView.addObject("today", Calendar.getInstance().getTime());

		modelAndView.setViewName("book");
		return modelAndView;
	}



	@RequestMapping(value = "order", params = "buy", method = RequestMethod.POST)
	public ModelAndView example(@RequestParam Map allParams) {
		System.out.println(allParams);
		int count = Integer.parseInt((String) allParams.get("count"));
		Long flightId = Long.parseLong((String) allParams.get("buy"));
		Long orderId = flightService.buy(2L, flightId, count);
		for (int i = 1; i <= count; i++) {
			Passenger passenger = new Passenger(String.valueOf(allParams.get("lastName-" + i)),
					String.valueOf(allParams.get("firstName-" + i)), String.valueOf(allParams.get("passport-" + i)),
					String.valueOf(allParams.get("birthdayDate-" + i)), String.valueOf(allParams.get("luggage-" + i)),
					String.valueOf(allParams.get("insurance")), String.valueOf(allParams.get("autoregistration")));
			flightService.savePassengerToDB(passenger, orderId);
		}
		return new ModelAndView("login");
	}

}
