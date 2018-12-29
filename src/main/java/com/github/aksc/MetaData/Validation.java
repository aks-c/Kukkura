package com.github.aksc.MetaData;

import com.github.aksc.DerivationSystem;
import com.github.aksc.Exceptions.BadLanguageException;
import com.github.aksc.Grammar.Symbol;

import java.util.ArrayList;

/**
 * Created by akselcakmak on 29/12/2018.
 *
 */
public class Validation {

    public static boolean checkAxiomSize(DerivationSystem ds, StringBuilder errorMsg) throws BadLanguageException {
        boolean isValid = true;
        if (ds.getAxiom().size() == 0) {
            errorMsg.append("The axiom (ie: the initial string) is empty.\n");
            errorMsg.append("There can be no derivation with an empty axiom. Please add symbols.\n");
            isValid = false;
        }
        return isValid;
    }


    public static boolean checkDeltaSizesExistence(DerivationSystem ds, StringBuilder errorMsg) {
        boolean isValid = true;
        if (ds.getDeltaSizes() == null) {
            errorMsg.append("There is no delta_sizes map. Please add one.\n");
            isValid = false;
        }
        return isValid;
    }

    public static boolean checkDeltaPositionsExistence(DerivationSystem ds, StringBuilder errorMsg) {
        boolean isValid = true;
        if (ds.getDeltaPositions() == null) {
            errorMsg.append("There is no delta_positions map. Please add one.\n");
            isValid = false;
        }
        return isValid;
    }

    public static boolean checkNTs(DerivationSystem ds, StringBuilder errorMsg) {
        // check that non-terminals do have an associated derivation rule.
        boolean isValid = true;
        for (String nt: ds.getNonTerminals()) {
            if (!ds.getRules().containsKey(nt)) {
                errorMsg.append("Symbol " + nt + " is declared to be a non-terminal, but doesn't have an associated production rule.\n");
                errorMsg.append("A non-terminal must have a production rule.\n");
                isValid = false;
            }
        }
        return isValid;
    }

    public static boolean checkSymbolsInAxiom(DerivationSystem ds, StringBuilder errorMsg) {
        // check that all the symbols in the initial axiom are valid.
        boolean isValid = true;
        for (Symbol symbol: ds.getAxiom()) {
            try {
                symbol.validateInitial(ds);
            } catch (BadLanguageException e) {
                errorMsg.append(e.getMessage());
                isValid = false;
            }
        }
        return isValid;
    }

    public static boolean checkSymbolsInRules(DerivationSystem ds, StringBuilder errorMsg) {
        // check that all the symbols in every rule are valid.
        boolean isValid = true;
        for (String lhs: ds.getRules().keySet()) {
            ArrayList<Symbol> rhs = ds.getRules().get(lhs);
            for (Symbol symbol: rhs) {
                try {
                    symbol.validate(lhs, ds);
                } catch (BadLanguageException e) {
                    errorMsg.append(e.getMessage());
                    isValid = false;
                }
            }
        }
        return isValid;
    }
}









