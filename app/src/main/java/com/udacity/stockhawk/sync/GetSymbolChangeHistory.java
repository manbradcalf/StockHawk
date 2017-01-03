package com.udacity.stockhawk.sync;

import android.content.Context;
import android.database.Cursor;

import com.jjoe64.graphview.series.DataPoint;
import com.udacity.stockhawk.Events.HistoricalQuotesMappedToDataSetEvent;
import com.udacity.stockhawk.data.Contract;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

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

        Cursor cursor =
                mContext.getContentResolver().query(
                        Contract.Quote.makeUriForStock(symbol), // table
                        queryFields,                            // columns
                        symbol,                                 // selection
                        symbolArgs,                             // selection args
                        null
                );

        // i is going to represent a week on the graph's x axis
        int i = -1;

        if (cursor.moveToFirst()) {
            // The History is stored in SQL as one big String in a single row, formatted
            // like a key:value pair. I'm creating this list here by getting that string from
            // the cursor and splitting it on commas and newlines. Each item in this list
            // is either a unix epoch time object or a double representing a price.
            // In the for loop below, I check for a decimal, which tells me I have a price object,
            // not a time object. I then increment "i", which I use to represent a week on the x axis
            // of my graph, because the graphview library I'm using doesn't elegantly show formatted
            // dates on the x axis.
            List<String> historyList = Arrays.asList(cursor.getString(0).split(",|\\n"));

            // Reversing the list because the list is ordered from most to least recent.
            // Since i'm incrementing i to be the x axis count for weeks, this is an issue.
            Collections.reverse(historyList);
            for (String historyItem : historyList) {
                if (historyItem.contains(".")) {
                    historyItem.replace(" ", "");
                    i++;
                    Double d = Double.parseDouble(historyItem);
                    // Passing in 0 as the column int in cursor.getDouble because there is
                    // only one column in the cursor we've created
                    dataPoints.add(new DataPoint(i, d));
                }
            }
        }

        // Creating the event and firing it, where StockDetailActivity is listening for it.
        HistoricalQuotesMappedToDataSetEvent event = new HistoricalQuotesMappedToDataSetEvent();
        event.setDataPoints(dataPoints);
        EventBus.getDefault().post(event);
    }
}
