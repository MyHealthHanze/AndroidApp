package myhealth.com.myhealth.measurements.viewing;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import java.util.List;

import myhealth.com.myhealth.R;

/**
 * Created by Sander on 5-10-2015.
 */
public class BPListAdapter extends AbstractMeasurementListAdapter {

    public BPListAdapter(Context context, int resource, List<Measurement> measurements) {
        super(context, resource, measurements);
    }

    @Override
    protected void buildListItem(View view, Measurement measurement) {
        TextView date = (TextView) view.findViewById(R.id.measurement_date);
        TextView value = (TextView) view.findViewById(R.id.measurement_value);
        date.setText(measurement.getMeasurementDate());
        value.setText(getContext().getString(R.string.beat_per_minute) + measurement.getMeasurementValue());
    }
}
