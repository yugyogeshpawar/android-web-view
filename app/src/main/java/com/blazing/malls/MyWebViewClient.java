
package com.blazing.malls;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.util.Log;


public class MyWebViewClient extends WebViewClient {

    @Override
    public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
        String url = request.getUrl().toString();


        if (url.startsWith("https://t.me/") || url.startsWith("tg://")) {
            Log.d("WhatsApp", "openTelegram");
            openTelegram(view.getContext(), url);
            return true; // The URL is handled
        } else if (isUpiPaymentLink(url)) {
           if (!openUpiPaymentApp(view.getContext(), url)) {
               // UPI app is not installed, open in Chrome browser
               openInChromeBrowser(view.getContext(), url);
          }
          //  openUpiPaymentApp(view.getContext(), url);
            return true; // The UPI link is handled
        }else if (url.startsWith("https://api.whatsapp.com/")) {
            openInWhatsApp(view.getContext(), url);
            return true; // The WhatsApp link is handled
        }
        else {
            view.loadUrl(url); // Load other URLs in the WebView
            return true;
        }
    }

    private void openTelegram(Context context, String url) {
        Intent telegramIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        telegramIntent.setPackage("org.telegram.messenger"); // Specify the package name of the Telegram app

        try {
            context.startActivity(telegramIntent); // Open Telegram app
        } catch (ActivityNotFoundException e) {
            // Telegram app is not installed, redirect the user to the Telegram website or show a message
            openTelegramWebsite(context, url);
        }
    }
    private void openInWhatsApp(Context context, String text) {
        Intent whatsappIntent = new Intent(Intent.ACTION_SEND);
        whatsappIntent.setType("text/plain");
        whatsappIntent.setPackage("com.whatsapp"); // Specify the package name of WhatsApp
        whatsappIntent.putExtra(Intent.EXTRA_TEXT, text); // The text you want to share

        try {
            context.startActivity(whatsappIntent); // Open WhatsApp
            // Log success message if WhatsApp was successfully opened

        } catch (ActivityNotFoundException e) {
            // Handle if WhatsApp is not installed or show an error message
            // You can provide a fallback, for example, open WhatsApp Web in a browser
            openWhatsAppWeb(context, text);
        }
    }


    private void openWhatsAppWeb(Context context, String text) {
        // Fallback: Open WhatsApp Web in a browser
        String url = "https://web.whatsapp.com/send?text=" + Uri.encode(text);
        Intent whatsappWebIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));

        try {
            Log.d("WhatsApp", "WhatsApp trying");
            context.startActivity(whatsappWebIntent);
        } catch (ActivityNotFoundException e) {
            // Handle if the user doesn't have a browser app installed or show an error message
        }
    }


    private boolean isUpiPaymentLink(String url) {
        return url.startsWith("upi://") || url.startsWith("phonepe://") || url.startsWith("paytmmp://") || url.startsWith("tez://");
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

    private void openTelegramWebsite(Context context, String url) {
        Intent telegramWebIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        try {
            context.startActivity(telegramWebIntent); // Open Telegram website
        } catch (ActivityNotFoundException e) {
            // Handle if the user doesn't have a browser app installed or show an error message
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

    // Other WebViewClient methods
}






