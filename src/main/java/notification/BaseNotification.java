package notification;

import android.content.Context;
import notification.definition.*;
import notification.templates.*;
import ui.R;

import java.io.Serializable;
import java.util.List;

/**
 * @author Simon Danner, 19.05.2015
 */
public abstract class BaseNotification implements Serializable
{
  private String id;
  private ITemplateComponent date, target, publicVisible;

  public BaseNotification(Context pContext, String pId, NotificationStartDate pDate, NotificationTarget pTarget,
                          boolean pPublicVisible)
  {
    id = pId;
    date = new TextFromActionTemplate(pContext.getString(R.string.key_date), pDate);
    target = new TextFromActionTemplate(pContext.getString(R.string.key_target), pTarget);
    publicVisible = new CheckBoxTemplate(pContext.getString(R.string.key_public_visible), pPublicVisible);
  }

  public abstract String getTitle(Context pContext);

  public abstract String getType(Context pContext);

  public abstract int getIconId();

  public abstract List<ITemplateComponent> createAdditionalFields(Context pContext);

  public String getId()
  {
    return id;
  }

  public ITemplateComponent getDateField()
  {
    return date;
  }

  public ITemplateComponent getTargetField()
  {
    return target;
  }

  public ITemplateComponent getIsPublicVisibleField()
  {
    return publicVisible;
  }
}
