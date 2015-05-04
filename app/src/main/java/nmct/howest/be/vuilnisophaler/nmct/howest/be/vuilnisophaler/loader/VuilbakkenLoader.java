package nmct.howest.be.vuilnisophaler.nmct.howest.be.vuilnisophaler.loader;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.provider.BaseColumns;
import android.util.JsonReader;
import android.util.JsonToken;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;

/**
 * Created by Glenn De Block on 4/05/2015.
 */
public class VuilbakkenLoader extends AsyncTaskLoader<Cursor> {

    private Cursor mCursor;
    private String url = "http://data.kortrijk.be/zwerfvuilbakjes/januari_2013.json";

    private static Object lock = new Object();
    private final String[] mColumnNames = new String[]
            {
                    BaseColumns._ID,
                    Contract.VuilbakColumns.COLUMN_VUILBAK_BARCODE,
                    Contract.VuilbakColumns.COLUMN_VUILBAK_CHIPNUMMER,
                    Contract.VuilbakColumns.COLUMN_VUILBAK_CHIPLOCATIE,
                    Contract.VuilbakColumns.COLUMN_VUILBAK_INTERCOMMUNALE,
                    Contract.VuilbakColumns.COLUMN_VUILBAK_ADRES,
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

    public VuilbakkenLoader(Context context) {
        super(context);
    }

    @Override
    protected void onStartLoading() {
        if (mCursor != null) {
            deliverResult(mCursor);
        }
        if (takeContentChanged() || mCursor == null) {
            forceLoad();
        }
    }

    @Override
    public Cursor loadInBackground() {
        if (mCursor == null) {
            loadCursor();
        }
        return mCursor;
    }

    private void loadCursor() {
        synchronized (lock) {
            if (mCursor != null) return;
            MatrixCursor cursor = new MatrixCursor(mColumnNames);

            //Code voor het ophalen van de data komt hier
            InputStream input = null;
            JsonReader reader = null;

            try {
                input = new URL(url).openStream();
                reader = new JsonReader(new InputStreamReader(input, "UTF-8"));

                int id = 1;
                reader.beginArray();
                while (reader.hasNext()) {
                    reader.beginObject();

                    String vuilbakBarcode = "";
                    String vuilbakChipnummer = "";
                    String vuilbakChiplocatie = "";
                    String vuilbakIntercommunale = "";

                    String vuilbakAdres = "";
                    String vuilbakGemeente = "";
                    String vuilbakVindbaarheid = "";
                    String vuilbakProductieplaats = "";
                    String vuilbakLocatie = "";
                    String vuilbakLocatietype = "";
                    String vuilbakCoordinaten = "";

                    String vuilbakKleur = "";
                    String vuilbakMerk = "";
                    String vuilbakVerhardingstype = "";
                    String vuilbakBevestiging = "";
                    String vuilbakGrondbuis = "";
                    String vuilbakInhoud = "";
                    String vuilbakMateriaal = "";
                    String vuilbakInwerpopening = "";
                    String vuilbakInwerpopeningen = "";
                    String vuilbakLedigingen = "";
                    String vuilbakVolume = "";
                    String vuilbakVullingsgraadTotaal = "";
                    String vuilbakVullingsgraadGemiddeld = "";
                    String vuilbakVullingsgraadMin = "";
                    String vuilbakVullingsgraadMax = "";
                    String vuilbakGemiddeldGewicht = "";

                    while (reader.hasNext()) {
                        String name = reader.nextName();

                        if (name.equals("Barcode")) {
                            vuilbakBarcode = reader.nextString();
                        } else if (name.equals("Chipnummer")) {
                            vuilbakChipnummer = reader.nextString();
                        } else if (name.equals("Locatie chip")) {
                            vuilbakChiplocatie = reader.nextString();
                        } else if (name.equals("Intercommunale")) {
                            vuilbakIntercommunale = reader.nextString();
                        } else if (name.equals("Adres/Plaats")) {
                            vuilbakAdres = reader.nextString();
                        } else if (name.equals("Gemeente/Stad")) {
                            vuilbakGemeente = reader.nextString();
                        } else if (name.equals("Vindbaarheid opvallend zichtbaar")) {
                            vuilbakVindbaarheid = reader.nextString();
                        } else if (name.equals("Gebruikersgemak plaats van productie afval")) {
                            vuilbakProductieplaats = reader.nextString();
                        } else if (name.equals("Locatie")) {
                            if (reader.peek().equals(JsonToken.NULL)) {
                                reader.skipValue();
                            } else if (reader.peek().equals(JsonToken.STRING)) {
                                vuilbakLocatie = reader.nextString();
                            }
                        } else if (name.equals("Locatiezone type")) {
                            vuilbakLocatietype = reader.nextString();
                        } else if (name.equals("GPS coordinaten")) {
                            if (reader.peek().equals(JsonToken.NULL)) {
                                reader.skipValue();
                            } else if (reader.peek().equals(JsonToken.STRING)) {
                                vuilbakCoordinaten = reader.nextString();
                            }
                        } else if (name.equals("Kleur")) {
                            vuilbakKleur = reader.nextString();
                        } else if (name.equals("Merk")) {
                            vuilbakMerk = reader.nextString();
                        } else if (name.equals("Verhardingstype")) {
                            vuilbakVerhardingstype = reader.nextString();
                        } else if (name.equals("Bevestiging")) {
                            vuilbakBevestiging = reader.nextString();
                        } else if (name.equals("Grondbuis")) {
                            vuilbakGrondbuis = reader.nextString();
                        } else if (name.equals("Inhoud")) {
                            vuilbakInhoud = reader.nextString();
                        } else if (name.equals("Materiaal")) {
                            vuilbakMateriaal = reader.nextString();
                        } else if (name.equals("Gebruikersgemak type inwerpopening")) {
                            vuilbakInwerpopening = reader.nextString();
                        } else if (name.equals("Gebruikersgemak aantal inwerpopeningen")) {
                            vuilbakInwerpopeningen = reader.nextString();
                        } else if (name.equals("Aantal ledigingen")) {
                            if (reader.peek().equals(JsonToken.NULL)) {
                                reader.skipValue();
                            } else if (reader.peek().equals(JsonToken.NUMBER)) {
                                vuilbakLedigingen = ""+reader.nextInt();
                            } else if (reader.peek().equals(JsonToken.STRING)) {
                                vuilbakLedigingen = reader.nextString();
                            }
                        } else if (name.equals("Volume")) {
                            if (reader.peek().equals(JsonToken.NULL)) {
                                reader.skipValue();
                            } else if (reader.peek().equals(JsonToken.NUMBER)) {
                                vuilbakVolume = ""+reader.nextDouble();
                            } else if (reader.peek().equals(JsonToken.STRING)) {
                                vuilbakVolume = reader.nextString();
                            }
                        } else if (name.equals("Vullingsgraad totaal")) {
                            if (reader.peek().equals(JsonToken.NULL)) {
                                reader.skipValue();
                            } else if (reader.peek().equals(JsonToken.NUMBER)) {
                                vuilbakVullingsgraadTotaal = ""+reader.nextDouble();
                            } else if (reader.peek().equals(JsonToken.STRING)) {
                                vuilbakVullingsgraadTotaal = reader.nextString();
                            }
                        } else if (name.equals("Vullingsgraad gemiddeld")) {
                            if (reader.peek().equals(JsonToken.NULL)) {
                                reader.skipValue();
                            } else if (reader.peek().equals(JsonToken.NUMBER)) {
                                vuilbakVullingsgraadGemiddeld = ""+reader.nextDouble();
                            } else if (reader.peek().equals(JsonToken.STRING)) {
                                vuilbakVullingsgraadGemiddeld = reader.nextString();
                            }
                        } else if (name.equals("Vullingsgraad min.")) {
                            if (reader.peek().equals(JsonToken.NULL)) {
                                reader.skipValue();
                            } else if (reader.peek().equals(JsonToken.NUMBER)) {
                                vuilbakVullingsgraadMin = ""+reader.nextDouble();
                            } else if (reader.peek().equals(JsonToken.STRING)) {
                                vuilbakVullingsgraadMin = reader.nextString();
                            }
                        } else if (name.equals("Vullingsgraad max.")) {
                            if (reader.peek().equals(JsonToken.NULL)) {
                                reader.skipValue();
                            } else if (reader.peek().equals(JsonToken.NUMBER)) {
                                vuilbakVullingsgraadMax = ""+reader.nextDouble();
                            } else if (reader.peek().equals(JsonToken.STRING)) {
                                vuilbakVullingsgraadMax = reader.nextString();
                            }
                        } else if (name.equals("Gemiddeld gewicht")) {
                            if (reader.peek().equals(JsonToken.NULL)) {
                                reader.skipValue();
                            } else if (reader.peek().equals(JsonToken.NUMBER)) {
                                vuilbakGemiddeldGewicht = ""+reader.nextDouble();
                            } else if (reader.peek().equals(JsonToken.STRING)) {
                                vuilbakGemiddeldGewicht = reader.nextString();
                            }
                        } else {
                            reader.skipValue();
                        }
                    }

                    MatrixCursor.RowBuilder row = cursor.newRow();
                    row.add(id);
                    row.add(vuilbakBarcode);
                    row.add(vuilbakChipnummer);
                    row.add(vuilbakChiplocatie);
                    row.add(vuilbakIntercommunale);

                    row.add(vuilbakAdres);
                    row.add(vuilbakGemeente);
                    row.add(vuilbakVindbaarheid);
                    row.add(vuilbakProductieplaats);
                    row.add(vuilbakLocatie);
                    row.add(vuilbakLocatietype);
                    row.add(vuilbakCoordinaten);

                    row.add(vuilbakKleur);
                    row.add(vuilbakMerk);
                    row.add(vuilbakVerhardingstype);
                    row.add(vuilbakBevestiging);
                    row.add(vuilbakGrondbuis);
                    row.add(vuilbakInhoud);
                    row.add(vuilbakMateriaal);
                    row.add(vuilbakInwerpopening);
                    row.add(vuilbakInwerpopeningen);
                    row.add(vuilbakLedigingen);
                    row.add(vuilbakVolume);
                    row.add(vuilbakVullingsgraadTotaal);
                    row.add(vuilbakVullingsgraadGemiddeld);
                    row.add(vuilbakVullingsgraadMin);
                    row.add(vuilbakVullingsgraadMax);
                    row.add(vuilbakGemiddeldGewicht);

                    id++;

                    reader.endObject();
                }
                reader.endArray();
                mCursor = cursor;
            } catch (IOException ex) {
                ex.printStackTrace();
            } catch (Exception ex) {
                //Zelf toegevoegd om fout met openStream(); te analyseren
                //VERGEET NIET in android-manifest
                //<uses-permission android:name="android.permission.INTERNET"/>
                ex.printStackTrace();
            } finally {
                try {
                    reader.close();
                } catch (IOException ex) {

                }
                try {
                    input.close();
                } catch (IOException ex) {

                }
            }
        }
    }
}
