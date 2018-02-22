package com.abmiues.push;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;


public class PushReciver extends Service {
    public PushReciver() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
