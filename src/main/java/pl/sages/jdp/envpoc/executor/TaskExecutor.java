package pl.sages.jdp.envpoc.executor;

import lombok.AllArgsConstructor;
import pl.sages.jdp.envpoc.model.Task;

import java.io.IOException;

@AllArgsConstructor
public class TaskExecutor {

    private Task task;

    public void execute() {

        try {
            String commands = "cd " + task.getTaskPath() + "; docker-compose up; sleep 10";
            Process p = Runtime.getRuntime().exec(new String[]{"bash","-c",commands});
            p.waitFor();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
