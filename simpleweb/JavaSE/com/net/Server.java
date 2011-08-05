package com.net;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.net.Socket;
import java.security.KeyStore;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLServerSocket;
import javax.net.ssl.TrustManagerFactory;

public class Server {
	static String keystore = "JavaSE/com/security/tls_ssl/simpleDemo/key.cert";
	public static void main(String[] args) {
		try {
			Server myserver = new Server();
			SSLServerSocket mysslsocket = myserver.start();
			do {
				mysslsocket.setNeedClientAuth(true);
				Recv temp = new Recv(mysslsocket.accept());
				Thread td = new Thread(temp);
				td.start();
			} while (true);
		} catch (Exception err) {
			System.out.println(err.getMessage());
			err.printStackTrace();
		}
	}

	public SSLServerSocket start() {
		try {
			SSLContext ctx = SSLContext.getInstance("TLS");
			KeyManagerFactory kmf = KeyManagerFactory.getInstance("SunX509");
			TrustManagerFactory tmf = TrustManagerFactory
					.getInstance("SunX509");

			KeyStore ks = KeyStore.getInstance("JKS");
			KeyStore tks = KeyStore.getInstance("JKS");

			ks.load(new FileInputStream("bin\\server.keystore"),
					"123456".toCharArray());
			tks.load(new FileInputStream("bin\\client.keystore"),
					"123456".toCharArray());

			kmf.init(ks, "123456".toCharArray());
			tmf.init(tks);

			ctx.init(kmf.getKeyManagers(), tmf.getTrustManagers(), null);

			return (SSLServerSocket) ctx.getServerSocketFactory()
					.createServerSocket(8080);
		} catch (Exception err) {
			err.printStackTrace();
		}
		return null;
	}
}

class Recv implements Runnable {
	Socket mysocket = null;
	Recv(Socket mysocket) {
		this.mysocket = mysocket;
	}

	public void run() {
		try {
			BufferedReader myreader = new BufferedReader(new InputStreamReader(
					this.mysocket.getInputStream()));
			do {
				String data = myreader.readLine();

				if (data == null) {
					break;
				} else {
					System.out.println("recvfrom "
							+ this.mysocket.getInetAddress().toString() + ":"
							+ this.mysocket.getPort() + ":" + data);
				}

			} while (true);

			myreader.close();
			this.mysocket.close();
		} catch (Exception err) {
			System.out.println("connect from "
					+ this.mysocket.getInetAddress().toString() + ":"
					+ err.getMessage());
		}
	}
}
