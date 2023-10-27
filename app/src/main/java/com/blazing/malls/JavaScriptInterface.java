package com.blazing.malls;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.webkit.JavascriptInterface;

public class JavaScriptInterface {
    private Context context;

    JavaScriptInterface(Context context) {
        this.context = context;
    }

    @JavascriptInterface
    public void copyText(String text) {
        ClipboardManager clipboard = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText("Copied Text", text);
        clipboard.setPrimaryClip(clip);
    }

    @JavascriptInterface
    public void requestFileChooserPermission() {
        // Request file chooser permission
        ((MainActivity) context).requestStoragePermission();
    }

    @JavascriptInterface
    public void openFileChooser() {
        // Start an intent to open the file chooser dialog
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("*/*"); // Set the MIME type as per your requirement
        ((MainActivity) context).startActivityForResult(intent, MainActivity.FILECHOOSER_RESULT_CODE);
    }
}

