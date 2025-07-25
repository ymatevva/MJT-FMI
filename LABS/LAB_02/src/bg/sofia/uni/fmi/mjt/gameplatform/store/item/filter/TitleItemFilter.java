package bg.sofia.uni.fmi.mjt.gameplatform.store.item.filter;

import bg.sofia.uni.fmi.mjt.gameplatform.store.item.StoreItem;

public class TitleItemFilter  implements ItemFilter{

    private String title;
    private boolean caseSensitive;
    
    public TitleItemFilter(String title, boolean caseSensitive){
      setCaseSensitive(caseSensitive);
      setTitle(title);
    }

    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title == null || title.isEmpty() ? "Unknown" : title;
    }
    public boolean isCaseSensitive() {
        return caseSensitive;
    }
    public void setCaseSensitive(boolean caseSensitive) {
        this.caseSensitive = caseSensitive;
    }
   
    @Override
    public boolean matches(StoreItem item) {
       if(caseSensitive){
        return item.getTitle().equals(title);
       } else{
        return item.getTitle().equals(title) || item.getTitle().equals(title.toLowerCase()) || item.getTitle().equals(title.toUpperCase());
       }
    }

}
