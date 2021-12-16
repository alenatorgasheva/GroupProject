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
import java.util.Map;


@Controller
@RequestMapping("/")
public class FlightsController {

	private InMemoryFlightRepository flightRepository;

	private final FlightService flightService;

	@Autowired
	public FlightsController(InMemoryFlightRepository flightRepository) {
		this.flightRepository = flightRepository;
		this.flightService = FlightService.getInstance();
	}

	@RequestMapping()
	public ModelAndView start() {
		return new ModelAndView("login");
	}

	@RequestMapping("login")
	public ModelAndView login() {
		return new ModelAndView("login");
	}

	@RequestMapping(value = "sign-in", method = RequestMethod.GET)
	public ModelAndView singIn(@RequestParam Map allParams, @ModelAttribute Search search) {
		ModelAndView modelAndView = new ModelAndView();

		User user = new User(String.valueOf(allParams.get("email")), String.valueOf(allParams.get("password")));
		user = flightService.findUser(user);

		if (user.getRole() != null) {
			flightRepository.saveUser(user);

			if (user.getRole().equals(User.ROLE.ADMIN)) {
				Iterable<Flight> flights = this.flightRepository.findAllFlights();
				modelAndView.setViewName("admin");
				modelAndView.addObject("flights", flights);
			} else {
				Iterable<Flight> flights = search.getResult();
				modelAndView.setViewName("flights");
				modelAndView.addObject("flights", flights);
				modelAndView.addObject("email", user.getLogin());
			}
		} else {
			modelAndView.setViewName("login");
		}
		return modelAndView;
	}

	@RequestMapping(value = "sign-out")
	public ModelAndView singOut() {
		flightRepository = flightRepository.removeAll();
		return new ModelAndView("login");
	}

	@RequestMapping("admin")
	public ModelAndView admin(@ModelAttribute Search search) {
		ModelAndView modelAndView = new ModelAndView();

		if (flightRepository.getUser().getRole() != null) {
			if (flightRepository.getUser().getRole().equals(User.ROLE.ADMIN)) {
				Iterable<Flight> flights = this.flightRepository.findAllFlights();
				modelAndView.setViewName("admin");
				modelAndView.addObject("flights", flights);
			} else {
				Iterable<Flight> flights = search.getResult();
				modelAndView.setViewName("flights");
				modelAndView.addObject("flights", flights);
				modelAndView.addObject("email", flightRepository.getUser().getLogin());
			}
		} else {
			modelAndView.setViewName("login");
		}
		return modelAndView;
	}

	@RequestMapping(value = "admin", method = RequestMethod.POST)
	public ModelAndView download(@RequestParam("path") String path, @ModelAttribute Search search) {
		System.out.println(path);
		ModelAndView modelAndView = new ModelAndView();

		if (flightRepository.getUser().getRole() != null) {
			if (flightRepository.getUser().getRole().equals(User.ROLE.ADMIN)) {
				this.flightRepository.downloadData(path);
				Iterable<Flight> flights = this.flightRepository.findAllFlights();
				modelAndView.setViewName("admin");
				modelAndView.addObject("flights", flights);
			} else {
				Iterable<Flight> flights = search.getResult();
				modelAndView.setViewName("flights");
				modelAndView.addObject("flights", flights);
				modelAndView.addObject("email", flightRepository.getUser().getLogin());
			}
		} else {
			modelAndView.setViewName("login");
		}
		return modelAndView;
	}

	@RequestMapping(value = "flights", method = RequestMethod.GET)
	public ModelAndView flights(@ModelAttribute Search search) {
		ModelAndView modelAndView = new ModelAndView();

		if (flightRepository.getUser().getRole() != null) {
			Iterable<Flight> flights = search.getResult();
			modelAndView.setViewName("flights");
			modelAndView.addObject("flights", flights);

			if (flightRepository.getUser().getRole().equals(User.ROLE.ADMIN)) {
				modelAndView.addObject("email", "admin");
			} else {
				modelAndView.addObject("email", flightRepository.getUser().getLogin());
			}
		} else {
			modelAndView.setViewName("login");
		}
		return modelAndView;
	}

	@RequestMapping(value = "flights", params = "search", method = RequestMethod.POST)
	public ModelAndView find(@Valid Search search, BindingResult result) {
		ModelAndView modelAndView = new ModelAndView();

		if (flightRepository.getUser().getRole() != null) {
			if (result.hasErrors()) {
				modelAndView.addObject("flightsErrors", result.getAllErrors());
			} else {
				Iterable<Flight> flights = search.getInputResult();
				modelAndView.setViewName("flights");
				modelAndView.addObject("flights", flights);

				if (flightRepository.getUser().getRole().equals(User.ROLE.ADMIN)) {
					modelAndView.addObject("email", "admin");
				} else {
					modelAndView.addObject("email", flightRepository.getUser().getLogin());
				}
			}
		} else {
			modelAndView.setViewName("login");
		}
		return modelAndView;
	}

	@RequestMapping(value = "order", params = "flight", method = RequestMethod.POST)
	public ModelAndView book(@RequestParam("flight") Long flightId, @RequestParam("count") int passengersCount,
							@Valid Search search, BindingResult result, RedirectAttributes redirect) {
		ModelAndView modelAndView = new ModelAndView();
		if (flightRepository.getUser().getRole() != null) {
			Flight flight = flightService.findFlight(flightId);
			modelAndView.addObject("flight", flight);

			modelAndView.addObject("flightId", flightId);
			modelAndView.addObject("count", passengersCount);
			modelAndView.addObject("insurance", null);
			modelAndView.addObject("autoregistration", null);

			modelAndView.setViewName("book");

			if (flightRepository.getUser().getRole().equals(User.ROLE.ADMIN)) {
				modelAndView.addObject("email", "admin");
			} else {
				modelAndView.addObject("email", flightRepository.getUser().getLogin());
			}
		} else {
			modelAndView.setViewName("login");
		}
		return modelAndView;
	}

	@RequestMapping(value = "order", params = "buy", method = RequestMethod.POST)
	public ModelAndView example(@RequestParam Map allParams, @ModelAttribute Search search) {
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

		ModelAndView modelAndView = new ModelAndView();

		if (flightRepository.getUser().getRole() != null) {
			Iterable<Flight> flights = search.getResult();
			modelAndView.setViewName("flights");
			modelAndView.addObject("flights", flights);

			if (flightRepository.getUser().getRole().equals(User.ROLE.ADMIN)) {
				modelAndView.addObject("email", "admin");
			} else {
				modelAndView.addObject("email", flightRepository.getUser().getLogin());
			}
		} else {
			modelAndView.setViewName("login");
		}
		return modelAndView;
	}
}
