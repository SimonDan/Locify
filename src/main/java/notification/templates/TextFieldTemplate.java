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
public class TextFieldTemplate implements ITemplateComponent, Serializable
{
  private String key;
  private Object value;
  private boolean onlyNumbers;
  private EditText textField;

  public TextFieldTemplate(String pKey, Object pValue)
  {
    this(pKey, pValue, false);
  }

  public TextFieldTemplate(String pKey, Object pValue, boolean pOnlyNumbers)
  {
    key = pKey;
    value = pValue;
    onlyNumbers = pOnlyNumbers;
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
      if (onlyNumbers)
        textField.setInputType(InputType.TYPE_CLASS_NUMBER);
      textField.setLayoutParams(new LinearLayout.LayoutParams
                                    (LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT, 1f));
      textField.setOnFocusChangeListener(new View.OnFocusChangeListener()
      {
        @Override
        public void onFocusChange(View v, boolean hasFocus)
        {
          if (!hasFocus)
          {
            String text = textField.getText().toString();
            Object value = onlyNumbers ? Double.valueOf(text) : text;
            setValue(value);
          }
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
  public Object getValue()
  {
    return value;
  }

  @Override
  public void setValue(Object pValue)
  {
    value = pValue;
    textField.setText(value != null ? value.toString() : "");
  }
}
