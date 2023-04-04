package fr.mickaelbaron.mysharelatexmanager;

import java.util.Date;

import org.jasypt.encryption.pbe.StandardPBEStringEncryptor;
import org.mindrot.jbcrypt.BCrypt;

/**
 * @author Mickael BARON (baron.mickael@gmail.com)
 */
public class MySharelatexManagerUtil {

	private MySharelatexManagerUtil() {
		throw new IllegalStateException("Utility class");
	}

	public static String hashPassword(String passwordPlaintext) {
		String salt = BCrypt.gensalt(MySharelatexManagerConstant.WORKLOAD);
		String hashedPassword = BCrypt.hashpw(passwordPlaintext, salt);

		return (hashedPassword);
	}

	public static boolean checkPassword(String newPassword, String old) {
		return BCrypt.checkpw(newPassword, old);
	}

	public static String encryptToken(AuthToken currentToken, String password, String noise) {
		if (currentToken == null || !currentToken.isValid()) {
			return null;
		}

		return MySharelatexManagerUtil.encrypt(
				currentToken.getIdentifiant() + MySharelatexManagerConstant.AUTH_TOKEN_SEPARATOR
						+ currentToken.getStartDateValue() + MySharelatexManagerConstant.AUTH_TOKEN_SEPARATOR + noise,
				password);
	}

	public static AuthToken decryptToken(String value, String password, String noise) {
		if (value == null) {
			return null;
		}

		AuthToken currentToken = null;
		try {
			final String decrypt = MySharelatexManagerUtil.decrypt(value, password);

			if (decrypt == null) {
				return null;
			}

			final String[] split = decrypt.split(MySharelatexManagerConstant.AUTH_TOKEN_SEPARATOR);

			if (split == null || split.length != 3) {
				return null;
			}

			String previousIdentifiant = split[0];
			Date previousStartDate = new Date(Long.parseLong(split[1]));
			String previousNoise = split[2];

			if (!noise.equals(previousNoise)) {
				return null;
			}

			currentToken = new AuthToken(previousIdentifiant, previousStartDate);
			return currentToken;
		} catch (Exception e) {
			return null;
		}
	}

	public static String encrypt(String content, String password) {
		return MySharelatexManagerUtil.getEncryptor(password).encrypt(content);
	}

	public static String decrypt(String content, String password) {
		return MySharelatexManagerUtil.getEncryptor(password).decrypt(content);
	}

	private static StandardPBEStringEncryptor getEncryptor(String password) {
		StandardPBEStringEncryptor cookieEncryptor = new StandardPBEStringEncryptor();
		cookieEncryptor.setStringOutputType("hexadecimal");
		cookieEncryptor.setPassword(password);
		return cookieEncryptor;
	}
}
