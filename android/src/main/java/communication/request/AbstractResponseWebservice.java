package communication.request;

import com.fasterxml.jackson.core.type.TypeReference;
import org.apache.commons.io.IOUtils;
import org.jetbrains.annotations.Nullable;

import java.io.*;

/**
 * Grundlage für eine Webservice-Anfrage, die ein Ergebnis-Objekt liefern kann
 *
 * @author Simon Danner, 17.12.2015
 */
public abstract class AbstractResponseWebservice<T> extends AbstractWebserviceRequest
{
  private Class<T> requestedClass; //Die angeforderte Ergebnis-Klasse
  private TypeReference<T> type; //Die angeforderte Klasse als TypeReference

  /**
   * Erzeugt eine neue Webservice-Anfrage
   *
   * @param pURLMethod      der Name des Webservice
   * @param pRequestedClass die angeforderte Ergebnis-Klasse
   * @param pParams         beliebig viele Parameter
   */
  public AbstractResponseWebservice(String pURLMethod, boolean pUserGetterMapper, Class<T> pRequestedClass, Object... pParams)
  {
    super(pURLMethod, pUserGetterMapper, pParams);
    requestedClass = pRequestedClass;
  }

  /**
   * Erzeugt eine neue Webservice-Anfrage
   *
   * @param pURLMethod der Name des Webservice
   * @param pType      die angeforderte Ergebnis-Klasse
   * @param pParams    beliebig viele Parameter
   */
  public AbstractResponseWebservice(String pURLMethod, boolean pUserGetterMapper, TypeReference<T> pType, Object... pParams)
  {
    super(pURLMethod, pUserGetterMapper, pParams);
    type = pType;
  }

  /**
   * Abstrakte Methode, um eine Anwort der speziellen Anfrage zu erhalten
   */
  protected abstract InputStream getResponse() throws IOException;

  /**
   * Gibt ein Objekt als Ergebnis der Abfrage zurück
   */
  @SuppressWarnings("unchecked")
  @Nullable
  public T getObject()
  {
    try
    {
      InputStream response;

      try
      {
        response = getResponse();
      }
      catch (IOException pE)
      {
        throw new RuntimeException(pE);
      }

      String responseAsString = IOUtils.toString(response, "UTF-8");

      if (requestedClass != null)
      {
        if (requestedClass == Void.class)
          return null;

        if (requestedClass == String.class)
          return (T) responseAsString;

        if (requestedClass.isPrimitive())
          return (T) _convert(responseAsString);

        return mapper.readValue(responseAsString, requestedClass);
      }

      return mapper.readValue(responseAsString, type);
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
