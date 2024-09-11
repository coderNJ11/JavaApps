package model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.Date;
import java.util.List;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class ProcessInstance {

    private String id;
    private String state;

    private String templateId;
    private String templateName;
    private String processId;
    private String processName;
    private String processType;

    private Date createdDate;
    private Date lastUpdatedDate;

    private List<TaskInfo> activeNodes;

    private Long version;

    private String formTitle;

    private String appId;

    private String formId;

    private String initiator;


    private ProcessInsatnceInfo processInsatnceInfo;




}
