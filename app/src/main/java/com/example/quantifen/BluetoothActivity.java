package com.example.quantifen;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.BluetoothManager;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanFilter;
import android.bluetooth.le.ScanResult;
import android.bluetooth.le.ScanSettings;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.hardware.camera2.params.BlackLevelPattern;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.ParcelUuid;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.SimpleExpandableListAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.api.GoogleApiClient;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.UUID;

public class BluetoothActivity extends AppCompatActivity {

    public static final String BLEPLUGIN_TAG = "BLEPLUGIN";

    private static final int REQUEST_ENABLE_BT = 1;

    private static BluetoothAdapter        bluetoothAdapter;
    private BluetoothDevice         bluetoothDevice;
    private String                  testUUID;
    private String                  actualUUID;
    private String                  testUUID2;
    private UUID                    quantifenUUID;

    private BluetoothGatt       gatt;

    private BluetoothLeService BLEDevice;

    private static final int STATE_LISTENING = 1;
    private static final int STATE_CONNECTING = 2;
    private static final int STATE_CONNECTED = 3;
    private static final int STATE_CONNECTION_FAILED = 4;
    private static final int STATE_MESSAGE_RECEIVED = 5;

    private static final String DEVICE_NAME = "Quantifen";
    private static final String APP_NAME = "Quantifen";

    private static boolean scanning = false;
    private static Handler scanHandler = new Handler();

    private static BluetoothManager bm;

    private static ArrayList<BluetoothLeService> discoveredDevices = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bluetooth);

        testUUID = "0000ffe1-0000-1000-8000-00805f9b34fb";
        testUUID2="00001101-0000-1000-8000-00805F9B34FB";

        quantifenUUID = UUID.fromString(testUUID);

        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        checkBluetooth(bluetoothAdapter);

        if (!getPackageManager().hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE)) {
            Toast.makeText(this, "BLE not supported", Toast.LENGTH_SHORT).show();
            finish();
        }

        if(bluetoothAdapter.isEnabled()){
            bluetoothAdapter.startDiscovery();
            IntentFilter intentFilter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
            registerReceiver(broadcastReceiver, intentFilter);

            listDevices(bluetoothAdapter);

            Intent intent = new Intent(BluetoothActivity.this, MainActivity.class);
            startActivity(intent);
        }

    }
    Handler connectionHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message message) {

            switch(message.what){
                case STATE_LISTENING:
                    System.out.println("Listening\n");
                    break;
                case STATE_CONNECTING:
                    System.out.println("Connecting\n");
                    break;
                case STATE_CONNECTED:
                    System.out.println("Connected\n");
                    break;
                case STATE_CONNECTION_FAILED:
                    System.out.println(("Connection Failed\n"));
                    break;
                case STATE_MESSAGE_RECEIVED:
                    byte [] readBuffer = (byte[]) message.obj;
                    String tmpM = new String(readBuffer, 0, message.arg1);
                    System.out.println(tmpM);
                    break;
            }
            return true;
        }
    });




    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == REQUEST_ENABLE_BT){
            if(resultCode == RESULT_OK){
                Toast.makeText(this, "Bluetooth is enabled", Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(BluetoothActivity.this, BluetoothActivity.class);
                startActivity(intent);

            }else if(resultCode == RESULT_CANCELED){
                Toast.makeText(this, "Bluetooth enabling cancelled", Toast.LENGTH_SHORT).show();
            }
        }
    }

    //Check if Bluetooth is on, if not on, ask user to turn on Bluetooth
    public void checkBluetooth(BluetoothAdapter bluetoothAdapter){
        if(bluetoothAdapter == null || !bluetoothAdapter.isEnabled()) {
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
        }
    }

    //Lists all paired devices and stores them in an arrayList and find Quantifen
    public void listDevices(final BluetoothAdapter bluetoothAdapter){
        Set<BluetoothDevice> pairedDevices = bluetoothAdapter.getBondedDevices();
        ArrayList pairedDeviceList = new ArrayList();
        String address = null;

        if (pairedDevices.size() > 0) {
            int i = 0;
            // There are paired devices. Get the name and address of each paired device.
            for (BluetoothDevice device : pairedDevices) {
                String deviceName = device.getName().trim();
                String deviceHardwareAddress = device.getAddress(); // MAC address
                if (deviceName.equals(DEVICE_NAME)){
                    address = device.getAddress().trim();
                    System.out.println(address +"\n");
                }
                pairedDeviceList.add("Device Name: " + deviceName + " Device MAC Address: " + deviceHardwareAddress);
            }

            System.out.println(pairedDeviceList.get(0) + "\n");

            final String finalAddress = address;
            AsyncTask.execute(new Runnable(){
                @Override
                public void run(){
                    bluetoothDevice = bluetoothAdapter.getRemoteDevice(finalAddress);
                    BLEDevice = new BluetoothLeService(bluetoothDevice, quantifenUUID);
                    BLEDevice.connect(BluetoothActivity.this, testUUID);
                }
            });


        }
    }

    BroadcastReceiver broadcastReceiver  = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            ArrayList discoveredDeviceList = new ArrayList();
            if(BluetoothDevice.ACTION_FOUND.equals(action)){
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                discoveredDeviceList.add(device.getName());
            }
        }
    };

    public static boolean isConnected(BluetoothDevice device) {
        List<BluetoothDevice> connectedDevices = bm.getConnectedDevices(BluetoothGatt.GATT);
        return connectedDevices.contains(device);
    }

    private static ScanCallback scanCallback = new ScanCallback() {
        @Override
        public void onScanResult(int callbackType, ScanResult result) {
            Log.v(BLEPLUGIN_TAG, result.toString());
            discoveredDevices.add(new BluetoothLeService(result.getDevice(), result.getScanRecord().getServiceUuids().get(0).getUuid()));
        }
        @Override
        public void onScanFailed(int errorCode){
            Log.v(BLEPLUGIN_TAG, "Scan failed with error: " + errorCode);
        }
        @Override
        public void onBatchScanResults(List<ScanResult> results){
            Log.v(BLEPLUGIN_TAG,"BATCHES");
        }
    };

    public static void startScan(String[] uuidStrings) {
        if (bluetoothAdapter != null) {

            // Create UUIDs
            ParcelUuid[] uuids = new ParcelUuid[uuidStrings.length];
            for (int i = 0; i < uuids.length; ++i) {
                uuids[i] = ParcelUuid.fromString(uuidStrings[i]);
            }

            // Clear previous results
            discoveredDevices.clear();

            // Stop scanning after 5 seconds
            scanHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    stopScan();
                }
            }, 10000);

            List<ScanFilter> scanFilters = new ArrayList<ScanFilter>();
            ScanFilter.Builder filterBuilder = new ScanFilter.Builder();
            ScanSettings.Builder settingsBuilder = new ScanSettings.Builder();

            settingsBuilder.setCallbackType(ScanSettings.CALLBACK_TYPE_FIRST_MATCH);

            for(int i = 0; i < uuids.length; i++){
                filterBuilder.setServiceUuid(uuids[i]);
                scanFilters.add(filterBuilder.build());
            }

            // Start scan
            Log.v(BLEPLUGIN_TAG, "Starting scan!");
            scanning = true;
            bluetoothAdapter.getBluetoothLeScanner().startScan(scanFilters, settingsBuilder.build(), scanCallback);
        }


    }
    public static void stopScan() {
        Log.v(BLEPLUGIN_TAG, "Stopping scan!");
        scanning = false;
        bluetoothAdapter.getBluetoothLeScanner().stopScan(scanCallback);
    }
    public static boolean isScanning() {
        return scanning;
    }

    public static ArrayList<BluetoothLeService> getDiscoveredDevices() {
        return discoveredDevices;
    }

}
