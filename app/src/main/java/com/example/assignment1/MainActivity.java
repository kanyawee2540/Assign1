package com.example.assignment1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DecimalFormat;

//https://www.journaldev.com/9438/android-sqlite-database-example-tutorial

public class MainActivity extends AppCompatActivity {
    public static final String PREFS_NAME = "MyPrefsFile";
    private DBManager dbManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final TextView txtAppName = (TextView)findViewById(R.id.assign);
        final EditText sale = (EditText) findViewById(R.id.inputSales);
        final EditText sharePer = (EditText) findViewById(R.id.inputSharePercentage);
        final EditText saleMi = (EditText) findViewById(R.id.inputSalesMinusShare);
        final EditText saleSha = (EditText) findViewById(R.id.inputSalesShare);
        final ImageButton historyBtn = (ImageButton) findViewById(R.id.historyBtn);
        final DecimalFormat changeFormat = new DecimalFormat(("#,###,###"));
        dbManager = new DBManager(this);

        TelephonyManager tel = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        String networkOperator = tel.getNetworkOperator();
        if (!TextUtils.isEmpty(networkOperator)) {
            int mcc = Integer.parseInt(networkOperator.substring(0, 3));
            int mnc = Integer.parseInt(networkOperator.substring(3));

//            txtAppName.setText(txtAppName.getText() + " MCC-" + mcc + " MNC-" + mnc + " RAW-" + networkOperator);
            int temp = 0;
            switch (mnc) {
                case 0:
                    temp = R.color.cat;
                    break;
                case 3:
                case 15:
                    temp = R.color.aisLine;
                    break;
                case 4:
                    temp = R.color.trueM;
                    break;
                case 5:
                case 18:
                case 47:
                    temp = R.color.dtac;
                    break;
                default:
                    temp = R.color.historyColorBtn;
            }
            historyBtn.setColorFilter(getResources().getColor(temp));
        }
        historyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, HistoryActivity.class));
            }
        });
        final Button calBth = (Button)findViewById(R.id.calBtn) ;
        calBth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                double numSale = 0;
                double numSharePer = 0;
                if (!sale.getText().toString().equals("") && !sharePer.getText().toString().equals("")) {
                    numSale = Double.parseDouble(sale.getText().toString().replace(",",""));
                    numSharePer = Double.parseDouble(sharePer.getText().toString());
                    if (numSharePer > 100) {
                        Toast.makeText(getApplicationContext(),"Nani???, percentage > 100", Toast.LENGTH_SHORT).show();
                    } else {
                        int salesMinus = (int) (numSale * (((100 - numSharePer)) / 100));
                        int saleShare = (int) numSale - salesMinus;
                        saleMi.setText(changeFormat.format(salesMinus));
                        saleSha.setText(changeFormat.format(saleShare));
//                        insert(int sales, double sharePercentage, int salesMinusShare, int salesShare)

                        dbManager.open();
                        dbManager.insert((int) numSale, numSharePer ,salesMinus, saleShare);
                        dbManager.close();
                    }
                } else {
                    Toast.makeText(getApplicationContext(),"Plz, input", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
    @Override
    //change font size follow device
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
//        final TextView txtView=(TextView)findViewById(R.id.assign);
//        txtView.setTextSize(newConfig.fontScale*32);
    }
}