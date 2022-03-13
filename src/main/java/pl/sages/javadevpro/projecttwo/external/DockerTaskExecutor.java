package pl.sages.javadevpro.projecttwo.external;

import lombok.extern.java.Log;
import org.springframework.stereotype.Service;
import pl.sages.javadevpro.projecttwo.domain.ExecutionStatus;
import pl.sages.javadevpro.projecttwo.domain.task.Task;
import pl.sages.javadevpro.projecttwo.domain.TaskExecutor;

import java.io.IOException;

import static java.lang.Runtime.getRuntime;
import static pl.sages.javadevpro.projecttwo.domain.ExecutionStatus.*;

@Log
@Service
public class DockerTaskExecutor implements TaskExecutor {

    private static final int SUCCESS = 0;

    @Override
    public ExecutionStatus execute(Task task) {
        try {
            var command = prepareCommand(task.getWorkspaceUrl());
            var process = getRuntime().exec(command);
            return SUCCESS ==  process.waitFor() ? COMPLETED : FAILED;
        } catch (InterruptedException | IOException exception) {
            log.info(exception.toString());
            return FAILED;
        }
    }

    private String[] prepareCommand(String workspaceUrl) {
        return new String[] { "bash", "-c", "cd " + workspaceUrl + ";docker-compose up" };
    }

}
