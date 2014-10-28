package com.aidigame.hisun.pet.util;

import com.aidigame.hisun.pet.R;
import com.aidigame.hisun.pet.ui.NewHomeActivity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

/**
 * 连接网络进行数据交互时，产生的所有Exception，在此处理
 * 设置网络断开广播监听，DeviceNetInfoUtil
 * @author admin
 *
 */
public class HandleHttpConnectionException {
	public static final int Network_Status_Error=1001;//网络连接异常，网络断开
	public static final int Connect_Error_1XX=2001;//http 错误 1xx 信息提示
	public static final int Connect_Error_2XX=2002;//http 错误 2xx 访问成功
	public static final int Connect_Error_3XX=2003;//http 错误 3xx 重定向
	public static final int Connect_Error_4XX=2004;//http 错误 4xx 客户端错误
	public static final int Connect_Error_5XX=2005;//http 错误 5xx 服务器错误
	public static final int Json_Data_Server_Exception=3001;//服务器返回的json数据中，errorMessage
	public static final int Json_Data_Parse_Exception=3002;//json数据解析异常
	public static final int CONNECT_OUT_TIME=4000;//网络连接超时
	public static final int LOGIN_SUCCESS=5000;//登陆成功
//	public static final int 
    private static HandleHttpConnectionException handleHttpConnectionException=new HandleHttpConnectionException();
    Context context;
    public Handler handler=new Handler(){
    	public void handleMessage(android.os.Message msg) {
    		switch (msg.what) {
			case Network_Status_Error:
				
				Toast.makeText(context, "请检查网络连接", Toast.LENGTH_LONG).show();
				break;
			case Connect_Error_1XX:
				Toast.makeText(context, "信息提示", Toast.LENGTH_LONG).show();
				break;
			case Connect_Error_3XX:
				Toast.makeText(context, "重定向", Toast.LENGTH_LONG).show();
				break;
			case Connect_Error_4XX:
				Toast.makeText(context, "客户端错误", Toast.LENGTH_LONG).show();
				break;
			case Connect_Error_5XX:
				Toast toast=new Toast(context);
				View view=LayoutInflater.from(context).inflate(R.layout.widget_toast_one_message, null);
				toast.setView(view);
				toast.setDuration(Toast.LENGTH_LONG);
				toast.show();
//				Toast.makeText(context, "服务端错误", Toast.LENGTH_LONG).show();
				break;
			case Json_Data_Server_Exception:
				String errorMsg=(String)msg.obj;
				Toast.makeText(context, ""+errorMsg, Toast.LENGTH_LONG).show();
//				Toast.makeText(context, "系统错误", Toast.LENGTH_LONG).show();
				break;
			case Json_Data_Parse_Exception:
				Toast.makeText(context, "Json数据解析异常", Toast.LENGTH_LONG).show();
				break;
			case CONNECT_OUT_TIME:
				Toast.makeText(context, "网络连接超时", Toast.LENGTH_LONG).show();
				break;
			case LOGIN_SUCCESS:
				if(NewHomeActivity.homeActivity!=null)NewHomeActivity.homeActivity.update();
				break;
			}
    	};
    };
	private HandleHttpConnectionException(){
		
	}
	public static HandleHttpConnectionException getInstance(){
		return handleHttpConnectionException;
	}
	public Handler getHandler(Context context){
		this.context=context;
		return handler;
	}

}
