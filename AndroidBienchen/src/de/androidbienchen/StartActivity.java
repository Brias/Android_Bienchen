package de.androidbienchen;

import android.app.Fragment;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;


public class StartActivity extends AbstractNavDrawerActivity {
    
	private CamActivity cam;
	private ScaleActivity scale;
	private ChatActivity chat;
	private PresenceStatusActivity presence;
	private CalendarActivity calendar;
	
	private Fragment current;
	private CalendarActivity currentCalendar;
	
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        if ( savedInstanceState == null ) {
        	init();
        	setCurrent(presence);
        	addFragments();
        	hideFragments();
        }
    }
    
    void init(){
    	current = null;
    	currentCalendar = null;
    	cam = new CamActivity();
        scale = new ScaleActivity();
        calendar = new CalendarActivity();
        chat = new ChatActivity();
        presence = new PresenceStatusActivity();
    }
    
    void addFragments(){
    	getFragmentManager().beginTransaction().add(R.id.content_frame, cam).commit();
        getFragmentManager().beginTransaction().add(R.id.content_frame, scale).commit();
        getSupportFragmentManager().beginTransaction().add(R.id.content_frame,	calendar).commit();
        getFragmentManager().beginTransaction().add(R.id.content_frame, chat).commit();
        getFragmentManager().beginTransaction().add(R.id.content_frame,	presence).commit();
    }
    
    void hideFragments(){
    	getFragmentManager().beginTransaction().hide(cam).commit();
    	getFragmentManager().beginTransaction().hide(scale).commit();
    	getSupportFragmentManager().beginTransaction().hide(calendar).commit();
    	getFragmentManager().beginTransaction().hide(chat).commit();
    }
    
    @Override
    protected NavDrawerActivityConfiguration getNavDrawerConfiguration() {
        
        NavDrawerItem[] menu = new NavDrawerItem[] {
                NavMenuItem.create(1,"Start", "navdrawer_start", true, this),
                NavMenuItem.create(2, "Chat", "navdrawer_chat", true, this), 
                NavMenuItem.create(3, "Kalender", "navdrawer_calendar", true, this),
                NavMenuItem.create(4, "Statistik", "navdrawer_statistic", true, this), 
                NavMenuItem.create(5, "Live-Cam", "navdrawer_live_cam", true, this),
                NavMenuItem.create(6, "zur Website", "navdrawer_website", true, this)};
        
        NavDrawerActivityConfiguration navDrawerActivityConfiguration = new NavDrawerActivityConfiguration();
        navDrawerActivityConfiguration.setMainLayout(R.layout.activity_main);
        navDrawerActivityConfiguration.setDrawerLayoutId(R.id.drawer_layout);
        navDrawerActivityConfiguration.setLeftDrawerId(R.id.left_drawer);
        navDrawerActivityConfiguration.setNavItems(menu);
        navDrawerActivityConfiguration.setDrawerShadow(R.drawable.red_border_gray_bg);    			//CHANGE PICTURE       
        navDrawerActivityConfiguration.setDrawerOpenDesc(R.string.drawer_open);
        navDrawerActivityConfiguration.setDrawerCloseDesc(R.string.drawer_close);
        navDrawerActivityConfiguration.setBaseAdapter(
            new NavDrawerAdapter(this, R.layout.navdrawer_item, menu ));
        return navDrawerActivityConfiguration;
    }
    
    @Override
    protected void onNavItemSelected(int id) {
    	
    	switch ((int)id) {
        case 1:
        	changeViewTo(presence);
            break;
        case 2:
        	changeViewTo(chat);
            break;
	    case 3:
	    	changeViewTo(calendar);
	        break;
	    case 4:
	    	changeViewTo(scale);
	        break;
	    case 5:
	    	changeViewTo(cam);
	        break;
	    case 6:
	    	startWebsite();
	        break;
	    }
    }
    
    void showClicked(Fragment clicked){
    	getFragmentManager().beginTransaction().show(clicked).commit();
    }
    
    void showClicked(CalendarActivity clicked){
    	getSupportFragmentManager().beginTransaction().show(clicked).commit();
    }
    
    void hideCurrent(){
    	if(current != null){
    		getFragmentManager().beginTransaction().hide(current).commit();
    	}else{
    		getSupportFragmentManager().beginTransaction().hide(currentCalendar).commit();
    	}
    }
    
    void setCurrent(Fragment next){
    	current = next;
    	currentCalendar = null;
    }
    
    void setCurrent(CalendarActivity calendar){
    	currentCalendar = calendar;
    	current = null;
    }
    
    void changeViewTo(Fragment next){
    	hideCurrent();
    	showClicked(next);
    	setCurrent(next);
    }
    
    void changeViewTo(CalendarActivity next){
    	hideCurrent();
    	showClicked(next);
    	setCurrent(next);
    }
    
    void startWebsite(){
		Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(AppConfig.server.WEBSITE_URL));
		startActivity(browserIntent);
	}
}