package be.howest.nmct.vuilnisophaler;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.database.Cursor;
import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.gms.maps.MapFragment;

import nmct.howest.be.vuilnisophaler.R;
import be.howest.nmct.vuilnisophaler.loader.Contract;


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

    private void showMapFragment(String lat, String lon){
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        Fragment vuilbakMapFragment = VuilbakMapFragment.newInstance(lat, lon);
        fragmentTransaction.replace(R.id.container, vuilbakMapFragment);

        //Add this transaction to the back stack. This means that the transaction will be remembered
        //after it is committed, and will reverse its operation when later popped off the stack.
        //name: An optional name for this back stack state, or null.
        fragmentTransaction.addToBackStack("showMapFragment");
        fragmentTransaction.commit();

        setTitle("Kaart");
    }

    @Override
    public void onButtonShowListClicked(String latitude, String longitude) { showVuilbakListFragment(latitude, longitude); }
    public void onButtonShowMapClicked(String lat, String lon) { showMapFragment(lat, lon); }
    public void onSelectVuilbak(Cursor c) { showVuilbakDetailsFragment(c); }

}
