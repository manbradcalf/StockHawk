package com.udacity.stockhawk.sync;

import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import com.jjoe64.graphview.helper.DateAsXAxisLabelFormatter;
import com.jjoe64.graphview.series.DataPoint;
import com.udacity.stockhawk.Events.HistoricalQuotesMappedToDataSetEvent;
import com.udacity.stockhawk.data.Contract;

import org.greenrobot.eventbus.EventBus;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import yahoofinance.YahooFinance;
import yahoofinance.histquotes.HistoricalQuote;

/**
 * Created by ben.medcalf on 12/18/16.
 */

public class GetSymbolChangeHistory {
    private Context mContext;

    public GetSymbolChangeHistory(Context mContext) {
        this.mContext = mContext;
    }


    public void callDB(String symbol) {
        String[] queryFields = new String[]{
                Contract.Quote.COLUMN_HISTORY
        };
        String[] symbolArgs = new String[]{
                symbol
        };

        List<DataPoint> dataPoints = new ArrayList<>();
        int i = -1;

        Cursor cursor =
                mContext.getContentResolver().query(
                        Contract.Quote.makeUriForStock(symbol), // table
                        queryFields,                            // columns
                        symbol,                                 // selectionac
                        symbolArgs,                                   // selection args
                        null
                );

        String firstHistory = cursor.getString(0);
        String[] fullhistory = firstHistory.split(",");
        List<String> history = Arrays.asList(fullhistory);
        Iterator<String> iterator = history.iterator();
        while (iterator.hasNext()) {
            if (!iterator.next().contains(".")) {
                iterator.remove();
            }
        }

        //TODO: Need to find out why this is only iterating through once. I'm only getting 1st row back
        while (iterator.hasNext()) {

            if (iterator.next().contains(".")) {
            i++;
            Double d = Double.parseDouble(iterator.next());
            // Passing in 0 as the column int in cursor.getDouble because there is
            // only one column in the cursor we've created
            dataPoints.add(new DataPoint(i, d));
        }


        Log.d("DateArray: ", dataPoints.toString());


        HistoricalQuotesMappedToDataSetEvent event = new HistoricalQuotesMappedToDataSetEvent();
        event.setDataPoints(dataPoints);
        EventBus.getDefault().post(event);
    }
}

//    @Override
//    protected void onPostExecute(List<HistoricalQuote> historicalQuotes)
//    {
//        // Creating our iterator to iterate through the historical quotes
//        // returned by doInBackground
//        Iterator<HistoricalQuote> iterator = historicalQuotes.iterator();
//
//        // Newing up our ArrayList of DataPoints
//        List<DataPoint> dataPoints = new ArrayList<>();
//
//        // Instantiating i for the loop
//        int i = -1;
//
//        // This while loop iterates through the list of HistoricalQuotes
//        // and adds the value of i as the first data point (to serve as the X axis) and the ClosePrice value
//        // taken from the historicalQuote as the second data point (Y Axis). Once the iteration is finished
//        // we will have an arraylist of DataPoints to send to our DetailActivity via
//        // HistoricalQuotesMappedToDataSetEvent
//        while (iterator.hasNext()) {
//            HistoricalQuote quote = iterator.next();
//
//            i++;
//            Double closePrice = quote.getClose().doubleValue();
//            dataPoints.add(new DataPoint(i, closePrice));
//        }
//
//        HistoricalQuotesMappedToDataSetEvent event = new HistoricalQuotesMappedToDataSetEvent();
//        event.setDataPoints(dataPoints);
//
//        EventBus.getDefault().post(event);
//    }
}
