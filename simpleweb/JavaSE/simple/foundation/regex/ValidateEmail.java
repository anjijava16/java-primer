package simple.foundation.regex;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.junit.Test;

public class ValidateEmail {
    @Test
    public void testEmail(){
        String[] email = new String[5];
        email[0] = "abc@g.cn";
        email[1] = "sli@z.cn";
        email[2] = "abc@g.cn";
        email[3] = "slieer@t.cn";
        email[4] = "";
        System.out.println(validateEmail(email));
    }
    
    public static boolean validateEmail(String[] email){
        if(email == null || email.length == 0){
            return false;
        }
        final String EMAIL_EXGEX = "^([a-z0-9A-Z]+[-|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$";
        Pattern regex = Pattern.compile(EMAIL_EXGEX);
        for(int i = 0; i < email.length; i++){
            if(!email[i].equals("")){
                Matcher matcher = regex.matcher(email[i]);          
                boolean match = matcher.matches();
                if(! match){
                    return false;
                }               
            }
        }
        return true;
    }

}
