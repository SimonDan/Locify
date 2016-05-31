package objects;

import annotations.StorableObject;
import definition.AbstractStorableObject;
import definition.fields.*;

/**
 * Collection, die alle aktuellen Positionen der User verwaltet
 *
 * @author Simon Danner, 06.04.2016.
 */
@StorableObject(fieldName = "Positions", boxName = "Positions", allowInsertion = false, allowDeletion = false)
public class UserPosition extends AbstractStorableObject
{
  public static final Field<String> phoneNumber = FieldFactory.field(String.class);
  public static final Field<Long> longitude = FieldFactory.field(Long.class);
  public static final Field<Long> latitude = FieldFactory.field(Long.class);
  public static final Field<Long> lastUpdated = FieldFactory.field(Long.class);

  public long getLongitude()
  {
    return getValue(longitude);
  }

  public long getLatitude()
  {
    return getValue(longitude);
  }
}
