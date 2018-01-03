package server;

public class ServerProtocolFactoryImpl implements ServerProtocolFactory {

	@Override
	public TBGPProtocol create() {
		return new TBGPProtocol();
	}
}
