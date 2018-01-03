package server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;

import tokenizer.*;

public class ConnectionHandler implements Runnable {

	private BufferedReader in;
	private PrintWriter out;
	private Socket _socket;
	private TBGPProtocol _protocol;
	private ProtocolCallback<String> callback;
	private StringTokenizer _tokenizer;
	private Encoder _encoder;

	public ConnectionHandler(Socket acceptedSocket, Encoder encoder, StringTokenizer tokenizer, TBGPProtocol protocol) {
		in = null;
		out = null;
		this._tokenizer = tokenizer;
		_socket = acceptedSocket;
		_protocol = protocol;
		_encoder = encoder;
		System.out.println("Accepted connection from client!");
		System.out.println("The client is from: " + acceptedSocket.getInetAddress() + ":" + acceptedSocket.getPort());
	}

	@Override
	public void run() {

		String msg;

		try {
			initialize();
		} catch (IOException e) {
			System.out.println("Error in initializing I/O");
		}

		try {
			process();
		} catch (IOException e) {
			System.out.println("Error in I/O");
		}

		System.out.println("Connection closed - bye bye...");
		close();

	}

	public void process() throws IOException {
		while (!_protocol.shouldClose() && !_socket.isClosed()) {
			try {
				if (!_tokenizer.isAlive())
					_protocol.connectionTerminated();
				else {
					String msg = _tokenizer.nextToken();
					System.out.println("got " + msg + " from client");
					_protocol.processMessage(msg, callback);
					boolean end = _protocol.isEnd(msg);

				}
			} catch (IOException e) {
				_protocol.connectionTerminated();
				break;
			}

		}
	}

	// Starts listening
	public void initialize() throws IOException {
		// Initialize I/O
		in = new BufferedReader(new InputStreamReader(_socket.getInputStream(), "UTF-8"));
		out = new PrintWriter(new OutputStreamWriter(_socket.getOutputStream(), "UTF-8"), true);
		callback = new ProtocolCallback<String>() {
			public void sendMessage(String msg) throws IOException {
				out.println(msg);
			}
		};
		System.out.println("I/O initialized");
	}

	// Closes the connection
	public void close() {
		try {
			if (in != null) {
				in.close();
			}
			if (out != null) {
				out.close();
			}

			_socket.close();
		} catch (IOException e) {
			System.out.println("Exception in closing I/O");
		}
	}

}
