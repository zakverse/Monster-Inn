package monsterinn.modules.room.model;

import lombok.Getter;

@Getter
public enum RoomStatus {
    AVAILABLE("Available", "status-available"),
    OCCUPIED("Occupied", "status-occupied"),
    DIRTY("Dirty", "status-dirty"),
    MAINTENANCE("Maintenance", "status-maintenance");

    private final String label;
    private final String cssClass;

    RoomStatus(String label, String cssClass) {
        this.label = label;
        this.cssClass = cssClass;
        
    }
}