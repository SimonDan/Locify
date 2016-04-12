package notification.templates;

import android.content.Context;
import android.graphics.Color;
import android.text.InputType;
import android.view.*;
import android.widget.*;
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
      textField = new EditText(pContext);
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
            setValue(textField.getText().toString());
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
    if (textField != null)
    {
      textField.setClickable(pEditable);
      textField.setFocusable(pEditable);
      textField.setFocusableInTouchMode(pEditable);
      textField.setEnabled(pEditable);
    }
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
