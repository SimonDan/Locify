package storable;

import annotations.StorableObject;
import definition.AbstractUniqueStorableObject;
import definition.fields.*;

import java.util.Date;

/**
 * Beschreibt die Grundlage einer Erinnerung
 *
 * @author Simon Danner, 06.04.2016.
 */
@StorableObject(fieldName = "notifications", boxName = "notifications")
public class StorableBaseNotification extends AbstractUniqueStorableObject
{
  public static final Field<String> creator = FieldFactory.field(String.class);
  public static final Field<String> target = FieldFactory.field(String.class);
  public static final Field<Long> startDate = FieldFactory.field(Long.class);
  public static final Field<Boolean> visibleForTarget = FieldFactory.field(Boolean.class);

  public String getCreator()
  {
    return getValue(creator);
  }

  public String getTarget()
  {
    return getValue(target);
  }

  public long getStartDate()
  {
    Long value = getValue(startDate);
    return value == null ? new Date().getTime() : value;
  }

  public boolean isVisibleForTarget()
  {
    Boolean value = getValue(visibleForTarget);
    return value == null ? false : value;
  }
}
