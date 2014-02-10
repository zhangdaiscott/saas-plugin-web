package org.core.p3.web;

import java.util.Properties;
import java.util.Set;

public class SystemInfo {

	private static Properties prop = new Properties();

	public SystemInfo() {
	}

	public synchronized static void put(String key, String value) {
		prop.put(key, value);
	}

	public static String get(String key) {
		return prop.getProperty(key);
	}

	public static Set<Object> names() {
		return prop.keySet();
	}
}
