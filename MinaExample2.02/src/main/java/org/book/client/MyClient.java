package org.book.client;

import java.net.InetSocketAddress;
import java.nio.charset.Charset;

import org.apache.mina.core.future.ConnectFuture;
import org.apache.mina.core.future.IoFutureListener;
import org.apache.mina.core.service.IoConnector;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.filter.codec.textline.LineDelimiter;
import org.apache.mina.filter.codec.textline.TextLineCodecFactory;
import org.apache.mina.transport.socket.nio.NioSocketConnector;
import org.book.CmccSipcCodecFactory;

public class MyClient {
	public static void main(String[] args) {

		IoConnector connector = new NioSocketConnector();
		connector.setConnectTimeoutMillis(30000);

		connector.getFilterChain().addLast(
				"codec",
				new ProtocolCodecFilter(new CmccSipcCodecFactory(Charset
						.forName("UTF-8"))));
		/*
		 * connector.getFilterChain().addLast( "codec", new
		 * ProtocolCodecFilter(new
		 * TextLineCodecFactory(Charset.forName("UTF-8"),
		 * LineDelimiter.WINDOWS.getValue(),
		 * LineDelimiter.WINDOWS.getValue())));
		 */
		connector.setHandler(new ClientHandler("你好！\r\n 大家好！"));
		ConnectFuture future = connector.connect(new InetSocketAddress(
				"localhost", 9123));

		future.addListener(new IoFutureListener<ConnectFuture>() {
			@Override
			public void operationComplete(ConnectFuture future) {
				try {
					Thread.sleep(5000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				IoSession session = future.getSession();
				System.out.println("++++++++++++++++++++++++++++");
			}
		});
		System.out.println("*************");
	}
}