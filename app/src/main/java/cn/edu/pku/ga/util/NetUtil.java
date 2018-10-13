package cn.edu.pku.ga.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/*存放公共的工具类——检测网络状态*/
public class NetUtil {
    public static final int NETWORN_NONE = 0;
    public static final int NETWORN_WIFI = 1;
    public static final int NETWORN_MOBILE = 2;

    public static int getNetworkState(Context context) {
        ConnectivityManager conManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkinfo = conManager.getActiveNetworkInfo();
        if (networkinfo == null) {
            return NETWORN_NONE;
        }

        int nType = networkinfo.getType();
        if (nType == ConnectivityManager.TYPE_MOBILE) {
            return NETWORN_MOBILE;
        }else if (nType == ConnectivityManager.TYPE_WIFI){
            return NETWORN_WIFI;
        }
        return NETWORN_NONE;
    }
}
