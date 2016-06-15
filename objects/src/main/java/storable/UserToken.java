package storable;

import annotations.StorableObject;
import definition.AbstractStorableObject;
import definition.fields.*;

/**
 * @author Simon Danner, 16.05.2016.
 */
@StorableObject(fieldName = "usertoken", boxName = "userToken")
public class UserToken extends AbstractStorableObject
{
  public static final Field<String> phoneNumber = FieldFactory.field(String.class);
  public static final Field<String> gcmToken = FieldFactory.field(String.class);

  public String getPhoneNumber()
  {
    return getValue(phoneNumber);
  }

  public String getToken()
  {
    return getValue(gcmToken);
  }
}
