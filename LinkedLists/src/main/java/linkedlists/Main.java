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

}
