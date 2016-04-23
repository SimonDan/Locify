package notification;

import android.app.Activity;
import android.content.Context;
import notification.definition.*;

import java.util.List;

/**
 * Beschreibt eine Erinnerung.
 * Zusätzlich werden hier die grafischen Komponenten zur Bearbeitung geliefert
 *
 * @author Simon Danner, 12.04.2016.
 */
public interface INotification
{
  String getID();

  String getTitle(Context pContext);

  String getTypeName(Context pContext);

  int getIconID();

  NotificationStartDate getStartDate();

  NotificationTarget getTarget();

  void setTarget(NotificationTarget pTarget); //Für die Contact-Picker Intent

  boolean isVisibleForTarget();

  List<ITemplateComponent> getFields(Activity pContext);
}
