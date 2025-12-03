package bg.sofia.uni.fmi.mjt.eventbus.iterator;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

public class CustomIterator<T> implements Iterator<T> {

    private static final int START_INDEX = 0;
    private final List<T> events;
    private int position;

    public CustomIterator(Collection<T> events) {
        this.events = new ArrayList<>(events);
        position = START_INDEX;
    }

    @Override
    public boolean hasNext() {
        return position < events.size();
    }

    @Override
    public T next() {
        if (!hasNext()) {
            throw new RuntimeException("There are no more elements.");
        }
        return events.get(position++);
    }
}
