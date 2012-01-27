package fr.insarouen.inforep.harbormaster.client;

import fr.insarouen.inforep.harbormaster.common.Jeu;

/**
 * Interface permettant de définir les différentes fonctions qui interagissent entre le client et le joueur
 * @author julie et franfran
 *
 */
public interface EvenementClient {
	
	/**
	 * Methode permettant de se connecter
	 */
	public void seConnecter();
	
	/**
	 * Methode permettant de selectionner un bateau
	 * @param laPosition position du clic de souris
	 * @return le bateau selectionne
	 */
	public void selectionnerBateau(int x,int y);
	
	/**
	 * Methode permettant de deselectionner un bateau
	 * @param x
	 * @param y
	 */
	public void deselectionnerBateau(int x,int y);
	
	/**
	 * Methode permettant de tracer une nouvelle trajectoire pour un bateau déja selectionner
	 * @param laListeDePositions la liste des positions représentant la trajectoire
	 * @return la nouvelle trajectoire
	 */
	public void tracerTrajectoire(int xOld, int yOld,int xNew,int yNew);
	
	/**
	 * Méthode permettant de se déconnecter
	 */
	public void seDeconnecter();

	public Jeu getJeu();

		
}
