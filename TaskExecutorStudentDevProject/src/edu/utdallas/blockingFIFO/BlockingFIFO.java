package edu.utdallas.blockingFIFO;

import edu.utdallas.taskExecutor.Task;

public class BlockingFIFO {
	
	Task[] buffer;
	int nextin, nextout;
	int count;
	Object full, empty;
	
	public BlockingFIFO(int N) {
		buffer = new Task[N];
		nextin = 0;
		nextout = 0;
		count = 0;
		full = new Object();
		empty = new Object();
	}
	
	public void append(Task task){
		synchronized(this) {
			if(count == buffer.length) {
				full.wait();
			}
			buffer[nextin] = task;
			nextin = (nextin + 1) % buffer.length;
			count++;
			empty.notify();
		}
	}
	
	public Task fetch(){
		synchronized(this) {
			if(count == 0) {
				empty.wait();
			}
			Task task = buffer[nextout];
			nextout = (nextin + 1) % buffer.length;
			count--;
			full.notify();
			return task;
		}
	}
}
