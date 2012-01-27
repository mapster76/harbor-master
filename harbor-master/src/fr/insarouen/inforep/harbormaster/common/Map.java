package fr.insarouen.inforep.harbormaster.common;

import java.io.Serializable;
import java.util.ArrayList;

import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Circle;
import org.newdawn.slick.geom.Polygon;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.geom.Shape;
import org.newdawn.slick.tiled.TiledMap;

import fr.insarouen.inforep.harbormaster.Chocobo;

public class Map implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private ArrayList<FormeCase> contours;
	
	private ArrayList<Shape> bords;

	private ArrayList<Port> ports;

	private transient TiledMap tiledMap;

	private int height = 0;

	private int width = 0;
	
	private int mapHeight = 0;
	
	private int mapWidth = 0;
	
	public Chocobo chocobo;
	
	public Position departChocobo;
	
	public ArrayList<Shape> polygoneChocobo; 
	

	public Map(String nomMap) throws SlickException {
		tiledMap = new TiledMap("maps/" + nomMap, "maps");

		mapWidth = tiledMap.getWidth(); 
		mapHeight= tiledMap.getHeight(); 
		width = mapWidth*Globals.tileSize;
		height = mapHeight*Globals.tileSize;
		contours = new ArrayList<FormeCase>();
		ports = new ArrayList<Port>();
		bords = new ArrayList<Shape>();
		departChocobo=new Position();
		polygoneChocobo=new ArrayList<Shape>();
		parcourirMapEtInitialiserObjets();
		initialiserBords();
	}
	
	private void initialiserBords() {
		float ecart = (float)(Globals.tileSize*1.5);
		bords.add(new Rectangle(-ecart, 0, Globals.tileSize, height)); // gauche
		bords.add(new Rectangle(width+ecart, 0, Globals.tileSize, height)); // droite
		bords.add(new Rectangle(0, -ecart, width, Globals.tileSize)); // haut 
		bords.add(new Rectangle(0, height+ecart, width, Globals.tileSize)); // bas
		
	}

	private void parcourirMapEtInitialiserObjets() {
		ArrayList<FormeCase> polygones = new ArrayList<FormeCase>();
		for (int y = 0; y < height; y+=Globals.tileSize) {
			for (int x = 0; x < width; x+=Globals.tileSize) {
				int tileCollisionID = tiledMap.getTileId(x/Globals.tileSize, y/Globals.tileSize, 0);
				int directionID = tiledMap.getTileId(x/Globals.tileSize, y/Globals.tileSize, 1);
				Position direction=null;
				// on regarde la direction si y en a une
				if (directionID == 13) 
					{direction = new Position(0,-1);} // haut
				else if (directionID == 14) 
					{direction = new Position(0,1);} // bas
				else if (directionID == 15) 
					{direction = new Position(-1,0);} // gauche
				else if (directionID == 16) 
					{direction = new Position(1,0);} // droite
				else if (directionID == 18) {chocobo = new Chocobo(new Position(x, y));} // chocobo !
				if (tileCollisionID == 1) { // TERRE
					polygones.add(new FormeCase(Case.TERRE, new Rectangle(x, y, Globals.tileSize, Globals.tileSize)));
				} else if (tileCollisionID == 2) { // PORT_BRUN
					ports.add(new Port(CouleurBateau.BRUN, new FormeCase(Case.PORT_BRUN,new Circle(x+Globals.tileSize/2, y+Globals.tileSize/2, (float)(Math.sqrt(2)/2)*Globals.tileSize)), new Position(x, y), direction));
				} else if (tileCollisionID == 3) { // PORT_VERT
					ports.add(new Port(CouleurBateau.VERT, new FormeCase(Case.PORT_VERT,new Circle(x+Globals.tileSize/2, y+Globals.tileSize/2, (float)(Math.sqrt(2)/2)*Globals.tileSize)), new Position(x, y), direction));
				} else if (tileCollisionID == 8) { // DIAGONALE_BAS_DROITE
					polygones.add(new FormeCase(Case.TERRE_DIAGONALE_BAS_DROITE,new Polygon(new float[]{
							x+Globals.tileSize, y,
			                x+Globals.tileSize, y+Globals.tileSize,
			                x, y+Globals.tileSize})));
				} else if (tileCollisionID == 9) { // DIAGONALE_BAS_GAUCHE
					polygones.add(new FormeCase(Case.TERRE_DIAGONALE_BAS_GAUCHE,new Polygon(new float[]{
			                x, y,
			                x+Globals.tileSize, y+Globals.tileSize,
			                x, y+Globals.tileSize})));
				} else if (tileCollisionID == 10) { // DIAGONALE_HAUT_DROITE
					polygones.add(new FormeCase(Case.TERRE_DIAGONALE_HAUT_DROITE,new Polygon(new float[]{
			                x, y,
			                x+Globals.tileSize, y,
			                x+Globals.tileSize, y+Globals.tileSize})));
				} else if (tileCollisionID == 11) { // DIAGONALE_HAUT_GAUCHE
					polygones.add(new FormeCase(Case.TERRE_DIAGONALE_HAUT_GAUCHE,new Polygon(new float[]{
							x, y,
							x+Globals.tileSize, y,
							x, y+Globals.tileSize})));
				} else if (tileCollisionID ==  17) { // COLLISION_CHOCOBO
					polygoneChocobo.add(new Rectangle(x, y, Globals.tileSize, Globals.tileSize));
				}
			}
		}
		setContours(polygones);
	}


	/**
	 * Cette methode retourne les positions extreme de la carte ou il y a la mer
	 * Ces positions correspondent eventuellement a des endroits ou peuvent
	 * entrer des nouveaux bateaux
	 * 
	 * @return une liste de positions
	 */
	public ArrayList<Position> getPositionsEntree() {
		ArrayList<Position> positions = new ArrayList<Position>();
		int x = 0;
		int y = 0;
		int tileID = 0;
		// verification a gauche de la carte
		for (y = 0; y < mapHeight; y++) {
			tileID = tiledMap.getTileId(x, y, 0);
			if (tileID==7)
				positions.add(new Position(x*Globals.tileSize, y*Globals.tileSize));
		}

		// verification a droite de la carte
		x = mapWidth - 1;
		for (y = 0; y < mapHeight; y++) {
			tileID = tiledMap.getTileId(x, y, 0);
		if (tileID==7)
				positions.add(new Position(x*Globals.tileSize, y*Globals.tileSize));
		}
		// verification en haut de la carte
		y = 0;
		for (x = 1; x < mapWidth; x++){
			tileID = tiledMap.getTileId(x, y, 0);
		if (tileID==7)
				positions.add(new Position(x*Globals.tileSize, y*Globals.tileSize));
		}

		// verification en bas de la carte
		y = mapHeight - 1;
		for (x = 1; x < mapWidth; x++) {
			tileID = tiledMap.getTileId(x, y, 0);
		if (tileID==7)
				positions.add(new Position(x*Globals.tileSize, y*Globals.tileSize));
		}
		//System.out.println(positions);
		return positions;
	}

	/**
	 * @return la hauteur de la Map
	 */
	public int getHeight() {
		return height;
	}

	public void setContours(ArrayList<FormeCase> contours) {
		this.contours = contours;
	}

	public ArrayList<FormeCase> getContours() {
		return contours;
	}
	
	public void setBords(ArrayList<Shape> bords) {
		this.bords = bords;
	}

	public ArrayList<Shape> getBords() {
		return bords;
	}

	/**
	 * @return la largeur de la carte
	 */
	public int getWidth() {
		return width;
	}

	public ArrayList<Port> getPorts() {
		return ports;
	}
	
	public Case isShapeCollisionAvecMap(Shape shape) {
		for (FormeCase contour : getContours()) {
			if (contour.getBoundingBox().intersects(shape)) {
				return contour.getTypeCase();
			}
		}
		return null;
	}
	
	public boolean isShapeCollisionAvecBords(Shape shape) {
		for (Shape bord : getBords()) {
			if (bord.intersects(shape)) {
				return true;
			}
		}
		return false;
	}

	public TiledMap getTiledMap() {
		return tiledMap;
	}

	
}
