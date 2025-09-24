package src.models;

import java.time.LocalDateTime;

public class Session {
    private final Agent agent;
    private final LocalDateTime startedAt;

    public Session(Agent agent) {
        this.agent = agent;
        this.startedAt = LocalDateTime.now();
    }

    public Agent getAgent() {
        return agent;
    }

    public LocalDateTime getStartedAt() {
        return startedAt;
    }
}
