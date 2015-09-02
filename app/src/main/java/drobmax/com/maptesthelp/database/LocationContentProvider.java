package drobmax.com.maptesthelp.database;

import java.util.ArrayList;

import android.content.ContentProvider;
import android.content.ContentProviderOperation;
import android.content.ContentProviderResult;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.OperationApplicationException;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

public class LocationContentProvider extends ContentProvider {

	public static final String AUTHORITY = "drobmax.com.maptesthelp.database";
	public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + AUTHORITY);

	private DataBaseSqlHelper mOpenHelper;
	private final UriMatcher mUriMatcher = buildUriMatcher();

	private static final int MARKERS = 1;


	@Override
	public boolean onCreate() {
		mOpenHelper = new DataBaseSqlHelper(getContext());
		return false;
	}

	@Override
	public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
		final SQLiteDatabase db = mOpenHelper.getReadableDatabase();
		return db.rawQuery(selection, null);
	}

	@Override
	public String getType(Uri uri) {
		final int match = mUriMatcher.match(uri);
		return getTableName(match);
	}

	private UriMatcher buildUriMatcher() {
		final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);

		matcher.addURI(AUTHORITY, TableMarkers.TABLE_NAME, MARKERS);

		return matcher;
	}

	@Override
	public synchronized ContentProviderResult[] applyBatch(ArrayList<ContentProviderOperation> operations)
			throws OperationApplicationException {
		ContentProviderResult[] contentProviderResults = super.applyBatch(operations);
		return contentProviderResults;
	}

	@Override
	public synchronized Uri insert(Uri uri, ContentValues contentValues) {
		final String tableName = getTableName(mUriMatcher.match(uri));
		if (!tableName.isEmpty() && contentValues != null) {
			final SQLiteDatabase sqlDB = mOpenHelper.getWritableDatabase();
			final long id = sqlDB.insert(tableName, null, contentValues);

			final Uri contentUri = ContentUris.withAppendedId(uri, id);
			getContext().getContentResolver().notifyChange(contentUri, null);
			return contentUri;
		} else {
			throw new IllegalArgumentException("Unknown URI: " + uri);
		}
	}

	@Override
	public int delete(Uri uri, String selection, String[] selectionArgs) {
		final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
		final SelectionBuilder builder = buildSimpleSelection(uri);
		int retVal = builder.where(selection, selectionArgs).delete(db);
		getContext().getContentResolver().notifyChange(uri, null);
		return retVal;
	}

	private SelectionBuilder buildSimpleSelection(Uri uri) {
		final SelectionBuilder builder = new SelectionBuilder();
		final int match = mUriMatcher.match(uri);
		return builder.table(getTableName(match));
	}

	@Override
	public int update(Uri uri, ContentValues contentValues, String whereClause, String[] selectionArgs) {
		final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
		final String tableName = getTableName(mUriMatcher.match(uri));
		return db.update(tableName, contentValues, whereClause, selectionArgs);
	} 

	private String getTableName(int uriType) {
		switch (uriType) {

		case MARKERS:
			return TableMarkers.TABLE_NAME;

		default:
			return null;
		}
	}

}
