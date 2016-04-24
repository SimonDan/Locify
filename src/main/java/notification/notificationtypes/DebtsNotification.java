package notification.notificationtypes;

import android.content.Context;
import com.sdanner.ui.R;
import notification.*;
import notification.definition.*;
import notification.templates.NumberFieldTemplate;

import java.util.*;

/**
 * @author simon, 11.06.2015
 */
public class DebtsNotification extends BaseNotification
{
  private double amount;

  public DebtsNotification(Context pContext, String pCreator)
  {
    super(pContext, pCreator);
    amount = 0;
  }

  @Override
  public String getTitle(Context pContext)
  {
    NotificationTarget target = getNotificationTarget();
    String tar = target == null || target.getName() == null ? "" :
        " " + pContext.getString(R.string.debts_target) + " " + target.getName();
    return amount + pContext.getString(R.string.debts_title) + tar;
  }

  @Override
  public String getTypeName(Context pContext)
  {
    return pContext.getString(R.string.type_debts);
  }

  @Override
  public int getIconID()
  {
    return R.drawable.dollar;
  }

  @Override
  public List<ITemplateComponent> createAdditionalFields(final Context pContext)
  {
    return new ArrayList<ITemplateComponent>()
    {
      {
        add(new NumberFieldTemplate<>(pContext.getString(R.string.key_amount), amount));
      }
    };
  }
}
