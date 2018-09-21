package net.digitalbd.ramadan;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

/**
 * Created by Rukon on 23/5/2017.
 */

public class RsDb extends SQLiteOpenHelper {
    private static String DB_PATH = "";
    public Context context;
    public static final String db_Name = "ramadan_db1";
    public static final String settingTable = "settings";
    public static final String defaultDivisionCol = "defaultDivision";
    public SQLiteDatabase db;
    public RsDb(Context context) {
        super(context, db_Name, null, 1);
        db = this.getWritableDatabase();
        if(android.os.Build.VERSION.SDK_INT >= 17){
            DB_PATH = context.getApplicationInfo().dataDir + "/databases/";
        }
        else
        {
            DB_PATH = "/data/data/" + context.getPackageName() + "/databases/";
        }
        this.context = context;
        copyDataBase();
    }
    private void copyDataBase()
    {
        InputStream mInput = null;
        Cursor TempCursor = GetData("select * from base_time");
        if(TempCursor.getCount()>=1){
            return;
        }
        try {
            mInput = context.getAssets().open("database/"+db_Name+".db");
            String outFileName = DB_PATH + db_Name;
            OutputStream mOutput = new FileOutputStream(outFileName);
            byte[] mBuffer = new byte[1024];
            int mLength;
            while ((mLength = mInput.read(mBuffer))>0)
            {
                mOutput.write(mBuffer, 0, mLength);
            }
            mOutput.flush();
            mOutput.close();
            mInput.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table "+settingTable+" ("+defaultDivisionCol+" INTEGER)");
        db.execSQL("CREATE TABLE base_time(id integer,s_no integer,day_name varchar,date varchar,seheri varchar, ifter varchar)");
        db.execSQL("CREATE TABLE district(id integer,name varchar,ifter varchar,seheri varchar,ifter_type integer,seheri_type integer)");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table if exists"+settingTable);
        onCreate(db);
    }
    public boolean InsertData(String sql){
        this.db.execSQL(sql);
        return true;
    }
    public void UpdateData(String sqlData){
        this.db.execSQL(sqlData);
    }
    public void UpdateLastDivision(int id){
        this.db.execSQL("UPDATE "+this.settingTable+" SET "+this.defaultDivisionCol+" = "+id+"");
    }

    public Cursor GetData(String sqlData){
        return this.db.rawQuery(sqlData,null);
    }

    public void deleteData(String sql){
        this.db.rawQuery(sql,null);
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

}
