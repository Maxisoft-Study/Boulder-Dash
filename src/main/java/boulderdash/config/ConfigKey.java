/**
 * 
 */
package boulderdash.config;

import java.io.Reader;
import java.io.Serializable;
import java.io.Writer;

import com.thoughtworks.xstream.XStream;

/**
 * Liste des clef en mode console.
 */
public final class ConfigKey implements Serializable {

	private char key_moveup;
	private char key_movedown;
	private char key_moveleft;
	private char key_moverigth;

	private static final long serialVersionUID = 1L;

	/**
	 * Init avec Param par defaut.
	 */
	public ConfigKey() {
		this.key_moveup = 'z';
		this.key_movedown = 's';
		this.key_moveleft = 'q';
		this.key_moverigth = 'd';
	}

	/**
	 * Crée un objet configKey avec un fichier xml
	 * 
	 * @param reader
	 *            le reader d'xml
	 */
	public ConfigKey(final Reader reader) {
		final XStream xstream = new XStream();
		xstream.setMode(XStream.NO_REFERENCES);
		this.getFrom((ConfigKey) xstream.fromXML(reader));
	}

	/**
	 * Permet de copier les attributs de l'objet fournis en param dans l'objet
	 * actuel.
	 * 
	 * @param cfg
	 */
	private void getFrom(final ConfigKey cfg) {
		this.setKey_moveup(cfg.getKey_moveup());
		this.setKey_movedown(cfg.getKey_movedown());
		this.setKey_moveleft(cfg.getKey_moveleft());
		this.setKey_moverigth(cfg.getKey_moverigth());
	}

	/**
	 * Exporte en xml la configuration.<br/>
	 * Ecrit dans le Writer fournis en param.
	 * 
	 * @param writer
	 */
	public void exporter(final Writer writer) {
		final XStream xstream = new XStream();
		xstream.setMode(XStream.NO_REFERENCES);
		xstream.toXML(this, writer);
	}

	public final char[] values() {
		return new char[] { this.key_movedown, this.key_moveleft, this.key_moverigth, this.key_moveup };
	}

	// GETTER - SETTER

	/**
	 * @return le key_moveup
	 */
	public char getKey_moveup() {
		return this.key_moveup;
	}

	/**
	 * @param key_moveup
	 *            le key_moveup à définir
	 */
	void setKey_moveup(final char key_moveup) {
		this.key_moveup = key_moveup;
	}

	/**
	 * @return le key_movedown
	 */
	public char getKey_movedown() {
		return this.key_movedown;
	}

	/**
	 * @param key_movedown
	 *            le key_movedown à définir
	 */
	void setKey_movedown(final char key_movedown) {
		this.key_movedown = key_movedown;
	}

	/**
	 * @return le key_moveleft
	 */
	public char getKey_moveleft() {
		return this.key_moveleft;
	}

	/**
	 * @param key_moveleft
	 *            le key_moveleft à définir
	 */
	void setKey_moveleft(final char key_moveleft) {
		this.key_moveleft = key_moveleft;
	}

	/**
	 * @return le key_moverigth
	 */
	public char getKey_moverigth() {
		return this.key_moverigth;
	}

	/**
	 * @param key_moverigth
	 *            le key_moverigth à définir
	 */
	void setKey_moverigth(final char key_moverigth) {
		this.key_moverigth = key_moverigth;
	}

}
