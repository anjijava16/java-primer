package com.slieer.ejbpro.ejb;

import javax.interceptor.AroundInvoke;
import javax.interceptor.InvocationContext;

public class MyInterceptor {

   @AroundInvoke
   public Object log(InvocationContext ctx) throws Exception{
      System.out.println("*** TracingInterceptor intercepting " + ctx.getMethod().getName());
      long start = System.currentTimeMillis();
      String param = (String)ctx.getParameters()[0];
      
      if (param == null){
      	ctx.setParameters(new String[]{"default"});      	
      }
      
      try{
         return ctx.proceed(); 
      }catch(Exception e){
         throw e;
      }finally{
      	
         long time = System.currentTimeMillis() - start;
         String method = ctx.getClass().getName();
         System.out.println("*** TracingInterceptor invocation of " + method + " took " + time + "ms");
      }
   }
}

