// This file contains material supporting section 3.7 of the textbook:
// "Object Oriented Software Engineering" and is issued under the open-source
// license found at www.lloseng.com 
package GUI;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;

import client.*;
import common.*;

import javax.swing.*;

/**
 * This class constructs the UI for a chat client.  It implements the
 * chat interface in order to activate the display() method.
 * Warning: Some of the code here is cloned in ServerConsole 
 *
 * @author Fran&ccedil;ois B&eacute;langer
 * @author Dr Timothy C. Lethbridge  
 * @author Dr Robert Lagani&egrave;re
 * @version July 2000
 */
public class ClientGUI extends JFrame implements ChatIF, ActionListener
{
	//Class variables *************************************************

	/**
	 * The default port to connect on.
	 */
	final public static int DEFAULT_PORT = 5555;

	//Instance variables **********************************************

	/**
	 * The instance of the client that created this ConsoleChat.
	 */
	ChatClient client;

	private JTextArea userInput;
	private JButton send;
	private JPanel messages;
	private JTextArea messagesRecus;


	//Constructors ****************************************************

	/**
	 * Constructs an instance of the ClientConsole UI.
	 *
	 * @param host The host to connect to.
	 * @param port The port to connect on.
	 */
	public ClientGUI(String id, String host, int port) 
	{
		try 
		{
			client= new ChatClient(id, host, port, this);
			userInput = new JTextArea(3, 50);
			send = new JButton("Send message");
			messages = new JPanel();
			messages.setLayout(new BorderLayout());
			messagesRecus = new JTextArea(20, 50);
			messagesRecus.setEditable(false);
			messages.add(messagesRecus, BorderLayout.CENTER);
			JScrollPane sp = new JScrollPane(messages);
			
			//messages.setEditable(false);
			this.setMinimumSize(new Dimension());
			this.setSize(1920, 1080);
			send.addActionListener(this);
			send.setActionCommand("send");
			BorderLayout layoutGeneral = new BorderLayout();
			this.setLayout(layoutGeneral);
			this.add(sp, BorderLayout.NORTH);
			FlowLayout fl = new FlowLayout();
			JPanel sud = new JPanel();
			sud.setLayout(fl);
			sud.add(userInput);
			sud.add(send);
			this.add(sud);
			this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			pack();
			setVisible(true);
		} 
		catch(IOException exception) 
		{
			System.out.println("Error: Can't setup connection!"
					+ " Terminating client.");
			System.exit(1);
		}
	}



	//Instance methods ************************************************

	/**
	 * This method waits for input from the console.  Once it is 
	 * received, it sends it to the client's message handler.
	 */
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
				client.handleMessageFromClientUI(message);
			}
		} 
		catch (Exception ex) 
		{
			System.out.println
			("Unexpected error while reading from console!");
		}
	}

	/**
	 * This method overrides the method in the ChatIF interface.  It
	 * displays a message onto the screen.
	 *
	 * @param message The string to be displayed.
	 */
	public void display(String message) 
	{
		//System.out.println("> " + message);
		if(message.equals("please login (hint: use the command #login your_name)")){
			JOptionPane.showMessageDialog(null, message, "Erreur login", JOptionPane.ERROR_MESSAGE);
		}
		else if(message.matches("Port = [0-9]{4}")){
			JOptionPane.showMessageDialog(null, message, "Port", JOptionPane.INFORMATION_MESSAGE);
		}
		else if(message.matches("Host = [A-z]+")){
			JOptionPane.showMessageDialog(null, message, "Host", JOptionPane.INFORMATION_MESSAGE);
		}
		else
			messagesRecus.setText(messagesRecus.getText()+"\n"+message);
	}


	//Class methods ***************************************************

	/**
	 * This method is responsible for the creation of the Client UI.
	 *
	 * @param args[0] The host to connect to.
	 */
	public static void main(String[] args) 
	{
		String host = "localhost";
		int port = 0;  //The port number
		String id = null;

		try
		{
			id = args[0];
			host = args[1];
			port = Integer.parseInt(args[2]);
		}
		catch(ArrayIndexOutOfBoundsException e)
		{
			host = "localhost";
		}
		ClientGUI chat= new ClientGUI(id, host, DEFAULT_PORT);
		chat.accept();  //Wait for console data
	}



	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getActionCommand().equals("send")){
			if(!userInput.getText().equals("")){
				client.handleMessageFromClientUI(userInput.getText());
				userInput.setText("");
			}
		}
		
	}
}
//End of ConsoleChat class
