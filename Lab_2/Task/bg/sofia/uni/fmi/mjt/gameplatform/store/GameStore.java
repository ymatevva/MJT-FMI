package bg.sofia.uni.fmi.mjt.gameplatform.store;

import java.math.BigDecimal;

import bg.sofia.uni.fmi.mjt.gameplatform.store.item.StoreItem;
import bg.sofia.uni.fmi.mjt.gameplatform.store.item.filter.ItemFilter;

public class GameStore implements StoreAPI{

    private StoreItem[] availableItems;


    public GameStore(StoreItem[] availableItems) {
       setAvailableItems(availableItems);
    } 
    
    public StoreItem[] getAvailableItems() {
        StoreItem[] copyItems = new StoreItem[availableItems.length];
        System.arraycopy(availableItems, 0, copyItems, 0, availableItems.length);
        return copyItems;
    }

    public void setAvailableItems(StoreItem[] availableItems) {
        this.availableItems = new StoreItem[availableItems.length];
        System.arraycopy(availableItems, 0, this.availableItems, 0, availableItems.length);
    }

    @Override
    public void applyDiscount(String promoCode) {
        if(promoCode == null || (!promoCode.equals("VAN40") && !promoCode.equals("100YO"))){
            return;
        }

        if(promoCode.equals("VAN40")) {
        
            for (int i = 0; i < availableItems.length; i++) {
                 BigDecimal oldPrice = availableItems[i].getPrice();
                 BigDecimal newPrice = oldPrice.subtract(BigDecimal.valueOf(0.40).multiply(oldPrice));
                 availableItems[i].setPrice(newPrice);
            }
        } 
        else {
            
            for (int i = 0; i < availableItems.length; i++) {
                availableItems[i].setPrice(BigDecimal.ZERO);
            }
        }
    }


    @Override
    public StoreItem[] findItemByFilters(ItemFilter[] itemFilters) {
        StoreItem[] filteredItems = new StoreItem[availableItems.length];
        int ind = 0;
        for (int i = 0; i < filteredItems.length; i++) {
            
            boolean matches = true;
           
            if(!itemFilters[i].matches(availableItems[i])){
                 matches = false;
            }
            
            if(matches) {
                filteredItems[ind++] = availableItems[i];
            }
        }
        StoreItem[] finalSetOfItems = new StoreItem[ind];
        System.arraycopy(filteredItems,0,finalSetOfItems,0,ind);
        return finalSetOfItems;
    }

    @Override
    public boolean rateItem(StoreItem item, int rating) {
        if(item == null || rating < 1 || rating > 5){
            return false;
        }

        item.rate((double)rating);
        return true;
    }

}
