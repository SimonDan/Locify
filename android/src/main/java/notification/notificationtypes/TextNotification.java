package notification.notificationtypes;

import android.content.Context;
import com.sdanner.ui.R;
import definition.StorableTextNotification;
import notification.*;
import notification.templates.TextFieldTemplate;

import java.util.*;

/**
 * Beschreibt eine spezielle Erinnerung, welche per Freitext erstellt werden kann
 *
 * @author Simon Danner, 20.06.2015
 */
public class TextNotification extends BaseNotification
{
  private StorableTextNotification notification;
  private TextFieldTemplate title, description;

  public TextNotification(StorableTextNotification pNotification)
  {
    super(pNotification);
    notification = pNotification;
    title = new TextFieldTemplate();
    description = new TextFieldTemplate();
  }

  @Override
  public void shiftValuesToGraphicComponents()
  {
    super.shiftValuesToGraphicComponents();
    title.setValue(notification.getTitle());
    title.setValue(notification.getDescription());
  }

  @Override
  public void setValuesFromGraphicComponents()
  {
    super.setValuesFromGraphicComponents();
    notification.setValue(StorableTextNotification.title, title.getValue());
    notification.setValue(StorableTextNotification.description, description.getValue());
  }

  @Override
  public String getNotificationTitle(Context pContext)
  {
    if (title == null || title.getValue() == null || title.getValue().isEmpty())
      return pContext.getString(R.string.emptyTextNotification);
    return title.getValue();
  }

  @Override
  public String getTypeName(Context pContext)
  {
    return pContext.getString(R.string.type_text);
  }

  @Override
  public int getIconID()
  {
    return R.drawable.change;
  }

  @Override
  public boolean isValid()
  {
    return super.isValid() && !title.getValue().isEmpty(); //Details dürfen leer sein
  }

  @Override
  public List<ITemplateComponent> createAdditionalFields(final Context pContext)
  {
    return new ArrayList<ITemplateComponent>()
    {
      {
        title.setKey(pContext.getString(R.string.key_title));
        description.setKey(pContext.getString(R.string.key_details));
        add(title);
        add(description);
      }
    };
  }
}
