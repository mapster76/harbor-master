package fr.insarouen.inforep.harbormaster.common;

import java.io.Serializable;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;


public class Flotte implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Set<Bateau> lesBateaux;
	

	public Flotte() {
		lesBateaux=new CopyOnWriteArraySet<Bateau>();
	}
	
	/** 
	 * Cette méthode crée un nouveau bateau
	 */
	public void ajouterBateau (CouleurBateau couleurBateau) {
		Bateau leBateau=new Bateau(this,couleurBateau);
		lesBateaux.add(leBateau);
	}
	
	public Set<Bateau> getBateaux() {
		return  this.lesBateaux;
	}

	public synchronized void supprimerBateau(Bateau leBateauASupprimer) {
		this.getBateaux().remove(leBateauASupprimer);
	}
	
	public String toString() {
		String resultat="";
		for (Iterator<Bateau> i = lesBateaux.iterator();i.hasNext();) {
			Bateau leBateau=i.next();
			resultat+="\t"+leBateau.toString()+";\n";
		}
		return resultat;
	}
}
