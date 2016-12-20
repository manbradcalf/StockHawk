package com.udacity.stockhawk.sync;

import android.os.AsyncTask;

import com.jjoe64.graphview.series.DataPoint;
import com.udacity.stockhawk.Events.HistoricalQuotesMappedToDataSetEvent;

import org.greenrobot.eventbus.EventBus;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import yahoofinance.YahooFinance;
import yahoofinance.histquotes.HistoricalQuote;

/**
 * Created by ben.medcalf on 12/18/16.
 */

public class GetSymbolChangeHistory extends AsyncTask<String, Void, List<HistoricalQuote>>
{
    @Override
    protected List<HistoricalQuote> doInBackground(String... strings)
    {
        List<HistoricalQuote> historicalQuotes = null;
        try
        {
            historicalQuotes = YahooFinance.get(strings[0]).getHistory();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

        return historicalQuotes;
    }

    @Override
    protected void onPostExecute(List<HistoricalQuote> historicalQuotes)
    {
        // Creating our iterator to iterate through the historical quotes
        // returned by doInBackground
        Iterator<HistoricalQuote> iterator = historicalQuotes.iterator();

        // Newing up our ArrayList of DataPoints
        List<DataPoint> dataPoints = new ArrayList<>();

        // Instantiating i for the loop
        int i = -1;

        // This while loop iterates through the list of HistoricalQuotes
        // and adds the value of i as the first data point (to serve as the X axis) and the ClosePrice value
        // taken from the historicalQuote as the second data point (Y Axis). Once the iteration is finished
        // we will have an arraylist of DataPoints to send to our DetailActivity via
        // HistoricalQuotesMappedToDataSetEvent
        while (iterator.hasNext()) {
            HistoricalQuote quote = iterator.next();
            i++;
            Double closePrice = quote.getClose().doubleValue();
            dataPoints.add(new DataPoint(i, closePrice));
        }

        HistoricalQuotesMappedToDataSetEvent event = new HistoricalQuotesMappedToDataSetEvent();
        event.setDataPoints(dataPoints);

        EventBus.getDefault().post(event);
    }
}
