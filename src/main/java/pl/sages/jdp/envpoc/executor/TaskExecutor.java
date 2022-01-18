package pl.sages.jdp.envpoc.executor;

import lombok.AllArgsConstructor;
import pl.sages.jdp.envpoc.model.Task;

import java.io.IOException;

@AllArgsConstructor
public class TaskExecutor {

    private Task task;

    public int execute() {

        try {
            String commands = "cd " + task.getTaskPath() + "; docker-compose up";
            Process p = Runtime.getRuntime().exec(new String[]{"bash", "-c", commands});
            return p.waitFor();
        } catch (InterruptedException e) {
            e.printStackTrace();
            return -1;
        } catch (IOException e) {
            e.printStackTrace();
            return -1;
        }

    }
}
