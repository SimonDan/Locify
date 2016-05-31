package util;

/**
 * Beschreibt das (JSON-) Objekt, welches als Push-Nachricht an die Apps versendet werden soll
 *
 * @author Simon Danner, 16.05.2016.
 */
public class PushContent
{
  private String to;
  private _Helper data;

  public PushContent(String pNotificationID, String pToken)
  {
    to = pToken;
    data = new _Helper(pNotificationID);
  }

  private class _Helper
  {
    private String message;

    public _Helper(String pMessage)
    {
      message = pMessage;
    }
  }
}
