package communication.request;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.databind.*;
import org.glassfish.jersey.jackson.JacksonFeature;

import javax.ws.rs.client.*;

/**
 * Grundlage für eine Webservice-Anfrage. Hier wird die Ziel-URL festgelegt.
 *
 * @author Simon Danner, 13.11.2015
 */
public abstract class AbstractWebserviceRequest
{
  protected WebTarget target;
  protected ObjectMapper mapper;

  /**
   * Erzeugt eine neue Webservice-Anfrage
   *
   * @param pURLMethod der Name des Webservice
   * @param pParams    beliebig viele Parameter
   */
  public AbstractWebserviceRequest(String pURLMethod, Object... pParams)
  {
    String url = "localhost:8080/" + pURLMethod; //TODO
    for (Object param : pParams)
      url += "/" + param.toString();
    Client client = ClientBuilder.newClient().register(JacksonFeature.class);
    target = client.target(url);

    //Jackson Object-Mapper
    mapper = new ObjectMapper();
    mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
    mapper.setVisibilityChecker(mapper.getSerializationConfig().getDefaultVisibilityChecker()
                                    .withFieldVisibility(JsonAutoDetect.Visibility.ANY)
                                    .withGetterVisibility(JsonAutoDetect.Visibility.NONE)
                                    .withSetterVisibility(JsonAutoDetect.Visibility.NONE)
                                    .withCreatorVisibility(JsonAutoDetect.Visibility.NONE));
  }
}
