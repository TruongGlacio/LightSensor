package buixexamplecom.mylightsensor;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;
import android.provider.Settings;
import android.provider.Settings.SettingNotFoundException;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;

import static android.util.Log.*;

public class MainActivity extends AppCompatActivity implements SensorEventListener{
    private SensorManager mgr;
    private Sensor light;
    private TextView text;
    private CheckBox mCheckBox;
    int Valuelight=0;
    private SeekBar seekBar;
    String originalBrightness;
    int brightness=0;
    int progress=0;
    private StringBuilder msg = new StringBuilder(2048);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mgr = (SensorManager) this.getSystemService(SENSOR_SERVICE);
        mCheckBox=(CheckBox)findViewById(R.id.checkBox);
        light = mgr.getDefaultSensor(Sensor.TYPE_LIGHT);

        text = (TextView) findViewById(R.id.text);
        // phan cai dat do sang man hinh
      // setBrightness();
        final CheckBox checkBox = (CheckBox) findViewById(R.id.checkbox);
        if (checkBox.isChecked()) {
            progress=Valuelight/4;
        }
        else
        {
            progress=0;
        }
    }
    public void setBrightness()
    {
        seekBar = (SeekBar) findViewById(R.id.seekBar1);
        seekBar.setMax(300);
        originalBrightness = Settings.System.SCREEN_BRIGHTNESS;
        float curBrightnessValue = 0;
        try {
            curBrightnessValue = android.provider.Settings.System.getInt(getContentResolver(), android.provider.Settings.System.SCREEN_BRIGHTNESS);
        } catch (SettingNotFoundException e) {
            e.printStackTrace();
        }

        int screen_brightness = (int) curBrightnessValue;
        seekBar.setProgress(screen_brightness);
        seekBar.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {


         //   int progress=Valuelight/4;



            @Override
            public void onProgressChanged(SeekBar seekBar, int progresValue, boolean fromUser)
            {
                android.provider.Settings.System.putInt(getContentResolver(),android.provider.Settings.System.SCREEN_BRIGHTNESS, progress);
                //   progress = progresValue;
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                //// Do something here,
                // if you want to do anything at the start of
                // touching the seekbar
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                //setBrightness(brightness);
                android.provider.Settings.System.putInt(getContentResolver(),android.provider.Settings.System.SCREEN_BRIGHTNESS, progress);
            }
        });
    }
    protected void onResume() {
        mgr.registerListener(this, light, SensorManager.SENSOR_DELAY_NORMAL);
        super.onResume();
    }

    @Override
    protected void onPause() {
        mgr.unregisterListener(this, light);
        super.onPause();
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        msg.insert(0, "Light Value " + event.values[0] + "\n");
        int light_value= (int) event.values[0];
        Valuelight=light_value;
        Log.d("Valuelight=", "" + Valuelight);
        text.setText(light_value+"Lux");
        setBrightness();
       // text.invalidate();
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        msg.insert(0, sensor.getName() + " accuracy changed: " + accuracy +
                (accuracy==1?" (LOW)":(accuracy==2?" (MED)":" (HIGH)")) + "\n");
        //text.setText(msg);
      //  text.invalidate();
    }
}
