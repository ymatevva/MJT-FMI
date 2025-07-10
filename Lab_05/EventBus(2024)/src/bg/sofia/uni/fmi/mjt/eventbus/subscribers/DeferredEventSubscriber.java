package bg.sofia.uni.fmi.mjt.eventbus.subscribers;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import bg.sofia.uni.fmi.mjt.eventbus.events.Event;

public class DeferredEventSubscriber<T extends Event<?>> implements Subscriber<T>, Iterable<T> {

    private Set<T> events;

    public DeferredEventSubscriber(Set events) {
        this.events = new TreeSet<>(new EventComparator<>());
    }

    private static class EventComparator<T extends Event> implements Comparator<T> {
        @Override
        public int compare(T o1, T o2) {
            if (o1.getPriority() != o2.getPriority()) {
                return o2.getPriority() - o1.getPriority();
            } else {
                return o1.getTimestamp().compareTo(o2.getTimestamp());
            }
        }
    }

    @Override
    public void onEvent(T event) {

        if (event == null) {
            throw new IllegalArgumentException("The event does not exist.");
        }

        events.add(event);
    }

    @Override
    public Iterator<T> iterator() {
        return events.iterator();
    }

    public boolean isEmpty() {
        return events.size() != 0;
    }

}