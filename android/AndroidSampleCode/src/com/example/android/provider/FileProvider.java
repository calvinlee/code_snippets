
package com.example.android.provider;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.os.Binder;
import android.os.ParcelFileDescriptor;
import android.os.Process;
import android.text.TextUtils;
import android.util.Log;

/**
 * This demo shows how to provide large files via ContentProvider in addition to
 * permission control on this provider.
 * 
 * @see http://stackoverflow.com/questions/3034575/passing-binary-blob-through-an-android-content-provider
 * @author calvin
 */
public class FileProvider extends ContentProvider {
    public static final String TAG = FileProvider.class.getSimpleName();

    public static final String AUTHORITY = "com.example.file.provider";

    public static final String DB_NAME = "files.db";

    public static final String DB_TABLE_NAME = "files";

    public static final int DB_VERSION = 1;

    static final String FILES_PATH = "files";
    
    static final String MIME_PATH = "mime";

    private static final int FILES = 1;

    private static final int FILES_ID = 2;
    
    private static final int MIME = 3;

    private static final UriMatcher sUriMatcher;

    private static final int MY_PID = Process.myPid();

    private static final boolean DEBUG = true;

    private void log(String message) {
        if (DEBUG) {
            Log.d(TAG, message);
        }
    }

    static {
        sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        sUriMatcher.addURI(AUTHORITY, FILES_PATH, FILES);
        sUriMatcher.addURI(AUTHORITY, MIME_PATH, MIME);
        sUriMatcher.addURI(AUTHORITY, FILES_PATH + "/#", FILES_ID);
    }

    /** The database that lies underneath this content provider */
    private SQLiteOpenHelper mOpenHelper = null;

    private final class DatabaseHelper extends SQLiteOpenHelper {
        public DatabaseHelper(final Context context) {
            super(context, DB_NAME, null, DB_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            log("database oncreate");
            db.execSQL("CREATE TABLE " + DB_TABLE_NAME + " (" + NamedFile._ID
                    + " INTEGER PRIMARY KEY," + NamedFile._NAME + " TEXT," + NamedFile._DATA
                    + " TEXT" + ");");
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            log("database onupgrade");
            db.execSQL("DROP TABLE IF EXISTS " + DB_TABLE_NAME);
            onCreate(db);
        }
    }

    @Override
    public boolean onCreate() {
        mOpenHelper = new DatabaseHelper(getContext());
        return true;
    }

    @Override
    public ParcelFileDescriptor openFile(Uri uri, String mode) throws FileNotFoundException {
        log("openFile");
        String path = getFilePath(uri);
        log("opening file,path=" + path);
        if (path == null) {
            throw new FileNotFoundException("No filename found.");
        }

        if (!"r".equals(mode)) {
            throw new FileNotFoundException("Bad mode for " + uri + ": " + mode);
        }

        ParcelFileDescriptor ret = ParcelFileDescriptor.open(new File(path),
                ParcelFileDescriptor.MODE_READ_ONLY);

        if (ret == null) {
            log("couldn't open file");
            throw new FileNotFoundException("couldn't open file");
        }

        return ret;
    }

    private String getFilePath(Uri uri) throws FileNotFoundException {
        Cursor cursor = query(uri, new String[] {
            NamedFile._DATA
        }, null, null, null);
        String path = null;
        try {
            int count = (cursor != null) ? cursor.getCount() : 0;
            if (count != 1) {
                // If there is not exactly one result, throw an appropriate
                // exception.
                if (count == 0) {
                    throw new FileNotFoundException("No entry for " + uri);
                }
                throw new FileNotFoundException("Multiple items at " + uri);
            }

            cursor.moveToFirst();
            path = cursor.getString(0);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }

        return path;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs,
            String sortOrder) {
        if (checkCallingPermission() != PackageManager.PERMISSION_GRANTED) {
            throw new SecurityException(
                    "Only certificated apps are allowed to access this provider");
        }
        SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
        qb.setTables(DB_TABLE_NAME);

        switch (sUriMatcher.match(uri)) {
            case FILES:
                break;
            case FILES_ID:
                qb.appendWhere(NamedFile._ID + "=" + uri.getPathSegments().get(1));
                break;
            case MIME:
                MatrixCursor cursor = new MatrixCursor(new String[] {
                    "supported"
                });

                cursor.addRow(new String[] {
                        "text/plain"
                    });
                cursor.addRow(new String[] {
                        "img/png"
                    });
                cursor.addRow(new String[] {
                        "text/v_card"
                    });
                cursor.setNotificationUri(getContext().getContentResolver(), uri);
                return cursor;
            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }

        SQLiteDatabase db = mOpenHelper.getReadableDatabase();
        Cursor ret = qb.query(db, projection, selection, selectionArgs, null, null, sortOrder);
        if (ret != null) {
            ret.setNotificationUri(getContext().getContentResolver(), uri);
        }
        return ret;
    }

    @Override
    public String getType(Uri uri) {
        // not implement
        return null;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        if (checkCallingPermission() != PackageManager.PERMISSION_GRANTED) {
            throw new SecurityException(
                    "Only certificated apps are allowed to access this provider");
        }

        if (Binder.getCallingPid() != MY_PID) {
            throw new UnsupportedOperationException("Can not insert values into this provider");
        }

        switch (sUriMatcher.match(uri)) {
            case FILES:
                SQLiteDatabase db = mOpenHelper.getWritableDatabase();
                long rowID = db.insert(DB_TABLE_NAME, null, values);
                if (rowID == -1) {
                    log("couldn't insert record into database");
                    return null;
                }
                return ContentUris.withAppendedId(NamedFile.CONTENT_URI, rowID);
            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }
    }

    @Override
    public int delete(Uri uri, String where, String[] whereArgs) {
        if (checkCallingPermission() != PackageManager.PERMISSION_GRANTED) {
            throw new SecurityException(
                    "Only certificated apps are allowed to access this provider");
        }

        int count = 0;
        switch (sUriMatcher.match(uri)) {
            case FILES:
                break;
            case FILES_ID:
                boolean deleted = deleteFileFromStorage(uri);
                if (!deleted) {
                    log("Delete file from storage failed,uri=" + uri);
                }

                SQLiteDatabase db = mOpenHelper.getWritableDatabase();
                String fileId = uri.getPathSegments().get(1);
                count = db.delete(DB_TABLE_NAME,
                        NamedFile._ID + "=" + fileId
                                + (!TextUtils.isEmpty(where) ? " AND (" + where + ')' : ""),
                        whereArgs);
                break;
            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }

        getContext().getContentResolver().notifyChange(uri, null);
        return count;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        // not implement
        return 0;
    }

    private int checkCallingPermission() {
        int pid = Binder.getCallingPid();
        int uid = Binder.getCallingUid();

        if (uid == 0 || uid == Process.SYSTEM_UID || pid == MY_PID) {
            return PackageManager.PERMISSION_GRANTED;
        }

        List<String> callerPkgNames = getCallingPackageName(pid, uid);
        log("callerPid=" + pid + ",package=" + callerPkgNames);
        if (callerPkgNames.contains("com.example.another.package")) {
            return PackageManager.PERMISSION_GRANTED;
        }

        return PackageManager.PERMISSION_DENIED;
    }

    private List<String> getCallingPackageName(int pid, int uid) {
        List<String> pkgNames = new ArrayList<String>();

        ActivityManager am = (ActivityManager) getContext().getSystemService(
                Context.ACTIVITY_SERVICE);
        List<RunningAppProcessInfo> runningAppProcesses = am.getRunningAppProcesses();
        for (RunningAppProcessInfo processInfo : runningAppProcesses) {
            if (processInfo.pid == pid && processInfo.uid == uid) {
                pkgNames = Arrays.asList(processInfo.pkgList);
                break;
            }
        }

        return pkgNames;
    }

    private boolean deleteFileFromStorage(Uri uri) {
        try {
            String path = getFilePath(uri);
            if (!TextUtils.isEmpty(path)) {
                return new File(path).delete();
            }
        } catch (FileNotFoundException e) {
            // ignore
        }
        return false;
    }

}
