package drobmax.com.maptesthelp.database;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;

import drobmax.com.maptesthelp.models.MarkerModel;

public class LocationDataProvider {

    private ContentResolver mContentResolver;

    public LocationDataProvider(Context context) {
        mContentResolver = context.getContentResolver();
    }

    public long saveMarker(MarkerModel model) {
        ContentValues cv = TableMarkers.createContentValues(model);
        return saveValues(cv, TableMarkers.CONTENT_URI);
    }

    public synchronized void getMarkers(final LocksRequestCallback callback) {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                final String request = "SELECT * FROM " + TableMarkers.TABLE_NAME;
                Cursor cursor = mContentResolver.query(LocationContentProvider.BASE_CONTENT_URI, null, request, null, null);
                List<MarkerModel> markerModels = new ArrayList<>();
                if (cursor != null) {
                    if (cursor.getCount() > 0) {
                        cursor.moveToFirst();
                        do {
                            String name = getString(cursor, TableMarkers.NAME);
                            long id = getInt(cursor, TableMarkers.ID);
                            String description = getString(cursor, TableMarkers.DESCRIPTION);
                            double lat = getDouble(cursor, TableMarkers.LAT);
                            double lng = getDouble(cursor, TableMarkers.LNG);
                            boolean enabled = getInt(cursor, TableMarkers.ON_OF) == 1 ? true : false;
                            MarkerModel markerModel = new MarkerModel(id, name, lat, lng,
                                    description, enabled);
                            markerModels.add(markerModel);
                        } while (cursor.moveToNext());
                    }
                    cursor.close();
                }
                callback.onLocksLoaded(markerModels);
            }
        });
        thread.start();
    }

    public synchronized void updateMarkers(final MarkerModel markerModel) {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                int update = mContentResolver.update(TableMarkers.CONTENT_URI,
                        TableMarkers.createContentValues(markerModel),
                        TableMarkers.ID + " = '" + markerModel.getId() + "'", null);

                Log.e(this.getClass().getName(), "update count = " + update);
            }
        });
        thread.start();
    }


    public synchronized void deleteMarker(final MarkerModel markerModel) {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                int delete = mContentResolver.delete(TableMarkers.CONTENT_URI,
                        TableMarkers.ID + " = '" + markerModel.getId() + "'", null);
                Log.e(this.getClass().getName(), "update count = " + delete);
            }
        });
        thread.start();
    }

    public static interface LocksRequestCallback {

        public void onLocksLoaded(List<MarkerModel> locks);

    }

    public static interface KyesRequestCallback {

        public void onKyesLoaded(List<MarkerModel> markerModels);

    }

    private long saveValues(ContentValues cv, Uri uri) {
        Uri paymentUri = mContentResolver.insert(uri, cv);
        return ContentUris.parseId(paymentUri);
    }

    private int getInt(Cursor cursor, String columnName) {
        int columnIndex = cursor.getColumnIndex(columnName);
        return columnIndex == -1 ? -1 : cursor.getInt(columnIndex);
    }

    private long getLong(Cursor cursor, String columnName) {
        int columnIndex = cursor.getColumnIndex(columnName);
        return columnIndex == -1 ? -1 : cursor.getLong(columnIndex);
    }

    private double getDouble(Cursor cursor, String columnName) {
        int columnIndex = cursor.getColumnIndex(columnName);
        return columnIndex == -1 ? -1 : cursor.getDouble(columnIndex);
    }

    private boolean getBoolean(Cursor cursor, String columnName) {
        int columnIndex = cursor.getColumnIndex(columnName);
        return columnIndex >= 0 && cursor.getInt(columnIndex) == 1;
    }

    private String getString(Cursor cursor, String columnName) {
        int columnIndex = cursor.getColumnIndex(columnName);
        return columnIndex == -1 ? "" : cursor.getString(columnIndex);
    }

}
