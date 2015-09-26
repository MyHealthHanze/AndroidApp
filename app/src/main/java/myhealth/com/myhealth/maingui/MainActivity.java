package myhealth.com.myhealth.maingui;

import android.app.Fragment;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import myhealth.com.myhealth.R;
import myhealth.com.myhealth.measurements.DataReceiverFragment;
import myhealth.com.myhealth.welcome.WelcomeFragment;

/**
 * Created by Sander on 26-9-2015.
 * The Main Activity that holds the possible fragments and builds the Drawer list
 */
public class MainActivity extends AppCompatActivity {

    private String[] fragments = new String[]{"Measurements", "Receive measurements"};
    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        createDrawer();
        getFragmentManager().beginTransaction().replace(R.id.fragment_holder, new WelcomeFragment()).commit();
    }

    /**
     * Create the drawer
     */
    private void createDrawer() {
        fragments = new String[]{getString(R.string.drawer_view_measurements), getString(R.string.drawer_receive_measurements), "Welcome"};
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList = (ListView) findViewById(R.id.left_drawer);

        ArrayAdapter drawerAdapter = new ArrayAdapter<>(this,
                R.layout.drawer_list_item, fragments);
        // Set the adapter for the list view
        mDrawerList.setAdapter(drawerAdapter);
        drawerAdapter.notifyDataSetChanged();

        // Set the list's click listener
        mDrawerList.setOnItemClickListener(new ListView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selectFragment(position);
            }
        });
    }

    /**
     * Change the current fragment, based on the selection in the drawer
     *
     * @param id The id of the new fragment
     */
    public void selectFragment(int id) {
        Fragment fragment = new WelcomeFragment();
        Log.d("MyHealth", "Fragment id: " + id);
        switch (id) {
            case 0:
                getFragmentManager().beginTransaction().replace(R.id.fragment_holder, fragment).commit();
                break;
            case 1:
                getFragmentManager().beginTransaction().replace(R.id.fragment_holder, new DataReceiverFragment()).commit();
                break;
            case 2:
                getFragmentManager().beginTransaction().replace(R.id.fragment_holder, fragment).commit();
                break;
            default:
                getFragmentManager().beginTransaction().replace(R.id.fragment_holder, fragment).commit();
                break;
        }
        mDrawerList.setItemChecked(id, true);
        setTitle(fragments[id]);
        mDrawerLayout.closeDrawer(mDrawerList);
    }


}
