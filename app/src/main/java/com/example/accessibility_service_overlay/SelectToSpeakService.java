package com.example.accessibility_service_overlay;

import android.accessibilityservice.AccessibilityService;
import android.accessibilityservice.AccessibilityServiceInfo;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.view.accessibility.AccessibilityEvent;

public class SelectToSpeakService extends AccessibilityService {
    private BroadcastReceiver broadcastReceiver;

    @Override
    protected void onServiceConnected() {
        super.onServiceConnected();
        AccessibilityServiceInfo info = new AccessibilityServiceInfo();
        info.eventTypes = AccessibilityEvent.TYPE_VIEW_TEXT_SELECTION_CHANGED;
        info.feedbackType = AccessibilityServiceInfo.FEEDBACK_GENERIC;
        setServiceInfo(info);

        // Create and register a BroadcastReceiver
        broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                String readingText = intent.getStringExtra("reading_text");
                // Process the reading text here
            }
        };

        IntentFilter intentFilter = new IntentFilter("com.example.thalavali.READING_TEXT");
        registerReceiver(broadcastReceiver, intentFilter);
    }

    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {
        int eventType = event.getEventType();
        if (eventType == AccessibilityEvent.TYPE_VIEW_TEXT_SELECTION_CHANGED) {
            CharSequence selectedText = event.getText().get(0);
            if (selectedText != null && !selectedText.toString().isEmpty()) {
                // Send broadcast with the reading text
                Intent broadcastIntent = new Intent("com.example.thalavali.READING_TEXT");
                broadcastIntent.putExtra("reading_text", selectedText.toString());
                sendBroadcast(broadcastIntent);
            }
        }
    }

    @Override
    public void onInterrupt() {
        // Do nothing
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        // Unregister the BroadcastReceiver
        unregisterReceiver(broadcastReceiver);
    }
}
