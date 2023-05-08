/*
 * add, isFull, isEmpty, and remove methods are from, either whole
 * or in part, ICS-440 2/13/23 lecture by Professor Paul Hyde. The 
 * lecture was on multi-threading on a shared circular array. 
 */

public class CircularBuffer {
	
	private final int[] buffer;
	private int head;
	private int tail;
	private int count;
	
	public CircularBuffer() {
		buffer = new int[5];
		head = 0;
		tail = 0;
		count = 0;
	}
	
	//adds the integer passed to the circular array
	public synchronized boolean add(int num) {
		if(isFull()) {
			return false;
		}
		buffer[tail] = num;
		tail = (tail + 1) % buffer.length;
		count++;
		return true;
	}
	
	//returns true/false if the circular array is full
	public synchronized boolean isFull() {
		return count == buffer.length;
	}
	
	//returns true/false if the circular array is empty
	public synchronized boolean isEmpty() {
		return count == 0;
	}	
	
	//returns the next integer to be removed
	public synchronized int read() {
		return buffer[head];
	}
		
	//removes the next integer in the circular array
	public synchronized boolean remove() {
		if (isEmpty()) {
			return false;
		}
		head = (head + 1) % buffer.length;
		count--;
		return true;
	}	
}
