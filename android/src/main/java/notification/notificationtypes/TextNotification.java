package notification.notificationtypes;

import android.content.Context;
import com.sdanner.ui.R;
import notification.*;
import notification.templates.TextFieldTemplate;
import storable.StorableTextNotification;

import java.util.*;

/**
 * Beschreibt eine spezielle Erinnerung, welche per Freitext erstellt werden kann
 *
 * @author Simon Danner, 20.06.2015
 */
public class TextNotification extends BaseNotification<StorableTextNotification>
{
  private static final int MAX_TITLE_CHARACTERS = 30;

  private TextFieldTemplate title, description;

  public TextNotification(StorableTextNotification pNotification)
  {
    super(pNotification);
    title = new TextFieldTemplate(MAX_TITLE_CHARACTERS);
    description = new TextFieldTemplate(-1);
  }

  @Override
  public void shiftValuesToGraphicComponents()
  {
    super.shiftValuesToGraphicComponents();
    title.setValue(getStorableNotification().getTitle());
    description.setValue(getStorableNotification().getDescription());
  }

  @Override
  public void setValuesFromGraphicComponents()
  {
    super.setValuesFromGraphicComponents();
    getStorableNotification().setValue(StorableTextNotification.title, title.getValue());
    getStorableNotification().setValue(StorableTextNotification.description, description.getValue());
  }

  @Override
  public String getNotificationTitle(Context pContext, boolean pIAmTheCreator)
  {
    if (getStorableNotification().getTitle() == null || getStorableNotification().getTitle().isEmpty())
      return pContext.getString(R.string.emptyTextNotification);

    return getStorableNotification().getTitle();
  }

  @Override
  public String getTypeName(Context pContext)
  {
    return pContext.getString(R.string.type_text);
  }

  @Override
  public int getIconID(boolean pIAmTheCreator)
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
