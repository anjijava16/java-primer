package simple.foundation;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

public class URL {
    public static void main(String[] args) {
        //f();
        ffff();
    }

    static void ffff (){
        String s = "%E4%B8%BB%E9%A2%98";
        String ss;
        try {
            ss = new String(s.getBytes(),"UTF-8");
            System.out.println(ss);
        } catch (UnsupportedEncodingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        
    }
    
    private static void ff() {
        //String s = "%E4%B8%BB%E9%A2%98";
        String s = "%E8%9C%9C%E8%9C%82%E8%AF%BB%E4%B9%A6%E5%85%85%E5%80%BC10%E8%9C%9C%E8%9C%82%E5%B8%81";
        String enc = "UTF-8";
        try {
            String re = URLDecoder.decode(s, enc);
            System.out.println(re);
        } catch (UnsupportedEncodingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    
    static void f(){
        String s = "http://www.omscn.com/payment/pay/alipayDirectRetunUrl.action?buyer_email=zxiaobi%40126.com&buyer_id=2088002260212306&exterface=create_direct_pay_by_user&is_success=T&notify_id=RqPnCoPT3K9%252Fvwbh3I7ykXW04iu5YTlt0gLB1apeM3YSLHFLBYrP3eLKt8aVulq5WZ2p&notify_time=2012-06-15+11%3A56%3A41&notify_type=trade_status_sync&out_trade_no=20121528790867&payment_type=1&seller_email=nizhenzhou%40gmail.com&seller_id=2088701734298869&subject=%E8%9C%9C%E8%9C%82%E8%AF%BB%E4%B9%A6%E5%85%85%E5%80%BC10%E8%9C%9C%E8%9C%82%E5%B8%81&total_fee=0.10&trade_no=2012061512404630&trade_status=TRADE_SUCCESS&sign=KVkleEDLDM3bcHd4fxKeFteEUlWpyDAn3N86CPkL1bAIHgyYXusyU8I7mhHk9SHWnQigb%2FFxCBS0SoBw3OhDaTVDjmihvO3EhULgLTGbykh%2B2bD7L2Y8U1ys9AZxkq3fzI%2FdX4KbeoGXwXR%2BiW1%2F0hjYGY15rCFAl1R%2FHQJjnxE%3D&sign_type=RSA";
        String enc = "UTF-8";
        try {
            String re = URLDecoder.decode(s, enc);
            System.out.println(re);
        } catch (UnsupportedEncodingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        
    }
}
