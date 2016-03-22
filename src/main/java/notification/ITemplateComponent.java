package notification;

import android.content.Context;
import android.view.*;

/**
 * @author simon, 19.05.2015
 */
public interface ITemplateComponent
{
  String getKey();

  View getGraphicComponent(Context pContext, ViewGroup pParent);

  void setEditable(boolean pEditable);

  Object getValue();

  void setValue(Object pValue);
}
