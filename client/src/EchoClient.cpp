#include <stdlib.h>
#include <boost/locale.hpp>
#include <boost/thread.hpp>
#include <iostream>
#include <boost/asio.hpp>
#include "../include/connectionHandler.h"

void sendMsg(ConnectionHandler *handler) {
	while(1) {
		const short bufsize = 1024;
		char buf[bufsize];
		std::cin.getline(buf, bufsize);
		std::string line(buf);
		if (!handler->sendLine(line)) {
			std::cout << "Disconnected. Exiting...\n" << std::endl;
			break;
		}

	}
	handler->close();
	delete handler;
}



void receiveMsg(ConnectionHandler *handler) {
	int len;
	while(1) {
		std::string answer;
		if (!handler->getLine(answer)) {
			std::cout << "Disconnected. Exiting...\n" << std::endl;
		}
		len=answer.length();
		answer.resize(len-1);
		std::cout << "SERVER: " << answer << std::endl;
		if (answer == "bye") {
			std::cout << "Exiting...\n" << std::endl;
			break;
		}
	}
	handler->close();
	delete handler;
}


/**
 * This code assumes that the server replies the exact text the client sent it (as opposed to the practical session example)
 */
int main (int argc, char *argv[]) {
	std::string host = argv[1];
	unsigned short port = atoi(argv[2]);
	ConnectionHandler *connectionHandler = new ConnectionHandler(host, port);
	if (!connectionHandler->connect()) {
		std::cerr << "Cannot connect to " << host << ":" << port << std::endl;
		return 1;
	}
	boost::thread t1(sendMsg,connectionHandler);
	boost::thread t2(receiveMsg,connectionHandler);
	t1.join();
	t2.join();
}
