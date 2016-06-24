package notification.templates;

import android.content.Context;
import android.view.View;
import android.widget.EditText;
import notification.*;

import java.io.Serializable;

/**
 * Beschreibt ein Template für ein Textfeld, welches nur Zahlen erlaubt
 *
 * @author Simon Danner, 12.04.2016.
 */
public class NumberFieldTemplate implements ITemplateComponent<Double>
{
  private String key;
  private EditText textField;
  private int maxNumbers;

  public NumberFieldTemplate(int pMaxNumbers)
  {
    maxNumbers = pMaxNumbers;
  }

  @Override
  public String getKey()
  {
    return key;
  }

  @Override
  public void setKey(String pKey)
  {
    key = pKey;
  }

  @Override
  public Double getValue()
  {
    return _getTextFieldValue();
  }

  @Override
  public void setValue(Double pValue)
  {
    textField.setText(String.valueOf(pValue));
  }

  @Override
  public View getGraphicComponent(Context pContext)
  {
    if (textField == null)
    {
      textField = NotificationUtil.createTemplateTextfield(pContext, true, maxNumbers);
      setEditable(false);
    }
    return textField;
  }

  @Override
  public void setEditable(boolean pEditable)
  {
    NotificationUtil.setTextFieldEditable(textField, pEditable);
  }

  private Double _getTextFieldValue()
  {
    if (textField == null)
      return null;

    String currentText = textField.getText().toString();
    try
    {
      return Double.parseDouble(currentText);
    }
    catch (NumberFormatException pE)
    {
      return 0.0;
    }
  }
}
