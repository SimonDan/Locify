package notification;

import android.content.Context;
import android.graphics.Color;
import android.text.*;
import android.view.*;
import android.widget.*;
import com.sdanner.ui.R;
import notification.templates.*;
import org.jetbrains.annotations.*;

import java.io.*;

/**
 * Hilfsmethoden, welche die Erinnerungen betreffen
 *
 * @author Simon Danner, 20.06.2015
 */
public final class NotificationUtil
{
  private NotificationUtil()
  {
  }

  /**
   * Erzeugt eine Zeile einer Erinnerung für eine Listen-Ansicht
   *
   * @param pContext der Kontext
   * @param pParent  der Parent (die Liste)
   * @param pTitle   der Titel der Erinnerung
   * @param pIconId  die ID des Icons
   * @return eine View, welche die Zeile für die Liste darstellt
   */
  public static View createListRow(Context pContext, ViewGroup pParent, String pTitle, int pIconId)
  {
    LayoutInflater inflater = (LayoutInflater) pContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    View rowView = inflater.inflate(R.layout.listrow, pParent, false);
    ImageView imgView = (ImageView) rowView.findViewById(R.id.item_icon);
    TextView titleView = (TextView) rowView.findViewById(R.id.item_title);

    imgView.setImageResource(pIconId);
    titleView.setText(pTitle);
    return rowView;
  }

  /**
   * Erzeugt eine editierbares Textfeld speziell für die Templates (Text und Number)
   * Zum einen kann festgelegt werden, ob man nur Zahlen eingeben darf und
   * zum anderen wird bei diesem Textfeld jede Eingabe direkt im Template als Value gesetzt
   *
   * @param pContext          der Kontext
   * @param pOnlyAllowNumbers sind nur Zahlen erlaubt?
   * @param pTemplate         das Template
   * @return das erstellte Textfeld mit den besonderen Eigenschaften
   */
  public static EditText createTemplateTextfield(final Context pContext, final boolean pOnlyAllowNumbers,
                                                 final ITemplateComponent pTemplate)
  {
    final EditText textField = new EditText(pContext);
    textField.setTextColor(Color.WHITE);
    textField.setSingleLine(true);
    textField.setLayoutParams(new LinearLayout.LayoutParams
                                  (LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT, 1f));
    //Nur Zahlen erlaubt?
    if (pOnlyAllowNumbers)
      textField.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);

    return textField;
  }

  /**
   * Ändert den Bearbeitungs-Zustand eines Textfeldes
   *
   * @param pTextField das Textfeld (kann null sein)
   * @param pEditable der neue Zustand
   */
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

  /**
   * Adapter für eine Text-Change-Listener
   */
  private static class _TextChangeAdapter implements TextWatcher
  {
    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after)
    {
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count)
    {
    }

    @Override
    public void afterTextChanged(Editable s)
    {
    }
  }
}
