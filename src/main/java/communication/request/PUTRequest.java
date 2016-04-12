package communication.request;

import com.fasterxml.jackson.core.JsonProcessingException;

import javax.ws.rs.ProcessingException;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.*;

/**
 * Führt einen PUT-Webservice-Request durch.
 *
 * @author Simon Danner, 12.11.2015
 */
public class PUTRequest extends AbstractWebserviceRequest
{
  /**
   * Erzeugt den Request
   *
   * @param pURLMethod die Webservice Methode
   */
  public PUTRequest(String pURLMethod)
  {
    super(pURLMethod);
  }

  /**
   * Führt die Abfrage aus
   *
   * @return der Response-Code der Abfrage
   */
  public boolean execute(Object pEntity)
  {
    try
    {
      MediaType mediaType = pEntity instanceof String ? MediaType.TEXT_PLAIN_TYPE : MediaType.APPLICATION_JSON_TYPE;
      String value = pEntity instanceof String ? (String) pEntity : mapper.writeValueAsString(pEntity);
      Response response = target.request().put(Entity.entity(value, mediaType));
      return response.getStatus() == 204;
    }
    catch (ProcessingException | JsonProcessingException pE)
    {
      return false;
    }
  }
}
