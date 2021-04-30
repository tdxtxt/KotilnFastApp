package com.fast.libdeveloper;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;


final class Prefers {
  @SuppressLint("StaticFieldLeak") private static Prefers singleton;
  private Context context;

  private Prefers(Context context) {
    this.context = context.getApplicationContext();
  }

  /**
   * Prefer file: shared preference file, save, read
   * or remove value via this class.
   */
  final static class PreferFile {
    SharedPreferences sp;

    private PreferFile(SharedPreferences sp) {
      this.sp = sp;
    }

    void save(String key, int value) {
      sp.edit().putInt(key, value).apply();
    }

    void save(String key, long value) {
      sp.edit().putLong(key, value).apply();
    }

    void save(String key, float value) {
      sp.edit().putFloat(key, value).apply();
    }

    void save(String key, boolean value) {
      sp.edit().putBoolean(key, value).apply();
    }

    void save(String key, String value) {
      sp.edit().putString(key, value).apply();
    }

    int getInt(String key, int defValue) {
      return sp.getInt(key, defValue);
    }

    long getLong(String key, long defValue) {
      return sp.getLong(key, defValue);
    }

    float getFloat(String key, float defValue) {
      return sp.getFloat(key, defValue);
    }

    boolean getBoolean(String key, boolean defValue) {
      return sp.getBoolean(key, defValue);
    }

    String getString(String key, String defValue) {
      return sp.getString(key, defValue);
    }

    boolean contains(String key) {
      return sp.contains(key);
    }

    void remove(String key) {
      sp.edit().remove(key).apply();
    }

    void clear() {
      sp.edit().clear().apply();
    }

    SharedPreferences.Editor editor() {
      return sp.edit();
    }
  }

  /**
   * The global default shared preference util instance.
   * This instance is automatically created with this method.
   *
   * @param context context
   * @return the single instance
   */
  public static Prefers with(Context context) {
    if (singleton == null) {
      synchronized (Prefers.class) {
        if (singleton == null) {
          singleton = new Prefers(context);
        }
      }
    }
    return singleton;
  }

  /**
   * get shared preferences according to api
   */
  private SharedPreferences getPrefer(Context context, String preferName) {
    return context.getSharedPreferences(preferName, Context.MODE_PRIVATE);
  }

  /**
   * Load default shared preferences(packagename_preferences).
   *
   * @return {@link PreferFile}
   */
  public PreferFile load() {
    if (singleton == null) {
      throw new IllegalStateException("call with(context) first");
    }
    if (context == null) {
      throw new IllegalArgumentException("context cannot be null");
    }
    SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);

    return new PreferFile(sp);
  }

  /**
   * Load shared preferences according to prefer name.
   *
   * @param preferName the name of shared preference
   * @return {@link PreferFile}
   */
  public PreferFile load(String preferName) {
    if (singleton == null) {
      throw new IllegalStateException("call with(context) first");
    }
    if (context == null) {
      throw new IllegalArgumentException("context cannot be null");
    }
    SharedPreferences sp = getPrefer(context, preferName);

    return new PreferFile(sp);
  }
}
