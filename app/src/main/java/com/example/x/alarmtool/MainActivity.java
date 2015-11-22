package com.example.x.alarmtool;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.Arrays;
import java.util.List;


public class MainActivity extends AppCompatActivity {


    EditText hours_tx, min_tx, sec_tx;
    PendingIntent pi;
    BroadcastReceiver br;
    AlarmManager am;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        List<String> buttonNames = Arrays.asList("Parking Meter", "Oven", "Other");
        am = (AlarmManager)(this.getSystemService( Context.ALARM_SERVICE ));
        hours_tx = (EditText)findViewById(R.id.editText);
        min_tx = (EditText)findViewById(R.id.editText2);
        sec_tx = (EditText)findViewById(R.id.editText3);
        ListView lv = (ListView)findViewById(R.id.listView);
        //Button b1 = new Button(this);
        //lv.addView(b1);
        lv.setAdapter(new ButtonListAdapter(this, R.layout.list_item, buttonNames));
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private class ButtonListener implements View.OnClickListener
    {
        String title, text;
        public ButtonListener(String _title, String _text)
        {
            super();
            title = _title;
            text = _text;
        }
        public ButtonListener()
        {
            super();
            title = "Alarm";
            text = "";
        }
        @Override
        public void onClick(View v) {
            boolean parse_error = false;
            int hours=0, minutes=0, seconds=0;
            try
            {
                if(hours_tx.getText().length()!=0)
                    hours = Integer.parseInt(hours_tx.getText().toString());
            }
            catch (NumberFormatException e)
            {
                parse_error = true;
                hours=0;
            }
            try
            {
                if(min_tx.getText().length()!=0)
                    minutes = Integer.parseInt(min_tx.getText().toString());
            }
            catch (NumberFormatException e)
            {
                parse_error = true;
                minutes = 0;
            }
            try
            {
                if(sec_tx.getText().length()!=0)
                    seconds = Integer.parseInt(sec_tx.getText().toString());
            }
            catch (NumberFormatException e)
            {
                parse_error = true;
                seconds=0;
            }
            if(parse_error)
            {
                Toast.makeText(getBaseContext(), "Parse Error", Toast.LENGTH_LONG).show();
            }
            //notification stuff
            BroadcastReceiver br = new AlarmReceiver(title, text);

            registerReceiver(br, new IntentFilter("com.com.example.x.alarmtool"));
            Intent intent1 = new Intent("com.com.example.x.alarmtool");
            intent1.putExtra("Title", title);//overkill?
            pi = PendingIntent.getBroadcast( v.getContext(), 0, intent1,
                    0 );
            Toast.makeText(getBaseContext(), Integer.toString(hours) + ":" + Integer.toString(minutes)
                    + ":" + Integer.toString(seconds), Toast.LENGTH_LONG).show();
            am.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, SystemClock.elapsedRealtime() +
                    36000000 * hours + 60000 * minutes + 1000*seconds, pi);
        }
    }
    private class ButtonListAdapter extends ArrayAdapter<String>
    {

        private int layout;
        public ButtonListAdapter( Context c, int res, List<String> obj)
        {
            super(c, res, obj);
            layout = res;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder mainViewHolder = null;
            if(convertView == null)
            {

                LayoutInflater inflater = LayoutInflater.from(getContext());
                convertView = inflater.inflate(layout, parent, false);
                ViewHolder viewHolder = new ViewHolder();
                viewHolder.button = (Button)convertView.findViewById(R.id.list_item_button);
                viewHolder.button.setText(getItem(position));
                viewHolder.button.setOnClickListener(new ButtonListener(getItem(position), ""));
                convertView.setTag(viewHolder);
            }
            else
            {
                mainViewHolder = (ViewHolder)convertView.getTag();
                mainViewHolder.button.setText(getItem(position));
                mainViewHolder.button.setOnClickListener(new ButtonListener(getItem(position), ""));
            }
            //return super.getView(position, convertView, parent);
            return convertView;
        }
    }

    public class ViewHolder
    {
        Button button;
    }
}
