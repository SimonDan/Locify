package storable;

import annotations.StorableObject;
import definition.AbstractStorableObject;
import definition.fields.*;

/**
 * Collection, die alle aktuellen Positionen der User verwaltet
 *
 * @author Simon Danner, 06.04.2016.
 */
@StorableObject(fieldName = "positions", boxName = "positions", allowInsertion = false, allowDeletion = false)
public class UserPosition extends AbstractStorableObject
{
  public static final Field<String> phoneNumber = FieldFactory.field(String.class);
  public static final Field<Double> longitude = FieldFactory.field(Double.class);
  public static final Field<Double> latitude = FieldFactory.field(Double.class);
  public static final Field<Long> lastUpdated = FieldFactory.field(Long.class);

  public double getLongitude()
  {
    return getValue(longitude);
  }

  public double getLatitude()
  {
    return getValue(latitude);
  }
}
