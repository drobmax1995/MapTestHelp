package drobmax.com.maptesthelp.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.gms.maps.model.LatLng;
import com.melnykov.fab.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

import drobmax.com.maptesthelp.R;
import drobmax.com.maptesthelp.adapters.ListMarkersAdapter;
import drobmax.com.maptesthelp.database.LocationDataProvider;
import drobmax.com.maptesthelp.interfaces.OnMyEventListener;
import drobmax.com.maptesthelp.models.MarkerModel;
import drobmax.com.maptesthelp.utils.ListnersHosting;

/**
 * Created by Admin on 29.07.2015.
 */
public class Fragment2 extends Fragment implements OnMyEventListener {
    static final String ARGUMENT_PAGE_NUMBER = "arg_page_number";
    int pageNumber;
    ListView listView;
    ListMarkersAdapter adapter;
    ArrayList<MarkerModel> markerModels=null;
    public static Fragment2 newInstance(int page) {
        Fragment2 pageFragment = new Fragment2();
        Bundle arguments = new Bundle();
        arguments.putInt(ARGUMENT_PAGE_NUMBER, page);
        pageFragment.setArguments(arguments);
        return pageFragment;
    }
    public Fragment2(){
        ListnersHosting.getInstance().setOnMyEventListener(this);
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        pageNumber = getArguments().getInt(ARGUMENT_PAGE_NUMBER);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_show_locations, null);
        listView = (ListView)view.findViewById(R.id.listView);
        FloatingActionButton fab = (FloatingActionButton)view.findViewById(R.id.fab);
        fab.attachToListView(listView);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ListnersHosting.getInstance().getOnNewItemCreatedListener().onStartCreation();
            }
        });
        listView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

            }
        });
        ListnersHosting.getInstance().getOnMyEventListener().onDataChanged();
        ListnersHosting.getInstance().getOnMyEventListener1().onDataChanged();

        return view;
    }

    @Override
    public void onAddPressed() {

    }

    @Override
    public void onLocationListenerSeted() {

    }

    @Override
    public void onDataChanged() {
        LocationDataProvider dataProvider = new LocationDataProvider(getActivity());
        dataProvider.getMarkers(new LocationDataProvider.LocksRequestCallback() {
            @Override
            public void onLocksLoaded(final List<MarkerModel> markers) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        markerModels = (ArrayList<MarkerModel>) markers;
                        adapter = new ListMarkersAdapter(markers, getActivity());
                        listView.setAdapter(adapter);
                    }
                });
            }
        });


    }

    @Override
    public void onLocationSelected(LatLng latLng) {
        MarkerModel marker;
        for(int i = 0; i < listView.getAdapter().getCount(); i ++){
            marker = ((ListMarkersAdapter) listView.getAdapter()).getItem(i);
            if((marker.getLat()==latLng.latitude)&&(marker.getLng()==latLng.longitude)){
                int position = i;
                if(i<listView.getAdapter().getCount()-3)
                    position = i+3;
                listView.smoothScrollToPosition(position);
                return;
            }
        }
    }
}
