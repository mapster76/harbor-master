package fr.insarouen.inforep.harbormaster.client.ordi;

import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArrayList;

import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Shape;

import fr.insarouen.inforep.harbormaster.client.Client;
import fr.insarouen.inforep.harbormaster.client.EvenementClient;
import fr.insarouen.inforep.harbormaster.client.MoteurClient;
import fr.insarouen.inforep.harbormaster.common.Affichage;
import fr.insarouen.inforep.harbormaster.common.Bateau;
import fr.insarouen.inforep.harbormaster.common.Globals;
import fr.insarouen.inforep.harbormaster.common.Jeu;
import fr.insarouen.inforep.harbormaster.common.Position;

public class ClientOrdi extends Client implements EvenementClient{
	// NOTE : les trois fonctions redefinies ici sont des fonctions appellées
	// par le serveur pour indiquer des changements dans l'etat du jeu, pour
	// modifier l'etat du jeu, vous avez a votre disposition les fonctions
	// verouillerBateau, deverouillerBateau et modifierTrajectoireDepuisListePositions
	
	private Affichage affichageClient;
	
	private MoteurClient leMoteurClient;
	
	private static Jeu jeu;
	
	private List<Position> bufferDesPositionsAbsoluesATracerCoteServeur;
	
	private List<Position> bufferDesPositionsAbsoluesATracerCoteClient;
	
	private Bateau leBateauVerouille;
	
	public ClientOrdi() {
		super();
		
	}
	
	public Jeu getJeu()
	{
		if(leMoteurClient!=null) {
			leMoteurClient.setUnJeu(ClientOrdi.jeu);
		}
		return leMoteurClient.getUnJeu();
	}
	
	@Override
	public void verrouillageBateau(int bateauId) {
		
		System.out.println("le bateau numéro "+bateauId+"a été modifier");
		// cette methode est appellée par le serveur => mettre a jour le modele
	}

	@Override
	public void deverrouillageBateau(int bateauId) {
		// TODO Auto-generated method stub
		// cette methode est appellée par le serveur => mettre a jour le modele

	}
	
	@Override
	public void updateJeu(Jeu jeu) {
		ClientOrdi.jeu=jeu;
	}

	@Override
	public void seConnecter() {
		
		// TODO Auto-generated method stub
		
	}

	@Override
	public void selectionnerBateau(int xPositionClic, int yPositionClic) {
		float x = (float) (xPositionClic / Globals.windowScale);
		float y = (float) (yPositionClic / Globals.windowScale);
		Set<Bateau> lesBateaux = jeu.getFlotte().getBateaux();
		Shape lePolygoneDuBateau;
		boolean bateauVerrouille = false;
		for (Iterator<Bateau> i = lesBateaux.iterator(); i.hasNext() && !bateauVerrouille;) {
			
			Bateau leBateau = i.next();
			lePolygoneDuBateau = leBateau.getEllipse();
			if (lePolygoneDuBateau.contains(x, y) && !leBateau.estBateauVerrouille()) {
				bufferDesPositionsAbsoluesATracerCoteServeur=null;
				bufferDesPositionsAbsoluesATracerCoteClient=null;
				leBateau.verrouillerBateau();
				verrouillerBateau(leBateau.getId());
				leBateauVerouille=leBateau;
				bateauVerrouille = true;
			}
		}
		
	}

	@Override
	public void deselectionnerBateau(int x, int y) {
		if(leBateauVerouille!=null) {
			deverrouillerBateau(leBateauVerouille.getId());
			leBateauVerouille=null;
		}
	}

	@Override
	public void tracerTrajectoire(int xOld, int yOld, int xNew, int yNew) {
		if (leBateauVerouille != null ){
			if(bufferDesPositionsAbsoluesATracerCoteClient == null) {
				bufferDesPositionsAbsoluesATracerCoteClient=new CopyOnWriteArrayList<Position>();
			}
			bufferDesPositionsAbsoluesATracerCoteClient.add(new Position((int)(xNew/Globals.windowScale), (int)(yNew/Globals.windowScale)));
			
			boolean bateauTrouve=false;
			Set<Bateau> lesBateaux = jeu.getFlotte().getBateaux();
			
			if(bufferDesPositionsAbsoluesATracerCoteClient != null && bufferDesPositionsAbsoluesATracerCoteClient.size() > 1) {
				for (Iterator<Bateau> i = lesBateaux.iterator(); i.hasNext() && !bateauTrouve;) {
					Bateau leBateau = i.next();
					if(leBateauVerouille!=null && leBateauVerouille.getId()==leBateau.getId()) {
						leBateauVerouille=leBateau;
						leBateauVerouille.modifierTrajectoireDepuisListePositions(bufferDesPositionsAbsoluesATracerCoteClient);
						bateauTrouve=true;
						
						
					}
				}
				bufferDesPositionsAbsoluesATracerCoteClient = null;	
				
			}
			if(bufferDesPositionsAbsoluesATracerCoteServeur == null) {
				bufferDesPositionsAbsoluesATracerCoteServeur=new CopyOnWriteArrayList<Position>();
			}
			bufferDesPositionsAbsoluesATracerCoteServeur.add(new Position((int)(xNew/Globals.windowScale), (int)(yNew/Globals.windowScale)));
		
			
			if(bufferDesPositionsAbsoluesATracerCoteServeur != null && bufferDesPositionsAbsoluesATracerCoteServeur.size() > 4) {
				modifierTrajectoire(leBateauVerouille.getId(), bufferDesPositionsAbsoluesATracerCoteServeur);
				bufferDesPositionsAbsoluesATracerCoteServeur = null;	
			}
		}
	}

	@Override
	public void seDeconnecter() {
		// TODO Auto-generated method stub
		
	}

	public void start() {
		// TODO Auto-generated method stub
		jeu=super.getJeu();
		leMoteurClient=new MoteurClient(jeu);
		affichageClient=new Affichage("Harbor-client");
		affichageClient.setInterfaceClient(this);
		try {
			affichageClient.demarrerAffichageClient();
			System.out.println("affichage demaré");
		} catch (SlickException e) {
			e.printStackTrace();
		}

		
	}
	
	

	
}
