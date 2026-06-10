package com.aivle.bookapp.global.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Sha256Util {
    public static String encrypt(String text) {
        try {
            // SHA-256 MessageDigest 인스턴스 생성
            MessageDigest md = MessageDigest.getInstance("SHA-256");

            // 문자열을 바이트 배열로 변환하여 해시값 계산
            md.update(text.getBytes());
            byte[] digest = md.digest();

            // 바이트 배열을 16진수 문자열로 변환
            StringBuilder hexString = new StringBuilder();
            for (byte b : digest) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) {
                    hexString.append('0');
                }
                hexString.append(hex);
            }
            return hexString.toString();

        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("SHA-256 알고리즘을 찾을 수 없습니다.", e);
        }
    }
}
