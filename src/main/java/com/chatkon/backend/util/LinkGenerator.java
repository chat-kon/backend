package com.chatkon.backend.util;

public class LinkGenerator {
    public static String generate(int length) {
        String AlphaNumericString = "ABCDEFGHIJKLMNOPQRSTUVWXYZ" + "0123456789" + "abcdefghijklmnopqrstuvxyz";
        StringBuilder sb = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            int index = (int) (AlphaNumericString.length() * Math.random());
            sb.append(AlphaNumericString
                    .charAt(index));
        }
        String link = sb.toString();
        return "@msgrInviteLink." + link; //todo replace name of app by msgr
    }
}
