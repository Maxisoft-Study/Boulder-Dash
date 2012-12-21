/**
 * 
 */
package boulderdash.enemy;

import javax.annotation.Nullable;

import boulderdash.Constantes;
import boulderdash.Damier;

/**
 * Thread Qui Appelle la methode Ia de chaque Personnage ennemies.
 */
public class EnnemyIA implements Runnable, Constantes {
	/**
	 * Tous les ennemies
	 */
	private final Personnage[] __allEnnemy;
	/**
	 * le damier .
	 */
	private Damier __damier;

	public EnnemyIA(@Nullable final Personnage[] all) {
		this.__allEnnemy = all;
		if (all != null && this.__allEnnemy.length > 0) {
			this.__damier = this.__allEnnemy[0].getDamier();
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Runnable#run()
	 */
	@Override
	public synchronized void run() {
		Thread.currentThread().setName("EnnemyIA");
		if (this.__allEnnemy == null || this.__damier == null) {
			return;
		}
		while (this.__damier.getAnimfin() == 0) {
			for (final Personnage j : this.__allEnnemy) {
				if (j == null) {
					continue;
				}
				j.IA();

			}
			try {
				Thread.sleep(300);
			} catch (final InterruptedException e) {
				e.printStackTrace();
			}

		}

	}

}
