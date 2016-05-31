package objects;

import annotations.StorableObject;
import definition.AbstractUniqueStorableObject;
import definition.fields.*;

/**
 * Beschreibt die Grundlage einer Erinnerung
 *
 * @author Simon Danner, 06.04.2016.
 */
@StorableObject(fieldName = "Notifications", boxName = "notifications")
public class BaseNotification extends AbstractUniqueStorableObject
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

  public long getStartDateColumn()
  {
    return getValue(startDate);
  }

  public boolean isVisibleForTarget()
  {
    return getValue(visibleForTarget);
  }
}
