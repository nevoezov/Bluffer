package protocol;

import java.io.IOException;

import server.ProtocolCallback;
import tokenizer.StringMessage;

/**
 * a simple implementation of the server protocol interface
 */
public class EchoProtocol implements AsyncServerProtocol<String> {

	private boolean _shouldClose = false;
	private boolean _connectionTerminated = false;

	/**
	 * processes a message<BR>
	 * this simple interface prints the message to the screen, then composes a
	 * simple reply and sends it back to the client
	 *
	 * @param msg
	 *            the message to process
	 * @return the reply that should be sent to the client, or null if no reply
	 *         needed
	 */
	@Override
	public void processMessage(String msg, ProtocolCallback<String> callback) {
		if (this._connectionTerminated) {
			return;
		}
		if (this.isEnd(msg)) {
			this._shouldClose = true;
			try {
				callback.sendMessage("Ok, bye bye");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		try {
			callback.sendMessage("Your message \"" + msg + "\" has been received");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * detetmine whether the given message is the termination message
	 *
	 * @param msg
	 *            the message to examine
	 * @return false - this simple protocol doesn't allow termination...
	 */
	@Override
	public boolean isEnd(String msg) {
		return msg.equals("bye");
	}

	/**
	 * Is the protocol in a closing state?. When a protocol is in a closing
	 * state, it's handler should write out all pending data, and close the
	 * connection.
	 * 
	 * @return true if the protocol is in closing state.
	 */
	@Override
	public boolean shouldClose() {
		return this._shouldClose;
	}

	/**
	 * Indicate to the protocol that the client disconnected.
	 */
	@Override
	public void connectionTerminated() {
		this._connectionTerminated = true;
	}

}
