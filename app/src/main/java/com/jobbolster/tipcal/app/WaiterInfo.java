package com.jobbolster.tipcal.app;


import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

import java.util.ArrayList;


/**
 * Created by Gilbert Rodriguez on 6/24/2014.
 */
public class WaiterInfo extends ActionBarActivity{

    private String restaurantName;
    private String restaurantLocale;
    private String serverName;

    AutoCompleteTextView etRestaurantName;
    AutoCompleteTextView etRestaurantLocale;
    EditText etServerName;

    Button bttnAddServer;
    Button bttnReset;

    //Setup DB
    DBAdapter serverDB;


    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.waiter_info_layout);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        etRestaurantName = (AutoCompleteTextView) findViewById(R.id.restaurantNameEditText);
        etRestaurantLocale = (AutoCompleteTextView) findViewById(R.id.locationEditText);
        etServerName = (EditText) findViewById(R.id.serverNameEditText);


        bttnAddServer = (Button) findViewById(R.id.addServerToList);
        bttnReset = (Button) findViewById(R.id.resetDB);
        setBttnOnClickListener();


        openDB();
        populateListViewFromDB();

        createResturantAutoPopulate();
        createLocationAutoPopulate();

    }

    private void createResturantAutoPopulate() {
        ArrayList<String> theListToPopulate = new ArrayList<String>();
        String[] listNames = serverDB.getAllRestaurantNames();

        for(int i = 0; i < listNames.length; i++){
            boolean isDistinct = false;
            for (int j = 0; j < i; j++){
                if(listNames[i].equals(listNames[j])){
                    System.out.println("Name in i = " + listNames[i]);
                    System.out.println("Name in j = " + listNames[j]);
                    isDistinct = true;
                    break;
                }
            }
            System.out.println("isDistinct = " + isDistinct);
            if (!isDistinct){
                theListToPopulate.add(listNames[i]);
                System.out.println("Name in is not distinct = " + listNames[i]);
            }
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,R.layout.list_item,theListToPopulate);
        etRestaurantName.setAdapter(adapter);
    }

    private void createLocationAutoPopulate(){
        ArrayList<String> theListToPopulate = new ArrayList<String>();
        String[] listNames = serverDB.getAllLocations();

        for(int i = 0; i < listNames.length; i++){
            boolean isDistinct = false;
            for (int j = 0; j < i; j++){
                if(listNames[i].equals(listNames[j])){
                    isDistinct = true;
                    break;
                }
            }
            if (!isDistinct){
                theListToPopulate.add(listNames[i]);
            }
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,R.layout.list_item,theListToPopulate);
        etRestaurantLocale.setAdapter(adapter);
    }



    private void populateListViewFromDB() {
        Cursor cursor = serverDB.getAllRows();

        //Allow activity to manage lifetime of cursor
        startManagingCursor(cursor);

        //Setup mapping from cursor to view fields
        String[] fromFieldNames = new String[]
                {DBAdapter.KEY_SERVER_NAME,DBAdapter.KEY_RESTAURANT_LOCALE};

        int[] toViewIDs = new int[]
                {R.id.serverNameTextView,R.id.serverScoreTextView};

        //Create adapter to my columns of the DB onto elements in the UI
        SimpleCursorAdapter myCursorAdapter = new SimpleCursorAdapter(this,R.layout.server_layout,cursor,fromFieldNames,toViewIDs);

        //Set adapter for the list view
        ListView serverInfoListView = (ListView) findViewById(R.id.serverListView);
        serverInfoListView.setAdapter(myCursorAdapter);
    }

    private void openDB() {
        serverDB = new DBAdapter(this);
        serverDB.open();
    }

    private void setBttnOnClickListener() {
        bttnAddServer.setOnClickListener(new Button.OnClickListener(){

            @Override
            public void onClick(View view) {


                restaurantName = etRestaurantName.getText().toString().trim();
                restaurantLocale = etRestaurantLocale.getText().toString().trim();
                serverName = etServerName.getText().toString().trim();

                if(checkEmptyFields(restaurantName,restaurantLocale,serverName)== true) {
                    serverDB.insertRow(restaurantName, restaurantLocale, serverName);
                    populateListViewFromDB();
                    createResturantAutoPopulate();
                    createLocationAutoPopulate();
                    clearText();
                }


            }
        });

        bttnReset.setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View view) {
                serverDB.deleteAll();
                populateListViewFromDB();
            }
        });

    }



    private void clearText() {
        etRestaurantName.setText("");
        etRestaurantLocale.setText("");
        etServerName.setText("");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        closeDB();
    }

    private void closeDB() {
        serverDB.close();
    }

    public boolean checkEmptyFields(String restName,String restLocale, String serName){

        if(restName.isEmpty() || restName == "" ){
               Toast toast = Toast.makeText(getApplicationContext(),"Restaurant Name cannot be blank",Toast.LENGTH_LONG);
                toast.show();
                toast.setGravity(Gravity.CENTER,0,0);
            return false;
        }else if (restLocale.isEmpty() || restLocale == ""){
            Toast toast = Toast.makeText(getApplicationContext(),"Restaurant location cannot be blank",Toast.LENGTH_LONG);
            toast.show();
            toast.setGravity(Gravity.CENTER,0,0);
            return false;
        }else if (serName.isEmpty() || serName == ""){
            Toast toast = Toast.makeText(getApplicationContext(),"Server Name cannot be blank",Toast.LENGTH_LONG);
            toast.show();
            toast.setGravity(Gravity.CENTER,0,0);
            return false;
        }
        return true;
    }

}
