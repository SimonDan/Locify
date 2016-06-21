package notification.notificationtypes;

import android.content.Context;
import com.sdanner.ui.R;
import com.sdanner.ui.util.AndroidUtil;
import notification.*;
import notification.definition.NotificationTarget;
import notification.templates.*;
import storable.StorableDebtsNotification;

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
  private TextFieldTemplate details;

  public DebtsNotification(StorableDebtsNotification pNotification)
  {
    super(pNotification);
    amount = new NumberFieldTemplate();
    details = new TextFieldTemplate();
  }

  @Override
  public void shiftValuesToGraphicComponents()
  {
    super.shiftValuesToGraphicComponents();
    amount.setValue(getStorableNotification().getAmount());
    details.setValue(getStorableNotification().getDetails());
  }

  @Override
  public void setValuesFromGraphicComponents()
  {
    super.setValuesFromGraphicComponents();
    getStorableNotification().setValue(StorableDebtsNotification.amount, amount.getValue());
    getStorableNotification().setValue(StorableDebtsNotification.details, details.getValue());
  }

  @Override
  public String getNotificationTitle(Context pContext, boolean pIAmTheCreator)
  {
    NotificationTarget target = getNotificationTarget();
    String amountString = _toPrettyNumber(getStorableNotification().getAmount());
    if (pIAmTheCreator)
    {
      String tar = target == null || target.getName() == null ? "" : target.getName();
      return pContext.getString(R.string.title_debts_my, amountString, tar);
    }
    else
    {
      String creator = AndroidUtil.getContactNameFromNumber(pContext, getCreator());
      String creatorString = creator != null ? creator : pContext.getString(R.string.somebody_text);
      return pContext.getString(R.string.title_debts_target, creatorString, amountString);
    }
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
  public int getIconID(boolean pIAmTheCreator)
  {
    return pIAmTheCreator ? R.drawable.debt : R.drawable.debtget;
  }

  @Override
  public List<ITemplateComponent> createAdditionalFields(final Context pContext)
  {
    return new ArrayList<ITemplateComponent>()
    {
      {
        amount.setKey(pContext.getString(R.string.key_amount));
        add(amount);
        details.setKey(pContext.getString(R.string.key_details));
        add(details);
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
