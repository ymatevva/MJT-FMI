package bg.sofia.uni.fmi.mjt.vehiclerent.vehicle;

import bg.sofia.uni.fmi.mjt.vehiclerent.driver.Driver;

import bg.sofia.uni.fmi.mjt.vehiclerent.exception.InvalidRentingPeriodException;
import bg.sofia.uni.fmi.mjt.vehiclerent.exception.VehicleAlreadyRentedException;
import bg.sofia.uni.fmi.mjt.vehiclerent.exception.VehicleNotRentedException;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;


public abstract sealed class Vehicle permits Car, Bicycle, Caravan{
    
    private String id;
    private String model;
    private Driver driver;
    private LocalDateTime startRentTime;
    private LocalDateTime endRentTime;
    private boolean isRented;
    protected final int DAYS_IN_WEEK = 7;
    protected final int HOURS_IN_DAY = 24;


    public Vehicle(String id, String model) {
        this.id = id;
        this.model = model;
        setRented(false);
    }
    
    public LocalDateTime getStartRentTime() {
        return startRentTime;
    }

    public LocalDateTime getEndRentTime() {
        return endRentTime;
    }

    public boolean isRented() {
        return isRented;
    }

    public void setRented(boolean isRented) {
        this.isRented = isRented;
    }

    /* 
     * @throws VehicleAlreadyRentedException in case the vehicle is already rented by someone else or by the same driver. // runtime
     */

    public void rent(Driver driver, LocalDateTime startRentTime) {

        if(isRented) {
            throw new VehicleAlreadyRentedException("The vehicle is already rented.");
        }

       this.driver = driver;
       this.startRentTime = startRentTime;
        setRented(true);
    }

    /**
    
     * @throws IllegalArgumentException in case @rentalEnd is null
     * @throws VehicleNotRentedException in case the vehicle is not rented at all
     * @throws InvalidRentingPeriodException in case the rentalEnd is before the currently noted start date of rental or
     * in case the Vehicle does not allow the passed period for rental, e.g. Caravans must be rented for at least a day
     * and the driver tries to return them after an hour. // CHECKED
     */
    public void returnBack(LocalDateTime rentalEnd) throws InvalidRentingPeriodException {
        if(!isRented){
            throw new VehicleNotRentedException("The vehicle is not rented");
        }

        if(rentalEnd == null){
            throw new IllegalArgumentException();
        }
 
        if(rentalEnd.isBefore(startRentTime)){
            throw new InvalidRentingPeriodException("The rental end is before the rental start.");
        }
        
        this.endRentTime = rentalEnd;
        calculateRentalPrice(rentalEnd, rentalEnd);
         setRented(false);
    }

protected double[] periodsRented(double pricePerHour, double pricePerDay,  double pricePerWeek){

    int weeksRented = (int)ChronoUnit.WEEKS.between(startRentTime, endRentTime);
    int daysRented = (int)ChronoUnit.DAYS.between(startRentTime, endRentTime) - weeksRented * DAYS_IN_WEEK;
    int hoursRented = (int)Duration.between(startRentTime, endRentTime).toHours() % HOURS_IN_DAY;

    return new double[]{hoursRented,daysRented,weeksRented};
}
    /**
     * Used to calculate potential rental price without the vehicle to be rented.
     * The calculation is based on the type of the Vehicle (Car/Caravan/Bicycle).
     *
     * @param startOfRent the beginning of the rental
     * @param endOfRent the end of the rental
     * @return potential price for rent
     * @throws InvalidRentingPeriodException in case the vehicle cannot be rented for that period of time or 
     * the period is not valid (end date is before start date)
     */
    public abstract double calculateRentalPrice(LocalDateTime startOfRent, LocalDateTime endOfRent) throws InvalidRentingPeriodException;

}
