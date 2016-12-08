package com.thomas.easylink;
import android.content.Context;
import com.mxchip.easylink.EasyLinkAPI;
import com.mxchip.wifiman.EasyLinkWifiManager;
import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaPlugin;
import org.json.JSONArray;
import org.json.JSONException;

/**
 * Created by Thomas.Wang on 16/11/11.
 */
public class EasyLink extends CordovaPlugin{
    private EasyLinkAPI elapi;
    private Context ctx = null;
    private EasyLinkWifiManager mWifiManager = null;

    @Override
    public boolean execute(String action, JSONArray args, CallbackContext callbackContext) throws JSONException {
        ctx = cordova.getActivity();
        elapi = new EasyLinkAPI(ctx);

        //获取wifiSSid
        if (action.equals("getWifiSSid")){
            mWifiManager = new EasyLinkWifiManager(ctx);
            callbackContext.success(mWifiManager.getCurrentSSID());
            return true;
        }

        //开始搜索
        if (action.equals("startSearch")) {
            startSearch(args,callbackContext);
            return true;
        }

        //停止搜索
        if(action.equals("stopSearch")){
            elapi.stopEasyLink();
            callbackContext.success("停止配网");
            return  true;
        }
        return false;
    }

    private void startSearch(JSONArray args,CallbackContext callbackContext) throws JSONException{
        String wifiSSid = args.getString(0).trim();
        String wifiPsw = args.getString(1).trim();

        if (wifiSSid==null||wifiSSid.isEmpty()){
            callbackContext.error("请输入wifiSSid");

        }else
        if (wifiPsw==null||wifiPsw.isEmpty()) {
            callbackContext.error("请输入wifiPsw");
        }
        else {
            elapi.startEasyLink(ctx, wifiSSid,wifiPsw);
            callbackContext.success("开始配网");
        }

    }

}
