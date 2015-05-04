package nmct.howest.be.vuilnisophaler;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.List;
import java.util.Locale;


public class VuilnisOphalerActivity extends Activity implements VuilnisOphalerFragment.OnVuilnisOphalerFragmentListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vuilnis_ophaler);

        if(savedInstanceState == null){
            //Initial start
            //To manage the fragments in your activity, you need to use FragmentManager
            FragmentManager fragmentManager = getFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            VuilnisOphalerFragment vuilnisOphalerFragment = VuilnisOphalerFragment.newInstance();

            //parameters:
            //1: ID container
            //2: fragment
            //3: Optional tag name for the fragment, to later retrieve the fragment with FragmentManager.findFragmentByTag(String).
            fragmentTransaction.add(R.id.container, vuilnisOphalerFragment, "vuilnisOphalerFragment");
            fragmentTransaction.commit();

            setTitle("Vuilnis Ophaler");
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_vuilnis_ophaler, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return super.onOptionsItemSelected(item);
    }

    private void showVuilbakListFragment(String latitude, String longitude) {

        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        VuilbakListFragment vuilbakListFragment = VuilbakListFragment.newInstance(latitude, longitude);
        fragmentTransaction.replace(R.id.container, vuilbakListFragment);

        //Add this transaction to the back stack. This means that the transaction will be remembered
        //after it is committed, and will reverse its operation when later popped off the stack.
        //name: An optional name for this back stack state, or null.
        fragmentTransaction.addToBackStack("showVuilbakListFragment");
        fragmentTransaction.commit();

        setTitle("Selecteer een vuilbak: ");
    }

    @Override
    public void onButtonShowListClicked(String latitude, String longitude) { showVuilbakListFragment(latitude, longitude); }

}
