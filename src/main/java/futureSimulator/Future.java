package futureSimulator;

import java.util.ArrayList;
import java.util.List;

public class Future {
	List<Listener> listeners = new ArrayList<>();
	boolean isDone = false;
	
	public void addListener(Listener listener) {
		if(isDone) {
			listener.operationCompleted();
		}
		else {
			listeners.add(listener);
		}
	}
	
	public void run() {
			new Thread(() -> {
				System.out.println("Do sth until it's finished");
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				for (Listener listener:listeners) {
					System.out.println("execute listener during thread execution");
					listener.operationCompleted();
				}
				isDone = true;
		}).start();
	}
}
