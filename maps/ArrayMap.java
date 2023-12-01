package maps;

import java.util.Iterator;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Objects;

/**
 * @see AbstractIterableMap
 * @see Map
 */
public class ArrayMap<K, V> extends AbstractIterableMap<K, V> {

    private int size;

    private static final int DEFAULT_INITIAL_CAPACITY = 0;
    /*
    Warning:
    You may not rename this field or change its type.
    We will be inspecting it in our secret tests.
     */
    SimpleEntry<K, V>[] entries;

    // You may add extra fields or helper methods though!

    public ArrayMap() {
        this(DEFAULT_INITIAL_CAPACITY);
    }

    public ArrayMap(int initialCapacity) {
        this.entries = this.createArrayOfEntries(initialCapacity);
        size = 0;
    }

    /**
     * This method will return a new, empty array of the given size that can contain
     * {@code Entry<K, V>} objects.
     *
     * Note that each element in the array will initially be null.
     *
     * Note: You do not need to modify this method.
     */
    @SuppressWarnings("unchecked")
    private SimpleEntry<K, V>[] createArrayOfEntries(int arraySize) {
        /*
        It turns out that creating arrays of generic objects in Java is complicated due to something
        known as "type erasure."

        We've given you this helper method to help simplify this part of your assignment. Use this
        helper method as appropriate when implementing the rest of this class.

        You are not required to understand how this method works, what type erasure is, or how
        arrays and generics interact.
        */
        return (SimpleEntry<K, V>[]) (new SimpleEntry[arraySize]);
    }

    @Override
    public V get(Object key) {
        for (int i = 0; i < size; i++) {
            if (Objects.equals(key, entries[i].getKey())) {
                return entries[i].getValue();
            }
        }
        return null;
    }

    @Override
    public V put(K key, V value) {
        if (size >= entries.length) {
            resize();
        }
        for (int i = 0; i < size; i++) {
            if (Objects.equals(key, entries[i].getKey())) {
                V prev = entries[i].getValue();
                entries[i] = new SimpleEntry<>(key, value);
                return prev;
            }
        }
        entries[size] = new SimpleEntry<>(key, value);
        size++;
        return null;
    }

    private void resize() {
        SimpleEntry<K, V>[] newEntries = createArrayOfEntries(size * 2 + 1);
        if (size >= 0) {
            System.arraycopy(entries, 0, newEntries, 0, size);
        }
        this.entries = newEntries;
    }

    @Override
    public V remove(Object key) {
        if (this.containsKey(key)) {
            for (int i = 0; i < size; i++) {
                if (Objects.equals(key, entries[i].getKey())) {
                    V value = entries[i].getValue();
                    entries[i] = entries[size - 1];
                    entries[size - 1] = null;
                    size--;
                    return value;
                }
            }
        }
        return null;
    }

    @Override
    public void clear() {
        for (int i = 0; i < size; i++) {
            entries[i] = null;
        }
        size = 0;
    }

    @Override
    public boolean containsKey(Object key) {
        for (int i = 0; i < size; i++) {
            if (Objects.equals(key, entries[i].getKey())) {
                return true;
            }
        }
        return false;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public Iterator<Map.Entry<K, V>> iterator() {
        // Note: you won't need to change this method (unless you add more constructor parameters)
        return new ArrayMapIterator<>(this.entries, size);
    }

    private static class ArrayMapIterator<K, V> implements Iterator<Map.Entry<K, V>> {
        private final SimpleEntry<K, V>[] entries;

        private int index;

        private final int size;

        public ArrayMapIterator(SimpleEntry<K, V>[] entries, int size) {
            this.entries = entries;
            this.size = size;
            this.index = 0;
        }

        @Override
        public boolean hasNext() {
            return index < size;
        }

        @Override
        public Map.Entry<K, V> next() {
            if (index >= size) {
                throw new NoSuchElementException();
            }
            int tempIndex = index;
            index++;
            return this.entries[tempIndex];
        }
    }
}
