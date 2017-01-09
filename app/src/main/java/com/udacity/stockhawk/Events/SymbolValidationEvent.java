package com.udacity.stockhawk.Events;

/**
 * Created by Ben on 12/17/16.
 */

public class SymbolValidationEvent {
    Boolean validated;
    String symbol;

    public SymbolValidationEvent(Boolean validated, String symbol) {
        this.validated = validated;
        this.symbol = symbol;
    }

    public Boolean getValidated() {
        return this.validated;
    }

    public String getSymbol() {
        return this.symbol;
    }
}
