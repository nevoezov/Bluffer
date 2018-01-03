package server;

import protocol.AsyncServerProtocol;

public interface ServerProtocolFactory {
	public AsyncServerProtocol create();
}
