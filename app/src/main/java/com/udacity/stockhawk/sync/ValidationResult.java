package com.udacity.stockhawk.sync;

/**
 * Created by Ben on 12/17/16.
 */

public class ValidationResult {
    Boolean validated;
    String symbol;


    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public Boolean getValidated() {
        return validated;
    }

    public void setValidated(Boolean validated) {
        this.validated = validated;
    }


}
