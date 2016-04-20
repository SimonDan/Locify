package com.sdanner.ui.util;

import android.app.*;
import android.content.*;
import android.graphics.Color;
import android.telephony.TelephonyManager;
import android.text.InputType;
import android.view.View;
import android.widget.*;
import com.sdanner.ui.R;
import notification.ITemplateComponent;
import notification.templates.NumberFieldTemplate;
import org.jetbrains.annotations.Nullable;

import java.util.*;

/**
 * @author simon, 11.04.2016.
 */
public class AndroidUtil
{
  private AndroidUtil()
  {
  }

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

  public static <T> EditText createTemplateTextfield(Context pContext, final boolean pOnlyAllowNumbers,
                                                     final ITemplateComponent<T> pTemplate)
  {
    final EditText textField = new EditText(pContext);
    textField.setTextColor(Color.WHITE);
    textField.setSingleLine(true);
    textField.setLayoutParams(new LinearLayout.LayoutParams
                                  (LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT, 1f));
    //Nur Zahlen erlaubt?
    if (pOnlyAllowNumbers)
      textField.setInputType(InputType.TYPE_CLASS_NUMBER);

    //Ändert sich der Fokus, soll der Wert bei der Template-Komponente gesetzt werden
    textField.setOnFocusChangeListener(new View.OnFocusChangeListener()
    {
      @Override
      public void onFocusChange(View v, boolean hasFocus)
      {
        if (!hasFocus)
        {
          String currentText = textField.getText().toString();
          if (pOnlyAllowNumbers)
            ((NumberFieldTemplate) pTemplate).setValueAsString(currentText);
          else
            pTemplate.setValue((T) currentText);
        }
      }
    });

    return textField;
  }

  public static void setTextFieldEditable(@Nullable EditText pTextField, boolean pEditable)
  {
    if (pTextField != null)
    {
      pTextField.setClickable(pEditable);
      pTextField.setFocusable(pEditable);
      pTextField.setFocusableInTouchMode(pEditable);
      pTextField.setEnabled(pEditable);
    }
  }

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

  public interface IDatePickerCallback
  {
    void onResult(Date pDate);
  }
}
