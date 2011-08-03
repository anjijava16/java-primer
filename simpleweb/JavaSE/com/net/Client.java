package com.net;

import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.OutputStreamWriter;
import java.security.KeyStore;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.TrustManagerFactory;

public class Client {

	public static void main(String[] args) {
		// TODO Auto-generated methodstub
		try {
			Client myclient = new Client();
			SSLSocket mysslsocket = myclient.start();
			BufferedWriter mywriter = new BufferedWriter(
					new OutputStreamWriter(mysslsocket.getOutputStream()));

			mywriter.write("HelloWorld!");
			mywriter.close();
			mysslsocket.close();

		} catch (Exception err) {
			err.printStackTrace();
		}
	}

	public SSLSocket start() {
		try {
			SSLContext ctx = SSLContext.getInstance("TLS");

			KeyManagerFactory kmf = KeyManagerFactory.getInstance("SunX509");
			TrustManagerFactory tmf = TrustManagerFactory
					.getInstance("SunX509");

			KeyStore ks = KeyStore.getInstance("JKS");
			KeyStore tks = KeyStore.getInstance("JKS");

			ks.load(new FileInputStream("bin\\client.keystore"),
					"123456".toCharArray());
			tks.load(new FileInputStream("bin\\server.keystore"),
					"123456".toCharArray());

			kmf.init(ks, "123456".toCharArray());
			tmf.init(tks);

			ctx.init(kmf.getKeyManagers(), tmf.getTrustManagers(), null);

			return (SSLSocket) ctx.getSocketFactory().createSocket("10.1.1.52",
					8080);

		} catch (Exception err) {
			err.printStackTrace();
		}
		return null;
	}
}
