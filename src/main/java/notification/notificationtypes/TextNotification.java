package notification.notificationtypes;

import android.content.Context;
import com.sdanner.ui.R;
import notification.*;
import notification.definition.*;
import notification.templates.TextFieldTemplate;

import java.util.*;

/**
 * @author Simon Danner, 20.06.2015
 */
public class TextNotification extends BaseNotification
{
  private String title, details;

  public TextNotification(Context pContext, String pCreator)
  {
    super(pContext, pCreator);
  }

  @Override
  public String getTitle(Context pContext)
  {
    if (title == null || title.isEmpty())
      return pContext.getString(R.string.emptyTextNotification);
    return title;
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
