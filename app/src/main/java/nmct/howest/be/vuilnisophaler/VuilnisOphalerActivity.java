package nmct.howest.be.vuilnisophaler;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.SearchManager;
import android.content.Context;
import android.database.Cursor;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import nmct.howest.be.vuilnisophaler.nmct.howest.be.vuilnisophaler.loader.Contract;


public class VuilnisOphalerActivity extends Activity implements VuilnisOphalerFragment.OnVuilnisOphalerFragmentListener, VuilbakListFragment.OnVuilbakListFragmentListener {

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

    private void showVuilbakDetailsFragment(Cursor c) {

        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        VuilbakDetailsFragment vuilbakDetailsFragment = VuilbakDetailsFragment.newInstance(c);
        fragmentTransaction.replace(R.id.container, vuilbakDetailsFragment);

        //Add this transaction to the back stack. This means that the transaction will be remembered
        //after it is committed, and will reverse its operation when later popped off the stack.
        //name: An optional name for this back stack state, or null.
        fragmentTransaction.addToBackStack("showVuilbakDetailsFragment");
        fragmentTransaction.commit();

        setTitle("Vuilbak chip nr. "+c.getString(c.getColumnIndex(Contract.VuilbakColumns.COLUMN_VUILBAK_CHIPNUMMER)));
    }

    @Override
    public void onButtonShowListClicked(String latitude, String longitude) { showVuilbakListFragment(latitude, longitude); }
    public void onSelectVuilbak(Cursor c) { showVuilbakDetailsFragment(c); }

}
