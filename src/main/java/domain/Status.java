package domain;

/**
 * Created by Kevin.
 */
public enum Status {
    Offline("Offline"),
    Online("Online"),
    Busy("Busy");

    private final String statusName;

    Status(String status) {
        this.statusName = status;
    }

    public String getStatusName() {
        return statusName;
    }
}
