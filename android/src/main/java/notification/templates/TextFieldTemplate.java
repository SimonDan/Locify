package notification.templates;

import android.content.Context;
import android.view.View;
import android.widget.EditText;
import notification.*;

import java.io.Serializable;

/**
 * Beschreibt ein Template für ein Textfeld
 *
 * @author Simon Danner, 19.05.2015
 */
public class TextFieldTemplate implements ITemplateComponent<String>, Serializable
{
  private String key;
  private EditText textField;
  private int maxCharacters;

  public TextFieldTemplate(int pMaxCharacters)
  {
    maxCharacters = pMaxCharacters;
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
  public String getValue()
  {
    return textField.getText().toString();
  }

  @Override
  public void setValue(String pValue)
  {
    textField.setText(pValue);
  }

  @Override
  public View getGraphicComponent(Context pContext)
  {
    if (textField == null)
    {
      textField = NotificationUtil.createTemplateTextfield(pContext, false, maxCharacters);
      setEditable(false);
    }
    return textField;
  }

  @Override
  public void setEditable(boolean pEditable)
  {
    NotificationUtil.setTextFieldEditable(textField, pEditable);
  }
}
