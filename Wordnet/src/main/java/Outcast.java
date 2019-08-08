import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

public class Outcast {
    private WordNet wordnet;

    // constructor takes a WordNet object
    public Outcast(WordNet wordnet) {
        this.wordnet = wordnet;
    }

    // given an array of WordNet nouns, return an outcast
    public String outcast(String[] nouns) {
        int distance = 0;
        String outcast = null;

        for (String i : nouns) {
            int d = 0;

            for (String j : nouns) {
                int dist = wordnet.distance(i, j);
                d += dist;
            }

            if (d > distance) {
                distance = d;
                outcast = i;
            }

        }

        assert outcast != null;
        return outcast;
    }

    // see test client below
    public static void main(String[] args) {
    }
}