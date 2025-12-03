package bg.sofia.uni.fmi.mjt.eventbus.comparator;

import bg.sofia.uni.fmi.mjt.eventbus.events.Event;
import java.util.Comparator;

public class EventsComparator<T extends Event<?>> implements Comparator<T> {

    @Override
    public int compare(T o1, T o2) {
        int compPriority = Integer.compare(o1.getPriority(), o2.getPriority());
        if (compPriority == 0) {
            return Long.compare(o2.getTimestamp().getEpochSecond(), o1.getTimestamp().getEpochSecond());
        }
        return compPriority;
    }
}
