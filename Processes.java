import java.io.File;
import java.io.PrintWriter;
import java.util.*;

//outer class to allow sharing a variable with subclasses 
public class Processes {
	
	CircularBuffer buffer;
	PrintWriter output;
	Scanner scan;
	 
	public Processes() {
		
		//prompt user for run number so each output has different filenames 
		scan = new Scanner (System.in);
		System.out.print("Enter run number ");
		int run = scan.nextInt(); 
		
		//PrinterWriter & I/O File code copied from ICS-340 InitialCodebase 
		//by Metropolitan State University Professor Michael Stein
		File outputFile;
		output = null;
		
		outputFile = new File( "output" + run+ ".txt" );
		if ( outputFile.exists() ) {
			outputFile.delete();
		}
		
		try {
			output = new PrintWriter(outputFile);			
		}
		catch (Exception x ) { 
			System.err.format("Exception: %s%n", x);
			System.exit(0);
		}
		
		output.print("Computer Operating Systems Assignment #3\n\n");
		
		
		//create a Circular Buffer, a Producer thread, and a Consumer thread 
		//when the Processes constructor is called.
		buffer = new CircularBuffer();
		new Producer();
		new Consumer();
	}
	
	public class Producer{
		//calling the Producer constructor method creates a thread running the runWork method
		public Producer() {
			Thread t = new Thread(() -> runWork());
			t.start();
		}
		
		//runWork method loops 100 times, each time with a random delay, and each time 
		//passing the loop counter value to the share variable.
		private void runWork() {
			for(int i = 0; i < 101; i++) {
				delay(2, 5);
				while(buffer.isFull()) {
					System.out.println("Producer waiting");
					delay(1, 1);	
				}
				if(i < 100) {
					System.out.println("Producer add " + i + " " + buffer.add(i));
				}else {
					System.out.println("Producer add -1 " + buffer.add(-1));
				}
			}	
		}
	}
	
	public class Consumer{
		//calling the Consumer constructor method creates a thread running the runWork method
		public Consumer() {
			Thread t = new Thread(() -> runWork());
			t.start();
		}
		
		//runWork method loops until it receives a -1 from the circular buffer. during each loop,
		//the method reads the buffer and outputs the value to a file.
		private void runWork() {
			int value = 0;
			while(value != -1) {
				delay(1, 4);
				if(buffer.isEmpty()) {
					System.out.println("\t\t\tConsumer waiting");
					output.print("Consumer waiting\n");
					delay(1, 1);
				}else {
					value = buffer.read();
					System.out.println("\t\t\tConsumer remove " + value + " " + buffer.remove());
					if(value != -1) {
						output.print(value + "\n");
					}
				}
			}
			System.out.println("Consumer done");
			output.print("Consumer Done");
			
			output.flush();
			scan.close();
		}
	}
	
	//random delay between min to max seconds
	public static void delay(int min, int max) {
		Random r = new Random();
		int millisec = (r.nextInt(max) + min) * 1000;
		try {
			Thread.sleep(millisec); //delay in milliseconds 
		}catch(InterruptedException e) {
			Thread.currentThread().interrupt();
		}
	}
	
	public static void main(String[] args) {
		//create threads 
		new Processes();
	}
}
