/**
 * 
 */
package boulderdash.utils;

import java.io.File;
import java.io.FileReader;
import java.io.Reader;
import java.io.Serializable;
import java.io.Writer;

import javax.annotation.Nullable;

import org.apache.commons.lang.NullArgumentException;

import boulderdash.Constantes;
import boulderdash.Damier;
import boulderdash.Joueur;
import boulderdash.cases.Case;
import boulderdash.cases.Diamant;
import boulderdash.cases.Exit;
import boulderdash.cases.Mur;
import boulderdash.cases.Pierre;
import boulderdash.cases.Spawn;
import boulderdash.cases.Terre;
import boulderdash.cases.Vide;
import boulderdash.config.Config;
import boulderdash.enemy.Personnage;

import com.thoughtworks.xstream.XStream;

/**
 * Serialization d'objet en xml a l'aide d'une Librairie (Xstream).
 */
public class Serialization {
	private static XStream xstream = new XStream();

	static {
		Serialization.xstream.setMode(XStream.ID_REFERENCES);

		Serialization.xstream.alias(Case.class.getSimpleName(), Case.class);
		Serialization.xstream.alias(Diamant.class.getSimpleName(), Diamant.class);
		Serialization.xstream.alias(Exit.class.getSimpleName(), Exit.class);
		Serialization.xstream.alias(Mur.class.getSimpleName(), Mur.class);
		Serialization.xstream.alias(Pierre.class.getSimpleName(), Pierre.class);
		Serialization.xstream.alias(Spawn.class.getSimpleName(), Spawn.class);
		Serialization.xstream.alias(Terre.class.getSimpleName(), Terre.class);
		Serialization.xstream.alias(Vide.class.getSimpleName(), Vide.class);

	}

	private Serialization() {
	}

	// public static void main(String[] args) throws IOException {
	// saveObjectTo(new Config(), new FileWriter(new File("./config.xml")));
	// }

	/**
	 * Permet de valider un damier.
	 * 
	 * @param d
	 *            Le damier
	 * @throws Exception
	 *             si le damier n'est pas valide
	 */
	public static void validate_MAP(final Damier d) throws Exception {
		// TODO faire ses propres Exception.

		// local
		Position current_pos;
		Case current_case;

		if (d == null) {
			throw new NullArgumentException(null);
		}
		if (d.getJoueur() == null) {
			throw new NullArgumentException("Pas de Joueur");
		}
		if (d.getCaseAt(d.getJoueur().getPos()).getClass() != Spawn.class) {
			throw new Exception("Le joueur n'est pas au Spawn");
		}

		final Case[][] content2 = d.getContent();

		for (int i = 0; i < content2.length; i++) {
			for (int j = 0; j < content2[i].length; j++) {
				current_pos = new Position(j, i);
				current_case = d.getCaseAt(current_pos);
				if (current_case.getClass() == Exit.class) {
					return;
				}
			}
		}
		throw new Exception("Pas de Fin au niveau !");
	}

	/**
	 * Sauvegarde n'importe quelle objet dans un writer.
	 * 
	 * @param obj
	 *            l'objet a sauvegarde
	 * @param w
	 *            le writer.
	 */
	public static void saveObjectTo(final Object obj, final Writer w) {
		if (w == null) {
			return;
		}
		Serialization.xstream.toXML(obj, w);
	}

	/**
	 * Sauvegarde un damier dans un writer.
	 * 
	 * @param damier
	 *            le damier a sauvegarde
	 * @param w
	 *            le writer.
	 */
	public static void saveObjectTo(final Damier damier, final Writer w) {
		if (w == null) {
			return;
		}

		final MapSaver toSave = new MapSaver(damier.getContent(), damier.getAll_Enemies());

		Serialization.xstream.toXML(toSave, w);
	}

	/**
	 * Cree un nouveau damier a partir d'un Reader.
	 * 
	 * @param r
	 *            le reader
	 * @param joueur
	 *            le joueur
	 * @return nouveau damier
	 */
	public static Damier restoreDamier(final Reader r, @Nullable final Joueur joueur) {
		MapSaver read;
		read = (MapSaver) Serialization.xstream.fromXML(r);
		return new Damier(read.getContent(), joueur, read.getEnemies());
	}

	/**
	 * Lit la config.
	 * 
	 * @param f
	 *            le fichier xml a lire.
	 * @return La Config Lu ou null si erreur.
	 */
	public static Config readConfig(@Nullable final File f) {
		try {
			final FileReader r = new FileReader(f);
			final Config read = (Config) Serialization.xstream.fromXML(r);
			r.close();
			return read;
		} catch (final Exception e) {
			return null;
		}

	}

}


/**
 * Permet de sauvegarder facilement & efficacement un damier.<br/>
 * Ne sauvegarde que le necessaire.
 */
final class MapSaver implements Serializable {
	private static final long serialVersionUID = 5L;
	private Case[][] content = null;
	private Personnage[] enemies = null;

	/**
	 * @return le content
	 */
	final Case[][] getContent() {
		return this.content;
	}

	/**
	 * @param content
	 *            le content à définir
	 */
	final void setContent(final Case[][] content) {
		this.content = content;
	}

	/**
	 * @return le enemies
	 */
	final Personnage[] getEnemies() {
		return this.enemies;
	}

	/**
	 * @param enemies_
	 *            le enemies à définir
	 */
	final void setEnemies(@Nullable final Personnage[] enemies_) {

		if (enemies_ == null) {
			return;
		}
		this.enemies = new Personnage[enemies_.length];
		for (int i = 0; i < enemies_.length; i++) {
			final Personnage e = enemies_[i];
			try {
				this.enemies[i] = e.clone();
			} catch (final CloneNotSupportedException e1) {
				Constantes.LOGGER.error("", e1);
			}
			this.enemies[i].setDamier(null);
		}
	}

	MapSaver(final Case[][] content, @Nullable final Personnage[] enemies_) {
		this.setContent(content);
		this.setEnemies(enemies_);
	}

}
