package net.digitalbd.ramadan;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.BaseAdapter;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.zip.Inflater;

/**
 * Created by Rukon on 22/5/2017.
 */

class SingleDua{
    String title,banglaText,arabicText,bngOrtho;
    SingleDua(String title,String banglaText,String arabicText,String bngOrtho){
        this.title = title;
        this.banglaText = banglaText;
        this.arabicText = arabicText;
        this.bngOrtho = bngOrtho;
    }
}
public class Duwalist_Adapter extends BaseAdapter {
    private ArrayList<SingleDua> duaListArray;
    Context context;
    public  Resources rs;
    Duwalist_Adapter(Context c){
        this.context = c;
        duaListArray = new ArrayList<SingleDua>();
        rs = this.context.getResources();
        String[] duaString = rs.getStringArray(R.array.dualist_array);
        JSONObject jsonData;
        for (int i = 0; i<duaString.length; i++){
            String TempTitle,TempBng,TempAr,tempBngOrtho;
            TempTitle = "";
            tempBngOrtho = "";
            TempBng ="";
            TempAr = "";
            try {
                jsonData = new JSONObject(duaString[i]);
                TempTitle = jsonData.getString("title");
                TempBng += jsonData.getString("bng");
                tempBngOrtho += jsonData.getString("bngmeaning");
                TempAr = jsonData.getString("ar");
            } catch (JSONException e) {
                TempTitle = "error";
                e.printStackTrace();
            }
            duaListArray.add(new SingleDua(TempTitle,TempBng,TempAr,tempBngOrtho));
        }
    }
    @Override
    public int getCount() {
        return duaListArray.size();
    }

    @Override
    public Object getItem(int position) {
        return duaListArray.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View row = inflater.inflate(R.layout.dualist_item,parent,false);
        TextView rowTitle,rowBng,rowAr,rowBngMean;
        rowTitle =(TextView) row.findViewById(R.id.title);
        rowBngMean = (TextView) row.findViewById(R.id.bng_meaning);
        rowBng = (TextView) row.findViewById(R.id.bangla_conent);
        rowAr = (TextView) row.findViewById(R.id.arabic_content);
        SingleDua tempDua = duaListArray.get(position);
        rowBngMean.setText(tempDua.bngOrtho);
        rowTitle.setText(tempDua.title);
        rowAr.setText(tempDua.arabicText);
        rowBng.setText(tempDua.banglaText);
        Typeface fontFamily = Typeface.createFromAsset(context.getAssets(), "fonts/HindSiliguri-Regular.ttf");
        rowBngMean.setTypeface(fontFamily);
        rowTitle.setTypeface(fontFamily);
        rowBng.setTypeface(fontFamily);
        return row;
    }
}
