
window.mmkv = {
    exec: function(success,failure,action,args){
        console.info("action: " + action);
        console.info("args: " + args);
        cordova.exec(function(result){
            success(result);
        },function(result){
            failure(result);
        },"MMKVPlugin",action,args);
    },
    //     /**
    //  * init mmkv
    //  * @param {function} success Callback function
    //  * @param {function} failure Callback function
    //  * @param {string} rootDir file save path
    //  */
    // init: function(success,failure,rootDir) {
    //     this.exec(success,failure,"init",[rootDir]);
    // },
    /**
     * create a mmkv instance.
     * @param {function} success Callback function
     * @param {function} failure Callback function
     * @param {string} mmkvId mmkv instance ID
     */
    create: function(success,failure,mmkvId) {
        this.exec(success,failure,"create",[mmkvId]);
    },
    /**
     * close a mmkv instance
     * @param {function} success Callback function
     * @param {function} failure Callback function
     * @param {string} mmkvId mmkv instance ID
     */
    close: function(success,failure,mmkvId) {
        this.exec(success,failure,"close",[mmkvId]);
    },
    /**
     * destroy mmkv.
     * @param {function} success Callback function
     * @param {function} failure Callback function
     */
    destroy: function(success,failure) {
        this.exec(success,failure,"destroy",[]);
    },
    /**
     * read a key's value.
     * @param {function} success Callback function
     * @param {function} failure Callback function
     * @param {string} mmkvId mmkv instance ID
     * @param {string} key the key
     */
    read: function(success,failure,mmkvId,key) {
        this.exec(success,failure,"read",[mmkvId,key]);
    },
    /**
     * write a kv to disk.
     * @param {function} success Callback function
     * @param {function} failure Callback function
     * @param {string} mmkvId mmkv instance ID
     * @param {string} key the key
     * @param {string} value the value
     */
    write: function(success,failure,mmkvId,key,value) {
        this.exec(success,failure,"write",[mmkvId,key,value]);
    },
    /**
     * erase(delete) a kv.
     * @param {function} success Callback function
     * @param {function} failure Callback function
     * @param {string} mmkvId mmkv instance ID
     * @param {string} key the key
     */
    erase: function(success,failure,mmkvId,key) {
        this.exec(success,failure,"erase",[mmkvId,key]);
    },
    /**
     * check mmkv instance is contain k.
     * @param {function} success Callback function
     * @param {function} failure Callback function
     * @param {string} mmkvId mmkv instance ID
     * @param {string} key the key
     */
    contain: function(success,failure,mmkvId,key) {
        this.exec(success,failure,"contain",[mmkvId,key]);
    },
    /**
     * get mmkv file size.
     * @param {function} success Callback function
     * @param {function} failure Callback function
     * @param {string} mmkvId mmkv instance ID
     */
    fileSize: function(success,failure,mmkvId) {
        this.exec(success,failure,"fileSize",[mmkvId]);
    },
    /**
     * get keys number from a mmkv instance.
     * @param {function} success Callback function
     * @param {function} failure Callback function
     * @param {string} mmkvId mmkv instance ID
     */
    count: function(success,failure,mmkvId) {
        this.exec(success,failure,"count",[mmkvId]);
    },
    /**
     * get value length/size by key with mmkv instance
     * @param {function} success Callback function
     * @param {function} failure Callback function
     * @param {string} mmkvId mmkv instance ID
     * @param {string} key the key
     */
    valueSize: function(success,failure,mmkvId,key) {
        this.exec(success,failure,"valueSize",[mmkvId,key]);
    },
    /**
     * trim a mmkv instance.
     * @param {function} success Callback function
     * @param {function} failure Callback function
     * @param {string} mmkvId mmkv instance ID
     */
    trim: function(success,failure,mmkvId) {
        this.exec(success,failure,"trim",[mmkvId]);
    },
    /**
     * clear all kv.
     * @param {function} success Callback function
     * @param {function} failure Callback function
     * @param {string} mmkvId mmkv instance ID
     */
    clearAll: function(success,failure,mmkvId) {
        this.exec(success,failure,"clearAll",[mmkvId]);
    }
}
module.exports = mmkv;