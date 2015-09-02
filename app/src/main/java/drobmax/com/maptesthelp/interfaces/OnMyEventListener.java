package drobmax.com.maptesthelp.interfaces;

import com.google.android.gms.maps.model.LatLng;

/**
 * Created by Admin on 06.08.2015.
 */
public interface OnMyEventListener {
    public void onAddPressed();
    public void onLocationListenerSeted();
    public void onDataChanged();
    public void onLocationSelected(LatLng latLng);
}
