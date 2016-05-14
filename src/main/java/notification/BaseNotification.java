package notification;

import android.app.Activity;
import android.content.*;
import android.provider.ContactsContract;
import com.sdanner.ui.*;
import com.sdanner.ui.util.AndroidUtil;
import notification.definition.*;
import notification.templates.*;
import notification.templates.util.*;

import java.util.*;

/**
 * Grundlage für jede Erinnerung
 * Verwaltet ID, Start-Datum, Ziel und der Angabe, ob sie für das Ziel sichtbar ist
 *
 * @author Simon Danner, 19.05.2015
 */
public abstract class BaseNotification implements INotification
{
  private String id;
  private String creator;
  private ValueFromActionTemplate<NotificationStartDate> date;
  private ValueFromActionTemplate<NotificationTarget> target;
  private CheckBoxTemplate visibleForTarget;
  private List<ITemplateComponent> fields;

  public BaseNotification(String pCreator)
  {
    this(null, pCreator, -1L, null, false);
  }

  public BaseNotification(String pId, String pCreator, long pDate, String pTarget, boolean pVisibleForTarget)
  {
    id = pId;
    creator = pCreator;
    date = new ValueFromActionTemplate<>(new NotificationStartDate(new Date(pDate)));
    target = new ValueFromActionTemplate<>(new NotificationTarget("", pTarget));
    visibleForTarget = new CheckBoxTemplate(pVisibleForTarget);
  }

  protected abstract List<ITemplateComponent> createAdditionalFields(Context pContext);

  public List<ITemplateComponent> getFields(Activity pContext)
  {
    if (fields == null)
    {
      _setupButtonActions(pContext);
      fields = new ArrayList<>();
      date.setKey(pContext.getString(R.string.key_date));
      fields.add(date);
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
    return id;
  }

  @Override
  public void setID(String pId)
  {
    id = pId;
  }

  @Override
  public long getStartDate()
  {
    return date.getValue().getDate().getTime();
  }

  @Override
  public String getCreator()
  {
    return creator;
  }

  @Override
  public String getTarget()
  {
    return target.getValue().getPhoneNumber();
  }

  @Override
  public NotificationTarget getNotificationTarget()
  {
    return target.getValue();
  }

  @Override
  public boolean isVisibleForTarget()
  {
    return visibleForTarget.getValue();
  }

  public void setTargetInContainer(NotificationTarget pTargetInContainer)
  {
    target.getValueContainer().setValue(pTargetInContainer);
  }

  private void _setupButtonActions(final Activity pContext)
  {
    date.setButtonAction(new IButtonAction<NotificationStartDate>()
    {
      @Override
      public void executeButtonAction(final ValueContainer<NotificationStartDate> pValueContainer)
      {
        Date currDate = date.getValue() != null ? date.getValue().getDate() : new Date(System.currentTimeMillis());
        AndroidUtil.IDatePickerCallback callback = new AndroidUtil.IDatePickerCallback()
        {
          @Override
          public void onResult(Date pDate)
          {
            pValueContainer.setValue(new NotificationStartDate(pDate));
          }
        };

        AndroidUtil.showDateTimePicker(pContext, currDate, callback);
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
