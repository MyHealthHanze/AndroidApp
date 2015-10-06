package myhealth.com.myhealth.measurements.viewing;

import android.view.View;

/**
 * Created by Sander on 5-10-2015.
 */
public class BPPresenter extends AbstractMeasurementPresenter {

    public BPPresenter(MeasurementViewerFragment fragment, View view) {
        super(fragment, view);
        listAdapter = new BPListAdapter(fragment.getActivity(), 0, measurementList);
        itemList.setAdapter(listAdapter);
        fillMeasurementList();
    }

    @Override
    public void fillMeasurementList() {
        Measurement mes = new Measurement();
        mes.setMeasurementDate("Date");
        mes.setMeasurementValue("This is a value");
        measurementList.clear();
        measurementList.add(mes);
        listAdapter.notifyDataSetChanged();
    }


}
