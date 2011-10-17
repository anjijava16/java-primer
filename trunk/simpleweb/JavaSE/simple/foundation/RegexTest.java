package simple.foundation;


public class RegexTest {
	public static void main(String[] args) {
		test();
	}
	
	public static void test(){
		String condition1 = "contact[@id=11]";
			
		if(condition1.matches("[0-9]+")){
			System.out.println("OK");
		}else{
			System.out.println("failure");
		}
	}
}
