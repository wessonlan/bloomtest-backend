package com.pingguo.bloomtest.common;

public class BtException extends RuntimeException{
    private BtException(String message) {
        super(message);
    }

    public static void throwException(String message) {
        throw new BtException(message);
    }
}