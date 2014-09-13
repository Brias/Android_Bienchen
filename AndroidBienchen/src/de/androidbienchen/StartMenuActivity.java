package de.androidbienchen;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;




public class StartMenuActivity extends AbstractNavDrawerActivity  {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (savedInstanceState == null) {
            getFragmentManager().beginTransaction()
                    .add(R.id.container, new PlaceholderFragment())
                    .commit();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_main, container, false);
            return rootView;
        }
    }

    @Override
    protected NavDrawerActivityConfiguration getNavDrawerConfiguration() {
        
        NavDrawerItem[] menu = new NavDrawerItem[] {
                NavMenuItem.create(1,"Start Menu", "navdrawer_start", false, this),
                NavMenuItem.create(2, "Chat", "navdrawer_chat", false, this), 
                NavMenuItem.create(3, "Kalender", "navdrawer_calendar", false, this),
                NavMenuItem.create(4, "Statistik", "navdrawer_statistics", false, this), 
                NavMenuItem.create(5, "Live-Cam", "navdrawer_cam", true, this),
                NavMenuItem.create(6, "Website", "navdrawer_webite", false, this),};
        
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
    	Intent intent;
    	switch ((int)id) {
        case 1:
            intent = new Intent(this, PresenceStatusActivity.class);
            startActivity(intent);
            break;
        case 2:
            intent = new Intent(this, ChatActivity.class);
            startActivity(intent);
            break;
        case 3:
            intent = new Intent(this, CalendarActivity.class);
            startActivity(intent);
            break;
        case 4:
            intent = new Intent(this, ScaleActivity.class);
            startActivity(intent);
            break;
        case 5:
            intent = new Intent(this, CamActivity.class);
            startActivity(intent);
            break;
        case 6:
            intent = new Intent(this, WebsiteLinkActivity.class);
            startActivity(intent);
            break;
        }
    }

}
