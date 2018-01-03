# All Targets
all: client

# Tool invocations
# Executable "client" depends on the files client.o and run.o.
client: bin/connectionHandler.o bin/EchoClient.o
	@echo 'Building target: Client'
	@echo 'Invoking: C++ Linker'
	g++ -o bin/client bin/connectionHandler.o bin/EchoClient.o -lboost_system -lboost_locale -lboost_thread
	@echo 'Finished building target: client'
	@echo ' '

# Depends on the source and header files 
bin/ConnectionHandler.o: src/connectionHandler.cpp
	g++ -g -Wall -Weffc++ -c -Linclude -o bin/connectionHandler.o src/connectionHandler.cpp
	
# Depends on the source and header files
bin/EchoClient.o: src/EchoClient.cpp
	g++ -g -Wall -Weffc++ -c -Linclude -o bin/EchoClient.o src/EchoClient.cpp
	
#Clean the build directory
clean: 
	rm -f bin/*