package com.example.libraryapp.db;

import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.util.Base64;
import org.bouncycastle.crypto.generators.Argon2BytesGenerator;
import org.bouncycastle.crypto.params.Argon2Parameters;

public class Encryption {
    public String encrypt(String password) {
        byte[] salt = new byte[16];
        new SecureRandom().nextBytes(salt);
        byte[] hash = saltEncrypt(password, salt);

        return Base64.getEncoder().encodeToString(salt) +
                "$" + Base64.getEncoder().encodeToString(hash);
    }

    public boolean verify(String password, String storedHash) {
        String[] parts = storedHash.split("\\$");

        if (parts.length == 2) {
            byte[] salt = Base64.getDecoder().decode(parts[0]);
            byte[] expectedHash = Base64.getDecoder().decode(parts[1]);
            byte[] computedHash = saltEncrypt(password, salt);

            return compare(expectedHash, computedHash);
        }

        return false;
    }

    private byte[] saltEncrypt(String password, byte[] salt) {
        byte[] result = new byte[32];

        Argon2Parameters parameters = new Argon2Parameters.Builder(Argon2Parameters.ARGON2_id)
                .withSalt(salt)
                .withIterations(3)
                .withMemoryAsKB(65536)
                .withParallelism(2)
                .build();

        Argon2BytesGenerator argonGenerator = new Argon2BytesGenerator();
        argonGenerator.init(parameters);
        argonGenerator.generateBytes(password.getBytes(StandardCharsets.UTF_8), result);

        return result;
    }

    private boolean compare(byte[] expected, byte[] actual) {
        if (expected.length == actual.length) {
            int difference = 0;

            for (int i = 0; i < expected.length; i++) {
                difference |= expected[i] ^ actual[i];
            }

            return difference == 0;
        }

        return false;
    }

}
