package notification;

import android.app.Activity;
import android.content.Context;
import com.fasterxml.jackson.annotation.*;
import notification.definition.*;
import notification.notificationtypes.*;

import java.io.Serializable;
import java.util.List;

/**
 * Beschreibt eine Erinnerung.
 * Zusätzlich werden hier die grafischen Komponenten zur Bearbeitung geliefert
 *
 * @author Simon Danner, 12.04.2016.
 */
@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS, include = JsonTypeInfo.As.EXTERNAL_PROPERTY, property = "type")
@JsonSubTypes({@JsonSubTypes.Type(value = DebtsNotification.class, name = "debts"),
               @JsonSubTypes.Type(value = TextNotification.class, name = "text") })
public interface INotification extends Serializable
{
  String getID();

  void setID(String pId);

  String getNotificationTitle(Context pContext);

  @JsonIgnore
  String getTypeName(Context pContext);

  @JsonIgnore
  int getIconID();

  boolean isValid();

  long getStartDate();

  String getCreator();

  @JsonIgnore
  NotificationTarget getNotificationTarget();

  String getTarget();

  boolean isVisibleForTarget();

  @JsonIgnore
  List<ITemplateComponent> getFields(Activity pContext);
}
