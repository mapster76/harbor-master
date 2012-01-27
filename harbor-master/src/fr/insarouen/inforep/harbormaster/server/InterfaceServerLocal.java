package fr.insarouen.inforep.harbormaster.server;

import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArrayList;

import org.newdawn.slick.geom.Shape;

import fr.insarouen.inforep.harbormaster.client.EvenementClient;
import fr.insarouen.inforep.harbormaster.common.Bateau;
import fr.insarouen.inforep.harbormaster.common.Globals;
import fr.insarouen.inforep.harbormaster.common.Jeu;
import fr.insarouen.inforep.harbormaster.common.Position;

/**
 * Le InterfaceServerLocal est un objet qui permet de gerer les interactions entre le joueur
 * et le client
 * 
 */
public class InterfaceServerLocal implements EvenementClient {
	/**
	 * Le Jeu auquel l objet InterfaceServerLocal est rattache
	 */
	private Jeu leJeu;
	
	private Bateau leBateauVerouille;
	
	private List<Position> bufferDesPositionsAbsoluesATracer;

	//private double scale = 1.5;
	

	/**
	 * Le construteur de la classe InterfaceServerLocal
	 * 
	 * @param jeu
	 *            le Jeu auquel le InterfaceServerLocal est rattache
	 */
	public InterfaceServerLocal(Jeu jeu) {
		//MoteurServer moteur = new MoteurServer(nomMap);
		//moteur.demarrerNouvellePartieClientTest();
		this.leJeu = jeu;
		
	}

	/**
	 * Methode pour se connecter
	 */
	public void seConnecter() {

	}

	/**
	 * Methode de selection d un bateau
	 * 
	 * @param laPosition
	 *            la Position ou le joueur a clique
	 * @return bateau le bateau selectionne, ou un objet null s il n y a pas de
	 *         bateau a cette position
	 */
	public void selectionnerBateau(int xPositionClic, int yPositionClic) {
		float x = (float) (xPositionClic / Globals.windowScale);
		float y = (float) (yPositionClic / Globals.windowScale);
		Set<Bateau> lesBateaux = leJeu.getFlotte().getBateaux();
		Shape lePolygoneDuBateau;
		boolean bateauVerrouille = false;
		for (Iterator<Bateau> i = lesBateaux.iterator(); i.hasNext() && !bateauVerrouille;) {

			Bateau leBateau = i.next();
			lePolygoneDuBateau = leBateau.getEllipse();
			if (lePolygoneDuBateau.contains(x, y)) {
				bufferDesPositionsAbsoluesATracer=null;
				leBateau.reinitialiserTrajectoire();
				leBateau.verrouillerBateau();
				leBateauVerouille=leBateau;
				bateauVerrouille = true;
			} else {
				//leBateau.reinitialiserTrajectoire();
				leBateau.deverrouillerBateau();
			}
			
		}
		//System.out.println(leJeu.getLeBateauSelectionne());
	}

	
	/**
	 * méthode permettant de tracer la trajectoire du bateau selectionné
	 */
	public void tracerTrajectoire(int xOld, int yOld, int xNew, int yNew) {
		
		if (leBateauVerouille != null ){
			if(bufferDesPositionsAbsoluesATracer == null) {
				bufferDesPositionsAbsoluesATracer=new CopyOnWriteArrayList<Position>();
			}
			bufferDesPositionsAbsoluesATracer.add(new Position((int)(xNew/Globals.windowScale), (int)(yNew/Globals.windowScale)));

			if(bufferDesPositionsAbsoluesATracer != null && bufferDesPositionsAbsoluesATracer.size() > 1) {
				leBateauVerouille.modifierTrajectoireDepuisListePositions(bufferDesPositionsAbsoluesATracer);
				bufferDesPositionsAbsoluesATracer = null;	
			}
		}
	}

	/**
	 * Methode pour se deconnecter
	 */
	public void seDeconnecter() {

	}

	@Override
	public void deselectionnerBateau(int x, int y) {
		if(leBateauVerouille!=null)
		{
			//bufferDesPositionsAbsoluesATracer=null;
			leBateauVerouille.deverrouillerBateau();
			leBateauVerouille=null;
		}
	}

	@Override
	public Jeu getJeu() {
		return leJeu;
	}






}
