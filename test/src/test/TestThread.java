package test;

public class TestThread implements Runnable {
	
	private SubTest test;
	public TestThread(SubTest test) {
		this.test = test;
	}

	@Override
	public void run() {
		test.test1();
	}

}
