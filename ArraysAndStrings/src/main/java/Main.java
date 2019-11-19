public class Main {

    public static void main(String[] args) {
        System.out.println("isUniqueChars(abcd)/isUniqueChars(abbc): "
                + isUniqueChars("abc") + "/" + isUniqueChars("abbc"));
        System.out.println("isUniqueCharsForLowerCaseEnglish(abcd)/isUniqueCharsForLowerCaseEnglish(abbc): "
                + isUniqueCharsForLowerCaseEnglish("abc") + "/" + isUniqueCharsForLowerCaseEnglish("abbc"));
    }

    /*
    * Is Unique: Implement an algorithm to determine if a string has all unique characters. What if you
        cannot use additional data structures?
    * */
    private static boolean isUniqueChars(String str) {
        boolean[] charSet = new boolean[256];

        for (int i = 0; i < str.length(); i++) {
            char ch = str.charAt(i);
            if (charSet[ch]) {
                return false;
            }
            charSet[ch] = true;
        }

        return true;
    }

    private static boolean isUniqueCharsForLowerCaseEnglish(String str) {

        int bitVector = 0;
        for (int i = 0; i < str.length(); i++) {
            int intCh = str.charAt(i) - 'a';
            if ((bitVector & (1 << intCh)) > 0) {
                return false;
            }
            bitVector |= 1 << intCh;
        }

        return true;
    }
}
