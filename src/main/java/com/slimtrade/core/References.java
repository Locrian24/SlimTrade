package com.slimtrade.core;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class References {

	public static final String APP_NAME = "SlimTrade";
	private static String APP_VERSION = null;

	public static final String POE_WINDOW_TITLE = "Path of Exile";

	public static final int DEFAULT_IMAGE_SIZE = 18;
	public static final String REGEX_PREFIX = "((?<date>\\d{4}\\/\\d{2}\\/\\d{2}) (?<time>\\d{2}:\\d{2}:\\d{2}))?.*@(?<messageType>To|From) (?<guildName><.+> )?(?<playerName>.+):(\\s+)";
	public static final String REGEX_PREFIX_ALT = "((?<date>\\d{4}\\/\\d{2}\\/\\d{2}) (?<time>\\d{2}:\\d{2}:\\d{2}))?.*(#|\\$)(?<guildName><.+> )?(?<playerName>.+):(\\s+)";

	public static void loadAppVersion() {

	}

	public static String getAppVersion() {
		if(APP_VERSION == null) {
			final Properties properties = new Properties();
			try {
				InputStream stream = References.class.getClassLoader().getResourceAsStream("project.properties");
				properties.load(stream);
				stream.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			APP_VERSION = properties.getProperty("version");
		}

		return APP_VERSION;

	}

}
