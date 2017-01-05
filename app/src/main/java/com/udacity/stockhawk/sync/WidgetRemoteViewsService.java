package com.udacity.stockhawk.sync;

import android.content.Intent;
import android.database.Cursor;
import android.widget.AdapterView;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.udacity.stockhawk.R;
import com.udacity.stockhawk.data.Contract;

/**
 * Created by ben.medcalf on 1/3/17.
 */

public class WidgetRemoteViewsService extends RemoteViewsService
{
    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent)
    {

        return new RemoteViewsFactory()
        {
            private Cursor cursor = null;

            @Override
            public void onCreate()
            {

            }

            @Override
            public void onDataSetChanged()
            {
                if (cursor != null) {
                    cursor.close();
                }

                cursor = getContentResolver().query(
                        // table (FROM)
                        Contract.Quote.URI,
                        // which columns (SELECT)
                        new String[]{Contract.Quote.COLUMN_SYMBOL,
                        Contract.Quote.COLUMN_PERCENTAGE_CHANGE,
                        Contract.Quote.COLUMN_PRICE,
                        Contract.Quote.COLUMN_ABSOLUTE_CHANGE},
                        // selection (WHERE)
                        null,
                        // selection args ( = ? )
                        null,
                        // sort order
                        null
                );

            }

            @Override
            public void onDestroy()
            {
                if (cursor != null) {
                    cursor.close();
                    cursor = null;
                }
            }

            @Override
            public int getCount()
            {
                return cursor == null ? 0 : cursor.getCount();
            }

            @Override
            public RemoteViews getViewAt(int i)
            {
                if (i == AdapterView.INVALID_POSITION ||
                        cursor == null || !cursor.moveToPosition(i)) {
                    return null;
                }

                RemoteViews views = new RemoteViews(getPackageName(), R.layout.list_item_quote);
                String symbol = cursor.getString(cursor.getColumnIndex(Contract.Quote.COLUMN_SYMBOL));

                views.setTextViewText(R.id.symbol, symbol);
                views.setTextViewText(R.id.price, cursor.getString(cursor.getColumnIndex(Contract.Quote.COLUMN_PRICE)));
                views.setTextViewText(R.id.change, cursor.getString(cursor.getColumnIndex(Contract.Quote.COLUMN_PERCENTAGE_CHANGE)));


                if (cursor.getInt(cursor.getColumnIndex(Contract.Quote.COLUMN_ABSOLUTE_CHANGE)) > 0) {
                    views.setInt(R.id.change, "setBackgroundResource", R.drawable.percent_change_pill_green);
                } else {
                    views.setInt(R.id.change, "setBackgroundResource", R.drawable.percent_change_pill_red);
                }

                return views;
            }

            @Override
            public RemoteViews getLoadingView()
            {
                return null;
            }

            @Override
            public int getViewTypeCount()
            {
                return 1;
            }

            @Override
            public long getItemId(int i)
            {
                if (cursor.moveToPosition(i))
                    return cursor.getLong(cursor.getColumnIndexOrThrow(Contract.Quote._ID));
                return i;
            }

            @Override
            public boolean hasStableIds()
            {
                return true;
            }
        };
    }
}
