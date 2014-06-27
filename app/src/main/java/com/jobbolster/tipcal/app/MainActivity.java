package com.jobbolster.tipcal.app;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;


public class MainActivity extends ActionBarActivity {

    private static final String TOTAL_BILL = "TOTAL_BILL";
    private static final String CURRENT_TIP = "CURRENT_TIP";
    private static final String BILL_WITHOUT_TIP = "BILL_WITHOUT_TIP";

    private double billBeforeTip;
    private int tipAmount;
    private double finalBill;

    EditText etBillBeforeTip;
    EditText etTipAmount;

    TextView tvFinalBill;
    TextView tvTipAmount;
    TextView tvAmountToTip;
    TextView tvWaitressScore;
    TextView tvWaitressScoreNum;

    SeekBar sbAdjustTip;
    Spinner spiNameSelector;

    Button bttnResetAll;
    Button bttnAddWaiter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        etBillBeforeTip = (EditText) findViewById(R.id.billEditText);
        tvFinalBill = (TextView) findViewById(R.id.finalBillAmountTextView);

        //Add Edit Text listener
        etBillBeforeTip.addTextChangedListener(billBeforeTipListener);

        //Setup SeekBar
        sbAdjustTip = (SeekBar) findViewById(R.id.tipSeekBar);
        sbAdjustTip.setOnSeekBarChangeListener(tipSeekBarListener);

        tvTipAmount = (TextView) findViewById(R.id.tipPercentTextView);
        tvAmountToTip = (TextView) findViewById(R.id.tipAmountTextView);
        tvWaitressScore = (TextView) findViewById(R.id.waitressScoreTextView);
        tvWaitressScoreNum = (TextView) findViewById(R.id.waitressScoreNumTextView);
        tvWaitressScore.setVisibility(View.INVISIBLE);
        tvWaitressScoreNum.setVisibility(View.INVISIBLE);

        //setupSpinner
//        spiNameSelector = (Spinner) findViewById(R.id.spiWaitressNames);
//        spinnerSetup();


        //Setup Buttons
        bttnResetAll = (Button) findViewById(R.id.resetAllBttn);
        bttnAddWaiter = (Button) findViewById(R.id.openWaiterInfoBttn);
        setBttnOnClickListener();
    }

    private TextWatcher billBeforeTipListener = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {

            try{
                billBeforeTip = Double.parseDouble(charSequence.toString());
            }catch (NumberFormatException nfe){
                billBeforeTip = 0.00;
            }

            updateTipFinalBill();

        }

        @Override
        public void afterTextChanged(Editable editable) {

        }
    };

    private SeekBar.OnSeekBarChangeListener tipSeekBarListener = new SeekBar.OnSeekBarChangeListener() {
        @Override
        public void onProgressChanged(SeekBar seekBar, int i, boolean b) {

            tipAmount = (sbAdjustTip.getProgress());
            tvTipAmount.setText(Integer.toString(tipAmount));
            updateTipFinalBill();
            System.out.println("Set progress " + tipAmount + "%");

        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {

        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {

        }
    };

    private void updateTipFinalBill(){

        double tipFromText = Double.parseDouble(tvTipAmount.getText().toString()) * .01;
        double finalBill = billBeforeTip + (billBeforeTip * tipFromText);
        double amountToTip = finalBill - billBeforeTip;
        tvAmountToTip.setText(String.format("%.02f",amountToTip));
        tvFinalBill.setText(String.format("%.02f",finalBill));
    }

    private void setBttnOnClickListener(){
        bttnResetAll.setOnClickListener(new Button.OnClickListener(){

            @Override
            public void onClick(View view) {
                etBillBeforeTip.setText(String.format("%.02f",0.00));
                sbAdjustTip.setProgress(15);
                tvWaitressScore.setVisibility(View.INVISIBLE);
                tvWaitressScoreNum.setVisibility(View.INVISIBLE);
//                spiNameSelector.setSelection(0);
            }
        });

        bttnAddWaiter.setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this,WaiterInfo.class);
                startActivity(intent);
            }
        });
    }

    private void spinnerSetup(){
        spiNameSelector.setOnItemSelectedListener(new Spinner.OnItemSelectedListener(){

            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (spiNameSelector.getSelectedItem().equals("Jason Rodriguez")){
                    tvWaitressScore.setVisibility(View.VISIBLE);
                    tvWaitressScoreNum.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);

    }
}
