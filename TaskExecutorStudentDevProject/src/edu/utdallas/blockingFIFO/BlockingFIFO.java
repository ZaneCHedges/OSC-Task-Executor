package edu.utdallas.blockingFIFO;

import edu.utdallas.taskExecutor.Task;

public class BlockingFIFO {
	
	public Task[] buffer;
	public int nextin, nextout;
	public int count;
	public Object full, empty;
	
	public BlockingFIFO(int N) {
		buffer = new Task[N];
		nextin = 0;
		nextout = 0;
		count = 0;
		full = new Object();
		empty = new Object();
	}
	
	public void append(Task task){
		while(count == buffer.length) {
			try {
				full.wait();
			}
			catch(InterruptedException e) {
				Thread.currentThread().interrupt(); 
				System.out.println("Append Thread interrupted");
			}
		}
		
		synchronized(this) {
			buffer[nextin] = task;
			nextin = (nextin + 1) % buffer.length;
			count++;
			empty.notify();
		}
	}
	
	public Task fetch(){
		while(count == 0) {
			try {
				empty.wait();
			}
			catch(InterruptedException e) {
				Thread.currentThread().interrupt(); 
	            System.out.println("Fetch Thread interrupted");
			}
		}
		
		synchronized(this) {
			Task task = buffer[nextout];
			nextout = (nextin + 1) % buffer.length;
			count--;
			full.notify();
			return task;
		}
	}
}
