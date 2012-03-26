package simple.foundation;


public class VarargsTest {
	
	
	public static void queryFirst(int serviceId,int refType, long... refID){
		querySecond(serviceId, refType, refID);
		return;
	}
	
	public static void querySecond(int serviceId,int refType, long[]... refID){
		for (int i = 0; i < refID.length; i++) {
			long[] r = refID[i];
			for (int j = 0; r != null && j < r.length; j++) {
				System.out.print(r[j] + ",");
			}
			System.out.println();
		}

		return;
	}
	
	static void test1(){
		long[][]refIDs = new long[2][];
		
		long[]refID1 = new long[2];
		refID1[0] = 11;
		refID1[1] = 112;
		
		long[]refID2 = new long[2];
		refID2[0] = 4;
		refID2[1] = 5;

		refIDs[0] = refID1;
		refIDs[1] = refID2;

		querySecond(0,0, refIDs);
	}
	
	static void test2(){
		long[][]refIDs = new long[2][];
		
		long[]refID1 = new long[2];
		refID1[0] = 11;
		refID1[1] = 112;
		
		long[]refID2 = new long[2];
		refID2[0] = 4;
		refID2[1] = 5;

		refIDs[0] = refID1;
		//refIDs[1] = refID2;

		querySecond(0,0, refIDs);
	}
	
	
	public static void main(String[] args) {
		test2();
	}
}
