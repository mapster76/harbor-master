package fr.insarouen.inforep.harbormaster.client.ordi;

public class ClientLauncher {
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		if(args.length<2){
			System.out.println("Please specify at least 2 arguments (third one is optionnal)");
			System.out.println("	arg1 : pseudo");
			System.out.println("	arg2 : server address under the form http://servername:port/");
			System.out.println("	arg3 : port to be used to recieve notifications from the server");
		}	
		ClientOrdi c = new ClientOrdi();		
		c.configure(args[0], args[1], (args.length==2) ? 5224 : Integer.parseInt(args[3]));
		c.start();
	}
}
