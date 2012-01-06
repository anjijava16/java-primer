package simple.foundation;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.apache.commons.lang.time.FastDateFormat;
import org.junit.Test;


public class FormatTest {
	public static FastDateFormat dateFormat = FastDateFormat.getInstance("yyyy年MM月dd日");
	
	public static void main(String[] args) {
		new FormatTest().dateFormat();
	}
	
	@Test
	public void dateFormat(){
		String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";
		DateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT);
		String d = dateFormat.format(new Date());
		System.out.println(d);
		
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.HOUR_OF_DAY, 10);
		d = dateFormat.format(cal.getTime());
		System.out.println(d);

		MessageFormat SIMPLE_NODE_FORMAT = new MessageFormat("<{0}>{1}</{0}>");
		String NODE_LASTMODIFY = "last";
		
		d = SIMPLE_NODE_FORMAT.format(new Object[]{NODE_LASTMODIFY,dateFormat.format(cal.getTime())});
		System.out.println(d);
	}
		
	@Test
	public void numFormat() {
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
