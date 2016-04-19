package notification;

import android.content.Context;
import com.sdanner.ui.util.AndroidUtil;
import notification.definition.*;
import notification.templates.*;
import com.sdanner.ui.R;

import java.io.Serializable;
import java.util.*;

/**
 * @author Simon Danner, 19.05.2015
 */
public abstract class BaseNotification implements INotification, Serializable
{
  private String id;
  private ValueFromActionTemplate<NotificationStartDate> date;
  private ValueFromActionTemplate<NotificationTarget> target;
  private CheckBoxTemplate visibleForTarget;
  private List<ITemplateComponent> fields;

  public BaseNotification(Context pContext)
  {
    id = null;
    date = new ValueFromActionTemplate<>(pContext.getString(R.string.key_date));
    target = new ValueFromActionTemplate<>(pContext.getString(R.string.key_target));
    visibleForTarget = new CheckBoxTemplate(pContext.getString(R.string.key_public_visible));
  }

  public BaseNotification(Context pContext, String pId, NotificationStartDate pDate, NotificationTarget pTarget,
                          boolean pPublicVisible)
  {
    id = pId;
    date = new ValueFromActionTemplate<>(pContext.getString(R.string.key_date), pDate);
    target = new ValueFromActionTemplate<>(pContext.getString(R.string.key_target), pTarget);
    visibleForTarget = new CheckBoxTemplate(pContext.getString(R.string.key_public_visible), pPublicVisible);
  }

  protected abstract List<ITemplateComponent> createAdditionalFields(Context pContext);

  public List<ITemplateComponent> getFields(Context pContext)
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
  public NotificationStartDate getStartDate()
  {
    return date.getValue();
  }

  @Override
  public NotificationTarget getTarget()
  {
    return target.getValue();
  }

  @Override
  public boolean isVisibleForTarget()
  {
    return visibleForTarget.getValue();
  }

  private void _setupButtonActions(final Context pContext)
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
  }
}
