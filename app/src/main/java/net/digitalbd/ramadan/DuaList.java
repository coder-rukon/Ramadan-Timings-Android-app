package net.digitalbd.ramadan;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ListView;

import java.util.ArrayList;

public class DuaList extends AppCompatActivity {
    ListView duaList;
    private Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dua_list);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.title_dua);
        setSupportActionBar(toolbar);
        duaList = (ListView) findViewById(R.id.dua_list_scroll_view);
        duaList.setAdapter(new Duwalist_Adapter(this));
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
                Intent intentHome = new Intent(this,MainActivity.class);
                startActivity(intentHome);
                break;
            default:
                break;
        }
        return true;
    }
}
