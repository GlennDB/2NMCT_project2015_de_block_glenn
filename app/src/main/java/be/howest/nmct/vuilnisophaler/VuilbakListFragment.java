package be.howest.nmct.vuilnisophaler;


import android.app.Activity;
import android.app.ListFragment;
import android.app.LoaderManager;
import android.app.SearchManager;
import android.content.Context;
import android.content.Loader;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.os.Bundle;
import android.provider.BaseColumns;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import nmct.howest.be.vuilnisophaler.R;
import be.howest.nmct.vuilnisophaler.loader.Contract;
import be.howest.nmct.vuilnisophaler.loader.VuilbakkenLoader;


public class VuilbakListFragment extends ListFragment implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final String ARG_LATITUDE="Latitude";
    private static final String ARG_LONGITUDE="Longitude";

    private Cursor mCursor;

    private VuilbakAdapter vAdapter;
    private OnVuilbakListFragmentListener vListener;

    public VuilbakListFragment() {
        // Required empty public constructor
    }

    public static VuilbakListFragment newInstance(String latitude, String longitude) {
        VuilbakListFragment fragment = new VuilbakListFragment();
        if(!latitude.equals("")&&!longitude.equals("")) {
            Bundle args = new Bundle();
            args.putString(ARG_LATITUDE, latitude);
            args.putString(ARG_LONGITUDE, longitude);
            fragment.setArguments(args);
        }
        return fragment;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            vListener = (OnVuilbakListFragmentListener) activity;
        } catch (ClassCastException ex) {
            throw new ClassCastException(activity.toString() + " must implement OnStudentsFragmentListener");
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);


        // Inflate the menu; this adds items to the action bar if it is present.
        inflater.inflate(R.menu.menu_vuilbakken, menu);

        // Get the SearchView and set the searchable configuration
        SearchManager searchManager = (SearchManager) getActivity().getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) menu.findItem(R.id.menu_search).getActionView();
        // Assumes current activity is the searchable activity
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getActivity().getComponentName()));
        searchView.setIconifiedByDefault(true); // Do not iconify the widget; expand it by default

        SearchView.OnQueryTextListener queryTextListener = new SearchView.OnQueryTextListener() {
            public boolean onQueryTextChange(String newText) {
                // Filter adapter
                if(!newText.equals("")) {

                    Cursor filteredCursor  = filterCursorOnStreet(newText);
                    vAdapter.swapCursor(filteredCursor);
                    setListAdapter(vAdapter);
                }

                return true;
            }

            public boolean onQueryTextSubmit(String query) {

                Toast.makeText(getActivity().getApplicationContext(), query, Toast.LENGTH_SHORT).show();

                return true;
            }
        };
        searchView.setOnQueryTextListener(queryTextListener);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        getActivity().findViewById(R.id.container).setPadding(32,32,32,32);
        return inflater.inflate(R.layout.fragment_vuilbak_list, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        String[] columns = new String[]{Contract.VuilbakColumns.COLUMN_VUILBAK_ADRES, Contract.VuilbakColumns.COLUMN_VUILBAK_LOCATIE,
                Contract.VuilbakColumns.COLUMN_VUILBAK_COORDINATEN, Contract.VuilbakColumns.COLUMN_VUILBAK_KLEUR};
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

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        Cursor c = (Cursor)vAdapter.getItem(position);
        if (vListener!=null) vListener.onSelectVuilbak(c);
    }

    class VuilbakAdapter extends SimpleCursorAdapter {
        public VuilbakAdapter(Context context, int layout, Cursor c, String[] from, int[] to, int flags){
            super(context, layout, c, from, to, flags);
        }

        @Override
        public void bindView(View view, Context context, Cursor cursor) {
            super.bindView(view, context, cursor);

            mCursor = cursor;

            View row = view;

            ViewHolder holder = (ViewHolder) row.getTag();

            if (holder == null){
                holder = new ViewHolder(row);
                row.setTag(holder);
            }

            TextView textViewAdres = holder.textViewAdres;
            TextView textViewLocatie = holder.textViewLocatie;
            TextView textViewAfstand = holder.textViewAfstand;
            ImageView imageViewColorVuilbak = holder.imageViewColorVuilbak;

            int colnr = cursor.getColumnIndex(Contract.VuilbakColumns.COLUMN_VUILBAK_ADRES);
            String adres = cursor.getString(colnr);
            adres = adres.replace(" - ", " ");
            adres = adres.replace(" -", " ");
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
                    if(coordinaten.contains(" "))
                    {
                        LatLong = coordinaten.split(" ");
                        LatLong[0] = LatLong[0].replace(" ", "");
                        LatLong[1] = LatLong[1].replace(" ", "");
                    }
                    else {
                        LatLong = coordinaten.split(",");
                    }
                }
                double dist = distance(Double.valueOf(LatLong[0]), Double.valueOf(LatLong[1]), Double.valueOf(getArguments().get(ARG_LATITUDE).toString()), Double.valueOf(getArguments().get(ARG_LONGITUDE).toString()), "K");
                double factor = 1e3; // = 1 * 10^5 = 100000.
                dist = Math.round(dist * factor) / factor;
                textViewAfstand.setText("" + dist + "km");
            }
            catch(Exception ex){
                textViewAfstand.setText("/");
            }

            int colnr4 = cursor.getColumnIndex(Contract.VuilbakColumns.COLUMN_VUILBAK_KLEUR);
            String kleur = cursor.getString(colnr4);

            switch(kleur)
            {
                case "groen": imageViewColorVuilbak.setImageResource(R.drawable.vuilbak_groen);
                    break;
                case "oranje": imageViewColorVuilbak.setImageResource(R.drawable.vuilbak_oranje);
                    break;
                case "inox": imageViewColorVuilbak.setImageResource(R.drawable.vuilbak_inox);
                    break;
                case "grijs": imageViewColorVuilbak.setImageResource(R.drawable.vuilbak_grijs);
                    break;
                default: imageViewColorVuilbak.setImageResource(R.drawable.vuilbak_onbekend);
            }
        }

        class ViewHolder {
            public TextView textViewAdres = null;
            public TextView textViewLocatie = null;
            public TextView textViewAfstand = null;
            public ImageView imageViewColorVuilbak = null;

            public ViewHolder(View row) {
                this.textViewAdres = (TextView) row.findViewById(R.id.textViewAdres);
                this.textViewLocatie = (TextView) row.findViewById(R.id.textViewLocatie);
                this.textViewAfstand = (TextView) row.findViewById(R.id.textViewAfstand);
                this.imageViewColorVuilbak = (ImageView) row.findViewById(R.id.imageViewColorVuilbak);
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

    public interface OnVuilbakListFragmentListener {
        public void onSelectVuilbak(Cursor vuilbak);
    }

    private Cursor filterCursorOnStreet(String straat){
        String[] mColumnNames = new String[]{
                BaseColumns._ID,
                Contract.VuilbakColumns.COLUMN_VUILBAK_ADRES,
                Contract.VuilbakColumns.COLUMN_VUILBAK_LOCATIE,
                Contract.VuilbakColumns.COLUMN_VUILBAK_COORDINATEN,
                Contract.VuilbakColumns.COLUMN_VUILBAK_KLEUR,
                Contract.VuilbakColumns.COLUMN_VUILBAK_CHIPNUMMER,
                Contract.VuilbakColumns.COLUMN_VUILBAK_CHIPLOCATIE,
                Contract.VuilbakColumns.COLUMN_VUILBAK_INTERCOMMUNALE,
                Contract.VuilbakColumns.COLUMN_VUILBAK_GEMEENTE,
                Contract.VuilbakColumns.COLUMN_VUILBAK_VINDBAARHEID,
                Contract.VuilbakColumns.COLUMN_VUILBAK_PRODUCTIEPLAATS,
                Contract.VuilbakColumns.COLUMN_VUILBAK_LOCATIE,
                Contract.VuilbakColumns.COLUMN_VUILBAK_LOCATIETYPE,
                Contract.VuilbakColumns.COLUMN_VUILBAK_COORDINATEN,
                Contract.VuilbakColumns.COLUMN_VUILBAK_KLEUR,
                Contract.VuilbakColumns.COLUMN_VUILBAK_MERK,
                Contract.VuilbakColumns.COLUMN_VUILBAK_VERHARDINGSTYPE,
                Contract.VuilbakColumns.COLUMN_VUILBAK_BEVESTIGING,
                Contract.VuilbakColumns.COLUMN_VUILBAK_GRONDBUIS,
                Contract.VuilbakColumns.COLUMN_VUILBAK_INHOUD,
                Contract.VuilbakColumns.COLUMN_VUILBAK_MATERIAAL,
                Contract.VuilbakColumns.COLUMN_VUILBAK_INWERPOPENING,
                Contract.VuilbakColumns.COLUMN_VUILBAK_INWERPOPENINGEN,
                Contract.VuilbakColumns.COLUMN_VUILBAK_LEDIGINGEN,
                Contract.VuilbakColumns.COLUMN_VUILBAK_VOLUME,
                Contract.VuilbakColumns.COLUMN_VUILBAK_VULLINGSGRAAD_TOTAAL,
                Contract.VuilbakColumns.COLUMN_VUILBAK_VULLINGSGRAAD_GEMIDDELD,
                Contract.VuilbakColumns.COLUMN_VUILBAK_VULLINGSGRAAD_MIN,
                Contract.VuilbakColumns.COLUMN_VUILBAK_VULLINGSGRAAD_MAX,
                Contract.VuilbakColumns.COLUMN_VUILBAK_GEMIDDELD_GEWICHT
        };

        MatrixCursor newCursor = new MatrixCursor(mColumnNames);
        int colnr1 = mCursor.getColumnIndex(Contract.VuilbakColumns.COLUMN_VUILBAK_ADRES);
        int colnr2 = mCursor.getColumnIndex(Contract.VuilbakColumns.COLUMN_VUILBAK_LOCATIE);
        int colnr3 = mCursor.getColumnIndex(Contract.VuilbakColumns.COLUMN_VUILBAK_COORDINATEN);
        int colnr4 = mCursor.getColumnIndex(Contract.VuilbakColumns.COLUMN_VUILBAK_KLEUR);
        int colnr5 = mCursor.getColumnIndex(Contract.VuilbakColumns.COLUMN_VUILBAK_CHIPNUMMER);
        int colnr6 = mCursor.getColumnIndex(Contract.VuilbakColumns.COLUMN_VUILBAK_CHIPLOCATIE);
        int colnr7 = mCursor.getColumnIndex(Contract.VuilbakColumns.COLUMN_VUILBAK_INTERCOMMUNALE);
        int colnr8 = mCursor.getColumnIndex(Contract.VuilbakColumns.COLUMN_VUILBAK_GEMEENTE);
        int colnr9 = mCursor.getColumnIndex(Contract.VuilbakColumns.COLUMN_VUILBAK_VINDBAARHEID);
        int colnr10 = mCursor.getColumnIndex(Contract.VuilbakColumns.COLUMN_VUILBAK_PRODUCTIEPLAATS);
        int colnr11 = mCursor.getColumnIndex(Contract.VuilbakColumns.COLUMN_VUILBAK_LOCATIE);
        int colnr12 = mCursor.getColumnIndex(Contract.VuilbakColumns.COLUMN_VUILBAK_LOCATIETYPE);
        int colnr13 = mCursor.getColumnIndex(Contract.VuilbakColumns.COLUMN_VUILBAK_COORDINATEN);
        int colnr14 = mCursor.getColumnIndex(Contract.VuilbakColumns.COLUMN_VUILBAK_KLEUR);
        int colnr15 = mCursor.getColumnIndex(Contract.VuilbakColumns.COLUMN_VUILBAK_MERK);
        int colnr16 = mCursor.getColumnIndex(Contract.VuilbakColumns.COLUMN_VUILBAK_VERHARDINGSTYPE);
        int colnr17 = mCursor.getColumnIndex(Contract.VuilbakColumns.COLUMN_VUILBAK_BEVESTIGING);
        int colnr18 = mCursor.getColumnIndex(Contract.VuilbakColumns.COLUMN_VUILBAK_GRONDBUIS);
        int colnr19 = mCursor.getColumnIndex(Contract.VuilbakColumns.COLUMN_VUILBAK_INHOUD);
        int colnr20 = mCursor.getColumnIndex(Contract.VuilbakColumns.COLUMN_VUILBAK_MATERIAAL);
        int colnr21 = mCursor.getColumnIndex(Contract.VuilbakColumns.COLUMN_VUILBAK_INWERPOPENING);
        int colnr22 = mCursor.getColumnIndex(Contract.VuilbakColumns.COLUMN_VUILBAK_INWERPOPENINGEN);
        int colnr23 = mCursor.getColumnIndex(Contract.VuilbakColumns.COLUMN_VUILBAK_LEDIGINGEN);
        int colnr24 = mCursor.getColumnIndex(Contract.VuilbakColumns.COLUMN_VUILBAK_VOLUME);
        int colnr25 = mCursor.getColumnIndex(Contract.VuilbakColumns.COLUMN_VUILBAK_VULLINGSGRAAD_TOTAAL);
        int colnr26 = mCursor.getColumnIndex(Contract.VuilbakColumns.COLUMN_VUILBAK_VULLINGSGRAAD_GEMIDDELD);
        int colnr27 = mCursor.getColumnIndex(Contract.VuilbakColumns.COLUMN_VUILBAK_VULLINGSGRAAD_MIN);
        int colnr28 = mCursor.getColumnIndex(Contract.VuilbakColumns.COLUMN_VUILBAK_VULLINGSGRAAD_MAX);
        int colnr29 = mCursor.getColumnIndex(Contract.VuilbakColumns.COLUMN_VUILBAK_GEMIDDELD_GEWICHT);


        int id = 1;
        if(mCursor.moveToFirst()) {
            do {
                if (mCursor.getString(colnr1).toLowerCase().contains(straat.toLowerCase().trim())) {
                    MatrixCursor.RowBuilder row = newCursor.newRow();
                    row.add(id++);
                    row.add(mCursor.getString(colnr1)); //Adres
                    row.add(mCursor.getString(colnr2)); //Locatie
                    row.add(mCursor.getString(colnr3)); //Coordinaten
                    row.add(mCursor.getString(colnr4)); //Kleur
                    row.add(mCursor.getString(colnr5));
                    row.add(mCursor.getString(colnr6));
                    row.add(mCursor.getString(colnr7));
                    row.add(mCursor.getString(colnr8));
                    row.add(mCursor.getString(colnr9));
                    row.add(mCursor.getString(colnr10));
                    row.add(mCursor.getString(colnr11));
                    row.add(mCursor.getString(colnr12));
                    row.add(mCursor.getString(colnr13));
                    row.add(mCursor.getString(colnr14));
                    row.add(mCursor.getString(colnr15));
                    row.add(mCursor.getString(colnr16));
                    row.add(mCursor.getString(colnr17));
                    row.add(mCursor.getString(colnr18));
                    row.add(mCursor.getString(colnr19));
                    row.add(mCursor.getString(colnr20));
                    row.add(mCursor.getString(colnr21));
                    row.add(mCursor.getString(colnr22));
                    row.add(mCursor.getString(colnr23));
                    row.add(mCursor.getString(colnr24));
                    row.add(mCursor.getString(colnr25));
                    row.add(mCursor.getString(colnr26));
                    row.add(mCursor.getString(colnr27));
                    row.add(mCursor.getString(colnr28));
                    row.add(mCursor.getString(colnr29));

                }
            }
            while (mCursor.moveToNext());
        }

        return newCursor;
    }
}
