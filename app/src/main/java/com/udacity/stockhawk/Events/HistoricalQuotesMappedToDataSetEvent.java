package com.udacity.stockhawk.Events;

import com.jjoe64.graphview.series.DataPoint;

import java.util.List;

/**
 * Created by ben.medcalf on 12/18/16.
 */

public class HistoricalQuotesMappedToDataSetEvent
{
    public List<DataPoint> getDataPoints()
    {
        return dataPoints;
    }

    public void setDataPoints(List<DataPoint> dataPoints)
    {
        this.dataPoints = dataPoints;
    }

    List<DataPoint> dataPoints;
}
