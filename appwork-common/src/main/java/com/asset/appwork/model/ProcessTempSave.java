package com.asset.appwork.model;

import com.asset.appwork.util.SystemUtil;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

@Data
@Entity
@Table(name = "ACA_Entity_processTempSave")
public class ProcessTempSave {

    @Id
    Long Id;

    private String outputSchema;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private Date pauseDate;
    private String processInstanceId;
    private String processName;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private Date resumeDate;
    private String taskId;
    private String status;

    public String toString() {
        return SystemUtil.writeObjectIntoString(this).replace(",\"id\":null", "");
    }
}
