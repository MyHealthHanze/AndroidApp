package myhealth.com.myhealth.measurements;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import myhealth.com.myhealth.R;

/**
 * Created by Sander on 26-9-2015.
 * The logic for connecting over bluetooth and receiving the data
 */
class DataReceiverPresenter {

    private static final String DEBUG_TAG = "MyHealth_Bluetooth";
    // The bluetooth adapter
    protected BluetoothAdapter mBluetoothAdapter;
    // The fragment to control
    private DataReceiverFragment fragment;
    // The list of paired devices
    private ArrayList<BluetoothDevice> listData;
    // The adapter to match the arraylist to a listview
    private BluetoothListAdapter listAdapter;
    // The receiver
    private DataReceiverThread receiver;
    // The data manager
    private MeasurementManager manager;

    // The receiver to mock
    private DataReceiverThread testReceiver;
    // Bluetooth device to mock
    private BluetoothDevice mTestDevice;
    // Bluetooth adapter to mock
    private BluetoothAdapter mTestBluetoothAdapter;


    /**
     * sets a data receiver thread for mocking
     *
     * @param receiver
     */
    protected void setTestReceiver(DataReceiverThread receiver) {
        this.receiver = receiver;
        this.testReceiver = receiver;
    }

    /**
     * sets a list data for mocking
     *
     * @param listData
     */
    protected void setTestListData(ArrayList<BluetoothDevice> listData) {
        this.listData = listData;
    }

    /**
     * sets a list adapter for mocking
     *
     * @param listAdapter
     */
    protected void setTestListAdapter(BluetoothListAdapter listAdapter) {
        this.listAdapter = listAdapter;
    }

    /**
     * sets a bluetooth device for mocking
     *
     * @param mTestDevice
     */
    protected void setmTestDevice(BluetoothDevice mTestDevice) {
        this.mTestDevice = mTestDevice;
    }

    /**
     * sets a bluetooth adapter for mocking
     *
     * @param mTestBluetoothAdapter
     */
    protected void setmTestBluetoothAdapter(BluetoothAdapter mTestBluetoothAdapter) {
        this.mTestBluetoothAdapter = mTestBluetoothAdapter;
    }

    /**
     * Construct a Presenter to control the fragment
     *
     * @param fragment The fragment to manage
     */
    public DataReceiverPresenter(DataReceiverFragment fragment, View view) {
        this.fragment = fragment;
        manager = new MeasurementManager(fragment.getActivity(), (AppCompatActivity) fragment.getActivity());
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
        if(mTestBluetoothAdapter != null){
            mBluetoothAdapter = mTestBluetoothAdapter;
        }else {
            mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        }
        mBluetoothAdapter.enable();
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
    protected void connectToDevice(String address) {
        BluetoothDevice device = mBluetoothAdapter.getRemoteDevice(address);
        if (receiver != null) {
            receiver.cancel();
        }
        if (testReceiver != null){
            receiver = testReceiver;
        } else {
            receiver = new DataReceiverThread(device);
        }
        new Thread(receiver).start();
    }

    /**
     * Inner class to handle an outgoing Bluetooth connection
     */
    protected class DataReceiverThread implements Runnable {

        // Unique identifier necessary for a connection
        private static final String UUID_STRING = "34824060-611f-11e5-a837-0800200c9a66";
        // The bluetoothsocket
        private BluetoothSocket mmSocket;

        /**
         * Create the DataReceiverThread thread to try and connect with the given device
         *
         * @param device The device to connect to
         */
        public DataReceiverThread(BluetoothDevice device) {
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
            List<String> json = null;
            try {
                // Connect the device through the socket. This will block
                // until it succeeds or throws an exception
                mmSocket.connect();
                fragment.giveStatusUpdate(fragment.getString(R.string.connection_success));
                json = readFromDevice();
            } catch (IOException connectException) {
                // Unable to connect; close the socket and get out
                fragment.giveStatusUpdate(fragment.getString(R.string.connection_error));
                Log.d(DEBUG_TAG, "IOException: " + connectException);
                connectException.printStackTrace();
                try {
                    mmSocket.close();
                } catch (IOException closeException) {
                    Log.d(DEBUG_TAG, "" + closeException);
                }
            }
            if (json != null && json.size() > 0) {
                for (String e :
                        json) {
                    Log.d(DEBUG_TAG, e);
                }
                manager.saveMeasurements(json);
            }
        }

        /**
         * Read incoming data from the device
         */
        private List<String> readFromDevice() {
            String tmp;
            List<String> json = new ArrayList<>();
            try {
                BufferedReader reader = new BufferedReader(new InputStreamReader(mmSocket.getInputStream()));
                while ((tmp = reader.readLine()).length() > 0) {
                    json.add(tmp);
                }
            } catch (IOException e) {
                // Do nothing
            }
            fragment.giveStatusUpdate(fragment.getString(R.string.data_received));
            return json;
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
