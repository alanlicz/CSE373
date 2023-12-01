package deques;

public class LinkedDeque<T> extends AbstractDeque<T> {
    private int size;
    // IMPORTANT: Do not rename these fields or change their visibility.
    // We access these during grading to test your code.
    Node<T> front;
    Node<T> back;
    // Feel free to add any additional fields you may need, though.

    public LinkedDeque() {
        size = 0;
        front = new Node<T>(null, null, null);
        back = new Node<T>(null, null, null);
        front.next = back;
        back.prev = front;
    }

    public void addFirst(T item) {
        size += 1;
        Node<T> toAdd = new Node<T>(item, front, front.next);
        front.next.prev = toAdd;
        front.next = toAdd;
    }

    public void addLast(T item) {
        size += 1;
        Node<T> toAdd = new Node<T>(item, back.prev, back);
        back.prev.next = toAdd;
        back.prev = toAdd;
    }

    public T removeFirst() {
        if (size == 0) {
            return null;
        }
        size -= 1;
        T toReturn = front.next.value;
        front.next = front.next.next;
        front.next.prev = front;
        return toReturn;
    }

    public T removeLast() {
        if (size == 0) {
            return null;
        }
        size -= 1;
        T toReturn = back.prev.value;
        back.prev = back.prev.prev;
        back.prev.next = back;
        return toReturn;
    }

    public T get(int index) {
        if ((index >= size) || (index < 0)) {
            return null;
        }

        /*cur = front.next;
        for (int i = 0; i < index; i++) {
            cur = cur.next;
        }
        */
        Node<T> cur;
        if (index < size / 2) {
            cur = front.next;
            for (int i = 0; i < index; i++) {
                cur = cur.next;
            }
        } else {
            cur = back.prev;
            for (int i = size - 1; i > index; i--) {
                cur = cur.prev;
            }
        }
        return cur.value;
    }

    public int size() {
        return size;
    }
}
