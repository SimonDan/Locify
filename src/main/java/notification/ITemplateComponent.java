package notification;

import android.content.Context;
import android.view.*;

/**
 * Beschrebt eine Key-Value-Komponente einer Erinnerung.
 * Beinhaltet auch die grafische Komponente.
 *
 * @author Simon Danner, 19.05.2015
 */
public interface ITemplateComponent<T>
{
  String getKey();

  View getGraphicComponent(Context pContext, ViewGroup pParent);

  void setEditable(boolean pEditable);

  T getValue();

  void setValue(T pValue);
}
