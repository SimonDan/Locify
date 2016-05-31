package objects;

import annotations.StorableObject;
import definition.fields.*;

/**
 * Beschreibt eine Erinnerungen, wo einer Person ein bestimmter Geld-Betrag geschuldet wird
 *
 * @author Simon Danner, 10.04.2016.
 */
@StorableObject(fieldName = "DebtsNotifications", boxName = "notifications", allowInsertion = false)
public class DebtsNotification extends BaseNotification
{
  public static final Field<Double> amount = FieldFactory.field(Double.class);

  public double getAmount()
  {
    return getValue(amount);
  }
}
