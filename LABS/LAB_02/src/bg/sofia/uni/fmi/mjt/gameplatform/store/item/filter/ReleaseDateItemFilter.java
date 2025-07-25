package bg.sofia.uni.fmi.mjt.gameplatform.store.item.filter;

import java.time.LocalDateTime;

import bg.sofia.uni.fmi.mjt.gameplatform.store.item.StoreItem;

public class ReleaseDateItemFilter implements ItemFilter{

    private LocalDateTime lowerBound;
    private LocalDateTime upperBound;

    private LocalDateTime[] checkDates(LocalDateTime lowerB, LocalDateTime upperB){
        LocalDateTime[] correctDates;
        if(!upperB.isAfter(lowerB)){
            LocalDateTime tmp = lowerB;
            lowerB = upperB;
            upperB = tmp;
       }
       correctDates = new LocalDateTime[]{lowerB,upperB};
       return correctDates;
    }

    public ReleaseDateItemFilter(LocalDateTime lowerBound, LocalDateTime upperBound) {
        LocalDateTime[] checkedDates = checkDates(lowerBound, upperBound);
        setLowerBound(checkedDates[0]);
        setUpperBound(checkedDates[1]);
    }

    
    public LocalDateTime getLowerBound() {
        return lowerBound;
    }

    public void setLowerBound(LocalDateTime lowerBound) {
        this.lowerBound = lowerBound;
    }

    public LocalDateTime getUpperBound() {
        return upperBound;
    }

    public void setUpperBound(LocalDateTime upperBound) {
        this.upperBound = upperBound;
    }

    @Override
    public boolean matches(StoreItem item) {
        boolean checkLower = item.getReleaseDate().isBefore(lowerBound);
        boolean checkUpper = item.getReleaseDate().isAfter(upperBound);
        return !checkLower && !checkUpper;
    }

}
