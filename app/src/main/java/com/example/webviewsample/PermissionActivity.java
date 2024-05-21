package com.example.webviewsample;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;

import androidx.core.app.ActivityCompat;

import java.util.ArrayList;

public class PermissionActivity extends Activity {

    private final int PERMISSION_ALL = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        checkAndRequestAllPermissions();
    }

    public void checkAndRequestAllPermissions(){
        ArrayList<String> permissionsToValidate = new ArrayList<>();
        permissionsToValidate.add(Manifest.permission.CAMERA);
        permissionsToValidate.add(Manifest.permission.RECORD_AUDIO);

        String[] PERMISSIONS = permissionsToValidate.toArray(new String[0]);

        if(!hasPermissions(this, PERMISSIONS)){
            ActivityCompat.requestPermissions(this, PERMISSIONS, PERMISSION_ALL);
        } else {
            startDemo();
        }
    }

    public static boolean hasPermissions(Context context, String... permissions) {
        if (context != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_ALL: {
                if (grantResults.length > 0) {
                    boolean allPermissionsGranted = true;
                    for(int i=0; i < grantResults.length && allPermissionsGranted; i++){
                        allPermissionsGranted = (grantResults[i] == PackageManager.PERMISSION_GRANTED);
                    }
                    if(allPermissionsGranted){
                        startDemo();
                    } else {
                        finishAndRemoveTask();
                    }
                }
                return;
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    private void startDemo(){
        final Activity ctx = this;
        Intent intent = new Intent(ctx, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setData(ctx.getIntent().getData());

        new Thread(() -> {
            ctx.startActivity(intent);
            ctx.finish();
        }).start();
    }
}