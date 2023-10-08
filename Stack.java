//Description: This is a generic stack class.
//Author: Amar Sahbazovic
//Date: 10/3/2023

public class Stack<T> {
	private class Node<T> {
		private T item;
		private Node<T> next;

		public Node() {
			item = null;
			next = null;
		}

		public Node(T newItem, Node<T> newNext) {
			item = newItem;
			next = newNext;
		}
	} // End of Node inner class

	private Node<T> head;
	int size;

	// Create an empty stack.
	public Stack() {

	}

	// This method pushes an item onto the stack.
	public void push(T itemName) {
		Node<T> newNode = new Node<>(itemName, head);
		head = newNode;
		size++;
	}

	// This method pops/removes the top item from the stack and returns the value
	// of the popped item.
	public T pop() {
		if (head == null) {
			return null;
		}

		T info = head.item;
		head = head.next;
		size--;
		return info;
	}

	// This method returns the "top" item on the stack without popping it.
	public T top() {
		if(head == null) {
			return null;
		}
		return head.item;
	}

	// This method returns the number of elements in the stack.
	public int size() {
		return size;
	}

	// This method check if the stack has no elements or is empty.
	public boolean isEmpty() {
		return size == 0;
	}

	// This method makes the stack empty.
	public void makeEmpty() {
		head = null;
		size = 0;
	}

}