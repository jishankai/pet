package com.aidigame.hisun.pet.ui;

import java.util.ArrayList;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AbsListView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.aidigame.hisun.pet.R;
import com.aidigame.hisun.pet.adapter.UsersListAdapter;
import com.aidigame.hisun.pet.bean.Animal;
import com.aidigame.hisun.pet.bean.User;
import com.aidigame.hisun.pet.http.HttpUtil;
import com.aidigame.hisun.pet.http.json.UserJson;
import com.aidigame.hisun.pet.util.HandleHttpConnectionException;
import com.aidigame.hisun.pet.util.StringUtil;
import com.aidigame.hisun.pet.util.UiUtil;
import com.aidigame.hisun.pet.util.UserStatusUtil;
import com.aidigame.hisun.pet.widget.fragment.ClawStyleFunction;
import com.aidigame.hisun.pet.widget.fragment.HomeFragment;
import com.aidigame.hisun.pet.widget.fragment.ClawStyleFunction.ClawFunctionChoseListener;
/**
 * 围观群众界面
 * @author admin
 *
 */
public class UsersListActivity extends Activity {
	FrameLayout frameLayout;
	View viewTopWhite;
	public View popup_parent;
	public RelativeLayout black_layout;
	UsersListAdapter adapter;
	ArrayList<User> list;
	ListView  listView;
	String likerString;
	String senderString;
	ImageView back;
	TextView title;
	HandleHttpConnectionException handleHttpConnectionException;
	Handler handler=new Handler(){
		public void handleMessage(android.os.Message msg) {
			
		};
	};
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		UiUtil.setScreenInfo(this);
		setContentView(R.layout.activity_users_list);
		listView=(ListView)findViewById(R.id.users_list_listview);
		listView.setDivider(null);
		back=(ImageView)findViewById(R.id.imageView1);
		title=(TextView)findViewById(R.id.textView1);
		handleHttpConnectionException=HandleHttpConnectionException.getInstance();
		setBlurImageBackground();
		popup_parent=findViewById(R.id.popup_parent);
		title.setText(getIntent().getStringExtra("title"));
		likerString=getIntent().getStringExtra("likers");
		senderString=getIntent().getStringExtra("senders");
		black_layout=(RelativeLayout)findViewById(R.id.black_layout);
		list=new ArrayList<User>();
		adapter=new UsersListAdapter(this, list,null,getIntent().getIntExtra("animalType", 1));
		listView.setAdapter(adapter);
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
//				if(!UserStatusUtil.isLoginSuccess(UsersListActivity.this,popup_parent,black_layout))return;
				final User user=list.get(position);
				if(UserDossierActivity.userDossierActivity!=null)UserDossierActivity.userDossierActivity.finish();
				Intent intent=new Intent(UsersListActivity.this,UserDossierActivity.class);
				intent.putExtra("user", user);
				UsersListActivity.this.startActivity(intent);
				UsersListActivity.this.finish();
				
			}
		});
		back.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				UsersListActivity.this.finish();
			}
		});
		new Thread(new Runnable() {
			ArrayList<User> temp,temp1;
			@Override
			public void run() {
				// TODO Auto-generated method stub
				if(!StringUtil.isEmpty(likerString)){
					temp=HttpUtil.getOthersList(likerString, handler,UsersListActivity.this,2);
				}
				if(!StringUtil.isEmpty(senderString)){
					temp1=HttpUtil.getOthersList(senderString, handler,UsersListActivity.this,1);
				}
				
				
					runOnUiThread(new Runnable() {
						
						@Override
						public void run() {
							// TODO Auto-generated method stub
							if(list==null){
								list=new ArrayList<User>();
							}
							if(temp1!=null){
								for(int i=0;i<temp1.size();i++){
									list.add(temp1.get(i));
								}
							}
							if(temp!=null){
								for(int i=0;i<temp.size();i++){
									list.add(temp.get(i));
								}
							}
							adapter.updateList(list);
							adapter.notifyDataSetChanged();
						}
					});
				
			}
		}).start();
	}
	/**
	 * 设置毛玻璃背景，列表滑动时顶部变透明并显示列表
	 */

	private void setBlurImageBackground() {
		// TODO Auto-generated method stub
		frameLayout=(FrameLayout)findViewById(R.id.framelayout);
		viewTopWhite=(View)findViewById(R.id.top_white_view);
		if(HomeFragment.blurBitmap==null){
			frameLayout.setBackgroundDrawable(getResources().getDrawable(R.drawable.blur));
		}
        new Thread(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				while(HomeFragment.blurBitmap==null){
					try {
						Thread.sleep(50);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				runOnUiThread(new Runnable() {
					
					@Override
					public void run() {
						// TODO Auto-generated method stub
						frameLayout.setBackgroundDrawable(new BitmapDrawable(HomeFragment.blurBitmap));
//						frameLayout.setAlpha(0.9342857f);
					}
				});
			}
		}).start();
		 listView.setOnScrollListener(new OnScrollListener() {
				
				@Override
				public void onScrollStateChanged(AbsListView view, int scrollState) {
					// TODO Auto-generated method stub
					if(listView.getFirstVisiblePosition()==0&&listView.getChildAt(0).getTop()==0){
						viewTopWhite.setVisibility(View.VISIBLE);
					}else{
						if(viewTopWhite.getVisibility()!=View.GONE){
							viewTopWhite.setVisibility(View.GONE);
						}
					}
				}
				
				@Override
				public void onScroll(AbsListView view, int firstVisibleItem,
						int visibleItemCount, int totalItemCount) {
					// TODO Auto-generated method stub
					
				}
			});
	}

	

}
