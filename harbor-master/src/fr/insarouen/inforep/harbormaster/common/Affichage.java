package fr.insarouen.inforep.harbormaster.common;


import org.newdawn.slick.Animation;
import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.BasicGame;
import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.SpriteSheet;
import org.newdawn.slick.geom.Path;
import org.newdawn.slick.geom.Shape;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.opengl.renderer.Renderer;

import fr.insarouen.inforep.harbormaster.client.EvenementClient;
import fr.insarouen.inforep.harbormaster.client.ordi.Souris;

 
public class Affichage extends BasicGame implements Runnable{
 
    private Image imageBateauVertVide = null;
    private Image imageBateauBrunVide = null;
    private Image imageBateauVertPlein= null;
    private Image imageBateauBrunPlein= null;
    private Image imageGameOver = null;
    private Image imageHalo = null;
    protected Map laCarte;
    protected EvenementClient interfaceClient;
    protected Jeu leJeuAfficher;
    private int compteurLayer;
    private int tempsPasseDepuisDerniereAnimation;
    
    private SpriteSheet imageChocobo;
	public Animation[] chocoboDefault;
	public Animation fallback;
    
    public Affichage(String nomApplication)
    {
        super(nomApplication); 
        tempsPasseDepuisDerniereAnimation=0;
        compteurLayer=3;
    }
    
    public void incrementerCompteurLayer() {
    	if (compteurLayer>=5) {
    		compteurLayer=3;
    	} else {
    		compteurLayer++;
    	}
    }
 
    public void setInterfaceClient(EvenementClient interfaceClient) {
		this.interfaceClient = interfaceClient;
	}



	public Jeu getLeJeuAfficher() {
		return leJeuAfficher;
	}


	public void setLeJeuAfficher(Jeu leJeuAfficher) {
		this.leJeuAfficher = leJeuAfficher;
	}

	
	public void initChocobo() {
		chocoboDefault = new Animation[4]; 
		for (int i=0; i<4; i++) {
			chocoboDefault[i]= new Animation();
			chocoboDefault[i].setAutoUpdate(true);
			for (int number = 0; number < 3; number++) {
				chocoboDefault[i].addFrame(imageChocobo.getSprite(number, i), 150);
			}
		}
		fallback = chocoboDefault[1];

	}

	@Override
    public void init(GameContainer gc) throws SlickException 
    {
		gc.setAlwaysRender(true);
    	//désactive l'affichage du rafraichissement des frames
    	gc.setShowFPS(false);
    	laCarte=new Map("map2.tmx");
    	leJeuAfficher=interfaceClient.getJeu();
    	
    	Input input=gc.getInput();
		input.addPrimaryListener(new Souris(interfaceClient));
		lierImageAuxElementsDuJeu();
        //boat = new Image("image/boat_green.png");
    }

	@Override
    public void update(GameContainer gc, int delta) throws SlickException
    {
		tempsPasseDepuisDerniereAnimation+=delta;
		if (tempsPasseDepuisDerniereAnimation>=200) {
			incrementerCompteurLayer();
			tempsPasseDepuisDerniereAnimation=0;
		}
    }
 
	/**
	 * cette methode est appelé a chaque fois que l'on souhaite afficher les mises a jour sur le dessin
	 */
    public void render(GameContainer gc, Graphics g) throws SlickException
    {
    	leJeuAfficher=interfaceClient.getJeu();
    	g.scale(Globals.mapScale,Globals.mapScale);
		g.setColor(Color.red);
		
		//System.out.println(leJeuAfficher);
    	if(Globals.debugMode) {
    		renderCollisionLayer(g);
    		for (Shape contourchocobo : laCarte.polygoneChocobo) {
    			g.draw(contourchocobo);
    		}
    		renderChocoboSprite(g);
    		g.setColor(Color.yellow);
    		g.draw(laCarte.chocobo.getContour());
    		for(FormeCase leContour : laCarte.getContours()) {
    			g.setColor(Color.green);
    			g.draw(leContour.getBoundingBox());
    			g.setColor(Color.red);
    		}
    		for(Port lePort : laCarte.getPorts())
    		{
    			g.setColor(Color.blue);
    			g.draw(lePort.getContour().getBoundingBox());
    			g.setColor(Color.red);
    		}
    	}
    	else
    		renderFirstLayer(g);
    	for(Bateau unBateau : leJeuAfficher.getFlotte().getBateaux())
    	{
    		if(unBateau!=null)
			{
				afficherBateauVerrouille(gc,g,unBateau);
				afficherTrajectoireExistante(gc, g, unBateau);
				if(Globals.debugMode)
				{
					g.setColor(Color.white);
					g.draw(unBateau.getEllipse());
					
					if(rect!= null)
						g.draw(rect);
					g.setColor(Color.red);
				}
				if(unBateau.getType()==CouleurBateau.VERT)
				{
					afficherBateauVerrouille(gc,g,unBateau);
					if(unBateau.estCharge())
						afficherBateau(gc, g, unBateau, imageBateauVertPlein);
					else
						afficherBateau(gc, g, unBateau, imageBateauVertVide);
				}
				if(unBateau.getType()==CouleurBateau.BRUN)
				{
					afficherBateauVerrouille(gc,g,unBateau);
					if(unBateau.estCharge())
						afficherBateau(gc, g, unBateau, imageBateauBrunPlein);
					else
						afficherBateau(gc, g, unBateau, imageBateauBrunVide);
				}
			}
    	}

    	
    	if(!Globals.debugMode) {
    		renderAnimationLayers(g);
    		renderChocoboSprite(g);
    		if(leJeuAfficher.estGameOver()) {
    			imageGameOver.drawCentered(laCarte.getWidth()/2,laCarte.getWidth()/3);
    		}
    	}
    	afficherScoreEtNombreDeJoueur(g, leJeuAfficher.getScore(), leJeuAfficher.getNombreDeJoueurs());
    }

    /**
     * render du chocobo !
     * @param g
     */
    public void renderChocoboSprite(Graphics g) {
    	laCarte.chocobo.tournerChocoboSiCollisionSinonAvancer(laCarte.polygoneChocobo);
    	if (laCarte.chocobo.getDirection().getX()==0 && laCarte.chocobo.getDirection().getY()==-1) {
    		fallback = chocoboDefault[0];
    	} else if (laCarte.chocobo.getDirection().getX()==1 && laCarte.chocobo.getDirection().getY()==0) {
    		fallback = chocoboDefault[1];
    	} else if (laCarte.chocobo.getDirection().getX()==0 && laCarte.chocobo.getDirection().getY()==1) {
    		fallback = chocoboDefault[2];
    	} else if (laCarte.chocobo.getDirection().getX()==-1 && laCarte.chocobo.getDirection().getY()==0) {
    		fallback = chocoboDefault[3];
    	}
		g.drawAnimation(fallback, laCarte.chocobo.getPosition().getX()-Globals.tileSize/2, laCarte.chocobo.getPosition().getY()-Globals.tileSize);
    }
    
    /**
     * Cette méthode permet d'afficher la couche de collision
     * @param g
     */
    public void renderCollisionLayer(Graphics g)
    {
    	int offsetX=(int) (laCarte.getWidth()*(Globals.windowScale-Globals.mapScale))/2;
		int offsetY=(int) (laCarte.getHeight()*(Globals.windowScale-Globals.mapScale))/2;
		laCarte.getTiledMap().render(offsetX,offsetY,0);
		laCarte.getTiledMap().render(offsetX,offsetY,1);
    }
    
    /**
     * Cette méthode permet d'afficher le fond de carte
     * @param g
     */
    public void renderFirstLayer(Graphics g)
	{
		int offsetX=(int) (laCarte.getWidth()*(Globals.windowScale-Globals.mapScale))/2;
		int offsetY=(int) (laCarte.getHeight()*(Globals.windowScale-Globals.mapScale))/2;
		laCarte.getTiledMap().render(offsetX,offsetY,2);
	}
	
    /**
     * Cette méthode permet d'afficher les décors qui doivent aparaitre logiquement au dessus des bateaux
     * @param g
     */
	public void renderAnimationLayers(Graphics g)
	{
		
		int offsetX=(int) (laCarte.getWidth()*(Globals.windowScale-Globals.mapScale))/2;
		int offsetY=(int) (laCarte.getHeight()*(Globals.windowScale-Globals.mapScale))/2;
		laCarte.getTiledMap().render(offsetX,offsetY,compteurLayer);
	}
	
	
	/**
	 * Cette méthode permet d'encercler les bateau déjà sélectionné poru les afficher à l'utilisateur
	 * @param gc
	 * @param g
	 * @param leBateau
	 */
	private void afficherBateauVerrouille(GameContainer gc, Graphics g,Bateau leBateau)
	{
		if(leBateau.estBateauVerrouille())
		{
			int positionXBateau,positionYBateau;
			Position positionDuBateau=leBateau.getPosition();
			positionXBateau=positionDuBateau.getX();//+Globals.tileSize/2;
			positionYBateau=positionDuBateau.getY()-Globals.tileSize/2;
			if (leBateau.getPort()!=null && leBateau.getPort().getContour().getBoundingBox().intersects(leBateau.getEllipse())) {
				orienterBateau(leBateau.getPort().getDirection(), imageHalo);
			} else {
				orienterBateau(leBateau.getDirection(), imageHalo);
			}
			imageHalo.draw(positionXBateau, positionYBateau);

		}
	}
	
	/**
	 * Cette méthode permet d'affficher la trajectoire d'un bateau passé en paramètre
	 * @param gc
	 * @param g
	 * @param leBateau pour lequel on veut afficher la trajectoire
	 */
	private void afficherTrajectoireExistante(GameContainer gc, Graphics g,Bateau leBateau)
	{
		Trajectoire laTrajectoire= leBateau.getTrajectoire();
		if(leBateau.getPositionDepartTrajectoire()!=null)
		{
			Position nouvellePosition;
			Position positionPrecedente=new Position(leBateau.getPosition().getX(),leBateau.getPosition().getY());
			
			Path leCheminADessiner= new Path((positionPrecedente.getX())+Globals.tileSize/2,(positionPrecedente.getY())+Globals.tileSize/2);
			for(Position laPosition:laTrajectoire.getPositions())
			{
				//System.out.println("x : "+laPosition.getX()+"y : "+laPosition.getY());
				nouvellePosition=new Position(positionPrecedente.getX()+laPosition.getX(),positionPrecedente.getY()+laPosition.getY());
				leCheminADessiner.lineTo(nouvellePosition.getX()+Globals.tileSize/2,nouvellePosition.getY()+Globals.tileSize/2);
				positionPrecedente=nouvellePosition;
			}
			
			Renderer.getLineStripRenderer().setLineCaps(true);
			g.setLineWidth(2); 
			g.setColor(Color.orange);
			g.draw(leCheminADessiner);
		}
	}
	
	/**
	 * Methode qui permet d'afficher un bateau associer a une image
	 * @param gc
	 * @param g
	 * @param leBateau
	 * @param lImage
	 */
	private void afficherBateau(GameContainer gc,Graphics g,Bateau leBateau,Image lImage)
	{
		Position laPositionDuBateau=leBateau.getPosition();
		if (leBateau.getPort()!=null && leBateau.getPort().getContour().getBoundingBox().intersects(leBateau.getEllipse())) {
			orienterBateau(leBateau.getPort().getDirection(), lImage);
		} else {
			orienterBateau(leBateau.getDirection(), lImage);
		}
		lImage.draw(laPositionDuBateau.getX(),laPositionDuBateau.getY()-Globals.tileSize/2,Globals.boatWidth,Globals.boatHeight);
	}
	
	Rectangle rect;
	@Override
	public void mouseDragged(int xOld, int yOld, int xNew, int yNew)
	{
		rect = new Rectangle(xOld/Globals.windowScale,yOld/Globals.windowScale,10,10);
	}
	
	
	private void afficherScoreEtNombreDeJoueur(Graphics g,int score,int nombreDeJoueur)
	{
		g.setColor(Color.yellow);
		g.drawString("Score : "+ score,10,10);
		g.drawString("Nombre de joueur : "+ nombreDeJoueur,10,30);
		g.setColor(Color.red);
		
	}
	
	/**
	 * Cette méthode permet d'initialiser les images aux éléments du jeu
	 * @param leJeu
	 */
	protected void lierImageAuxElementsDuJeu()
	{
		try {
			imageBateauVertVide = new Image("image/boat_green_empty.png");
			imageBateauBrunVide = new Image("image/boat_brown_empty.png");
			imageBateauVertPlein = new Image("image/boat_green_full.png");
			imageBateauBrunPlein = new Image("image/boat_brown_full.png");
			imageGameOver = new Image("image/game_over.png");
			imageHalo = new Image("image/fond.png");
			imageBateauVertVide=imageBateauVertVide.getScaledCopy(Globals.boatWidth, Globals.boatHeight);
			imageBateauBrunVide =imageBateauBrunVide.getScaledCopy(Globals.boatWidth, Globals.boatHeight);
			imageBateauVertPlein=imageBateauVertPlein.getScaledCopy(Globals.boatWidth, Globals.boatHeight);
			imageBateauBrunPlein =imageBateauBrunPlein.getScaledCopy(Globals.boatWidth, Globals.boatHeight);
			imageHalo=imageHalo.getScaledCopy(Globals.boatWidth,Globals.boatHeight);
			imageChocobo = new SpriteSheet("image/chocobo_rose.png", 24, 32);
			

		} catch (SlickException e1) {
			e1.printStackTrace();
		}
		initChocobo();
	}

	/**
	 * Cette méthode permet d'orienter le bateau en fonction de la direction qu'il va prendre
	 * @param directionRelative
	 * @param imageBateau
	 */
	public void orienterBateau(Position directionRelative, Image imageBateau) {
		int xRelatif = directionRelative.getX();
		int yRelatif = directionRelative.getY();
		int rotation = 0;

		if (xRelatif == 0 && yRelatif == -1) {
			rotation = 0;
		} else if (xRelatif == 1 && yRelatif == -1) {
			rotation = 45;
		} else if (xRelatif == 1 && yRelatif == 0) {
			rotation = 90;
		} else if (xRelatif == 1 && yRelatif == 1) {
			rotation = 135;
		} else if (xRelatif == 0 && yRelatif == 1) {
			rotation = 180;
		} else if (xRelatif == -1 && yRelatif == 1) {
			rotation = -135;
		} else if (xRelatif == -1 && yRelatif == 0) {
			rotation = -90;
		} else if (xRelatif == -1 && yRelatif == -1) {
			rotation = -45;
		}
		imageBateau.setRotation(rotation);
		
	}

	public void demarrerAffichageClient() throws SlickException {
		//this.setInterfaceClient(new InterfaceServerLocal("map2.tmx"));
        Thread t =new Thread(this);
        t.start();
	}

	@Override
	public void run() {
		AppGameContainer app;
		try {
			app = new AppGameContainer(this);
			System.out.println("w: "+app.getWidth()+" h : "+app.getHeight());
			System.out.println("w: "+app.getWidth()*1.5+" h : "+app.getHeight()*1.5);
			app.setDisplayMode((int)(app.getWidth()*Globals.windowScale),(int)(app.getHeight()*Globals.windowScale), false);
			app.start();
		} catch (SlickException e) {
			e.printStackTrace();
		}
		
	}
}

