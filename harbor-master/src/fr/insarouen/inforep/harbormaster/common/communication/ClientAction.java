package fr.insarouen.inforep.harbormaster.common.communication;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

import fr.insarouen.inforep.harbormaster.exception.InvalidActionException;

/**
 * Classe permettant de faire une action sur le serveur.<br />
 * Elle permet de transmettre les parametres de maniere propre et securisee et verifie que le type d'action est connu.
 */
public class ClientAction implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * L'ensemble des actions possibles.
	 */
	private static HashSet<String> possibleActions;
	
	/**
	 * Chaine de caracteres representant le type d'action.
	 */
	private String action;
	
	/**
	 * La liste des parametres de l'action.
	 */
	private List<Object> params;
	
	/**
	 * Constructeur de la classe.
	 * @param anAction Une chaine de caractere representant le type d'action.
	 * @param someParams La liste des parametres de l'action.
	 * @throws InvalidActionException
	 */
	public ClientAction(String anAction, Object ... someParams) throws InvalidActionException {
		if(this.estUneActionPossible(anAction)) {
			this.action=anAction;
			this.params=(List<Object>)Arrays.asList(someParams);
			//TODO
		} else {
			throw new InvalidActionException("L'action \""+anAction+"\" est invalide !");
		}
	}
	
	/**
	 * Methode permettant de recuperer le type d'action.
	 * @return Une chaine de caracteres representant le type d'action.
	 */
	public String getType(){
		return this.action;
	}
	
	/**
	 * Methode permettant d'obtenir une {@link ArrayList} des parametres
	 * @return L'ArrayList des parametres.
	 */
	public List<Object> getParams(){
		return this.params;
	}
	
	/**
	 * Methode privee permettant de verifier si une action est connue.<br />
	 * Elle initialise la liste des actions si ce n'est pas deja fait.<br />
	 * Actions possibles :
	 * <ul>
	 * <li>verrouillageBateau (params : Integer bateauId)</li>
	 * <li>deverrouillageBateau (params : Integer bateauId)</li>
	 * <li>envoiJeu (params : Jeu jeu)</li>
	 * </ul>
	 * @param action L'action dont on veut verifier la validite.
	 * @return Le resultat du test.
	 */
	private boolean estUneActionPossible(String action){
		// Si la liste des actions n'existe pas, je l'initialise
		if(possibleActions==null){
			possibleActions=new HashSet<String>();
			possibleActions.add("verrouillageBateau");
			possibleActions.add("deverrouillageBateau");
			possibleActions.add("updateJeu");
		}
		// Je check si �a existe
		return possibleActions.contains(action);
	}

	@Override
	public String toString() {
		return "ClientAction [action=" + action + ", params=" + params + "]";
	}
	
}
