package fr.insarouen.inforep.harbormaster.common;

import java.io.Serializable;

import org.newdawn.slick.geom.Ellipse;
import org.newdawn.slick.geom.Shape;
import org.newdawn.slick.geom.Transform;

public abstract class Entite implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public Position position;
	public CouleurBateau type;
	public Shape ellipse;


	public Entite(CouleurBateau type, float centerPointX, float centerPointY, float radius1, float radius2) {
		this.position = new Position();
		setType(type);
		this.setEllipse(centerPointX, centerPointY, radius1, radius2);
		
	}
	
	public CouleurBateau getType() {
		return type;
	}

	public void setType(CouleurBateau type) {
		this.type = type;
	}
	
	public void setEllipse(float centerPointX, float centerPointY, float radius1, float radius2) {
		this.ellipse = new Ellipse(centerPointX, centerPointY, radius1, radius2);
	}

	public void setEllipse(float angle, float x, float y) {
		this.ellipse=this.ellipse.transform(Transform.createRotateTransform(angle, x, y));
	}
	
	public Shape getEllipse() {
		return ellipse;
	}
	

	/**
	 * Cette méthode permet d'obtenir la position du bateau sur la grille de
	 * jeu.
	 */
	public Position getPosition() {
		return position;
	}

	/**
	 * Cette méthode permet de définir la position du bateau sur la grille de
	 * jeu.
	 */
	public void setPosition(Position unePosition) {
		position = unePosition;
	}
	
	public boolean isCollisionWith(Entite autre) {
		return ellipse.intersects(autre.ellipse);
	}
}
