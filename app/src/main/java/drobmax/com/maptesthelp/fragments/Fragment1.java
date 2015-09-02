package drobmax.com.maptesthelp.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.maps.model.LatLng;

import drobmax.com.maptesthelp.R;
import drobmax.com.maptesthelp.database.LocationDataProvider;
import drobmax.com.maptesthelp.interfaces.OnDataEditingListner;
import drobmax.com.maptesthelp.interfaces.OnMyEventListener;
import drobmax.com.maptesthelp.interfaces.OnLocationAddListener;
import drobmax.com.maptesthelp.interfaces.OnNewItemCreatedListener;
import drobmax.com.maptesthelp.models.MarkerModel;
import drobmax.com.maptesthelp.utils.ListnersHosting;

/**
 * Created by Admin on 29.07.2015.
 */
public class Fragment1 extends Fragment implements OnLocationAddListener, OnDataEditingListner,
        OnNewItemCreatedListener {
    @Nullable
    static final String ARGUMENT_PAGE_NUMBER = "arg_page_number";
    Button btnAdd, btnSave;
    EditText edtTitle;
    EditText edtDescription;
    TextView tvInfo;
    LatLng currentLatLng;
    OnMyEventListener onMyEventListener;
    int pageNumber;
    int actionId;
    static Fragment1 pageFragment;
    long currentId;

    public static Fragment1 newInstance(int page, OnMyEventListener onMyEventListener) {

        pageFragment = new Fragment1();
        pageFragment.onMyEventListener = onMyEventListener;
        pageFragment.onMyEventListener.onLocationListenerSeted();
        Bundle arguments = new Bundle();


        arguments.putInt(ARGUMENT_PAGE_NUMBER, page);
        pageFragment.setArguments(arguments);
        return pageFragment;
    }

    public Fragment1() {
        ListnersHosting.getInstance().setOnLocationAddListener(this);
        ListnersHosting.getInstance().setOnDataEditingListner(this);
        ListnersHosting.getInstance().setOnNewItemCreatedListener(this);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        pageNumber = getArguments().getInt(ARGUMENT_PAGE_NUMBER);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_edit_location, null);
        edtTitle = (EditText) view.findViewById(R.id.edtTitle);
        edtDescription = (EditText) view.findViewById(R.id.edtDescryption);
        btnAdd = (Button) view.findViewById(R.id.btnAdd);
        btnSave = (Button) view.findViewById(R.id.btnSave);
        tvInfo = (TextView) view.findViewById(R.id.tvInfo);
        btnSave.setEnabled(false);
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onMyEventListener.onAddPressed();
            }
        });
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnSave.setEnabled(false);
                switch (actionId) {
                    case 0:
                        saveMarker();
                        break;
                    case 1:
                        updateMarker();
                        actionId = 0;
                        break;
                }
                ListnersHosting.getInstance().getOnReplaceFragmentListener().onReplaceFragment(1);
            }
        });

        return view;
    }

    private MarkerModel getCurrentItem() {
        String title = edtTitle.getText().toString();
        String description = edtDescription.getText().toString();
        double lat = 0;
        double lng = 0;
        if (currentLatLng != null) {
            lat = currentLatLng.latitude;
            lng = currentLatLng.longitude;
        }
        MarkerModel marker = new MarkerModel(currentId, title, lat, lng, description, true);
        return marker;
    }

    private void saveMarker() {
        LocationDataProvider dataProvider = new LocationDataProvider(getActivity());
        dataProvider.saveMarker(getCurrentItem());
        notifiAboutDataChanging();
    }

    private void deleteMarker() {
        LocationDataProvider dataProvider = new LocationDataProvider(getActivity());
        dataProvider.deleteMarker(getCurrentItem());
        notifiAboutDataChanging();
    }

    private void updateMarker() {
        LocationDataProvider dataProvider = new LocationDataProvider(getActivity());
        dataProvider.updateMarkers(getCurrentItem());
        notifiAboutDataChanging();
    }

    @Override
    public void onLocationAdded(LatLng latLng) {
        actionId = 0;
        btnSave.setEnabled(true);
        currentLatLng = latLng;
        tvInfo.setText("latitude: " + latLng.latitude + "\nlongitude: " + latLng.longitude);
        ListnersHosting.getInstance().getOnReplaceFragmentListener().onReplaceFragment(0);
    }

    @Override
    public void onEditData(long id, String title, String description, LatLng latLng) {
        currentLatLng = latLng;
        btnSave.setEnabled(true);
        tvInfo.setText("latitude: " + latLng.latitude + "\nlongitude: " + latLng.longitude);
        edtTitle.setText(title);
        edtDescription.setText(description);
        actionId = 1;
        currentId = id;
    }

    @Override
    public void onDeleteData(long id) {
        actionId = 2;
        currentId = id;
        deleteMarker();
    }

    public void notifiAboutDataChanging() {
        onMyEventListener.onDataChanged();
        ListnersHosting.getInstance().getOnMyEventListener().onDataChanged();
    }

    @Override
    public void onStartCreation() {
        edtDescription.setText("");
        edtTitle.setText("");
        btnSave.setEnabled(false);
        tvInfo.setText("");
        ListnersHosting.getInstance().getOnReplaceFragmentListener().onReplaceFragment(0);
    }
}
