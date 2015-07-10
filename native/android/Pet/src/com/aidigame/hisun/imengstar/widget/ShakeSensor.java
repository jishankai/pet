package com.aidigame.hisun.imengstar.widget;

import com.aidigame.hisun.imengstar.util.LogUtil;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorListener;
import android.hardware.SensorManager;

public class ShakeSensor implements SensorEventListener{
	//晃动的速度，当速度超过这个值时，进行事件处理
	private static final int SHAKE_SPEED=3000;
	//两次晃动的时间间隔，超过此时间进行事件处理
	private static final int UPDATE_INTERVAL_TIME=70;
	//传感器管理器
	private SensorManager sensorManager;
	//传感器
	private Sensor sensor;
	//晃动事件处理器
	private OnShakeLisener onShakeLisener;
	Context context;
	float lastX;
	float lastY;
	float lastZ;
	long lastTime;
	public ShakeSensor(Context context){
		this.context=context;
		start();
	}
	public void start(){
		//获得传感器管理器
		sensorManager=(SensorManager)context.getSystemService(Context.SENSOR_SERVICE);
		//获得重力传感器
		if(sensorManager!=null){
			sensor=sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
		}
		//注册重力传感器监听器
		if(sensor!=null){
			sensorManager.registerListener(this,sensor,SensorManager.SENSOR_DELAY_GAME);
		}
	}
	public void stop(){
		if(sensorManager!=null){
			sensorManager.unregisterListener(this);
		}
	}
	public void setOnShakeListener(OnShakeLisener listener){
		this.onShakeLisener=listener;
	}
	
	/**
	 * 当产生晃动事件时，回调的方法，也就是说晃动后要做的处理
	 * @author admin
	 *
	 */
	public interface OnShakeLisener{
		void onShake();
	}
	@Override
	public void onSensorChanged(SensorEvent event) {
		// TODO Auto-generated method stub
		long currentTime=System.currentTimeMillis();
		long timeInterval=currentTime-lastTime;
		if(timeInterval<UPDATE_INTERVAL_TIME){
			return;
		}
		long time=currentTime-lastTime;
		lastTime=currentTime;
		
		float nowX=event.values[0];
		float nowY=event.values[1];
		float nowZ=event.values[2];
		float dx=nowX-lastX;
		float dy=nowY-lastY;
		float dz=nowZ-lastZ;
		lastX=nowX;
		lastY=nowY;
		lastZ=nowZ;
		double speed=Math.sqrt(dx*dx+dy*dy+dz*dz)/time*10000;
		if(speed>SHAKE_SPEED){
			onShakeLisener.onShake();
			LogUtil.i("pull", "x="+lastX+",y="+lastY+",z="+lastZ+",speed="+(speed)/10000+",time="+time+",distance="+Math.sqrt(dx*dx+dy*dy+dz*dz));
		}
	}
	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy) {
		// TODO Auto-generated method stub
		
	}

}
