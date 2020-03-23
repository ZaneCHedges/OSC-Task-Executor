package edu.utdallas.taskExecutorImpl;

import edu.utdallas.taskExecutor.Task;
import edu.utdallas.taskExecutor.TaskExecutor;

import java.util.concurrent.ArrayBlockingQueue;


public class TaskExecutorImpl implements TaskExecutor{
	
	/*  The TaskExecutor Implementation maintains a thread pool of some positive integer
	 *  input and a blocking queue of some predetermined integer size. 
	 */
	ArrayBlockingQueue<Task> Fifo;
	PoolWorker[] threadPool;
	int queueSize = 15; // Set to some integer value 10-100
	
	
	
	public TaskExecutorImpl(int threadPoolSize) {
		/* Here is the constructor for our implementation. We expect a positive integer input for 
		 * threadPoolSize. We initialize a blockingFifoQueue of predetermined size and create a thread pool as
		 * an array of worker threads which we loop through, creating and running each worker thread.
		 */
		
		this.Fifo = new ArrayBlockingQueue<Task>(queueSize);
		this.threadPool = new PoolWorker[threadPoolSize];
		
		for (int i = 0 ; i < threadPoolSize; i++) {
			// Create each worker thread and run it. Each should block and wait for the Queue to fill up.
			threadPool[i] = new PoolWorker();
			System.out.println("making worker " + i);
			threadPool[i].run();
			
			
		}
		
	}
	
	@Override
	public void addTask(Task task){
		/* Here we have a method for adding tasks into a TaskExecutor. We assume our Fifo is blocking 
		 * so that we just add the task and the Queue will handle the rest.
		 */
		this.Fifo.add(task);
	}
	
	public class PoolWorker extends Thread {
		
		public void run() {
			/* The below section of code is a lightly modified block taken from the suggested
			 *  implementation of TaskExecutor found in the Project description document. The difference is that 
			 *  fifo.take() statement is within the Try Block instead of before.
			 */
			while(true) {
				
				Task nextTask; 
				
				try {
					nextTask = Fifo.take();
					nextTask.execute();
					
				} catch (Throwable th) {
					// Log (e.g. print exception’s message to console) 
			           	// and drop any exceptions thrown by the task’s
			           	// execution.
					}
							
			}
		}

	}

