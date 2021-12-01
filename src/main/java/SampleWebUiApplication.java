import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;

@Configuration
@EnableAutoConfiguration
@ComponentScan
public class SampleWebUiApplication {

	@Bean
	public InMemoryFlightRepository flightRepository() {
		return new InMemoryFlightRepository();
	}

	@Bean
	public Converter<String, Flight> flightConverter() {
		return new Converter<String, Flight>() {
			@Override
			public Flight convert(String id) {
				return flightRepository().findFlight(Long.valueOf(id));
			}
		};
	}

	public static void main(String[] args) {
		SpringApplication.run(SampleWebUiApplication.class, args);
	}

}
