public class Main {

    public static void main(String[] args) {
        System.out.println("----- isUniqueChars -----");
        System.out.println("isUniqueChars(abcd)/isUniqueChars(abbc): "
                + isUniqueChars("abc") + "/" + isUniqueChars("abbc"));
        System.out.println("isUniqueCharsForLowerCaseEnglish(abcd)/isUniqueCharsForLowerCaseEnglish(abbc): "
                + isUniqueCharsForLowerCaseEnglish("abc") + "/" + isUniqueCharsForLowerCaseEnglish("abbc"));

        System.out.println("----- checkPermutation -----");
        System.out.println("checkPermutation(abbc, bbca): " + checkPermutation("abbc", "bbca"));
        System.out.println("checkPermutation(abbc, aabc): " + checkPermutation("abbc", "aabc"));
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

    /*
    * Check Permutation: Given two strings, write a method to decide if one is a permutation of the other.
    */
    private static boolean checkPermutation(String str1, String str2) {
        if (str1 == null || str2 == null) {
            return false;
        }

        if (str1.length() != str2.length()) {
            return false;
        }

        int[] charset = new int[256];

        for (int i = 0; i < str1.length(); i++) {
            int intCh1 = str1.charAt(i) - 'a';
            int intCh2 = str2.charAt(i) - 'a';

            charset[intCh1]++;
            charset[intCh2]--;
        }

        for (int i = 0; i < str1.length(); i++) {
            if (charset[i] != 0) {
                return false;
            }
        }

        return true;
    }
}
