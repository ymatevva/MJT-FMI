package bg.sofia.uni.fmi.mjt.eventbus;

import bg.sofia.uni.fmi.mjt.eventbus.events.Event;
import bg.sofia.uni.fmi.mjt.eventbus.exception.MissingSubscriptionException;
import bg.sofia.uni.fmi.mjt.eventbus.subscribers.Subscriber;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class EventBusImpl<T extends Event<?>> implements EventBus {

    private Map<Class, Set<Subscriber<?>>> subscriptions;
    private Set<Event<?>> eventLogs;

    public EventBusImpl() {
        subscriptions = new HashMap<>();
        eventLogs = new HashSet<>();
    }

    private void validateData(Class<?> eventType, Subscriber<?> subscriber) {
        if (eventType == null) {
            throw new IllegalArgumentException("The event type cannot be null.");
        }

        if (subscriber == null) {
            throw new IllegalArgumentException("The subscriber cannot be null.");
        }
    }

    private void validateIsSubscriber(Class<?> eventType, Subscriber<?> subscriber)
            throws MissingSubscriptionException {
        Set<Subscriber<?>> subs = subscriptions.get(eventType);
        if (subs == null || !subs.contains(subscriber)) {
            throw new MissingSubscriptionException("The subscriber is not subscribed to the event.");
        }
    }

    @Override
    public <T extends Event<?>> void subscribe(Class<T> eventType, Subscriber<? super T> subscriber) {
        validateData(eventType, subscriber);
        subscriptions.putIfAbsent(eventType, new HashSet<>());
        subscriptions.get(eventType).add(subscriber);
    }

    @Override
    public <T extends Event<?>> void unsubscribe(Class<T> eventType, Subscriber<? super T> subscriber)
            throws MissingSubscriptionException {

        validateData(eventType, subscriber);
        validateIsSubscriber(eventType, subscriber);

        subscriptions.get(eventType).remove(subscriber);
    }

    @Override
    public <T extends Event<?>> void publish(T event) {
        if (event == null) {
            throw new IllegalArgumentException("The event cannot be null.");
        }

        Set<Subscriber<?>> subs = subscriptions.get(event.getClass());
        if (subs != null) {
            for (Subscriber<?> subscriber : subs) {
                ((Subscriber<T>) subscriber).onEvent(event);
            }
        }

        eventLogs.add(event);
    }

    @Override
    public void clear() {
        subscriptions.clear();
        eventLogs.clear();
    }

    @Override
    public Collection<? extends Event<?>> getEventLogs(Class<? extends Event<?>> eventType, Instant from, Instant to) {
        validateEventType(eventType);
        validateTimestamp(from, to);

        List<Event<?>> eventsInRange = new ArrayList<>();
        for (Event<?> event : eventLogs) {
            if (event != null && event.getClass().equals(eventType) &&
                    !event.getTimestamp().isBefore(from) &&
                    !event.getTimestamp().isAfter(to)) {
                eventsInRange.add(event);
            }
        }
        return Collections.unmodifiableList(eventsInRange);
    }

    @Override
    public <T extends Event<?>> Collection<Subscriber<?>> getSubscribersForEvent(Class<T> eventType) {
        validateEventType(eventType);

        return Collections.unmodifiableCollection(subscriptions.get(eventType));
    }

    private <T extends Event<?>> void validateEventType(Class<T> eventType) {
        if (eventType == null) {
            throw new IllegalArgumentException("The event type cannot be null.");
        }
    }

    private void validateTimestamp(Instant from, Instant to) {
        if (from == null) {
            throw new IllegalArgumentException("The start of the timestamp cannot be null.");
        }

        if (to == null) {
            throw new IllegalArgumentException("The end of the timestamp cannot be null.");
        }
    }
}
