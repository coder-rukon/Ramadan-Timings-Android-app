package net.digitalbd.ramadan;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.TimeUnit;


public class MainActivity extends AppCompatActivity {
    private boolean isDivisionListVisible = false;
    private LinearLayout column_02,column_01,column_03;
    private TextView mTextMessage;
    public Typeface fontFamily;
    private final static String ramdanStartDate = "28 05 2017";
    private TextView welcomeTitle,welcom_ifter,welcom_sheheri;
    private BottomNavigationView navigation;
    private Toolbar toolbar;
    public Context context;
    private RsDb db;
    /*List View*/
    private ListView ramdanListView;
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {

            switch (item.getItemId()) {
                case R.id.navigation_home:
                    showDivisionList();
                    return true;
                case R.id.dua_list:
                    Intent intentDualist = new Intent(context,DuaList.class);
                    startActivity(intentDualist);
                    return true;
                case R.id.roja_vonger_karon:
                    Intent intent = new Intent(context, Roja_Vonger_Karon.class);
                    startActivity(intent);
                    return true;
            }
            return false;
        }

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.welcome_sort_details);
        context = getApplicationContext();
        fontFamily = Typeface.createFromAsset(context.getAssets(), "fonts/HindSiliguri-Regular.ttf");
        db = new RsDb(context);
        this.isDivisionListVisible = false;
        navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        column_02 = (LinearLayout) findViewById(R.id.column_02);
        column_01 = (LinearLayout) findViewById(R.id.column_01);
        column_03 = (LinearLayout) findViewById(R.id.column_03);
        try{
            //printSortDsc();
        }catch (Exception e){
            column_02.setVisibility(View.GONE);
            column_01.setVisibility(View.GONE);
            column_03.setVisibility(View.GONE);
        }

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.top_menu,menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem){
        switch (menuItem.getItemId()){
            case R.id.gotohome:
                Intent intentHome = new Intent(this,this.getClass());
                startActivity(intentHome);
                break;
            default:
                break;
        }
        return true;
    }

    public void showDivisionList(){
        setContentView(R.layout.activity_main);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.title_division);
        setSupportActionBar(toolbar);
        navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        ramdanListView = (ListView) findViewById(R.id.ramdanListView);
        ramdanListView.setAdapter(new DivisionAdapter(this));
        ramdanListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(parent.getAdapter().getCount()<=40){
                    return;
                }
                SinlgeDivision temp = (SinlgeDivision) parent.getAdapter().getItem(position);
                db.deleteData("DELETE FROM settings");
                db.InsertData("insert into settings(defaultDivision) values("+temp.id+")");
                ramdanListView.setAdapter(new RamdanListAdapter(context,temp,toolbar));
                ramdanListView.setPadding(0,0,0,0);
            }
        });
    }

    public void printSortDsc(){
        Resources rs = getResources();
        Cursor dataFromDb = this.db.GetData("select * from settings");
        welcomeTitle = (TextView) findViewById(R.id.welcome_title);
        welcom_ifter = (TextView) findViewById(R.id.welcom_ifter);
        welcom_sheheri = (TextView) findViewById(R.id.welcom_sheheri);
        TextView  dirstrictDisplay;
        dirstrictDisplay = (TextView) findViewById(R.id.display_distric);

        int curentDivision = 10;
        while (dataFromDb.moveToNext()){
            curentDivision = dataFromDb.getInt(0);
        }
        TimeZone.setDefault(TimeZone.getTimeZone("Asia/Dhaka"));
        SimpleDateFormat myFormat = new SimpleDateFormat("dd-MM-yyyy");
        Cursor resultSet = this.db.GetData("select * from district where id ="+curentDivision);
        Cursor DataBaseTimeShedule = this.db.GetData("Select * from base_time");
        resultSet.moveToFirst();
        boolean ramadanIsRunning = false;
        String messageForTitle = "";
        long diff = 0;
        Date todayDateTime = new Date();
        String tempSheheri = "",tempIfter = "",tempDate,tempDistrictName = "";
        while (DataBaseTimeShedule.moveToNext()){
            if(DataBaseTimeShedule.getString(3).equals(myFormat.format(todayDateTime))){
                messageForTitle = myFormat.format(todayDateTime);
                ramadanIsRunning = true;
                tempDate = DataBaseTimeShedule.getString(3);
                tempSheheri = DataBaseTimeShedule.getString(4);
                tempDistrictName = resultSet.getString(1);
                long diffSeheri = 60000 * Long.parseLong(resultSet.getString(3));
                if(resultSet.getString(5).equals("1")){
                    tempSheheri = Long.toString( this.db.dateToMili(tempDate+tempSheheri) + diffSeheri);
                }else{
                    tempSheheri = Long.toString( this.db.dateToMili(tempDate+tempSheheri) - diffSeheri);
                }
                tempSheheri = this.db.miliSecToH(tempSheheri);

            /*Ifter Time Clculation*/
                tempIfter = DataBaseTimeShedule.getString(5);
                long diffIfter = 60000 * Long.parseLong(resultSet.getString(2));
                if(resultSet.getString(4).equals("1")){
                    tempIfter = Long.toString( this.db.dateToMili(tempDate+tempIfter) + diffIfter);
                }else{
                    tempIfter = Long.toString( this.db.dateToMili(tempDate+tempIfter) - diffIfter);
                }
                tempIfter = this.db.miliSecToH(tempIfter);
                diff = Long.parseLong(DataBaseTimeShedule.getString(1));
            }
        }

        //myFormat.setTimeZone(TimeZone.getTimeZone("Asia/Dhaka"));

        if(ramadanIsRunning ){
            String dayPostFix = "তম";
            String tempDiffString = Long.toString(diff);
            if(tempDiffString.equals("1") || tempDiffString.equals("5") || tempDiffString.equals("7") || tempDiffString.equals("8") || tempDiffString.equals("9") || tempDiffString.equals("10") ){
                dayPostFix = "ম";
            }else if(tempDiffString.equals("2") || tempDiffString.equals("3")){
                dayPostFix = "য়";
            }else if(tempDiffString.equals("4")){
                dayPostFix = "র্থ";
            }else if(tempDiffString.equals("6")){
                dayPostFix = "র্থ";
            }
            messageForTitle = "আজ "+tempDiffString+dayPostFix+" রমযান";
            welcom_sheheri.setText(tempSheheri);
            welcom_ifter.setText(tempIfter);
            dirstrictDisplay.setText("শুধুমাত্র "+tempDistrictName+" জেলার জন্য");
            column_02.setVisibility(View.VISIBLE);
            column_01.setVisibility(View.VISIBLE);
            column_03.setVisibility(View.VISIBLE);
        }else{
            column_02.setVisibility(View.GONE);
            column_01.setVisibility(View.GONE);
            column_03.setVisibility(View.GONE);
        }
        String[] bngNumber = {"০","১","২","৩","৪","৫","৬","৭","৮","৯"};
        for(int i=0; i<bngNumber.length; i++){
            messageForTitle = messageForTitle.replaceAll(Integer.toString(i),bngNumber[i]);
        }

        welcomeTitle.setTypeface(fontFamily);
        welcomeTitle.setText(messageForTitle);
    }
    public int daysBetween(Date d1, Date d2){
        return (int)( (d2.getTime() - d1.getTime()) / (1000 * 60 * 60 * 24));
    }

}