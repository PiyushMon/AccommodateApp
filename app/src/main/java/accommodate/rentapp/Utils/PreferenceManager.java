package accommodate.rentapp.Utils;

import static android.content.Context.MODE_PRIVATE;

import android.content.SharedPreferences;


public class PreferenceManager {

    public static SharedPreferences sharedPreferences;
    public static SharedPreferences.Editor editor;

    public static void init() {
        sharedPreferences = AppClassData.getContext().getSharedPreferences("AccommodateLoacalSave", MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }

    public static void editor(String key, String value) {

        editor.putString(key, value);
        editor.apply();
    }

    public static void editor(String key, int value) {

        editor.putInt(key, value);
        editor.apply();
    }

    public static void editor(String key, boolean value) {

        editor.putBoolean(key, value);
        editor.apply();
    }

    public static void editor(String key, long value) {

        editor.putLong(key, value);
        editor.apply();
    }

    public static void remove(String key) {

        editor.remove(key);
        editor.apply();
    }

    public static void clear() {

        editor.clear();
        editor.apply();
    }

    public static boolean getUserFristTime() {
        return sharedPreferences.getBoolean("UserFristTime", false);
    }

    public static boolean getUserisLogin() {
        return sharedPreferences.getBoolean("UserisLogin", false);
    }

    public static String getUsername() {
        return sharedPreferences.getString(Datakey.USERNAME, "guest");
    }

    public static String getUserEmail() {
        return sharedPreferences.getString(Datakey.USEREMAIL, "");
    }

    public static String getMobileNumber() {
        return sharedPreferences.getString(Datakey.MobileNumber, "");
    }

    public static String getUserprofile() {
        return sharedPreferences.getString(Datakey.USERProfile, "");
    }

    public static String getSelectHouseType() {
        return sharedPreferences.getString(Datakey.SelectHouseType, "Property owner");
    }

    public static Boolean getSelectHouseTypeSelected() {
        return sharedPreferences.getBoolean(Datakey.SelectHouseTypeSelected, false);
    }

}
