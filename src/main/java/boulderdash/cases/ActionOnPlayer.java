/**
 * 
 */
package boulderdash.cases;

import boulderdash.Joueur;

/**
 * Permet de definir que l'objet implantant cette interface doit effectuer une
 * action lorsqu un joueur est a la meme position que l'instance de l'objet sur
 * le damier <br/>
 * exemple : le Joueur est sur une case Terre nommee « t ». On appellera donc la
 * méthode t.onPlayer(Joueur)
 */
public interface ActionOnPlayer {
	/**
	 * Appeler lorsque le joueur est sur la presente Case
	 * 
	 * @param joueur
	 *            le joueur.
	 */
	public void OnPlayer(Joueur joueur);
}
