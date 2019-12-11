package stacksandqueues;

public class Main {

    public static void main(String[] args) {
        MultiStack<Integer> ms = new MultiStack<>(3, 3);
        ms.push(0, 0);
        ms.push(0, 1);
        ms.push(0, 2);
        ms.pop(0);
        ms.push(0, 3);
        System.out.println(ms);

        ms.push(1, 5);
        ms.push(1, 6);
        System.out.println(ms);

        ms.push(2, 7);
        ms.push(2, 8);
        ms.push(2, 9);
        System.out.println(ms);

        ms.pop(2);
        ms.pop(2);
        System.out.println(ms);

        System.out.println(ms.peek(0));
        System.out.println(ms.peek(1));
        System.out.println(ms.peek(2));

    }


}
