package org.book.server;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.charset.Charset;

import org.apache.mina.core.filterchain.DefaultIoFilterChainBuilder;
import org.apache.mina.core.service.IoAcceptor;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.filter.codec.textline.LineDelimiter;
import org.apache.mina.filter.codec.textline.TextLineCodecFactory;
import org.apache.mina.filter.logging.LogLevel;
import org.apache.mina.filter.logging.LoggingFilter;
import org.apache.mina.transport.socket.nio.NioSocketAcceptor;
import org.book.CmccSipcCodecFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MyServer {
	private final static Logger LOG = LoggerFactory.getLogger(MyServer.class);

	public static void main(String[] args) {
		
		IoAcceptor acceptor = new NioSocketAcceptor();
		acceptor.getSessionConfig().setReadBufferSize(2048);
		acceptor.getSessionConfig().setIdleTime(IdleStatus.BOTH_IDLE, 10);
		
		DefaultIoFilterChainBuilder chain = acceptor.getFilterChain();
		chain.addFirst("log", new LoggingFilter(){
			@Override
			public void setMessageReceivedLogLevel(LogLevel level) {
				LOG.info("LoggingFilter+++++++++++++++");
				setMessageReceivedLogLevel(LogLevel.DEBUG);
			}
			
			@Override
			public void setSessionCreatedLogLevel(LogLevel level) {
				super.setSessionCreatedLogLevel(level);
			}
		});
		
		acceptor.getFilterChain().addLast(
				"codec",
				new ProtocolCodecFilter(new CmccSipcCodecFactory(Charset
				.forName("UTF-8"))));
		
/*		chain.addLast(
				"codec",
				new ProtocolCodecFilter(new TextLineCodecFactory(Charset.forName("UTF-8"), 
						LineDelimiter.WINDOWS.getValue(),
						LineDelimiter.WINDOWS.getValue())
				)
		);
*/		
		acceptor.setHandler(new MyIoHandler());
		try {
			acceptor.bind(new InetSocketAddress(9123));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
