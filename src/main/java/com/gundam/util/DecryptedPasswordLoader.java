package com.gundam.util;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Properties;

public class DecryptedPasswordLoader {

    /**
     * Reads the "password" field from the provided properties file and decrypts it if it starts with "{enc}".
     */
    public static String getDecryptedPassword(String propertiesFilePath) {
        try (InputStream input = new FileInputStream(propertiesFilePath)) {
            Properties prop = new Properties();
            prop.load(input);

            String encrypted = prop.getProperty("password");
            if (encrypted != null && encrypted.startsWith("{enc}")) {
                return FieldEncryptor.decrypt(encrypted.substring(5));
            }
            return encrypted; // fallback to plain if not encrypted
        } catch (Exception ex) {
            throw new RuntimeException("Failed to load or decrypt password from " + propertiesFilePath, ex);
        }
    }
}