package fr.insarouen.inforep.harbormaster.server;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Timer;
import java.util.TimerTask;

import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;

import fr.insarouen.inforep.harbormaster.client.ordi.Souris;
import fr.insarouen.inforep.harbormaster.common.Affichage;
import fr.insarouen.inforep.harbormaster.common.Bateau;
import fr.insarouen.inforep.harbormaster.common.Case;
import fr.insarouen.inforep.harbormaster.common.CouleurBateau;
import fr.insarouen.inforep.harbormaster.common.Globals;
import fr.insarouen.inforep.harbormaster.common.Jeu;
import fr.insarouen.inforep.harbormaster.common.Map;
import fr.insarouen.inforep.harbormaster.common.Port;
import fr.insarouen.inforep.harbormaster.common.Position;
import fr.insarouen.inforep.harbormaster.common.Trajectoire;

/**
 * Cette classe contient les méthodes permettant de creer des trajectoires et gérer les collisions 
 * avec les autres bateaux. Elle doit être instancier une seule et unique fois sur le serveur.
 * @author pilli
 *
 */
/**
 * @author pilli
 *
 */
public class MoteurServer extends Affichage implements Runnable{
	
	private Jeu unJeu;
	
	private Map map;
	
	private String nomMap;
	
	private Timer leTimer;
	
	private int compteurCreationBateau=0;
	
	private int niveau;
	
	private PeriodicUpdate periodicUpdate;
	
	private static boolean jeuLocal=false;
	
	/**
	 * Les positions d'entrée possible
	 */
	private static ArrayList<Position> lesPositionsEntreesPossibles;
	
	public MoteurServer(String nomMap) {
		super("Harbor-Master serveur");
		this.nomMap=nomMap;
		this.niveau=1;
	}
	
	public Jeu getLeJeu() {
		return unJeu;
	}

	public void setLeJeu(Jeu leJeu) {
		unJeu= leJeu;
	}

	public void demarrerNouvellePartieServeur() {
		Thread t=new Thread(this);
		t.start();
		System.out.println("Serveur en cours de démarrage");
	}
	
	/**
	 * Change la direction du bateau s'il atteint un bord (et qu'il n'est pas
	 * chargé !)
	 * 
	 * @param leBateau
	 * @param positionRelative
	 * @param tailleMapX
	 * @param tailleMapY
	 * @return
	 */
	public Position changerDirectionOuSupprimerBateauSiBord(Bateau leBateau, Position positionRelative, int tailleMapX, int tailleMapY) {
		Position directionDeBase = positionRelative;
		positionRelative = tournerBateauSiCollisionAvecMapOuBord(leBateau,
				directionDeBase);
		// si on lui a dit de tourner, on déselectionne le bateau si besoin, et
		// on fait que réinitialiser sa trajectoire sinon
		if (!directionDeBase.equals(positionRelative)) {
			// décalage anti-bug
			for (int i=0; i<Globals.tileSize/2; i++) {
				leBateau.setPosition(leBateau.getPosition().additionner(positionRelative));
			}
			if (leBateau.estBateauVerrouille()) {
				leBateau.deverrouillerBateau();
			} else {
				leBateau.reinitialiserTrajectoire();
			}
		}
		return positionRelative;
	}

	public Position tournerBateauSiCollisionAvecMapOuBord(Bateau leBateau, Position positionRelative) {
		// si on tape les côtes, on tourne a 45° le bateau
		Position collisionMap=tournerBateauSelonTypeTerre(positionRelative, map.isShapeCollisionAvecMap(leBateau.getEllipse()));
		if(collisionMap!=null){
			return collisionMap;
		}
		else if (map.isShapeCollisionAvecBords(leBateau.getEllipse()) && !leBateau.estCharge()) {
			return new Position(-leBateau.getDirection().getX(), -leBateau.getDirection().getY());
		// si on tape les bords et qu'on est chargé, on supprime le bateau
		} else if (map.isShapeCollisionAvecBords(leBateau.getEllipse()) && leBateau.estCharge() && !leBateau.estAuPort()) {
			unJeu.incrementerScore();
			leBateau.setSurLaCarte(false);
			unJeu.getFlotte().supprimerBateau(leBateau);
			return new Position();
		}

		return positionRelative;
	}

	public Position tournerBateauSelonTypeTerre(Position direction,Case typeTerre) {

		if (typeTerre == Case.TERRE_DIAGONALE_BAS_DROITE) {
			if (direction.getX() == 1 && direction.getY() == 0) {
				return new Position(0, -1);
			} else if (direction.getX() == 0 && direction.getY() == 1) {
				return new Position(-1, 0);
			}
		} else if (typeTerre == Case.TERRE_DIAGONALE_BAS_GAUCHE) {
			if (direction.getX() == 0 && direction.getY() == 1) {
				return new Position(1, 0);
			} else if (direction.getX() == -1 && direction.getY() == 0) {
				return new Position(0, -1);
			}
		} else if (typeTerre == Case.TERRE_DIAGONALE_HAUT_GAUCHE) {
			if (direction.getX() == 0 && direction.getY() == -1) {
				return new Position(1, 0);
			} else if (direction.getX() == -1 && direction.getY() == 0) {
				return new Position(0, 1);
			}
		} else if (typeTerre == Case.TERRE_DIAGONALE_HAUT_DROITE) {
			if (direction.getX() == 0 && direction.getY() == -1) {
				return new Position(-1, 0);
			} else if (direction.getX() == 1 && direction.getY() == 0) {
				return new Position(0, 1);
			}
		} else if (typeTerre == Case.TERRE) {
			return new Position(-direction.getX(), -direction.getY());
		}
		return null;
	}


	/**
	 * méthode mettant à jour la position du bateau
	 * 
	 * @param mapWidth
	 * @param mapHeight
	 * @param leBateau
	 */
	public void rafraichirBateau(int mapWidth, int mapHeight,Bateau leBateau) {
		for (Port port : map.getPorts()) {
			// si le bateau arrive en collision avec le bon port et qu'il n'y était pas déjà
			if (port.getContour().getBoundingBox().intersects(leBateau.getEllipse()) && port.getTypePort()==leBateau.getType() && !leBateau.estAuPort()) {
				//System.out.println("hurray !");
				leBateau.mettreAuPort(port);
				leBateau.setPosition(port.getPosition());
				if (leBateau.estBateauVerrouille()) {
					leBateau.deverrouillerBateau();
				} else {
					leBateau.reinitialiserTrajectoire();
				}
			}
		}
		//si le bateau était au port mais qu'il en est sorti
		if(leBateau.estAuPort() && !leBateau.getPort().getContour().getBoundingBox().intersects(leBateau.getEllipse())) {
			leBateau.sortirDuPort();
		}
		// si le bateau est au port et qu'il est pas encore chargé, on le remplit
		else if (leBateau.estAuPort() && !leBateau.estCharge()) {
			
			leBateau.charger();
		// le reste du temps
		} else {
			// si il a une trajectoire, on change sa direction en fonction
			if (!leBateau.getTrajectoire().isEmpty()) {
				Position positionRelative = leBateau.getTrajectoire()
				.getPremierePosition();
				leBateau.setDirection(changerDirectionOuSupprimerBateauSiBord(
						leBateau, positionRelative, mapWidth, mapHeight));
				// si le bateau n'a pas de trajectoire
			} else {
				leBateau.setDirection(changerDirectionOuSupprimerBateauSiBord(
						leBateau, leBateau.getDirection(), mapWidth, mapHeight));

			}
			// dans tous les cas, on set la position du bateau à partir de sa
			// direction
				leBateau.setPosition(new Position(leBateau.getPosition().getX()
					+ leBateau.getDirection().getX(), leBateau.getDirection()
					.getY() + leBateau.getPosition().getY()));
		}
	}
	
	/**
	 * cette méthode génere la flotte si des places sont disponibles 
	 */
	public synchronized void genererFlotte()
	{
		Position positionInitiale,direction;
		Trajectoire trajectoireInitiale;
		for (Iterator<Bateau> i = unJeu.getFlotte().getBateaux().iterator();i.hasNext();) {
				Bateau leBateau=i.next();
			if(!leBateau.estSurLaCarte()) {
				positionInitiale=creerPositionInitiale();
				if(!positionInitiale.equals(new Position(-Globals.boatWidth,-Globals.boatHeight))) {
					direction = creerDirectionInitiale(positionInitiale);
					trajectoireInitiale=new Trajectoire(direction);
					leBateau.setPosition(positionInitiale);
					leBateau.setDirection(direction);
					leBateau.setTrajectoire(trajectoireInitiale);
					leBateau.setSurLaCarte(true);
				}
			}	
		}
	}

	

	/** Cette méthode génère une position initiale d'un bateau, en prenant en compte la position actuelle des autres bateaux 
	 * 
	 */
	private synchronized Position creerPositionInitiale () {
		
		//System.out.println(lesPositionsPossibles.toString());
		boolean positionIndisponible=false;
		for(Iterator<Bateau> i = unJeu.getFlotte().getBateaux().iterator();i.hasNext();)
		{
			Bateau leBateau=i.next();
			Position laPositionTeste=leBateau.getPosition();
			positionIndisponible=false;
			for(Iterator<Position> iterator=lesPositionsEntreesPossibles.iterator();iterator.hasNext() && !positionIndisponible;)
			{
				Position laPositionPossible=iterator.next();
				if(laPositionPossible.equals(laPositionTeste)) 
				{
					lesPositionsEntreesPossibles.remove(laPositionTeste);
					positionIndisponible=true;
				}
			}
			
		}
		if(!lesPositionsEntreesPossibles.isEmpty())
		{
			//System.out.println("création réussi"+lesPositionsEntreesPossibles.size());
			Position laPositionPossibleHasard=lesPositionsEntreesPossibles.get((int)(Math.random() * (lesPositionsEntreesPossibles.size())));
			return laPositionPossibleHasard;
		}
		else
		{
			//System.out.println("Echec création");
			return new Position(-Globals.boatWidth,-Globals.boatHeight);
		}
	}

	/** Cette méthode crée la Trajectoire initiale à donner au nouveau Bateau. 
	 * @param map TODO*/
	private Position creerDirectionInitiale ( Position positionInitiale) {
		if (positionInitiale.getX()==(int)(map.getWidth()-Globals.tileSize)) {
			if (positionInitiale.getY()==(int)(map.getHeight()-Globals.tileSize)) { // en haut à droite
				return new Position(-1, -1);
			}
			else if (positionInitiale.getY()==0) { // en bas à droite
				return new Position(-1, 1);
			}
			else { // n'importe où à droite
				return new Position(1, 0);
			}
		}
		else if (positionInitiale.getX()==0) {
			if (positionInitiale.getY()==map.getHeight()-Globals.tileSize) { // en haut à gauche
				return new Position(1, -1);
			}
			else if (positionInitiale.getY()==0) { // en bas à gauche
				return new Position(1, 1);
			}
			else { // n'importe où à gauche
				return new Position(-1, 0);
			}
		}
		else {
			if (positionInitiale.getY()==map.getHeight()-Globals.tileSize) { // n'importe où en haut
				return new Position(0, 1);
			}
			else if (positionInitiale.getY()==0) { // n'importe où en bas
				return new Position(0, -1);
			}
		}
		return new Position(0,0);
		
	}


	/**
	 * méthode mettant à jour la position de tous les bateaux de la flotte
	 */
	public void rafraichirFlotte() {
		int mapWidth = map.getWidth();
		int mapHeight = map.getHeight();
		genererFlotte();
		// si le bateau sélectionné n'est pas arrêté au port
		for (Iterator<Bateau> i = unJeu.getFlotte().getBateaux().iterator();i.hasNext();) {
			Bateau leBateau=i.next();
			if(leBateau!=null)
				rafraichirBateau(mapWidth, mapHeight, leBateau);
			
		}
	}

	public boolean collisionEntreBateaux() {
		ArrayList<Bateau> dejaTestes = new ArrayList<Bateau>();
		for (Iterator<Bateau> i = unJeu.getFlotte().getBateaux().iterator();i.hasNext();) {
			Bateau leBateau1 =i.next();
			for (Iterator<Bateau> j = unJeu.getFlotte().getBateaux().iterator();j.hasNext();) {
			Bateau leBateau2 =j.next();
				if (leBateau1 != leBateau2
						&& leBateau1.isCollisionWith(leBateau2)) {
					return true;
				}
				dejaTestes.add(leBateau1);
			}
		}
		return false;
	}

	
	public void generateurDeBateau(int level) {
		lesPositionsEntreesPossibles = map.getPositionsEntree();
		int typeBateau;
		int timeBeforeRespawn;
		//on initialise le temps de démmarage en fonction du nombre de jouer ou a défaut si persomme
		if(unJeu.getNombreDeJoueurs()!=0)
			timeBeforeRespawn=200/(unJeu.getNombreDeJoueurs());
		else
			timeBeforeRespawn=200;
		//si le temps de démmarrage est inférieure à 50 on le met a 50 pour éviter les collisions d'aparition de bateau
		if(timeBeforeRespawn<50)
			timeBeforeRespawn=50;
		
		//a chaque appel on regarde si on doit faire apparaitre des bateaux
		if(compteurCreationBateau<timeBeforeRespawn) {
			compteurCreationBateau++;
		} else {
			//on crée autant de bateau que de joueur avec une couleur au hasard
			if(unJeu.getNombreDeJoueurs()<3)
				typeBateau=(int)(Math.random()*unJeu.getNombreDeJoueurs());
			else
				typeBateau=(int)(Math.random()*3);
				for(int i=0;i<level;i++) {
					if(typeBateau==0) {
						unJeu.getFlotte().ajouterBateau(CouleurBateau.VERT);
					}
					if(typeBateau==1) {
						unJeu.getFlotte().ajouterBateau(CouleurBateau.BRUN);
					}
			}
			compteurCreationBateau=0;
		}
	}
	
	
	/**
	 * Classe gérant la mise à jour périodique des positions et autres artéfacts
	 * 
	 */
	private class PeriodicUpdate extends TimerTask {

		@Override
		public void run() {
			rafraichirFlotte();
			for (Iterator<Bateau> i = unJeu.getFlotte().getBateaux().iterator();i.hasNext();) {
				Bateau bateau =i.next();
				bateau.orienterPolygoneBateau();
			}
			if (collisionEntreBateaux()) {
				unJeu.setGameOver(true);
				waitTenSecondAndRestart();
				//System.out.println("Game over !");
			}
			generateurDeBateau(niveau);
		}
	}
	
	private void waitTenSecondAndRestart() {
		try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		if(periodicUpdate!=null) periodicUpdate.cancel();
		periodicUpdate=new PeriodicUpdate();
		leTimer.schedule(periodicUpdate, 0, 30);
		unJeu.restart();
	}

	@Override
    public void update(GameContainer gc, int delta) throws SlickException {
		super.update(gc, delta);
	}
	
	

	@Override
	public void init(GameContainer gc) throws SlickException {
		gc.setAlwaysRender(true);
		this.unJeu=new Jeu(nomMap);
		//System.out.println(unJeu);
		map = new Map(nomMap);
		interfaceClient=new InterfaceServerLocal(unJeu); 
		
		leJeuAfficher=interfaceClient.getJeu();
		if(jeuLocal) {
			Input input=gc.getInput();
			input.addPrimaryListener(new Souris(interfaceClient));
		}
		lierImageAuxElementsDuJeu();
		laCarte=map;
		
		leTimer = new Timer();
		periodicUpdate = new PeriodicUpdate();
		leTimer.schedule(periodicUpdate, 0, 30);
		
		lierImageAuxElementsDuJeu();
		
		lesPositionsEntreesPossibles = map.getPositionsEntree();
		/*unJeu.getFlotte().ajouterBateau(CouleurBateau.VERT);
		unJeu.getFlotte().ajouterBateau(CouleurBateau.BRUN);
		unJeu.getFlotte().ajouterBateau(CouleurBateau.VERT);
		unJeu.getFlotte().ajouterBateau(CouleurBateau.BRUN);*/
		
		//genererFlotte();
	}

	@Override
	public void run() {
		try {
			AppGameContainer app = new AppGameContainer(this);
			app.setDisplayMode((int)(app.getWidth()*Globals.windowScale),(int)(app.getHeight()*Globals.windowScale), false);
			app.start();
		} catch (SlickException e) {
			e.printStackTrace();
		}
		
	}
	
	public static void main(String args[])
	{
		jeuLocal=true;
		MoteurServer leMoteur=new MoteurServer("map2.tmx");
		leMoteur.demarrerNouvellePartieServeur();
	}

		
}
	