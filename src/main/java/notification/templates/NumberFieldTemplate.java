package notification.templates;

import android.content.Context;
import android.graphics.Color;
import android.text.InputType;
import android.view.*;
import android.widget.*;
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
  public View getGraphicComponent(Context pContext, ViewGroup pParent)
  {
    if (textField == null)
    {
      textField = new EditText(pContext);
      textField.setInputType(InputType.TYPE_CLASS_NUMBER);
      textField.setTextColor(Color.WHITE);
      textField.setSingleLine(true);
      textField.setLayoutParams(new LinearLayout.LayoutParams
                                    (LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT, 1f));
      textField.setOnFocusChangeListener(new View.OnFocusChangeListener()
      {
        @Override
        public void onFocusChange(View v, boolean hasFocus)
        {
          if (!hasFocus)
            setValue((T) textField.getText());
        }
      });

      setEditable(false);
      setValue(value);
    }
    return textField;
  }

  @Override
  public void setEditable(boolean pEditable)
  {

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

  private T _checkValue(T pValue)
  {
    if (pValue == null)
      return pValue;

    double amount = pValue.doubleValue();
    int intAmount = pValue.intValue();

    Number result;

    if ((amount - intAmount) == 0)
      result = Integer.valueOf(intAmount);
    else
    {
      DecimalFormatSymbols dfs = DecimalFormatSymbols.getInstance();
      dfs.setDecimalSeparator('.');
      DecimalFormat format = new DecimalFormat("#0.00", dfs);
      result = Double.valueOf(Double.parseDouble(format.format(amount)));
    }

    return (T) result;
  }
}
