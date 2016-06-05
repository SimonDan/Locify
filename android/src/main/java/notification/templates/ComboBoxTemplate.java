package notification.templates;

import android.content.Context;
import android.view.View;
import notification.ITemplateComponent;

import java.io.Serializable;
import java.util.List;

/**
 * @author simon, 19.05.2015
 */
public class ComboBoxTemplate<T> implements ITemplateComponent<T>, Serializable
{
  //TODO Alles

  private String key;
  private List<Object> options;

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
    return null; //TODO
  }

  @Override
  public void setEditable(boolean pEditable)
  {
  }

  @Override
  public T getValue()
  {
    return null;
  }

  @Override
  public void setValue(T pValue)
  {
  }
}
