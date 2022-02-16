package pl.sages.javadevpro.projecttwo.external.env.executor;

import lombok.AllArgsConstructor;
import pl.sages.javadevpro.projecttwo.external.env.usertask.UserTaskEnv;

import java.io.IOException;

@AllArgsConstructor
public class TaskExecutor {

    private UserTaskEnv task;

    public int execute() {

        try {
            String commands = "cd " + task.getUserTaskFolder() + "; docker-compose up";
            Process p = Runtime.getRuntime().exec(new String[]{"bash", "-c", commands});
            return p.waitFor();
        } catch (InterruptedException | IOException e) {
            e.printStackTrace();
            return -1;
        }

    }
}
