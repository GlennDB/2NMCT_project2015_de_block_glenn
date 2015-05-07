package be.howest.nmct.vuilnisophaler;


import android.app.FragmentTransaction;
import android.app.LoaderManager;
import android.content.Context;
import android.content.Loader;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.LinearLayout;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Document;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import be.howest.nmct.vuilnisophaler.loader.Contract;
import be.howest.nmct.vuilnisophaler.loader.VuilbakkenLoader;
import nmct.howest.be.vuilnisophaler.R;


public class VuilbakMapFragment extends Fragment implements OnMapReadyCallback{

    private MapFragment mMapFragment;

    public VuilbakMapFragment() {
        // Required empty public constructor
    }

    public static VuilbakMapFragment newInstance() {
        VuilbakMapFragment fragment = new VuilbakMapFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mMapFragment = MapFragment.newInstance();
        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        fragmentTransaction.add(R.id.map, mMapFragment);
        fragmentTransaction.commit();

        mMapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        LatLng dikkelvenne = new LatLng(50.916667, 3.683333);

        googleMap.setMyLocationEnabled(true);
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(dikkelvenne, 13));

        googleMap.addMarker(new MarkerOptions()
                .title("Dikkelvenne")
                .snippet("City of Dikkelvenne")
                .position(dikkelvenne));

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_vuilbak_map, container, false);
        new loadmore().execute();

        getActivity().findViewById(R.id.container).setPadding(0,0,0,0);

        return v;
    }

    @Override
    public void onStop() {
        super.onStop();
        getActivity().findViewById(R.id.container).setPadding(16,16,16,16);
    }

    public class loadmore extends AsyncTask<String, Integer, ArrayList<String>> {

        ArrayList<String> getCoordinates = new ArrayList<String>();

        @Override
        protected ArrayList<String> doInBackground(
                String... params) {
            try {
                URL url = new URL(
                        "http://data.kortrijk.be/zwerfvuilbakjes/januari_2013.json");
                URLConnection tc = url.openConnection();
                BufferedReader in = new BufferedReader(new InputStreamReader(
                        tc.getInputStream()));

                String line;
                while ((line = in.readLine()) != null) {
                    JSONArray ja = new JSONArray(line);

                    for (int i = 0; i < ja.length(); i++) {
                        JSONObject jo = (JSONObject) ja.get(i);
                        getCoordinates.add(jo.getString("GPS coordinaten"));
                    }
                }
            } catch (MalformedURLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(ArrayList<String> result) {
           Log.d("Size",getCoordinates.size()+"");
            ArrayList<String> test = new ArrayList<String>();
           for(int i=0 ; i < 500 ; i++)
           {
               String[] LatLong;

               if(getCoordinates.get(i).equals(""))
               {
                   Log.d("Mislukt: ",""+i);
               }
               else if (getCoordinates.get(i).contains(", ")) {
                   LatLong = getCoordinates.get(i).split(", ");
                   LatLong[0] = LatLong[0].replace(",", ".");
                   LatLong[1] = LatLong[1].replace(",", ".");

                   Log.d("VORM1: ",""+i);
               } else {
                   if (getCoordinates.get(i).contains(" ")) {
                       LatLong = getCoordinates.get(i).split(" ");
                       LatLong[0] = LatLong[0].replace(" ", "");
                       LatLong[1] = LatLong[1].replace(" ", "");
                       Log.d("VORM2: : ",""+i);
                   } else {
                       LatLong = getCoordinates.get(i).split(",");
                       Log.d("VORM3: ",""+i);
                   }
               }
           }
        }
    }
}
