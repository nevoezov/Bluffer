package server;

import java.io.*;
import java.net.*;

import org.omg.PortableInterceptor.SYSTEM_EXCEPTION;

import tokenizer.*;

class MultipleClientProtocolServer implements Runnable {

	ServerSocket serverSocket;
	private int listenPort;
	private ServerProtocolFactoryImpl factory;

	public MultipleClientProtocolServer(int port, ServerProtocolFactoryImpl p) {
		serverSocket = null;
		listenPort = port;
		factory = p;
	}

	public void run() {
		try {
			serverSocket = new ServerSocket(listenPort);
			System.out.println("Listening...");
		} catch (IOException e) {
			System.out.println("Cannot listen on port " + listenPort);
		}

		while (true) {
			try {
				Socket s = serverSocket.accept();
				Encoder encoder = new EncoderImpl("UTF-8");
				StringTokenizer tokenizer = new StringTokenizer(
						new InputStreamReader(s.getInputStream(), encoder.getCharset()), '\n');
				ConnectionHandler newConnection = new ConnectionHandler(s, encoder, tokenizer, factory.create());
				new Thread(newConnection).start();
			} catch (IOException e) {
				System.out.println("Failed to accept on port " + listenPort);
			}
		}
	}

	// Closes the connection
	public void close() throws IOException {
		serverSocket.close();
	}

	public static void main(String[] args) throws IOException {
		// Get port
		int port = Integer.decode(args[0]).intValue();

		MultipleClientProtocolServer server = new MultipleClientProtocolServer(port, new ServerProtocolFactoryImpl());
		Thread serverThread = new Thread(server);
		serverThread.start();
		try {
			serverThread.join();
		} catch (InterruptedException e) {
			System.out.println("Server stopped");
		}
	}
}