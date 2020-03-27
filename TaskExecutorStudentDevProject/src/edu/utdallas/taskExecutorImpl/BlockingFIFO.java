// Package Containing the Implementations of the Task Executor, Blocking FIFO, and Pool Workers
package edu.utdallas.taskExecutorImpl;

// Needs Tasks to store and manage them
import edu.utdallas.taskExecutor.Task;

// Class represents a First-In, First-Out Array which Blocks inserting and retrieving Tasks from its buffer
public class BlockingFIFO {
	// The buffer which contains the Tasks
	private Task[] buffer;
	// Cursor integers for keeping track of which index is needed for the put and take methods
	private int nextin, nextout;
	// Count represents the number of Tasks currently in the buffer
	private int count;
	// Whether to print testing information
	private final boolean test = false;
	
	// Parameterized constructor sets default variables and sets size of buffer
	public BlockingFIFO(int N) {
		buffer = new Task[N];
		nextin = 0;
		nextout = 0;
		count = 0;
	}
	
	// Add a Task to the buffer
	public void put(Task task) throws InterruptedException{
		synchronized(this) {
			// Wait while the buffer is full
			while(count == buffer.length) {
				wait();
			}
			// Add the task at index next in
			count++;
			buffer[nextin] = task;
			// Update next in
			nextin = (nextin + 1) % buffer.length;
			// If the buffer was empty but is no longer, wake up the threads waiting
			if(count == 1)
				notifyAll();
			// If in debug mode, print buffer's contents
			if(test)
				print();
		}
	}
	
	// Fetch and remove a Task from the buffer
	public Task take() throws InterruptedException{
		synchronized(this) {
			// Wait while the buffer is empty
			while(count == 0) {
				wait();
			}
			// Remove the Task from the buffer at index next out
			count--;
			Task task = buffer[nextout];
			buffer[nextout] = null;
			// Update next out
			nextout = (nextout + 1) % buffer.length;
			// If the buffer was full but is no longer, wake up the threads waiting
			if(count == buffer.length - 1)
				notifyAll();
			// If in debug mode, print buffer's contents
			if(test)
				print();
			// Return the fetched Task
			return task;
		}
	}
	
	// Debug Method which prints the contents of the Buffer
	private void print() {
		for (int i = 0; i < buffer.length; i++) {
			Task task = buffer[i];
			if (task != null)
				System.out.print(task.getName() + " ");
			else
				System.out.print("EmptySlot ");
		}
		System.out.println();
	}
}
