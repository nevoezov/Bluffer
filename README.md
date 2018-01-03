# Bluffer

Client/Server Bluffer game, implemented using JAVA and C++.
The server side is implemented using JAVA, in two seperated designs: Reactor Server and Thread-Per-client Server,
and able to support multiple remote clients.
The client side is implemented using C++.

# Server To Client Commands
• Command: ASKTXT
Parameter: text - question to ask.
Used to ask the client the specified question, expecting a TXTRESP.

• Command: ASKCHOICES
Parameter: text - question to ask.
Used to ask the client the specified question, expecting a SELECTRESP.

• Command: SYSMSG
Parameter: text.
Response to every command the user sends. The format is:
SYSMSG <Original Command> <Result> <Optional : Custom Message>\n
Where <Result> is one of: ACCEPTED, REJECTED or UNIDENTIFIED.
  
• Command GAMEMSG
Parameter: text.
Custom messages that are a part of the game being played.

• Command: USRMSG
Parameter: text.
Used to deliver a message sent by another user, in the context of a game-room.
  
# Client To Server Commands
• Command: NICK
Parameter: nickname.
Requests the specified nickname.

• Command: JOIN
Parameter: room name.
Joins the specified room, creating it if it doesn’t exist.
This also causes the user to leave the current room, therefore is impossible while a game is in progress.

• Command: MSG
Parameter: text to be sent.
Sends the specified message to the users current room.

• Command: LISTGAMES
Lists the games supported by the server.

• Command: STARTGAME
Parameter: game name.
Starts the specified game in the current room. Game is specified using a unique string identifier.

• Command: TXTRESP
Parameter: text to be sent.
Used as a response to ASKTXT command, providing a textual response.

• Command: SELECTRESP
Parameter: integer - selected response.
Used as a response to ASKCHOICES command, providing a numeric selection.

• Command: QUIT
Closes the connection.
