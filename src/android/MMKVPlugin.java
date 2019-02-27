package cordova.plugin.mmkv;

import java.security.KeyStore.CallbackHandlerProtection;
import java.util.HashMap;
import java.util.Map;

import com.tencent.mmkv.MMKV;
import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.CallbackContext;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * This class echoes a string called from JavaScript.
 */
public class MMKVPlugin extends CordovaPlugin {

    // System.out.println("mmkv root: " + mmkvDir);
    // private MMKV kv = MMKV.defaultMMKV();//默认实例
    private Map<String, MMKV> kvMap;
    private boolean isInit = false;

    @Override
    public boolean execute(String action, JSONArray data, CallbackContext cb) throws JSONException {
        if(action.equals("init")) {
            return initMMKV(data.getString(0), cb);
        }else if(action.equals("create")){
            return createMMKV(data.getString(0), cb);
        }else if(action.equals("destroy")){
            return destroyMMKV(cb);
        }else if(action.equals("read")) {
            return kvRead(data.getString(0), data.getString(1), cb);
        }else if(action.equals("write")) {
            return kvWrite(data.getString(0), data.getString(1), data.getString(2),cb);
        }else if(action.equals("erase")) {
            return kvErase(data.getString(0),data.getString(1),cb);
        }else if(action.equals("contain")){
            return kvContains(data.getString(0), data.getString(1), cb);
        }else if(action.equals("fileSize")){
            return kvFileSize(data.getString(0),cb);
        }else if(action.equals("count")){
            return kvKeyCount(data.getString(0),cb);
        }else if(action.equals("valueSize")){
            return kvValueSize(data.getString(0), data.getString(1),cb);
        }else if(action.equals("trim")){
            return kvTrim(data.getString(0), cb);
        }else if(action.equals("close")){
            return kvClose(data.getString(0), cb);
        }else if(action.equals("clearAll")){
            return kvClearAll(data.getString(0), cb);
        }
        else{
            cb.error("Unknow action.");
        }
        return false;
    }

    // 初始化mmkv实例,首次运行,必须初始化.
    private boolean initMMKV(String rootDir, CallbackContext cb) {
        if (!isInit) {
            MMKV.initialize(rootDir);
            kvMap = new HashMap();
            isInit = true;
            cb.success("Success");
            return true;
        } else {
            cb.error("It has been initialized.");
        }
        return false;
    }

    // 创建mmkv实例,可创建多个.
    private boolean createMMKV(String id, CallbackContext cb) {
        // if(!kvMap.containsKey(id)){ //应该是可以重复赋值,所以无需检测
        kvMap.put(id, MMKV.mmkvWithID(id, MMKV.MULTI_PROCESS_MODE));
        cb.success("Init MMKV (" + id + ") Success.");
        return true;
        // }else{
        // cb.error("Init MMKV (" + id + ") Failed. Already exist same ID.");
        // }
        // return false;
    }

    // 销毁并退出.
    private boolean destroyMMKV(CallbackContext cb) {
        MMKV.onExit();
        isInit = false;
        kvMap.clear();
        cb.success("Success");
        return true;
    }

    // 写入.
    private boolean kvWrite(String id, String k, String v, CallbackContext cb) {
        if (kvMap.containsKey(id)) {
            MMKV mm = kvMap.get(id);
            mm.encode(k, v);
            cb.success("Success");
            return true;
        } else {
            cb.error("Not Exist MMKV: " + id);
        }
        return false;
    }

    // 读取.
    private boolean kvRead(String id, String k, CallbackContext cb) {
        if (kvMap.containsKey(id)) {
            MMKV mm = kvMap.get(id);
            cb.success(mm.decodeString(k));
            return true;
        } else {
            cb.error("Not Exist MMKV: " + id);
        }
        return false;
    }

    // 删除.
    private boolean kvErase(String id, String k, CallbackContext cb) {
        if (kvMap.containsKey(id)) {
            MMKV mm = kvMap.get(id);
            mm.removeValueForKey(k);
            cb.success("Success");
            return true;
        } else {
            cb.error("Not Exist MMKV: " + id);
        }
        return false;
    }

    // 是否包含key.
    private boolean kvContains(String id, String k, CallbackContext cb) {
        if (kvMap.containsKey(id)) {
            MMKV mm = kvMap.get(id);
            if (mm.containsKey(k)) {
                cb.success("exist");
                return true;
            } else {
                cb.error("Not exist key: " + k);
                return false;
            }
        } else {
            cb.error("Not exist MMKV: " + id);
        }
        return false;
    }

    // 获取文件大小.
    private boolean kvFileSize(String id, CallbackContext cb) {
        if (kvMap.containsKey(id)) {
            MMKV mm = kvMap.get(id);
            cb.success(String.valueOf((mm.totalSize())));
            return true;
        } else {
            cb.error("Not exist MMKV: " + id);
        }
        return false;
    }

    // 获取所有k数.
    private boolean kvKeyCount(String id, CallbackContext cb) {
        if (kvMap.containsKey(id)) {
            MMKV mm = kvMap.get(id);
            cb.success(String.valueOf(mm.count()));
            return true;
        } else {
            cb.error("Not exist MMKV: " + id);
        }
        return false;
    }

    // 获取指定key的value的length.
    private boolean kvValueSize(String id, String k, CallbackContext cb) {
        if (kvMap.containsKey(id)) {
            MMKV mm = kvMap.get(id);
            cb.success(String.valueOf(mm.getValueSize(k)));
            return true;
        } else {
            cb.error("Not exist MMKV: " + id);
        }
        return false;
    }

    // 我的理解就是整理并优化文件大小.
    private boolean kvTrim(String id, CallbackContext cb) {
        if (kvMap.containsKey(id)) {
            MMKV mm = kvMap.get(id);
            mm.trim();
            cb.success("Success");
            return true;
        } else {
            cb.error("Not exist MMKV: " + id);
        }
        return false;
    }

    // 关闭某个mmkv实例.
    private boolean kvClose(String id, CallbackContext cb) {
        if (kvMap.containsKey(id)) {
            MMKV mm = kvMap.get(id);
            mm.close();
            cb.success("Success");
            return true;
        } else {
            cb.error("Not exist MMKV: " + id);
        }
        return false;
    }

    // 清楚所有KV.
    private boolean kvClearAll(String id, CallbackContext cb) {
        if (kvMap.containsKey(id)) {
            MMKV mm = kvMap.get(id);
            mm.clearAll();
            cb.success("Success");
        } else {
            cb.error("Not exist MMKV: " + id);
        }
        return false;
    }

}
