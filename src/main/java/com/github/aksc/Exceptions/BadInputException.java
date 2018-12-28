package com.github.aksc.Exceptions;

/**
 * Created by akselcakmak on 28/12/2018.
 *
 * Used for errors due to malformed input.
 * eg: file not found, bad input json syntax
 */
public class BadInputException extends Exception {
    public BadInputException(String msg) {
        super(msg);
    }
}
