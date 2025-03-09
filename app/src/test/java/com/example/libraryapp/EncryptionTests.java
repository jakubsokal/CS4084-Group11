package com.example.libraryapp;

import org.junit.Test;

import static org.junit.Assert.*;

import com.example.libraryapp.db.Encryption;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class EncryptionTests {
    Encryption ENC;

    public EncryptionTests(){
        ENC = new Encryption();
    }

    @Test
    public void CheckCorrectEncryptionReturn_OK() {
        String hashedPassword = ENC.encrypt("Jakub123");

        assertEquals(2, hashedPassword.split("\\$").length);
    }

    @Test
    public void NotEncryptedPasswordReturns_NotOK(){
        String hashedPassword = "Jakub12345";

        assertNotEquals(2, hashedPassword.split("\\$").length);
    }

    @Test
    public void verifyPasswordReturns_OK(){
        String hashedPassword = ENC.encrypt("MyPasswordIsGood@1_-.");

        assertTrue(ENC.verify("MyPasswordIsGood@1_-.",hashedPassword));
    }

    @Test
    public void verifyPasswordReturns_NotOK(){
        String hashedPassword = "MyPasswordIsGood@1_-.";

        assertFalse(ENC.verify("MyPasswordIsGood@1_-.",hashedPassword));
    }
}