package nmct.howest.be.vuilnisophaler;


import android.app.ListFragment;
import android.app.LoaderManager;
import android.content.Context;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import nmct.howest.be.vuilnisophaler.nmct.howest.be.vuilnisophaler.loader.Contract;
import nmct.howest.be.vuilnisophaler.nmct.howest.be.vuilnisophaler.loader.VuilbakkenLoader;


public class VuilbakListFragment extends ListFragment implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final String ARG_LATITUDE="Latitude";
    private static final String ARG_LONGITUDE="Longitude";

    private VuilbakAdapter vAdapter;

    public VuilbakListFragment() {
        // Required empty public constructor
    }

    public static VuilbakListFragment newInstance(String altitude, String longitude) {
        VuilbakListFragment fragment = new VuilbakListFragment();
        if(!altitude.equals("")&&!longitude.equals("")) {
            Bundle args = new Bundle();
            args.putString(ARG_LATITUDE, altitude);
            args.putString(ARG_LONGITUDE, longitude);
            fragment.setArguments(args);
        }
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_vuilbak_list, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        String[] columns = new String[]{Contract.VuilbakColumns.COLUMN_VUILBAK_ADRES, Contract.VuilbakColumns.COLUMN_VUILBAK_LOCATIE,
                Contract.VuilbakColumns.COLUMN_VUILBAK_COORDINATEN};
        int[] viewIds = new int[]{R.id.textViewAdres, R.id.textViewLocatie, R.id.textViewAfstand};

        vAdapter = new VuilbakAdapter(getActivity(), R.layout.row_vuilbak, null, columns, viewIds, 0);
        setListAdapter(vAdapter);

        getLoaderManager().initLoader(0, null, this);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new VuilbakkenLoader(getActivity());
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        vAdapter.swapCursor(cursor);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        vAdapter.swapCursor(null);
    }

    class VuilbakAdapter extends SimpleCursorAdapter {
        public VuilbakAdapter(Context context, int layout, Cursor c, String[] from, int[] to, int flags){
            super(context, layout, c, from, to, flags);
        }

        @Override
        public void bindView(View view, Context context, Cursor cursor) {
            super.bindView(view, context, cursor);

            View row = view;

            ViewHolder holder = (ViewHolder) row.getTag();

            if (holder == null){
                holder = new ViewHolder(row);
                row.setTag(holder);
            }

            TextView textViewAdres = holder.textViewAdres;
            TextView textViewLocatie = holder.textViewLocatie;
            TextView textViewAfstand = holder.textViewAfstand;

            int colnr = cursor.getColumnIndex(Contract.VuilbakColumns.COLUMN_VUILBAK_ADRES);
            String adres = cursor.getString(colnr);
            adres = adres.replace(" - ", " ");
            textViewAdres.setText(adres);

            int colnr2 = cursor.getColumnIndex(Contract.VuilbakColumns.COLUMN_VUILBAK_LOCATIE);
            String locatie = cursor.getString(colnr2);
            if(locatie.equals("")){locatie="/";}
            textViewLocatie.setText(locatie);

            int colnr3 = cursor.getColumnIndex(Contract.VuilbakColumns.COLUMN_VUILBAK_COORDINATEN);
            String coordinaten;

            try {
                coordinaten = cursor.getString(colnr3);
                String[] LatLong;

                if (coordinaten.contains(", ")) {
                    LatLong = coordinaten.split(", ");
                    LatLong[0] = LatLong[0].replace(",", ".");
                    LatLong[1] = LatLong[1].replace(",", ".");
                } else {
                    LatLong = coordinaten.split(",");
                }
                double dist = distance(Double.valueOf(LatLong[0]), Double.valueOf(LatLong[1]), Double.valueOf(getArguments().get(ARG_LATITUDE).toString()), Double.valueOf(getArguments().get(ARG_LONGITUDE).toString()), "K");
                double factor = 1e3; // = 1 * 10^5 = 100000.
                dist = Math.round(dist * factor) / factor;
                textViewAfstand.setText("" + dist + "km");
            }
            catch(Exception ex){
                textViewAfstand.setText("/");
            }
        }

        class ViewHolder {
            public TextView textViewAdres = null;
            public TextView textViewLocatie = null;
            public TextView textViewAfstand = null;

            public ViewHolder(View row) {
                this.textViewAdres = (TextView) row.findViewById(R.id.textViewAdres);
                this.textViewLocatie = (TextView) row.findViewById(R.id.textViewLocatie);
                this.textViewAfstand = (TextView) row.findViewById(R.id.textViewAfstand);
            }
        }

        private double distance(double lat1, double lon1, double lat2, double lon2, String unit) {
            double theta = lon1 - lon2;
            double dist = Math.sin(deg2rad(lat1)) * Math.sin(deg2rad(lat2)) + Math.cos(deg2rad(lat1)) * Math.cos(deg2rad(lat2)) * Math.cos(deg2rad(theta));
            dist = Math.acos(dist);
            dist = rad2deg(dist);
            dist = dist * 60 * 1.1515;
            if (unit.equals("K")) {
                dist = dist * 1.609344;
            } else if (unit.equals("N")) {
                dist = dist * 0.8684;
            }
            return (dist);
        }

        private double deg2rad(double deg) {
            return (deg * Math.PI / 180.0);
        }

        private double rad2deg(double rad) {
            return (rad * 180 / Math.PI);
        }
    }
}