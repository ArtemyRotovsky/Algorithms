package stacksandqueues;

import java.util.Arrays;
import java.util.EmptyStackException;

/*
 * Three in One: Describe how you could use a single array to implement three stacks.
 * */
public class MultiStack<T> {

    private int stacksCount;
    private int stackSize;

    private T[] values;
    private int[] capacities;

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        Arrays.stream(values)
                .forEach(val -> sb.append(val + ", "));
        return sb.toString();
    }

    MultiStack(int stacksCount, int stackSize) {
        this.stacksCount = stacksCount;
        this.stackSize = stackSize;

        //noinspection unchecked
        values = (T[]) new Object[stacksCount * stackSize];
        capacities = new int[stacksCount];
    }

    public void push(int stackNum, T value) {
        assertBoundaries(stackNum);

        if (isFull(stackNum)) {
            throw new FullStackException();
        }

        capacities[stackNum]++;
        int topIndex = getTopIndex(stackNum);
        values[topIndex] = value;
    }


    @SuppressWarnings({"UnusedReturnValue", "WeakerAccess"})
    public T pop(int stackNum) {
        assertBoundaries(stackNum);

        if (isEmpty(stackNum)) {
            throw  new EmptyStackException();
        }

        int topIndex = getTopIndex(stackNum);
        capacities[stackNum]--;
        T value = values[topIndex];
        values[topIndex] = null;
        return value;
    }

    public T peek(int stackNum) {
        assertBoundaries(stackNum);

        if (isEmpty(stackNum)) {
            throw  new EmptyStackException();
        }

        return values[getTopIndex(stackNum)];
    }

    private void assertBoundaries(int stackNum) {
        if (stackNum > stacksCount - 1 || stackNum < 0) {
            throw new IllegalArgumentException();
        }
    }

    private boolean isEmpty(int stackNum) {
        return capacities[stackNum] == 0;
    }

    private boolean isFull(int stackNum) {
        return capacities[stackNum] == stackSize;
    }


    private int getTopIndex(int stackNum) {
        return stackNum * stackSize + capacities[stackNum] - 1;
    }

    private static class FullStackException extends RuntimeException {}
}

