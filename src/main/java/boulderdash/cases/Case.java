/**
 * 
 */
package boulderdash.cases;

import boulderdash.GameObject;
import boulderdash.annotations.UniqueInstance;

/**
 * Represente chaque element du Damier.<br/>
 * Utilise la methode clone() comme « Factory method ».
 */
public abstract class Case extends GameObject implements ActionOnPlayer, Cloneable {
	/** Permet de savoir si la case tombe. */
	boolean falling = false;

	/**
	 * Raccourcis pour Avoir toutes les Classes qui heritent de Case.
	 */
	public final static Case[] all_standar_instance = new Case[] { Mur.instance, Terre.instance, Pierre.instance,
			Diamant.instance, Vide.instance };
	/** La Seul Instance de Spawn. */
	public final static Spawn SPAWN_CASE = Spawn.instance;
	/** La Seul Instance de Exit. */
	public final static Exit EXIT_CASE = Exit.instance;
	/** La Seul Instance de VIDE. */
	public final static Vide VIDE_CASE = Vide.instance;
	/** La Seul Instance de MUR. */
	public final static Mur MUR_CASE = Mur.instance;

	final public boolean isFalling() {
		return this.falling;
	}

	final public void setFalling(final boolean falling) {
		if (this.est_fixe()) {
			throw new IllegalStateException("L'objet est Fixe et ne peut etre bouger");
		}
		this.falling = falling;
	}

	/**
	 * Permet d'instancier une nouvelle instance . (Factory Method)
	 */
	@Override
	public final Case clone() {
		if (this.getClass().getAnnotation(UniqueInstance.class) != null) {
			return this;
		}

		try {
			return this.getClass().cast(super.clone());
		} catch (final CloneNotSupportedException e) {
			e.printStackTrace();
		}
		return null;
	}

	/** @return true si la case Bloque les chutes de pierres. */
	public final boolean bloque_Chute_Pierre() {
		return this.getClass() != Vide.class;
	}

	protected Case() {
		super();
	}

}
