package myhealth.com.myhealth.measurements.viewing;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import java.util.List;

import myhealth.com.myhealth.R;

/**
 * Created by Sander on 5-10-2015.
 */
public abstract class AbstractMeasurementListAdapter extends ArrayAdapter<Measurement> {

    // The layout inflater
    LayoutInflater inflater;
    // The list of paired measurements
    private List<Measurement> measurements;

    public AbstractMeasurementListAdapter(Context context, int resource, List<Measurement> measurements) {
        super(context, resource, measurements);
        this.measurements = measurements;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return measurements.size();
    }

    @Override
    public Measurement getItem(int position) {
        return measurements.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Measurement measurement = measurements.get(position);

        View view = inflater.inflate(R.layout.measurement_list_item, null);
        buildListItem(view, measurement);

        return view;
    }

    protected abstract void buildListItem(View view, Measurement measurement);
}
