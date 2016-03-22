package notification.notificationtypes;

import android.content.Context;
import notification.*;
import notification.definition.*;
import notification.templates.TextFieldTemplate;
import ui.R;

import java.text.*;
import java.util.*;

/**
 * @author simon, 11.06.2015
 */
public class DebtsNotification extends BaseNotification
{
  private Number amount;

  public DebtsNotification(Context pContext, int pId, NotificationStartDate pNotificationDate,
                           NotificationTarget pTarget, boolean pPublicVisible, Number pAmount)
  {
    super(pContext, pId, pNotificationDate, pTarget, pPublicVisible);
    amount = _checkValue(pAmount);
  }

  @Override
  public String getTitle(Context pContext)
  {
    NotificationTarget target = (NotificationTarget) getTargetField().getValue();
    String tar = target == null || target.getName() == null ? "" :
        " " + pContext.getString(R.string.debts_target) + " " + target.getName();
    return amount + pContext.getString(R.string.debts_title) + tar;
  }

  @Override
  public String getType(Context pContext)
  {
    return pContext.getString(R.string.type_debts);
  }

  @Override
  public int getIconId()
  {
    return R.drawable.dollar;
  }

  @Override
  public List<ITemplateComponent> createAdditionalFields(final Context pContext)
  {
    return new ArrayList<ITemplateComponent>() {
      {
        add(new TextFieldTemplate(pContext.getString(R.string.key_amount), amount)
        {

          @Override
          public void setValue(Object pValue)
          {
            super.setValue(_checkValue(pValue));
          }
        });
      }
    };
  }

  private Number _checkValue(Object pValue)
  {
    if (pValue == null || !(pValue instanceof Number))
      throw new RuntimeException(); //TODO

    Number number = (Number) pValue;

    double amount = number.doubleValue();
    int intAmount = number.intValue();

    if( (amount - intAmount) == 0)
      return Integer.valueOf(intAmount);
    else
    {
      DecimalFormatSymbols dfs = DecimalFormatSymbols.getInstance();
      dfs.setDecimalSeparator('.');
      DecimalFormat format = new DecimalFormat("#0.00", dfs);
      return Double.valueOf(Double.parseDouble(format.format(amount)));
    }
  }
}
