package fr.insarouen.inforep.harbormaster.common;

import java.io.Serializable;

/*Cette classe repésente un port*/
public class Port implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private CouleurBateau typePort;
	private Bateau bateau;
	private int tempsDechargement;
	private FormeCase contour;
	private Position position;
	private Position direction;

	public String toString() {
		return ""+typePort;
	}
	public Port(CouleurBateau couleur, FormeCase contour, Position position, Position direction) {
		setPosition(position);
		setDirection(direction);
		setTypePort(couleur);
		setContour(contour);
	}
	
	public Position getDirection() {
		return direction;
	}
	
	public void setDirection(Position direction) {
		this.direction=direction;
	}

	public Position getPosition() {
		return position;
	}

	public void setPosition(Position position) {
		this.position=position;
	}
	
	public FormeCase getContour() {
		return contour;
	}

	public void setContour(FormeCase contour) {
		this.contour = contour;
	}

	public CouleurBateau getTypePort() {
		return typePort;
	}


	public void setTypePort(CouleurBateau typePort) {
		this.typePort = typePort;
	}


	public Bateau getBateau() {
		return bateau;
	}


	public void setBateau(Bateau bateau) {
		this.bateau = bateau;
	}


	public int getTempsDechargement() {
		return tempsDechargement;
	}


	public void setTempsDechargement(int tempsDechargement) {
		this.tempsDechargement = tempsDechargement;
	}

	/** retourne si le port est occupé */
	public boolean estOccupe () {
		if(bateau!=null)
			return true;
		else return false;
	}
}
