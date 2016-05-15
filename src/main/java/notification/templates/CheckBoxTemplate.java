package notification.templates;

import android.content.Context;
import android.view.View;
import android.widget.CheckBox;
import notification.ITemplateComponent;

import java.io.Serializable;

/**
 * @author Simon Danner, 19.05.2015
 */
public class CheckBoxTemplate implements ITemplateComponent<Boolean>, Serializable
{
  private String key;
  private boolean value;
  private CheckBox checkBox;

  public CheckBoxTemplate(boolean pValue)
  {
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
    if (checkBox == null)
    {
      checkBox = new CheckBox(pContext);
      setEditable(false);
      shiftValueToGraphicComponent();
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
  public Boolean getGraphicValue()
  {
    return checkBox != null && checkBox.isChecked();
  }

  @Override
  public void shiftValueToGraphicComponent()
  {
    if (checkBox != null)
      checkBox.setChecked(getValue());
  }

  @Override
  public void setValueFromGraphicComponent()
  {
    value = checkBox != null && checkBox.isChecked();
  }
}
