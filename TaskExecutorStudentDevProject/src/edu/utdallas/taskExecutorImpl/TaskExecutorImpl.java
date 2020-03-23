package edu.utdallas.taskExecutorImpl;

import edu.utdallas.taskExecutor.Task;
import edu.utdallas.taskExecutor.TaskExecutor;
import edu.utdallas.blockingFIFO.BlockingFIFO;

public class TaskExecutorImpl implements TaskExecutor
{

	public final int sizeFIFO = 5;
	public BlockingFIFO fifo;
	
	public TaskExecutorImpl(int numThreads) { 
		fifo = new BlockingFIFO(sizeFIFO);
		
		Runnable TaskThread = new Runnable() {
			public void run()
			{
				while(true) {
					Task task = fifo.fetch();
					
					try {
						task.execute();
					}
					catch(Exception e) {
						//System.out.println(e.getMessage());
					}
				}
			}
		};
		
		for(int i = 0; i < numThreads; i++) {
			Thread taskThread = new Thread(TaskThread);
			taskThread.start();
		}
	}
	
	@Override
	public void addTask(Task task)
	{
		fifo.append(task);
	}

}
