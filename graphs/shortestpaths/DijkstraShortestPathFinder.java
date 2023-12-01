package graphs.shortestpaths;

import graphs.BaseEdge;
import graphs.Graph;
import priorityqueues.DoubleMapMinPQ;
import priorityqueues.ExtrinsicMinPQ;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * Computes shortest paths using Dijkstra's algorithm.
 * @see SPTShortestPathFinder for more documentation.
 */
public class DijkstraShortestPathFinder<G extends Graph<V, E>, V, E extends BaseEdge<V, E>>
    extends SPTShortestPathFinder<G, V, E> {

    protected <T> ExtrinsicMinPQ<T> createMinPQ() {
        return new DoubleMapMinPQ<>();
        /*
        If you have confidence in your heap implementation, you can disable the line above
        and enable the one below.
         */
        // return new ArrayHeapMinPQ<>();

        /*
        Otherwise, do not change this method.
        We override this during grading to test your code using our correct implementation so that
        you don't lose extra points if your implementation is buggy.
         */
    }

    @Override
    protected Map<V, E> constructShortestPathsTree(G graph, V start, V end) {
        Map<V, E> edgeTo = new HashMap<>();
        Map<V, Double> distTo = new HashMap<>();
        ExtrinsicMinPQ<V> perimeter = createMinPQ();
        distTo.put(start, 0.0);
        perimeter.add(start, distTo.get(start));

        while (!perimeter.isEmpty()) {
            V u = perimeter.removeMin();

            if (Objects.equals(u, end)) {
                return edgeTo;
            }

            for (E edge : graph.outgoingEdgesFrom(u)) {
                if (Objects.equals(distTo.get(edge.to()), null)) {
                    distTo.put(edge.to(), Double.POSITIVE_INFINITY);
                }
                double oldDist = distTo.get(edge.to());
                double newDist = distTo.get(u) + edge.weight();
                if (newDist < oldDist) {
                    distTo.put(edge.to(), newDist);
                    edgeTo.put(edge.to(), edge);
                    if (perimeter.contains(edge.to())) {
                        perimeter.changePriority(edge.to(), newDist);
                    } else {
                        perimeter.add(edge.to(), newDist);
                    }
                }
            }
        }
        return edgeTo;
    }

    @Override
    protected ShortestPath<V, E> extractShortestPath(Map<V, E> spt, V start, V end) {
        if (Objects.equals(start, end)) {
            return new ShortestPath.SingleVertex<>(start);
        }
        E edge = spt.get(end);
        if (Objects.equals(edge, null)) {
            return new ShortestPath.Failure<>();
        }
        List<E> edges = new ArrayList<>();
        E curEdge = spt.get(end);
        edges.add(curEdge);
        while (!Objects.equals(curEdge.from(), start)) {
            curEdge = spt.get(curEdge.from());
            edges.add(curEdge);
        }
        Collections.reverse(edges);

        return new ShortestPath.Success<>(edges);
    }

}
