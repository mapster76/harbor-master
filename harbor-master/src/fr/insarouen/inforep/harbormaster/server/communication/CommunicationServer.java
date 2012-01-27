package fr.insarouen.inforep.harbormaster.server.communication;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.restlet.resource.ClientResource;
import org.restlet.resource.ServerResource;

import fr.insarouen.inforep.harbormaster.client.HarborClient;
import fr.insarouen.inforep.harbormaster.common.Bateau;
import fr.insarouen.inforep.harbormaster.common.Jeu;
import fr.insarouen.inforep.harbormaster.common.Position;
import fr.insarouen.inforep.harbormaster.common.communication.ClientAction;
import fr.insarouen.inforep.harbormaster.common.communication.ServerAction;
import fr.insarouen.inforep.harbormaster.exception.InvalidActionException;
import fr.insarouen.inforep.harbormaster.server.MoteurServer;

/**
 * Classe de communication avec les clients.<br />
 * <br />
 * Elle a deux roles :
 * <ul>
 * <li>Repondre aux requetes du client (telles que register, getJeu, etc.)</li>
 * <li>Informer les clients de changements (tels que verouillage d'un bateau par
 * un autre clients, etc.). Il s'agit de quelque chose de similaire a un PUSH.</li>
 * </ul>
 */
public class CommunicationServer extends ServerResource implements HarborServer, Runnable{

	private static Map<Integer, Map<String, Object>> clients;
	private static int nextAvailableId;
	private static MoteurServer leMoteur;//=MoteurServer.demarrerNouvellePartieServeur("map2.tmx");

	public CommunicationServer() {
		if (clients == null) {
			clients = new ConcurrentHashMap<Integer, Map<String, Object>>();
			nextAvailableId = 1;
		}
		
	}

	//appelable par un client
	@Override
	public Jeu getJeu() {
		if(leMoteur!=null) {
			//System.out.println(leMoteur.getLeJeuModele().toString());
			return leMoteur.getLeJeu();
		}
		else 
			return null;
	}
	
	
	//TODO methode static qui permet de lancer le jeu

	@Override
	public Integer action(ServerAction action) {
		// TODO Auto-generated method stub

		//System.out.println("action : " + action + " de la part de "
			//	+ this.getClientInfo().getAddress());

		String actionType = action.getType();

		if (actionType.equals("inscription")) {
			Map<String, Object> client = new ConcurrentHashMap<String, Object>();
			client.put("adresse", "http://"+getClientInfo().getAddress()+":"+action.getParams().get(1)+"/");
			System.out.println(client.get("adresse"));
			client.put("pseudo", (String) action.getParams().get(0));
			System.out.println("Creation de la ressource");
			client.put("resource",
					new ClientResource(""+client.get("adresse")));
			client.put("client", ((ClientResource) client.get("resource"))
					.wrap(HarborClient.class));
			clients.put(nextAvailableId, client);

			if(leMoteur==null)
			{
				leMoteur=new MoteurServer("map2.tmx");
				leMoteur.demarrerNouvellePartieServeur();
				while(leMoteur.getLeJeu()==null){try {
					Thread.sleep(200);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}}
				//initialise le moteur du jeu
				updateJeu(leMoteur.getLeJeu());
				//System.out.println("partie démarrée");
				Thread t=new Thread(this);
				t.start();
				
				
			}
			
			leMoteur.getLeJeu().ajouterJoueur();
			
			//mettre un modèle en static
			// TODO informer le modèle du nouveau client. le client s'est connecté
			//client.getPseudo

			nextAvailableId++;
			return nextAvailableId-1;
		}

		if (actionType.equals("desinscription")) {
			Integer clientId = (Integer) action.getParams().get(0);
			// fermeture de la connexion
			((ClientResource) clients.get(clientId).get("resource")).release();
			clients.remove(clientId);

			leMoteur.getLeJeu().supprimerJoueur();
			// TODO informer le modèle du depart du client.

			return 0;
		}

		if (actionType.equals("verrouillageBateau")) {
			// TODO interface avec le jeu
			//a voir
			Integer userId = (Integer) action.getParams().get(0);
			Integer bateauId = (Integer) action.getParams().get(1);
			//System.out.println("verrouillage bateau");
			for(Bateau leBateau : leMoteur.getLeJeu().getFlotte().getBateaux())
			{
				if(leBateau.getId()==bateauId) {
					leBateau.reinitialiserTrajectoire();
					leBateau.verrouillerBateau(userId);
					return 0;
				}
			}
			// TODO retourner 0 si le bateau été verouillé, -1 sinon
			return -1;
		}

		if (actionType.equals("deverrouillageBateau")) {
			// TODO interface avec le jeu
			Integer userId = (Integer) action.getParams().get(0);
			Integer bateauId = (Integer) action.getParams().get(1);
			for(Bateau leBateau : leMoteur.getLeJeu().getFlotte().getBateaux())
			{
				if(leBateau.getId()==bateauId) {
					leBateau.deverrouillerBateau();
					return 0;
				}
			}
			// TODO retourner 0 si le bateau été deverouillé, -1 sinon
			return -1;
		}

		if (actionType.equals("modificationTrajectoire")) {
			// TODO interface avec le jeu
			Integer userId = (Integer) action.getParams().get(0);
			Integer bateauId = (Integer) action.getParams().get(1);
			List<Position> bufferDesPositions = (List<Position>) action.getParams().get(2);
			
			for(Bateau leBateau : leMoteur.getLeJeu().getFlotte().getBateaux())
			{
				if(leBateau.getId()==bateauId) {
					if(leBateau.estBateauVerrouille() && leBateau.getUserId()==userId)
						leBateau.modifierTrajectoireDepuisListePositions(bufferDesPositions);
					return 0;
				}
			}
			// TODO retourner 0 si la trajectore à été modifiée, -1 sinon
			return -1;
		}

		// si on arrive la y'a un probleme
		System.err
				.println("Le client "
						+ getClientInfo().getAddress()
						+ " a demandé un truc pas certifié ISO : ca sent la non conformité tout ça...");
		return -1;
	}

	public void verrouillerBateau(int userId, int bateauId) {
		// On envoie les messages
		
		for (Integer clientId : clients.keySet()) {
			if (!clientId.equals(Integer.valueOf(userId))) {
				try {
					((HarborClient) clients.get(clientId).get("client"))
							.action(new ClientAction("verrouillageBateau",
									bateauId));
				} catch (InvalidActionException e) {
					e.printStackTrace();
				}
			}
		}
	}

	public void deverrouillerBateau(int userId, int bateauId) {
		// On envoie les messages
		for (Integer clientId : clients.keySet()) {
			if (!clientId.equals(Integer.valueOf(userId))) {
				try {
					((HarborClient) clients.get(clientId).get("client"))
							.action(new ClientAction("deverrouillageBateau",
									bateauId));
				} catch (InvalidActionException e) {
					e.printStackTrace();
				}
			}
		}
	}
		
	
	public void updateJeu(Jeu jeu) {
		// On envoie les messages
		for (Integer clientId : clients.keySet()) {
			try {
				((HarborClient) clients.get(clientId).get("client"))
				.action(new ClientAction("updateJeu",
						jeu));
			} catch (InvalidActionException e) {
				e.printStackTrace();
			}
		}
	}
    @Override 	 
    public void run() { 	 
	 
            while(!clients.isEmpty()) { 	 
                    //System.out.println("updateJeu"); 	 
                    updateJeu(leMoteur.getLeJeu()); 	 
            } 	 
    }

}
