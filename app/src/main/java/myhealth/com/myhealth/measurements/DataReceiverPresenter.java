package myhealth.com.myhealth.measurements;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Set;
import java.util.UUID;

import myhealth.com.myhealth.R;

/**
 * Created by Sander on 26-9-2015.
 * The logic for connecting over bluetooth and receiving the data
 */
class DataReceiverPresenter {

    // The bluetooth adapter
    protected BluetoothAdapter mBluetoothAdapter;
    // The fragment to control
    private DataReceiverFragment fragment;
    // The list of paired devices
    private ArrayList<BluetoothDevice> listData;
    // The adapter to match the arraylist to a listview
    private BluetoothListAdapter listAdapter;
    // The connector
    private BluetoothConnection connector;

    /**
     * Construct a Presenter to control the fragment
     *
     * @param fragment The fragment to manage
     */
    public DataReceiverPresenter(DataReceiverFragment fragment, View view) {
        this.fragment = fragment;
        // Build the listview
        ListView itemList = (ListView) view.findViewById(R.id.list_view);
        listData = new ArrayList<>();
        listAdapter = new BluetoothListAdapter(fragment.getActivity(), 0, listData);
        itemList.setAdapter(listAdapter);
        itemList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View viewClicked, int position, long id) {
                TextView text = (TextView) viewClicked.findViewById(R.id.device_mac);
                connectToDevice(text.getText().toString());
            }
        });
        // Populate the listview
        buildDeviceList();
    }

    /**
     * Build a list of paired devices
     */
    public void buildDeviceList() {
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        Set<BluetoothDevice> pairedDevices = mBluetoothAdapter.getBondedDevices();
        listData.clear();
        listAdapter.notifyDataSetChanged();
        if (pairedDevices.size() > 0) {
            // Loop through paired devices
            for (BluetoothDevice device : pairedDevices) {
                // Add the name and address to an array adapter to show in a ListView
                listData.add(device);
                listAdapter.notifyDataSetChanged();
            }
        }
    }

    /**
     * Connect to the chosen device
     *
     * @param address The MAC address of the device
     */
    private void connectToDevice(String address) {
        BluetoothDevice device = mBluetoothAdapter.getRemoteDevice(address);
        if (connector != null) {
            connector.cancel();
        }
        connector = new BluetoothConnection(device);
        new Thread(connector).start();
    }

    /**
     * Inner class to handle an outgoing Bluetooth connection
     */
    private class BluetoothConnection implements Runnable {

        // Unique identifier necessary for a connection
        private static final String UUID_STRING = "34824060-611f-11e5-a837-0800200c9a66";
        // The bluetoothsocket
        private BluetoothSocket mmSocket;

        /**
         * Create the BluetoothConnection thread to try and connect with the given device
         *
         * @param device The device to connect to
         */
        public BluetoothConnection(BluetoothDevice device) {
            // Use a temporary object that is later assigned to mmSocket,
            // because mmSocket is final
            BluetoothSocket tmp = null;
            // Get a BluetoothSocket to connect with the given BluetoothDevice
            try {
                // MY_UUID is the app's UUID string, also used by the server code
                tmp = device.createRfcommSocketToServiceRecord(UUID.fromString(UUID_STRING));
            } catch (IOException e) {
                // Do nothing
            }
            mmSocket = tmp;
        }

        /**
         * Try and connect to the device
         */
        @Override
        public void run() {
            // Cancel discovery because it will slow down the connection
            mBluetoothAdapter.cancelDiscovery();
            try {
                // Connect the device through the socket. This will block
                // until it succeeds or throws an exception
                mmSocket.connect();
                fragment.giveStatusUpdate(fragment.getString(R.string.connection_success));
                readFromDevice();
            } catch (IOException connectException) {
                // Unable to connect; close the socket and get out
                fragment.giveStatusUpdate(fragment.getString(R.string.connection_error));
                Log.d("BluetoothTest", connectException.getMessage());
                try {
                    mmSocket.close();
                } catch (IOException closeException) {
                    Log.d("BluetoothTest", closeException.getMessage());
                }
            }
        }

        /**
         * Read incoming data from the device
         */
        private void readFromDevice() throws IOException {
            BufferedReader reader = new BufferedReader(new InputStreamReader(mmSocket.getInputStream()));
            fragment.giveStatusUpdate(reader.readLine());
        }

        /**
         * Will cancel an in-progress connection, and close the socket
         */
        public void cancel() {
            try {
                mmSocket.close();
            } catch (IOException e) {
                // Do nothing
            }
        }
    }
}