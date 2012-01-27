package fr.insarouen.inforep.harbormaster.common;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/* Cette classe propose des méthodes permettant
 *  d'obtenir des informations sur l'état du bateau. 
 *  Elle permet aussi de modéliser son fonctionnement.*/
public class Bateau extends Entite implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static int nbBateaux = 0;
	private static final float capacite = 100;
	private int id;
	private int userId;
	private Flotte flotte;
	private float coefficientVitesse = (float) 0.1;
	private boolean surLaCarte;
	private float coefficientChargement = 1;
	private float tauxChargement = 0;
	private Trajectoire trajectoire;
	private Position positionDepartTrajectoire;
	private Position positionFinTrajectoire;
	private boolean charge;
	private Position direction;
	private boolean bateauVerrouille;
	private boolean estAuPort;
	private Port port;

	
	public String toString() {
		String resultat="bateau "+id+" ayant comme couleur le " +getType()+", ";
		if(estAuPort)
		{
			resultat+=" est au port "+port.toString();
		}
		else
		{
			resultat+=" est en train de naviguer";
		}
		if(estCharge())
		{
			resultat+=" chargé";
		}
		else
		{
			resultat+=" à vide";
		}
		resultat+=". Il est à la position "+position.toString()+" avec comme direction"+direction.toString();
			resultat+="\n\t\t avec comme trajectoire"+ trajectoire.toString();
		return resultat;
	}
	/**
	 * Le constructeur de la classe Bateau. Il permet d'instancier un nouveau
	 * bateau avant de le lancer dans le jeu.
	 * 
	 * @param direction
	 *            TODO
	 * @param size
	 *            TODO
	 * @param type
	 *            TODO
	 */
	public Bateau(Flotte flotte, CouleurBateau type) {
		super(type, 0, 0, (float)(Globals.boatWidth / 2.5), (float)(Globals.boatHeight / 2.5));
		id = nbBateaux;
		nbBateaux++;
		this.flotte = flotte;
		charge = false;
		surLaCarte = false;
		position = new Position(-1, -1);
		direction = new Position(0, 0);
		trajectoire = new Trajectoire();
		positionDepartTrajectoire = null;
		positionFinTrajectoire = null;
		estAuPort = false;
	}

	/** Cette méthode permet de récupérer l'identifiant unique d'un bateau. */
	public int getId() {
		return id;
	}

	public Port getPort() {
		return port;
	}

	public void setPort(Port port) {
		this.port = port;
	}

	/** Cette méthode permet d'obtenir la capitainerie qui référence le bateau. */
	public Flotte getFlotte() {
		return flotte;
	}

	/**
	 * Cette méthode permet d'obtenir la vitesse de déplacement du bateau en
	 * fonction de sa longueur et de son coefficient de vitesse.
	 */
	public float getVitesse() {
		return coefficientVitesse;
	}

	/**
	 * Cette méthode permet d'obtenir la vitesse de chargement du bateau en
	 * fonction de sa longueur et de son coefficient de chargement.
	 */
	public float getVitesseChargement() {
		return tauxChargement;
	}

	/** Cette méthode permet d'obtenir le taux de chargement actuel du bateau. */
	public float getTauxChargement() {
		return tauxChargement;
	}

	/**
	 * permet de savoir si un bateau est rempli
	 * 
	 * @return
	 */
	public boolean estEnCoursDeChargement() {
		return getTauxChargement() > 0 && getTauxChargement() != capacite;
	}

	public void charger() {
		setDirection(new Position());
		// s'il est pas encore plein, on le charge
		if (getTauxChargement() != capacite) {
			this.tauxChargement += this.coefficientChargement;
			// s'il est plein, on change la valeur de estCharge
		} else {
			//System.out.println("il est chargé ! ");
			this.charge = true;
		}
	}

	/** Cette méthode renvoie true si le bateau est charge, false sinon **/
	public boolean estCharge() {
		return charge;
	}

	public void mettreAuPort(Port port) {
		estAuPort=true;
		setPort(port);
		//System.out.println("position port : "+port.getPosition());
		//System.out.println("direction port : "+port.getDirection());
		setPosition(port.getPosition());
		setDirection(port.getDirection());
		orienterPolygoneBateau();
	}

	public boolean estAuPort() {
		return estAuPort;
	}
	
	public void sortirDuPort() {
		estAuPort=false;
	}

	/**
	 * Cette méthode permet d'obtenir la trajectoire que le bateau est en train
	 * de suivre.
	 */
	public Trajectoire getTrajectoire() {
		return trajectoire;
	}

	/**
	 * Cette méthode permet de définir la trajectoire que le bateau devra
	 * parcourir.
	 */
	public void setTrajectoire(Trajectoire uneTrajectoire) {
		this.trajectoire = uneTrajectoire;
	}

	public void decharger() {
		charge = false;
	}

	public Position getDirection() {
		return direction;
	}

	public void setDirection(Position direction) {
		this.direction = direction;
	}

	public boolean estSurLaCarte() {
		return surLaCarte;
	}

	public void setSurLaCarte(boolean surLaCarte) {
		this.surLaCarte = surLaCarte;
	}

	private boolean estDernierElementListePositions(
			List<Position> totalPositionsAbsolues, Position element) {
		if (totalPositionsAbsolues.isEmpty()) {
			return false;
		} else {
			return element == totalPositionsAbsolues.get(totalPositionsAbsolues.size() - 1);
		}
	}

	public void modifierTrajectoireDepuisListePositions(
			List<Position> laListeDePositionsAbsolues) {
		Position positionCourante = new Position();
		List<Position> totalPositionsAbsolues = new CopyOnWriteArrayList<Position>();
		List<Position> totalPositionsRelatives = new CopyOnWriteArrayList<Position>();
		List<Position> totalPositionsRelativesEtendues = new CopyOnWriteArrayList<Position>();

		for (Position pos : laListeDePositionsAbsolues) {
			positionCourante = new Position(pos.getX(),pos.getY());
			// si la position courante n'est pas dans ma liste des positions
			// enregistrées
			if (!estDernierElementListePositions(totalPositionsAbsolues,
					positionCourante)) {
				totalPositionsAbsolues.add(positionCourante);
			}
		}
		Position positionAAjouter = this.getPosition();
		// si j'ai une trajectoire, je met la position de fin dans positionAAjouter
		if (!this.getTrajectoire().isEmpty()) {
			positionAAjouter = this.getPositionFinTrajectoire();
		}
		// j'ajoute aux position absolues la position avant l'ajout
		totalPositionsAbsolues.add(0, new Position(positionAAjouter.getX(),
				positionAAjouter.getY()));
		// pour toutes les positions absolues (sauf la dernière)
		if (totalPositionsAbsolues.size() > 1) {
			// System.out.println("positions absolues : "+totalPositionsAbsolues);
			for (int i = 0; i < totalPositionsAbsolues.size() - 1; i++) {
				// on met dans position relative la positionAbsolue[i+1] -
				// posAbsolue[i]
				totalPositionsRelatives.add(totalPositionsAbsolues.get(i + 1)
						.soustraire(totalPositionsAbsolues.get(i)));
			}
			totalPositionsRelativesEtendues = decomposerToutesPositionsSiTropGrandes(totalPositionsRelatives);
		}

		// on ajoute à la trajectoire toutes les positions relatives mergées
		for (Position position : totalPositionsRelativesEtendues) {
			// for (Position position : totalPositionsRelativesMergees) {
			ajouterPositionATrajectoire(position);
		}
	}

	/**
	 * méthode pour décomposer toutes les positions si leurs composantes sont
	 * trop grandes
	 * 
	 * @param positions
	 * @return
	 */
	public List<Position> decomposerToutesPositionsSiTropGrandes(
			List<Position> positions) {
		List<Position> resultat = new ArrayList<Position>();
		for (Position position : positions) {
			resultat.addAll(decomposerSiPositionTropGrande(position));
		}
		return resultat;
	}

	/**
	 * méthode pour décomposer une position
	 * 
	 * @param position
	 * @return
	 */
	public ArrayList<Position> decomposerSiPositionTropGrande(Position position) {
		ArrayList<Position> resultat = new ArrayList<Position>();
		int bufX = Math.abs(position.getX()), bufY = Math.abs(position.getY());
		int xTmp = 0, yTmp = 0;
		// si bufX et bufY sont des positions relatives raisonnables (que des 0
		// ou des 1)
		if (bufX <= 1 && bufY <= 1) {
			resultat.add(position);
			return resultat;
		}
		while (bufX > 0 || bufY > 0) {
			xTmp = 0;
			yTmp = 0;
			if (bufX > 0) {
				bufX--;
				xTmp = (int) Math.signum(position.getX());
			}
			if (bufY > 0) {
				bufY--;
				yTmp = (int) Math.signum(position.getY());
			}
			resultat.add(new Position(xTmp, yTmp));
		}
		return resultat;
	}

	/**
	 * methode pour ajouter une position à une trajectoire
	 * @param positionAAjouter
	 */
	public void ajouterPositionATrajectoire(Position positionAAjouter) {
		if (trajectoire.isEmpty()) {
			setPositionDepartTrajectoire(getPosition());
		}
		if(!positionAAjouter.equals(new Position(0,0)))
			getTrajectoire().addPosition(positionAAjouter);
		if (getPositionFinTrajectoire() != null) {
			setPositionFinTrajectoire(getPositionFinTrajectoire().additionner(
					positionAAjouter));
		} else {
			setPositionFinTrajectoire(getPosition().additionner(
					positionAAjouter));
		}
	}

	public void orienterPolygoneBateau() {
		if (getPort()!=null && getPort().getContour().getBoundingBox().intersects(getEllipse())) {
			setEllipse(getPort().getPosition().getX()+Globals.tileSize/2, getPort().getPosition().getY()+Globals.tileSize/2, (1+Math.abs(getPort().getDirection().getX()))*Globals.tileSize/2, (1+Math.abs(getPort().getDirection().getY()))*Globals.tileSize/2);
		}
		int xRelatif = this.getDirection().getX();
		int yRelatif = this.getDirection().getY();
		int xAbs = (this.getPosition().getX() + Globals.tileSize / 2);
		int yAbs = (this.getPosition().getY() + Globals.tileSize / 2);
		if (xRelatif == 0 && Math.abs(yRelatif) == 1) {
			// nord ou sud
			this.setEllipse(xAbs, yAbs, (float)(Globals.boatWidth / 2.5),
					(float)(Globals.boatHeight / 2.5));
			// nord-est ou sud-ouest
		} else if ((xRelatif == 1 && yRelatif == -1)
				|| (xRelatif == -1 && yRelatif == 1)) {
			this.setEllipse(xAbs, yAbs, (float)(Globals.boatWidth / 2.5),
					(float)(Globals.boatHeight / 2.5));
			this.setEllipse((float) Math.toRadians(45.0), (float) xAbs,
					(float) yAbs);
			// est ou ouest
		} else if (Math.abs(xRelatif) == 1 && yRelatif == 0) {
			this.setEllipse(xAbs, yAbs, (float)(Globals.boatHeight / 2.5),
					(float)(Globals.boatWidth / 2.5));
			// nord-ouest ou sud-est
		} else if ((xRelatif == 1 && yRelatif == 1)
				|| (xRelatif == -1 && yRelatif == -1)) {
			this.setEllipse(xAbs, yAbs, (float)(Globals.boatWidth / 2.5),
					(float)(Globals.boatHeight / 2.5));
			this.setEllipse((float) Math.toRadians(-45.0), (float) xAbs,
					(float) yAbs);
		}

	}

	public Position getPositionDepartTrajectoire() {
		return positionDepartTrajectoire;
	}

	public void setPositionDepartTrajectoire(Position positionDepartTrajectoire) {
		this.positionDepartTrajectoire = positionDepartTrajectoire;
	}

	public Position getPositionFinTrajectoire() {
		return positionFinTrajectoire;
	}

	public void setPositionFinTrajectoire(Position positionFinTrajectoire) {
		this.positionFinTrajectoire = positionFinTrajectoire;
	}

	public void reinitialiserTrajectoire() {
		trajectoire = new Trajectoire();
		positionDepartTrajectoire = null;
		positionFinTrajectoire = null;
	}

	public boolean estBateauVerrouille() {
		return bateauVerrouille;
	}


	public int verrouillerBateau(int userId) {
		this.userId=userId;
		this.bateauVerrouille = true;
		return this.id;
	}
	
	public int verrouillerBateau() {
		this.bateauVerrouille = true;
		return this.id;
	}
	
	public int deverrouillerBateau() {
		//reinitialiserTrajectoire();
		this.bateauVerrouille = false;
		return this.id;
	}
	
	public int getUserId() {
		return userId;
	}

	
}
