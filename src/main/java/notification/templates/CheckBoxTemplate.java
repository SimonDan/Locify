package notification.templates;

import android.content.Context;
import android.view.*;
import android.widget.CheckBox;
import notification.ITemplateComponent;

import java.io.Serializable;

/**
 * @author simon, 19.05.2015
 */
public class CheckBoxTemplate implements ITemplateComponent<Boolean>, Serializable
{
  private String key;
  private boolean value;
  private CheckBox checkBox;

  public CheckBoxTemplate(String pKey)
  {
    this(pKey, false);
  }

  public CheckBoxTemplate(String pKey, boolean pValue)
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
    if (checkBox == null)
    {
      checkBox = new CheckBox(pContext);
      setEditable(false);
      setValue(value);
    }
    return checkBox;
  }

  @Override
  public void setEditable(boolean pEditable)
  {
    if (checkBox != null)
      checkBox.setClickable(pEditable);
  }

  @Override
  public Boolean getValue()
  {
    return value;
  }

  @Override
  public void setValue(Boolean pValue)
  {
    value = pValue;
    checkBox.setSelected(value);
  }
}
