package org.litteraworlds.utils;

import javax.crypto.SecretKey;
import java.security.SecureRandom;
import java.util.stream.Collectors;

public class PasswordGen {
    public static String getRandomPasswordByKey(SecretKey key){

        SecureRandom rnd = new SecureRandom(key.getEncoded());

        return rnd.ints(5, '0','z')
                .mapToObj(Character::toString)
                .collect(Collectors.joining());
    }
}
