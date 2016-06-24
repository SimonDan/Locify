package communication;

/**
 * Definiert ein Callback, welches nach einer BackgroundTask ausgeführt wird
 * Es liefert das Ergebnis und gibt an, ob der Server erreichbar war
 *
 * @author Simon Danner, 24.04.2016.
 */
public interface ITaskCallback<T>
{
  void onFinish(T pResult, boolean pIsServerUnavailable);
}
