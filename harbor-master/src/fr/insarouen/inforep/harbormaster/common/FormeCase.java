package fr.insarouen.inforep.harbormaster.common;

import java.io.Serializable;

import org.newdawn.slick.geom.Shape;

public class FormeCase implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Shape boundingBox;
	
	private Case typeCase;
	
	public FormeCase(Case typeCase,Shape boundingBox)
	{
		this.boundingBox=boundingBox;
		this.typeCase=typeCase;
	}

	public Shape getBoundingBox() {
		return boundingBox;
	}

	public Case getTypeCase() {
		return typeCase;
	}
	
	public boolean estTypeCase(Case laCase){
		return laCase==this.typeCase;
	}
	
}
