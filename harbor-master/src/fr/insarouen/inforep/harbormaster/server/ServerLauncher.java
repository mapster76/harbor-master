package fr.insarouen.inforep.harbormaster.server;

import org.restlet.data.Protocol;

import fr.insarouen.inforep.harbormaster.server.communication.CommunicationServer;

public class ServerLauncher {


	public static void main(String args[]) throws Exception {
		if(args.length==0){
			System.out.println("Please specify the port to use");
		}
		new org.restlet.Server(Protocol.HTTP, Integer.parseInt(args[0]), CommunicationServer.class)
				.start();
	}

}
