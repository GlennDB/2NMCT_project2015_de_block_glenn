package nmct.howest.be.vuilnisophaler;


import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class VuilnisOphalerFragment extends Fragment implements LocationListener {

    LocationManager locationManager ;
    String provider;

    TextView textViewStreetNumber;
    TextView textViewZipcodeCity;
    TextView textViewCountry;
    Button buttonShowLijst;

    String longitude = "";
    String latitude = "";

    private OnVuilnisOphalerFragmentListener onVuilnisOphalerFragmentListener;

    public interface OnVuilnisOphalerFragmentListener{
        public void onButtonShowListClicked(String latitude, String longitude);
    }

    public VuilnisOphalerFragment() {
        // Required empty public constructor
    }

    public static VuilnisOphalerFragment newInstance() {
        VuilnisOphalerFragment fragment = new VuilnisOphalerFragment();
        return fragment;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            onVuilnisOphalerFragmentListener = (OnVuilnisOphalerFragmentListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnVuilnisOphalerFragmentListener");
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Getting LocationManager object
        locationManager = (LocationManager)getActivity().getSystemService(Context.LOCATION_SERVICE);

        // Creating an empty criteria object
        Criteria criteria = new Criteria();

        // Getting the name of the provider that meets the criteria
        provider = locationManager.getBestProvider(criteria, false);

        if(provider!=null && !provider.equals(""))
        {
            // Get the location from the given provider
            Location location = locationManager.getLastKnownLocation(provider);
            locationManager.requestLocationUpdates(provider, 500, (float)0.01, this);

            if(location!=null)
            {
                onLocationChanged(location);
            }
            else
            {
                Toast.makeText(getActivity().getBaseContext(), "Location can't be retrieved", Toast.LENGTH_SHORT).show();
            }
        }
        else
        {
            Toast.makeText(getActivity(), "No Provider Found", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_vuilnis_ophaler, container, false);

        textViewStreetNumber = (TextView) v.findViewById(R.id.textViewStreetNumber);
        textViewZipcodeCity = (TextView) v.findViewById(R.id.textViewZipcodeCity);
        textViewCountry = (TextView) v.findViewById(R.id.textViewCountry);

        buttonShowLijst = (Button) v.findViewById(R.id.buttonViewList);
        buttonShowLijst.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToShowListFragment();
            }
        });

        return v;
    }

    private void goToShowListFragment() {
        onVuilnisOphalerFragmentListener.onButtonShowListClicked(latitude, longitude);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        onVuilnisOphalerFragmentListener = null;
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onLocationChanged(Location location) {

        // Getting user its longitude/ latitude
        longitude = String.valueOf(location.getLongitude());
        latitude = String.valueOf(location.getLatitude());

        // Getting user its address
        String address = null;
        String city = null;
        String country = null;
        List<Address> addresses;
        Geocoder mGeocoder = new Geocoder(getActivity().getApplicationContext(), Locale.getDefault());
        try {
            addresses = mGeocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);

            address = addresses.get(0).getAddressLine(0);
            city = addresses.get(0).getAddressLine(1);
            country = addresses.get(0).getAddressLine(2);

        } catch (IOException e) {
            e.printStackTrace();
        }

        if(address == null){address="Onbekende straat en nummer!";}
        if(city == null){city="Onbekende postcode en stad!";}
        if(country == null){country="Onbekend land!";}

        if(textViewCountry!=null || textViewZipcodeCity!=null || textViewStreetNumber!=null)
        {
            textViewStreetNumber.setText(address);
            textViewZipcodeCity.setText(city);
            textViewCountry.setText(country);
        }
    }

    @Override
    public void onProviderDisabled(String provider) {
        // TODO Auto-generated method stub
    }

    @Override
    public void onProviderEnabled(String provider) {
        // TODO Auto-generated method stub
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        // TODO Auto-generated method stub
    }

}
