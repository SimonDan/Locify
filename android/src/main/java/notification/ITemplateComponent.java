package notification;

import android.content.Context;
import android.view.View;

import java.io.Serializable;

/**
 * Beschreibt eine Key-Value-Komponente einer Erinnerung.
 * Beinhaltet auch die grafische Komponente (Value).
 *
 * @author Simon Danner, 19.05.2015
 */
public interface ITemplateComponent<T> extends Serializable
{
  /**
   * Liefert den Schlüssel des Templates
   */
  String getKey();

  /**
   * Setzt den Schlüssel des Templates
   *
   * @param pKey der neue Schlüssel
   */
  void setKey(String pKey);

  /**
   * Liefert den Wert des Templates
   */
  T getValue();

  /**
   * Setzt den Wert des Templates
   *
   * @param pValue der neue Wert
   */
  void setValue(T pValue);

  /**
   * Liefert die grafische Komponente, durch welches der Wert verändert werden kann
   *
   * @param pContext der aktuelle Kontext
   * @return die grafische Komponente als View
   */
  View getGraphicComponent(Context pContext);

  /**
   * Legt den Editier-Zustand des Templates (der grafischen Komponente) fest
   *
   * @param pEditable <tt>true</tt> wenn editierbar
   */
  void setEditable(boolean pEditable);
}
