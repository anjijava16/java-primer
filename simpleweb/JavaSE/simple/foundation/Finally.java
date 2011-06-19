package simple.foundation;

public class Finally {

	/**
	 * @param args
	 *          add by zxx ,Dec 9, 2008
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		System.out.println(Finally.test());
		;
	}
	static int test() {
		int x = 1;
		try {
			return x;
		} finally {
			++x;
		}
	}
}
