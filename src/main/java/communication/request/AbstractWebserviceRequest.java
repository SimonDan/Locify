package communication.request;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.databind.*;
import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.*;
import org.jetbrains.annotations.Nullable;

/**
 * Grundlage für eine Webservice-Anfrage. Hier wird die Ziel-URL festgelegt.
 *
 * @author Simon Danner, 13.11.2015
 */
public abstract class AbstractWebserviceRequest
{
  private static final String SERVER_URL = "http://192.168.178.77:8080/locify/";

  protected String url;
  protected HttpClient httpClient;
  protected ObjectMapper mapper;

  /**
   * Erzeugt eine neue Webservice-Anfrage
   *
   * @param pURLMethod der Name des Webservice
   * @param pParams    beliebig viele Parameter
   */
  public AbstractWebserviceRequest(String pURLMethod, Object... pParams)
  {
    url = SERVER_URL + pURLMethod;
    for (Object param : pParams)
      if (param != null)
        url += "/" + param.toString();

    final HttpParams httpParams = new BasicHttpParams();
    HttpConnectionParams.setConnectionTimeout(httpParams, 5000);
    httpClient = new DefaultHttpClient(httpParams);

    //Jackson Object-Mapper
    mapper = new ObjectMapper();
    mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
    mapper.setVisibilityChecker(mapper.getSerializationConfig().getDefaultVisibilityChecker()
                                    .withFieldVisibility(JsonAutoDetect.Visibility.ANY)
                                    .withGetterVisibility(JsonAutoDetect.Visibility.NONE)
                                    .withSetterVisibility(JsonAutoDetect.Visibility.NONE)
                                    .withCreatorVisibility(JsonAutoDetect.Visibility.NONE));
  }

  public abstract boolean execute(@Nullable Object pEntity);
}

