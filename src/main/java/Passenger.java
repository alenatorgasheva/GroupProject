import lombok.Data;

@Data
public class Passenger {
    private Long id;

    private String lastName = "";

    private String firstName = "";

    private String passport = "";

    private String birthdayDate = "";

    private int luggage = 0;

    private int insurance = 0;

    private int autoregistration = 0;

    public Passenger() {}

    public Passenger(String lastName, String firstName, String passport, String birthdayDate,
                     String luggage, String insurance, String autoregistration) {
        this.lastName = lastName;
        this.firstName = firstName;
        this.passport = passport;
        this.birthdayDate = birthdayDate;
        if (luggage.equals("on")) {
            this.luggage = 1;
        } else {
            this.luggage = 0;
        }
        if (insurance.equals("on")) {
            this.insurance = 1;
        } else {
            this.insurance = 0;
        }
        if (autoregistration.equals("on")) {
            this.autoregistration = 1;
        } else {
            this.autoregistration = 0;
        }


    }
}
