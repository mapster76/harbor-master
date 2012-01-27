package fr.insarouen.inforep.harbormaster.client.ordi;
import org.newdawn.slick.util.InputAdapter;

import fr.insarouen.inforep.harbormaster.client.EvenementClient;


/**
 * La souris est un objet permettant de prendre en compte les mouvements effectues par l utilisateur a la souris
 * @author julie et franfran
 *
 */
public class Souris extends InputAdapter {
	/**
	 * L objet InterfaceServerLocal rattache a la souris
	 */
	private EvenementClient interfaceClient;

	public Souris(EvenementClient interfaceClientLocal){
		super();
		this.interfaceClient=interfaceClientLocal;
	}
	
	private int counter;
	
	@Override
	public void mousePressed(int button,int x,int y)
	{
		counter=0;
		interfaceClient.selectionnerBateau(x,y);
	}


	/**
	 * Methode permettant de recuperer le mouvement de la souris 
	 * @param xOld abcisse du debut du mouvement (lors du clic)
	 * @param yOld ordonnee du debut du mouvement (lors du clic)
	 * @param xNew abcisse de la fin du mouvement (relachement de la souris)
	 * @param yNew ordonnee de la fin du mouvement (relachement de la souris)
	 */
	@Override
	public void mouseDragged(int xOld, int yOld, int xNew, int yNew){
		counter++;
		if(counter>3)
			interfaceClient.tracerTrajectoire(xOld,yOld,xNew,yNew);
	}
	
	
	/**
	 * methode permettant de verifier si la souris a été relaché
	 */
	@Override
	public void mouseReleased(int button,int x,int y) {
		interfaceClient.deselectionnerBateau(x, y);
	}

}
