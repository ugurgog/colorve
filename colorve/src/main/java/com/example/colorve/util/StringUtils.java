package com.example.colorve.util;

import java.util.UUID;

public class StringUtils {
	private final static char[] TR_CHARS = "ğüşıöçĞÜŞİÖÇ".toCharArray();
	private final static char[] EN_CHARS = "gusiocGUSIOC".toCharArray();

	public static boolean isNullOrEmpty(String text) {
		return text == null || text.isEmpty();
	}

	public static String getUUID() {
		String uuID = UUID.randomUUID().toString();

		return uuID;
	}

	private static boolean hasTurkishChars(String text) {

		for (char ch : TR_CHARS) {
			if (text.indexOf(ch) != -1)
				return true;
		}

		return false;
	}

	private static String convertTurkishChars(String text) {

		char chars[] = text.toCharArray();

		for (int ci = 0; ci < chars.length; ci++) {

			for (int i = 0; i < TR_CHARS.length; i++) {
				if (chars[ci] == TR_CHARS[i]) {
					chars[ci] = EN_CHARS[i];
				}
			}
		}
		String converted = new String(chars);

		return converted;
	}
}
