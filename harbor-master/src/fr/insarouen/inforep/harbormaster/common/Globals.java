package fr.insarouen.inforep.harbormaster.common;

public class Globals {
	/**
	 * l'échelle de la fenêtre a afficher
	 */
	public static float windowScale=(float)2;
	
	/**
	 * l'echelle de la carte a afficher
	 */
	public static float mapScale=(float)2;
	
	/**
	 * la coté d'une tile représenté par un carré sur la carte
	 */
	public static int tileSize = 16;
	
	/**
	 * la largeur d'un bateau dans l'affichage
	 */
	public static int boatWidth = tileSize;
	
	/**
	 * La hauteur d'un bateau dans l'affichage
	 */
	public static int boatHeight = 2*tileSize;
	
	/**
	 * Cette variable permet de passer en Debug mode
	 */
	public static boolean debugMode = false;
	
}
