package fr.insarouen.inforep.harbormaster;

import java.io.Serializable;
import java.util.ArrayList;

import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.geom.Shape;
import fr.insarouen.inforep.harbormaster.common.Globals;
import fr.insarouen.inforep.harbormaster.common.Position;

public class Chocobo implements Serializable {

	private Position position;
	private Rectangle contour;
	
	public Chocobo (Position position) {
		this.position = position;
		this.direction = new Position(1,0);
		this.contour = new Rectangle(getPosition().getX()+2, getPosition().getY()+2, Globals.tileSize-4, Globals.tileSize-4);

	}
	
	
	private Position direction;
	public Position getPosition() {
		return position;
	}


	public void setPosition(Position position) {
		this.position = position;
	}


	public Rectangle getContour() {
		return contour;
	}


	public void setContour(Rectangle contour) {
		this.contour = contour;
	}


	
	public Position getDirection() {
		return direction;
	}


	public void setDirection(Position direction) {
		this.direction = direction;
	}


	public void tournerChocoboSiCollisionSinonAvancer(ArrayList<Shape> polygoneChocobo) {
		for (Shape shape : polygoneChocobo) {
			if (this.getContour().intersects(shape)) {
				position=position.soustraire(getDirection());
				position=position.soustraire(getDirection());
				position=position.soustraire(getDirection());
				position=position.soustraire(getDirection());

				if (direction.getX()==0 && direction.getY()==-1) {
					direction = new Position(-1,0);
				} else if (direction.getX()==-1 && direction.getY()==0) {
					direction = new Position(0,1);
				} else if (direction.getX()==0 && direction.getY()==1) {
					direction = new Position(1,0);
				}else if (direction.getX()==1 && direction.getY()==0) {
					direction = new Position(0,-1);
				}

			} else {
				position=position.additionner(direction);
				contour = new Rectangle(position.getX(), position.getY(), Globals.tileSize-4, Globals.tileSize-4);
			}
		}
	}

}
