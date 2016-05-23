package communication.wrapper;

/**
 * Speichert die Telfon-Nummer zusammen mit einem GCM-Token
 *
 * @author Simon Danner, 16.05.2016.
 */
public class TokenWrapper
{
  private String phoneNumber;
  private String token;

  public TokenWrapper(String pPhoneNumber, String pToken)
  {
    phoneNumber = pPhoneNumber;
    token = pToken;
  }
}
