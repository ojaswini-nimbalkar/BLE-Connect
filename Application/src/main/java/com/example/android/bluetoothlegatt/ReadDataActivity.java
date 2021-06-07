package com.example.android.bluetoothlegatt;

import android.app.Activity;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattService;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.ms.square.android.expandabletextview.ExpandableTextView;

import java.util.List;

public class ReadDataActivity extends Activity {
    private final static String TAG = ReadDataActivity.class.getSimpleName();
    public static final String EXTRAS_DEVICE_NAME = "DEVICE_NAME";
    public static final String EXTRAS_DEVICE_ADDRESS = "DEVICE_ADDRESS";

    private final String LIST_NAME = "NAME";
    private final String LIST_UUID = "UUID";

    private ExpandableTextView mExpTvSensorData;
    private TextView mConnectionState;
    private TextView mTvUuidCharacteristic;
    private Button mBtnReadData;

    private String mDeviceAddress;
    private String mDeviceName;
    private BluetoothLeService mBluetoothLeService;

    private boolean mConnected = false;
    private BluetoothGattCharacteristic mNotifyCharacteristic;
    private BluetoothGattCharacteristic mSensorDataCharacteristic;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.data_characteristic);
        final Intent intent = getIntent();
        mDeviceName = intent.getStringExtra(EXTRAS_DEVICE_NAME);
        mDeviceAddress = intent.getStringExtra(EXTRAS_DEVICE_ADDRESS);
        ((TextView) findViewById(R.id.device_address)).setText(mDeviceAddress);

        mConnectionState = findViewById(R.id.connection_state);
        // getting reference of  ExpandableTextView
        mExpTvSensorData =  findViewById(R.id.expand_text_view).findViewById(R.id.expand_text_view);
        mExpTvSensorData.setText(getString(R.string.no_data));
        mTvUuidCharacteristic = findViewById(R.id.txtUUID);
        mBtnReadData = findViewById(R.id.btnReadData);
        mBtnReadData.setEnabled(false);
        mBtnReadData.setOnClickListener( new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                readCharacteristic();
                //new changes


            }
        });

        getActionBar().setTitle(mDeviceName);
        getActionBar().setDisplayHomeAsUpEnabled(true);
        Intent gattServiceIntent = new Intent(this, BluetoothLeService.class);
        bindService(gattServiceIntent, mServiceConnection, BIND_AUTO_CREATE);
    }
    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver(mGattUpdateReceiver, makeGattUpdateIntentFilter());
        if (mBluetoothLeService != null) {
            final boolean result = mBluetoothLeService.connect(mDeviceAddress);
            Log.d(TAG, "Connect request result=" + result);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(mGattUpdateReceiver);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbindService(mServiceConnection);
        mBluetoothLeService = null;
    }

    private final ServiceConnection mServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder service) {
            mBluetoothLeService = ((BluetoothLeService.LocalBinder) service).getService();
            if (!mBluetoothLeService.initialize()) {
                Log.e(TAG, "Unable to initialize Bluetooth");
                finish();
            }

            // Automatically connects to the device upon successful start-up initialization.
            mBluetoothLeService.connect(mDeviceAddress);
        }
        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            mBluetoothLeService = null;
        }
    };
    private final BroadcastReceiver mGattUpdateReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            final String action = intent.getAction();
            if (BluetoothLeService.ACTION_GATT_CONNECTED.equals(action)) {
                mConnected = true;
                updateConnectionState(R.string.connected);
                invalidateOptionsMenu();
            } else if (BluetoothLeService.ACTION_GATT_DISCONNECTED.equals(action)) {
                mConnected = false;
                updateConnectionState(R.string.disconnected);
                invalidateOptionsMenu();
                clearUI();
            } else if (BluetoothLeService.ACTION_GATT_SERVICES_DISCOVERED.equals(action)) {
                // Show all the supported services and characteristics on the user interface.
                mSensorDataCharacteristic = filterGattServices(mBluetoothLeService.getSupportedGattServices());
                if(mSensorDataCharacteristic != null){
                    mTvUuidCharacteristic.setText(mSensorDataCharacteristic.getUuid().toString());
                    mBtnReadData.setEnabled(true);
                }
            } else if (BluetoothLeService.ACTION_DATA_AVAILABLE.equals(action)) {
                displayData(intent.getStringExtra(BluetoothLeService.EXTRA_DATA));
            }
        }
    };

    private void readCharacteristic(){
        if(mSensorDataCharacteristic == null){
            Log.d(TAG, "Can't find the characteristic uuid:" + SampleGattAttributes.GATT_CHARACTERISTIC);
            return;
        }
        final int charaProp =  mSensorDataCharacteristic.getProperties();
        if ((charaProp | BluetoothGattCharacteristic.PROPERTY_NOTIFY) > 0) {
            Log.d(TAG, "Checking for notifying property");

            mNotifyCharacteristic = mSensorDataCharacteristic;
            mBluetoothLeService.setCharacteristicNotification(
                    mNotifyCharacteristic, true); //mSensorDataCharcteristics
            //mBluetoothLeService.readCharacteristic(mSensorDataCharacteristic); //newly added
        }
        if ((charaProp | BluetoothGattCharacteristic.PROPERTY_READ) > 0){
            if (mNotifyCharacteristic != null) {
                Log.d(TAG, "entered this loop since notification is enabled");
                mBluetoothLeService.setCharacteristicNotification(mNotifyCharacteristic, true);  //false previously
                mNotifyCharacteristic = null;
            }
            mBluetoothLeService.readCharacteristic(mSensorDataCharacteristic);
        }
    }
    private void updateConnectionState(final int resourceId) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mConnectionState.setText(resourceId);
            }
        });
    }

    private void displayData(String data) {
        mExpTvSensorData.setText("");
        if (data != null) {
            Toast.makeText(getApplicationContext(),data,Toast.LENGTH_LONG).show();
            mExpTvSensorData.setText(data);
        }
    }

        private BluetoothGattCharacteristic filterGattServices(List<BluetoothGattService> gattServices){
            BluetoothGattCharacteristic result = null;
        if(gattServices == null){
            return result;
        }
        String uuidCharacteristic = null;
        for(BluetoothGattService gattService : gattServices){
            List<BluetoothGattCharacteristic> gattCharacteristics = gattService.getCharacteristics();
            for(BluetoothGattCharacteristic gattCharacteristic : gattCharacteristics){
                uuidCharacteristic = gattCharacteristic.getUuid().toString();
                if(uuidCharacteristic.equals(SampleGattAttributes.GATT_CHARACTERISTIC) ||
                    uuidCharacteristic.equals(SampleGattAttributes.GATT_CHARACTERISTIC_TEST)){

                    return gattCharacteristic;
                }
            }
        }
        return result;
    }


    private static IntentFilter makeGattUpdateIntentFilter() {
        final IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(BluetoothLeService.ACTION_GATT_CONNECTED);
        intentFilter.addAction(BluetoothLeService.ACTION_GATT_DISCONNECTED);
        intentFilter.addAction(BluetoothLeService.ACTION_GATT_SERVICES_DISCOVERED);
        intentFilter.addAction(BluetoothLeService.ACTION_DATA_AVAILABLE);
        return intentFilter;
    }

    private void clearUI() {
        //mGattServicesList.setAdapter((SimpleExpandableListAdapter) null);
        mExpTvSensorData.setText(getString(R.string.no_data));
    }

}
