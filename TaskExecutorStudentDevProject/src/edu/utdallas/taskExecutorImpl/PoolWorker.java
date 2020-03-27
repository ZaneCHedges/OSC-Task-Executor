// Package Containing the Implementations of the Task Executor, Blocking FIFO, and Pool Workers
package edu.utdallas.taskExecutorImpl;

// Needs Tasks to execute them
import edu.utdallas.taskExecutor.Task;

//The original version utilized the ArrayBlockingQueue instead of the custom Blocking FIFO
import java.util.concurrent.ArrayBlockingQueue;

// Class is a Thread which executes Tasks
public class PoolWorker extends Thread {
	
	// The FIFO passed by TaskExecutorImpl
	private BlockingFIFO BFifo;
	private ArrayBlockingQueue<Task> ABQFifo;
	
	// Which FIFO to utilize
	private Boolean ABQ;
	
	// Parameterized overloading of Thread.start which passes a BlockingFIFO
	public void start(BlockingFIFO fifo) {
		BFifo = fifo;
		ABQ = false;
		// Call the unparameterized start method from Thread
		super.start();
	}
	
	// Parameterized overloading of Thread.start which passes an ArrayBlockingQueue
	public void start(ArrayBlockingQueue<Task> fifo) {
		ABQFifo = fifo;
		ABQ = true;
		// Call the unparameterized start method from Thread
		super.start();
	}
	
	// The method being performed by the Thread after being started
	public void run() {
		// Infinite Loop
		while(true) {
			try {
				// Fetch (take) a Task from the FIFO being used
				Task nextTask; 
				if(!ABQ)
					nextTask = BFifo.take();
				else
					nextTask = ABQFifo.take();
				// Execute said task
				nextTask.execute();
			} 
			catch (InterruptedException e) {
				System.out.println("READ FROM FIFO FAILED");
			}
		}
	}
}
