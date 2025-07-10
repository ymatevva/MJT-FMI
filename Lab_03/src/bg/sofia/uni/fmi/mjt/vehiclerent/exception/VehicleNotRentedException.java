package bg.sofia.uni.fmi.mjt.vehiclerent.exception;

public class VehicleNotRentedException extends RuntimeException{

    public VehicleNotRentedException(String message){
        super(message);
    }
    
    public VehicleNotRentedException(String message, Throwable e){
        super(message, e);
    }
}
