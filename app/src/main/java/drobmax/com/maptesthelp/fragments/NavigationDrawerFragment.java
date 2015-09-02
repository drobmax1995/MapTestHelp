package drobmax.com.maptesthelp.fragments;

import android.support.v7.app.ActionBarActivity;
import android.app.Activity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBarDrawerToggle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.List;

import drobmax.com.maptesthelp.R;
import drobmax.com.maptesthelp.database.LocationDataProvider;
import drobmax.com.maptesthelp.interfaces.OnDrawerOpenListner;
import drobmax.com.maptesthelp.interfaces.OnMyEventListener;
import drobmax.com.maptesthelp.interfaces.OnLocationAddListener;
import drobmax.com.maptesthelp.models.MarkerModel;
import drobmax.com.maptesthelp.utils.ListnersHosting;

public class NavigationDrawerFragment extends Fragment implements OnMyEventListener,
        OnDrawerOpenListner {
    private static final String STATE_SELECTED_POSITION = "selected_navigation_drawer_position";
    private static final String PREF_USER_LEARNED_DRAWER = "navigation_drawer_learned";
    private NavigationDrawerCallbacks mCallbacks;
    private DrawerLayout mDrawerLayout;
    private View mFragmentContainerView;
    private RelativeLayout mRelativeLayout;
    private int mCurrentSelectedPosition = 0;
    private boolean mFromSavedInstanceState;
    private boolean mUserLearnedDrawer;
    private LinearLayout containerLayout;

    public void setContainerLayout(LinearLayout containerLayout) {
        this.containerLayout = containerLayout;
    }

    private OnLocationAddListener onLocationAddListener;
    private ImageButton btnClose;
    public NavigationDrawerFragment() {
        ListnersHosting.getInstance().setOnMyEventListener1(this);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ListnersHosting.getInstance().setOnDrawerOpenListner(this);
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getActivity());
        mUserLearnedDrawer = sp.getBoolean(PREF_USER_LEARNED_DRAWER, false);
        if (savedInstanceState != null) {
            mCurrentSelectedPosition = savedInstanceState.getInt(STATE_SELECTED_POSITION);
            mFromSavedInstanceState = true;
        }
        selectItem(mCurrentSelectedPosition);
    }
    private ActionBarDrawerToggle toggle;
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setHasOptionsMenu(true);
    }

    GoogleMap map, curentMap;
    SupportMapFragment smFragment;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mRelativeLayout = (RelativeLayout) inflater.inflate(
                R.layout.fragment_navigation_drawer, container, false);
        smFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.myMap);
        map = smFragment.getMap();
        map.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
//                map.addMarker(new MarkerOptions().
//                        position(latLng).title("myFirstPoint"));
                onLocationAddListener.onLocationAdded(latLng);
                mDrawerLayout.closeDrawer(mFragmentContainerView);

            }
        });
        map.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick(Marker marker) {
                ListnersHosting.getInstance().getOnMyEventListener().onLocationSelected(marker.getPosition());
                mDrawerLayout.closeDrawer(mFragmentContainerView);
                mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
            }
        });
        btnClose = (ImageButton) mRelativeLayout.findViewById(R.id.imgBtnClose);
        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDrawerLayout.closeDrawer(mFragmentContainerView);
                mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
            }
        });
        curentMap = map;
        return mRelativeLayout;
    }

    public boolean isDrawerOpen() {
        return mDrawerLayout != null && mDrawerLayout.isDrawerOpen(mFragmentContainerView);
    }

    public void openDrawer() {
        mDrawerLayout.openDrawer(GravityCompat.END);
    }


    /**
     * Users of this fragment must call this method to set up the navigation drawer interactions.
     *
     * @param fragmentId   The android:id of this fragment in its activity's layout.
     * @param drawerLayout The DrawerLayout containing this fragment's UI.
     */

    public void setUp(int fragmentId, DrawerLayout drawerLayout) {
        mFragmentContainerView = getActivity().findViewById(fragmentId);
        mDrawerLayout = drawerLayout;


        // set a custom shadow that overlays the main content when the drawer opens
        mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START);
        // set up the drawer's list view with items and click listener

        ActionBar actionBar = getActionBar();
        actionBar.setDisplayHomeAsUpEnabled(false);
        actionBar.setHomeButtonEnabled(false);


        // If the user hasn't 'learned' about the drawer, open it to introduce them to the drawer,
        // per the navigation drawer design guidelines.
        if (!mUserLearnedDrawer && !mFromSavedInstanceState) {
            mDrawerLayout.openDrawer(mFragmentContainerView);
        }

        // Defer code dependent on restoration of previous instance state.
        toggle = new ActionBarDrawerToggle(getActivity(),mDrawerLayout,R.string.navigation_drawer_open,
                R.string.navigation_drawer_close){
            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                super.onDrawerSlide(drawerView, slideOffset);
                float moveFactor = (mDrawerLayout.getWidth()*slideOffset/2);
                containerLayout.setTranslationY(moveFactor);
                containerLayout.setTranslationX(-moveFactor);
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                ListnersHosting.getInstance().getOnDrawerOpenListner().onDrawerOpen();
            }
        };
        mDrawerLayout.post(new Runnable() {
            @Override
            public void run() {
                toggle.syncState();
            }
        });

        mDrawerLayout.setDrawerListener(toggle);
    }

    private void selectItem(int position) {
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mCallbacks = (NavigationDrawerCallbacks) activity;
            if(isDrawerOpen()){
                mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_OPEN);
            }
        } catch (ClassCastException e) {
            throw new ClassCastException("Activity must implement NavigationDrawerCallbacks.");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mCallbacks = null;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(STATE_SELECTED_POSITION, mCurrentSelectedPosition);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // Forward the new configuration the drawer toggle component.
    }

    private ActionBar getActionBar() {
        return ((ActionBarActivity) getActivity()).getSupportActionBar();
    }

    @Override
    public void onAddPressed() {
        mDrawerLayout.openDrawer(mFragmentContainerView);
    }

    @Override
    public void onLocationListenerSeted() {
        onLocationAddListener = ListnersHosting.getInstance().getOnLocationAddListener();
    }

    @Override
    public void onDataChanged() {
        curentMap.clear();
        LocationDataProvider dataProvider = new LocationDataProvider(getActivity());
        dataProvider.getMarkers(new LocationDataProvider.LocksRequestCallback() {
            @Override
            public void onLocksLoaded(final List<MarkerModel> markers) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        for (MarkerModel marker:markers){
                            LatLng latLng = new LatLng(marker.getLat(),marker.getLng());
                            curentMap.addMarker(new MarkerOptions().position(latLng).title(marker.getName()));
                        }
                    }
                });
            }
        });
    }

    @Override
    public void onLocationSelected(LatLng latLng) {
        mDrawerLayout.openDrawer(mFragmentContainerView);
        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(latLng)
                .zoom(10)
                .bearing(45)
                .tilt(20)
                .build();
        CameraUpdate cameraUpdate = CameraUpdateFactory.newCameraPosition(cameraPosition);
        map.animateCamera(cameraUpdate);
    }

    @Override
    public void onDrawerOpen() {
        mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_OPEN);
    }

    /**
     * Callbacks interface that all activities using this fragment must implement.
     */
    public static interface NavigationDrawerCallbacks {
        /**
         * Called when an item in the navigation drawer is selected.
         */
        void onNavigationDrawerItemSelected(int position);
    }
}
