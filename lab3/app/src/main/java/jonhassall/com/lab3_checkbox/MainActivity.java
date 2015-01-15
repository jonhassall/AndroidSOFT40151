package jonhassall.com.lab3_checkbox;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Toast;


public class MainActivity extends Activity {

    private CheckBox chkIos, chkAndroid, chkWindows;
    private Button btnDisplay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        addListenerOnChkIos();
        addListenerOnButton();
    }

    public void addListenerOnChkIos() {
        chkIos = (CheckBox) findViewById(R.id.chkIos);
        chkIos.setOnClickListener(new View.OnClickListener() {
           @Override
        public void onClick(View v) {
               //Is iOS checked?
               if (((CheckBox) v).isChecked()) {
                   Toast.makeText(MainActivity.this, "iPhone is for n00bs", Toast.LENGTH_LONG).show();
               }
           }
        });
    }

    public void addListenerOnButton() {
        chkIos = (CheckBox) findViewById(R.id.chkIos);
        chkAndroid = (CheckBox) findViewById(R.id.chkAndroid);
        chkWindows = (CheckBox) findViewById(R.id.chkWindows);
        btnDisplay = (Button) findViewById(R.id.btnDisplay);

        btnDisplay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Run when button is clicked
                StringBuffer result = new StringBuffer();
                result.append("iPhone check: ").append(chkIos.isChecked());
                result.append("\nAndroid check: ").append(chkAndroid.isChecked());
                result.append("\nWindows Mobile check: ").append(chkWindows.isChecked());

                Toast.makeText(MainActivity.this, result.toString(), Toast.LENGTH_LONG).show();
                Log.d("Debug", result.toString());
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
}
