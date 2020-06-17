package futureSimulator;

public class Test {
	public static void main(String[] args) {
		Future future = new Future();
		future.run();
		future.addListener(new Listener() {
			
			@Override
			public void operationCompleted() {
				System.out.println("Capiche");
			}
		});
	}
}
