
package com.example.android.provider;

import android.net.Uri;

public class NamedFile {

    public static final Uri CONTENT_URI = Uri.parse("content://" + FileProvider.AUTHORITY + "/"
            + FileProvider.FILES_PATH);

    public static final String _ID = "_id";

    public static final String _DATA = "_data";

    public static final String _NAME = "_name";
}
