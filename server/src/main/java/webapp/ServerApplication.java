package webapp;

import application.CMSBaseServerApplication;
import definition.AbstractStorableObject;
import handling.EDatabaseType;
import registry.BoxRegistry;

import javax.ws.rs.ApplicationPath;
import java.util.*;

/**
 * Applikation des Servers
 *
 * @author Simon Danner, 07.04.2016.
 */
@ApplicationPath("")
public class ServerApplication extends CMSBaseServerApplication
{
  @Override
  public Set<Class<?>> getCustomWebserviceResources()
  {
    return Collections.singleton(LocifyServerInterface.class);
  }

  @Override
  public EDatabaseType getDatabaseType()
  {
    return EDatabaseType.MONGO_DB;
  }

  @Override
  public List<Class<? extends AbstractStorableObject>> getBoxTypes()
  {
    return BoxRegistry.getAllBoxTypes();
  }
}