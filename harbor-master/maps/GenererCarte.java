public class GenererCarte {
    private static final int largeur=400;
    private static final int hauteur=300;

    public static void main(String args[]) {
	System.out.println(largeur);
	System.out.println(hauteur);
	for(int i=0;i<hauteur;i++){
	    for(int j=0;j<largeur;j++) {
		if(i<=50 || i>=200) {
		    if(i>40 && i<=50 && j>50 && j<=65)
			System.out.print("1");
		    else if(i>=200 && i<=210 && j>150 && j<=165)
		    	System.out.print("2");
		    else
			System.out.print("*");
		}
		else 
		    System.out.print(" ");
	    }
	    System.out.println();
	}
    }
}