package com.example.user.mobilebankingthesis.events;

import android.os.Build;
import android.util.Base64;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

public class ApiEvents {

    public static class onErrorEvent {

        String error;

        public onErrorEvent(String error) {
            this.error = error;
        }

        public String getError() {
            return error;
        }

    }

    public static class onReceivePublicKeyEvent {

        byte[] publicKey;
        public onReceivePublicKeyEvent(String publicKey) {
            this.publicKey = Base64.decode(publicKey,Base64.DEFAULT);
        }

        public byte[] getPublicKey() {
            return publicKey;
        }
    }

    public static class onSuccessEvent {

        String message;

        public onSuccessEvent(String message) {
            this.message = message;
        }

        public String getMessage() {
            return message;
        }
    }

}
