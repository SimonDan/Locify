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
  private EditText textField;

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
      textField = NotificationUtil.createTemplateTextfield(pContext, true);
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
    return currentText.isEmpty() ? 0.0 : Double.parseDouble(currentText);
  }
}
