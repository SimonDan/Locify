package notification;

import android.content.Context;
import android.view.*;

/**
 * @author simon, 19.05.2015
 */
public interface ITemplateComponent<T>
{
  String getKey();

  View getGraphicComponent(Context pContext, ViewGroup pParent);

  void setEditable(boolean pEditable);

  T getValue();

  void setValue(T pValue);
}
