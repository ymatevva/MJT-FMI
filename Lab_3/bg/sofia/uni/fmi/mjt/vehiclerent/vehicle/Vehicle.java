package bg.sofia.uni.fmi.mjt.vehiclerent.vehicle;

import bg.sofia.uni.fmi.mjt.vehiclerent.driver.Driver;

import bg.sofia.uni.fmi.mjt.vehiclerent.exception.InvalidRentingPeriodException;
import bg.sofia.uni.fmi.mjt.vehiclerent.exception.VehicleAlreadyRentedException;
import bg.sofia.uni.fmi.mjt.vehiclerent.exception.VehicleNotRentedException;

import java.time.LocalDateTime;


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
        setId(id);
        setModel(model);
        setRented(false);
    }

    
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id == null || id.isBlank() ? "Unkown" : id;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model == null || model.isBlank() ? "Unkown" : model;
    }

    public Driver getDriver() {
        return driver;
    }

    public void setDriver(Driver driver) {
        this.driver = driver;
    }

    public LocalDateTime getStartRentTime() {
        return startRentTime;
    }

    public void setStartRentTime(LocalDateTime startRentTime) {
        this.startRentTime = startRentTime;
    }

    public LocalDateTime getEndRentTime() {
        return endRentTime;
    }

    public void setEndRentTime(LocalDateTime endRentTime) {
        this.endRentTime = endRentTime;
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

        setDriver(driver);
        setStartRentTime(startRentTime);
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
        
        setEndRentTime(rentalEnd);
        calculateRentalPrice(rentalEnd, rentalEnd);
         setRented(false);
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
