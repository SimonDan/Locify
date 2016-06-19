package notification;

import android.app.Activity;
import android.content.Context;
import notification.definition.NotificationTarget;
import org.jetbrains.annotations.Nullable;
import storable.StorableBaseNotification;

import java.io.Serializable;
import java.util.List;

/**
 * Beschreibt eine Erinnerung
 * Zusätzlich werden hier die grafischen Komponenten zur Bearbeitung geliefert
 *
 * @author Simon Danner, 12.04.2016.
 */
public interface INotification<T extends StorableBaseNotification> extends Serializable
{
  /**
   * Liefert die ID einer Erinnerung
   */
  String getID();

  /**
   * Setzt die ID einer Erinnerung
   */
  void setID(String pID);

  /**
   * Liefert den Ersteller der Erinnerung
   */
  String getCreator();

  /**
   * Liefert diese Erinnerung als speicherbare Erinnerung (serialisierbar)
   */
  T getStorableNotification();

  /**
   * Erlaubt es die interne Storable-Erinnerung neu zu setzen oder auf null zu setzen
   * Wird für die Serialisierung zwischen den Activities benötigt
   *
   * @param pStorableNotification die speicherbare Erinnerung oder null
   */
  void setStorableNotification(@Nullable T pStorableNotification);

  /**
   * Trägt die Eigenschaften der Erinnerung bei den grafischen Komponenten ein
   */
  void shiftValuesToGraphicComponents();

  /**
   * Trägt die Werte der grafischen Komponenten bei der Erinnerung ein
   */
  void setValuesFromGraphicComponents();

  /**
   * Evaluiert den Anzeige-Titel einer Erinnerung
   *
   * @param pContext       der akutelle Kontext
   * @param pIAmTheCreator bestimmt, ob die Erinnerung vom User erstellt wurde
   */
  String getNotificationTitle(Context pContext, boolean pIAmTheCreator);

  /**
   * Liefert den Typ-Namen der Erinnerung
   *
   * @param pContext der aktuelle Kontext
   */
  String getTypeName(Context pContext);

  /**
   * Liefert die Icon-ID des Erinnerungs-Typen
   */
  int getIconID();

  /**
   * Liefert die Resource-ID für die Schriftfarbe der Erinnerung
   *
   * @param pIAmTheCreator bestimmt, ob die Erinnerung vom User erstellt wurde
   */
  int getFontColorID(boolean pIAmTheCreator);

  /**
   * Gibt an, ob die Erinnerung gültig ist. Dh. sie ist vollständig und kann gespeichert werden
   */
  boolean isValid();

  /**
   * Liefert den Betreffenden einer Erinnerung
   */
  NotificationTarget getNotificationTarget();

  /**
   * Liefert die Key-Value-Templates, welche den grafischen Aufbau der Erinnerung festlegen
   *
   * @param pContext der aktuelle Kontext
   * @return eine List von ITemplateComponents
   */
  List<ITemplateComponent> getFields(Activity pContext);
}
