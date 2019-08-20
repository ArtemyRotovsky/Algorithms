import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.SET;
import edu.princeton.cs.algs4.StdOut;

public class BoggleSolver {

    private final MyTST<Boolean> dict;

    // Initializes the data structure using the given array of strings as the dictionary.
    // (You can assume each word in the dictionary contains only the uppercase letters A through Z.)
    public BoggleSolver(String[] dictionary) {
        dict = new MyTST<>();
        for (String word : dictionary) {
            dict.put(word, true);
        }
    }

    // Returns the set of all valid words in the given Boggle board, as an Iterable.
    public Iterable<String> getAllValidWords(BoggleBoard board) {
        SET<String> validWords = new SET<>();
        int cols = board.cols();
        int rows = board.rows();

        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                boolean[][] marked = new boolean[rows][cols];
                collect(board, row, col, marked, validWords, "");
            }
        }

        return validWords;
    }

    private void collect(BoggleBoard board, int row, int col, boolean[][] marked, SET<String> validWords, String prefix) {
        if (marked[row][col]) {
            return;
        }

        String word = prefix;
        char c = board.getLetter(row, col);

        // handle Q case
        if (c == 'Q') {
            word += "QU";
        } else {
            word += c;
        }

        if (!dict.hasPrefix(word)) {
            return;
        }

        if (word.length() > 2 && dict.contains(word)) {
            validWords.add(word);
        }

        marked[row][col] = true;

        // visit to <= 8 neighbours
        for (int i = -1; i <= 1; i++) {
            for (int j = -1; j <= 1; j++) {
                if (i == 0 && j == 0) {
                    continue;
                }

                if ((row + i >= 0) && (row + i < board.rows()) && (col + j >= 0) && (col + j < board.cols())) {
                    collect(board, row + i, col + j, marked, validWords, word);
                }
            }
        }

        // set marked to false since we go from one cell a lot of times with different paths
        marked[row][col] = false;

    }

    // Returns the score of the given word if it is in the dictionary, zero otherwise.
    // (You can assume the word contains only the uppercase letters A through Z.)
    public int scoreOf(String word) {
        if (dict.contains(word)) {
            switch (word.length()) {
                case 0:
                case 1:
                case 2:
                    return 0;
                case 3:
                case 4:
                    return 1;
                case 5:
                    return 2;
                case 6:
                    return 3;
                case 7:
                    return 5;
                default:
                    return 11;
            }
        } else {
            return 0;
        }
    }

    public static void main(String[] args) {
        In in = new In("Boggle/src/main/resources/dictionary-algs4.txt");
        String[] dictionary = in.readAllStrings();
        BoggleSolver solver = new BoggleSolver(dictionary);
        BoggleBoard board = new BoggleBoard("Boggle/src/main/resources/board-q.txt");
        int score = 0;
        for (String word : solver.getAllValidWords(board)) {
            StdOut.println(word);
            score += solver.scoreOf(word);
        }
        StdOut.println("Score = " + score);
    }
}
