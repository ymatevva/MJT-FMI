package bg.sofia.uni.fmi.mjt.vehiclerent.exception;

public class VehicleAlreadyRentedException extends RuntimeException{
    
    public VehicleAlreadyRentedException(String message){
        super(message);
    }
    
    public VehicleAlreadyRentedException(String message, Throwable e){
        super(message, e);
    }
}
