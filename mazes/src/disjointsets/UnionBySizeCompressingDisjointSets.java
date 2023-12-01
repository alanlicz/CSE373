package disjointsets;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * A quick-union-by-size data structure with path compression.
 * @see DisjointSets for more documentation.
 */
public class UnionBySizeCompressingDisjointSets<T> implements DisjointSets<T> {
    // Do NOT rename or delete this field. We will be inspecting it directly in our private tests.
    List<Integer> pointers;
    Map<T, Integer> map;
    /*
    However, feel free to add more fields and private helper methods. You will probably need to
    add one or two more fields in order to successfully implement this class.
    */

    public UnionBySizeCompressingDisjointSets() {
        pointers = new ArrayList<Integer>();
        map = new HashMap<T, Integer>();
    }

    @Override
    public void makeSet(T item) {
        if (map.keySet().contains(item)) {
            throw new IllegalArgumentException();
        }
        map.put(item, pointers.size());
        pointers.add(-1);
    }

    @Override
    public int findSet(T item) {
        if (!map.keySet().contains(item)) {
            throw new IllegalArgumentException();
        }
        int index = map.get(item);
        Set<Integer> childIndices = new HashSet<Integer>();
        while (pointers.get(index) >= 0) {
            childIndices.add(index);
            index = pointers.get(index);
        }
        for (Integer child : childIndices) {
            pointers.set(child, index);
        }
        return index;

    }

    @Override
    public boolean union(T item1, T item2) {
        if (!map.keySet().contains(item1) || !map.keySet().contains(item2)) {
            throw new IllegalArgumentException();
        }
        int root1 = findSet(item1);
        int root2 = findSet(item2);
        if (root1 == root2) {
            return false;
        }

        int root1Weight = -1 * pointers.get(root1);
        int root2Weight = -1 * pointers.get(root2);

        if (root1Weight > root2Weight) {
            pointers.set(root1, pointers.get(root1) + pointers.get(root2));
            pointers.set(root2, root1);
        } else {
            pointers.set(root2, pointers.get(root1) + pointers.get(root2));
            pointers.set(root1, root2);
        }
        return true;
    }
}
