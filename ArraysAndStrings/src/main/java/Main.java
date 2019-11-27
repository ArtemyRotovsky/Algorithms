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

        System.out.println("----- URLify -----");
        System.out.println("URLify(\"Mr John Smith    \", 13): " + URLify("Mr John Smith    ".toCharArray(), 13));

        System.out.println("----- isPalindromPremutation -----");
        System.out.println("isPalindromPremutation(Tact Coa): " + isPalindromPremutation("Tact Coa"));
        System.out.println("isPalindromPremutation(Tactcasda Coa): " + isPalindromPremutation("Tactcasda Coa"));

        System.out.println("----- isOneAwayEdit -----");
        System.out.println("isOneAwayEdit(pale, ple): " + isOneAwayEdit("pale", "ple"));
        System.out.println("isOneAwayEdit(pales, pale): " + isOneAwayEdit("pales", "pale"));
        System.out.println("isOneAwayEdit(pale, bale): " + isOneAwayEdit("pale", "bale"));
        System.out.println("isOneAwayEdit(pale, bake): " + isOneAwayEdit("pale", "bake"));
        System.out.println("isOneAwayEdit(pale, ale): " + isOneAwayEdit("pale", "ale"));
        System.out.println("isOneAwayEdit(ale, akes): " + isOneAwayEdit("ale", "akes"));

        System.out.println("----- compress -----");
        System.out.println("compress(aabcccccaaa): " + compress("aabcccccaaa"));
        System.out.println("compress(abc): " + compress("abc"));
        System.out.println("compress(abcabbbbbbb): " + compress("abcabbbbbbb"));



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

    /*
    * URLify: Write a method to replace all spaces in a string with '%20'. You may assume that the string
                has sufficient space at the end to hold the additional characters, and that you are given the "true"
                length of the string. (Note: If implementing in Java, please use a character array so that you can
                perform this operation in place.)
                EXAMPLE
                Input: "Mr John Smith ", 13
                Output: "Mr%20John%20Smith"
    * */
    private static String URLify(char[] s, int effectiveLength) {

        int spaceCount = 0;
        for (int i = 0; i < effectiveLength; i++) {
            if (s[i] == ' ') {
                spaceCount++;
            }
        }

        int requiredLength = effectiveLength + 2 * spaceCount; // not 3 because a space takes 1 slot already
        int index = requiredLength - 1;

        for (int i = effectiveLength - 1; i >= 0 ; i--) {
            if (s[i] == ' ') {
                s[index] = '0';
                s[index - 1] = '2';
                s[index - 2] = '%';
                index -= 3;
            } else {
                s[index] = s[i];
                index--;
            }
        }

        return String.valueOf(s);
    }

    /*
    * Palindrome Permutation: Given a string, write a function to check if it is a permutation of a palindrome.
                A palindrome is a word or phrase that is the same forwards and backwards. A permutation
                is a rearrangement of letters. The palindrome does not need to be limited to just dictionary words.
                1.5
                1.6
                EXAMPLE
                Input: Tact Coa
                Output: True (permutations: "taco cat", "atco eta", etc.)
    * */
    private static boolean isPalindromPremutation(String s) {

        int[] charset = new int[256];

        for (int i = 0; i < s.length(); i++) {

            int intCh = Character.getNumericValue(s.charAt(i)) - Character.getNumericValue('a');

            if (intCh < 0) {
                continue;
            }

            if (charset[intCh] > 0) {
                charset[intCh]--;
            } else {
                charset[intCh]++;
            }
        }

        int counter = 0;
        for (int i = 0; i < s.length(); i++) {
            counter += charset[i];
            if (counter > 1) {
                return false;
            }
        }

        return true;
    }

    /*
    * One Away: There are three types of edits that can be performed on strings: insert a character,
                remove a character, or replace a character. Given two strings, write a function to check if they are
                one edit (or zero edits) away.
                EXAMPLE
                pale, ple -> true
                pales, pale -> true
                pale, bale -> true
                pale, bake -> false
    * */
    private static boolean isOneAwayEdit(String s, String t) {
        int diffCounter = 0;
        int index1 = 0, index2 = 0;

        String shortStr = s.length() < t.length() ? s : t;
        String longStr = s.length() < t.length() ? t : s;

        while (index2 < longStr.length() && index1 < shortStr.length()) {
            char c1 = shortStr.charAt(index1);
            char c2 = longStr.charAt(index2);

            if (c1 == c2) {
                index1++; index2++;
                continue;
            } else {
                diffCounter++;
                if (shortStr.length() == longStr.length()) index1++;
            }
            index2++;

            if (diffCounter > 1) return false;
        }

        return true;
    }

    /*
    * String Compression: Implement a method to perform basic string compression using the counts
                of repeated characters. For example, the string aabcccccaaa would become a2blc5a3. If the
                "compressed" string would not become smaller than the original string, your method should return
                the original string. You can assume the string has only uppercase and lowercase letters (a - z).
    * */
    private static String compress(String s) {
        StringBuilder compressed = new StringBuilder();

        int counter = 0;
        for (int i = 0; i < s.length(); i++) {
            counter++;
            char curChar = s.charAt(i);
            if ((i + 1) >= s.length() || curChar != s.charAt(i + 1)) {
                compressed.append(curChar);
                compressed.append(counter);
                counter = 0;
            }
        }

        return s.length() > compressed.length() ? compressed.toString() : s;
    }


}
