package com.udacity.stockhawk.sync;

import android.os.AsyncTask;

import com.udacity.stockhawk.Events.SymbolValidationEvent;

import org.greenrobot.eventbus.EventBus;

import java.io.IOException;

import yahoofinance.Stock;
import yahoofinance.YahooFinance;
import yahoofinance.quotes.stock.StockQuote;

import static yahoofinance.YahooFinance.get;

/**
 * Created by Ben on 12/17/16.
 */

public class ValidateSymbol extends AsyncTask<String, Void, ValidationResult> {

    @Override
    protected ValidationResult doInBackground(String... strings) {
        StockQuote quote = null;
        Boolean validation;
        try {
            quote = YahooFinance.get(strings[0]).getQuote();
        } catch (IOException e) {
            e.printStackTrace();
        }
        validation = !(quote.getPrice() == null || quote.getChange() == null || quote.getChangeInPercent() == null);

        ValidationResult validationResult = new ValidationResult();
        validationResult.setValidated(validation);
        validationResult.setSymbol(strings[0]);

        return validationResult;
    }

    @Override
    protected void onPostExecute(ValidationResult result) {
        EventBus.getDefault().post(new SymbolValidationEvent(result.getValidated(), result.getSymbol()));
    }
}
