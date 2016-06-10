package notification;

import android.content.*;
import android.graphics.Color;
import android.text.InputType;
import android.view.*;
import android.widget.*;
import autodiscover.CustomObjectMapper;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.sdanner.ui.*;
import definition.StorableBaseNotification;
import org.jetbrains.annotations.Nullable;
import registry.BoxRegistry;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

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
   * Erzeugt eine neue Erinnerung anhand einer Storable-Erinnerung
   *
   * @param pNotification die speicherbare Erinnerung
   * @return die neu erzeugte INotification
   */
  public static INotification createNotificationFromStorable(StorableBaseNotification pNotification)
  {
    return createNotificationFromType(CreateNotification.TYPES.inverse().get(pNotification.getClass()), pNotification);
  }

  /**
   * Erzeugt eine neue Erinnerung eines eines Typen
   *
   * @param pType     der bestimmte Typ der Erinnerung
   * @param pStorable das zugehöroge Storable
   * @return die neu erzeugte INotification
   */
  public static INotification createNotificationFromType(Class<? extends INotification> pType,
                                                         StorableBaseNotification pStorable)
  {
    try
    {
      return pType.getDeclaredConstructor(pStorable.getClass()).newInstance(pStorable);
    }
    catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException pE)
    {
      throw new RuntimeException(pE);
    }
  }

  /**
   * Wandelt eine Erinnerung in einen String um (zur Serialisierung)
   *
   * @param pNotification die Erinnerung
   * @return die Erinnerung als String
   */
  public static String getNotificationAsString(StorableBaseNotification pNotification)
  {
    CustomObjectMapper mapper = new CustomObjectMapper(BoxRegistry.getAllBoxTypes());
    try
    {
      return mapper.writeValueAsString(pNotification);
    }
    catch (JsonProcessingException pE)
    {
      throw new RuntimeException(pE);
    }
  }

  /**
   * Wandelt eine Notification-String in eine Erinnerung zurück
   *
   * @param pNotificationString die Erinnerung als String
   * @return die umgewandelte INotification
   */
  public static StorableBaseNotification getNotificationFromString(String pNotificationString)
  {
    CustomObjectMapper mapper = new CustomObjectMapper(BoxRegistry.getAllBoxTypes());
    try
    {
      return mapper.readValue(pNotificationString, StorableBaseNotification.class);
    }
    catch (IOException pE)
    {
      throw new RuntimeException(pE);
    }
  }

  /**
   * Erzeugt ein Intent, welches zum Versenden einer INotification zwischen zwei Activities verwendet wird
   *
   * @param pBaseIntent   das grundlegende Intent (Quelle + Ziel)
   * @param pNotification die Erinnerung, welches versendet werden soll
   * @param pPhoneNumber  die eigene Telefon-Nummer
   * @return das bearbeitete Intent
   */
  public static Intent createNotificationIntent(Intent pBaseIntent, INotification<?> pNotification, String pPhoneNumber)
  {
    String storableString = NotificationUtil.getNotificationAsString(pNotification.getStorableNotification());
    pBaseIntent.putExtra(Overview.STORABLE_NOTIFICATION, storableString);
    pNotification.setStorableNotification(null);
    pBaseIntent.putExtra(Overview.NOTIFICATION, pNotification);
    pBaseIntent.putExtra(Overview.PHONE_NUMBER, pPhoneNumber);
    return pBaseIntent;
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
   * @return das erstellte Textfeld mit den besonderen Eigenschaften
   */
  public static EditText createTemplateTextfield(final Context pContext, final boolean pOnlyAllowNumbers)
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
   * @param pEditable  der neue Zustand
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
}
