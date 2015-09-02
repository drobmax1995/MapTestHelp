package drobmax.com.maptesthelp.database;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.location.LocationProvider;
import android.net.Uri;

import drobmax.com.maptesthelp.models.MarkerModel;

/**
 * Created by Admin on 03.08.2015.
 */
public class TableMarkers {
    public static final String TABLE_NAME = "TableMarkers";
    public static final String ID = "id";
    public static final String NAME = "name";
    public static final String LAT = "lat";
    public static final String LNG = "lng";
    public static final String DESCRIPTION = "description";
    public static final String ON_OF = "on_of";
    public static final Uri CONTENT_URI = LocationContentProvider.BASE_CONTENT_URI.buildUpon().
            appendPath(TABLE_NAME).build();

    public static final String CREATE_TABLE_REQUEST =
            "create table " + TABLE_NAME + " (" +
                    ID + " integer primary key autoincrement, " +
                    NAME + " text, " +
                    LAT + " real, " +
                    LNG + " real, " +
                    DESCRIPTION + " text, " +
                    ON_OF + " integer );";

    public static void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(CREATE_TABLE_REQUEST);
    }

    public static void onUpgrade(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("drop table if exists " + TABLE_NAME);
    }

    public static ContentValues createContentValues(MarkerModel markerModel) {
        ContentValues cv = new ContentValues();
        cv.put(NAME, markerModel.getName());
        cv.put(LAT, markerModel.getLat());
        cv.put(LNG, markerModel.getLng());
        cv.put(DESCRIPTION, markerModel.getDescription());
        cv.put(ON_OF, markerModel.isEnabled() ? 1 : 0);
        return cv;
    }
}
