package fr.insarouen.inforep.harbormaster.client;

import java.util.Iterator;
import java.util.Timer;
import java.util.TimerTask;

import fr.insarouen.inforep.harbormaster.common.Bateau;
import fr.insarouen.inforep.harbormaster.common.Jeu;
import fr.insarouen.inforep.harbormaster.common.Position;

public class MoteurClient {
	
	private Jeu unJeu;
	
	private Timer leTimer;
	
	private PeriodicUpdate periodicUpdate;
	
	public MoteurClient(Jeu unJeu){
		leTimer = new Timer();
		periodicUpdate = new PeriodicUpdate();
		leTimer.schedule(periodicUpdate, 0, 30);
		this.unJeu=unJeu; 
	}
	
	public void setUnJeu(Jeu leJeu) {
		unJeu=leJeu;
	}
	public Jeu getUnJeu() {
		return unJeu;
	}


	public void predictionFlotte() {
		if(unJeu!=null) {
			for (Iterator<Bateau> i = unJeu.getFlotte().getBateaux().iterator();i.hasNext();) {
				Bateau bateau =i.next();
				bateau.orienterPolygoneBateau();
				predictionPositionBateau(bateau);
			}
		}
	}
	
	public void predictionPositionBateau(Bateau leBateau)
	{
		// si il a une trajectoire, on change sa direction en fonction
		if (!leBateau.getTrajectoire().isEmpty()) {
			Position positionRelative = leBateau.getTrajectoire()
			.getPremierePosition();
			leBateau.setDirection(positionRelative);
			// si le bateau n'a pas de trajectoire on ne fait rien ici
		}
		// dans tous les cas, on set la position du bateau à partir de sa direction
		leBateau.setPosition(new Position(leBateau.getPosition().getX()
				+ leBateau.getDirection().getX(), leBateau.getDirection()
				.getY() + leBateau.getPosition().getY()));
	}
	
	private class PeriodicUpdate extends TimerTask {

		@Override
		public void run() {
			predictionFlotte();
			if(unJeu.estGameOver())
				waitTenSecondAndRestart();
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
}
