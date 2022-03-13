package pl.sages.javadevpro.projecttwo.domain;

import pl.sages.javadevpro.projecttwo.domain.task.Task;

public interface TaskProcessor {

    void onReceive(Task task);

}
