package com.victap.locker;

import android.app.Activity;
import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.CheckBox;
import android.widget.CompoundButton;


public class MainActivity extends Activity
        implements CompoundButton.OnCheckedChangeListener {

    private ComponentName mAdminComponent;

    private DevicePolicyManager mDeviceManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.startService(new Intent(this, MainService.class));

        mDeviceManager = (DevicePolicyManager) getSystemService(Context.DEVICE_POLICY_SERVICE);
        mAdminComponent = new ComponentName(this.getApplicationContext(), AdminReceiver.class);

        CheckBox toggleBox = (CheckBox) this.findViewById(R.id.main_deviceManagerToggle_cb);
        boolean activated = mDeviceManager.isAdminActive(mAdminComponent);
        toggleBox.setChecked(activated);
        toggleBox.setOnCheckedChangeListener(this);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {


        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean checked) {
        switch (compoundButton.getId()) {
            case R.id.main_deviceManagerToggle_cb:
                if (checked) {
                    this.activeDeviceManager();
                } else {
                    this.removeDeviceManager();
                }
            default:
                break;
        }
    }

    private void activeDeviceManager() {
        Intent intent = new Intent(DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN);
        intent.putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN, mAdminComponent);
        intent.putExtra(DevicePolicyManager.EXTRA_ADD_EXPLANATION, "Other words");
        startActivity(intent);
    }

    private void removeDeviceManager() {
        mDeviceManager.removeActiveAdmin(mAdminComponent);
    }
}
