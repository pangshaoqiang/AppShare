package com.appshare.base;

import android.app.Application;
import android.graphics.drawable.Drawable;

public class AppTrans extends Application {
    private String value;
    private Drawable icon;
    
    public void setValue(String value) {
        this.value = value;
    }
    
    public void setIcon(Drawable drawable){
    	this.icon=drawable;
    }
    
    public Drawable getIcon(){
    	
    	return icon;
    }
    public String getValue() {
        
    	return value;
    }

	private String share_app_name;
	private String share_name;
	private String brief_intro;
	private Drawable app_icon;
	private Drawable share_icon;
	private String detail_intro;
    private String share_id;

	public void setAppName(String app_name) {
		this.share_app_name = app_name;
	}

	public void setShareName(String share_name) {
		this.share_name = share_name;
	}

	public void setBriefIntro(String brief_intro) {
		this.brief_intro = brief_intro;
	}

    public void setDetailIntro(String detail_intro) {
        this.detail_intro = detail_intro;
    }

	public void setAppIcon(Drawable drawable) {
		this.app_icon = drawable;
	}

	public void setShareIcon(Drawable drawable) {
		this.share_icon = drawable;
	}

    public void setShareId(String share_id) {
        this.share_id = share_id;
    }

	public String getAppName() {

		return share_app_name;
	}

	public String getShareName() {

		return share_name;
	}

	public String getBriefIntro() {

        return brief_intro;
    }

    public String getDetailIntro() {

        return detail_intro;
    }

    public String getShareId() {

        return share_id;
    }
	public Drawable getShareIcon() {

		return share_icon;
	}

	public Drawable getAppIcon() {
		
		return app_icon;
	}
	
	private String friend_name;
	private String friend_phone;
    private String sign;
	private Drawable friend_icon;
    private String friend_id;

	
	public void setFriendName(String friend_name){
		this.friend_name=friend_name;
	}
	public void setFriendPhone(String friend_phone){
		this.friend_phone=friend_phone;
	}
	public void setFriendIcon(Drawable friend_icon){this.friend_icon=friend_icon;}
    public void setSign(String sign){
        this.sign=sign;
    }
    public void setFriendId(String friend_id){
        this.friend_id=friend_id;
    }

	public String getFriendName(){
		return this.friend_name;
	}
	public String getFriendphone(){
		return this.friend_phone;
	}
    public String getSign() {return this.sign;}
    public String getFriendId() {return this.friend_id;}
	public Drawable getFriendIcon(){
		return this.friend_icon;
	}
}
