package frontend;

import javafx.event.Event;
import javafx.event.EventType;

public class UpdateRequestedEvent extends Event {

  public static final EventType<UpdateRequestedEvent> UPDATE = new EventType<>(ANY);
  private final String targetId;

  public UpdateRequestedEvent(String targetId) {
    super(UPDATE);
    this.targetId = targetId;
  }

  public String getTargetId() {
    return targetId;
  }
}
