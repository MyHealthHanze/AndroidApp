package myhealth.com.myhealth.measurements.viewing;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import myhealth.com.myhealth.R;

/**
 * Created by Sander on 5-10-2015.
 * A class for viewing measurements
 */
public abstract class MeasurementViewerFragment extends Fragment {

    // The presenter
    protected AbstractMeasurementPresenter presenter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_measurement_viewer, container, false);
        return view;
    }
}
