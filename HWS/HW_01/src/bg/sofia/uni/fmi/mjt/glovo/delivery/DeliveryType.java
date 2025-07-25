package bg.sofia.uni.fmi.mjt.glovo.delivery;

public enum DeliveryType {

    CAR(5, 3),
    BIKE(3, 5);

    public final int pricePerKm;
    public final int timePerKm;

    DeliveryType(int pricePerKilometer, int timePerKilometer) {
        this.pricePerKm = pricePerKilometer;
        this.timePerKm = timePerKilometer;
    }

    public int getPricePerKm() {
        return pricePerKm;
    }

    public int getTimePerKm() {
        return timePerKm;
    }
}
