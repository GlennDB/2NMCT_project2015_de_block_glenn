package nmct.howest.be.vuilnisophaler.nmct.howest.be.vuilnisophaler.loader;

import android.provider.BaseColumns;

/**
 * Created by Glenn De Block on 4/05/2015.
 */
public class Contract {
    public interface VuilbakColumns extends BaseColumns {

        public static final String COLUMN_VUILBAK_BARCODE = "vuilbak_barcode";
        public static final String COLUMN_VUILBAK_CHIPNUMMER = "vuilbak_chipnummer";
        public static final String COLUMN_VUILBAK_CHIPLOCATIE = "vuilbak_chiplocatie";
        public static final String COLUMN_VUILBAK_INTERCOMMUNALE = "vuilbak_intercommunale";

        public static final String COLUMN_VUILBAK_ADRES = "vuilbak_ades";
        public static final String COLUMN_VUILBAK_GEMEENTE = "vuilbak_gemeente";

        public static final String COLUMN_VUILBAK_VINDBAARHEID = "vuilbak_vindbaarheid";
        public static final String COLUMN_VUILBAK_PRODUCTIEPLAATS = "vuilbak_productieplaats";
        public static final String COLUMN_VUILBAK_LOCATIE = "vuilbak_locatie";
        public static final String COLUMN_VUILBAK_LOCATIETYPE = "vuilbak_locatietype";
        public static final String COLUMN_VUILBAK_COORDINATEN = "vuilbak_coordinaten";

        public static final String COLUMN_VUILBAK_KLEUR= "vuilbak_kleur";
        public static final String COLUMN_VUILBAK_MERK = "vuilbak_merk";
        public static final String COLUMN_VUILBAK_VERHARDINGSTYPE = "vuilbak_verhardingstype";
        public static final String COLUMN_VUILBAK_BEVESTIGING = "vuilbak_bevestiging";
        public static final String COLUMN_VUILBAK_GRONDBUIS = "vuilbak_grondbuis";
        public static final String COLUMN_VUILBAK_INHOUD = "vuilbak_inhoud";
        public static final String COLUMN_VUILBAK_MATERIAAL = "vuilbak_materiaal";
        public static final String COLUMN_VUILBAK_INWERPOPENING = "vuilbak_inwerpopening";
        public static final String COLUMN_VUILBAK_INWERPOPENINGEN = "vuilbak_inwerpopeningen";

        public static final String COLUMN_VUILBAK_LEDIGINGEN = "vuilbak_ledigingen";
        public static final String COLUMN_VUILBAK_VOLUME = "vuilbak_volume";
        public static final String COLUMN_VUILBAK_VULLINGSGRAAD_TOTAAL = "vuilbak_vullingsgraad_totaal";
        public static final String COLUMN_VUILBAK_VULLINGSGRAAD_GEMIDDELD = "vuilbak_vullingsgraad_gemiddeld";
        public static final String COLUMN_VUILBAK_VULLINGSGRAAD_MIN = "vuilbak_vullingsgraad_min";
        public static final String COLUMN_VUILBAK_VULLINGSGRAAD_MAX = "vuilbak_vullingsgraad_max";
        public static final String COLUMN_VUILBAK_GEMIDDELD_GEWICHT = "vuilbak_gewicht";

    }
}
