package com.udacity.stockhawk.data;

/**
 * Created by ben.medcalf on 12/18/16.
 */

public class DoublePair
{
    private final Double key;
    private final Double value;

    public DoublePair(Double aKey, Double aValue)
    {
        key   = aKey;
        value = aValue;
    }

    public Double key()   { return key; }
    public Double value() { return value; }
}
