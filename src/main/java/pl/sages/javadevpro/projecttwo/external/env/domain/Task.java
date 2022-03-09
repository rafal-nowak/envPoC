package pl.sages.javadevpro.projecttwo.external.env.domain;

import lombok.Value;

@Value
public class Task {

    String id;
    String name;
    String description;
    String workspaceUrl;
    TaskStatus status;

    public Task withStatus(TaskStatus status) {
        return new Task(id, name, description, workspaceUrl, status);
    }

}
