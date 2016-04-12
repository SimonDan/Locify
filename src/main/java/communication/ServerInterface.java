package communication;

import communication.request.*;
import notification.*;

import java.util.List;

/**
 * Beschreibt das Webservice-Interface zum Server
 *
 * @author Simon Danner, 10.04.2016.
 */
public class ServerInterface
{
  public void updatePosition(PositionUpdate pUpdate)
  {
    new PUTRequest("updatePosition").execute(pUpdate);
  }

  @SuppressWarnings("unchecked")
  public List<INotification> getNotifications(String pPhoneNumber)
  {
    return (List<INotification>) new GETRequest<>("getNotifications", List.class, pPhoneNumber).getObject();
  }

  public void updateNotification(INotification pNotification)
  {
    new PUTRequest("updateNotification").execute(pNotification);
  }

  public void deleteNotification(String pNotificationID)
  {
    new PUTRequest("deleteNotification").execute(pNotificationID);
  }
}
