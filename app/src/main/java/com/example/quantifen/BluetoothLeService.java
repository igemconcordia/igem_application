package com.example.quantifen;

import android.app.Service;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.BluetoothManager;
import android.bluetooth.BluetoothProfile;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

import java.util.List;
import java.util.UUID;

public class BluetoothLeService extends BluetoothGattCallback {

    private BluetoothDevice device;
    private BluetoothGatt gatt;

    private UUID deviceUUID;
    private UUID characteristicUUID;

    private byte[] data = null;
    public static String rawSensor ="";
    public static String concentrationData="";
    public static String temperatureData="";

    public static String color ="";

    public BluetoothLeService(BluetoothDevice device, UUID uuid) {
        this.device = device;
        this.deviceUUID = uuid;
    }

    public void connect(Context context, String characteristicUUID) {
        gatt = device.connectGatt(context, true, this);
        this.characteristicUUID = UUID.fromString(characteristicUUID);
    }

    public boolean isConnected() {
        return BluetoothActivity.isConnected(device);
    }

    public synchronized boolean hasData() {
        return data != null;
    }

    public String getUUID() { return deviceUUID.toString(); }

    public synchronized byte[] popData() {
        byte[] bytes = data;
        data = null;
        return bytes;
    }

    @Override
    public void onConnectionStateChange(BluetoothGatt gatt, int status, int newState) {
        if (newState == BluetoothProfile.STATE_CONNECTED) {
            Log.i(BluetoothActivity.BLEPLUGIN_TAG, "Connected to GATT server.");
            Log.i(BluetoothActivity.BLEPLUGIN_TAG, "Attempting to start service discovery:" + gatt.discoverServices());

        }
        else if (newState == BluetoothProfile.STATE_DISCONNECTED) {
            Log.i(BluetoothActivity.BLEPLUGIN_TAG, "Disconnected from GATT server.");
        }
    }

    private String uuid1="00001800-0000-1000-8000-00805f9b34fb";
    private String uuid2="00001801-0000-1000-8000-00805f9b34fb";
    private String uuid3="0000180a-0000-1000-8000-00805f9b34fb";
    private String uuid4="0000ffe0-0000-1000-8000-00805f9b34fb";

    @Override
    public void onServicesDiscovered(BluetoothGatt gatt, int status) {
        if (status == BluetoothGatt.GATT_SUCCESS) {
            List<BluetoothGattService> services = gatt.getServices();

            // Search for correct characteristic and set notification
            for (BluetoothGattService service : services) {
                System.out.println(service.getUuid().toString()+ "\n");
                if(service.getUuid().toString().equals("0000ffe0-0000-1000-8000-00805f9b34fb")){
                    for (BluetoothGattCharacteristic characteristic : service.getCharacteristics()) {
                        if(characteristicUUID.equals(characteristic.getUuid())){
                            Log.i(BluetoothActivity.BLEPLUGIN_TAG, "Setting notification on for: " + characteristicUUID.toString());
                            gatt.setCharacteristicNotification(characteristic, true);

                            int result = characteristic.getProperties();

                            if(( result & BluetoothGattCharacteristic.PROPERTY_READ) != 0){
                                Log.i(BluetoothActivity.BLEPLUGIN_TAG, "We got read");
                            }
                            if((result & BluetoothGattCharacteristic.PROPERTY_NOTIFY) != 0){
                                Log.i(BluetoothActivity.BLEPLUGIN_TAG, "We got notify");
                            }

                            BluetoothGattDescriptor descriptor = characteristic.getDescriptor(UUID.fromString("0000ffe1-0000-1000-8000-00805f9b34fb"));
                            descriptor.setValue(BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE);

                            gatt.writeDescriptor(descriptor);
                        }
                    }
                }
            }
        }
    }

    @Override
    public void onCharacteristicChanged(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic) {
        if(characteristicUUID.equals(characteristic.getUuid())){
            //data = characteristic.getValue();
            data = characteristic.getValue();
            rawSensor = new String(data);
            String[] datas = rawSensor.split(",");
            concentrationData = datas[0];
            temperatureData = datas[1];

            double concentration = Double.parseDouble(concentrationData.trim());
            if(concentration > 0.1){
                color = "#FF0000";
            }else if(concentration < 0.1){
                color = "#32CD32";
            }
            //gatt.readCharacteristic(characteristic);
            //System.out.println(sensor+"\n");
        }
    }

    @Override
    public synchronized void onCharacteristicRead(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {
        if (status == BluetoothGatt.GATT_SUCCESS) {
            data = characteristic.getValue();
        }
    }


}
