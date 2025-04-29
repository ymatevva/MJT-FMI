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

    private double calculateTax( boolean isPerHour) {
    
        if(isPerHour){
            int hoursRented = (int)ChronoUnit.HOURS.between(getStartRentTime(),getEndRentTime()) + 1;
            return hoursRented * pricePerHour;
        } else {
            return (double)ChronoUnit.DAYS.between(getStartRentTime(), getEndRentTime())*pricePerDay;
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

        if(ChronoUnit.DAYS.between(startOfRent, endOfRent) < 1){
            return calculateTax(true);
        } else{
            return calculateTax(false);
        }
    }

}
