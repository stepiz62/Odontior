Debugging/Development notes:

1)
remember that the presence of Joty.xml file in the start-up space of the project (or of the jar file) makes the 
application to run like a Desktop application that asks for (configuration of) direct access to a dbms through 
the jdbc layer.

2) 
to run the application for a web connection when the Java Web Start scenario is not in asset, the url of the server, 
like that:

 	http://<hostname>[:<port>]/odontior2-server/
 	
 must be copied in the clip-board before launching the java application.