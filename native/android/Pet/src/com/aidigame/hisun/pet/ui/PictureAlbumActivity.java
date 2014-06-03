package com.aidigame.hisun.pet.ui;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ListView;
import android.widget.TextView;

import com.aidigame.hisun.pet.R;
import com.aidigame.hisun.pet.adapter.PictureAlbumAdapter;
import com.aidigame.hisun.pet.util.UiUtil;

public class PictureAlbumActivity extends Activity {
	ListView listView;
	TextView cancel;
	PictureAlbumAdapter adapter;
	Handler handler=new Handler(){
		public void handleMessage(android.os.Message msg) {
			adapter.update();
			adapter.notifyDataSetChanged();;
		};
	};
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		UiUtil.setScreenInfo(this);
		setContentView(R.layout.activity_show_album);
		listView=(ListView)findViewById(R.id.listView1);
		cancel=(TextView)findViewById(R.id.textView1);
		adapter=new PictureAlbumAdapter(this, handler);
		listView.setAdapter(adapter);
		cancel.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				PictureAlbumActivity.this.finish();
			}
		});
	}

}
