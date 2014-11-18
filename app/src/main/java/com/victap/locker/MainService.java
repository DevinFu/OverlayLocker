package com.victap.locker;

import android.app.Dialog;
import android.app.Service;
import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

public class MainService extends Service
        implements View.OnClickListener {

    private ComponentName mAdminComponent;

    private DevicePolicyManager mDeviceManager;


    @Override
    public IBinder onBind(Intent intent) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onCreate() {
        super.onCreate();

        mDeviceManager = (DevicePolicyManager) getSystemService(Context.DEVICE_POLICY_SERVICE);
        mAdminComponent = new ComponentName(this.getApplicationContext(), AdminReceiver.class);

        this.showOverlayButton();
    }

    private void showOverlayButton() {
        final Dialog dlg = new Dialog(getApplicationContext(), R.style.Dialog_NoBackground);
        dlg.setContentView(R.layout.window_overlay);

        dlg.setCancelable(false);
        Button button = (Button) dlg.findViewById(R.id.overlayWindow_lockScreen_butn);

        button.setOnClickListener(this);

        int height = this.getResources().getDisplayMetrics().heightPixels;
        int width = this.getResources().getDisplayMetrics().widthPixels;

        Window window = dlg.getWindow();
        WindowManager.LayoutParams attrs = window.getAttributes();
        attrs.flags = attrs.flags | WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH;
        attrs.x = -width / 2;
        attrs.y = height - 96;
        window.setAttributes(attrs);
        window.setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
        window.addFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE);
        dlg.show();
    }

    @Override
    public void onClick(View view) {
        boolean activited = mDeviceManager.isAdminActive(mAdminComponent);
        if (activited) {
            this.lockScreen();
        }
    }

    private void lockScreen() {
        mDeviceManager.lockNow();
    }
}
