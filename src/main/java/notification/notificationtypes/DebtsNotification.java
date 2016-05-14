package notification.notificationtypes;

import android.content.Context;
import com.fasterxml.jackson.annotation.*;
import com.sdanner.ui.R;
import notification.*;
import notification.definition.NotificationTarget;
import notification.templates.NumberFieldTemplate;

import java.text.*;
import java.util.*;

/**
 * Beschreibt eine spezielle Erinnerung für Schulden bei einer Person
 *
 * @author Simon Danner, 11.06.2015
 */
public class DebtsNotification extends BaseNotification
{
  private NumberFieldTemplate amount;

  public DebtsNotification(String pCreator)
  {
    super(pCreator);
    amount = new NumberFieldTemplate(0.0);
  }

  @JsonCreator
  public DebtsNotification(@JsonProperty("id") String pId, @JsonProperty("creator") String pCreator,
                           @JsonProperty("startDate") long pDate, @JsonProperty("target") String pTarget,
                           @JsonProperty("visibleForTarget") boolean pVisibleForTarget, @JsonProperty("amount") double pAmount)
  {
    super(pId, pCreator, pDate, pTarget, pVisibleForTarget);
    amount = new NumberFieldTemplate(pAmount);
  }

  @Override
  public String getNotificationTitle(Context pContext)
  {
    NotificationTarget target = getNotificationTarget();
    String tar = target == null || target.getName() == null ? "" :
        " " + pContext.getString(R.string.debts_target) + " " + target.getName();
    return _toPrettyNumber(amount.getValue()) + pContext.getString(R.string.debts_title) + tar;
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
        amount.setKey(pContext.getString(R.string.key_amount));
        add(amount);
      }
    };
  }

  public double getAmount()
  {
    return amount.getValue();
  }

  private String _toPrettyNumber(double pNumber)
  {
    int intValue = (int) pNumber;

    if (pNumber - intValue == 0)
      return "" + intValue;
    else
    {
      DecimalFormatSymbols dfs = DecimalFormatSymbols.getInstance();
      dfs.setDecimalSeparator('.');
      DecimalFormat format = new DecimalFormat("#0.00", dfs);
      return format.format(pNumber);
    }
  }
}
