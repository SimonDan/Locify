package notification;

import android.app.Activity;
import android.content.Context;
import com.fasterxml.jackson.annotation.JsonIgnore;
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

  @JsonIgnore
  String getTypeName(Context pContext);

  @JsonIgnore
  int getIconID();

  long getStartDate();

  String getCreator();

  @JsonIgnore
  NotificationTarget getNotificationTarget();

  String getTarget();

  void setTarget(NotificationTarget pTarget); //Für die Contact-Picker Intent

  boolean isVisibleForTarget();

  @JsonIgnore
  List<ITemplateComponent> getFields(Activity pContext);
}
