package communication.request;

import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.protocol.HTTP;
import org.jetbrains.annotations.Nullable;

import java.io.*;

/**
 * Führt einen POST-Webservice-Request durch.
 *
 * @author Simon Danner, 26.04.2016.
 */
public class POSTRequest<T> extends AbstractResponseWebservice<T>
{
  private InputStream response;

  /**
   * Erzeugt den POST-Request
   *
   * @param pURLMethod      die benötigte Webservice Methode
   * @param pRequestedClass der Typ des abzufragenden Objektes
   * @param pParams         zusätzliche Parameter für die Abfrage (werden an die URL gehängt)
   */
  public POSTRequest(String pURLMethod, Class<T> pRequestedClass, Object... pParams)
  {
    super(pURLMethod, pRequestedClass, pParams);
  }

  /**
   * Liefert das Ergebnis der Abfrage als JSON-String
   */
  @Override
  protected InputStream getResponse() throws IOException
  {
    return response;
  }

  /**
   * Führt den Request aus
   *
   * @param pEntity eine optionale Entity als Parameter für den Request
   * @return <tt>true</tt> wenn als gut ging
   */
  @Override
  public boolean execute(@Nullable Object pEntity)
  {
    try
    {
      String value = pEntity instanceof String ? (String) pEntity : mapper.writeValueAsString(pEntity);
      HttpPost request = new HttpPost(url);
      request.setHeader(HTTP.CONTENT_TYPE, getContentType(pEntity));
      request.setEntity(new StringEntity(value, HTTP.UTF_8));
      response = httpClient.execute(request).getEntity().getContent();
      return true;
    }
    catch (Exception pE)
    {
      return false;
    }
  }
}
