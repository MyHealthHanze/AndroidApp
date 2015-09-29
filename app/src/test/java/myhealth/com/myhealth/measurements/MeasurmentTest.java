package myhealth.com.myhealth.measurements;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.view.View;
import android.widget.ListAdapter;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


/**
 * Created by wessel on 29/09/2015.
 */
@RunWith(MockitoJUnitRunner.class)
public class MeasurmentTest {
    private DataReceiverPresenter presenter;
    @Mock
    private DataReceiverPresenter.DataReceiverThread dataReceiverThread;
    @Mock
    private BluetoothAdapter adapter;
    @Mock
    private BluetoothDevice device;
    private Set<BluetoothDevice> deviceSet;
    @Mock
    private DataReceiverFragment fragment;
    @Mock
    private View view;
    @Mock
    private BluetoothListAdapter listAdapter;
    private ArrayList<BluetoothDevice> listData;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        deviceSet = new HashSet<BluetoothDevice>();
        deviceSet.add(device);
        presenter = new DataReceiverPresenter(fragment, view);
        presenter.setmTestBluetoothAdapter(adapter);
        presenter.setmTestDevice(device);
        presenter.setTestListAdapter(listAdapter);
        presenter.setTestListData(listData);
        presenter.setTestReceiver(dataReceiverThread);
    }

    @Test
    public void testBuildDeviceList() throws  Exception {
        when(adapter.getBondedDevices()).thenReturn(new HashSet<BluetoothDevice>());
        presenter.buildDeviceList();

        when(adapter.getBondedDevices()).thenReturn(deviceSet);
        presenter.buildDeviceList();

        verify(adapter, times(2)).enable();
        verify(listAdapter, times(3)).notifyDataSetChanged();
    }

    @Test
    public void testConnectToDevice() throws Exception {
        when(adapter.getBondedDevices()).thenReturn(new HashSet<BluetoothDevice>());
        when(device.getName()).thenReturn("test");
        presenter.buildDeviceList();
        presenter.connectToDevice(device.getName());

        when(adapter.getRemoteDevice(device.getName())).thenReturn(device);
        verify(dataReceiverThread, times(1)).cancel();
        verify(dataReceiverThread, times(1)).run();
    }
}
