import java.time.LocalDateTime;

public class Note {
    private String content;
    private String name;
    private LocalDateTime creation;
    private LocalDateTime lastSeen;

    public LocalDateTime getCreation() {
        if (creation == null) return LocalDateTime.now();
        return creation;
    }

    public void setCreation(LocalDateTime creation) {
        this.creation = creation;
    }

    public LocalDateTime getLastSeen() {
        if (creation == null) return LocalDateTime.now();
        return lastSeen;
    }

    public void setLastSeen(LocalDateTime lastSeen) {
        this.lastSeen = lastSeen;
    }
    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
