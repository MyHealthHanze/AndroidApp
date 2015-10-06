package myhealth.com.myhealth.measurements.viewing;

import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import myhealth.com.myhealth.R;
import myhealth.com.myhealth.measurements.data.MeasurementManager;

/**
 * Created by Sander on 5-10-2015.
 */
public abstract class AbstractMeasurementPresenter {

    // The fragment to manage
    protected MeasurementViewerFragment fragment;
    // The data manager
    protected MeasurementManager manager;
    // The list of measurements
    protected List<Measurement> measurementList;
    // The ListView
    protected ListView itemList;
    // The adapter to match the measurementList to the itemList
    protected AbstractMeasurementListAdapter listAdapter;

    /**
     * Construct a Presenter to control the fragment
     *
     * @param fragment The fragment to manage
     * @param view
     */
    public AbstractMeasurementPresenter(MeasurementViewerFragment fragment, View view) {
        this.fragment = fragment;
        manager = new MeasurementManager(fragment.getActivity(), (AppCompatActivity) fragment.getActivity());
        // Build the listview
        itemList = (ListView) view.findViewById(R.id.measurement_list);
        measurementList = new ArrayList<>();
    }

    public abstract void fillMeasurementList();
}
