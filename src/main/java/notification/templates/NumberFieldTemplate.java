package notification.templates;

import android.content.Context;
import android.view.View;
import android.widget.EditText;
import notification.*;

import java.io.Serializable;

/**
 * @author Simon Danner, 12.04.2016.
 */
public class NumberFieldTemplate implements ITemplateComponent<Double>, Serializable
{
  private String key;
  private double value;
  private EditText textField;

  public NumberFieldTemplate(double pValue)
  {
    this(null, pValue);
  }

  public NumberFieldTemplate(String pKey, double pValue)
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
  public void setKey(String pKey)
  {
    key = pKey;
  }

  @Override
  public View getGraphicComponent(Context pContext)
  {
    if (textField == null)
    {
      textField = NotificationUtil.createTemplateTextfield(pContext, true, this);
      setEditable(false);
      shiftValueToGraphicComponent();
    }
    return textField;
  }

  @Override
  public void setEditable(boolean pEditable)
  {
    NotificationUtil.setTextFieldEditable(textField, pEditable);
  }

  @Override
  public Double getValue()
  {
    return value;
  }


  @Override
  public void shiftValueToGraphicComponent()
  {
    if (textField != null)
      textField.setText(String.valueOf(getValue()));
  }

  @Override
  public void setValueFromGraphicComponent()
  {
    if (textField == null)
      return;

    String currentText = textField.getText().toString();
    value = currentText.isEmpty() ? 0.0 : Double.parseDouble(currentText);
  }
}
