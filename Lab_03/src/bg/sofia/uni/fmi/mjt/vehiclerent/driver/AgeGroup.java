package bg.sofia.uni.fmi.mjt.vehiclerent.driver;

public enum AgeGroup {
   JUNIOR(10),
   EXPERIENCED(0),
   SENIOR(15);

   final private int tax;

    AgeGroup(int tax){
    this.tax = tax; 
    }

   public int getTax() {
    return tax;
   }
}
