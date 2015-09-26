package myhealth.com.myhealth.measurements;

import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import myhealth.com.myhealth.R;

/**
 * Created by Sander on 26-9-2015.
 * An Activity to connect to the Measurement device and receive the data
 */
public class DataReceiverFragment extends Fragment {

    private DataReceiverPresenter presenter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("MyHealth", "Fragment created!");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_bluetooth, container, false);
        // SEND THE VIEW
        presenter = new DataReceiverPresenter(this, view);
        Button button = (Button) view.findViewById(R.id.bluetooth_button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.buildDeviceList();
            }
        });
        return view;
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

    /**
     * Give a status update using Toast.
     *
     * @param status The text to display
     */
    public void giveStatusUpdate(final String status) {
        getActivity().runOnUiThread(new Runnable() {
            public void run() {
                Toast.makeText(getActivity().getApplicationContext(), status, Toast.LENGTH_LONG).show();
            }
        });
    }
}
