package fr.insarouen.inforep.harbormaster.common;

import java.io.Serializable;

public class Position implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int x;
	private int y;

	public Position() {
		
	}

	/** Ce constructeur permet d'instancier une position en précisant son abscisse et son ordonnée. */
	public Position (int x, int y) {
		this.x=x;
		this.y=y;
	}

	/** Cette méthode permet d'obtenir l'abscisse de la position. */
	public int getX () {
		return x;
	}

	/** Cette méthode permet d'obtenir l'ordonnée de la position. */
	public int getY () {
		return y;
	}
	
	public String toString() {
		return "( x: "+getX()+", y: "+getY()+" )";
	}
	
	public boolean equals(Object object)
	{
		if(object instanceof Position) {
			return this.x==((Position) object).getX() && this.y==((Position) object).getY();
		} else
			return false;
		
	}
	
	public Position soustraire(Position p){
		return new Position(this.x-p.x,this.y-p.y);
	}
	
	public Position additionner(Position p)
	{
		return new Position(this.x+p.x,this.y+p.y);
	}

}
