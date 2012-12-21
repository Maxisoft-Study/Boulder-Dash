package boulderdash.utils;

import java.io.IOException;

import org.apache.log4j.ConsoleAppender;
import org.apache.log4j.FileAppender;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.PatternLayout;

import boulderdash.Constantes;

/**
 * Permet de Log les erreur, info, ... Utilise Log4j.
 */
public class Log implements Constantes {
	public final static Logger logger = Logger.getLogger(Log.class);

	static {
		final PatternLayout layout = new PatternLayout("%d %-5p %c - %F:%L - %m%n");
		FileAppender appender = null;
		try {
			appender = new FileAppender(layout, "log.txt", true);
		} catch (final IOException e) {
			e.printStackTrace();
		}
		Log.logger.addAppender(appender);

		if (Constantes.CONFIG.isLogDebug()) {
			Log.logger.setLevel(Level.DEBUG);
			Log.logger.addAppender(new ConsoleAppender(layout));
		}

	}
}