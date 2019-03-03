package org.apache.cordova.mmkv;

import android.app.Application;
import android.util.Log;
import com.tencent.mmkv.MMKV;

public class MMKVApplication  extends Application {
    @Override
    public void onCreate(){
        Log.d("Application","onCreate");
        String rootDir = MMKV.initialize(this);
//        String rootDir = MMKV.initialize("/data/0/"); //if you want specify path.
        Log.d("rootDir",rootDir);
        super.onCreate();
    }
}
