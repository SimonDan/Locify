package web;

import definition.*;
import registry.BoxRegistry;
import util.*;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.Collection;

/**
 * Locify-Webservice-Schnittstelle
 *
 * @author Simon Danner, 07.04.2016.
 */
@Path("")
public class LocifyServerInterface
{
  @PUT
  @Path("updatePosition")
  @Consumes(MediaType.APPLICATION_JSON)
  public void updatePosition(PositionUpdate pUpdate)
  {
    //In Datenbank eintragen
    UserPosition position = BoxRegistry.createObjectFromFields(BoxRegistry.POSITIONS, pUpdate.getAsArray());
    BoxRegistry.POSITIONS.update(position, UserPosition.phoneNumber.asSearch(pUpdate.getPhoneNumber()));

    //Überprüfen, ob dieses Positions-Update eine Push-Nachricht einer Erinnerung auslöst
    PositionChecker.checkAfterPositionUpdate(pUpdate);
  }

  @PUT
  @Path("setUserToken")
  @Consumes(MediaType.APPLICATION_JSON)
  public void setUserToken(TokenWrapper pToken)
  {
    UserToken userToken = BoxRegistry.createObjectFromFields(BoxRegistry.USERTOKEN, pToken.getAsArray());
    BoxRegistry.USERTOKEN.update(userToken, UserToken.phoneNumber.asSearch(pToken.getPhoneNumber()));
  }

  @GET
  @Path("getNotifications/{phoneNumber}")
  @Consumes(MediaType.TEXT_PLAIN)
  @Produces(MediaType.APPLICATION_JSON)
  public Collection<StorableBaseNotification> getNotifications(@PathParam("phoneNumber") String pPhoneNumber)
  {
    return BoxRegistry.NOTIFICATIONS.find(StorableBaseNotification.creator.asSearch(pPhoneNumber));
  }

  @GET
  @Path("getTargetNotifications/{phoneNumber}")
  @Consumes(MediaType.TEXT_PLAIN)
  @Produces(MediaType.APPLICATION_JSON)
  public Collection<StorableBaseNotification> getTargetNotifications(@PathParam("phoneNumber") String pPhoneNumber)
  {
    return BoxRegistry.NOTIFICATIONS.find(StorableBaseNotification.target.asSearch(pPhoneNumber),
                                          StorableBaseNotification.visibleForTarget.asSearch(true));
  }

  @GET
  @Path("getNotification/{notificationID}")
  @Consumes(MediaType.TEXT_PLAIN)
  @Produces(MediaType.APPLICATION_JSON)
  public StorableBaseNotification getNotification(@PathParam("notificationID") String pNotificationID)
  {
    return BoxRegistry.NOTIFICATIONS.findOne(StorableBaseNotification.id.asSearch(pNotificationID));
  }

  @POST
  @Path("updateNotification")
  @Consumes(MediaType.APPLICATION_JSON)
  public String updateNotification(StorableBaseNotification pNotification)
  {
    String id = pNotification.getID() != null ? pNotification.getID() : null;
    BoxRegistry.NOTIFICATIONS.update(pNotification, StorableBaseNotification.id.asSearch(id));
    return pNotification.getID();
  }

  @PUT
  @Path("deleteNotification")
  @Consumes(MediaType.TEXT_PLAIN)
  public void deleteNotification(String pNotificationID)
  {
    BoxRegistry.NOTIFICATIONS.remove(StorableBaseNotification.id.asSearch(pNotificationID));
  }

  @POST
  @Path("isNotifyUser")
  @Consumes(MediaType.TEXT_PLAIN)
  @Produces(MediaType.TEXT_PLAIN)
  public boolean isNotifyUser(String pPhoneNumber)
  {
    return BoxRegistry.USERTOKEN.count(UserToken.phoneNumber.asSearch(pPhoneNumber)) == 1;
  }
}

