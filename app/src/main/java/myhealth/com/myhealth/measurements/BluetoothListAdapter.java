package myhealth.com.myhealth.measurements;

import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import myhealth.com.myhealth.R;

/**
 * Created by Sander on 22-9-2015.
 * For creating a nice ListView
 */
class BluetoothListAdapter extends ArrayAdapter<BluetoothDevice> {

    // The context
    private Context context;
    // The list of paired devices
    private List<BluetoothDevice> devices;

    public BluetoothListAdapter(Context context, int resource, ArrayList<BluetoothDevice> devices) {
        super(context, resource, devices);
        this.context = context;
        this.devices = devices;
    }

    @Override
    public int getCount() {
        return devices.size();
    }

    @Override
    public BluetoothDevice getItem(int position) {
        return devices.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        BluetoothDevice device = devices.get(position);

        // You should fetch the LayoutInflater once in your constructor
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.two_line_list_item, null);
        TextView name = (TextView) view.findViewById(R.id.device_name);
        TextView mac = (TextView) view.findViewById(R.id.device_mac);

        name.setText(device.getName());
        mac.setText(device.getAddress());

        return view;
    }
}
