package com.github.aksc.ErrorHandling;

import com.github.aksc.DerivationSystem;
import com.github.aksc.ErrorHandling.BadLanguageException;
import com.github.aksc.Grammar.Symbol;

import java.util.ArrayList;

/**
 * Created by akselcakmak on 29/12/2018.
 *
 *
 * All the logic necessary to (in)validate data supplied by the user is here.
 *
 * The main idea is that we validate the DerivationSystem, and every initial symbol the user specifies.
 * Then if (1) the DerivationSystem is valid, (2) every rule is valid, (3) every initial symbol is valid (symbols in the axiom),
 * we can show that every symbol at every subsequent step will also be valid.
 * So we do this big, heavy, exhaustive validation at first, and then don't need to check for anything for the rest for the runtime.
 *
 * There's a lot that can go wrong in the user input;
 * After all, this is pretty much a mini compiler's semantic analyser for the L-system/CFG/language supplied by the user.
 * That's why the validation has to be big; it has to be exhaustive.
 */
public class ValidationUtility {

    //// Checks for DerivationSystem ////

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

    //// Checks for Symbol ////

    public static boolean checkDeltaPos(Symbol symbol, DerivationSystem ds, StringBuilder errorMsg) {
        boolean isValid = true;
        boolean deltaPosIsNull = (symbol.getDeltaPosition() == null);
        boolean deltaPosIsDefined = !deltaPosIsNull && (symbol.getDeltaPosition().isDeltaDefined());
        boolean deltaPosRefIsNull = deltaPosIsNull || (symbol.getDeltaPosition().getDeltaReference() == null);
        boolean deltaPosRefExists = !deltaPosRefIsNull && (ds.getDeltaPositions().containsKey(symbol.getDeltaPosition().getDeltaReference()));

        // the deltaPos is not defined
        if (deltaPosIsNull || (!deltaPosIsDefined && deltaPosRefIsNull)) {
            errorMsg.append("The delta_position is not defined. Please define one, or reference one of those in the global delta_positions map.\n");
            isValid = false;
        }

        // the delta_pos_ref is invalid
        if (!deltaPosRefIsNull && !deltaPosRefExists) {
            errorMsg.append("The delta_pos.ref " + symbol.getDeltaPosition().getDeltaReference() + " doesn't exist in the language's derivation system.\n");
            errorMsg.append("Please check its spelling or create a reference in the delta_positions map.\n");
            isValid = false;
        }

        // the deltaPos is defined twice
        if (deltaPosIsDefined && !deltaPosRefIsNull) {
            errorMsg.append("Symbol has both the delta_position and the delta_position.ref defined. (ie: its delta_position is defined twice).\n");
            errorMsg.append("Please delete the reference ('ref') or the explicit definition ('dx', 'dy', 'dz').\n");
            isValid = false;
        }
        return isValid;
    }

    public static boolean checkDeltaSize(Symbol symbol, DerivationSystem ds, StringBuilder errorMsg) {
        boolean isValid = true;
        boolean deltaSizeIsNull = (symbol.getDeltaSize() == null);
        boolean deltaSizeIsDefined = !deltaSizeIsNull && (symbol.getDeltaSize().isDeltaDefined());
        boolean deltaSizeRefIsNull = deltaSizeIsNull || (symbol.getDeltaSize().getDeltaReference() == null);
        boolean deltaSizeRefExists = !deltaSizeRefIsNull && (ds.getDeltaSizes().containsKey(symbol.getDeltaSize().getDeltaReference()));

        // the deltaSize is not defined
        if (deltaSizeIsNull || (!deltaSizeIsDefined && deltaSizeRefIsNull)) {
            errorMsg.append("The delta_size is not defined. Please define one, or reference one of those in the global delta_sizes map.\n");
            isValid = false;
        }

        // the delta_size_ref is invalid
        if (!deltaSizeRefIsNull && !deltaSizeRefExists) {
            errorMsg.append("The delta_size.ref " + symbol.getDeltaSize().getDeltaReference() + " doesn't exist in the language's derivation system.\n");
            errorMsg.append("Please check its spelling or create a reference in the delta_sizes map.\n");
            isValid = false;
        }

        // the deltaSize is defined twice
        if (deltaSizeIsDefined && !deltaSizeRefIsNull) {
            errorMsg.append("Symbol has both the delta_size and the delta_size.ref defined. (ie: its delta_size is defined twice).\n");
            errorMsg.append("Please delete the reference ('ref') or the explicit definition ('dx', 'dy', 'dz').\n");
            isValid = false;
        }
        return isValid;
    }

    public static boolean checkInitialSize(Symbol symbol, DerivationSystem ds, StringBuilder errorMsg) {
        boolean isValid = true;
        boolean sizeIsNull = (symbol.getSize() == null);

        // initial size is not defined
        if (sizeIsNull) {
            errorMsg.append("The initial size is not defined. Define a size field.\n");
            isValid = false;
        }
        return isValid;
    }

    public static boolean checkInitialPosition(Symbol symbol, DerivationSystem ds, StringBuilder errorMsg) {
        boolean isValid = true;
        boolean positionIsNull = (symbol.getPosition() == null);

        // initial position is not defined
        if (positionIsNull) {
            errorMsg.append("The initial position is not defined. Define a size field.\n");
            isValid = false;
        }
        return isValid;
    }

    public static boolean checkProbabilityWeight(Symbol symbol, DerivationSystem ds, StringBuilder errorMsg) {
        boolean isValid = true;
        boolean probabilityIsValid = (symbol.getProbability() >= 0);
        if (!probabilityIsValid) {
            errorMsg.append("Symbol's probability weight is negative. Please set it to a valid (non-negative) value.\n");
            isValid = false;
        }
        return isValid;
    }

    public static boolean checkSymbolIDExistence(Symbol symbol, DerivationSystem ds, StringBuilder errorMsg) {
        boolean isValid = true;
        boolean symbolIDIsNull = (symbol.getSymbolID() == null);

        // the symbol doesn't have an ID
        if (symbolIDIsNull) {
            errorMsg.append("The symbol doesn't have an ID.\n");
            errorMsg.append("Please give a name/an ID to the symbol with the symbol field.\n");
            isValid = false;
        }
        return isValid;
    }

    /**
     * Only used for the symbols in the axiom.
     * Symbols in the axiom don't have parent symbols, so in their context references don't make sense.
     * That's why we check for the existence of references.
     */
    public static boolean checkReferencesExist(Symbol symbol, DerivationSystem ds, StringBuilder errorMsg) {
        boolean isValid = true;
        boolean deltaPosIsNull = (symbol.getDeltaPosition() == null);
        boolean deltaSizeIsNull = (symbol.getDeltaSize() == null);

        boolean deltaPosRefIsNull = deltaPosIsNull || (symbol.getDeltaPosition().getDeltaReference() == null);
        boolean deltaSizeRefIsNull = deltaSizeIsNull || (symbol.getDeltaSize().getDeltaReference() == null);

        // the deltaPos is defined (ie the position is defined as a delta)
        if (!deltaPosIsNull || !deltaPosRefIsNull) {
            errorMsg.append("Field delta_pos is defined.\n");
            errorMsg.append("Please delete any deltas.\n");
            isValid = false;
        }
        // the deltaSize is defined (ie the size is defined as a delta)
        if (!deltaSizeIsNull || !deltaSizeRefIsNull) {
            errorMsg.append("Field delta_size is defined.\n");
            errorMsg.append("Please delete any deltas.\n");
            isValid = false;
        }

        return isValid;
    }
}









