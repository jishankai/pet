package com.aidigame.hisun.imengstar.util;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;

import org.apache.http.conn.util.InetAddressUtils;

import com.aidigame.hisun.imengstar.constant.Constants;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.widget.Toast;
/**
 * 
 * @author shichunxiang
 * 1.判断手机联网状态
 * 2.获取手机IP地址
 * 3.获取手机MAC地址ַ
 * 
 * 需要权限
 *
 */
public class DeviceNetInfoUtil {
	public static final int NET_CONNECTED_LOGIN_IN=11;
	/**
	 * 注册广播接受者，当网络连接状态发生改变时，需要处理的事项
	 * IntentFilter filter=new IntentFilter();
	 * filter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
	 * context.registerReceiver(receiver);
	 * 
	 * 使用完毕，注销接受者
	 * if(receiver!=null){
	 *    context.unregisterReceiver(receiver);
	 * }
	 */
	public static class NetworkStatusChangeReceiver extends BroadcastReceiver{
		public void onReceive(Context context, android.content.Intent intent) {
			ConnectivityManager cm=(ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
			NetworkInfo wifiInfo=cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
			NetworkInfo mobileInfo=cm.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
			//注意wifiInfo与mobileInfo可能为空
			if(mobileInfo!=null&&mobileInfo.isConnected()){
				//连接网络
				Constants.netStatus=true;
//				Toast.makeText(context, "手机移动网络已连接", Toast.LENGTH_LONG).show();
			}else if(wifiInfo!=null&&wifiInfo.isConnected()){
				//连接网络
//				Toast.makeText(context, "wifi网络已连接", Toast.LENGTH_LONG).show();
				Constants.netStatus=true;
			}
			if(!mobileInfo.isConnected()&&!wifiInfo.isConnected()){
				//未连接网络
				Toast.makeText(context, "网络连接异常", Toast.LENGTH_LONG).show();
				Constants.netStatus=false;
			}else{
				//连接网络
			}
		};
	};
	/**
	 * 判断手机网络是否连接
	 * @param context
	 * @return true 连接;false 未连接
	 */
	public static boolean isNetworkConnected(Context context){
	    
		if(context!=null){
			ConnectivityManager cm=(ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
			NetworkInfo ni=cm.getActiveNetworkInfo();
			if(ni!=null){
				LogUtil.i("me", "ni.isAvailable="+ni.isAvailable());
				LogUtil.i("me", "ni.isConnected="+ni.isConnected());
				LogUtil.i("me", "ni.isConnectedOrConnecting="+ni.isConnectedOrConnecting());
				LogUtil.i("me", "ni.isFailover="+ni.isFailover());//failover 失效备援（为系统备援能力的一种，当系统中其中一项设备失效而无法运作时，另一项设备即可自动接手原失效系统所执行的工作。）
				LogUtil.i("me", "ni.isRoaming="+ni.isRoaming());//roam 漫游漫步，流浪
				return ni.isAvailable();
			}
		}
		return false;
		
	}
	/**
	 * 判断当前网络连接方式是否为wifi
	 * @param 
	 * @return 如果为false，有可能网络未连接
	 */
	public static boolean isWifiConnected(Context context){
		if(context!=null){
			ConnectivityManager cm=(ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
			NetworkInfo ni=cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
			if(ni!=null){
				return ni.isConnected();
			}
		}
		return false;
	}
	/**
	 * 判断当前网络连接方式是否为 通过 手机卡
	 * @param 
	 * @return 如果为false，有可能网络未连接
	 */
	public static boolean isMobileConnected(Context context){
		if(context!=null){
			ConnectivityManager cm=(ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
			NetworkInfo ni=cm.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
			if(ni!=null){
				return ni.isConnected();
			}
		}
		return false;
	}
	/**
	 * 获得手机联网方式
	 * @param context
	 * @return
	 */
	public static int getConnectedType(Context context){
		if(context!=null){
			ConnectivityManager cm=(ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
			NetworkInfo ni=cm.getActiveNetworkInfo();
			if(ni!=null&&ni.isConnected()){
				return ni.getType();
			}
		}
		//网络未连接
		return -1;
	}
	/**
	 * 获得网络连接类型的名字。
	 * @param context
	 * @return
	 */
	public static String getConnectedTypeName(Context context){
		if(context!=null){
			ConnectivityManager cm=(ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
			NetworkInfo ni=cm.getActiveNetworkInfo();
			if(ni!=null){
				return ni.getTypeName();
			}
		}
		return null;
	}
	/**
	 * 获得网络连接的IPv4地址
	 * @return 如果为null，有可能网络未连接
	 */
	public static String getIp(){
		try {
			Enumeration<NetworkInterface> en=NetworkInterface.getNetworkInterfaces();
			if(en!=null){
				while(en.hasMoreElements()){
					Enumeration<InetAddress> addresses=en.nextElement().getInetAddresses();
					if(addresses!=null){
						while(addresses.hasMoreElements()){
							InetAddress address=addresses.nextElement();
							//loopback address 回环地址，在windows中回环地址是127.0.0.1
							if(!address.isLoopbackAddress()&&InetAddressUtils.isIPv4Address(address.getHostAddress())){
								return address.getHostAddress();
							}
						}
					}
					
				}
			}
		} catch (SocketException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	/**
	 * 获得手机的MAC地址
	 * @param context
	 * @return
	 */
	public static String getMac(Context context){
		if(context!=null){
			WifiManager wm=(WifiManager)context.getSystemService(Context.WIFI_SERVICE);
			WifiInfo wi=wm.getConnectionInfo();
			if(wi!=null){
				return wi.getMacAddress();
			}
		}
		return null;
	}
	
	

}
