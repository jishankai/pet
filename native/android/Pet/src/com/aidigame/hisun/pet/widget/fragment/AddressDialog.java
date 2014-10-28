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

public class AddressDialog {
	NumberPicker provicePicker;
	NumberPicker cityPicker;
	ArrayList<String> provincesList;
	ArrayList<String> cityList;
	int provinceValue=0;
	int cityValue=0;
	String province;
	String city;
	TextView provinceTV;
	
	
	Context context;
	View view;
	public AddressDialog(Context context){
		this.context=context;
		view=LayoutInflater.from(context).inflate(R.layout.widget_address_picker, null);
		provicePicker=(NumberPicker)view.findViewById(R.id.numberpicker1);
		cityPicker=(NumberPicker)view.findViewById(R.id.numberpicker2);
		provinceTV=(TextView)view.findViewById(R.id.province_tv);
		
		provincesList=new ArrayList<String>();
		for(int i=0;i<AddressData.PROVINCES.length;i++){
			provincesList.add(AddressData.PROVINCES[i]);
		}
		cityList=new ArrayList<String>();
		for(int i=0;i<AddressData.CITIES[0].length;i++){
			cityList.add(AddressData.CITIES[0][i]);
		}
		provicePicker.setDisplayedValues(AddressData.PROVINCES);
		provicePicker.setMaxValue(AddressData.PROVINCES.length-1);
		provicePicker.setMinValue(0);
		provicePicker.setOnValueChangedListener(new OnValueChangeListener() {
			
			@Override
			public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
				// TODO Auto-generated method stub
				cityPicker.setValue(0);
				provinceValue=newVal;
				province=AddressData.PROVINCES[newVal];
				
				cityPicker.setDisplayedValues(AddressData.CITIES[newVal]);
				cityPicker.setMaxValue(AddressData.CITIES[newVal].length-1);
				cityPicker.setMinValue(0);
				
				
				city=AddressData.CITIES[newVal][0];
				provinceTV.setText(province);
			}
		});
		cityPicker.setDisplayedValues(AddressData.CITIES[0]);
		cityPicker.setMaxValue(AddressData.CITIES[0].length-1);
		cityPicker.setMinValue(0);
		cityPicker.setOnValueChangedListener(new OnValueChangeListener() {
			
			@Override
			public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
				// TODO Auto-generated method stub
				cityValue=newVal;
				city=AddressData.CITIES[provinceValue][newVal];
			}
		});
		
		province=AddressData.PROVINCES[0];
		city=AddressData.CITIES[0][0];
		provinceTV.setText(province);
	}
	public View getView(){
		return view;
	}
	public String getAddress(){
		return province=AddressData.PROVINCES[provinceValue]+"|"+AddressData.CITIES[provinceValue][cityValue];
		
	}
	public String getAddressCode(){
		String string="";
		if(cityValue<10){
			string="0"+cityValue;
		}else{
			string=""+cityValue;
		}
		return ""+(10+provinceValue)+string;
		
	}

}
