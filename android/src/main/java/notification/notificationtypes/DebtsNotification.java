package notification.notificationtypes;

import android.content.Context;
import com.sdanner.ui.R;
import definition.StorableDebtsNotification;
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
public class DebtsNotification extends BaseNotification<StorableDebtsNotification>
{
  private NumberFieldTemplate amount;

  public DebtsNotification(StorableDebtsNotification pNotification)
  {
    super(pNotification);
    amount = new NumberFieldTemplate();
  }

  @Override
  public void shiftValuesToGraphicComponents()
  {
    super.shiftValuesToGraphicComponents();
    amount.setValue(getStorableNotification().getAmount());
  }

  @Override
  public void setValuesFromGraphicComponents()
  {
    super.setValuesFromGraphicComponents();
    getStorableNotification().setValue(StorableDebtsNotification.amount, amount.getValue());
  }

  @Override
  public String getNotificationTitle(Context pContext)
  {
    NotificationTarget target = getNotificationTarget();
    String tar = target == null || target.getName() == null ? "" :
        " " + pContext.getString(R.string.debts_target) + " " + target.getName();
    return _toPrettyNumber(getStorableNotification().getAmount()) + pContext.getString(R.string.debts_title) + tar;
  }

  @Override
  public String getTypeName(Context pContext)
  {
    return pContext.getString(R.string.type_debts);
  }

  @Override
  public boolean isValid()
  {
    return super.isValid() && amount.getValue() > 0;
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

  /**
   * Wandelt den Schuld-Wert in einen Geld-darstellbaren String um
   */
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
