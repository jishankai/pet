package com.aidigame.hisun.pet.huanxin;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Hashtable;
import java.util.List;

import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.aidigame.hisun.pet.PetApplication;
import com.aidigame.hisun.pet.R;
import com.aidigame.hisun.pet.bean.MyUser;
import com.aidigame.hisun.pet.constant.Constants;
import com.aidigame.hisun.pet.ui.Dialog4Activity;
import com.aidigame.hisun.pet.ui.Dialog6Activity;
import com.aidigame.hisun.pet.ui.DialogNoteActivity;
import com.aidigame.hisun.pet.ui.HomeActivity;
import com.aidigame.hisun.pet.util.HandleHttpConnectionException;
import com.easemob.EMCallBack;
import com.easemob.chat.EMChatManager;
import com.easemob.chat.EMContact;
import com.easemob.chat.EMContactManager;
import com.easemob.chat.EMConversation;
import com.easemob.chat.EMGroup;
import com.easemob.chat.EMGroupManager;
import com.easemob.chat.EMMessage;
import com.easemob.exceptions.EaseMobException;

/**
 * 显示所有会话记录，比较简单的实现，更好的可能是把陌生人存入本地，这样取到的聊天记录是可控的
 * 
 */
public class ChatAllHistoryFragment extends Fragment {

	private InputMethodManager inputMethodManager;
	private ListView listView;
	private ChatAllHistoryAdapter adapter;
	private EditText query;
	private ImageButton clearSearch;
	public RelativeLayout errorItem;
	public TextView errorText;
	private boolean hidden;
	private List<EMGroup> groups;
	private List<EMConversation> conversationList;
	ArrayList<User> users;
    LinearLayout rooLayout;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		return inflater.inflate(R.layout.fragment_conversation_history, container, false);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		if(savedInstanceState != null && savedInstanceState.getBoolean("isConflict", false))
            return;
		inputMethodManager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
		errorItem = (RelativeLayout) getView().findViewById(R.id.rl_error_item);
		errorText = (TextView) errorItem.findViewById(R.id.tv_connect_errormsg);
		
		conversationList = loadConversationsWithRecentChat();
		loadUsersInfo();
		
		rooLayout=(LinearLayout)getView().findViewById(R.id.root_layout);
		BitmapFactory.Options options=new BitmapFactory.Options();
		options.inSampleSize=4;
		rooLayout.setBackgroundDrawable(new BitmapDrawable(BitmapFactory.decodeResource(getResources(), R.drawable.blur, options)));
		
		
		listView = (ListView) getView().findViewById(R.id.list);
		adapter = new ChatAllHistoryAdapter(getActivity(), 1, conversationList);
		// 设置adapter
		listView.setAdapter(adapter);
				
		groups = EMGroupManager.getInstance().getAllGroups();
        
		
		getView().findViewById(R.id.back).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				getActivity().finish();
			}
		});
		
	
		
		
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				EMConversation conversation = adapter.getItem(position);
				String username = conversation.getUserName();
				String nick="";
				String tx="";
				try {
					nick=conversation.getLastMessage().getStringAttribute("nickname");
					tx=conversation.getLastMessage().getStringAttribute("tx");
				} catch (EaseMobException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				if(PetApplication.myUser.u_nick.equals(nick)){
					try {
						nick=conversation.getLastMessage().getStringAttribute("other_nickname");
					} catch (EaseMobException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					try {
						tx=conversation.getLastMessage().getStringAttribute("other_tx");
					} catch (EaseMobException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				/*if (username.equals(com.aidigame.hisun.pet.PetApplication.getUserName()))
					Toast.makeText(getActivity(), "不能和自己聊天", 0).show();
				else {*/
					// 进入聊天页面
					Intent intent = new Intent(getActivity(), ChatActivity.class);
					/*EMContact emContact = null;
					groups = EMGroupManager.getInstance().getAllGroups();
					for (EMGroup group : groups) {
						if (group.getGroupId().equals(username)) {
							emContact = group;
							break;
						}
					}
					if (emContact != null && emContact instanceof EMGroup) {
						// it is group chat
						intent.putExtra("chatType", ChatActivity.CHATTYPE_GROUP);
						intent.putExtra("groupId", ((EMGroup) emContact).getGroupId());
					} else {*/
						// it is single chat
					MyUser user=new MyUser();
					user.userId=Integer.parseInt(username);
					user.u_nick=nick;
					user.u_iconUrl=tx;
						intent.putExtra("user", user);
					/*}*/
					startActivity(intent);
				/*}*/
			}
		});
		// 注册上下文菜单
		registerForContextMenu(listView);

		listView.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// 隐藏软键盘
				hideSoftKeyboard();
				return false;
			}

		});
		// 搜索框
		query = (EditText) getView().findViewById(R.id.query);
		// 搜索框中清除button
		clearSearch = (ImageButton) getView().findViewById(R.id.search_clear);
		query.addTextChangedListener(new TextWatcher() {
			public void onTextChanged(CharSequence s, int start, int before, int count) {

				adapter.getFilter().filter(s);
				if (s.length() > 0) {
					clearSearch.setVisibility(View.VISIBLE);
				} else {
					clearSearch.setVisibility(View.INVISIBLE);
				}
			}

			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
			}

			public void afterTextChanged(Editable s) {
			}
		});
		clearSearch.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				query.getText().clear();
				hideSoftKeyboard();
			}
		});
		
		
		if(!DemoHXSDKHelper.getInstance().isLogined()){
			Dialog4Activity.listener=new Dialog4Activity.Dialog3ActivityListener() {
				
				@Override
				public void onClose() {
					// TODO Auto-generated method stub
					
				}
				
				@Override
				public void onButtonTwo() {
					// TODO Auto-generated method stub
					EMChatManager.getInstance().login(""+PetApplication.myUser.userId, PetApplication.myUser.code, new EMCallBack() {
						
						@Override
						public void onSuccess() {
							// TODO Auto-generated method stub
							PetApplication.setUserName(""+PetApplication.myUser.userId);
	 						PetApplication.setPassword(PetApplication.myUser.code);
	 						Activity a=getActivity();
	 						if(a!=null)
	 						a.runOnUiThread(new Runnable() {
								
								@Override
								public void run() {
									// TODO Auto-generated method stub
									update();
								}
							});
	 						
						}
						
						@Override
						public void onProgress(int arg0, String arg1) {
							// TODO Auto-generated method stub
							
						}
						
						@Override
						public void onError(int arg0, String arg1) {
							// TODO Auto-generated method stub
							
						}
					});
				}
				
				@Override
				public void onButtonOne() {
					// TODO Auto-generated method stub
					
				}
			};
		    Intent intent=new Intent(getActivity(),Dialog4Activity.class);
		    intent.putExtra("mode", 6);
		    getActivity().startActivity(intent);
		}

	}
	public void update(){
		conversationList = loadConversationsWithRecentChat();
		if(conversationList!=null){
			adapter.upadte(new ArrayList<EMConversation>());;
			adapter.notifyDataSetChanged();
			adapter=new ChatAllHistoryAdapter(getActivity(), 1, conversationList);
			listView.setAdapter(adapter);
		}
		
		
	}

	private void loadUsersInfo() {/*
        // TODO Auto-generated method stub
	    
	    users=new ArrayList<User>();
	    
	    User user=null;
        for(int i=0;i<conversationList.size();i++){
            user=new User();
            user.setUsername(conversationList.get(i).getUserName());
        }
        if(users.size()>0){
            new Thread(new Runnable() {
                
                @Override
                public void run() {
                    // TODO Auto-generated method stub
                    String ids="";
                    if(users.size()==1){
                        ids=""+users.get(0);
                    }else{
                        for(int i=0;i<users.size();i++){
                            if(i==0){
                                ids=""+users.get(i);
                            }else{
                                ids+=","+users.get(i);
                            }
                        }
                    }
                    
                    String url ="http://"+"release4pet.aidigame.com"+"/index.php?"+"r=user/othersApi&";
                    DefaultHttpClient client = new DefaultHttpClient();
                    String value = "usr_ids=" + ids + "dog&cat";
                    String SIG = "";
                    try {
                        MessageDigest md5 = MessageDigest.getInstance("MD5");
                        md5.update(value.getBytes("UTF-8"));
                        byte[] data = md5.digest();
                        StringBuffer sb = new StringBuffer();
                        for (byte b : data) {
                            if ((b & 0xFF) < 0X10)
                                sb.append("0");
                            sb.append(Integer.toHexString(b & 0xFF));
                        }
                        SIG=sb.toString();
                    } catch (NoSuchAlgorithmException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    } catch (UnsupportedEncodingException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                    String param = "usr_ids=" + ids + "&sig=" + SIG + "&SID="
                            +Constants.SID ;
                    url = url + param;
                    boolean flag=false;
                    ArrayList<User> animalList=null;
                    HttpGet get = new HttpGet(url);
                    try {
                        String result =connect(client, handler, get);
                        LogUtil.i("me","获取一串用户信息的url="+url);
                        
                        JSONObject jsonObject=null;
                        if(!StringUtil.isEmpty(result)&&!"null".equals(result)&&!"false".equals(result)){
                            LogUtil.i("me", "获取一串用户信息的返回结果" + result);
                            UserJson json=new UserJson();
                            json.datas=new ArrayList<UserJson.Data>();
                            JSONObject object1=new JSONObject(result);
                            json.errorCode=object1.getInt("errorCode");
                            if(judgeSID(json.errorCode)){
                                return getOthersList(likers, handler, activity,mode);
                            }
                            json.errorMessage=object1.getString("errorMessage");
                            if(json.errorCode==0){
                                JSONArray array=object1.getJSONArray("data");
                                if(array!=null&&array.length()>0){
//                                  JSONArray array=object2.getJSONArray(0);
                                    if(array!=null&&array.length()>0){
                                        
                                         *{"usr_id":"231","name":"01","tx":"231_userHeadImage.png",
                                         *"city":"1000","gender":"1"}
                                         
                                        JSONObject object3=null;
                                        User user=null;
                                        animalList=new ArrayList<User>();
                                        for(int i=0;i<array.length();i++){
                                            object3=array.getJSONObject(i);
                                            user=new User();
                                            user.userId=object3.getInt("usr_id");
                                            user.u_nick=object3.getString("name");
                                            user.u_iconUrl=object3.getString("tx");
                                            user.locationCode=object3.getInt("city");
                                            user.u_gender=object3.getInt("gender");
                                           user.senderOrLiker=mode;
                                            String temp=""+user.locationCode;
                                              if(temp.length()==4){
                                                  int p=Integer.parseInt(temp.substring(0, 2));
                                                  user.province=AddressData.PROVINCES[p-10];
                                                  int p2=Integer.parseInt(temp.substring(2, 4));
                                                  user.city=AddressData.CITIES[p-10][p2];
                                              }else{
                                                  user.province=AddressData.PROVINCES[0];
                                                  user.city=AddressData.CITIES[0-10][0];
                                              }
                                              if(!animalList.contains(user))
                                            animalList.add(user);
                                         }
                                        return animalList;
                                    }
                                }
                            }
                            }
                    }catch (JSONException e) {
                        // TODO Auto-generated catch block
//                      if(ShowDialog.count==0)
//                      ShowDialog.show(Constants.NOTE_MESSAGE_2, activity);
                        e.printStackTrace();
                        if(handler!=null)
                            handler.sendEmptyMessage(HandleHttpConnectionException.Json_Data_Parse_Exception);
                    } 
                    return animalList;
                }
            }).start();
           
        }
    */}

    void hideSoftKeyboard() {
		if (getActivity().getWindow().getAttributes().softInputMode != WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN) {
			if (getActivity().getCurrentFocus() != null)
				inputMethodManager.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(),
						InputMethodManager.HIDE_NOT_ALWAYS);
		}
	}
	@Override
	public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
		super.onCreateContextMenu(menu, v, menuInfo);
		// if(((AdapterContextMenuInfo)menuInfo).position > 0){ m,
		getActivity().getMenuInflater().inflate(R.menu.delete_message1, menu);
		// }
	}

	@Override
	public boolean onContextItemSelected(MenuItem item) {
		if (item.getItemId() == R.id.delete_message) {
			EMConversation tobeDeleteCons = adapter.getItem(((AdapterContextMenuInfo) item.getMenuInfo()).position);
			// 删除此会话
			EMChatManager.getInstance().deleteConversation(tobeDeleteCons.getUserName(), tobeDeleteCons.isGroup());
			InviteMessgeDao inviteMessgeDao = new InviteMessgeDao(getActivity());
			inviteMessgeDao.deleteMessage(tobeDeleteCons.getUserName());
			adapter.remove(tobeDeleteCons);
			adapter.notifyDataSetChanged();

			// 更新消息未读数
			((MainActivity) getActivity()).updateUnreadLabel();

			return true;
		}else if(item.getItemId() == R.id.block_user){
			EMConversation tobeDeleteCons = adapter.getItem(((AdapterContextMenuInfo) item.getMenuInfo()).position);
			try {
				EMContactManager.getInstance().addUserToBlackList(tobeDeleteCons.getUserName(),true);
				Intent intent=new Intent(getActivity(),DialogNoteActivity.class);
				intent.putExtra("mode", 10);
				intent.putExtra("info", "拉黑成功");
				getActivity().startActivity(intent);
			} catch (EaseMobException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return super.onContextItemSelected(item);
	}

	/**
	 * 刷新页面
	 */
	public void refresh() {
		conversationList.clear();
		conversationList.addAll(loadConversationsWithRecentChat());
		adapter.notifyDataSetChanged();
	}

	/**
	 * 获取所有会话
	 * 
	 * @param context
	 * @return
	 */
	private List<EMConversation> loadConversationsWithRecentChat() {
		// 获取所有会话，包括陌生人
		Hashtable<String, EMConversation> conversations = EMChatManager.getInstance().getAllConversations();
		List<EMConversation> list = new ArrayList<EMConversation>();
		// 过滤掉messages seize为0的conversation
		for (EMConversation conversation : conversations.values()) {
			if (conversation.getAllMessages().size() != 0)
				list.add(conversation);
		}
		// 排序
		sortConversationByLastChatTime(list);
		return list;
	}

	/**
	 * 根据最后一条消息的时间排序
	 * 
	 * @param usernames
	 */
	private void sortConversationByLastChatTime(List<EMConversation> conversationList) {
		Collections.sort(conversationList, new Comparator<EMConversation>() {
			@Override
			public int compare(final EMConversation con1, final EMConversation con2) {

				EMMessage con2LastMessage = con2.getLastMessage();
				EMMessage con1LastMessage = con1.getLastMessage();
				if (con2LastMessage.getMsgTime() == con1LastMessage.getMsgTime()) {
					return 0;
				} else if (con2LastMessage.getMsgTime() > con1LastMessage.getMsgTime()) {
					return 1;
				} else {
					return -1;
				}
			}

		});
	}

	@Override
	public void onHiddenChanged(boolean hidden) {
		super.onHiddenChanged(hidden);
		this.hidden = hidden;
		if (!hidden) {
			refresh();
		}
	}

	@Override
	public void onResume() {
		super.onResume();
		if (!hidden && ! ((MainActivity)getActivity()).isConflict) {
			refresh();
		}
	}

	@Override
    public void onSaveInstanceState(Bundle outState) {
        if(((MainActivity)getActivity()).isConflict)
            outState.putBoolean("isConflict", true);
        super.onSaveInstanceState(outState);
        
    }

}
