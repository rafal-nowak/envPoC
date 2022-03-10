package pl.sages.javadevpro.projecttwo.external.env.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
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
