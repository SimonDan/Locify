package notification;

import android.content.Context;
import notification.definition.*;
import notification.templates.*;
import ui.R;

import java.io.Serializable;
import java.util.*;

/**
 * @author Simon Danner, 19.05.2015
 */
public abstract class BaseNotification implements INotification, Serializable
{
  private String id;
  private ITemplateComponent<NotificationStartDate> date;
  private ITemplateComponent<NotificationTarget> target;
  private CheckBoxTemplate visibleForTarget;

  public BaseNotification(Context pContext, String pId, NotificationStartDate pDate, NotificationTarget pTarget,
                          boolean pPublicVisible)
  {
    id = pId;
    date = new TextFromActionTemplate<>(pContext.getString(R.string.key_date), pDate);
    target = new TextFromActionTemplate<>(pContext.getString(R.string.key_target), pTarget);
    visibleForTarget = new CheckBoxTemplate(pContext.getString(R.string.key_public_visible), pPublicVisible);
  }

  protected abstract List<ITemplateComponent> createAdditionalFields(Context pContext);

  public List<ITemplateComponent> getFields(Context pContext)
  {
    List<ITemplateComponent> fields = new ArrayList<>();
    fields.add(date);
    fields.add(target);
    fields.add(visibleForTarget);
    fields.addAll(createAdditionalFields(pContext));
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
}
