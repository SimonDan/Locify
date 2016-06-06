package definition;

import annotations.StorableObject;
import definition.fields.*;

import java.io.Serializable;

/**
 * Beschreibt eine Erinnerungen, wo einer Person ein bestimmter Geld-Betrag geschuldet wird
 *
 * @author Simon Danner, 10.04.2016.
 */
@StorableObject(fieldName = "debtsnotifications", boxName = "notifications", allowInsertion = false)
public class StorableDebtsNotification extends StorableBaseNotification implements Serializable
{
  public static final Field<Double> amount = FieldFactory.field(Double.class);

  public double getAmount()
  {
    Double value = getValue(amount);
    return value == null ? 0.0 : value;
  }
}
