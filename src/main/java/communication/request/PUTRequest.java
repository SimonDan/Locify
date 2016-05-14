package communication.request;

import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.protocol.HTTP;
import org.jetbrains.annotations.Nullable;

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
    super(pURLMethod, false);
  }

  /**
   * Erzeugt den Request
   *
   * @param pURLMethod die Webservice Methode
   */
  public PUTRequest(String pURLMethod, boolean pUseGetterMapper)
  {
    super(pURLMethod, pUseGetterMapper);
  }

  /**
   * Führt die Abfrage aus
   *
   * @return der Response-Code der Abfrage
   */
  public boolean execute(@Nullable Object pEntity)
  {
    try
    {
      String value = pEntity instanceof String ? (String) pEntity : mapper.writeValueAsString(pEntity);
      HttpPut request = new HttpPut(url);
      request.setHeader(HTTP.CONTENT_TYPE, getContentType(pEntity));
      request.setEntity(new StringEntity(value, HTTP.UTF_8));
      httpClient.execute(request);
      return true;
    }
    catch (Exception pE)
    {
      return false;
    }
  }
}
