package com.example.colorve.util;

import java.util.Locale;

import org.apache.shiro.authc.credential.DefaultPasswordService;
import org.apache.shiro.crypto.hash.DefaultHashService;
import org.apache.shiro.crypto.hash.Sha256Hash;
import org.apache.shiro.util.SimpleByteSource;

public class PasswordUtils {

    protected static final String DEFAULT_PUBLIC_SALT = "ZW5pc2NhbmJpbGdl";

    public static String generateSha256Hash(String input) {
    	
        DefaultHashService hashService = new DefaultHashService();
        hashService.setHashIterations(DefaultPasswordService.DEFAULT_HASH_ITERATIONS);
        hashService.setHashAlgorithmName(Sha256Hash.ALGORITHM_NAME);
        hashService.setPrivateSalt(new SimpleByteSource(DEFAULT_PUBLIC_SALT));
        hashService.setGeneratePublicSalt(true);
        DefaultPasswordService passwordService = new DefaultPasswordService();
        passwordService.setHashService(hashService);
        return passwordService.encryptPassword(input);
    }

    public static boolean checkPassword(String input, String savedPassword) {
    	
        Locale locale = Locale.getDefault(); // store default locale
        Locale.setDefault(Locale.ENGLISH); // change locale to ENGLISH otherwise passwordMatch doesn't work
        
        DefaultHashService hashService = new DefaultHashService();
        
        hashService.setHashIterations(DefaultPasswordService.DEFAULT_HASH_ITERATIONS);
        hashService.setHashAlgorithmName(Sha256Hash.ALGORITHM_NAME);
        hashService.setPrivateSalt(new SimpleByteSource(DEFAULT_PUBLIC_SALT));
        hashService.setGeneratePublicSalt(true);
        
        DefaultPasswordService passwordService = new DefaultPasswordService();
        passwordService.setHashService(hashService);
        
        boolean result = passwordService.passwordsMatch(input, savedPassword);
        Locale.setDefault(locale); // revert back to default locale
        return result;
    }
}