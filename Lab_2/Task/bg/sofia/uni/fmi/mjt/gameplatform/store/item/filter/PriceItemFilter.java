package bg.sofia.uni.fmi.mjt.gameplatform.store.item.filter;

import java.math.BigDecimal;

import bg.sofia.uni.fmi.mjt.gameplatform.store.item.StoreItem;

public class PriceItemFilter implements ItemFilter{

    private BigDecimal lowerBound;
    private BigDecimal upperBound;

    private BigDecimal[]  checkBounds(BigDecimal lowerB, BigDecimal upperB){
        if(lowerB.compareTo(upperB) > 1){
            BigDecimal tmp = lowerB;
            lowerB = upperB;
            upperB = tmp;
        }
        return new BigDecimal[]{lowerB,upperB};
    }

    public PriceItemFilter(BigDecimal lowerBound, BigDecimal upperBound){
       BigDecimal[] bounds = checkBounds(lowerBound,upperBound);
       setLowerBound(bounds[0]);
       setUpperBound(bounds[1]);
    }
        
    public BigDecimal getLowerBound() {
        return lowerBound;
    }

    public void setLowerBound(BigDecimal lowerBound) {
        this.lowerBound = lowerBound;
    }

    public BigDecimal getUpperBound() {
        return upperBound;
    }

    public void setUpperBound(BigDecimal upperBound) {
        this.upperBound = upperBound;
    }

    @Override
    public boolean matches(StoreItem item) {
        int comparedToLowerBound = item.getPrice().compareTo(getLowerBound());
        int comparedToUpperBound = item.getPrice().compareTo(getLowerBound());

        return (comparedToLowerBound >= 0) &&
               (comparedToUpperBound <= 0);
    }

}
