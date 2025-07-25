package bg.sofia.uni.fmi.mjt.vehiclerent.vehicle;


import java.time.LocalDateTime;

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

    public double calculateTax(double pricePerHour, double pricePerDay, double pricePerWeek){

        double totalTax =0 ;
        double[] rentalPeriods = super.periodsRented(pricePerHour, pricePerDay, pricePerWeek);
        totalTax += PRICE_PER_SEAT * numberOfSeats;
        totalTax += fuelType.getTax() * (rentalPeriods[1] + rentalPeriods[2] * DAYS_IN_WEEK);
        return totalTax;
    }

    @Override
    public double calculateRentalPrice(LocalDateTime startOfRent, LocalDateTime endOfRent) throws InvalidRentingPeriodException {
        
        if(endOfRent.isBefore(startOfRent)){
            throw new InvalidRentingPeriodException("The end date is before the start date.");
        }

        return calculateTax(pricePerHour,pricePerDay,pricePerWeek);
    }

}
