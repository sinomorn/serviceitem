package com.eports.serviceitem.controller;

import com.eports.serviceitem.model.CtmSubmitAo;
import org.activiti.engine.*;
import org.activiti.engine.form.FormProperty;
import org.activiti.engine.form.TaskFormData;
import org.activiti.engine.history.HistoricActivityInstance;
import org.activiti.engine.history.HistoricDetail;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.Execution;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author: kevin
 * @Date: 2018/12/13 下午5:59
 * 送美金上传服务  服务表entry-执行对象表exeid--taskid--his comment
 * @Version 1.0
 */

@RestController
@RequestMapping("ctm")
public class CtmProgressController {
    @Autowired
    private RepositoryService repositoryService;
    @Autowired
    private RuntimeService runtimeService;

    @Autowired
    private TaskService taskService;

    @Autowired
    private IdentityService identityService;

    @Autowired
    private HistoryService historyService;

    @Autowired
    private FormService formService;
    /**
     * 委托方确认服务,开启执行流程
     * @param ctmSubmitAo
     */
    @PostMapping("submitCtm")
    public void submitCtm(@RequestBody CtmSubmitAo ctmSubmitAo){
        //服务项-流程定义id
        //通过服务项获取流程定义对象
        //开启流程 流程记录一个订单id>bussinesskey runtimeservice
        //委托方提交申请表单,orderid,weituoid,dailiid,comment,开启并开启流程,并完成自己的第一个任务,流程id关联进orderid orderid-实例id
        String weituo="weituo1";
        String daili="daili1";
    }

    /**
     * 开启一条流程  执行
     * @param deploykey
     */
    @GetMapping("submit")
    public ProcessInstance submit(@RequestParam("deploykey")String deploykey){
        //服务项-流程定义id
        //通过服务项获取流程定义对象
        //开启流程 流程记录一个订单id>bussinesskey runtimeservice
        //委托方提交申请表单,orderid,weituoid,dailiid,comment,开启并开启流程,并完成自己的第一个任务,流程id关联进orderid orderid-实例id
        String weituo="weituo1";
        String daili="daili1";
        HashMap<String, Object> stringObjectHashMap = new HashMap<>();
        stringObjectHashMap.put("weituo",weituo);
        stringObjectHashMap.put("daili",daili);
        //开启一条流程
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey(deploykey, stringObjectHashMap);

        String instanceId = processInstance.getId();
        //可以获取taskid
        Execution execution = runtimeService.createExecutionQuery().executionId(instanceId).singleResult();
        System.out.println("executionid是"+execution.getId());
        return processInstance;
    }



    /**
     * 查看自己的任务
     */
    @GetMapping("mytask")
    public List<Task>  mytask(@RequestParam("name")String name) {
        //返回的是列表
        List<Task> tasklist = taskService.createTaskQuery().taskAssignee(name).list();
        for (Task task : tasklist) {
            System.out.println("任务id"+task.getId());
            System.out.println("任务名称"+task.getName());
            System.out.println("任务时间"+task.getCreateTime());
            System.out.println("任务办理人"+task.getAssignee());
            System.out.println("流程实例id"+task.getProcessInstanceId());
            System.out.println("执行实例id"+task.getExecutionId());
        }

        TaskFormData taskFormData = formService.getTaskFormData("taskid");
        List<FormProperty> formProperties = taskFormData.getFormProperties();

        return tasklist;
    }


    /**
     * 完成任务
     * @return
     */
    @GetMapping("complete")
    public String complete(@RequestParam("taskid")String taskid) {
        //完成前需要查询 ,根据任务id查询
        taskService.complete(taskid);

        return "complete";
    }
    /**
     * 代理方完成任务
     * @return
     */
    @GetMapping("dailicomplete")
    public String dailicomplete(@RequestParam("taskid")String taskid) {

        //完成前需要查询 ,根据任务id查询
        Map<String, Object> objectObjectHashMap = new HashMap<>();
        objectObjectHashMap.put("confirm", "1");
        taskService.complete(taskid,objectObjectHashMap);

        return "dailicomplete";
    }


    /**
     * 代理方执行任务
     * @return
     */
    @GetMapping("dailidone")
    public String dailidone(@RequestParam("taskid")String taskid) {

        //完成前需要查询 ,根据任务id查询
        Map<String, Object> objectObjectHashMap = new HashMap<>();
        objectObjectHashMap.put("done", "1");
        taskService.complete(taskid,objectObjectHashMap);

        return "dailidone";
    }


    /**
     * 查询自己的历史
     * @return
     */
    @GetMapping("myhistory")
    public Object myhis(@RequestParam("name")String name,@RequestParam("instanceid")String instanceid) {
        //返回的是列表
        List<HistoricDetail> list1 = historyService.createHistoricDetailQuery().processInstanceId(instanceid).list();
        List<HistoricActivityInstance> list = historyService.createHistoricActivityInstanceQuery()
                .taskAssignee(name)
                .list();

        System.out.println(list);

        return list1;
    }





    /**
     * classpath加载文件,返回发布id   管理者创建  deployid存储在服务项config表中
     * @return
     */
    @GetMapping("deploy")
    public String deploy() {
        Deployment ctmprocess = repositoryService.createDeployment().addClasspathResource("diagrams/CtmProgress.bpmn")
                .name("CtmProgress")
                .deploy();
        String id = ctmprocess.getId();


        ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery().deploymentId(id).singleResult();

        return "部署后,流程定义key为:"+processDefinition.getKey();
    }

    /**
     * 查看任务进度
     */



    /**
     *
     */

    //

}
