package boulderdash.utils;

import java.awt.FileDialog;
import java.awt.Frame;

import javax.annotation.Nullable;

/**
 * Gestion simplifier des dialogue de fichier.
 * 
 */
public class UseFileDialog {
	/**
	 * Fenetre charge un fichier.
	 * 
	 * @param f
	 *            le parent
	 * @param title
	 *            le titre de la fenetre
	 * @return le path du fichier ou null
	 */
	public String loadFile(@Nullable final Frame f, final String title) {
		final FileDialog fd = new FileDialog(f, title, FileDialog.LOAD);
		fd.setVisible(true);
		return fd.getDirectory() + System.getProperty("file.separator") + fd.getFile();
	}

	/**
	 * Fenetre sauvegarde un fichier.
	 * 
	 * @param f
	 *            le parent
	 * @param title
	 *            le titre de la fenetre
	 * @return le path du fichier ou null
	 */
	public String saveFile(@Nullable final Frame f, final String title) {
		final FileDialog fd = new FileDialog(f, title, FileDialog.SAVE);
		fd.setVisible(true);

		if (fd.getFile() == null) {
			return null;
		}

		return fd.getDirectory() + System.getProperty("file.separator")
				+ (fd.getFile().endsWith(".bdxml") ? fd.getFile() : fd.getFile() + ".bdxml");
	}

}