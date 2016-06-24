package notification.templates;

import android.content.Context;
import android.view.View;
import android.widget.CheckBox;
import notification.ITemplateComponent;

import java.io.Serializable;

/**
 * Beschreibt ein Template für eine Checkbox (boolean)
 *
 * @author Simon Danner, 19.05.2015
 */
public class CheckBoxTemplate implements ITemplateComponent<Boolean>
{
  private String key;
  private CheckBox checkBox;

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
  public Boolean getValue()
  {
    return checkBox.isChecked();
  }

  @Override
  public void setValue(Boolean pValue)
  {
    checkBox.setChecked(pValue);
  }

  @Override
  public View getGraphicComponent(Context pContext)
  {
    if (checkBox == null)
    {
      checkBox = new CheckBox(pContext);
      setEditable(false);
    }
    return checkBox;
  }

  @Override
  public void setEditable(boolean pEditable)
  {
    if (checkBox != null)
      checkBox.setClickable(pEditable);
  }
}
