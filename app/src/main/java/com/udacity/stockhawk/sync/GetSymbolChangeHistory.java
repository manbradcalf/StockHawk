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
        Iterator<HistoricalQuote> iterator = historicalQuotes.iterator();
        List<DataPoint> dataPoints = new ArrayList<>();

        int i = -1;

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
