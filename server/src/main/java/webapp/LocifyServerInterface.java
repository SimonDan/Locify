package webapp;

import objects.*;
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
  public Collection<BaseNotification> getNotifications(@PathParam("phoneNumber") String pPhoneNumber)
  {
    return BoxRegistry.NOTIFICATIONS.find(BaseNotification.creator.asSearch(pPhoneNumber));
  }

  @GET
  @Path("getNotification/{notificationID}")
  @Consumes(MediaType.TEXT_PLAIN)
  @Produces(MediaType.APPLICATION_JSON)
  public BaseNotification getNotification(@PathParam("notificationID") String pNotificationID)
  {
    return BoxRegistry.NOTIFICATIONS.findOne(BaseNotification.id.asSearch(pNotificationID));
  }

  @POST
  @Path("updateNotification")
  @Consumes(MediaType.APPLICATION_JSON)
  public String updateNotification(BaseNotification pNotification)
  {
    String id = pNotification.getID() != null ? pNotification.getID() : null;
    BoxRegistry.NOTIFICATIONS.update(pNotification, BaseNotification.id.asSearch(id));
    return pNotification.getID();
  }

  @PUT
  @Path("deleteNotification")
  @Consumes(MediaType.TEXT_PLAIN)
  public void deleteNotification(String pNotificationID)
  {
    BoxRegistry.NOTIFICATIONS.remove(BaseNotification.id.asSearch(pNotificationID));
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

