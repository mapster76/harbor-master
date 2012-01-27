package fr.insarouen.inforep.harbormaster.client;

import org.restlet.resource.Put;

import fr.insarouen.inforep.harbormaster.common.communication.ClientAction;

/**
 * Cette interface permet de definir l'ensemble des methodes que le serveur peut appeler sur le client.
 */
public interface HarborClient {
	
	/**
	 * Methode permettant au serveur d'effectuer une action auprès d'un client
	 * 
	 * @param action
	 *            l'action a effectuer
	 */
	@Put
	void action(ClientAction action);


}
