package pl.sages.javadevpro.projecttwo.external.env.executor;

import lombok.NoArgsConstructor;
import org.springframework.stereotype.Service;
import pl.sages.javadevpro.projecttwo.external.env.usertask.UserTaskEnv;

import java.io.IOException;

@Service
@NoArgsConstructor
public class TaskExecutorService implements TaskExecutor{

    private UserTaskEnv task;

    public void setTask(UserTaskEnv task) {
        this.task = task;
    }

    @Override
    public TaskExecutorStatus execute() {

        try {
            String commands = "cd " + task.getUserTaskFolder() + "; docker-compose up";
            Process p = Runtime.getRuntime().exec(new String[]{"bash", "-c", commands});
            int status = p.waitFor();
            if (status == 0)
                return TaskExecutorStatus.COMPLETED;
            return TaskExecutorStatus.FAILED;
        } catch (InterruptedException | IOException e) {
            e.printStackTrace();
            return TaskExecutorStatus.FAILED;
        }

    }
}
