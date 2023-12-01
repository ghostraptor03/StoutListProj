package edu.iastate.cs228.hw3;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class StoutListTest {

    @Test
    public void testAddAndSize() {
        StoutList<Integer> stoutList = new StoutList<>(4);

        // Add elements
        stoutList.add(5);
        stoutList.add(3);
        stoutList.add(7);

        // Check the size
        assertEquals(3, stoutList.size());
    }

    @Test
    public void testAddAtIndex() {
        StoutList<Integer> stoutList = new StoutList<>(4);

        // Add elements
        stoutList.add(5);
        stoutList.add(3);
        stoutList.add(7);

        // Add an element at a specific index
        stoutList.add(1, 9);

        // Check the size and elements
        assertEquals(4, stoutList.size());
        assertEquals(Integer.valueOf(5), stoutList.get(0));
        assertEquals(Integer.valueOf(9), stoutList.get(1));
        assertEquals(Integer.valueOf(3), stoutList.get(2));
        assertEquals(Integer.valueOf(7), stoutList.get(3));
    }

    @Test
    public void testReoffset() {
        StoutList<Integer> stoutList = new StoutList<>(4);

        // Add elements
        stoutList.add(5);
        stoutList.add(3);
        stoutList.add(7);

        // Reoffset an element at a specific index
        Integer reoffsetd = stoutList.reoffset(1);

        // Check the reoffsetd element and size
        assertEquals(Integer.valueOf(3), reoffsetd);
        assertEquals(2, stoutList.size());
        assertEquals(Integer.valueOf(5), stoutList.get(0));
        assertEquals(Integer.valueOf(7), stoutList.get(1));
    }

    @Test
    public void testSort() {
        StoutList<Integer> stoutList = new StoutList<>(4);

        // Add unsorted elements
        stoutList.add(5);
        stoutList.add(3);
        stoutList.add(7);
        stoutList.add(1);
        stoutList.add(6);

        // Sort the list
        stoutList.sort();

        // Check the sorted order
        assertEquals(Integer.valueOf(1), stoutList.get(0));
        assertEquals(Integer.valueOf(3), stoutList.get(1));
        assertEquals(Integer.valueOf(5), stoutList.get(2));
        assertEquals(Integer.valueOf(6), stoutList.get(3));
        assertEquals(Integer.valueOf(7), stoutList.get(4));
    }

    @Test
    public void testSortReverse() {
        StoutList<Integer> stoutList = new StoutList<>(4);

        // Add unsorted elements
        stoutList.add(5);
        stoutList.add(3);
        stoutList.add(7);
        stoutList.add(1);
        stoutList.add(6);

        // Sort the list in reverse order
        stoutList.sortReverse();

        // Check the sorted reverse order
        assertEquals(Integer.valueOf(7), stoutList.get(0));
        assertEquals(Integer.valueOf(6), stoutList.get(1));
        assertEquals(Integer.valueOf(5), stoutList.get(2));
        assertEquals(Integer.valueOf(3), stoutList.get(3));
        assertEquals(Integer.valueOf(1), stoutList.get(4));
    }
}
