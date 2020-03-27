// Package Containing the Implementations of the Task Executor, Blocking FIFO, and Pool Workers
package edu.utdallas.taskExecutorImpl;

// The Task Executor Implementation implements TaskExecutor, and needs Tasks
import edu.utdallas.taskExecutor.Task;
import edu.utdallas.taskExecutor.TaskExecutor;

// The original version utilized the ArrayBlockingQueue instead of the custom Blocking FIFO
import java.util.concurrent.ArrayBlockingQueue;

// Class serves to manage adding Tasks and creating Threads (PoolWorkers) to execute the Tasks
public class TaskExecutorImpl implements TaskExecutor {
	// Blocking FIFOs for storing Tasks
	private BlockingFIFO BFifo;
	private ArrayBlockingQueue<Task> ABQFifo;
	// An array of the Thread Workers, which execute Tasks
	private PoolWorker[] threadPool;
	// The size of the FIFO
	private final int fifoSize = 100;
	// Whether to use the ArrayBlockingQueue (true) or the custom BlockingFIFO (false)
	private final boolean ABQ = false;
	// Whether to print testing information
	private final boolean test = false;
	
	// Parameterized Constructor which takes the # of Worker Threads to be created
	public TaskExecutorImpl(int threadPoolSize) {
		
		// Initialize the FIFO to be used
		if(!ABQ)
			this.BFifo = new BlockingFIFO(fifoSize);
		else
			this.ABQFifo = new ArrayBlockingQueue<Task>(fifoSize);
		
		// Initialize the array that will store the Thread Workers
		this.threadPool = new PoolWorker[threadPoolSize];
		
		// Populate the array with Thread Workers
		for (int i = 0 ; i < threadPoolSize; i++) {
			// Constructor
			threadPool[i] = new PoolWorker();
			
			// Debug Print Statement
			if (test)
				System.out.println("Creating Pool Worker " + i);
			
			// Name the Worker Thread
			threadPool[i].setName("TaskThread" + i);
			
			// Start the Thread, and pass it the FIFO
			if(!ABQ)
				threadPool[i].start(BFifo);
			else
				threadPool[i].start(ABQFifo);
		}
	}
	
	// Used by TaskExecutorSimpleTest to populate the FIFO with Tasks
	@Override
	public void addTask(Task task){
		try {
			// Use the put method for the type of FIFO being used
			if(!ABQ)
				this.BFifo.put(task);
			else
				this.ABQFifo.put(task);
		} 
		catch (InterruptedException e) {
			System.out.println("WRITE TO FIFO FAILED");
		}
	}
}

