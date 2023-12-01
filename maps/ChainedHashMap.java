package maps;

import java.util.Iterator;
import java.util.Map;
import java.util.NoSuchElementException;

/**
 * @see AbstractIterableMap
 * @see Map
 */
public class ChainedHashMap<K, V> extends AbstractIterableMap<K, V> {
    private static final double DEFAULT_RESIZING_LOAD_FACTOR_THRESHOLD = 1;
    private static final int DEFAULT_INITIAL_CHAIN_COUNT = 5;
    private static final int DEFAULT_INITIAL_CHAIN_CAPACITY = 3;


    /*
    Warning:
    You may not rename this field or change its type.
    We will be inspecting it in our secret tests.
     */
    AbstractIterableMap<K, V>[] chains;
    private double resizingThreshold;
    private int chainCount;
    private int chainCapacity;
    private int size = 0;

    // You're encouraged to add extra fields (and helper methods) though!

    public ChainedHashMap() {
        this(DEFAULT_RESIZING_LOAD_FACTOR_THRESHOLD, DEFAULT_INITIAL_CHAIN_COUNT, DEFAULT_INITIAL_CHAIN_CAPACITY);
    }

    public ChainedHashMap(double resizingLoadFactorThreshold, int initialChainCount, int chainInitialCapacity) {
        this.resizingThreshold = resizingLoadFactorThreshold;
        this.chainCount = initialChainCount;
        this.chainCapacity = chainInitialCapacity;
        chains = createArrayOfChains(chainCount);
    }

    /**
     * This method will return a new, empty array of the given size that can contain
     * {@code AbstractIterableMap<K, V>} objects.
     *
     * Note that each element in the array will initially be null.
     *
     * Note: You do not need to modify this method.
     * @see ArrayMap createArrayOfEntries method for more background on why we need this method
     */
    @SuppressWarnings("unchecked")
    private AbstractIterableMap<K, V>[] createArrayOfChains(int arraySize) {
        return (AbstractIterableMap<K, V>[]) new AbstractIterableMap[arraySize];
    }

    /**
     * Returns a new chain.
     *
     * This method will be overridden by the grader so that your ChainedHashMap implementation
     * is graded using our solution ArrayMaps.
     *
     * Note: You do not need to modify this method.
     */
    protected AbstractIterableMap<K, V> createChain(int initialSize) {
        return new ArrayMap<>(initialSize);
    }

    private int getHashCode(Object key) {
        int code;
        if (key == null) {
            code = 0;
        } else {
            code = Math.abs(key.hashCode()) % chainCount;
        }
        return code;
    }

    private AbstractIterableMap<K, V>[] resize() {
        this.chainCount = chainCount * 2;
        AbstractIterableMap<K, V>[] newChains = createArrayOfChains(this.chainCount * 2);
        for (int i = 0; i < chainCount / 2; i++) {
            if (chains[i] != null) {
                for (Entry<K, V> curEntry : chains[i].entrySet()) {
                    int code = getHashCode(curEntry.getKey());
                    if (newChains[code] == null) {
                        newChains[code] = createChain(chainCapacity);
                    }
                    newChains[code].put(curEntry.getKey(), curEntry.getValue());
                }
            }
        }
        return newChains;
    }

    @Override
    public V get(Object key) {
        int code = getHashCode(key);
        if (chains[code] == null) {
            return null;
        }
        return chains[code].get(key);
    }

    @Override
    public V put(K key, V value) {
        int code = getHashCode(key);
        if (chains[code] == null) {
            chains[code] = createChain(chainCapacity);
        }
        V result = chains[code].put(key, value);
        if (result == null) {
            size++;
        }
        if ((double) (size / chainCount) > resizingThreshold) {
            chains = resize();
        }
        return result;
    }

    @Override
    public V remove(Object key) {
        int index = getHashCode(key);
        if (chains[index] == null) {
            return null;
        }
        size--;
        return chains[index].remove(key);
    }

    @Override
    public void clear() {
        chains = createArrayOfChains(DEFAULT_INITIAL_CHAIN_COUNT);
        size = 0;
    }

    @Override
    public boolean containsKey(Object key) {
        int hash = getHashCode(key);
        if (chains[hash] == null) {
            return false;
        }
        return chains[hash].containsKey(key);
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public Iterator<Map.Entry<K, V>> iterator() {
        // Note: you won't need to change this method (unless you add more constructor parameters)
        return new ChainedHashMapIterator<>(this.chains);
    }


    /*
    See the assignment webpage for tips and restrictions on implementing this iterator.
     */
    private static class ChainedHashMapIterator<K, V> implements Iterator<Map.Entry<K, V>> {
        private final AbstractIterableMap<K, V>[] chains;

        private AbstractIterableMap<K, V> currentChain;

        private int index;

        private final int numChains;

        private Iterator<Entry<K, V>> curIterator;

        public ChainedHashMapIterator(AbstractIterableMap<K, V>[] chains) {
            this.chains = chains;

            this.index = 0;

            this.numChains = chains.length;

            this.currentChain = chains[index];

            curIterator = null;
        }

        @Override
        public boolean hasNext() {
            if (curIterator != null) {
                if (curIterator.hasNext()) {
                    return true;
                }
                index++;
            }
            while (index < numChains) {
                currentChain = chains[index];
                if (currentChain != null) {
                    curIterator = currentChain.iterator();
                    if (curIterator.hasNext()) {
                        return true;
                    }
                }
                index++;

            }
            return false;
        }

        @Override
        public Map.Entry<K, V> next() {
            if (hasNext()) {
                return curIterator.next();
            }
            throw new NoSuchElementException();
        }

    }
}
