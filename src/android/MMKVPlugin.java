package org.apache.cordova.mmkv;

import android.util.Log;

import java.util.HashMap;
import java.util.Map;

import com.tencent.mmkv.MMKV;
import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.CallbackContext;
import org.apache.cordova.PluginResult;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * This class echoes a string called from JavaScript.
 */
public class MMKVPlugin extends CordovaPlugin {

    // private MMKV kv = MMKV.defaultMMKV();// default instance
    private static final Map<String, MMKV> kvMap = new HashMap();
    private boolean isInit = false;
    private JSONObject jObj;
    private String v1,v2,v3;

    @Override
    public boolean execute(String action, JSONArray data, CallbackContext cb) throws JSONException {
        switch (data.length()){
            case 0:
                break;
            case 1:
                v1 = data.getString(0);
                break;
            case 2:
                v1 = data.getString(0);
                v2 = data.getString(1);
                break;
            case 3:
                v1 = data.getString(0);
                v2 = data.getString(1);
                v3 = data.getString(2);
                break;
        }
//      Log.e("MMKV step","init");
        cb.error(generateJsonObj(false,"Must have content."));
        if(action.equals("init")) {
            return (v1.length() >=0) && initMMKV(v1, cb);
        }else if(action.equals("create")){
            return (v1.length() >0) && createMMKV(v1, cb);
        }else if(action.equals("destroy")){
            return destroyMMKV(cb);
        }else if(action.equals("read")) {
            return (v1.length() >0) && (v2.length() >0) && kvRead(v1, v2, cb);
        }else if(action.equals("write")) {
            return (v1.length() >0) && (v2.length() >0) && (v3.length() > 0) && kvWrite(v1, v2, v3,cb);
        }else if(action.equals("erase")) {
            return (v1.length() >0) && (v2.length() >0) && kvErase(v1,v2,cb);
        }else if(action.equals("contain")){
            return (v1.length() >0) && (v2.length() >0) && kvContains(v1,v2, cb);
        }else if(action.equals("fileSize")){
            return (v1.length() >0) && kvFileSize(v1,cb);
        }else if(action.equals("count")){
            return (v1.length() >0) && kvKeyCount(v1,cb);
        }else if(action.equals("valueSize")){
            return (v1.length() >0) && (v2.length() >0) && kvValueSize(v1,v2,cb);
        }else if(action.equals("trim")){
            return (v1.length() >0) && kvTrim(v1, cb);
        }else if(action.equals("close")){
            return (v1.length() >0) &&  kvClose(v1, cb);
        }else if(action.equals("clearAll")){
            return (v1.length() >0) &&  kvClearAll(v1, cb);
        }
        else{
            cb.error(PluginResult.Status.INVALID_ACTION.toString());
        }
        return false;
    }

    private JSONObject generateJsonObj(boolean isSuccess, String result) throws JSONException {
        JSONObject jsonObj = new JSONObject();
        jsonObj.put("isSuccess",isSuccess);
        jsonObj.put("result",result);
        return jsonObj;
    }

    // 初始化mmkv实例,首次运行,必须初始化.
    private boolean initMMKV(String rootDir, CallbackContext cb) {
        try{
            if (!isInit) {
                if(rootDir.length() == 0){
//                    rootDir = MMKV.initialize();
                }else{
                    rootDir = MMKV.initialize(rootDir);
                }
                isInit = true;
                cb.success(generateJsonObj(true, rootDir));
                return true;
            } else {
                cb.error(generateJsonObj(false, "It has been initialized."));
            }
        }catch(JSONException e){
            cb.error(e.getMessage());
//            Log.e(e);
        }
        return false;
    }

    // 创建mmkv实例,可创建多个.
    private boolean createMMKV(String id, CallbackContext cb) throws JSONException{
        // if(!kvMap.containsKey(id)){ //应该是可以重复赋值,所以无需检测
        kvMap.put(id, MMKV.mmkvWithID(id, MMKV.MULTI_PROCESS_MODE));
        cb.success(generateJsonObj(true, "Init MMKV ("+id+") Success."));
        return true;
        // }else{
        // cb.error("Init MMKV (" + id + ") Failed. Already exist same ID.");
        // }
        // return false;
    }

    // 销毁并退出.
    private boolean destroyMMKV(CallbackContext cb) throws JSONException {
        MMKV.onExit();
        isInit = false;
        kvMap.clear();
        cb.success(generateJsonObj(true, "Success"));
        return true;
    }

    // 写入.
    private boolean kvWrite(String id, String k, String v, CallbackContext cb) throws JSONException {
        if (kvMap.containsKey(id)) {
            MMKV mm = kvMap.get(id);
            mm.encode(k, v);
            cb.success(generateJsonObj(true, "Success"));
            return true;
        } else {
            cb.error(generateJsonObj(false, "Not Exist MMKV: " + id));
        }
        return false;
    }

    // 读取.
    private boolean kvRead(String id, String k, CallbackContext cb) throws JSONException {
        if (kvMap.containsKey(id)) {
            MMKV mm = kvMap.get(id);
            cb.success(generateJsonObj(true, mm.decodeString(k)));
            return true;
        } else {
            cb.error(generateJsonObj(false, "Not Exist MMKV: " + id));
        }
        return false;
    }

    // 删除.
    private boolean kvErase(String id, String k, CallbackContext cb) throws JSONException {
        if (kvMap.containsKey(id)) {
            MMKV mm = kvMap.get(id);
            mm.removeValueForKey(k);
            cb.success(generateJsonObj(true, "Success"));
            return true;
        } else {
            cb.error(generateJsonObj(false, "Not Exist MMKV: " + id));
        }
        return false;
    }

    // 是否存在key.
    private boolean kvContains(String id, String k, CallbackContext cb) throws JSONException {
        if (kvMap.containsKey(id)) {
            MMKV mm = kvMap.get(id);
            if (mm.containsKey(k)) {
                cb.success(generateJsonObj(true, "exist"));
                return true;
            } else {
                cb.success(generateJsonObj(false, "Not exist"));
                return true;
            }
        } else {
            cb.error(generateJsonObj(false, "Not exist MMKV: " + id));
        }
        return false;
    }

    // 获取文件大小.
    private boolean kvFileSize(String id, CallbackContext cb) throws JSONException {
        if (kvMap.containsKey(id)) {
            MMKV mm = kvMap.get(id);
            cb.success(generateJsonObj(true, String.valueOf((mm.totalSize()))));
            return true;
        } else {
            cb.error(generateJsonObj(false, "Not exist MMKV: " + id));
        }
        return false;
    }

    // 获取所有k数.
    private boolean kvKeyCount(String id, CallbackContext cb) throws JSONException {
        if (kvMap.containsKey(id)) {
            MMKV mm = kvMap.get(id);
            cb.success(generateJsonObj(true, String.valueOf(mm.count())));
            return true;
        } else {
            cb.error(generateJsonObj(false, "Not exist MMKV: " + id));
        }
        return false;
    }

    // 获取指定key的value的length.
    private boolean kvValueSize(String id, String k, CallbackContext cb) throws JSONException {
        if (kvMap.containsKey(id)) {
            MMKV mm = kvMap.get(id);
            cb.success(generateJsonObj(true, String.valueOf(mm.getValueSize(k))));
            return true;
        } else {
            cb.error(generateJsonObj(false, "Not exist MMKV: " + id));
        }
        return false;
    }

    // 我的理解就是整理并优化文件大小.
    private boolean kvTrim(String id, CallbackContext cb) throws JSONException {
        if (kvMap.containsKey(id)) {
            MMKV mm = kvMap.get(id);
            mm.trim();
            cb.success(generateJsonObj(true, "Success"));
            return true;
        } else {
            cb.error(generateJsonObj(false, "Not exist MMKV: " + id));
        }
        return false;
    }

    // 关闭某个mmkv实例.
    private boolean kvClose(String id, CallbackContext cb) throws JSONException {
        if (kvMap.containsKey(id)) {
            MMKV mm = kvMap.get(id);
            mm.close();
            cb.success(generateJsonObj(true, "Success"));
            return true;
        } else {
            cb.error(generateJsonObj(false, "Not exist MMKV: " + id));
        }
        return false;
    }

    // 清楚所有KV.
    private boolean kvClearAll(String id, CallbackContext cb) throws JSONException {
        if (kvMap.containsKey(id)) {
            MMKV mm = kvMap.get(id);
            mm.clearAll();
            cb.success(generateJsonObj(true, "Success"));
        } else {
            cb.error(generateJsonObj(false, "Not exist MMKV: " + id));
        }
        return false;
    }

}
