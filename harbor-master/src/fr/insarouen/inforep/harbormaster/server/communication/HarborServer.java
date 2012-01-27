package fr.insarouen.inforep.harbormaster.server.communication;

import org.restlet.resource.Get;
import org.restlet.resource.Put;

import fr.insarouen.inforep.harbormaster.common.Jeu;
import fr.insarouen.inforep.harbormaster.common.communication.ServerAction;

/**
 * Interface permettant de proposer une API de communication aux clients. Elle
 * definit l'ensemble des methodes disponibles pour que le client agisse sur le
 * jeu.
 */
public interface HarborServer {
	/**
	 * Methode permettant a un client de demander l'etat complet du jeu.<br />
	 * 
	 * @return L'etat complet du jeu.
	 */
	@Get
	Jeu getJeu();

	/**
	 * Methode permettant a un client d'effectuer une action auprès du server
	 * 
	 * @param action
	 *            l'action a effectuer
	 * @return un entier negatif decrivant une erreur s'il y en a eut une, ou le
	 *         resultat sous forme d'un entier positif s'il y en a un, 0 si il
	 *         n'y a pas eut d'erreur et qu'il n'y a pas de resultat a retrouner
	 */
	@Put
	Integer action(ServerAction action);

}
