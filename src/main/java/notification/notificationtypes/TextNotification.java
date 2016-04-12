package notification.notificationtypes;

import android.content.Context;
import notification.*;
import notification.definition.*;
import notification.templates.TextFieldTemplate;
import ui.R;

import java.util.*;

/**
 * @author simon, 20.06.2015
 */
public class TextNotification extends BaseNotification
{
  private String title, details;

  public TextNotification(Context pContext, String pId, NotificationStartDate pNotificationDate, NotificationTarget pTarget,
                          boolean pPublicVisible, String pTitle, String pDetails)
  {
    super(pContext, pId, pNotificationDate, pTarget, pPublicVisible);
    title = pTitle;
    details = pDetails;
  }

  @Override
  public String getTitle(Context pContext)
  {
    if (title == null || title.isEmpty())
      return pContext.getString(R.string.emptyTextNotification);
    return title;
  }

  @Override
  public String getType(Context pContext)
  {
    return pContext.getString(R.string.type_text);
  }

  @Override
  public int getIconId()
  {
    return R.drawable.change;
  }

  @Override
  public List<ITemplateComponent> createAdditionalFields(final Context pContext)
  {
    return new ArrayList<ITemplateComponent>() {
      {
        add(new TextFieldTemplate(pContext.getString(R.string.key_title), title));
        add(new TextFieldTemplate(pContext.getString(R.string.key_details), details));
      }
    };
  }
}
