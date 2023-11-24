package storageClasses;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import java.time.LocalDateTime;

public class Note {
    private static final Logger logger = LogManager.getLogger("StorageClasses.Note logger ");
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
        logger.debug("Creation date set for " + this);
    }

    public LocalDateTime getLastSeen() {
        if (creation == null) return LocalDateTime.now();
        return lastSeen;
    }

    public void setLastSeen(LocalDateTime lastSeen) {
        this.lastSeen = lastSeen;
        logger.debug("Last seen date set for " + this);
    }
    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
        logger.debug("Content set for " + this);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
        logger.debug("Name set for " + this);
    }
}
