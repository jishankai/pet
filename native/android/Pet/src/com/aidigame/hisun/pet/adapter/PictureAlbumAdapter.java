package com.aidigame.hisun.pet.adapter;

import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.aidigame.hisun.pet.R;

public class PictureAlbumAdapter extends BaseAdapter {
	Context context;
	ArrayList<File> list=new ArrayList<File>();
	ArrayList<File> temp=new ArrayList<File>();
	Handler handler;
	public PictureAlbumAdapter(Context context,Handler handler){
		this.context=context;
		this.handler=handler;
        new Thread(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				loadData();
				PictureAlbumAdapter.this.handler.sendEmptyMessage(1);
			}
		}).start();
		
	}

	private void loadData() {
		// TODO Auto-generated method stub
		String path=Environment.getExternalStorageDirectory().getAbsolutePath();
		checkFile(path);
	}
	public void update(){
		list.clear();
		for(File f:temp){
			
			list.add(f);
		}
	}

	private void checkFile(String path) {
		// TODO Auto-generated method stub
		
		File file=new File(path);
		File[] files=file.listFiles(new FileFilter() {
			
			@Override
			public boolean accept(File pathname) {
				// TODO Auto-generated method stub
				if(pathname.isDirectory())return true;
				return false;
			}
		});
		for(File f:files){
			File[] fs2=f.listFiles();
			if(fs2==null)continue;
			for(File f1:fs2){
				if(f1.isDirectory()){
					checkFile(f1.getAbsolutePath());
				}else{
					String name=f1.getName();
					if(name.endsWith("jpg")||name.endsWith("png")||name.endsWith("jpeg")){
						temp.add(f);
						handler.sendEmptyMessage(1);
						break;
					}
				}
			}
		}
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return list.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		Holder holder=null;
		if(convertView==null){
			holder=new Holder();
			convertView=LayoutInflater.from(context).inflate(R.layout.item_picture_album, null);
			holder.picture=(ImageView)convertView.findViewById(R.id.imageView1);
			holder.fileName=(TextView)convertView.findViewById(R.id.textView1);
			holder.fileNum=(TextView)convertView.findViewById(R.id.textView2);
			convertView.setTag(holder);;
			
		}else{
			holder=(Holder)convertView.getTag();
		}
		File f=list.get(position);
		File[] files=f.listFiles(new FileFilter() {
			
			@Override
			public boolean accept(File pathname) {
				// TODO Auto-generated method stub
				String name=pathname.getName();
				if(name.endsWith("jpg")||name.endsWith("png")||name.endsWith("jpeg")){
					
					return true;
				}
				return false;
			}
			
		});
		if(files.length>0){
			File temp=files[0];
			BitmapFactory.Options options=new BitmapFactory.Options();
			options.inSampleSize=4;
			Bitmap bmp=BitmapFactory.decodeFile(temp.getAbsolutePath(), options);
			holder.picture.setImageBitmap(bmp);
			holder.fileName.setText(""+f.getName());
			holder.fileNum.setText(""+files.length);
		}
		return convertView;
	}
	class Holder{
		ImageView picture;
		TextView fileName;
		TextView fileNum;
	}

}
