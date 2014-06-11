package com.aidigame.hisun.pet.ui;

import java.io.File;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;

import com.aidigame.hisun.pet.adapter.PictureAlbumAdapter;
import com.aidigame.hisun.pet.util.UiUtil;
import com.aidigame.hisun.pet.PetApplication;
import com.aidigame.hisun.pet.R;

public class PictureAlbumActivity extends Activity {
	ListView listView;
	TextView cancel;
	PictureAlbumAdapter adapter;
	int mode;
	Handler handler=new Handler(){
		public void handleMessage(android.os.Message msg) {
			adapter.update();
			adapter.notifyDataSetChanged();
		};
	};
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		UiUtil.setScreenInfo(this);
		PetApplication.petApp.activityList.add(this);
		setContentView(R.layout.activity_show_album);
		listView=(ListView)findViewById(R.id.listView1);
		cancel=(TextView)findViewById(R.id.textView1);
		adapter=new PictureAlbumAdapter(this, handler);
		listView.setAdapter(adapter);
		mode=getIntent().getIntExtra("mode", -1);
		cancel.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent=new Intent(PictureAlbumActivity.this,TakePictureActivity.class);
				PictureAlbumActivity.this.startActivity(intent);
				PictureAlbumActivity.this.finish();
			}
		});
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				File file=(File)parent.getItemAtPosition(position);
				Intent intent=new Intent(PictureAlbumActivity.this,ScanPictureActivity.class);
				intent.putExtra("path", file.getAbsolutePath());
				intent.putExtra("mode", mode);
				PictureAlbumActivity.this.startActivityForResult(intent, 10);
//				PictureAlbumActivity.this.startActivity(intent);
//				PictureAlbumActivity.this.finish();
			}
		});
	}
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		if(resultCode==RESULT_OK){
			if(mode==1){
				Intent intent=getIntent();
				intent.putExtra("path", data.getStringExtra("path"));
				setResult(RESULT_OK, intent);
				this.finish();
			}else{
				Intent intent=new Intent(this,TakePictureActivity.class);
				intent.setData(data.getData());
				setResult(resultCode, intent);
				this.finish();
			}

		}
	}

}
