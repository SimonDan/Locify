package notification.templates;

import android.content.Context;
import android.view.View;
import android.widget.EditText;
import notification.*;

import java.io.Serializable;

/**
 * @author Simon Danner, 19.05.2015
 */
public class TextFieldTemplate implements ITemplateComponent<String>, Serializable
{
  private String key;
  private String value;
  private EditText textField;

  public TextFieldTemplate(String pValue)
  {
    this(null, pValue);
  }

  public TextFieldTemplate(String pKey, String pValue)
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
      textField = NotificationUtil.createTemplateTextfield(pContext, false, this);
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
  public String getValue()
  {
    return value;
  }

  @Override
  public String getGraphicValue()
  {
    return textField != null ? textField.getText().toString() : null;
  }

  @Override
  public void shiftValueToGraphicComponent()
  {
    if (textField != null)
      textField.setText(getValue());
  }

  @Override
  public void setValueFromGraphicComponent()
  {
    if (textField != null)
      value = textField.getText().toString();
  }
}
