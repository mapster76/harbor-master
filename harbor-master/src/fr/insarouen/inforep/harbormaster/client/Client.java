package fr.insarouen.inforep.harbormaster.client;

import java.util.List;

import org.restlet.Server;
import org.restlet.data.Protocol;
import org.restlet.resource.ClientResource;
import org.restlet.resource.ServerResource;

import fr.insarouen.inforep.harbormaster.common.Jeu;
import fr.insarouen.inforep.harbormaster.common.Position;
import fr.insarouen.inforep.harbormaster.common.communication.ClientAction;
import fr.insarouen.inforep.harbormaster.common.communication.ServerAction;
import fr.insarouen.inforep.harbormaster.exception.HarborMasterException;
import fr.insarouen.inforep.harbormaster.exception.InvalidActionException;
import fr.insarouen.inforep.harbormaster.server.communication.HarborServer;

/**
 * Classe devant etre etendue par tous les clients. Elle permet de definir les
 * bases du fonctionnement client-serveur.
 */
public abstract class Client extends ServerResource implements HarborClient {
	/**
	 * Nom d'utilisateur utilise par le client.
	 */
	private static String mName;

	/**
	 * Identifiant de l'utilisateur.
	 */
	private static Integer mUserId;

	/**
	 * Adresse du serveur.
	 */
	private static String mServerAddress;

	/**
	 * Serveur de jeu.
	 */
	private static HarborServer mServer;

	/**
	 * Serveur de communication.
	 */
	private static ClientResource mServerResource;

	/**
	 * Serveur utilisé pour recevoir les notification du serveur
	 */
	private static Server mNotifiationServer;


	/**
	 * Constructeur d'un client.
	 * 
	 * @param name
	 *            Nom d'utilisateur utilise par le client.
	 * @param serverAddress
	 *            Adresse du serveur.
	 * @param notificationServerPort
	 *            the port on whitch the notifications from the server should be
	 *            received
	 */
	public void configure(String name, String serverAddress,
			int notificationServerPort) {
		mName = name;
		mServerAddress = serverAddress;

		// On recupere le serveur sur le reseau
		mServerResource = new ClientResource(mServerAddress);
		mServer = mServerResource.wrap(HarborServer.class);

		mNotifiationServer = new Server(Protocol.HTTP, notificationServerPort,
				this.getClass());
		try {
			mNotifiationServer.start();
		} catch (Exception e) {
			e.printStackTrace();
		}

		this.register();
	}

	/**
	 * Methode permettant de s'enregistrer en tant que joueur apres du serveur.
	 */
	public void register() {
		try {
			mUserId = mServer.action(new ServerAction("inscription", mName,mNotifiationServer.getPort()));
		} catch (HarborMasterException e) {
			e.printStackTrace();
			System.exit(0);
		}
	}

	/**
	 * Methode permettant d'obtenir l'instance du serveur de jeu pour pouvoir
	 * jouer.
	 * 
	 * @return Le serveur de jeu.
	 */
	public HarborServer getServer() {
		return mServer;
	}

	/**
	 * Methode permettant d'obtenir l'identifiant du client.
	 * 
	 * @return L'identifiant du client.
	 */
	public Integer getUserId() {
		return mUserId;
	}

	/**
	 * Methode permettant de quitter le jeu proprement.
	 */
	public void quit() {
		try {
			mServer.action(new ServerAction("desinscription", mUserId));
		} catch (HarborMasterException e) {
			e.printStackTrace();
			System.exit(0);
		}
		mServerResource.release();
	}

	@Override
	public void action(ClientAction action) {
		// TODO Auto-generated method stub
		String actionType = action.getType();

		if (actionType.equals("verrouillageBateau")) {
			verrouillageBateau((Integer) action.getParams().get(0));
		}

		if (actionType.equals("deverrouillageBateau")) {
			deverrouillageBateau((Integer) action.getParams().get(0));
		}

		if (actionType.equals("updateJeu")) {
			updateJeu((Jeu) action.getParams().get(0));
		}
		
		if (actionType.equals("testBateau")) {
			System.out.println(action.getParams().get(0));
		}

	}

	/**
	 * methode appellée par le serveur
	 * 
	 * @param bateauId
	 *            l'id du bateau verouillé par un autre joueur
	 */
	public abstract void verrouillageBateau(int bateauId);

	/**
	 * methode appellée par le serveur
	 * 
	 * @param bateauId
	 *            l'id du bateau deverouillé par un autre joueur
	 */
	public abstract void deverrouillageBateau(int bateauId);

	/**
	 * methode appellée par le serveur
	 * 
	 * @param jeu
	 *            le nouveau jeu
	 */
	public abstract void updateJeu(Jeu jeu);

	/**
	 * Methode utilisée pour verouiller un bateau
	 * 
	 * @param bateauId le bateau a verouiller
	 * @return 0 si tout s'est bien passé, un nombre negatif en cas d'erreur
	 */
	public int verrouillerBateau(int bateauId) {
		try {
			return mServer.action(new ServerAction("verrouillageBateau",
					mUserId, bateauId));
		} catch (InvalidActionException e) {
			e.printStackTrace();
			return -1;
		}
	}
	
	public Jeu getJeu() {
		return mServer.getJeu();
	}

	/**
	 * Methode utilisée pour deverouiller un bateau
	 * 
	 * @param bateauId le bateau a deverouiller
	 * @return 0 si tout s'est bien passé, un nombre negatif en cas d'erreur
	 */
	public int deverrouillerBateau(int bateauId) {
		try {
			return mServer.action(new ServerAction("deverrouillageBateau",
					mUserId, bateauId));
		} catch (InvalidActionException e) {
			e.printStackTrace();
			return -1;
		}
	}

	/**
	 * Methode utilisée pour modifier la trajectoire d'un bateau
	 * 
	 * @param bateauId le bateau dont il faut modifier la trajectoire
	 * @param trajectoire la nouvelle trajectoire
	 * @return 0 si tout s'est bien passé, un nombre negatif en cas d'erreur
	 */
	public int modifierTrajectoire(int bateauId, List<Position> bufferDesPositionsAbsoluesATracer) {
		new Thread(new EnvoiTrajectoire(bateauId, bufferDesPositionsAbsoluesATracer)).start();
		return 0;
	}
	
	private class EnvoiTrajectoire implements Runnable {
		private int mBateauId;
		private List<Position> mBufferDesPositionsAbsoluesATracer;
		
		public EnvoiTrajectoire(int bateauId, List<Position> bufferDesPositionsAbsoluesATracer) {
			mBateauId=bateauId;
			mBufferDesPositionsAbsoluesATracer=bufferDesPositionsAbsoluesATracer;
		}
		
		public void run() {
			try {
				mServer.action(new ServerAction("modificationTrajectoire",
						mUserId, mBateauId, mBufferDesPositionsAbsoluesATracer));
			} catch (InvalidActionException e) {
				e.printStackTrace();
			}
			
		}
		
	}
}
