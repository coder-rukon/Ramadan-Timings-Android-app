package net.digitalbd.ramadan;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.method.ScrollingMovementMethod;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;

public class Roja_Vonger_Karon extends AppCompatActivity {
    private TextView roja_vonger_karon_panel;
    private Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.roja__vonger__karon);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.title_roja_vangar_karon);
        setSupportActionBar(toolbar);
        roja_vonger_karon_panel = (TextView) findViewById(R.id.roja_vonger_karon_view);
        roja_vonger_karon_panel.setMovementMethod(new ScrollingMovementMethod());
        roja_vonger_karon_panel.setText(R.string.roja_vonger_karon_details);
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
