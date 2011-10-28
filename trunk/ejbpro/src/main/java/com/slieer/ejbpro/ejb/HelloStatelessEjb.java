package com.slieer.ejbpro.ejb;

import java.util.Date;

import javax.annotation.Resource;
import javax.ejb.Local;
import javax.ejb.Remote;
import javax.ejb.SessionContext;
import javax.ejb.Stateless;
import javax.ejb.Timeout;
import javax.ejb.Timer;
import javax.ejb.TimerService;

import org.apache.log4j.Logger;
import org.jboss.ejb3.annotation.LocalBinding;
import org.jboss.ejb3.annotation.RemoteBinding;

import com.slieer.ejbpro.ifc.HelloStatelessLocalIfc;
import com.slieer.ejbpro.ifc.HelloStatelessRemoteIfc;

@Stateless
@Local(value = HelloStatelessLocalIfc.class)
@Remote(value = HelloStatelessRemoteIfc.class)
@LocalBinding(jndiBinding = "HelloStatelessLocal")
@RemoteBinding(jndiBinding = "HelloStatelessRemote")
public class HelloStatelessEjb implements HelloStatelessLocalIfc, HelloStatelessRemoteIfc {
	Logger log = Logger.getLogger(HelloStatelessEjb.class);
	
	@Resource
	private SessionContext ctx;
	
	@Resource
	private TimerService timer;

	@Override
	public String hello() {
		return "hello I'm slieer.";
	}

	@Override
	public void scheduleTimer(long milliseconds) {
		count = 1;
		//TimerService timer = ctx.getTimerService();
		
		Date newDatePoint = new Date(new Date().getTime() + milliseconds);
		timer.createTimer(newDatePoint, milliseconds, "大家好，这是我的第一个定时器");
	}

	private int count = 1; // 不太好啊

	@Timeout
	public void timeoutHandler(Timer timer) {
		log.info("---------------------");
		log.info("定时器事件发生,传进的参数为: " + timer.getInfo());
		log.info("定时器事件发生,传进的参数为: " + timer.getNextTimeout());
		log.info("---------------------");
		if (count >= 5) {
			log.info("timer stop!");
			timer.cancel();// 如果定时器触发5次，便终止定时器
		}
		count++;
	}

}
