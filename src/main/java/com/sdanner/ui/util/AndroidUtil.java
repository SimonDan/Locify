package com.sdanner.ui.util;

import android.app.*;
import android.content.*;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
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
    SharedPreferences prefs = pActivity.getPreferences(Context.MODE_PRIVATE);
    String phoneNumber = prefs.getString(pActivity.getString(R.string.key_phoneNumber), null);

    if (phoneNumber != null && !phoneNumber.isEmpty())
      return phoneNumber;

    try
    {
      TelephonyManager tMgr = (TelephonyManager) pActivity.getSystemService(Context.TELEPHONY_SERVICE);
      return tMgr.getLine1Number();
    }
    catch (Exception pE)
    {
      return null;
    }
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
   * @param pActivity  die Activity
   * @param pException die ServerUnavailableException, über welche berichtet werden soll
   */
  public static void showErrorOnUIThread(final Activity pActivity, final ServerUnavailableException pException)
  {
    pActivity.runOnUiThread(new Runnable()
    {
      @Override
      public void run()
      {
        Toast.makeText(pActivity, pException.getErrorMessage(pActivity.getApplicationContext()), Toast.LENGTH_LONG).show();
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
  public static void showDateTimePicker(final Context pContext, Date pDate, final IDatePickerCallback pCallback)
  {
    final Calendar calendar = Calendar.getInstance();
    calendar.setTime(pDate);

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
            pCallback.onResult(calendar.getTime());
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
    void onResult(Date pDate);
  }
}
