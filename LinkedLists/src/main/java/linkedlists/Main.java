package linkedlists;

import java.util.*;

public class Main {

    public static void main(String[] args) {
        System.out.println("----- removeDuplicatesWithBuffer -----");
        LinkedList<Integer> list = new LinkedList<>(Arrays.asList(1, 1, 2, 2, 3, 4, 5, 6, 6, 6, 6));
        removeDuplicatesWithBuffer(list);
        System.out.println(list);

        System.out.println("----- getKthToLast -----");
        list = new LinkedList<>(Arrays.asList(1, 1, 2, 2, 3, 4, 5, 6, 6, 6, 6));
        System.out.println(getKthToLast(list, 10));

        System.out.println("----- addLists -----");
        list = new LinkedList<>(Arrays.asList(1, 9, 9));
        LinkedList<Integer> list2 = new LinkedList<>(Arrays.asList(0, 9, 9));
        System.out.println(addLists(list, list2));


    }

    /*
    * Remove Dups! Write code to remove duplicates from an unsorted linked list.
    * */
    private static void removeDuplicatesWithBuffer(LinkedList<Integer> linkedList) {
        Iterator<Integer> it = linkedList.iterator();
        HashSet<Integer> values = new HashSet<>();
        while (it.hasNext()) {
            Integer current = it.next();
            if (values.contains(current)) {
                it.remove();
            } else {
                values.add(current);
            }
        }
    }

    /*
    * Return Kth to Last: Implement an algorithm to find the kth to last element of a singly linked list.
    * */
    private static Integer getKthToLast(LinkedList<Integer> linkedList, int k) {
        Iterator<Integer> p1 = linkedList.iterator();
        Iterator<Integer> p2 = linkedList.iterator();
        for (int i = 0; i < k; i++) {
            p2.next();
        }

        while (p2.hasNext()) {
            p2.next();
            p1.next();
        }

        return p1.next();
    }

    /*
    * Sum Lists: You have two numbers represented by a linked list, where each node contains a single
                digit. The digits are stored in reverse order, such that the 1 's digit is at the head of the list. Write a
                function that adds the two numbers and returns the sum as a linked list.
    * */
    private static LinkedList<Integer> addLists(LinkedList<Integer> n1, LinkedList<Integer> n2) {
        LinkedList<Integer> result = new LinkedList<>();
        carry(n1.iterator(), n2.iterator(), result);
        return result;
    }

    private static int carry(Iterator<Integer> n1, Iterator<Integer> n2, LinkedList<Integer> result) {
        if (!n1.hasNext()) return 0;
        Integer val1 = n1.next();
        Integer val2 = n2.next();

        int sum = val1 + val2 + carry(n1, n2, result);
        int digitToWrite = sum % 10;
        result.push(digitToWrite);
        return sum > 10 ? 1: 0;
    }

}
