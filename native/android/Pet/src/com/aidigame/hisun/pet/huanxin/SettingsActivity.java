/**
 * Copyright (C) 2013-2014 EaseMob Technologies. All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *     http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.aidigame.hisun.pet.huanxin;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.aidigame.hisun.pet.R;
import com.easemob.EMCallBack;
import com.easemob.chat.EMChatManager;
import com.easemob.chat.EMChatOptions;

/**
 * 设置界面
 * 
 * @author Administrator
 * 
 */
public class SettingsActivity extends Activity implements OnClickListener {

	/**
	 * 设置新消息通知布局
	 */
	private RelativeLayout rl_switch_notification;
	/**
	 * 设置声音布局
	 */
	private RelativeLayout rl_switch_sound;
	/**
	 * 设置震动布局
	 */
	private RelativeLayout rl_switch_vibrate;
	/**
	 * 设置扬声器布局
	 */
	private RelativeLayout rl_switch_speaker;

	/**
	 * 打开新消息通知imageView
	 */
	private ImageView iv_switch_open_notification;
	/**
	 * 关闭新消息通知imageview
	 */
	private ImageView iv_switch_close_notification;
	/**
	 * 打开声音提示imageview
	 */
	private ImageView iv_switch_open_sound;
	/**
	 * 关闭声音提示imageview
	 */
	private ImageView iv_switch_close_sound;
	/**
	 * 打开消息震动提示
	 */
	private ImageView iv_switch_open_vibrate;
	/**
	 * 关闭消息震动提示
	 */
	private ImageView iv_switch_close_vibrate;
	/**
	 * 打开扬声器播放语音
	 */
	private ImageView iv_switch_open_speaker;
	/**
	 * 关闭扬声器播放语音
	 */
	private ImageView iv_switch_close_speaker;

	/**
	 * 声音和震动中间的那条线
	 */
	private TextView textview1, textview2;

	private LinearLayout blacklistContainer;
	
	/**
	 * 退出按钮
	 */
	private Button logoutBtn;

	private EMChatOptions chatOptions;
 
	/**
	 * 诊断
	 */
	private LinearLayout llDiagnose;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.fragment_conversation_settings);
		initView() ;
	}

	
	public void initView() {
		rl_switch_notification = (RelativeLayout) findViewById(R.id.rl_switch_notification);
		rl_switch_sound = (RelativeLayout) findViewById(R.id.rl_switch_sound);
		rl_switch_vibrate = (RelativeLayout) findViewById(R.id.rl_switch_vibrate);
		rl_switch_speaker = (RelativeLayout) findViewById(R.id.rl_switch_speaker);

		iv_switch_open_notification = (ImageView) findViewById(R.id.iv_switch_open_notification);
		iv_switch_close_notification = (ImageView) findViewById(R.id.iv_switch_close_notification);
		iv_switch_open_sound = (ImageView) findViewById(R.id.iv_switch_open_sound);
		iv_switch_close_sound = (ImageView) findViewById(R.id.iv_switch_close_sound);
		iv_switch_open_vibrate = (ImageView) findViewById(R.id.iv_switch_open_vibrate);
		iv_switch_close_vibrate = (ImageView) findViewById(R.id.iv_switch_close_vibrate);
		iv_switch_open_speaker = (ImageView) findViewById(R.id.iv_switch_open_speaker);
		iv_switch_close_speaker = (ImageView) findViewById(R.id.iv_switch_close_speaker);
		logoutBtn = (Button) findViewById(R.id.btn_logout);
		if(!TextUtils.isEmpty(EMChatManager.getInstance().getCurrentUser())){
			logoutBtn.setText(getString(R.string.button_logout) + "(" + EMChatManager.getInstance().getCurrentUser() + ")");
		}

		textview1 = (TextView) findViewById(R.id.textview1);
		textview2 = (TextView) findViewById(R.id.textview2);
		
		blacklistContainer = (LinearLayout) findViewById(R.id.ll_black_list);
		llDiagnose=(LinearLayout) findViewById(R.id.ll_diagnose);
		findViewById(R.id.back).setOnClickListener(this);
		blacklistContainer.setOnClickListener(this);
		rl_switch_notification.setOnClickListener(this);
		rl_switch_sound.setOnClickListener(this);
		rl_switch_vibrate.setOnClickListener(this);
		rl_switch_speaker.setOnClickListener(this);
		logoutBtn.setOnClickListener(this);
		llDiagnose.setOnClickListener(this);
		chatOptions = EMChatManager.getInstance().getChatOptions();
		if (chatOptions.getNotificationEnable()) {
			iv_switch_open_notification.setVisibility(View.VISIBLE);
			iv_switch_close_notification.setVisibility(View.INVISIBLE);
		} else {
			rl_switch_sound.setVisibility(View.GONE);
			rl_switch_vibrate.setVisibility(View.GONE);
			textview1.setVisibility(View.GONE);
			textview2.setVisibility(View.GONE);
			iv_switch_open_notification.setVisibility(View.INVISIBLE);
			iv_switch_close_notification.setVisibility(View.VISIBLE);
		}
		if (chatOptions.getNoticedBySound()) {
			iv_switch_open_sound.setVisibility(View.VISIBLE);
			iv_switch_close_sound.setVisibility(View.INVISIBLE);
		} else {
			iv_switch_open_sound.setVisibility(View.INVISIBLE);
			iv_switch_close_sound.setVisibility(View.VISIBLE);
		}
		if (chatOptions.getNoticedByVibrate()) {
			iv_switch_open_vibrate.setVisibility(View.VISIBLE);
			iv_switch_close_vibrate.setVisibility(View.INVISIBLE);
		} else {
			iv_switch_open_vibrate.setVisibility(View.INVISIBLE);
			iv_switch_close_vibrate.setVisibility(View.VISIBLE);
		}

		if (chatOptions.getUseSpeaker()) {
			iv_switch_open_speaker.setVisibility(View.VISIBLE);
			iv_switch_close_speaker.setVisibility(View.INVISIBLE);
		} else {
			iv_switch_open_speaker.setVisibility(View.INVISIBLE);
			iv_switch_close_speaker.setVisibility(View.VISIBLE);
		}

	}

	
	@Override
	public void onClick(View v) {
		int id = v.getId();
        if (id == R.id.rl_switch_notification) {
            if (iv_switch_open_notification.getVisibility() == View.VISIBLE) {
				iv_switch_open_notification.setVisibility(View.INVISIBLE);
				iv_switch_close_notification.setVisibility(View.VISIBLE);
				rl_switch_sound.setVisibility(View.GONE);
				rl_switch_vibrate.setVisibility(View.GONE);
				textview1.setVisibility(View.GONE);
				textview2.setVisibility(View.GONE);
				chatOptions.setNotificationEnable(false);
				chatOptions.setShowNotificationInBackgroud(false);
				EMChatManager.getInstance().setChatOptions(chatOptions);
				

				HXSDKHelper.getInstance().getModel().setSettingMsgNotification(false);
			} else {
				iv_switch_open_notification.setVisibility(View.VISIBLE);
				iv_switch_close_notification.setVisibility(View.INVISIBLE);
				rl_switch_sound.setVisibility(View.VISIBLE);
				rl_switch_vibrate.setVisibility(View.VISIBLE);
				textview1.setVisibility(View.VISIBLE);
				textview2.setVisibility(View.VISIBLE);
				chatOptions.setNotificationEnable(true);
				chatOptions.setShowNotificationInBackgroud(true);
				EMChatManager.getInstance().setChatOptions(chatOptions);
				
				HXSDKHelper.getInstance().getModel().setSettingMsgNotification(true);
			}
        } else if (id == R.id.rl_switch_sound) {
            if (iv_switch_open_sound.getVisibility() == View.VISIBLE) {
				iv_switch_open_sound.setVisibility(View.INVISIBLE);
				iv_switch_close_sound.setVisibility(View.VISIBLE);
				chatOptions.setNoticeBySound(false);
				EMChatManager.getInstance().setChatOptions(chatOptions);
				HXSDKHelper.getInstance().getModel().setSettingMsgSound(false);
			} else {
				iv_switch_open_sound.setVisibility(View.VISIBLE);
				iv_switch_close_sound.setVisibility(View.INVISIBLE);
				chatOptions.setNoticeBySound(true);
				EMChatManager.getInstance().setChatOptions(chatOptions);
				HXSDKHelper.getInstance().getModel().setSettingMsgSound(true);
			}
        } else if (id == R.id.rl_switch_vibrate) {
            if (iv_switch_open_vibrate.getVisibility() == View.VISIBLE) {
				iv_switch_open_vibrate.setVisibility(View.INVISIBLE);
				iv_switch_close_vibrate.setVisibility(View.VISIBLE);
				chatOptions.setNoticedByVibrate(false);
				EMChatManager.getInstance().setChatOptions(chatOptions);
				HXSDKHelper.getInstance().getModel().setSettingMsgVibrate(false);
			} else {
				iv_switch_open_vibrate.setVisibility(View.VISIBLE);
				iv_switch_close_vibrate.setVisibility(View.INVISIBLE);
				chatOptions.setNoticedByVibrate(true);
				EMChatManager.getInstance().setChatOptions(chatOptions);
				HXSDKHelper.getInstance().getModel().setSettingMsgVibrate(true);
			}
        } else if (id == R.id.rl_switch_speaker) {
            if (iv_switch_open_speaker.getVisibility() == View.VISIBLE) {
				iv_switch_open_speaker.setVisibility(View.INVISIBLE);
				iv_switch_close_speaker.setVisibility(View.VISIBLE);
				chatOptions.setUseSpeaker(false);
				EMChatManager.getInstance().setChatOptions(chatOptions);
				HXSDKHelper.getInstance().getModel().setSettingMsgSpeaker(false);
			} else {
				iv_switch_open_speaker.setVisibility(View.VISIBLE);
				iv_switch_close_speaker.setVisibility(View.INVISIBLE);
				chatOptions.setUseSpeaker(true);
				EMChatManager.getInstance().setChatOptions(chatOptions);
				HXSDKHelper.getInstance().getModel().setSettingMsgVibrate(true);
			}
        } else if (id == R.id.btn_logout) {
            logout();
        } else if (id == R.id.ll_black_list) {
//            startActivity(new Intent(getActivity(), BlacklistActivity.class));
        } else if (id == R.id.ll_diagnose) {
//            startActivity(new Intent(getActivity(), DiagnoseActivity.class));
        } else if(id==R.id.back){
        	finish();
        }

	}

	void logout() {
		final ProgressDialog pd = new ProgressDialog(this);
		pd.setMessage("正在退出登陆..");
		pd.setCanceledOnTouchOutside(false);
		pd.show();
	}

	
   
}
