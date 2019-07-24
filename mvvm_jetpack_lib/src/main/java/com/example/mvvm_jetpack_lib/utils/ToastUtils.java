package com.example.mvvm_jetpack_lib.utils;

import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.widget.Toast;
import com.example.mvvm_jetpack_lib.utils.exception.ExceptionHandle;


/**
 * Description:
 * Data：2018/10/25-14:54
 * Author: cwh
 */
public class ToastUtils {
    private static Toast mToast = null;
    private static long mLastShowTime = 0;
    private static String mLastMessage = "";

    private ToastUtils() {
    }

    /**
     * show Toast
     *
     * @param context
     * @param msg
     */
    public static void showToast(Context context, String msg) {
        if (mToast == null) {
            mToast = Toast.makeText(context.getApplicationContext(), msg, Toast.LENGTH_SHORT);
            mToast.setGravity(Gravity.CENTER, 0, 0);
            mToast.show();
            mLastShowTime = System.currentTimeMillis();
            mLastMessage = msg;
        } else {
            if (mLastMessage.equals(msg)) {
                if ((System.currentTimeMillis() - mLastShowTime) > Toast.LENGTH_SHORT) {
                    mToast.setText(msg);
                    mToast.show();
                    mLastShowTime = System.currentTimeMillis();
                } else {
                    mToast.setText(msg);
                    mToast.show();
                }
            } else {
                mLastMessage = msg;
                mToast.setText(msg);
                mToast.show();
                mLastShowTime = System.currentTimeMillis();
            }
        }
    }

    /**
     * Toasts弹文字和弹View不能混用
     */
    public static void initToast(Context context, View v, int duration) {
        Toast toast = new Toast(context);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.setDuration(duration);
        toast.setView(v);
        toast.show();
    }


    public static void showToast(Context context, int id) {
        showToast(context, context.getString(id));
    }

    public static void showToast(Context context, ExceptionHandle.ResponeThrowable responseThrowable) {
        int code = responseThrowable.code;
        if (code == ExceptionHandle.ERROR.UNKONW_HOST_EXCEPTION) {
            showToast(context, "请检查网络是否连接");
        } else if (code == ExceptionHandle.ERROR.NETWORD_ERROR || code == ExceptionHandle.ERROR.SERVER_ADDRESS_ERROR) {
            showToast(context, "无法连接到服务器");
        } else if (code == ExceptionHandle.ERROR.PARSE_ERROR) {
            showToast(context, "解析数据出现错误");
        } else if (code == ExceptionHandle.ERROR.HTTP_ERROR) {
            showToast(context, "未连接到服务器");
        } else {
            showToast(context, "服务器开小差了，请稍候重试  " + code);
        }
    }


}
