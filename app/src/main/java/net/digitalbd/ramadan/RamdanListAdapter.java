package net.digitalbd.ramadan;

import android.content.Context;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.v7.widget.Toolbar;
import android.text.format.DateUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

/**
 * Created by Rukon on 22/5/2017.
 */
class sinlgeRamdan{
    public String ramdan_date,seheri,iftar,ramdan_no;

    public sinlgeRamdan(String ramdan_no,String ramdan_date,String seheri,String iftar) {
        this.ramdan_date = ramdan_date;
        this.seheri = seheri;
        this.iftar = iftar;
        this.ramdan_no = ramdan_no;
    }
}

public class RamdanListAdapter extends BaseAdapter {
    ArrayList<sinlgeRamdan> ramdanList;
    public RsDb db;
    Toolbar toolbar;
    public Cursor resultSet,baseTimeScale;
    Context context;
    SinlgeDivision district;
    Resources rs;
    RamdanListAdapter(Context c,SinlgeDivision dis,Toolbar toolbar){
        this.context  = c;
        this.district = dis;
        this.toolbar =toolbar;
        this.rs = this.context.getResources();
        this.ramdanList = new ArrayList<sinlgeRamdan>();
        ramdanList.add(new sinlgeRamdan("","","",""));
        ramdanList.add(new sinlgeRamdan("","","",""));
        db = new RsDb(this.context);
        baseTimeScale = db.GetData("select * from base_time");
        resultSet = db.GetData("select * from district where id ="+this.district.id);
        resultSet.moveToFirst();
        int k = 0;


        while (baseTimeScale.moveToNext()){
            toolbar.setTitle(resultSet.getString(1) + " জেলার সময়");
            String tempDate,tempNo,tempSheheri,tempIfter;
            tempNo = baseTimeScale.getString(1);
            tempDate = baseTimeScale.getString(3);


            tempSheheri = baseTimeScale.getString(4);
            long diffSeheri = 60000 * Long.parseLong(resultSet.getString(3));
            if(resultSet.getString(5).equals("1")){
                tempSheheri = Long.toString( dateToMili(tempDate+tempSheheri) + diffSeheri);
            }else{
                tempSheheri = Long.toString( dateToMili(tempDate+tempSheheri) - diffSeheri);
            }
            tempSheheri = miliSecToH(tempSheheri);

            /*Ifter Time Clculation*/
            tempIfter = baseTimeScale.getString(5);
            long diffIfter = 60000 * Long.parseLong(resultSet.getString(2));
            if(resultSet.getString(4).equals("1")){
                tempIfter = Long.toString( dateToMili(tempDate+tempIfter) + diffIfter);
            }else{
                tempIfter = Long.toString( dateToMili(tempDate+tempIfter) - diffIfter);
            }

            tempIfter = miliSecToH(tempIfter);

            SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
            Date tempDateObj = new Date();

            try {
                tempDateObj = dateFormat.parse(tempDate);
                tempDate = new SimpleDateFormat("dd MMM").format(tempDateObj);
            } catch (ParseException e) {
                e.printStackTrace();
            }

            ramdanList.add(new sinlgeRamdan(tempNo,tempDate,tempSheheri,tempIfter));
            if(k == 9 || k == 19){
                ramdanList.add(new sinlgeRamdan("","","",""));
                ramdanList.add(new sinlgeRamdan("","","",""));
            }
            k++;
        }
    }
    public String miliSecToH(String miliSec){
        String result = "";
        String x =  miliSec.replaceAll(" " ,"");
        TimeZone.setDefault(TimeZone.getTimeZone("Asia/Dhaka"));
        DateFormat formatter = new SimpleDateFormat("HH:mm");
        formatter.setTimeZone(TimeZone.getTimeZone("Asia/Dhaka"));
        long milliSeconds= Long.parseLong(x);
        Date date=new Date(milliSeconds);
        result = formatter.format(date.getTime());
        return result;
    }
    public long dateToMili(String dateTemp){
        long result = 0;
        TimeZone.setDefault(TimeZone.getTimeZone("Asia/Dhaka"));
        SimpleDateFormat myFormat = new SimpleDateFormat("dd-MM-yyyyHH:mm");
        myFormat.setTimeZone(TimeZone.getTimeZone("Asia/Dhaka"));
        Date localTempDate = new Date();
        try {
            localTempDate = myFormat.parse(dateTemp.replaceAll(" " ,""));
            result = localTempDate.getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return result;
    }
    @Override
    public int getCount() {
        return ramdanList.size();
    }

    @Override
    public Object getItem(int position) {
        return ramdanList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        TimeZone.setDefault(TimeZone.getTimeZone("Asia/Dhaka"));
        SimpleDateFormat myFormat = new SimpleDateFormat("dd MM yyyy");
        Date ramadanDate = new Date();

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View row = inflater.inflate(R.layout.list_item,parent,false);
        Typeface fontFamily = Typeface.createFromAsset(context.getAssets(), "fonts/HindSiliguri-Regular.ttf");

        if(position == 1 || position == 13 || position == 25 ){
            return row;
        }
        /*10 days Middle Title*/
        LayoutInflater inflaterMiddleTitle = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowMiddleTitle = inflater.inflate(R.layout.singe_list,parent,false);
        TextView tittle  = (TextView) rowMiddleTitle.findViewById(R.id.title);
        tittle.setTypeface(fontFamily);
        rowMiddleTitle.setBackgroundColor(context.getResources().getColor(R.color.colorPrimaryDark));
        tittle.setGravity(Gravity.CENTER);
        tittle.setTextColor(context.getResources().getColor(R.color.colorPrimary));
        if(position == 0){
            tittle.setText("রহমাতের ১০ দিন");
            return  rowMiddleTitle;
        }
        if(position == 12){
            tittle.setText("মাগফিরাতের ১০ দিন");
            return  rowMiddleTitle;
        }
        if(position == 24){
            tittle.setText("নাজাতের ১০ দিন");
            return  rowMiddleTitle;
        }

        sinlgeRamdan temp = ramdanList.get(position);
        try {
            ramadanDate = myFormat.parse(temp.ramdan_date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        if(DateUtils.isToday(ramadanDate.getDate())){
            row.setBackgroundColor(context.getResources().getColor(R.color.colorPrimaryDark));
        }

        TextView ramdanDate = (TextView) row.findViewById(R.id.ramdan_date);
        TextView ramdamNo = (TextView) row.findViewById(R.id.ramdan_no);
        TextView ramdan_seheri_time = (TextView) row.findViewById(R.id.seheri_time);
        TextView ramdan_iftar_time = (TextView) row.findViewById(R.id.iftar_time);
        ramdanDate.setText(temp.ramdan_date);
        ramdamNo.setText(temp.ramdan_no);
        ramdan_seheri_time.setText(temp.seheri);
        ramdan_iftar_time.setText(temp.iftar);
        ramdanDate.setTypeface(fontFamily);
        ramdamNo.setTypeface(fontFamily);
        ramdan_seheri_time.setTypeface(fontFamily);
        ramdan_iftar_time.setTypeface(fontFamily);

        return row;
    }
}
