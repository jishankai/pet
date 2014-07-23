package com.aidigame.hisun.pet.widget;

import android.R.integer;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;

public class ShowDialog {
	public static int count=0;
	public static void show(final String msg,final Activity context){
		if(msg==null)return;
		if(count==0&&context!=null)
		context.runOnUiThread(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				count+=1;
				if(count==1){
					AlertDialog.Builder builder=new AlertDialog.Builder(context);
					if(count==1){
						AlertDialog dialog=builder.setTitle("提示信息").setMessage(msg).setPositiveButton("确定", new DialogInterface.OnClickListener() {
							
							@Override
							public void onClick(DialogInterface dialog, int which) {
								// TODO Auto-generated method stub
								dialog.dismiss();
								count=0;
							}
						}).show();
					}
					
				}
				
			}
		});
		
	}

}
