package be.howest.nmct.vuilnisophaler;


import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.InputStream;

import nmct.howest.be.vuilnisophaler.R;
import be.howest.nmct.vuilnisophaler.loader.Contract;


public class VuilbakDetailsFragment extends Fragment {

    private static String chipLocatie;
    private static String interCommunale;

    private static String adres;
    private static String gemeente;
    private static String vindbaarheid;
    private static String productiePlaats;
    private static String locatie;

    private static String locatieType;
    private static String coordinaten;

    private static String kleur;
    private static String merk;
    private static String verhardingsType;
    private static String bevestiging;
    private static String grondBuis;
    private static String inhoud;
    private static String materiaal;
    private static String inwerpOpening;
    private static String inwerpOpeningen;

    private static String ledigingen;
    private static String volume;
    private static String vullingsGraadTotaal;
    private static String vullingsGraadMin;
    private static String vullingsGraadGemiddeld;
    private static String vullingsGraadMax;


    private TextView textViewAdres;
    private TextView textViewLocatie;
    private TextView textViewChipLocatie;
    private TextView textViewKleur;
    private TextView textViewLedigingen;
    private TextView textViewMerk;
    private TextView textViewVerharding;
    private TextView textViewBevestiging;
    private TextView textviewGrondbuis;
    private TextView textviewInhoud;
    private TextView textViewKenmerkenTitle;
    private TextView textViewOpening;
    private TextView textViewOpeningen;
    private TextView textViewVolume;
    private TextView textViewGem;
    private TextView textViewTot;
    private TextView textViewMin;
    private TextView textViewMax;

    private ImageView imageViewColor;

    public VuilbakDetailsFragment() {
        // Required empty public constructor
    }

    public static VuilbakDetailsFragment newInstance(Cursor c) {
        VuilbakDetailsFragment fragment = new VuilbakDetailsFragment();
        if(c!=null) {
            chipLocatie = c.getString(c.getColumnIndex(Contract.VuilbakColumns.COLUMN_VUILBAK_CHIPLOCATIE));
            interCommunale = c.getString(c.getColumnIndex(Contract.VuilbakColumns.COLUMN_VUILBAK_INTERCOMMUNALE));

            adres = c.getString(c.getColumnIndex(Contract.VuilbakColumns.COLUMN_VUILBAK_ADRES));
            gemeente = c.getString(c.getColumnIndex(Contract.VuilbakColumns.COLUMN_VUILBAK_GEMEENTE));
            vindbaarheid = c.getString(c.getColumnIndex(Contract.VuilbakColumns.COLUMN_VUILBAK_VINDBAARHEID));
            productiePlaats = c.getString(c.getColumnIndex(Contract.VuilbakColumns.COLUMN_VUILBAK_PRODUCTIEPLAATS));
            locatie = c.getString(c.getColumnIndex(Contract.VuilbakColumns.COLUMN_VUILBAK_LOCATIE));
            locatieType = c.getString(c.getColumnIndex(Contract.VuilbakColumns.COLUMN_VUILBAK_LOCATIETYPE));
            coordinaten = c.getString(c.getColumnIndex(Contract.VuilbakColumns.COLUMN_VUILBAK_COORDINATEN));

            kleur = c.getString(c.getColumnIndex(Contract.VuilbakColumns.COLUMN_VUILBAK_KLEUR));
            merk = c.getString(c.getColumnIndex(Contract.VuilbakColumns.COLUMN_VUILBAK_MERK));
            verhardingsType = c.getString(c.getColumnIndex(Contract.VuilbakColumns.COLUMN_VUILBAK_VERHARDINGSTYPE));
            bevestiging = c.getString(c.getColumnIndex(Contract.VuilbakColumns.COLUMN_VUILBAK_BEVESTIGING));
            grondBuis = c.getString(c.getColumnIndex(Contract.VuilbakColumns.COLUMN_VUILBAK_GRONDBUIS));
            inhoud = c.getString(c.getColumnIndex(Contract.VuilbakColumns.COLUMN_VUILBAK_INHOUD));
            materiaal = c.getString(c.getColumnIndex(Contract.VuilbakColumns.COLUMN_VUILBAK_MATERIAAL));
            inwerpOpening = c.getString(c.getColumnIndex(Contract.VuilbakColumns.COLUMN_VUILBAK_INWERPOPENING));
            inwerpOpeningen = c.getString(c.getColumnIndex(Contract.VuilbakColumns.COLUMN_VUILBAK_INWERPOPENINGEN));

            ledigingen = c.getString(c.getColumnIndex(Contract.VuilbakColumns.COLUMN_VUILBAK_LEDIGINGEN));
            volume = c.getString(c.getColumnIndex(Contract.VuilbakColumns.COLUMN_VUILBAK_VOLUME));
            vullingsGraadTotaal = c.getString(c.getColumnIndex(Contract.VuilbakColumns.COLUMN_VUILBAK_VULLINGSGRAAD_TOTAAL));
            vullingsGraadMin = c.getString(c.getColumnIndex(Contract.VuilbakColumns.COLUMN_VUILBAK_VULLINGSGRAAD_MIN));
            vullingsGraadGemiddeld = c.getString(c.getColumnIndex(Contract.VuilbakColumns.COLUMN_VUILBAK_VULLINGSGRAAD_GEMIDDELD));
            vullingsGraadMax = c.getString(c.getColumnIndex(Contract.VuilbakColumns.COLUMN_VUILBAK_VULLINGSGRAAD_MAX));
        }
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_vuilbak_details, container, false);

        textViewAdres = (TextView) v.findViewById(R.id.textViewAdres);
        textViewLocatie = (TextView) v.findViewById(R.id.textViewLocatie);
        textViewChipLocatie = (TextView) v.findViewById(R.id.textViewChipLocatie);
        textViewKleur = (TextView) v.findViewById(R.id.textViewKleur);
        textViewLedigingen = (TextView) v.findViewById(R.id.textViewLedigingen);
        textViewMerk = (TextView) v.findViewById(R.id.textViewMerk);
        textViewVerharding = (TextView) v.findViewById(R.id.textViewVerharding);
        textViewBevestiging = (TextView) v.findViewById(R.id.textViewBevestiging);
        textviewGrondbuis = (TextView) v.findViewById(R.id.textViewGrondbuis);
        textviewInhoud = (TextView) v.findViewById(R.id.textViewInhoud);
        textViewKenmerkenTitle = (TextView) v.findViewById(R.id.textViewkenmerkenTitle);
        textViewVolume = (TextView) v.findViewById(R.id.textViewVolume);
        textViewOpening = (TextView) v.findViewById(R.id.textViewOpening);
        textViewOpeningen = (TextView) v.findViewById(R.id.textViewOpeningen);
        textViewMin = (TextView) v.findViewById(R.id.textViewMin);
        textViewGem = (TextView) v.findViewById(R.id.textViewGem);
        textViewMax = (TextView) v.findViewById(R.id.textViewMax);
        textViewTot = (TextView) v.findViewById(R.id.textViewTot);
        imageViewColor = (ImageView) v.findViewById(R.id.imageViewColor);

        setValueTo(textViewAdres, adres+" ("+gemeente+" - "+interCommunale+")");
        if(productiePlaats.equals(""))productiePlaats = "Ongekend";
        setValueTo(textViewLocatie, locatieType + " - " + locatie + " ("+productiePlaats+") "+vindbaarheid.toUpperCase()+" ZICHTBAAR");
        chipLocatie = firstCharToUpper(chipLocatie);
        setValueTo(textViewChipLocatie, chipLocatie);
        setValueTo(textViewLedigingen, ledigingen);
        merk = firstCharToUpper(merk);
        setValueTo(textViewMerk, merk);
        verhardingsType = firstCharToUpper(verhardingsType);
        setValueTo(textViewVerharding, verhardingsType);
        bevestiging = firstCharToUpper(bevestiging);
        setValueTo(textViewBevestiging, bevestiging);
        grondBuis = firstCharToUpper(grondBuis);
        setValueTo(textviewGrondbuis, grondBuis);
        setValueTo(textviewInhoud, inhoud);
        if(materiaal.equals("")) materiaal = "Ongekend materiaal";
        materiaal = firstCharToUpper(materiaal);
        setValueTo(textViewKenmerkenTitle, "Kenmerken ("+materiaal+")");
        if(inwerpOpeningen.equals("")) inwerpOpeningen = "? openingen:";
        setValueTo(textViewOpeningen, inwerpOpeningen+":");
        setValueTo(textViewVolume, volume);
        inwerpOpening = firstCharToUpper(inwerpOpening);
        setValueTo(textViewOpening, inwerpOpening);
        setValueTo(textViewGem, vullingsGraadGemiddeld);
        setValueTo(textViewTot, vullingsGraadTotaal);
        setValueTo(textViewMin, vullingsGraadMin);
        setValueTo(textViewMax, vullingsGraadMax);

        try {
            String[] LatLong;

            if (coordinaten.contains(", ")) {
                LatLong = coordinaten.split(", ");
                LatLong[0] = LatLong[0].replace(",", ".");
                LatLong[1] = LatLong[1].replace(",", ".");
            } else {
                LatLong = coordinaten.split(",");
            }


                new DownloadImageTask((ImageView) v.findViewById(R.id.imageViewMap))
                        .execute("https://maps.googleapis.com/maps/api/staticmap?center=" + LatLong[0] + "," + LatLong[1] + "&zoom=17&size=475x275&scale=2" +
                                "&markers=color:green%7Clabel:YOU%7C" + LatLong[0] + "," + LatLong[1]);

        }
        catch(Exception ex) {

        }

        switch(kleur)
        {
            case "groen": imageViewColor.setImageResource(R.drawable.vuilbak_groen);
                textViewKleur.setText("Groen");
                break;
            case "oranje": imageViewColor.setImageResource(R.drawable.vuilbak_oranje);
                textViewKleur.setText("Oranje");
                break;
            case "inox": imageViewColor.setImageResource(R.drawable.vuilbak_inox);
                textViewKleur.setText("Inox");
                break;
            case "grijs": imageViewColor.setImageResource(R.drawable.vuilbak_grijs);
                textViewKleur.setText("Grijs");
                break;
            default: imageViewColor.setImageResource(R.drawable.vuilbak_onbekend);
                textViewKleur.setText("Ongekend");
        }

        return v;
    }

    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;

        public DownloadImageTask(ImageView bmImage) {
            this.bmImage = bmImage;
        }

        protected Bitmap doInBackground(String... urls) {
            String urldisplay = urls[0];
            Bitmap mIcon11 = null;
            try {
                InputStream in = new java.net.URL(urldisplay).openStream();
                mIcon11 = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return mIcon11;
        }

        protected void onPostExecute(Bitmap result) {
            bmImage.setImageBitmap(result);
        }
    }

    private void setValueTo(TextView textView, String string)
    {
        if(string.equals(""))
        {
            string = "/";
        }
        textView.setText(string);
    }

    private String firstCharToUpper(String lijn)
    {
        if(!lijn.equals("")) lijn  =  lijn.substring(0,1).toUpperCase() + lijn.substring(1);
        return lijn;
    }

}
