package bg.sofia.uni.fmi.mjt.vehiclerent.vehicle;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

import bg.sofia.uni.fmi.mjt.vehiclerent.exception.InvalidRentingPeriodException;

final public class Caravan extends Vehicle{

    private double pricePerDay;
    private double pricePerHour;
    private double pricePerWeek;
    private FuelType fuelType;
    private int numberOfBeds;
    private int numberOfSeats;
    final private int PRICE_PER_SEAT = 5;
    final private int PRICE_PER_BED = 10;


    public Caravan(String id, String model, FuelType fuelType, int numberOfSeats, int numberOfBeds, double pricePerWeek, double pricePerDay, double pricePerHour) {
        super(id,model);
        this.fuelType = fuelType;
        this.numberOfBeds = numberOfBeds;
        this.numberOfSeats = numberOfSeats;
        this.pricePerDay = pricePerDay;
        this.pricePerWeek = pricePerWeek;
        this.pricePerHour = pricePerHour;

    }

    private double calculateTax(LocalDateTime startOfRent, LocalDateTime endOfRent){

        double totalTax = 0;

        int weeksRented = (int)ChronoUnit.WEEKS.between(startOfRent, endOfRent);
        int daysRented = (int)ChronoUnit.DAYS.between(startOfRent, endOfRent) - weeksRented * DAYS_IN_WEEK;
        int hoursRented = (int)Duration.between(startOfRent, endOfRent).toHours() % HOURS_IN_DAY;

        totalTax += weeksRented * pricePerWeek + daysRented * pricePerDay + hoursRented*pricePerHour;
        totalTax +=  daysRented * fuelType.getTax();
        totalTax+= PRICE_PER_BED * numberOfBeds + PRICE_PER_SEAT*numberOfSeats;

        return totalTax;
    }


    @Override
    public double calculateRentalPrice(LocalDateTime startOfRent, LocalDateTime endOfRent) throws InvalidRentingPeriodException {
        
        if(endOfRent.isBefore(startOfRent)){
            throw new InvalidRentingPeriodException("The end date of rent is before the start date of rent");
        }

        if(ChronoUnit.DAYS.between(startOfRent,endOfRent) < 1){
            throw new InvalidRentingPeriodException("The caravan is rented for less than a day.");
        }

        return calculateTax(startOfRent,endOfRent);
    }

}
