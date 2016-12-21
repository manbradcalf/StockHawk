package com.udacity.stockhawk.ui;

import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.icu.text.SimpleDateFormat;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.helper.DateAsXAxisLabelFormatter;
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

    private String mSymbol;

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.stock_detail);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ButterKnife.bind(this);
        graphView.setVisibility(View.GONE);
        Bundle bundle = this.getIntent().getExtras();
        mSymbol = bundle.getString("symbol");
        GetSymbolChangeHistory getHistoryTask = new GetSymbolChangeHistory(this);
        getHistoryTask.callDB(mSymbol);
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
        graphView.getViewport().setScalable(true);
        graphView.addSeries(series);
        graphView.setTitle(mSymbol + " past year");
        graphView.getGridLabelRenderer().setHorizontalLabelsVisible(false);
        graphView.setVisibility(View.VISIBLE);
    }
}
