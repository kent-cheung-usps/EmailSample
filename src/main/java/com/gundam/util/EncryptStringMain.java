package com.gundam.util;

public class EncryptStringMain {
    /**
     * Utility to encrypt a plain password for storing in application.properties.
     * Prints out the value to use as the password property.
     */
    public static void main(String[] args) throws Exception {
        if (args.length == 0) {
            System.out.println("Usage: java EncryptPasswordMain <plain_password>");
            return;
        }
        String plain = args[0];
        String encrypted = FieldEncryptor.encrypt(plain);
        System.out.println("{enc}" + encrypted);
    }
}