package notification.templates;

import android.content.Context;
import android.graphics.Color;
import android.view.*;
import android.widget.*;
import com.sdanner.ui.util.AndroidUtil;
import notification.ITemplateComponent;

import java.io.Serializable;

/**
 * @author simon, 19.05.2015
 */
public class TextFieldTemplate implements ITemplateComponent<String>, Serializable
{
  private String key;
  private String value;
  private EditText textField;

  public TextFieldTemplate(String pKey)
  {
    this(pKey, "");
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
  public View getGraphicComponent(Context pContext, ViewGroup pParent)
  {
    if (textField == null)
    {
      textField = AndroidUtil.createTemplateTextfield(pContext, false, this);
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
  public String getValue()
  {
    return value;
  }

  @Override
  public void setValue(String pValue)
  {
    value = pValue;
    textField.setText(value);
  }
}
