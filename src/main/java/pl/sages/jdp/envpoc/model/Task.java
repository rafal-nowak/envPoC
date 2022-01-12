package pl.sages.jdp.envpoc.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Task {

    private String taskId;
    private String userId;
    private String taskPath;
    private String taskStatus;

}
