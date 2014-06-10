package com.jobbolster.tipcal.app;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
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

    SeekBar sbAdjustTip;

    Button bttnResetAll;

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

        //Setup Buttons
        bttnResetAll = (Button) findViewById(R.id.resetAllBttn);
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
            tvAmountToTip.setText(Integer.toString(tipAmount));
            updateTipFinalBill();

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
