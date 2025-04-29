package bg.sofia.uni.fmi.mjt.vehiclerent.vehicle;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

import bg.sofia.uni.fmi.mjt.vehiclerent.exception.InvalidRentingPeriodException;

final public class Car extends Vehicle {

    private FuelType fuelType;
    private int numberOfSeats;
    private double pricePerWeek;
    private double pricePerDay;
    private double pricePerHour;
    private final int PRICE_PER_SEAT = 5;
   
    
    public Car(String id, String model, FuelType fuelType, int numberOfSeats, double pricePerWeek, double pricePerDay, double pricePerHour) {
     super(id,model);
     this.fuelType = fuelType;
     this.numberOfSeats = numberOfSeats;
     this.pricePerDay = pricePerDay;
     this.pricePerHour = pricePerHour;
     this.pricePerWeek = pricePerWeek;
    }

    private double calculateTax(LocalDateTime startOfRent, LocalDateTime endOfRent){

        double totalTax = 0;
        totalTax += PRICE_PER_SEAT * numberOfSeats;

        int weeksRented = (int)ChronoUnit.WEEKS.between(startOfRent, endOfRent);
        int daysRented = (int)ChronoUnit.DAYS.between(startOfRent, endOfRent) - weeksRented*DAYS_IN_WEEK;
        int hoursRented = (int)Duration.between(startOfRent, endOfRent).toHours() % HOURS_IN_DAY;

        totalTax += weeksRented*pricePerWeek + daysRented*pricePerDay + hoursRented*pricePerHour;
        totalTax += fuelType.getTax() * (daysRented + weeksRented*DAYS_IN_WEEK);
        return totalTax;
    }

    @Override
    public double calculateRentalPrice(LocalDateTime startOfRent, LocalDateTime endOfRent) throws InvalidRentingPeriodException {
        
        if(endOfRent.isBefore(startOfRent)){
            throw new InvalidRentingPeriodException("The end date is before the start date.");
        }

        return calculateTax(startOfRent,endOfRent);
    }

}
