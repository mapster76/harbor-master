package fr.insarouen.inforep.harbormaster.common;

import java.io.Serializable;

/*Cette classe modélise l'ensemble du jeu. Elle contient ainsi le nombre de joueurs, la flotte, la grille, les ports et le score caractérisant le jeu.*/
/**
 * @author pilli
 * 
 */
public class Jeu implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int nombreDeJoueurs;
	private Flotte flotte;
	private int score;
	private boolean gameOver;
	
	public void restart() {
		score=0;
		flotte=new Flotte();
		gameOver=false;
	}
	
	public String toString()
	{
		
		return "il y a actuellement "+nombreDeJoueurs+" joueurs. la flotte est constituée de :\n"+flotte.toString()+"\n Le score actuel est de "+ score;
	}
	public Jeu(String nomMap) {
		flotte = new Flotte();
	}
	
	public int getScore() {
		return score;
	}

	public int getNombreDeJoueurs() {
		return this.nombreDeJoueurs;
	}

	public void ajouterJoueur() {
		this.nombreDeJoueurs++;
	}
	
	public void supprimerJoueur() {
		this.nombreDeJoueurs--;
	}

	public Flotte getFlotte() {
		return flotte;
	}

	public void incrementerScore() {
		score++;
	}
	public boolean estGameOver() {
		return gameOver;
	}
	public void setGameOver(boolean gameOver) {
		this.gameOver = gameOver;
	}
	
	

}