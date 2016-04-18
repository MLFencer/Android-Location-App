package net.nofool.dev.tbd;

import android.content.Intent;

import com.google.android.gms.iid.InstanceIDListenerService;


public class MyInstanceIDListenerService extends InstanceIDListenerService {
    @Override
    public void onTokenRefresh(){
        Intent intent = new Intent(this, GcmRegistrationService.class);
        startService(intent);
    }

}
