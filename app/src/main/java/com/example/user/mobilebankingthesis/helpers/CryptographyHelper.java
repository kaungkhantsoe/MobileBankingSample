package com.example.user.mobilebankingthesis.helpers;

import org.whispersystems.curve25519.Curve25519;
import org.whispersystems.curve25519.Curve25519KeyPair;

public class CryptographyHelper {

    static CryptographyHelper cryptographyHelper;

    static byte[] publicKey,privateKey;
    static Curve25519 cipher;
    static Curve25519KeyPair keyPair;

    public byte[] getPublicKey() {
        return publicKey;
    }

    public byte[] getPrivateKey() {
        return privateKey;
    }


    // Prepare curve for cryptography
    public static void prepareCurve() {
        cipher = Curve25519.getInstance(Curve25519.BEST);

        keyPair = cipher.generateKeyPair();

        publicKey = keyPair.getPublicKey();
        privateKey = keyPair.getPrivateKey();
    }


    // CryptographyHelper instance
    public static CryptographyHelper getInstance() {

        if (cryptographyHelper == null) {
            prepareCurve();
            return new CryptographyHelper();
        }
        return cryptographyHelper;
    }


    // Shared key
    public byte[] getSharedKey(byte[] publicKey, byte[] privateKey) {
        byte[] sharedSecret = cipher.calculateAgreement(publicKey, privateKey);

        return sharedSecret;
    }


    // Encryption
    public String Encrypt(byte[] sharedKey, String plainText) {
        String encrypted;

        AES aes = new AES();

        aes.setKey(sharedKey);
        encrypted = ((AES)aes).Encrypt(plainText);
        return encrypted;
    }


    // Decryption
    public String Decrypt(byte[] sharedKey, String encryptedText) {
        String decrypted;

        AES aes = new AES();

        aes.setKey(sharedKey);
        decrypted = ((AES)aes).Decrypt(encryptedText);
        return decrypted;
    }
}
