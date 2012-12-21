package boulderdash;

import boulderdash.io.Key_Parser;
import boulderdash.utils.Direction;
import boulderdash.utils.Generator;

/**
 * Lance la version Console.
 * 
 */
public class Console implements Constantes {
	private static final Damier damier = Generator.gen_FirstDamier(10, 10, new Joueur(null, ""));
	private static final Joueur joueur = Console.damier.getJoueur();

	public static void main(final String[] args) {
		Constantes.CONFIG.setUseThread(false);
		Direction input_dir;
		while (Console.damier.getAnimfin() == 0) {
			System.out.println(Console.damier);
			input_dir = Key_Parser.saisirDirection();
			System.out.println("Deplacement Possible : " + Console.joueur.seDeplace(input_dir));
			Console.damier.detectAndMoveFallingObject();
			Console.damier.update();

			System.out.println(Console.joueur.genInfoTab());
		}

	}

}
