package com.star.weatherstation;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener2;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {

    private SensorManager mSensorManager;
    private TextView mTemperatureTextView;
    private TextView mPressureTextView;
    private TextView mLightTextView;

    private float currentTemperature = Float.NaN;
    private float currentPressure = Float.NaN;
    private float currentLight = Float.NaN;

    private final SensorEventListener2 mTemperatureSensorEventListener2 = new SensorEventListener2() {
        @Override
        public void onFlushCompleted(Sensor sensor) {

        }

        @Override
        public void onSensorChanged(SensorEvent event) {
            currentTemperature = event.values[0];
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {

        }
    };

    private final SensorEventListener2 mPressureSensorEventListener2 = new SensorEventListener2() {
        @Override
        public void onFlushCompleted(Sensor sensor) {

        }

        @Override
        public void onSensorChanged(SensorEvent event) {
            currentPressure = event.values[0];
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {

        }
    };

    private final SensorEventListener2 mLightSensorEventListener2 = new SensorEventListener2() {
        @Override
        public void onFlushCompleted(Sensor sensor) {

        }

        @Override
        public void onSensorChanged(SensorEvent event) {
            currentLight = event.values[0];
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mTemperatureTextView = (TextView) findViewById(R.id.temperature);
        mPressureTextView = (TextView) findViewById(R.id.pressure);
        mLightTextView = (TextView) findViewById(R.id.light);

        mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);

        Timer updateTimer = new Timer("weatherUpdate");
        updateTimer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                updateGUI();
            }
        }, 0, 1000);
    }

    @Override
    protected void onResume() {
        super.onResume();

        Sensor temperatureSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_TEMPERATURE);

        if (temperatureSensor != null) {
            mSensorManager.registerListener(mTemperatureSensorEventListener2,
                    temperatureSensor,
                    SensorManager.SENSOR_DELAY_NORMAL);
        } else {
            mTemperatureTextView.setText("Temperature Sensor Unavailable");
        }

        Sensor pressureSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_PRESSURE);

        if (pressureSensor != null) {
            mSensorManager.registerListener(mPressureSensorEventListener2,
                    pressureSensor,
                    SensorManager.SENSOR_DELAY_NORMAL);
        } else {
            mPressureTextView.setText("Pressure Sensor Unavailable");
        }

        Sensor lightSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);

        if (lightSensor != null) {
            mSensorManager.registerListener(mLightSensorEventListener2,
                    lightSensor,
                    SensorManager.SENSOR_DELAY_NORMAL);
        } else {
            mLightTextView.setText("Light Sensor Unavailable");
        }
    }

    @Override
    protected void onPause() {

        mSensorManager.unregisterListener(mTemperatureSensorEventListener2);
        mSensorManager.unregisterListener(mPressureSensorEventListener2);
        mSensorManager.unregisterListener(mLightSensorEventListener2);

        super.onPause();
    }

    private void updateGUI() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (!Float.isNaN(currentTemperature)) {
                    mTemperatureTextView.setText(currentTemperature + "C");
                    mTemperatureTextView.invalidate();
                }

                if (!Float.isNaN(currentPressure)) {
                    mPressureTextView.setText(currentPressure + "hPa");
                    mPressureTextView.invalidate();
                }

                if (!Float.isNaN(currentLight)) {
                    String lightStr = "Sunny";

                    if (currentLight <= SensorManager.LIGHT_CLOUDY) {
                        lightStr = "Night";
                    } else if (currentLight <= SensorManager.LIGHT_OVERCAST) {
                        lightStr = "Cloudy";
                    } else if (currentLight <= SensorManager.LIGHT_SUNLIGHT) {
                        lightStr = "Overcast";
                    }

                    mLightTextView.setText(lightStr);
                    mLightTextView.invalidate();
                }
            }
        });
    }
}
