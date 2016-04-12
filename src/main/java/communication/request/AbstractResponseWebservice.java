package communication.request;

import org.apache.commons.io.IOUtils;
import org.jetbrains.annotations.Nullable;

import javax.ws.rs.core.*;
import java.io.*;

/**
 * Grundlage für eine Webservice-Anfrage, die ein Ergebnis-Objekt liefern kann
 *
 * @author Simon Danner, 17.12.2015
 */
public abstract class AbstractResponseWebservice<T> extends AbstractWebserviceRequest
{
  private Class<T> requestedClass; //Die angeforderte Ergebnis-Klasse

  /**
   * Erzeugt eine neue Webservice-Anfrage
   *
   * @param pURLMethod      der Name des Webservice
   * @param pRequestedClass die angeforderte Ergebnis-Klasse
   * @param pParams         beliebig viele Parameter
   */
  public AbstractResponseWebservice(String pURLMethod, Class<T> pRequestedClass, Object... pParams)
  {
    super(pURLMethod, pParams);
    requestedClass = pRequestedClass;
  }

  /**
   * Abstrakte Methode, um eine Anwort der speziellen Anfrage zu erhalten
   *
   * @param pMediaType der Media-Type des Ergebis-Objektes
   */
  protected abstract Response getResponse(String pMediaType) throws Exception;

  /**
   * Gibt ein Objekt als Ergebnis der Abfrage zurück
   */
  @SuppressWarnings("unchecked")
  @Nullable
  public T getObject()
  {
    String mediaType = requestedClass.isPrimitive() ? MediaType.TEXT_PLAIN : MediaType.APPLICATION_JSON;

    try
    {
      Response response;

      try
      {
        response = getResponse(mediaType);
      }
      catch (Exception pE)
      {
        throw new RuntimeException(pE);
      }

      String responseAsString = IOUtils.toString(response.readEntity(InputStream.class), "UTF-8");

      if (requestedClass == Void.class)
        return null;

      if (requestedClass.isPrimitive())
        return (T) _convert(responseAsString);

      return mapper.readValue(responseAsString, requestedClass);
    }
    catch (IOException e)
    {
      throw new RuntimeException(e);
    }
  }

  /**
   * Wandelt das ein primitives Ergebnis entsprechend um
   *
   * @param value der Wert als String
   * @return der Wert als angepassten primitiven Wert
   */
  private Object _convert(String value)
  {
    if (boolean.class == requestedClass) return Boolean.parseBoolean(value);
    if (byte.class == requestedClass) return Byte.parseByte(value);
    if (short.class == requestedClass) return Short.parseShort(value);
    if (int.class == requestedClass) return Integer.parseInt(value);
    if (long.class == requestedClass) return Long.parseLong(value);
    if (float.class == requestedClass) return Float.parseFloat(value);
    if (double.class == requestedClass) return Double.parseDouble(value);
    return value;
  }
}
