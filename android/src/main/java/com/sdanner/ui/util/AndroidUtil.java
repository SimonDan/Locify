package com.sdanner.ui.util;

import android.app.*;
import android.content.*;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.provider.ContactsContract;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.telephony.TelephonyManager;
import android.widget.*;
import com.sdanner.ui.R;
import org.jetbrains.annotations.Nullable;

import java.util.*;

/**
 * Utility-Klasse für Hilfsmethoden, die Android selbst betreffen
 *
 * @author Simon Danner, 11.04.2016.
 */
public final class AndroidUtil
{
  private AndroidUtil()
  {
  }

  /**
   * Fordert eine bestimmte Berechtigung zur Laufzeit an
   *
   * @param pActivity   die fragende Activity
   * @param pPermission die Berechtigung
   */
  public static void requestRuntimePermission(Activity pActivity, String pPermission)
  {
    if (ContextCompat.checkSelfPermission(pActivity, pPermission) != PackageManager.PERMISSION_GRANTED)
      ActivityCompat.requestPermissions(pActivity, new String[]{pPermission}, 123);
  }

  /**
   * Ermittelt die Telefon-Nummer des Smartphones.
   * Gibt null zurück, wenn sie nicht ermittelt werden kann
   *
   * @param pActivity die fragende Activity
   * @return die Handy-Nummer als String
   */
  @Nullable
  public static String getOwnNumber(Activity pActivity)
  {
    String fromPrefs = getOwnNumberFromPrefs(pActivity);
    if (fromPrefs != null)
      return fromPrefs;

    try
    {
      TelephonyManager tMgr = (TelephonyManager) pActivity.getSystemService(Context.TELEPHONY_SERVICE);
      String number = tMgr.getLine1Number();
      SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(pActivity);
      prefs.edit().putString(pActivity.getString(R.string.key_phoneNumber), number).apply(); //In Prefs speichern
      return number;
    }
    catch (Exception pE)
    {
      return null;
    }
  }

  /**
   * Liefert alle Telefon-Nummern aus dem Kontakt-Buch
   *
   * @param pContext der aktuelle Kontext
   * @return eine Liste mit Telefon-Nummern
   */
  public static List<String> getAllContactNumbers(Context pContext)
  {
    ArrayList<String> contacts = new ArrayList<>();
    ContentResolver cr = pContext.getContentResolver();
    Cursor cursor = cr.query(ContactsContract.Contacts.CONTENT_URI, null, null, null, null);

    if (cursor.moveToFirst())
    {
      do
      {
        String id = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));
        if (Integer.parseInt(cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER))) > 0)
        {
          Cursor c = cr.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,
                              ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?", new String[]{id}, null);
          if (c.moveToNext())
          {
            String contactNumber = c.getString(c.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
            contacts.add(contactNumber);
          }
          c.close();
        }

      }
      while (cursor.moveToNext());
    }
    cursor.close();

    return contacts;
  }

  @Nullable
  public static String getOwnNumberFromPrefs(Context pContext)
  {
    SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(pContext);
    String phoneNumber = prefs.getString(pContext.getString(R.string.key_phoneNumber), null);
    return phoneNumber != null && !phoneNumber.isEmpty() ? phoneNumber : null;
  }

  public static void storeOwnNumberInPrefs(Context pContext, String pNumber)
  {
    SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(pContext);
    prefs.edit().putString(pContext.getString(R.string.key_phoneNumber), pNumber).apply();
  }

  /**
   * Gibt den Kontakt-Namen anhand seiner Nummer zurück
   *
   * @param pContext     der Kontext
   * @param pPhoneNumber die Handy-Nummer
   * @return den Namen des Kontaktes oder null, wenn er nicht existiert
   */
  @Nullable
  public static String getContactNameFromNumber(Context pContext, String pPhoneNumber)
  {
    ContentResolver cr = pContext.getContentResolver();
    Uri uri = Uri.withAppendedPath(ContactsContract.PhoneLookup.CONTENT_FILTER_URI, Uri.encode(pPhoneNumber));
    Cursor cursor = cr.query(uri, new String[]{ContactsContract.PhoneLookup.DISPLAY_NAME}, null, null, null);
    if (cursor == null)
      return null;

    String contactName = null;
    if (cursor.moveToFirst())
      contactName = cursor.getString(cursor.getColumnIndex(ContactsContract.PhoneLookup.DISPLAY_NAME));

    if (!cursor.isClosed())
      cursor.close();

    return contactName;
  }

  /**
   * Zeigt den Fehler einer ServerUnavailableException auf dem UI-Thread
   *
   * @param pContext   der Context, hier MUSS es sich um eine Activity handeln
   * @param pException die ServerUnavailableException, über welche berichtet werden soll
   */
  public static void showErrorOnUIThread(final Context pContext, final ServerUnavailableException pException)
  {
    if (!(pContext instanceof Activity))
      throw new RuntimeException(); //TODO

    ((Activity) pContext).runOnUiThread(new Runnable()
    {
      @Override
      public void run()
      {
        Toast.makeText(pContext, pException.getErrorMessage(pContext), Toast.LENGTH_LONG).show();
      }
    });
  }

  /**
   * Zeigt einen Bestätigungs-Dialog, welcher bei OK ein Callback ausführt
   *
   * @param pContext    der Kontext
   * @param pMessage    die Bestätigungs-Nachricht
   * @param pOKCallback das Callback, welches bei OK ausgeführt wird
   */
  public static void showConfirmDialog(Context pContext, String pMessage, final Runnable pOKCallback)
  {
    AlertDialog.Builder builder = new AlertDialog.Builder(pContext);
    builder.setMessage(pMessage).setPositiveButton(pContext.getString(R.string.delete_dialog_ok), new DialogInterface.OnClickListener()
    {
      @Override
      public void onClick(DialogInterface dialog, int id)
      {
        pOKCallback.run();
      }
    })
        .setNegativeButton(pContext.getString(R.string.delete_dialog_cancel), new DialogInterface.OnClickListener()
        {
          @Override
          public void onClick(DialogInterface dialog, int id)
          {
            dialog.cancel();
          }
        })
        .show();
  }

  /**
   * Zeigt einen Date-Timer-Picker, welcher bei erfolgreicher Eingabe ein Callback ausführt
   *
   * @param pContext  der Kontext
   * @param pDate     das voreingestellt Datum
   * @param pCallback das Callback, welches den Zeitstempel der Eingabe mitliefert
   */
  public static void showDateTimePicker(final Context pContext, long pDate, final IDatePickerCallback pCallback)
  {
    final Calendar calendar = Calendar.getInstance();
    calendar.setTime(new Date(pDate));

    new DatePickerDialog(pContext, new DatePickerDialog.OnDateSetListener()
    {
      @Override
      public void onDateSet(DatePicker view, final int year, final int monthOfYear, final int dayOfMonth)
      {
        new TimePickerDialog(pContext, new TimePickerDialog.OnTimeSetListener()
        {
          @Override
          public void onTimeSet(TimePicker view, int hourOfDay, int minute)
          {
            calendar.set(year, monthOfYear, dayOfMonth, hourOfDay, minute);
            pCallback.onResult(calendar.getTime().getTime());
          }
        }, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), true).show();
      }
    }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show();
  }

  /**
   * Callback, um nach dem Resultat eines Date-Time-Pickers noch eine Aktion auszuführen
   */
  public interface IDatePickerCallback
  {
    void onResult(long pDate);
  }
}
