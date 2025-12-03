package bg.sofia.uni.fmi.mjt.eventbus.subscribers;

import java.time.Instant;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import bg.sofia.uni.fmi.mjt.eventbus.comparator.EventsComparator;
import bg.sofia.uni.fmi.mjt.eventbus.events.Event;
import bg.sofia.uni.fmi.mjt.eventbus.iterator.CustomIterator;

public class DeferredEventSubscriber<T extends Event<?>> implements Subscriber<T>, Iterable<T> {

    private Set<T> events;

    public DeferredEventSubscriber() {
        this.events = new HashSet<>();
    }

    /**
     * Store an event for processing at a later time.
     *
     * @param event the event to be processed
     * @throws IllegalArgumentException if the event is null
     */
    @Override
    public void onEvent(T event) {
        if (event == null) {
            throw new IllegalArgumentException("The event should not be null.");
        }
        events.add(event);
    }

    /**
     * Get an iterator for the unprocessed events. The iterator should provide the events sorted
     * by priority, with higher-priority events first (lower priority number = higher priority).
     * For events with equal priority, earlier events (by timestamp) come first.
     *
     * @return an iterator for the unprocessed events
     */

    @Override
    public Iterator<T> iterator() {
        List<T> sortedEvents = new ArrayList<>(events);
        sortedEvents.sort(new EventsComparator<>());
        return new CustomIterator<>(sortedEvents);
    }

    /**
     * Check if there are unprocessed events.
     *
     * @return true if there are unprocessed events, false otherwise
     */

    public boolean isEmpty() {
        for (var event : events) {
            if (!event.getTimestamp().isBefore(Instant.now())) {
                return true;
            }
        }
        return false;
    }

}