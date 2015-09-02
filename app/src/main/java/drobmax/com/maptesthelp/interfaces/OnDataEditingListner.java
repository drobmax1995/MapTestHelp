package drobmax.com.maptesthelp.interfaces;

import com.google.android.gms.maps.model.LatLng;

/**
 * Created by Admin on 07.08.2015.
 */
public interface OnDataEditingListner {
    public void onEditData(long id, String title, String description, LatLng latLng);
    public void onDeleteData(long id);
}
