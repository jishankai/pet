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

public class AgeDialog {
	NumberPicker yearPicker;
	NumberPicker monthPicker;
//	ArrayList<String> provincesList;
//	ArrayList<String> cityList;
	int yearValue=0;
	int monthValue=0;
	String y;
	String m;
	TextView yearTV;
	String[] year,month;
	
	Context context;
	View view;
	public AgeDialog(Context context){
		this.context=context;
		view=LayoutInflater.from(context).inflate(R.layout.widget_address_picker, null);
		yearPicker=(NumberPicker)view.findViewById(R.id.numberpicker1);
		monthPicker=(NumberPicker)view.findViewById(R.id.numberpicker2);
		yearTV=(TextView)view.findViewById(R.id.province_tv);
		year=new String[100];
		for(int i=0;i<year.length;i++){
			year[i]=""+i+"岁";
		}
		month=new String[13];
		for(int i=0;i<month.length;i++){
			month[i]=""+i+"个月";
		}
	/*	provincesList=new ArrayList<String>();
		for(int i=0;i<AddressData.PROVINCES.length;i++){
			provincesList.add(AddressData.PROVINCES[i]);
		}
		cityList=new ArrayList<String>();
		for(int i=0;i<AddressData.CITIES[0].length;i++){
			cityList.add(AddressData.CITIES[0][i]);
		}*/
		yearPicker.setDisplayedValues(year);
		yearPicker.setMaxValue(year.length-1);
		yearPicker.setMinValue(0);
		yearPicker.setOnValueChangedListener(new OnValueChangeListener() {
			
			@Override
			public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
				// TODO Auto-generated method stub
				monthPicker.setValue(0);
				yearValue=newVal;
				y=year[newVal];
				
				monthPicker.setDisplayedValues(month);
				monthPicker.setMaxValue(month.length-1);
				monthPicker.setMinValue(0);
				
				
				m=month[0];
				yearTV.setText(y);
			}
		});
		monthPicker.setDisplayedValues(month);
		monthPicker.setMaxValue(month.length-1);
		monthPicker.setMinValue(0);
		monthPicker.setOnValueChangedListener(new OnValueChangeListener() {
			
			@Override
			public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
				// TODO Auto-generated method stub
				monthValue=newVal;
				m=month[newVal];
			}
		});
		
		y=year[0];
		m=month[0];
		yearTV.setText(y);
	}
	public View getView(){
		return view;
	}
	public String getAge(){
		if(yearValue==0&&monthValue==0){
			return null;
		}
		
		if(yearValue==0&&monthValue!=0){
			return month[monthValue];
		}
		if(yearValue!=0&&monthValue==0){
			return year[yearValue];
		}
		if(yearValue!=0&&monthValue!=0){
			return y=year[yearValue]+month[monthValue];
		}
		return null;
	}
	public int getAgeByMonth(){
		return yearValue*12+monthValue;
	}

}
