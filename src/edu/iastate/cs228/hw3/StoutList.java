package edu.iastate.cs228.hw3;

import java.util.AbstractSequentialList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Iterator;
import java.util.ListIterator;
import java.util.NoSuchElementException;

/**
 * Implementation of the list interface based on linked nodes that store
 * multiple items per node. Rules for adding and removing elements ensure that
 * each node (except possibly the last one) is at least half full.
 */
public class StoutList<E extends Comparable<? super E>> extends AbstractSequentialList<E> {
	/**
	 * Default number of elements that may be stored in each node.
	 */
	private static final int DEFAULT_NODESIZE = 4;

	/**
	 * Number of elements that can be stored in each node.
	 */
	private final int nodeSize;

	/**
	 * Dummy node for head. It should be private but set to public here only for
	 * grading purpose. In practice, you should always make the head of a linked
	 * list a private instance variable.
	 */
	public Node head;

	/**
	 * Dummy node for tail.
	 */
	private Node tail;

	/**
	 * Number of elements in the list.
	 */
	private int size;

	/**
	 * Constructs an empty list with the default node size.
	 */
	public StoutList() {
		this(DEFAULT_NODESIZE);
	}

	/**
	 * Constructs an empty list with the given node size.
	 * 
	 * @param nodeSize number of elements that may be stored in each node, must be
	 *                 an even number
	 */
	public StoutList(int nodeSize) {
		if (nodeSize <= 0 || nodeSize % 2 != 0)
			throw new IllegalArgumentException();

		// dummy nodes
		head = new Node();
		tail = new Node();
		head.next = tail;
		tail.previous = head;
		this.nodeSize = nodeSize;
	}

	/**
	 * Constructor for grading only. Fully implemented.
	 * 
	 * @param head
	 * @param tail
	 * @param nodeSize
	 * @param size
	 */
	public StoutList(Node head, Node tail, int nodeSize, int size) {
		this.head = head;
		this.tail = tail;
		this.nodeSize = nodeSize;
		this.size = size;
	}

	// returns the size
	@Override
	public int size() {
		return size;
	}

	// adds item to the end of stout list
	@Override
	public boolean add(E item) {
		if (item == null) {
			throw new NullPointerException();
		}

		if (contains(item))
			return false;
		if (size == 0) {
			Node n = new Node();
			n.addItem(item);
			head.next = n;
			n.previous = head;
			n.next = tail;
			tail.previous = n;
		} else {
			if (tail.previous.count < nodeSize) {
				tail.previous.addItem(item);
			} else {
				Node n = new Node();
				n.addItem(item);
				Node thing = tail.previous;
				thing.next = n;
				n.previous = thing;
				n.next = tail;
				tail.previous = n;
			}
		}
		size++;
		return true;
	}

// helper method
	public boolean contains(E item) {
		if (size < 1)
			return false;
		Node thing = head.next;
		while (thing != tail) {
			for (int i = 0; i < thing.count; i++) {
				if (thing.data[i].equals(item))
					return true;
				thing = thing.next;
			}
		}
		return false;
	}

	// adds item to the Stout List at a specific location
	@Override
	public void add(int pos, E item) {
		if (pos < 0 || pos > size)
			throw new IndexOutOfBoundsException("Invalid position");

		if (head.next == tail)
			add(item);

		NodeInfo nodeInfo = find(pos);
		Node thing = nodeInfo.node;
		int offset = nodeInfo.offset;
		if (offset == 0) {
			if (thing.previous.count < nodeSize && thing.previous != head) {
				thing.previous.addItem(item);
				size++;
				return;
			} else if (thing == tail) {
				add(item);
				size++;
				return;
			}
		}
		if (thing.count < nodeSize) {
			thing.addItem(offset, item);
		} else {
			Node newSuccesor = new Node();
			int halfPoint = nodeSize / 2;
			int count = 0;
			while (count < halfPoint) {
				newSuccesor.addItem(thing.data[halfPoint]);
				thing.reoffsetItem(halfPoint);
				count++;
			}

			Node oldSuccesor = thing.next;

			thing.next = newSuccesor;
			newSuccesor.previous = thing;
			newSuccesor.next = oldSuccesor;
			oldSuccesor.previous = newSuccesor;

			if (offset <= nodeSize / 2) {
				thing.addItem(offset, item);
			}
			if (offset > nodeSize / 2) {
				newSuccesor.addItem((offset - nodeSize / 2), item);
			}

		}
		size++;
	}

	public E reoffset(int pos) {
		if (pos < 0 || pos > size)
			throw new IndexOutOfBoundsException("Invalid position");
		NodeInfo nodeInfo = find(pos);
		Node thing = nodeInfo.node;
		int offset = nodeInfo.offset;
		E nodeValue = thing.data[offset];

		if (thing.next == tail && thing.count == 1) {
			Node predecessor = thing.previous;
			predecessor.next = thing.next;
			thing.next.previous = predecessor;
			thing = null;
		} else if (thing.next == tail || thing.count > nodeSize / 2) {
			thing.reoffsetItem(offset);
		} else {
			thing.reoffsetItem(offset);
			Node succesor = thing.next;

			if (succesor.count > nodeSize / 2) {
				thing.addItem(succesor.data[0]);
				succesor.reoffsetItem(0);
			} else if (succesor.count <= nodeSize / 2) {
				for (int i = 0; i < succesor.count; i++) {
					thing.addItem(succesor.data[i]);
				}
				thing.next = succesor.next;
				succesor.next.previous = thing;
				succesor = null;
			}
		}
		size--;
		return nodeValue;
	}

	@Override
	public E remove(int pos) {
		if (pos < 0 || pos > size)
			throw new IndexOutOfBoundsException();
		NodeInfo nodeInfo = find(pos);
		Node temp = nodeInfo.node;
		int offset = nodeInfo.offset;
		E nodeValue = temp.data[offset];

		if (temp.next == tail && temp.count == 1) {
			Node last = temp.previous;
			last.next = temp.next;
			temp.next.previous = last;
			temp = null;
		} 
		else if (temp.next == tail || temp.count > nodeSize / 2) {
			temp.removeItem(offset);
		}
		else {
			temp.removeItem(offset);
			Node next = temp.next;
			
			if (next.count > nodeSize / 2) {
				temp.addItem(next.data[0]);
				next.removeItem(0);
			}
			else if (next.count <= nodeSize / 2) {
				for (int i = 0; i < next.count; i++) {
					temp.addItem(next.data[i]);
				}
				temp.next = next.next;
				next.next.previous = temp;
				next = null;
			}
		}
		size--;
		return nodeValue;
	}

//Helper method to find a node by its index
	private Node findNodeByIndex(int index) {
		if (index < 0 || index >= size)
			throw new IndexOutOfBoundsException("Invalid index");

		Node currentNode = head.next;
		int currentIndex = 0;

		while (currentIndex < index) {
			currentNode = currentNode.next;
			currentIndex++;
		}

		return currentNode;
	}

	// Helper method to link a new node
	private void link(Node predecessor, Node newNode) {
		newNode.next = predecessor.next;
		newNode.previous = predecessor;
		predecessor.next.previous = newNode;
		predecessor.next = newNode;
	}

	// Helper method to unlink a node
	private void unlink(Node node) {
		node.previous.next = node.next;
		node.next.previous = node.previous;
		node.next = null;
		node.previous = null;
		size--;
	}

	/**
	 * Sort all elements in the stout list in the NON-DECREASING order. You may do
	 * the following. Traverse the list and copy its elements into an array,
	 * deleting every visited node along the way. Then, sort the array by calling
	 * the insertionSort() method. (Note that sorting efficiency is not a concern
	 * for this project.) Finally, copy all elements from the array back to the
	 * stout list, creating new nodes for storage. After sorting, all nodes but
	 * (possibly) the last one must be full of elements.
	 * 
	 * Comparator<E> must have been implemented for calling insertionSort().
	 */
	public void sort() {
		E[] sortlistData = (E[]) new Comparable[size];

		int thingIndex = 0;
		Node thing = head.next;
		while (thing != tail) {
			for (int i = 0; i < thing.count; i++) {
				sortlistData[thingIndex] = thing.data[i];
				thingIndex++;
			}
			thing = thing.next;
		}

		head.next = tail;
		tail.previous = head;

		insertionSort(sortlistData, new ElementComparator());
		size = 0;
		for (int i = 0; i < sortlistData.length; i++) {
			add(sortlistData[i]);
		}

	}

	/**
	 * Sort all elements in the stout list in the NON-INCREASING order. Call the
	 * bubbleSort() method. After sorting, all but (possibly) the last nodes must be
	 * filled with elements.
	 * 
	 * Comparable<? super E> must be implemented for calling bubbleSort().
	 */
	public void sortReverse() {
		E[] listData = (E[]) new Comparable[size];

		int thingIndex = 0;
		Node thing = head.next;
		while (thing != tail) {
			for (int i = 0; i < thing.count; i++) {
				listData[thingIndex] = thing.data[i];
				thingIndex++;
			}
			thing = thing.next;
		}

		head.next = tail;
		tail.previous = head;

		bubbleSort(listData);
		size = 0;
		for (int i = 0; i < listData.length; i++) {
			add(listData[i]);
		}
	}

	@Override
	public Iterator<E> iterator() {
		return new StoutListIterator();
	}

	@Override
	public ListIterator<E> listIterator() {
		return new StoutListIterator();
	}

	@Override
	public ListIterator<E> listIterator(int index) {
		return new StoutListIterator(index);
	}

	/**
	 * Returns a string representation of this list showing the internal structure
	 * of the nodes.
	 */
	public String toStringInternal() {
		return toStringInternal(null);
	}

	/**
	 * Returns a string representation of this list showing the internal structure
	 * of the nodes and the position of the iterator.
	 *
	 * @param iter an iterator for this list
	 */
	public String toStringInternal(ListIterator<E> iter) {
		int count = 0;
		int position = -1;
		if (iter != null) {
			position = iter.nextIndex();
		}

		StringBuilder sb = new StringBuilder();
		sb.append('[');
		Node current = head.next;
		while (current != tail) {
			sb.append('(');
			E data = current.data[0];
			if (data == null) {
				sb.append("-");
			} else {
				if (position == count) {
					sb.append("| ");
					position = -1;
				}
				sb.append(data.toString());
				++count;
			}

			for (int i = 1; i < nodeSize; ++i) {
				sb.append(", ");
				data = current.data[i];
				if (data == null) {
					sb.append("-");
				} else {
					if (position == count) {
						sb.append("| ");
						position = -1;
					}
					sb.append(data.toString());
					++count;

					// iterator at end
					if (position == size && count == size) {
						sb.append(" |");
						position = -1;
					}
				}
			}
			sb.append(')');
			current = current.next;
			if (current != tail)
				sb.append(", ");
		}
		sb.append("]");
		return sb.toString();
	}

	/**
	 * Node type for this list. Each node holds a maximum of nodeSize elements in an
	 * array. Empty slots are null.
	 */
	private class Node {
		public E item;

		/**
		 * Array of actual data elements.
		 */
		// Unchecked warning unavoidable.
		public E[] data = (E[]) new Comparable[nodeSize];

		/**
		 * Link to next node.
		 */
		public Node next;

		/**
		 * Link to previous node;
		 */
		public Node previous;

		/**
		 * Index of the next available offset in this node, also equal to the number of
		 * elements in this node.
		 */
		public int count;

		/**
		 * Adds an item to this node at the first available offset. Precondition: count
		 * < nodeSize
		 * 
		 * @param item element to be added
		 */
		void addItem(E item) {
			if (count >= nodeSize) {
				return;
			}
			data[count++] = item;
			// useful for debugging
			// System.out.println("Added " + item.toString() + " at index " + count + " to
			// node " + Arrays.toString(data));
		}

		/**
		 * Adds an item to this node at the indicated offset, shifting elements to the
		 * right as necessary.
		 * 
		 * Precondition: count < nodeSize
		 * 
		 * @param offset array index at which to put the new element
		 * @param item   element to be added
		 */
		void addItem(int offset, E item) {
			if (count >= nodeSize) {
				return;
			}
			for (int i = count - 1; i >= offset; --i) {
				data[i + 1] = data[i];
			}
			++count;
			data[offset] = item;
			// useful for debugging
//      System.out.println("Added " + item.toString() + " at index " + offset + " to node: "  + Arrays.toString(data));
		}
		void removeItem(int offset) {
			E item = data[offset];
			for (int i = offset + 1; i < nodeSize; ++i) {
				data[i - 1] = data[i];
			}
			data[count - 1] = null;
			--count;
		}

		/**
		 * Deletes an element from this node at the indicated offset, shifting elements
		 * left as necessary. Precondition: 0 <= offset < count
		 * 
		 * @param offset
		 */
		void reoffsetItem(int offset) {
			E item = data[offset];
			for (int i = offset + 1; i < nodeSize; ++i) {
				data[i - 1] = data[i];
			}
			data[count - 1] = null;
			--count;
		}
	}

	private class NodeInfo {
		public Node node;
		public int offset;

		public NodeInfo(Node node, int offset) {
			this.node = node;
			this.offset = offset;
		}
	}

	private NodeInfo find(int pos) {
		Node thing = head.next;
		int currPos = 0;
		while (thing != tail) {
			if (currPos + thing.count <= pos) {
				currPos += thing.count;
				thing = thing.next;
				continue;
			}

			NodeInfo nodeInfo = new NodeInfo(thing, pos - currPos);
			return nodeInfo;

		}
		return null;
	}

	private class StoutListIterator implements ListIterator<E> {
		// constants you possibly use ...
		private int index;
		private Node current;
		// instance variables ...
		private int last = 0;
		private int next = 1;

		int currentPosition;

		public E[] listData;

		int lastOccured;

		/**
		 * Default constructor Sets the pointer of iterator to the beginning of the list
		 */
		public StoutListIterator() {
			currentPosition = 0;
			lastOccured = -1;
			setup();
		}

		/**
		 * Constructor finds node at a given position
		 * 
		 * 
		 * @param pos
		 */
		public StoutListIterator(int pos) {
			// TODO
			currentPosition = pos;
			lastOccured = -1;
			setup();
		}

		private void setup() {
			listData = (E[]) new Comparable[size];

			int thingIndex = 0;
			Node thing = head.next;
			while (thing != tail) {
				for (int i = 0; i < thing.count; i++) {
					listData[thingIndex] = thing.data[i];
					thingIndex++;
				}
				thing = thing.next;
			}
		}

		// returns true if index is less than size
		@Override
		public boolean hasNext() {
			if (currentPosition >= size)
				return false;
			else
				return true;
		}
		// will remove the current node
		@Override
		public void remove() {
			if (index <= 0) {
				throw new IllegalStateException("Remove not allowed before calling next.");
			}
			Node prevNode = current.previous;
			unlink(current);
			current = prevNode;
			index--;
		}
		// will check if there is a next element and throws an error if not
		@Override
		public E next() {
			if (!hasNext())
				throw new NoSuchElementException();
			lastOccured = next;
			return listData[currentPosition++];
		}

		public void reoffset() {
			if (lastOccured == next) {
				StoutList.this.reoffset(currentPosition - 1);
				setup();
				lastOccured = -1;
				currentPosition--;
				if (currentPosition < 0)
					currentPosition = 0;
			} else if (lastOccured == last) {
				StoutList.this.reoffset(currentPosition);
				setup();
				lastOccured = -1;
			} else {
				throw new IllegalStateException();
			}
		}

		public boolean hasPrevious() {
			if (currentPosition <= 0)
				return false;
			else
				return true;
		}

		public int nextIndex() {
			return currentPosition;
		}

		@Override
		public E previous() {
			if (!hasPrevious())
				throw new NoSuchElementException();
			lastOccured = last;
			currentPosition--;
			return listData[currentPosition];
		}

		public int previousIndex() {
			return currentPosition - 1;
		}

		@Override
		public void set(E arg0) {
			if (lastOccured == next) {
				NodeInfo nodeInfo = find(currentPosition - 1);
				nodeInfo.node.data[nodeInfo.offset] = arg0;
				listData[currentPosition - 1] = arg0;
			} else if (lastOccured == last) {
				NodeInfo nodeInfo = find(currentPosition);
				nodeInfo.node.data[nodeInfo.offset] = arg0;
				listData[currentPosition] = arg0;
			} else {
				throw new IllegalStateException();
			}

		}

		@Override
		public void add(E arg0) {
			if (arg0 == null)
				throw new NullPointerException();

			StoutList.this.add(currentPosition, arg0);
			currentPosition++;
			setup();
			lastOccured = -1;

		}

		// Other methods you may want to add or override that could possibly facilitate
		// other operations, for instance, addition, access to the previous element,
		// etc.
		//
		// ...
		//
	}

	/**
	 * Sort an array arr[] using the insertion sort algorithm in the NON-DECREASING
	 * order.
	 * 
	 * @param arr  array storing elements from the list
	 * @param comp comparator used in sorting
	 */
	private void insertionSort(E[] arr, Comparator<? super E> comp) {
		for (int i = 1; i < arr.length; i++) {
			E key = arr[i];
			int j = i - 1;

			while (j >= 0 && comp.compare(arr[j], key) > 0) {
				arr[j + 1] = arr[j];
				j--;
			}
			arr[j + 1] = key;
		}
	}

	/**
	 * Sort arr[] using the bubble sort algorithm in the NON-INCREASING order. For a
	 * description of bubble sort please refer to Section 6.1 in the project
	 * description. You must use the compareTo() method from an implementation of
	 * the Comparable interface by the class E or ? super E.
	 * 
	 * @param arr array holding elements from the list
	 */
	private void bubbleSort(E[] arr) {
		int n = arr.length;
		for (int i = 0; i < n - 1; i++) {
			for (int j = 0; j < n - i - 1; j++) {
				if (arr[j].compareTo(arr[j + 1]) < 0) {
					E thing = arr[j];
					arr[j] = arr[j + 1];
					arr[j + 1] = thing;
				}
			}
		}

	}

	class ElementComparator<E extends Comparable<E>> implements Comparator<E> {
		@Override
		public int compare(E arg0, E arg1) {
			// TODO Auto-generated method stub
			return arg0.compareTo(arg1);
		}

	}

}