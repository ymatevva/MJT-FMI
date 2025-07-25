package bg.sofia.uni.fmi.mjt.vehiclerent.vehicle;

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

    public double calculateTax(double pricePerHour, double pricePerDay, double pricePerWeek){

        double totalTax = 0;
        double[] rentalPeriods = super.periodsRented(pricePerHour, pricePerDay, pricePerWeek);
        totalTax +=  rentalPeriods[1] * fuelType.getTax();
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

        return calculateTax(pricePerHour,pricePerDay,pricePerWeek);
    }

}
