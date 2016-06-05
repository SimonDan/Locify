package notification;

import android.content.Context;
import android.view.View;

/**
 * Beschreibt eine Key-Value-Komponente einer Erinnerung.
 * Beinhaltet auch die grafische Komponente (Value).
 *
 * @author Simon Danner, 19.05.2015
 */
public interface ITemplateComponent<T>
{
  String getKey();

  void setKey(String pKey);

  T getValue();

  void setValue(T pValue);

  View getGraphicComponent(Context pContext);

  void setEditable(boolean pEditable);
}
