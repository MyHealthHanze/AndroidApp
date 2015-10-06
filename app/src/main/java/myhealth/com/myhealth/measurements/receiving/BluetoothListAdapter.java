package myhealth.com.myhealth.measurements.receiving;

import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import myhealth.com.myhealth.R;

/**
 * Created by Sander on 22-9-2015.
 * For creating a nice ListView
 */
class BluetoothListAdapter extends ArrayAdapter<BluetoothDevice> {

    // The layout inflater
    LayoutInflater inflater;
    // The list of paired devices
    private List<BluetoothDevice> devices;

    public BluetoothListAdapter(Context context, int resource, List<BluetoothDevice> devices) {
        super(context, resource, devices);
        this.devices = devices;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
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

        View view = inflater.inflate(R.layout.two_line_list_item, null);
        TextView name = (TextView) view.findViewById(R.id.device_name);
        TextView mac = (TextView) view.findViewById(R.id.device_mac);

        name.setText(device.getName());
        mac.setText(device.getAddress());

        return view;
    }
}
