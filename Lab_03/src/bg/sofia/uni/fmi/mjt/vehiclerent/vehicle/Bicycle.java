package bg.sofia.uni.fmi.mjt.vehiclerent.vehicle;


import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

import bg.sofia.uni.fmi.mjt.vehiclerent.exception.InvalidRentingPeriodException;

final public class Bicycle extends Vehicle{

    private double pricePerDay;
    private double pricePerHour;

    public Bicycle(String id, String model, double pricePerDay, double pricePerHour){
         super(id, model);
         this.pricePerDay = pricePerDay;
         this.pricePerHour = pricePerHour;
    }

    
    public double calculateTax(double pricePerHour, double pricePerDay, double pricePerWeek ) {
    
           int daysRented = (int)ChronoUnit.DAYS.between(super.getStartRentTime(), super.getEndRentTime());
           int hoursRented =(int)ChronoUnit.HOURS.between(super.getStartRentTime(), super.getEndRentTime()) % 24;

           if(daysRented ==  0) {
             return hoursRented * pricePerHour;
           }
            else {
            return daysRented * pricePerDay;
           }
    }
  

    @Override
    public double calculateRentalPrice(LocalDateTime startOfRent, LocalDateTime endOfRent) throws InvalidRentingPeriodException {
        
        if(endOfRent.isBefore(startOfRent)){
            throw new InvalidRentingPeriodException("The end date is before the start date.");
        }

        if(ChronoUnit.DAYS.between(startOfRent, endOfRent) > 7){
            throw new InvalidRentingPeriodException("The renting period is above one week.");
        }

       return calculateTax(pricePerHour, pricePerDay, 0.0);
    }
}
