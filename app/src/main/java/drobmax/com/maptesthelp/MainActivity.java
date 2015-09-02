package drobmax.com.maptesthelp;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.LinearLayout;

import drobmax.com.maptesthelp.fragments.NavigationDrawerFragment;
import drobmax.com.maptesthelp.fragments.PagerFragment;
import drobmax.com.maptesthelp.interfaces.OnMyEventListener;
import drobmax.com.maptesthelp.interfaces.OnLocationAddListener;
import drobmax.com.maptesthelp.utils.ListnersHosting;


public class MainActivity extends ActionBarActivity
        implements NavigationDrawerFragment.NavigationDrawerCallbacks {
    private Toolbar toolBar;
    private NavigationDrawerFragment mNavigationDrawerFragment;
    private CharSequence mTitle;
    private PagerFragment pagerFragment;
    private OnMyEventListener onMyEventListener;
    private OnLocationAddListener onLocationAddListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mNavigationDrawerFragment = (NavigationDrawerFragment)
                getSupportFragmentManager().findFragmentById(R.id.navigation_drawer);
        mTitle = getTitle();
        toolBar = (Toolbar) findViewById(R.id.toolBar);
        setSupportActionBar(toolBar);

        mNavigationDrawerFragment.setUp(
                R.id.navigation_drawer,
                (DrawerLayout) findViewById(R.id.drawer_layout));
        mNavigationDrawerFragment.setContainerLayout((LinearLayout)findViewById(R.id.containerLayout));
        onMyEventListener = mNavigationDrawerFragment;
        pagerFragment = PagerFragment.newInstance(onMyEventListener);

//        LocationDataProvider dataProvider = new LocationDataProvider(this);
//        LocationContentProvider contentProvider = new LocationContentProvider();
//        contentProvider.delete(TableMarkers.CONTENT_URI,null,null);
    }

    @Override
    protected void onResume() {
        super.onResume();
        replaceFragment(pagerFragment);
    }

    @Override
    public void onBackPressed() {
        if (getSupportFragmentManager().getBackStackEntryCount() == 1) {
            finish();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public void onNavigationDrawerItemSelected(int position) {

    }

    public void onSectionAttached(int number) {
        switch (number) {
            case 1:
                mTitle = getString(R.string.title_section1);
                break;
            case 2:
                mTitle = getString(R.string.title_section2);
                break;
            case 3:
                mTitle = getString(R.string.title_section3);
                break;
        }
    }

    public void restoreActionBar() {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setTitle(mTitle);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (!mNavigationDrawerFragment.isDrawerOpen()) {
            // Only show items in the action bar relevant to this screen
            // if the drawer is not showing. Otherwise, let the drawer
            // decide what to show in the action bar.
            getMenuInflater().inflate(R.menu.main, menu);
            restoreActionBar();
            return true;
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            if (!mNavigationDrawerFragment.isDrawerOpen()) {
                mNavigationDrawerFragment.openDrawer();
            }

            return true;
        }
        if (id == R.id.actionNew) {
            ListnersHosting.getInstance().getOnNewItemCreatedListener().onStartCreation();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void replaceFragment(final Fragment fragment) {
        try {
            Log.d("myLogs", "tryReplace");
            String backStackName = fragment.getClass().getSimpleName();
            FragmentManager manager = getSupportFragmentManager();
            boolean fragmentPopped = manager.popBackStackImmediate(backStackName, 0);
            if (!fragmentPopped && manager.findFragmentByTag(backStackName) == null) {
                FragmentTransaction fragmentTransaction = manager.beginTransaction();
                fragmentTransaction.replace(R.id.container, fragment, backStackName);
                fragmentTransaction.addToBackStack(backStackName);
                fragmentTransaction.commitAllowingStateLoss();
            }

        } catch (Exception e) {
            e.printStackTrace();
            replaceFragment(fragment);
        }
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        public PlaceholderFragment() {
        }

//        @Override
//        public View onCreateView(LayoutInflater inflater, ViewGroup container,
//                                 Bundle savedInstanceState) {
//            RelativeLayout rootView = (RelativeLayout) inflater.inflate(R.layout.fragment_main,
//                    container, false);
//            return rootView;
//        }

        @Override
        public void onAttach(Activity activity) {
            super.onAttach(activity);
            ((MainActivity) activity).onSectionAttached(
                    getArguments().getInt(ARG_SECTION_NUMBER));

        }
    }

}
