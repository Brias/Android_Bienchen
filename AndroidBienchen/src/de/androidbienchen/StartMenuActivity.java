package de.androidbienchen;

import java.util.ArrayList;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;




public class StartMenuActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (savedInstanceState == null) {
            getFragmentManager().beginTransaction()
                    .add(R.id.container, new PlaceholderFragment())
                    .commit();
        }
        init();
    }

    void init(){
    	LocationDatabase db = new LocationDatabase(this);
    	db.open();
    	
    	Weight weight = new Weight(1.5f, "2014/08/20");
    	Weight weight2 = new Weight(2.0f, "2014/08/24");
    	Weight weight3 = new Weight(1.7f, "2014/08/22");
    	
    	db.insertSizeValue(weight);
    	db.insertSizeValue(weight2);
    	db.insertSizeValue(weight3);
    	
    	ArrayList<Weight> weights = db.getWeights();
    	Log.d("ArrayListLenght",""+weights.size());
    	
    	db.removeOldestWeight();
    	ArrayList<Weight> newWeights = db.getWeights();
    	Log.d("ArrayListSize", ""+newWeights.size());
    	db.close();
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

}
