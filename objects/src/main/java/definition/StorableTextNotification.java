package definition;

import annotations.StorableObject;
import definition.fields.*;

/**
 * Beschreibt eine konkrete beliebige Erinnerung, wo Titel und Details durch einen Freitext bestimmbar seind
 *
 * @author Simon Danner, 10.04.2016.
 */
@StorableObject(fieldName = "textnotifications", boxName = "notifications", allowInsertion = false)
public class StorableTextNotification extends StorableBaseNotification
{
  public static final Field<String> title = FieldFactory.field(String.class);
  public static final Field<String> description = FieldFactory.field(String.class);

  public String getTitle()
  {
    return getValue(title);
  }

  public String getDescription()
  {
    return getValue(description);
  }
}
