import lombok.Data;

@Data
public class Price {

    private double ticketPrice;
    private double ticket = 0;
    private int ticketCount = 0;

    private double luggagePrice;
    private double luggage = 0;
    private int luggageCount = 0;

    private double insurancePrice;
    private double insurance = 0;
    private int insuranceCount = 0;

    private double autoregistrationPrice;
    private double autoregistration = 0;
    private int autoregistrationCount = 0;

    private double total = 0;

    public Price(Double ticketsPrice, Double luggagePrice,
                      Double insurancePrice, Double autoregistrationPrice) {
        this.ticketPrice = ticketsPrice;
        this.luggagePrice = luggagePrice;
        this.insurancePrice = insurancePrice;
        this.autoregistrationPrice = autoregistrationPrice;
    }

    public void addTicketsCount() {
        ticketCount += 1;
        ticket += ticketPrice;
        total += ticketPrice;
    }

    public void addLuggageCount(int n) {
        luggageCount += n;
        luggage += luggagePrice * n;
        total += luggagePrice * n;
    }

    public void addInsuranceCount(int n) {
        insuranceCount += n;
        insurance = insurancePrice * n;
        total += insurancePrice * n;
    }

    public void addAutoregistrationCount(int n) {
        autoregistrationCount += n;
        autoregistration = autoregistrationPrice * n;
        total += autoregistrationPrice * n;
    }
}
