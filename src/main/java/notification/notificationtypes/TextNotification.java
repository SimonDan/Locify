package notification.notificationtypes;

import android.content.Context;
import com.fasterxml.jackson.annotation.*;
import com.sdanner.ui.R;
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
  private TextFieldTemplate title, details;

  public TextNotification(String pCreator)
  {
    super(pCreator);
    _initTemplates("", "");
  }

  @JsonCreator
  public TextNotification(@JsonProperty("id") String pId, @JsonProperty("creator") String pCreator,
                          @JsonProperty("startDate") long pDate, @JsonProperty("target") String pTarget,
                          @JsonProperty("visibleForTarget") boolean pVisibleForTarget, @JsonProperty("title") String pTitle,
                          @JsonProperty("details") String pDetails)
  {
    super(pId, pCreator, pDate, pTarget, pVisibleForTarget);
    _initTemplates(pTitle, pDetails);
  }

  private void _initTemplates(String pTitle, String pDetails)
  {
    title = new TextFieldTemplate(pTitle);
    details = new TextFieldTemplate(pDetails);
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
  public List<ITemplateComponent> createAdditionalFields(final Context pContext)
  {
    return new ArrayList<ITemplateComponent>()
    {
      {
        title.setKey(pContext.getString(R.string.key_title));
        details.setKey(pContext.getString(R.string.key_details));
        add(title);
        add(details);
      }
    };
  }

  public String getTitle()
  {
    return title.getValue();
  }

  public String getDetails()
  {
    return details.getValue();
  }
}
