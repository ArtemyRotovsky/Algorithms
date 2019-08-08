import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.DirectedCycle;
import edu.princeton.cs.algs4.DirectedDFS;

import java.util.Map;
import java.util.HashMap;
import java.util.Set;
import java.util.TreeSet;

public class WordNet {

    private Map<Integer, String> synsets = new HashMap<>();
    private Map<String, Set<Integer>> nouns = new HashMap<>();

    private SAP sap;
    private Digraph digraph;

    // constructor takes the name of the two input files
    public WordNet(String synsets, String hypernyms) {
        In synIn = new In(synsets);

        while (!synIn.isEmpty()) {
            String[] line = synIn.readLine().split(",");
            Integer id = Integer.valueOf(line[0]);
            String synset = line[1];
            String[] nounsList = line[1].split(" ");

            this.synsets.put(id, synset);

            for (int i = 0; i < nounsList.length; i++) {
                String noun = nounsList[i];
                if (nouns.containsKey(noun)) {
                    nouns.get(noun).add(id);
                } else {
                    Set<Integer> s = new TreeSet<>();
                    s.add(id);
                    nouns.put(noun, s);
                }
            }

        }

        In hypIn = new In(hypernyms);
        digraph = new Digraph(this.synsets.size());
        while (!hypIn.isEmpty()) {
            String[] line = hypIn.readLine().split(",");
            Integer id = Integer.valueOf(line[0]);

            for (int i = 1; i < line.length; i++) {
                digraph.addEdge(id, Integer.parseInt(line[i]));
            }
        }

        sap = new SAP(digraph);

        // check cycle
        DirectedCycle finder = new DirectedCycle(digraph);
        if (finder.hasCycle()) {
            throw new IllegalArgumentException();
        }

        // check roots number
        Set<Integer> roots = new TreeSet<Integer>();
        for (int i = 0; i < digraph.V(); i++) {
            DirectedDFS dfs = new DirectedDFS(digraph, i);
            if (dfs.count() == 1) {
                roots.add(i);
            }
        }

        if (roots.size() > 1) {
            throw new IllegalArgumentException();
        }
    }

    // returns all WordNet nouns
    public Iterable<String> nouns() {
        return nouns.keySet();
    }

    // is the word a WordNet noun?
    public boolean isNoun(String word) {
        if (word == null) {
            throw new IllegalArgumentException();
        }
        return nouns.containsKey(word);
    }

    // distance between nounA and nounB (defined below)
    public int distance(String nounA, String nounB) {
        if (!isNoun(nounA) || !isNoun(nounB)) {
            throw new IllegalArgumentException();
        }
        Set<Integer> idA = nouns.get(nounA);
        Set<Integer> idB = nouns.get(nounB);

        return sap.length(idA, idB);
    }

    // a synset (second field of synsets.txt) that is the common ancestor of nounA and nounB
    // in a shortest ancestral path (defined below)
    public String sap(String nounA, String nounB) {
        if (!isNoun(nounA) || !isNoun(nounB)) {
            throw new IllegalArgumentException();
        }

        Set<Integer> idA = nouns.get(nounA);
        Set<Integer> idB = nouns.get(nounB);

        int ancestor = sap.ancestor(idA, idB);
        return synsets.get(ancestor);
    }

    // do unit testing of this class
    public static void main(String[] args) {
        WordNet wn = new WordNet("Wordnet/src/main/resources/wordnet/synsets15.txt",
                "Wordnet/src/main/resources/wordnet/hypernyms15Path.txt");

        System.out.println( wn.distance("a", "o") );
    }
}