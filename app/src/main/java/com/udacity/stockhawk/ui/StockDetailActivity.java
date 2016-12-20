package com.udacity.stockhawk.ui;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;
import com.udacity.stockhawk.Events.HistoricalQuotesMappedToDataSetEvent;
import com.udacity.stockhawk.R;
import com.udacity.stockhawk.sync.GetSymbolChangeHistory;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.udacity.stockhawk.R.id.graph;

/**
 * Created by ben.medcalf on 12/18/16.
 */

public class StockDetailActivity extends AppCompatActivity
{
    @BindView(graph)
    GraphView graphView;

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.stock_detail);
        ButterKnife.bind(this);
        graphView.setVisibility(View.GONE);
        Bundle bundle = this.getIntent().getExtras();
        String symbol = bundle.getString("symbol");
        GetSymbolChangeHistory getHistoryTask = new GetSymbolChangeHistory();
        getHistoryTask.execute(symbol);
    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe
    public void onMessageEvent(HistoricalQuotesMappedToDataSetEvent event) {

        List<DataPoint> dataPoints = event.getDataPoints();
        DataPoint[] dataPointArray = dataPoints.toArray(new DataPoint[0]);
        LineGraphSeries<DataPoint> series = new LineGraphSeries<DataPoint>(dataPointArray);
        graphView.addSeries(series);
        graphView.setVisibility(View.VISIBLE);
    }

}
