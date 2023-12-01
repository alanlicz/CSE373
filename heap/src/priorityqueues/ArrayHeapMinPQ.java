package priorityqueues;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.TreeMap;

/**
 * @see ExtrinsicMinPQ
 */
public class ArrayHeapMinPQ<T> implements ExtrinsicMinPQ<T> {
    // IMPORTANT: Do not rename these fields or change their visibility.
    // We access these during grading to test your code.
    static final int START_INDEX = 1;
    int size;
    TreeMap<T, Integer> shelf;
    List<PriorityNode<T>> items;

    public ArrayHeapMinPQ() {
        items = new ArrayList<>();
        size = 0;
        items.add(null);
        shelf = new TreeMap<T, Integer>();


    }

    // Here's a method stub that may be useful. Feel free to change or remove it, if you wish.
    // You'll probably want to add more helper methods like this one to make your code easier to read.
    /**
     * A helper method for swapping the items at two indices of the array heap.
     */
    private void swap(int a, int b) {
        shelf.put(items.get(a).getItem(), b);
        shelf.put(items.get(b).getItem(), a);
        PriorityNode<T> temp = items.get(a);
        items.set(a, items.get(b));
        items.set(b, temp);

    }

    private void percolateUp(int curNodeIndex) {
        int parentNodeIndex = curNodeIndex / 2;
        while (parentNodeIndex > 0) {
            if (items.get(curNodeIndex).getPriority() < items.get(parentNodeIndex).getPriority()) {
                swap(curNodeIndex, parentNodeIndex);
                curNodeIndex = parentNodeIndex;
                parentNodeIndex = curNodeIndex / 2;
            } else {
                parentNodeIndex = -1;
            }
        }

    }

    private int getSmallestChild(int parentIndex) {
        double leftChildPrio = -1;
        double rightChildPrio = -1;
        if (2 * parentIndex <= size) {
            leftChildPrio = items.get(2 * parentIndex).getPriority();
        }
        if ((2 * parentIndex) + 1 <= size) {
            rightChildPrio = items.get((2 * parentIndex) + 1).getPriority();
        }
        if (leftChildPrio == -1 && rightChildPrio == -1) {
            return -1;
        }
        if (leftChildPrio == -1) {
            return (2 * parentIndex) + 1;
        }
        if (rightChildPrio == -1) {
            return 2 * parentIndex;
        }
        if (leftChildPrio < rightChildPrio) {
            return 2 * parentIndex;
        }
        return (2 * parentIndex) + 1;
    }

    private void percolateDown(int curNode) {
        int smallestChild = getSmallestChild(curNode);
        while (smallestChild > 0) {
            if (items.get(smallestChild).getPriority() > items.get(curNode).getPriority()) {
                smallestChild = -1;
            } else {
                swap(curNode, smallestChild);
                curNode = smallestChild;
                smallestChild = getSmallestChild(curNode);
            }
        }
    }

    @Override
    public void add(T item, double priority) {
        if (Objects.equals(null, item) || this.contains(item)) {
            throw new IllegalArgumentException();
        }
        size++;
        PriorityNode<T> toAdd = new PriorityNode<T>(item, priority);
        items.add(toAdd);
        shelf.put(item, size);
        percolateUp(size);

    }

    @Override
    public boolean contains(T item) {
        return shelf.containsKey(item);
    }

    @Override
    public T peekMin() {
        if (size == 0) {
            throw new NoSuchElementException();
        }
        return items.get(START_INDEX).getItem();
    }

    @Override
    public T removeMin() {
        if (size == 0) {
            throw new NoSuchElementException();
        }
        T toReturn = items.get(START_INDEX).getItem();
        swap(START_INDEX, size);
        size--;
        percolateDown(START_INDEX);
        items.remove(size + 1);
        shelf.remove(toReturn);
        return toReturn;
    }

    @Override
    public void changePriority(T item, double priority) {
        if (size == 0 || !contains(item)) {
            throw new NoSuchElementException();
        }
        int index = shelf.get(item);
        double originalPriority = items.get(index).getPriority();
        items.get(index).setPriority(priority);
        if (originalPriority < priority) {
            percolateDown(index);
        } else if (priority < originalPriority) {
            percolateUp(index);
        }

    }

    @Override
    public int size() {
        return size;
    }
}
