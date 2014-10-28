package com.aidigame.hisun.pet.widget;

import java.io.BufferedOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import android.content.Context;
import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.media.MediaRecorder.AudioEncoder;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnLongClickListener;
import android.view.View.OnTouchListener;
import android.widget.Toast;

import com.aidigame.hisun.pet.constant.Constants;
import com.aidigame.hisun.pet.util.LogUtil;
import com.aidigame.hisun.pet.util.StringUtil;

public class AudioRecordAndPlayer {
	public  MediaRecorder mediaRecorder;
	public boolean isRecording=false;//是否在录音
	public String recordFileName;//录制的音频文件名
	private MediaPlayer mediaPlayer;
	boolean isPlaying=false;//是否在播放
	AudioRecord mRecorder;
	short[] mBuffer;
	String tempFile,finalFile;
	Context context;
	View clickView;//长按录音控件
	public AudioRecordAndPlayer(View view,Context context){
		this.clickView=view;
		this.context=context;
		
		initViewListener();
	}
	public AudioRecordAndPlayer(Context context){
		initRecorder();
	}
	/**
	 * 初始化长按控件的touch事件 longClick事件  click事件
	 */
	private void initViewListener() {
		// TODO Auto-generated method stub
		/*
		 *长按，开始录音 
		 */
		clickView.setOnLongClickListener(new OnLongClickListener() {
			
			@Override
			public boolean onLongClick(View v) {
				// TODO Auto-generated method stub
				if(!isRecording){
					startRecord();
					
				}
				return true;
			}
		});
		/*
		 * 长按，抬起手指后，停止录音
		 */
		clickView.setOnTouchListener(new OnTouchListener() {
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				
				switch (event.getAction()) {
				case MotionEvent.ACTION_DOWN:
					LogUtil.i("scroll","send_down");
					
					break;
				case MotionEvent.ACTION_MOVE:
					LogUtil.i("scroll","send_move");
					break;
				case MotionEvent.ACTION_UP:
					if(isRecording&&mediaRecorder!=null){
						stopRecord();
						//测试一下，播放刚才录制的声音
						playAudio(recordFileName);
						
						/*
						 * TODO 1.判断音频文件时间长短
						 *      2.发送音频文件  检测是否和谐
						 */
					}
					LogUtil.i("scroll","send_up");
					break;
				}
				return false;
			}
		});
	}
	/**
	 * 开始录音
	 * 
	 */
	public void startRecord(){
		/*if(!isRecording){
			isRecording=true;
			tempFile=Constants.Picture_Root_Path+File.separator+System.currentTimeMillis()+".raw";
			
			mRecorder.startRecording();
			startBufferedWrite(new File(tempFile));
		}
		*/
		/*
		 * 判断是否正在录音，如果没有则录音
		 */
		if(!isRecording){
			mediaRecorder=new MediaRecorder();
			/*
			 * 设置声音来源
			 * MediaRecord.AudioSource这个类内部详细介绍了声音来源
			 * 该类中有许多声音来源，最主要的还是麦克风MediaRecorder.AudioSource.MIC
			 */
			mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
			/*
			 * 设定文件输出格式,此语句必须在mediaRecorder.setAudioSource之后，在prepare之前
			 * OutputFormat内部类，定义了音频输出格式，主要有MPEG_4,THREE_GPP,等
			 */
			mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.AMR_NB);
			/*
			 * 设定文件编码格式
			 */
			mediaRecorder.setAudioEncoder(AudioEncoder.AMR_NB);
			/*
			 * 设定文件输出位置
			 */
			String fileName=Constants.Picture_Root_Path+File.separator+System.currentTimeMillis()+".amr";
			
			Constants.audioPath=fileName;
			mediaRecorder.setOutputFile(fileName);
			try {
				mediaRecorder.prepare();
				mediaRecorder.start();
				isRecording=true;
				recordFileName=fileName;
			} catch (IllegalStateException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	/**
	 * 停止录音，释放资源
	 * @return 音频文件的路径名或者为null
	 */
	public String stopRecord(){
		/*
		 * 录音前，先停止播放音频
		 */
		stopAudio();
		
		/*
		 * 判断是否正在录音
		 */
		if(isRecording&&mediaRecorder!=null){
			try {
				mediaRecorder.stop();
				mediaRecorder.reset();
				mediaRecorder.release();
				mediaRecorder=null;
				isRecording=false;
				/*FLameUtils lameUtils=new FLameUtils(1, 16000, 96);
				finalFile=Constants.Picture_Root_Path+File.separator+System.currentTimeMillis()+".mp3";
				if(lameUtils.raw2mp3(recordFileName, finalFile)){
					LogUtil.i("me", "成功");
				}else{
					LogUtil.i("me", "失败");
				}*/
				return recordFileName;
			} catch (Exception e) {
				// TODO: handle exception
				mediaRecorder.reset();
				mediaRecorder.release();
				mediaRecorder=null;
				isRecording=false;
			}
			
		}
		
		/*if(isRecording){
			mRecorder.stop();
			isRecording=false;
			
		}
		recordFileName=tempFile;
			new Thread(new Runnable() {
				
				@Override
				public void run() {
					// TODO Auto-generated method stub
					
					File file=new File(tempFile);
					while(!file.exists()){}
					if(file.exists()){
					finalFile=Constants.Picture_Root_Path+File.separator+System.currentTimeMillis()+".mp3";
					FLameUtils lameUtils=new FLameUtils(1, 16000, 96);
					if(lameUtils.raw2mp3(tempFile, finalFile)){
						LogUtil.i("me", "成功");
					}else{
						LogUtil.i("me", "失败");
					}
					}
				}
			}).start();*/
		
		return null;
		
	}
	public String getFinalFile(){
		if(!StringUtil.isEmpty(tempFile)&&!StringUtil.isEmpty(finalFile)){
			if(new File(finalFile).exists()){
				return finalFile;
			}
		}
		return null;
	}
	/**
	 * 开始播放音频，重新播放
	 * @param fileName
	 */
	public boolean playAudio(String fileName){
		isPause=false;
		if(!isPlaying){
			mediaPlayer=new MediaPlayer();
			try {
				mediaPlayer.setDataSource(fileName);
				mediaPlayer.prepare();
				mediaPlayer.start();
				isPlaying=true;
				
			} catch (IllegalArgumentException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (SecurityException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalStateException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}else{
			stopAudio();
			if(!isPlaying){
				playAudio(fileName);
			}
			
		}
		return isPlaying;
		
	}
	/**
	 * 暂停播放
	 */
	boolean isPause=false;
	public void pauseAudio(){
		if(!isPause){
			isPause=true;
			mediaPlayer.pause();
		}else{
			isPause=false;
			mediaPlayer.start();
		}
	}
	/**
	 * 停止播放音频
	 */
	public void stopAudio(){
		if(isPlaying&&mediaPlayer!=null){
			mediaPlayer.stop();
			mediaPlayer.release();
			mediaPlayer=null;
			isPlaying=false;
			isPause=false;
		}
	}
	public String getFilePath(){
		return recordFileName;
	}
	
	
	/**
	 * 初始化Recorder
	 */
	public void initRecorder()
	{
		int bufferSize = AudioRecord.getMinBufferSize(16000, AudioFormat.CHANNEL_IN_MONO,
				AudioFormat.ENCODING_PCM_16BIT);
		mBuffer = new short[bufferSize];
		mRecorder = new AudioRecord(MediaRecorder.AudioSource.MIC, 16000, AudioFormat.CHANNEL_IN_MONO,
				AudioFormat.ENCODING_PCM_16BIT, bufferSize);
	}
	/**
	 * 写入到文件
	 * @param file
	 */
	private void startBufferedWrite(final File file) {
		new Thread(new Runnable() {
			@Override
			public void run() {
				DataOutputStream output = null;
				try {
					output = new DataOutputStream(new BufferedOutputStream(new FileOutputStream(file)));
					while (isRecording) {
						int readSize = mRecorder.read(mBuffer, 0, mBuffer.length);
						for (int i = 0; i < readSize; i++) {
							output.writeShort(mBuffer[i]);
						}
					}
				} catch (IOException e) {
					Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
				} finally {
					if (output != null) {
						try {
							output.flush();
						} catch (IOException e) {
							Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
						} finally {
							try {
								output.close();
							} catch (IOException e) {
								Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
							}
						}
					}
				}
			}
		}).start();
	}

}
