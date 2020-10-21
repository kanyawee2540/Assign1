package com.example.assignment1;

import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class HistoryActivity extends AppCompatActivity {
    private DBManager dbManager;
    private SimpleCursorAdapter adapter;

    final String[] from = new String[] {
            DatabaseHelper.SALES,
            DatabaseHelper.SHARE_PERCENTAGE,
            DatabaseHelper.SALES_MINUS_SHARE,
            DatabaseHelper.SALES_SHARE
    };

    final int[] to = new int[] {
            R.id.salesColumn,
            R.id.sharePercenColumn,
            R.id.salesMinusShareColumn,
            R.id.salesShareColumn
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("History");
        setContentView(R.layout.activity_history);

        dbManager = new DBManager(this);
        dbManager.open();
        Cursor cursor = dbManager.fetch();

        final ListView historyListView = (ListView) findViewById(R.id.historyList);
        historyListView.setEmptyView(findViewById(R.id.empty));



        adapter = new SimpleCursorAdapter(this, R.layout.list, cursor, from, to, 0);
        adapter.setViewBinder(new SimpleCursorAdapter.ViewBinder() {
            public boolean setViewValue(View view, Cursor cursor, int columnIndex) {
                if(columnIndex==1 || columnIndex==3 || columnIndex==4) {
                    Double preco = cursor.getDouble(columnIndex);
                    TextView textView = (TextView) view;
                    textView.setText(String.format("%1$,.0f", preco));
                    return true;
                } else if (columnIndex == 2) {
                    Double preco = cursor.getDouble(columnIndex);
                    TextView textView = (TextView) view;
                    textView.setText(String.format("%.2f", preco));
                    return true;
                }
                return false;
            }
        });
        adapter.notifyDataSetChanged();

        historyListView.setAdapter(adapter);
        dbManager.close();
    }
}
