package com.aidigame.hisun.imengstar.ui;

import java.util.ArrayList;

import me.maxwin.view.XListView;
import me.maxwin.view.XListView.IXListViewListener;
import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.PopupWindow.OnDismissListener;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.aidigame.hisun.imengstar.PetApplication;
import com.aidigame.hisun.imengstar.adapter.ContributeRankListAdapter;
import com.aidigame.hisun.imengstar.adapter.GetTicketRankListAdapter;
import com.aidigame.hisun.imengstar.bean.Animal;
import com.aidigame.hisun.imengstar.bean.Banner;
import com.aidigame.hisun.imengstar.bean.MyUser;
import com.aidigame.hisun.imengstar.constant.Constants;
import com.aidigame.hisun.imengstar.http.HttpUtil;
import com.aidigame.hisun.imengstar.util.HandleHttpConnectionException;
import com.aidigame.hisun.imengstar.util.StringUtil;
import com.aidigame.hisun.imengstar.util.UiUtil;
import com.aidigame.hisun.imengstar.util.UserStatusUtil;
import com.aidigame.hisun.imengstar.R;
import com.huewu.pla.lib.internal.PLA_AdapterView;
import com.huewu.pla.lib.internal.PLA_AdapterView.OnItemClickListener;
/**
 * 王国贡献榜
 * @author admin
 *
 */
public class GetTicketRankListActivity extends BaseActivity implements IXListViewListener {
	FrameLayout frameLayout;
	View viewTopWhite;
	public View popupParent;
	public RelativeLayout black_layout;
	
	public XListView listView/*,listView2*/;
	TextView findMeTV,contributeTv;
	GetTicketRankListAdapter adapter;
//	ContributeRankListAdapter adapter2;
	public ArrayList<Animal> animalList;
	PopupWindow popupWindow;
	public boolean findMe=true;
	private int gold;
	private Banner banner;
	Handler handler;
	int page=0;
//	Animal animal;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_get_ticket_rank);
		black_layout=(RelativeLayout)findViewById(R.id.black_layout);
		popupParent=(View)findViewById(R.id.popup_parent);
		handler=HandleHttpConnectionException.getInstance().getHandler(this);
//		animal=(Animal)getIntent().getSerializableExtra("animal");
		banner=(Banner)getIntent().getSerializableExtra("Banner");
		gold=getIntent().getIntExtra("gold", 0);
		loadData(0);
		initView();
	}

	private void loadData( final int page) {
		// TODO Auto-generated method stub
		animalList=new ArrayList<Animal>();
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				final ArrayList<Animal> animals=HttpUtil.getTicketRankList(GetTicketRankListActivity.this, banner.star_id, page, handler);
				if(animals!=null){
					runOnUiThread(new Runnable() {
						public void run() {
							animalList=animals;
							adapter.updateData(animalList);
							adapter.notifyDataSetChanged();
							
						}
					});
				}
				
			}
		}).start();
		
	}
	int length,itemtH,itemNum;
    public int position;
	private void initView() {
		// TODO Auto-generated method stub
		listView=(XListView)findViewById(R.id.listview);
		findMeTV=(TextView)findViewById(R.id.textView3);
		contributeTv=(TextView)findViewById(R.id.textView2);
		adapter=new GetTicketRankListAdapter(this,animalList,1,gold,banner);
		listView.setAdapter(adapter);
		listView.setPullLoadEnable(true);
		listView.setPullRefreshEnable(true);
		listView.setXListViewListener(this);
        listView.setSelector(new BitmapDrawable());
		findViewById(R.id.imageView1).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				GetTicketRankListActivity.this.finish();
				
				System.gc();
			}
		});
		/*listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(PLA_AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				
			}
		});*/

	}


	public static class People{
		public String name;
		public int contributeNum;
		public int rankNo;
		public int userId;
		
		public boolean showArrow=false;
	}


		@Override
		public void onRefresh() {
			// TODO Auto-generated method stub
			page=0;
			new Thread(new Runnable() {
				
				@Override
				public void run() {
					// TODO Auto-generated method stub
					final ArrayList<Animal> animals=HttpUtil.getTicketRankList(GetTicketRankListActivity.this, banner.star_id, page, handler);
					
						runOnUiThread(new Runnable() {
							public void run() {
								if(animals!=null){
								animalList=animals;
								adapter.updateData(animalList);
								adapter.notifyDataSetChanged();
								}
								listView.stopRefresh();
								
							}
						});
					
					
				}
			}).start();
		}

		@Override
		public void onLoadMore() {
			// TODO Auto-generated method stub
			if(animalList.size()>0){
				page++;
			}else{
				page=0;
			}
			
			new Thread(new Runnable() {
				
				@Override
				public void run() {
					// TODO Auto-generated method stub
					final ArrayList<Animal> animals=HttpUtil.getTicketRankList(GetTicketRankListActivity.this, banner.star_id, page, handler);
					
						runOnUiThread(new Runnable() {
							public void run() {
								if(animals!=null){
									for(int i=0;i<animals.size();i++){
										if(!animalList.contains(animals.get(i))){
											animalList.add(animals.get(i));
										}
									}
//								animalList=animals;
								adapter.updateData(animalList);
								adapter.notifyDataSetChanged();
								}
								listView.stopLoadMore();;
								
							}
						});
					
					
				}
			}).start();
		}

}
