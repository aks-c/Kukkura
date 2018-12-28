package com.github.aksc.Exceptions;

/**
 * Created by akselcakmak on 28/12/2018.
 *
 * Used for errors where the language given is invalid.
 * For example, used when a non-terminals doesn't have a derivation rule (or when a terminal has one),
 * and for other linguistic inconsistencies.
 */
public class BadLanguageException extends Exception {
    public BadLanguageException(String msg) {
        super(msg);
    }
}
