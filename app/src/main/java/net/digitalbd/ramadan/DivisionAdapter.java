package net.digitalbd.ramadan;

import android.content.Context;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;

/**
 * Created by Rukon on 22/5/2017.
 */

class SinlgeDivision{
    String title;
    String id;
    SinlgeDivision(String title, String id){
        this.id = id;
        this.title = title;
    }
}

public class DivisionAdapter extends BaseAdapter {
    public RsDb db;
    public Cursor resultSet;
    ArrayList<SinlgeDivision> divisionList;
    Context context;
    DivisionAdapter(Context c){
        this.context = c;
        db = new RsDb(this.context);
        resultSet = db.GetData("select * from district order by name ASC");
        divisionList = new ArrayList<SinlgeDivision>();
        while(resultSet.moveToNext()){
            divisionList.add(new SinlgeDivision(resultSet.getString(1),resultSet.getString(0)));
        }
    }
    @Override
    public int getCount() {
        return divisionList.size();
    }

    @Override
    public Object getItem(int position) {
        return divisionList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View row = inflater.inflate(R.layout.singe_list,parent,false);
        TextView title = (TextView) row.findViewById(R.id.title);
        SinlgeDivision temp = divisionList.get(position);
        title.setText(temp.title);
        Typeface fontFamily = Typeface.createFromAsset(context.getAssets(), "fonts/HindSiliguri-Regular.ttf");
        title.setTypeface(fontFamily);
        return row;
    }
}
