package server;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import client.ChatClient;
import common.ChatIF;


public class ServerConsole implements ChatIF{
	
	final public static int DEFAULT_PORT = 5555;
	private EchoServer echoServer;

	@Override
	public void display(String message) {
		// TODO Auto-generated method stub
		System.out.println(message);
	}
	
	public ServerConsole(int port){
	    echoServer = new EchoServer(port, this);
	}
	
	public static void main(String[] args) 
	  {
		int port = 0; //Port to listen on

	    try
	    {
	      port = Integer.parseInt(args[0]); //Get port from command line
	    }
	    catch(Throwable t)
	    {
	      port = DEFAULT_PORT; //Set port to 5555
	    }
		
	    ServerConsole sc = new ServerConsole(port);
	    
	    try 
	    {
	      sc.accept(); //Start listening for connections
	    } 
	    catch (Exception ex) 
	    {
	      System.out.println("ERROR - Could not listen for clients!");
	    }
	  }
	
	public void accept() 
	  {
	    try
	    {
	      BufferedReader fromConsole = 
	        new BufferedReader(new InputStreamReader(System.in));
	      String message;

	      while (true) 
	      {
	        message = fromConsole.readLine();
	        echoServer.handleMessageFromAdmin(message);
	      }
	    } 
	    catch (Exception ex) 
	    {
	      System.out.println
	        ("Unexpected error while reading from console!");
	    }
	  }

}
