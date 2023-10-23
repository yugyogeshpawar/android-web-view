package com.blazing.malls;

import android.Manifest;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import android.content.pm.PackageManager;


public class MainActivity extends AppCompatActivity {

    WebView mWebView;
    SwipeRefreshLayout swipeRefreshLayout;
    public static final int FILECHOOSER_RESULT_CODE = 1;
    private static final int STORAGE_PERMISSION_CODE = 123; // You can choose any value


    public void requestStoragePermission() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            // If permission is not granted, request it
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    STORAGE_PERMISSION_CODE);
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == FILECHOOSER_RESULT_CODE && resultCode == RESULT_OK) {
            // Get the selected file URI
            Uri uri = data.getData();
            // Handle the selected file URI as per your requirement
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        requestStoragePermission();

        mWebView = findViewById(R.id.webview);
        swipeRefreshLayout = findViewById(R.id.swipeRefreshLayout);


        // Configure WebView settings
        WebSettings webSettings = mWebView.getSettings();
        webSettings.setJavaScriptEnabled(true); // Enable JavaScript
        webSettings.setGeolocationEnabled(true); // Enable Geolocation
        mWebView.addJavascriptInterface(new JavaScriptInterface(this), "Android");

        // Load the initial URL
        mWebView.loadUrl("https://www.blazingmalls.com");
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // Refresh the WebView content
                mWebView.reload();
            }
        });
    }

    public class myWebViewClient extends WebViewClient {

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            if (url.startsWith("mailto:")) {
                // Handle email links
                Intent intent = new Intent(Intent.ACTION_SENDTO);
                intent.setData(Uri.parse(url));
                try {
                    startActivity(intent);
                } catch (ActivityNotFoundException e) {
                    // Handle if no email client is installed
                    // You can show an error message or prompt the user to install an email client
                }
                return true; // URL handled by this WebViewClient for email links
            } else if (url.startsWith("https://t.me/") || url.startsWith("tg://")) {
                // Handle Telegram links
                Intent telegramIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                telegramIntent.setPackage("org.telegram.messenger"); // Specify the package name of the Telegram app
                try {
                    startActivity(telegramIntent);
                } catch (ActivityNotFoundException e) {
                    // Handle if Telegram app is not installed
                    // You can redirect the user to the Telegram website or prompt them to install the app
                }
                return true; // URL handled by this WebViewClient for Telegram links
            } else if (url.startsWith("https://wa.me/") || url.startsWith("whatsapp://")) {
                openWhatsApp(view.getContext(), url);
                return true; // The WhatsApp link is handled
            } else if (isUpiPaymentLink(url)) {
                if (!openUpiPaymentApp(view.getContext(), url)) {
                    // UPI app is not installed, open in Chrome browser
                    openInChromeBrowser(view.getContext(), url);
                }
                //  openUpiPaymentApp(view.getContext(), url);
                return true; // The UPI link is handled
            }else {
                // Load other URLs in the WebView
                view.loadUrl(url);
                return true; // Other URLs handled by this WebViewClient
            }
        }
        private void openInChromeBrowser(Context context, String url) {
            Uri webpage = Uri.parse(url);
            Intent intent = new Intent(Intent.ACTION_VIEW, webpage);
            intent.setPackage("com.android.chrome"); // Specify the package name of Chrome browser

            try {
                context.startActivity(intent); // Open in Chrome browser
            } catch (ActivityNotFoundException e) {
                // Handle if Chrome browser is not installed or show an error message
            }
        }


        private boolean openUpiPaymentApp(Context context, String url) {
            Intent upiIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));

            try {
                context.startActivity(upiIntent); // Open UPI payment app
                return true;
            } catch (ActivityNotFoundException e) {
                // UPI payment app is not installed
                return false;
            }
        }

        private boolean isUpiPaymentLink(String url) {
            return url.startsWith("upi://") || url.startsWith("phonepe://") || url.startsWith("paytmmp://") || url.startsWith("tez://");
        }

        private void openWhatsApp(Context context, String url) {
            Intent whatsappIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));

            try {
                context.startActivity(whatsappIntent); // Open WhatsApp
            } catch (ActivityNotFoundException e) {
                // Handle if WhatsApp is not installed or show an error message
                Log.e("WhatsApp", "WhatsApp is not installed.");
            }
        }


        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
            // Handle onPageStarted event if needed
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            // Handle onPageFinished event if needed
            swipeRefreshLayout.setRefreshing(false);
        }

        // Override other methods as needed
    }

    @Override
    public void onBackPressed() {
        if (mWebView.canGoBack()) {
            mWebView.goBack();
        } else {
            super.onBackPressed();
        }
    }
}
