package myhealth.com.myhealth.measurements.viewing;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import myhealth.com.myhealth.R;

/**
 * Created by Sander on 5-10-2015.
 */
public class BPViewerFragment extends MeasurementViewerFragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);
        presenter = new BPPresenter(this, view);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        getActivity().setTitle(getString(R.string.bp_history));
    }
}
