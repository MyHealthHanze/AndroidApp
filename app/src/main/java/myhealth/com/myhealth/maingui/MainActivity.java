package myhealth.com.myhealth.maingui;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.HashMap;

import myhealth.com.myhealth.R;
import myhealth.com.myhealth.measurements.receiving.DataReceiverFragment;
import myhealth.com.myhealth.measurements.viewing.BPViewerFragment;
import myhealth.com.myhealth.welcome.WelcomeFragment;

/**
 * Created by Sander on 26-9-2015.
 * The Main Activity that holds the possible fragments and builds the Drawer list
 */
public class MainActivity extends AppCompatActivity {

    private String[] fragments;
    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private HashMap<Integer, Fragment> loadedFragments;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        fragments = new String[]{getString(R.string.drawer_view_measurements), getString(R.string.drawer_receive_measurements), "Welcome"};
        createDrawer();
        loadedFragments = new HashMap<>();
        selectFragment(0);
    }

    /**
     * Create the drawer
     */
    private void createDrawer() {
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
        Fragment fragment = loadedFragments.get(id);
        if (fragment == null) {
            fragment = getFragment(id);
            loadedFragments.put(id, fragment);
        }
        Log.d("MyHealth", "Fragment id: " + id);
        getFragmentManager().beginTransaction().replace(R.id.fragment_holder, fragment).addToBackStack(null).commit();
        mDrawerList.setItemChecked(id, true);
        mDrawerLayout.closeDrawer(mDrawerList);
    }

    /**
     * Get a fragment based on id
     *
     * @param id The id
     * @return The fragment
     */
    private Fragment getFragment(int id) {
        switch (id) {
            case 0:
                return new WelcomeFragment();
            case 1:
                return new DataReceiverFragment();
            case 2:
                return new BPViewerFragment();
            default:
                return new WelcomeFragment();
        }
    }

    /**
     * Disables the apps history if the user logs in or changes its password for the first time
     */
    @Override
    public void onBackPressed() {
        Intent i = getIntent();
        // Get the "logged_in" extra from the intent, sets false as the default value
        boolean loggedIn = i.getExtras().getBoolean("logged_in", false);
        // Finish the app (closes app) when the user presses back and comes from login
        if (!loggedIn) {
            super.onBackPressed();
        } else {
            if (getFragmentManager().getBackStackEntryCount() > 0) {
                getFragmentManager().popBackStack();
            }
            return;
        }
    }
}
