package pl.sages.javadevpro.projecttwo.external.env.domain;

import pl.sages.javadevpro.projecttwo.external.env.usertask.UserTaskEnv;

public interface TaskExecutor {

    TaskExecutorStatus execute();

    void setTask(UserTaskEnv task);

}
