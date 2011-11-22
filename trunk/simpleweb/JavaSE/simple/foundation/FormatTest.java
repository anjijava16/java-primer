package simple.foundation;
import java.text.DecimalFormat;
import java.text.MessageFormat;
import java.util.Date;

import org.apache.commons.lang.time.FastDateFormat;


public class FormatTest {
	public static FastDateFormat dateFormat = FastDateFormat.getInstance("yyyy年MM月dd日");
	public static void main(String...args){
		System.out.println(dateFormat.format(new Date()));

		DecimalFormat format = new DecimalFormat("###.##");
		System.out.println(format.format(101010.000022));

		DecimalFormat format1 = new DecimalFormat(".00");
		System.out.println(format1.format(101010.000022));
		
		
		//字符串填充
		String result = MessageFormat.format(
				"At {0}, there was {1} on planet {2}.",
				"one", "two", "three");

		System.out.println(result);
		
	}
}
