package edu.iastate.cs228.hw3;

import java.util.ListIterator;

public class test {
    public static void main(String[] args) {
        // Test 1: Create a StoutList and add elements
        StoutList<Integer> stoutList = new StoutList<>();
        stoutList.add(5);
        stoutList.add(2);
        stoutList.add(8);
        stoutList.add(1);
        stoutList.add(3);

        System.out.println("Test 1: Create a StoutList and add elements:");
        System.out.println("Stout List: " + stoutList.toStringInternal());

        // Test 2: Test sorting the StoutList
        stoutList.sort();
        System.out.println("\nTest 2: Sort the StoutList in non-decreasing order:");
        System.out.println("Sorted List: " + stoutList.toStringInternal());

        // Test 3: Test sorting the StoutList in non-increasing order
        stoutList.sortReverse();
        System.out.println("\nTest 3: Sort the StoutList in non-increasing order:");
        System.out.println("Reverse Sorted List: " + stoutList.toStringInternal());

        // Test 4: Test list iterator
        ListIterator<Integer> listIterator = stoutList.listIterator();
        System.out.println("\nTest 4: List iterator:");
        while (listIterator.hasNext()) {
            System.out.println(listIterator.next());
        }

        // Test 5: Test add at a specific position
        stoutList.add(2, 6);
        System.out.println("\nTest 5: Add an element at position 2:");
        System.out.println("Updated Stout List: " + stoutList.toStringInternal());

        // Test 6: Test removal of an element at a specific position
        int removedElement = stoutList.reoffset(1);
        System.out.println("\nTest 6: Remove the element at position 1:");
        System.out.println("Removed element: " + removedElement);
        System.out.println("Updated Stout List: " + stoutList.toStringInternal());

        // Test 7: Test list iterator with a removed element
        listIterator = stoutList.listIterator();
        System.out.println("\nTest 7: List iterator after removal:");
        while (listIterator.hasNext()) {
            System.out.println(listIterator.next());
        }

        // Test 8: Test empty list
        StoutList<Integer> emptyStoutList = new StoutList<>();
        System.out.println("\nTest 8: Test empty list:");
        System.out.println("Empty list hasNext: " + emptyStoutList.listIterator().hasNext());
       
		
    }
}
