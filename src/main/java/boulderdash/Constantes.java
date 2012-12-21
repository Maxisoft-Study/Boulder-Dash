/**
 * 
 */
package boulderdash;

import java.awt.Color;
import java.io.File;

import org.apache.log4j.Logger;

import boulderdash.config.Config;
import boulderdash.utils.Log;
import boulderdash.utils.Serialization;

/**
 * Liste des Constantes Globales au project.
 * 
 */
public interface Constantes {
	/**
	 * Taille d'une case en nombre de pixels.
	 */
	public static int CASE_EN_PIXELS = 36;

	/**
	 * Nombre de points gagne lorsque le joueur ramasse un diamant.
	 */
	public final static int NBR_POINT_GAGNE_DIAMANT = 50;

	/**
	 * Couleur de fond (version Graphique)
	 */
	public static final Color BACKGROUND_COLOR = new Color(238, 238, 238);

	/**
	 * Vitesse maximal d'un ennemy.
	 */
	public static final int MAX_ENNEMY_SPEED = 10;

	/**
	 * Fichier Config
	 */
	public static final File _CONFIGFILE = new File("config.xml");

	/**
	 * Permet de mettre le jeu en mode Config
	 */
	public static final Config CONFIG = Serialization.readConfig(Constantes._CONFIGFILE) == null ? new Config()
			: Serialization.readConfig(Constantes._CONFIGFILE);

	/**
	 * Le LOGGER par Defaut.
	 */
	public static final Logger LOGGER = Log.logger;

}
