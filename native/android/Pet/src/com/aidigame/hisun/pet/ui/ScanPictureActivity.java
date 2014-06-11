package com.aidigame.hisun.pet.ui;

import java.io.File;
import java.io.FilenameFilter;
import java.util.Arrays;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.aidigame.hisun.pet.PetApplication;
import com.aidigame.hisun.pet.R;
import com.aidigame.hisun.pet.adapter.GridViewAdapter;
import com.aidigame.hisun.pet.constant.Constants;
import com.aidigame.hisun.pet.util.UiUtil;
import com.aviary.android.feather.FeatherActivity;

public class ScanPictureActivity extends Activity {
	TextView tv1;
	ImageView imageView;
	GridView gridView;
	List<File> list;
	GridViewAdapter adapter;
	String path;
	int mode;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		UiUtil.setScreenInfo(this);
		PetApplication.petApp.activityList.add(this);
		setContentView(R.layout.activity_scan_pictures);
		tv1=(TextView)findViewById(R.id.textView2);
		imageView=(ImageView)findViewById(R.id.imageView1);
		File file=new File(getIntent().getStringExtra("path"));
		tv1.setText(""+file.getName());
		mode=getIntent().getIntExtra("mode", -1);
		list=Arrays.asList(file.listFiles(new FilenameFilter() {
			
			@Override
			public boolean accept(File dir, String filename) {
				// TODO Auto-generated method stub
				if(filename.endsWith(".png")||filename.endsWith(".jpg")||filename.endsWith(".jpeg")){
					return true;
				}
				return false;
			}
		}));
		gridView=(GridView)findViewById(R.id.gridView1);
		adapter=new GridViewAdapter(this, list);
		gridView.setAdapter(adapter);
		gridView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				
				File file=(File)parent.getItemAtPosition(position);
				path=file.getAbsolutePath();
				if(mode==1){
					Intent intent=getIntent();
					intent.putExtra("path", path);
					setResult(RESULT_OK, intent);
					ScanPictureActivity.this.finish();
				}else{
					Uri uri=Uri.parse("file://"+file.getAbsolutePath());
				    Intent intent=new Intent(ScanPictureActivity.this,FeatherActivity.class);
				    intent.setData(uri);
				    intent.putExtra(com.aviary.android.feather.library.Constants.EXTRA_IN_API_KEY_SECRET, Constants.EXTRA_IN_API_KEY_SECRET);
				    ScanPictureActivity.this.startActivityForResult(intent, 10);
				}

			}
		});
		imageView.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent=new Intent(ScanPictureActivity.this,PictureAlbumActivity.class);
				ScanPictureActivity.this.startActivity(intent);
				ScanPictureActivity.this.finish();
			}
		});
	}
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		if(resultCode==RESULT_OK){
			Intent intent=new Intent(this,SubmitPictureActivity.class);
			intent.setData(data.getData());
			intent.putExtra("path", path);
			this.startActivity(intent);
			this.finish();
		}
	}

}
