package notification.templates;

import android.content.Context;
import android.view.*;
import android.widget.EditText;
import com.sdanner.ui.util.AndroidUtil;
import notification.ITemplateComponent;

import java.io.Serializable;
import java.text.*;

/**
 * @author simon, 12.04.2016.
 */
public class NumberFieldTemplate<T extends Number> implements ITemplateComponent<T>, Serializable
{
  private String key;
  private T value;
  private EditText textField;

  public NumberFieldTemplate(String pKey)
  {
    this(pKey, null);
  }

  public NumberFieldTemplate(String pKey, T pValue)
  {
    key = pKey;
    value = pValue;
  }

  @Override
  public String getKey()
  {
    return key;
  }

  @Override
  public View getGraphicComponent(Context pContext)
  {
    if (textField == null)
    {
      textField = AndroidUtil.createTemplateTextfield(pContext, true, this);
      setEditable(false);
      setValue(value);
    }
    return textField;
  }

  @Override
  public void setEditable(boolean pEditable)
  {
    AndroidUtil.setTextFieldEditable(textField, pEditable);
  }

  @Override
  public T getValue()
  {
    return value;
  }

  @Override
  public void setValue(T pValue)
  {
    value = _checkValue(pValue);
  }

  public void setValueAsString(String pValue)
  {
    setValue(_checkValue(pValue));
  }

  private T _checkValue(String pValueAsString)
  {
    if (pValueAsString == null || pValueAsString.isEmpty())
      return null;

    double doubleAmount = Double.parseDouble(pValueAsString);
    int intAmount = Integer.parseInt(pValueAsString);

    return _check(doubleAmount, intAmount);
  }

  private T _checkValue(T pValue)
  {
    if (pValue == null)
      return pValue;

    double amount = pValue.doubleValue();
    int intAmount = pValue.intValue();

    return _check(amount, intAmount);
  }

  @SuppressWarnings("unchecked")
  private T _check(double pDoubleAmount, int pIntAmount)
  {
    Number result;

    if ((pDoubleAmount - pIntAmount) == 0)
      result = Integer.valueOf(pIntAmount);
    else
    {
      DecimalFormatSymbols dfs = DecimalFormatSymbols.getInstance();
      dfs.setDecimalSeparator('.');
      DecimalFormat format = new DecimalFormat("#0.00", dfs);
      result = Double.valueOf(Double.parseDouble(format.format(pDoubleAmount)));
    }

    return (T) result;
  }
}
