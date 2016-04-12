package communication.request;

import javax.ws.rs.core.Response;

/**
 * Führt einen GET-Webservice-Request durch.
 *
 * @author Simon Danner, 12.11.2015
 */
public class GETRequest<T> extends AbstractResponseWebservice<T>
{
  /**
   * Erzeugt den GET-Request
   *
   * @param pURLMethod      die benötigte Webservice Methode
   * @param pRequestedClass der Typ des abzufragenden Objektes
   * @param pParams         zusätzliche Parameter für die Abfrage (werden an die URL gehängt)
   */
  public GETRequest(String pURLMethod, Class<T> pRequestedClass, Object... pParams)
  {
    super(pURLMethod, pRequestedClass, pParams);
  }

  /**
   * Liefert das Ergebnis der Abfrage als JSON-String
   */
  protected Response getResponse(String pMediaType) throws Exception
  {
    return target.request(pMediaType).get();
  }
}
