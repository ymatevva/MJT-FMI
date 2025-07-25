package bg.sofia.uni.fmi.mjt.eventbus;

import bg.sofia.uni.fmi.mjt.eventbus.events.Event;
import bg.sofia.uni.fmi.mjt.eventbus.exception.MissingSubscriptionException;
import bg.sofia.uni.fmi.mjt.eventbus.subscribers.DeferredEventSubscriber;
import bg.sofia.uni.fmi.mjt.eventbus.subscribers.Subscriber;

import java.time.Instant;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class EventBusImpl implements EventBus {

    private final Map<Class<?>, Set<Subscriber<?>>> events;
    private final Map<Class<?>, Set<Event<?>>> eventLogs;

    EventBusImpl() {
        this.events = new HashMap<>();
        this.eventLogs = new HashMap<>();
    }

    /**
     * Subscribes the given subscriber to the given event type.
     *
     * @param eventType  the type of event to subscribe to
     * @param subscriber the subscriber to subscribe
     * @throws IllegalArgumentException if the event type is null
     * @throws IllegalArgumentException if the subscriber is null
     */
    @Override
    public <T extends Event<?>> void subscribe(Class<T> eventType, Subscriber<? super T> subscriber) {
        if (eventType == null || subscriber == null) {
            throw new IllegalArgumentException("The event type or the subscriber is null.");
        }

        events.putIfAbsent(eventType, new HashSet<>());
        events.get(eventType).add(subscriber);
    }

    /**
     * Unsubscribes the given subscriber from the given event type.
     *
     * @param eventType  the type of event to unsubscribe from
     * @param subscriber the subscriber to unsubscribe
     * @throws IllegalArgumentException     if the event type is null
     * @throws IllegalArgumentException     if the subscriber is null
     * @throws MissingSubscriptionException if the subscriber is not subscribed to the event type
     */
    public <T extends Event<?>> void unsubscribe(Class<T> eventType, Subscriber<? super T> subscriber)
        throws MissingSubscriptionException {

        if (eventType == null || subscriber == null) {
            throw new IllegalArgumentException("The event type is null or the subscriber is null.");
        }

        if (!events.get(eventType).contains(subscriber) || !events.containsKey(eventType)) {
            throw new MissingSubscriptionException("The subscriber is not subscribed to the event.");
        }

        events.get(eventType).remove(subscriber);
    }

    /**
     * Publishes the given event to all subscribers of the event type.
     *
     * @param event the event to publish
     * @throws IllegalArgumentException if the event is null
     */
    public <T extends Event<?>> void publish(T event) {
        if (event == null) {
            throw new IllegalArgumentException("The event is null.");
        }

        if (!events.containsKey(event.getClass())) {
            return;
        }

        eventLogs.putIfAbsent(event.getClass(), new HashSet<>());
        eventLogs.get(event.getClass()).add(event);

        for (Subscriber subs : events.get(event)) {
            subs.onEvent(event);
        }

    }

    public void clear() {
        events.clear();
        eventLogs.clear();
    }
    /**
     * Returns all events of the given event type that occurred between the given timestamps. If
     * {@code from} and {@code to} are equal the returned collection is empty.
     * <p> {@code from} - inclusive, {@code to} - exclusive. </p>
     *
     * @param eventType the type of event to get
     * @param from      the start timestamp (inclusive)
     * @param to        the end timestamp (exclusive)
     * @return an unmodifiable collection of events of the given event type that occurred between
     * the given timestamps
     * @throws IllegalArgumentException if the event type is null
     * @throws IllegalArgumentException if the start timestamp is null
     * @throws IllegalArgumentException if the end timestamp is null
     */
    public Collection<? extends Event<?>> getEventLogs(Class<? extends Event<?>> eventType, Instant from,
                                                       Instant to) {
        if (eventType == null || from == null || to == null) {
            throw new IllegalArgumentException("The from, to, eventType cannot be null.");
        }

        Set<Event<?>> timestampSet = new HashSet<>();

        for (var event : eventLogs.get(eventType)){
             if(!event.getTimestamp().isBefore(from)
             && !event.getTimestamp().isAfter(to)) {
                 timestampSet.add(event);
             }
        }
        return Set.copyOf(timestampSet);
    }
    /**
     *
     * Returns all subscribers for the given event type in an unmodifiable collection. If there are
     * no subscribers for the event type, the method returns an empty unmodifiable collection.
     *
     * @param eventType the type of event to get subscribers for
     * @return an unmodifiable collection of subscribers for the given event type
     * @throws IllegalArgumentException if the event type is null
     */
    public <T extends Event<?>> Collection<Subscriber<?>> getSubscribersForEvent(Class<T> eventType) {
        if (eventType == null) {
            throw new IllegalArgumentException("The event type us null.");
        }

        if (events.get(eventType).size() == 0) {
            return Collections.emptySet();
        }

        return Set.copyOf(events.get(eventType));
    }

}

