package fr.insarouen.inforep.harbormaster.common;

import java.io.Serializable;
import java.util.Iterator;
import java.util.concurrent.LinkedBlockingQueue;
/**
 * Cette classe modélise une trajectoire qui pourra être suivie par un bateau.
 */
public class Trajectoire implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private LinkedBlockingQueue<Position> positions;

	public String toString() {
		int compteur=0;
		String resultat="[";
		for (Iterator<Position> i = positions.iterator(); i.hasNext();) {
			Position laPosition = i.next();
			if(compteur<6) {
				resultat+=laPosition.toString()+",";
			} else {
				compteur=1;
				resultat+="\n"+laPosition.toString()+",";
			}
		}
		resultat+="]";
		return resultat;
	}
	public Trajectoire(Position position) {
		positions=new LinkedBlockingQueue<Position>();
		addPosition(position);
	}

	public Trajectoire() {
		positions=new LinkedBlockingQueue<Position>();
	}

	
	/** Cette méthode permet d'obtenir l'ensemble des points de la trajectoire. */
	public LinkedBlockingQueue<Position> getPositions() {
		return positions;
	}
	
	/**
	 * Cette méthode nous dit si la trajectoire est vide ou non
	 * @return true si elle est vide, false sinon
	 */
	public boolean isEmpty() {
		if (positions.isEmpty()) {
			return true;
		}
		else {
			return false;
		}
	}

	public Position getPremierePosition(){
		if (!positions.isEmpty()) {
			return positions.remove();
		}
		return null;
	}
	public void addPosition(Position position) {
		this.positions.add(position);
	}
	
}
