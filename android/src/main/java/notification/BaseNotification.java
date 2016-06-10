package notification;

import android.app.Activity;
import android.content.*;
import android.provider.ContactsContract;
import com.sdanner.ui.*;
import com.sdanner.ui.util.AndroidUtil;
import definition.StorableBaseNotification;
import notification.definition.*;
import notification.templates.*;
import notification.templates.util.*;
import org.jetbrains.annotations.Nullable;

import java.util.*;

/**
 * Grundlage für jede Erinnerung
 * Verwaltet ID, Start-Datum, Ziel und der Angabe, ob sie für das Ziel sichtbar ist
 *
 * @author Simon Danner, 19.05.2015
 */
public abstract class BaseNotification<T extends StorableBaseNotification> implements INotification<T>
{
  //Gesetzte Werte
  private T storable;
  private NotificationTarget currentTarget;

  //Grafische Komponenten
  private ValueFromActionTemplate<NotificationStartDate> startDate;
  private ValueFromActionTemplate<NotificationTarget> target;
  private CheckBoxTemplate visibleForTarget;
  private List<ITemplateComponent> fields;

  public BaseNotification(T pStorable)
  {
    storable = pStorable;
    currentTarget = new NotificationTarget("", storable.getTarget());
    startDate = new ValueFromActionTemplate<>();
    target = new ValueFromActionTemplate<>();
    visibleForTarget = new CheckBoxTemplate();
  }

  /**
   * Liefert die zusätzlichen Felder (Templates) für die Erinnerung
   *
   * @param pContext der aktuelle Kontext
   */
  protected abstract List<ITemplateComponent> createAdditionalFields(Context pContext);

  @Override
  public List<ITemplateComponent> getFields(Activity pContext)
  {
    if (fields == null)
    {
      _setupButtonActions(pContext);
      fields = new ArrayList<>();
      startDate.setKey(pContext.getString(R.string.key_date));
      fields.add(startDate);
      target.setKey(pContext.getString(R.string.key_target));
      fields.add(target);
      visibleForTarget.setKey(pContext.getString(R.string.key_public_visible));
      fields.add(visibleForTarget);
      fields.addAll(createAdditionalFields(pContext));
    }
    return fields;
  }

  @Override
  public String getID()
  {
    return storable.getID();
  }

  @Override
  public void setID(String pID)
  {
    storable.setID(pID);
  }

  @Override
  public String getCreator()
  {
    return storable.getCreator();
  }

  @Override
  public void shiftValuesToGraphicComponents()
  {
    startDate.setValue(new NotificationStartDate(storable.getStartDate()));
    target.setValue(currentTarget);
    visibleForTarget.setValue(storable.isVisibleForTarget());
  }

  @Override
  public void setValuesFromGraphicComponents()
  {
    storable.setValue(StorableBaseNotification.startDate, startDate.getValue().getDate());
    storable.setValue(StorableBaseNotification.target, target.getValue().getPhoneNumber());
    currentTarget = target.getValue();
    storable.setValue(StorableBaseNotification.visibleForTarget, visibleForTarget.getValue());
  }

  @Override
  public T getStorableNotification()
  {
    return storable;
  }

  @Override
  public void setStorableNotification(@Nullable T pStorableNotification)
  {
    storable = pStorableNotification;
  }

  @Override
  public boolean isValid()
  {
    NotificationTarget currTarget = target.getValue();
    return currTarget != null && !currTarget.getPhoneNumber().isEmpty();
  }

  @Override
  public NotificationTarget getNotificationTarget()
  {
    return currentTarget;
  }

  /**
   * Legt eine neues Target in der grafischen Komponente fest
   *
   * @param pTargetInContainer das neue Ziel der Erinnerung
   */
  public void setTargetInGraphicComponent(NotificationTarget pTargetInContainer)
  {
    target.setValue(pTargetInContainer);
  }

  /**
   * Legt die Button-Aktionen für die ValueFromActionTemplates fest
   * Für das Start-Datum ein Date-Time-Picker und für das Target einen Contact-Picker
   *
   * @param pContext der aktuelle Kontext als Activity
   */
  private void _setupButtonActions(final Activity pContext)
  {
    startDate.setButtonAction(new IButtonAction<NotificationStartDate>()
    {
      @Override
      public void executeButtonAction(final ValueContainer<NotificationStartDate> pValueContainer)
      {
        AndroidUtil.IDatePickerCallback callback = new AndroidUtil.IDatePickerCallback()
        {
          @Override
          public void onResult(long pDate)
          {
            pValueContainer.setValue(new NotificationStartDate(pDate));
          }
        };

        AndroidUtil.showDateTimePicker(pContext, startDate.getValue().getDate(), callback);
      }
    });

    target.setButtonAction(new IButtonAction<NotificationTarget>()
    {
      @Override
      public void executeButtonAction(ValueContainer<NotificationTarget> pValueContainer)
      {
        Intent contactPickerIntent = new Intent(Intent.ACTION_PICK, ContactsContract.CommonDataKinds.Phone.CONTENT_URI);
        pContext.startActivityForResult(contactPickerIntent, NotificationView.CONTACT_PICKER_RESULT);
      }
    });
  }
}
