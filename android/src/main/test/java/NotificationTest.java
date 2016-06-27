
import notification.NotificationUtil;
import org.junit.Test;
import storable.*;

import static org.junit.Assert.assertEquals;

/***
 * Test-Klasse für die Notification De- und Serialisierung
 *
 * @author Simon Danner, 27.06.2016.
 */
public class NotificationTest
{
  private static final double DELTA = 1e-15;

  //Base
  private static final String BASE_CREATOR = "+49156123456";
  private static final String BASE_TARGET = "+491561234567";
  private static final long BASE_START_DATE = 1467046331000L;
  private static final boolean BASE_VISIBLE_FOR_TARGET = true;

  //Debts
  private static final double DEBTS_AMOUNT = 45.30;
  private static final String DEBTS_DETAILS = "Fürs Kino";

  //Text
  private static final String TEXT_TITLE = "CD Mitbringen";
  private static final String TEXT_DETAILS = "Nicht vergessen!";


  @Test
  public void testNotificationSerialization()
  {
    StorableDebtsNotification debts = _createTestDebtsNotification();
    StorableTextNotification text = _createTestTextNotification();

    String serialDebts = NotificationUtil.getNotificationAsString(debts);
    String serialText = NotificationUtil.getNotificationAsString(text);

    StorableDebtsNotification deserialDebts = (StorableDebtsNotification) NotificationUtil.getNotificationFromString(serialDebts);
    StorableTextNotification deserialText = (StorableTextNotification) NotificationUtil.getNotificationFromString(serialText);

    _checkBase(debts, deserialDebts);
    _checkBase(text, deserialText);

    assertEquals(debts.getAmount(), deserialDebts.getAmount(), DELTA);
    assertEquals(debts.getDetails(), deserialDebts.getDetails());
    assertEquals(text.getTitle(), deserialText.getTitle());
    assertEquals(text.getDescription(), deserialText.getDescription());
  }

  private void _checkBase(StorableBaseNotification pNotification1, StorableBaseNotification pNotification2)
  {
    assertEquals(pNotification1.getCreator(), pNotification2.getCreator());
    assertEquals(pNotification1.getTarget(), pNotification2.getTarget());
    assertEquals(pNotification1.getStartDate(), pNotification2.getStartDate());
    assertEquals(pNotification1.isVisibleForTarget(), pNotification2.isVisibleForTarget());
  }

  private StorableDebtsNotification _createTestDebtsNotification()
  {
    StorableDebtsNotification debtsNotification = new StorableDebtsNotification();
    _setBaseValues(debtsNotification);
    debtsNotification.setValue(StorableDebtsNotification.amount, DEBTS_AMOUNT);
    debtsNotification.setValue(StorableDebtsNotification.details, DEBTS_DETAILS);
    return debtsNotification;
  }

  private StorableTextNotification _createTestTextNotification()
  {
    StorableTextNotification textNotification = new StorableTextNotification();
    _setBaseValues(textNotification);
    textNotification.setValue(StorableTextNotification.title, TEXT_TITLE);
    textNotification.setValue(StorableTextNotification.description, TEXT_DETAILS);
    return textNotification;
  }

  private void _setBaseValues(StorableBaseNotification pNotification)
  {
    pNotification.setValue(StorableBaseNotification.creator, BASE_CREATOR);
    pNotification.setValue(StorableBaseNotification.target, BASE_TARGET);
    pNotification.setValue(StorableBaseNotification.startDate, BASE_START_DATE);
    pNotification.setValue(StorableBaseNotification.visibleForTarget, BASE_VISIBLE_FOR_TARGET);
  }
}
