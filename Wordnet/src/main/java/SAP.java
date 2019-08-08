import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.BreadthFirstDirectedPaths;

public class SAP {
    private Digraph digraph;
    private BreadthFirstDirectedPaths[] shortestPaths;
    // constructor takes a digraph (not necessarily a DAG)
    public SAP(Digraph G) {
        this.digraph = new Digraph(G);
        shortestPaths = new BreadthFirstDirectedPaths[this.digraph.V()];
    }

    // length of shortest ancestral path between v and w; -1 if no such path
    public int length(int v, int w) {
        if (v < 0 || v > digraph.V() - 1) {
            throw new IllegalArgumentException();
        }

        if (w < 0 || w > digraph.V() - 1) {
            throw new IllegalArgumentException();
        }

        BreadthFirstDirectedPaths bfsV;
        if (shortestPaths[v] == null) {
            bfsV = new BreadthFirstDirectedPaths(digraph, v);
            shortestPaths[v] = bfsV;
        } else {
            bfsV = shortestPaths[v];
        }

        BreadthFirstDirectedPaths bfsW;
        if (shortestPaths[w] == null) {
            bfsW = new BreadthFirstDirectedPaths(digraph, w);
            shortestPaths[w] = bfsW;
        } else {
            bfsW = shortestPaths[w];
        }

        Integer minLen = Integer.MAX_VALUE;
        for (int i = 0; i < digraph.V(); i++) {
            if (bfsV.hasPathTo(i) && bfsW.hasPathTo(i)) {
                int dist = bfsV.distTo(i)  + bfsW.distTo(i);
                if (dist < minLen) {
                    minLen = dist;
                }
            }
        }

        if (minLen == Integer.MAX_VALUE)
             return -1;

        return minLen;
    }

    // a common ancestor of v and w that participates in a shortest ancestral path; -1 if no such path
    public int ancestor(int v, int w) {
        if (v < 0 || v > digraph.V() - 1) {
            throw new IllegalArgumentException();
        }

        if (w < 0 || w > digraph.V() - 1) {
            throw new IllegalArgumentException();
        }

        BreadthFirstDirectedPaths bfsV;
        if (shortestPaths[v] == null) {
            bfsV = new BreadthFirstDirectedPaths(digraph, v);
            shortestPaths[v] = bfsV;
        } else {
            bfsV = shortestPaths[v];
        }

        BreadthFirstDirectedPaths bfsW;
        if (shortestPaths[w] == null) {
            bfsW = new BreadthFirstDirectedPaths(digraph, w);
            shortestPaths[w] = bfsW;
        } else {
            bfsW = shortestPaths[w];
        }

        Integer minLen = Integer.MAX_VALUE;
        Integer ancestor = -1;
        for (int i = 0; i < digraph.V(); i++) {
            if (bfsV.hasPathTo(i) && bfsW.hasPathTo(i)) {
                int dist = bfsV.distTo(i)  + bfsW.distTo(i);
                if (dist < minLen) {
                    minLen = dist;
                    ancestor = i;
                }
            }
        }

        return ancestor;

    }

    // length of shortest ancestral path between any vertex in v and any vertex in w; -1 if no such path
    public int length(Iterable<Integer> v, Iterable<Integer> w) {
        if (v == null || w == null) {
            throw new IllegalArgumentException();
        }

        Integer minLen = Integer.MAX_VALUE;
        for (int i : v) {
            for (int j : w) {
                int l = length(i, j);
                if (l != -1 && l < minLen) {
                    minLen = l;
                }
            }
        }

        if (minLen == Integer.MAX_VALUE)
            return -1;

        return minLen;
    }

    // a common ancestor that participates in shortest ancestral path; -1 if no such path
    public int ancestor(Iterable<Integer> v, Iterable<Integer> w) {
        if (v == null || w == null) {
            throw new IllegalArgumentException();
        }

        int minLen = Integer.MAX_VALUE;
        int ancestor = -1;

        for (int i : v) {
            for (int j : w) {
                int l = length(i, j);
                if (l != -1 && l < minLen) {
                    minLen = l;
                    ancestor = ancestor(i, j);
                }
            }
        }
        return ancestor;
    }

    // do unit testing of this class
    public static void main(String[] args) {

    }
}
