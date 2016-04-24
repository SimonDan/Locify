package notification;

import android.app.Activity;
import android.content.*;
import android.provider.ContactsContract;
import com.sdanner.ui.*;
import com.sdanner.ui.util.AndroidUtil;
import notification.definition.*;
import notification.templates.*;

import java.io.Serializable;
import java.util.*;

/**
 * @author Simon Danner, 19.05.2015
 */
public abstract class BaseNotification implements INotification, Serializable
{
  private String id;
  private String creator;
  private ValueFromActionTemplate<NotificationStartDate> date;
  private ValueFromActionTemplate<NotificationTarget> target;
  private CheckBoxTemplate visibleForTarget;
  private List<ITemplateComponent> fields;

  public BaseNotification(Context pContext, String pCreator)
  {
    id = null;
    creator = pCreator;
    date = new ValueFromActionTemplate<>(pContext.getString(R.string.key_date));
    target = new ValueFromActionTemplate<>(pContext.getString(R.string.key_target));
    visibleForTarget = new CheckBoxTemplate(pContext.getString(R.string.key_public_visible));
  }

  public BaseNotification(Context pContext, String pId, String pCreator, NotificationStartDate pDate,
                          NotificationTarget pTarget, boolean pPublicVisible)
  {
    id = pId;
    creator = pCreator;
    date = new ValueFromActionTemplate<>(pContext.getString(R.string.key_date), pDate);
    target = new ValueFromActionTemplate<>(pContext.getString(R.string.key_target), pTarget);
    visibleForTarget = new CheckBoxTemplate(pContext.getString(R.string.key_public_visible), pPublicVisible);
  }

  protected abstract List<ITemplateComponent> createAdditionalFields(Context pContext);

  public List<ITemplateComponent> getFields(Activity pContext)
  {
    if (fields == null)
    {
      _setupButtonActions(pContext);
      fields = new ArrayList<>();
      fields.add(date);
      fields.add(target);
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

  public void setTarget(NotificationTarget pTarget)
  {
    target.setValue(pTarget);
  }

  private void _setupButtonActions(final Activity pContext)
  {
    date.setButtonAction(new Runnable()
    {
      @Override
      public void run()
      {
        Date currDate = date.getValue() != null ? date.getValue().getDate() : new Date(System.currentTimeMillis());
        AndroidUtil.IDatePickerCallback callback = new AndroidUtil.IDatePickerCallback()
        {
          @Override
          public void onResult(Date pDate)
          {
            date.setValue(new NotificationStartDate(pDate)); //Neu setzen falls vorher null
          }
        };

        AndroidUtil.showDateTimePicker(pContext, currDate, callback);
      }
    });

    target.setButtonAction(new Runnable()
    {
      @Override
      public void run()
      {
        Intent contactPickerIntent = new Intent(Intent.ACTION_PICK, ContactsContract.CommonDataKinds.Phone.CONTENT_URI);
        pContext.startActivityForResult(contactPickerIntent, NotificationView.CONTACT_PICKER_RESULT);
      }
    });
  }
}
