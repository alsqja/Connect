package com.example.connect.global.util;

import java.util.Random;

public class VerifyKeyCreator {

    public static String createKey() {
        Random random = new Random();
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < 8; i++) {
            int idx = random.nextInt(2);

            switch (idx) {
                case 0:
                    sb.append(random.nextInt(9));
                    break;
                case 1:
                    sb.append((char) (random.nextInt(26) + 65));
                    break;
            }
        }

        return sb.toString();
    }
}
