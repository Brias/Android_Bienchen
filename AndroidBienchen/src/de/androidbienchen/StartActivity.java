package de.androidbienchen;

import android.os.Bundle;


public class StartActivity extends AbstractNavDrawerActivity {
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_main);
        if ( savedInstanceState == null ) {
        	PresenceStatusActivity status = new PresenceStatusActivity();
        	getFragmentManager().beginTransaction().replace(R.id.content_frame, status).commit();
        }
    }
    
    @Override
    protected NavDrawerActivityConfiguration getNavDrawerConfiguration() {
        
        NavDrawerItem[] menu = new NavDrawerItem[] {
                NavMenuItem.create(1,"Start", "navdrawer_start", true, this),
                NavMenuItem.create(2, "Chat", "navdrawer_chat", true, this), 
                NavMenuItem.create(3, "Kalender", "navdrawer_calendar", true, this),
                NavMenuItem.create(4, "Statistik", "navdrawer_statistic", true, this), 
                NavMenuItem.create(5, "Live-Cam", "navdrawer_live_cam", true, this),
                NavMenuItem.create(6, "Website", "navdrawer_website", true, this)};
        
        NavDrawerActivityConfiguration navDrawerActivityConfiguration = new NavDrawerActivityConfiguration();
        navDrawerActivityConfiguration.setMainLayout(R.layout.activity_main);
        navDrawerActivityConfiguration.setDrawerLayoutId(R.id.drawer_layout);
        navDrawerActivityConfiguration.setLeftDrawerId(R.id.left_drawer);
        navDrawerActivityConfiguration.setNavItems(menu);
        navDrawerActivityConfiguration.setDrawerShadow(R.drawable.ic_launcher);    			//CHANGE PICTURE       
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
        	PresenceStatusActivity status = new PresenceStatusActivity();
        	getFragmentManager().beginTransaction().replace(R.id.content_frame, status).commit();
            break;
        case 2:
        	ChatActivity chat = new ChatActivity();
        	getFragmentManager().beginTransaction().replace(R.id.content_frame, chat).commit();
            break;
	    case 3:
	    	CalendarActivity calendar = new CalendarActivity();
        	getFragmentManager().beginTransaction().replace(R.id.content_frame, calendar).commit();
	        break;
	    case 4:
	    	ScaleActivity scale = new ScaleActivity(this);
        	getFragmentManager().beginTransaction().replace(R.id.content_frame, scale).commit();
	        break;
	    case 5:
	    	CamActivity cam = new CamActivity();
        	getFragmentManager().beginTransaction().replace(R.id.content_frame, cam).commit();
	        break;
	    case 6:
	    	WebsiteLinkActivity website = new WebsiteLinkActivity();
        	getFragmentManager().beginTransaction().replace(R.id.content_frame, website).commit();
	        break;
	    }
    }
}
