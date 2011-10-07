package simple.foundation;

public class Finally1 {
	public static void main(String args[]) {
		Finally1 t = new Finally1();
		int b = t.get();
		System.out.println(b);    // 2
	}

	//finally block does not complete normally
	public int get() {
		try {
			return 1;
		} finally {
			return 2;
		}
	}
}
