package communication.request.autodiscover;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.*;
import com.sdanner.ui.CreateNotification;
import notification.*;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.lang.reflect.*;
import java.util.*;

/**
 * @author Simon Danner, 31.05.2016.
 */
public class NotificationDeserializer extends JsonDeserializer<INotification>
{
  @Override
  public INotification deserialize(JsonParser pJsonParser, DeserializationContext pContext) throws IOException
  {
    //Key-Value-Map des JSON-Dokuments
    ObjectMapper mapper = new ObjectMapper();
    TypeReference<Map<String, Object>> typeRef = new TypeReference<Map<String, Object>>()
    {
    };
    Map<String, Object> keyValueMap = mapper.convertValue(pJsonParser.getCodec().readTree(pJsonParser), typeRef);

    //Typ ermitteln
    Class<? extends INotification> realType = _findType(keyValueMap);
    INotification notification;
    try
    {
      notification = realType.getDeclaredConstructor().newInstance(keyValueMap.values());
    }
    catch (InstantiationException | IllegalAccessException | NoSuchMethodException | InvocationTargetException pE)
    {
      throw new RuntimeException(pE);
    }

    return notification;
  }

  @NotNull
  private Class<? extends INotification> _findType(Map<String, Object> pKeyValueMap)
  {
    Class<? extends INotification> found = null;

    for (Class<? extends BaseNotification> type : CreateNotification.TYPES)
    {
      if (_getKeySet(type).equals(pKeyValueMap.keySet()))
      {
        found = type;
        break;
      }
    }

    Objects.requireNonNull(found);
    return found;
  }

  private Set<String> _getKeySet(Class<? extends INotification> pType)
  {
    Set<String> keySet = new HashSet<>();
    Class current = pType;
    while (current != Object.class)
    {
      for (Field field : current.getDeclaredFields())
        if (field.getType().equals(String.class) || field.getType().isPrimitive() ||
            ITemplateComponent.class.isAssignableFrom(field.getType()))
          keySet.add(field.getName());
      current = current.getSuperclass();
    }
    return keySet;
  }
}
