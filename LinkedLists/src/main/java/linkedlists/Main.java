package linkedlists;

import java.util.*;

public class Main {

    public static void main(String[] args) {
        System.out.println("----- removeDuplicatesWithBuffer -----");
        LinkedList<Integer> list = new LinkedList<>(Arrays.asList(1, 1, 2, 2, 3, 4, 5, 6, 6, 6, 6));
        removeDuplicatesWithBuffer(list);
        System.out.println(list);

    }

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

}
