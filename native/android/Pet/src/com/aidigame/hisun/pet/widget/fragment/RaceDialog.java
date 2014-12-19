package com.aidigame.hisun.pet.widget.fragment;

import java.util.ArrayList;

import net.simonvt.numberpicker.NumberPicker;
import net.simonvt.numberpicker.NumberPicker.OnValueChangeListener;

import com.aidigame.hisun.pet.R;
import com.aidigame.hisun.pet.constant.AddressData;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
/**
 * 选择种族
 * @author admin
 *
 */
public class RaceDialog {
	NumberPicker starsPicker;
	NumberPicker racePicker;
	ArrayList<String> starsList;
	ArrayList<String> raceList;
	int starValue=0;
	int raceValue=0;
	String stars;
	String race;
	TextView provinceTV;
	
	
	Context context;
	View view;
	public RaceDialog(Context context){
		this.context=context;
		view=LayoutInflater.from(context).inflate(R.layout.widget_address_picker, null);
		starsPicker=(NumberPicker)view.findViewById(R.id.numberpicker1);
		racePicker=(NumberPicker)view.findViewById(R.id.numberpicker2);
		provinceTV=(TextView)view.findViewById(R.id.province_tv);
		String[] array=context.getResources().getStringArray(R.array.stars);
		starsList=new ArrayList<String>();
		for(int i=0;i<array.length;i++){
			starsList.add(array[i]);
		}
		
		
		starsPicker.setDisplayedValues(array);
		starsPicker.setMaxValue(array.length-1);
		raceList=new ArrayList<String>();
		
		starsPicker.setMinValue(0);
		starsPicker.setOnValueChangedListener(new OnValueChangeListener() {
			
			@Override
			public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
				// TODO Auto-generated method stub
				racePicker.setValue(0);
				starValue=newVal;
				stars=starsList.get(newVal);
				String[] array=null;
				
				switch (newVal) {
				case 0:
					array=RaceDialog.this.context.getResources().getStringArray(R.array.cat_race);
					break;
				case 1:
					array=RaceDialog.this.context.getResources().getStringArray(R.array.dog_race);
					break;
				case 2:
					array=RaceDialog.this.context.getResources().getStringArray(R.array.other_race);
					break;
				}
				raceList=new ArrayList<String>();
				for(int i=0;i<array.length;i++){
					raceList.add(array[i]);
				}
				racePicker.setDisplayedValues(array);
				racePicker.setMaxValue(array.length-1);
				racePicker.setMinValue(0);
				
				
				race=array[0];
				provinceTV.setText(stars);
			}
		});
		array=context.getResources().getStringArray(R.array.cat_race);
		for(int i=0;i<array.length;i++){
			raceList.add(array[i]);
		}
		racePicker.setDisplayedValues(array);
		racePicker.setMaxValue(array.length-1);
		racePicker.setMinValue(0);
		racePicker.setOnValueChangedListener(new OnValueChangeListener() {
			
			@Override
			public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
				// TODO Auto-generated method stub
				raceValue=newVal;
				race=raceList.get(newVal);
			}
		});
		
		stars=starsList.get(0);
		race=raceList.get(0);
		provinceTV.setText(stars);
	}
	public View getView(){
		return view;
	}
	public String getRace(){
		return stars=raceList.get(raceValue);
	}
	public String getRaceCode(){
		String string="";
		if(starValue==0){
			string=""+(101+raceValue);
		}else if(starValue==1){
			string=""+(201+raceValue);
		}else if(starValue==2){
			string=""+(301+raceValue);
		}
		return string;
		
	}

}
