package com.server.notetaking.utils;

import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;
import java.util.function.BinaryOperator;

@Component
public class CryptUtils {

    @org.springframework.beans.factory.annotation.Value("${auth.server.note.taking.password.secret.key:NoteTaking12345628#}")
    String passwordSecretKey;
    public String encodedBase64Key(String secretKey) {
        String encodedBase64Key = Crypt.encodeKey(passwordSecretKey);
        return encodedBase64Key;

    }

    public String decrypt(String request) {
        String encodedBase64Key = Crypt.encodeKey(passwordSecretKey);
        String decryptPassword = Crypt.decrypt(request.replace(" ", "+"), encodedBase64Key);
        return decryptPassword;
    }


    public static Long computeStringValue(String value) {
        final String operator = "\\*";
        final List<String> operands = Arrays.asList(value.split(operator));
        return operands.stream().map(Long::valueOf).reduce(1L, new BinaryOperator<Long>() {

            @Override
            public Long apply(Long oldL, Long newL) {
                return operator.contains("*") ? oldL * newL : 0;
            }
        });
    }

}
