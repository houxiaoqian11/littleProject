package test;

public class SubTest extends Test {

	public synchronized void test1(){
		System.out.println("subTest1================");
		try {
			Thread.sleep(10000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void test2(){
		System.out.println("subTest2================");
		super.test2();
		try {
			Thread.sleep(10000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}
