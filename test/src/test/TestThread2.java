package test;

public class TestThread2 implements Runnable {
	
	private SubTest test;
	public TestThread2(SubTest test) {
		this.test = test;
	}

	@Override
	public void run() {
		test.test2();
	}

}
