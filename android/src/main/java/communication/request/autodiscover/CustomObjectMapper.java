package communication.request.autodiscover;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.module.SimpleModule;
import notification.INotification;

/**
 * @author Simon Danner, 31.05.2016.
 */
public class CustomObjectMapper extends ObjectMapper
{
  public CustomObjectMapper(boolean pUseGetterMapper)
  {
    disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
    setVisibilityChecker(getSerializationConfig().getDefaultVisibilityChecker()
                             .withFieldVisibility(pUseGetterMapper ? JsonAutoDetect.Visibility.NONE : JsonAutoDetect.Visibility.ANY)
                             .withGetterVisibility(pUseGetterMapper ? JsonAutoDetect.Visibility.ANY : JsonAutoDetect.Visibility.NONE)
                             .withSetterVisibility(JsonAutoDetect.Visibility.NONE)
                             .withCreatorVisibility(JsonAutoDetect.Visibility.NONE));

    //Register
    SimpleModule module = new SimpleModule();
    module.addDeserializer(INotification.class, new NotificationDeserializer());
    registerModule(module);
  }
}
