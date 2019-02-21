package test;


public class Test {
	
	public synchronized void test1(){
		System.out.println("test1================");
		try {
			Thread.sleep(10000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public synchronized void test2(){
		System.out.println("test2================");
		try {
			Thread.sleep(10000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) throws Exception {
		SubTest test = new SubTest();
		new Thread(new TestThread(test)).start();
		new Thread(new TestThread2(test)).start();
	}
	
}
