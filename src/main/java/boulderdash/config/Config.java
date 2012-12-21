package boulderdash.config;

/**
 * Classe contenant des parametres.
 */
public final class Config {
	/**
	 * Enregistre les warn dans un log.
	 */
	private boolean logWarn = true;
	/**
	 * Enregistre les debugs/infos dans un log.
	 */
	private boolean logDebug = false;

	/** Vrai si le Jeu est en mode editeur de map. */
	private boolean mapeditor_mode = false;

	/** Vrai si le programme utilise des Thread.<br/> */
	private boolean use_thread = true;
	/**
	 * Utilise L'antialiasing. <br/>
	 * Note : Utilisse beaucoup (trop) le proccesseur & la Carte graphique.
	 */
	private boolean useAA = false;

	/**
	 * Click Pour bouger si vrai.<br/>
	 * Sinon laisser appuyer pour bouger.
	 */
	private boolean clickToMove = false;

	private final boolean USEASSERT = false;

	/**
	 * @return le logDebug
	 */
	public final boolean isLogDebug() {
		return this.logDebug;
	}

	/**
	 * @param logDebug
	 *            le logDebug à définir
	 */
	public final void setLogDebug(final boolean logDebug) {
		this.logDebug = logDebug;
	}

	/**
	 * @return le clickToMove
	 */
	public final boolean isClickToMove() {
		return this.clickToMove;
	}

	/**
	 * @param clickToMove
	 *            le clickToMove à définir
	 */
	public final void setClickToMove(final boolean clickToMove) {
		this.clickToMove = clickToMove;
	}

	/**
	 * @return le useAA
	 */
	public final boolean isUseAA() {
		return this.useAA;
	}

	/**
	 * @param useAA
	 *            le useAA à définir
	 */
	public final void setUseAA(final boolean useAA) {
		this.useAA = useAA;
	}

	/**
	 * @return le use_thread
	 */
	public final boolean isUseThread() {
		return this.use_thread;
	}

	/**
	 * @param useThread
	 *            le use_thread à définir
	 */
	public final void setUseThread(final boolean useThread) {
		this.use_thread = false;
	}

	/**
	 */
	public final boolean isMapEditorMode() {
		return this.mapeditor_mode;
	}

	/**
	 * @param MapEditorMode
	 *            true ou false
	 */
	public final void setMapEditorMode(final boolean MapEditorMode) {
		this.mapeditor_mode = MapEditorMode;
	}

	/**
	 * @return le logWarn
	 */
	public final boolean isLogWarn() {
		return this.logWarn;
	}

	/**
	 * @param logWarn
	 *            le logWarn à définir
	 */
	public final void setLogWarn(final boolean logWarn) {
		this.logWarn = logWarn;
	}

	/**
	 * @return le uSEASSERT
	 */
	public final boolean isUSEASSERT() {

		return this.USEASSERT;
	}

}