package communication.request;

import org.apache.http.client.methods.HttpGet;
import org.jetbrains.annotations.Nullable;

import java.io.*;

/**
 * Führt einen GET-Webservice-Request durch.
 *
 * @author Simon Danner, 12.11.2015
 */
public class GETRequest<T> extends AbstractResponseWebservice<T>
{
  private InputStream response;

  /**
   * Erzeugt den GET-Request
   *
   * @param pURLMethod        die benötigte Webservice Methode
   * @param pRequestedClass   der Typ des abzufragenden Objektes
   * @param pParams           zusätzliche Parameter für die Abfrage (werden an die URL gehängt)
   */
  public GETRequest(String pURLMethod, Class<T> pRequestedClass, Object... pParams)
  {
    this(pURLMethod, false,  pRequestedClass, pParams);
  }

  /**
   * Erzeugt den GET-Request
   *
   * @param pURLMethod        die benötigte Webservice Methode
   * @param pUserGetterMapper todo
   * @param pRequestedClass   der Typ des abzufragenden Objektes
   * @param pParams           zusätzliche Parameter für die Abfrage (werden an die URL gehängt)
   */
  public GETRequest(String pURLMethod, boolean pUserGetterMapper, Class<T> pRequestedClass, Object... pParams)
  {
    super(pURLMethod, pUserGetterMapper, pRequestedClass, pParams);
  }

  /**
   * Liefert das Ergebnis der Abfrage als JSON-String
   */
  protected InputStream getResponse(String pMediaType) throws IOException
  {
    return response;
  }

  @Override
  public boolean execute(@Nullable Object pEntity)
  {
    HttpGet request = new HttpGet(url);
    try
    {
      response = httpClient.execute(request).getEntity().getContent();
      return true;
    }
    catch (Exception pE)
    {
      return false;
    }
  }
}
