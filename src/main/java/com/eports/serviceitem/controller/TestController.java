package com.eports.serviceitem.controller;

import org.activiti.engine.*;
import org.activiti.engine.history.HistoricActivityInstance;
import org.activiti.engine.identity.User;
import org.activiti.engine.impl.persistence.entity.GroupEntity;
import org.activiti.engine.impl.persistence.entity.UserEntity;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.repository.ProcessDefinitionQuery;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;

/**
 * @Author: kevin
 * @Date: 2018/12/12 下午3:19
 * @Version 1.0
 */
@RestController
public class TestController {

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
    private ManagementService managementService;
    /**
     * 提交
     */






    /**
     * 查看图片
     * @return
     */
//    @GetMapping("viewImage")
//    public String viewImage(){
//        InputStream resourceAsStream = repositoryService.getResourceAsStream("22501", "diagrams/ctm.progress.myProcess_1.png");
//
//        InputStream in = repositoryService.getResourceAsStream().getImageStream(deploymentId,imageName);//此处方法实际项目应该放在service里面
//        HttpServletResponse resp = ServletActionContext.getResponse();
//        try {
//            OutputStream out = resp.getOutputStream();
//            // 把图片的输入流程写入resp的输出流中
//            byte[] b = new byte[1024];
//            for (int len = -1; (len= in.read(b))!=-1; ) {
//                out.write(b, 0, len);
//            }
//            // 关闭流
//            out.close();
//            in.close();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        return null;
//    }


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
     * 删除流程
     * @return
     */
    @GetMapping("delete")
    public String delete(String id) {

        String simpleName = this.getClass().getSimpleName();

        repositoryService.deleteDeployment(id);
        return "delete";
    }



    /**
     * classpath加载文件,返回发布id   管理者创建  deployid存储在服务项config表中
     * @return
     */
    @GetMapping("deploy")
    public String deploy() {
        Deployment ctmprocess = repositoryService.createDeployment().addClasspathResource("diagrams/demo2.bpmn")
                .name("ctmprocess")
                .deploy();
        String id = ctmprocess.getId();


        ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery().deploymentId(id).singleResult();

        return "部署后,流程定义key为:"+processDefinition.getKey();
    }
    /**
     * 委托方指定给代理方   要设置所有的节点?
     * @return
     */
    @GetMapping("startanddistribute")
    public List<Task> mytask(@RequestParam("weituo")String weituo,@RequestParam("daili")String daili,@RequestParam("defkey")String defkey) {
        HashMap<String, Object> variables = new HashMap<>();
        variables.put("weituo",weituo);
        variables.put("daili",daili);

        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey(defkey, variables);

        //返回的是列表  根据业务id
        //启动后查看任务 根据实例id
        String processInstanceid = processInstance.getId();
        List<Task> list = taskService.createTaskQuery().processInstanceId(processInstanceid).list();

        return list;
    }

    /**
     * 委托方指定给代理方   要设置所有的节点? 利用监听类 设置办理人
     * @return
     */
    @GetMapping("start")
    public List<Task> start(@RequestParam("defkey")String defkey) {
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey(defkey);

        //返回的是列表  根据业务id
        //启动后查看任务 根据实例id
        String processInstanceid = processInstance.getId();
        List<Task> list = taskService.createTaskQuery().processInstanceId(processInstanceid).list();

        return list;
    }

    /**
     * 查询自己的历史
     * @return
     */
    @GetMapping("myhis")
    public Object myhis(@RequestParam("name")String name) {
        //返回的是列表

        List<HistoricActivityInstance> list = historyService.createHistoricActivityInstanceQuery()
                .taskAssignee(name)
                .list();
        return list;
    }

    /**
     * 查询自己的节点
     * @return
     */
    @GetMapping("mytask")
    public String mytask(@RequestParam("name")String name) {
        //返回的是列表
        List<Task> kevin = taskService.createTaskQuery().taskAssignee(name).list();
        for (Task task : kevin) {
            System.out.println("任务id"+task.getId());
            System.out.println("任务名称"+task.getName());
            System.out.println("任务时间"+task.getCreateTime());
            System.out.println("任务办理人"+task.getAssignee());
            System.out.println("流程实例id"+task.getProcessInstanceId());
            System.out.println("执行实例id"+task.getExecutionId());
        }

        return "mytask";
    }






    /**
     * 查看关系
     * @return
     */
    @GetMapping("checkship")
    public List<User> checkship() {

        List<User> list = identityService.createUserQuery().list();

        return list;
    }

    /**
     * 查看所有任务
     * @return
     */
    @GetMapping("gettask")
    public List<Task> gettask() {

        List<Task> list = taskService.createTaskQuery().list();

        return list;
    }




    /**
     * 构建关系
     * @return
     */
    @GetMapping("relationship")
    public String relationship(){
        User user1 = new UserEntity();
        User user2 = new UserEntity();
        user1.setId("001");
        user1.setFirstName("weituokevin");
        user1.setEmail("sinopoc@163.com");
        user2.setId("002");
        user2.setFirstName("dailikevin2");
        user2.setEmail("memorn@163.com");

        identityService.saveUser(user1);
        identityService.saveUser(user2);

        GroupEntity weituo = new GroupEntity();
        GroupEntity daili = new GroupEntity();
        weituo.setId("weituoid");
        daili.setId("dailiid");
        weituo.setName("weituoname");
        daili.setName("dailiname");

        identityService.saveGroup(weituo);
        identityService.saveGroup(daili);

        identityService.createMembership("001","weituoid");
        identityService.createMembership("002","dailiid");

        return "success";
    }




    /**
     * 部署文件
     * @return
     */
    @GetMapping("test")
    public String test(){
        Deployment deploy = repositoryService.createDeployment().addClasspathResource("diagrams/demo2.bpmn").deploy();
        String deployid = deploy.getId();
        List<Deployment> list1 = repositoryService.createDeploymentQuery().deploymentId(deployid).list();



        ProcessDefinitionQuery processDefinitionQuery = repositoryService.createProcessDefinitionQuery();
        List<ProcessDefinition> list = processDefinitionQuery.list();

        return "success";
    }

    /**
     * 获取流程对象
     * @return
     */
    @GetMapping("getdef")
    public String getdef(){
        List<Deployment> list = repositoryService.createDeploymentQuery().list();
        List<ProcessDefinition> list1 = repositoryService.createProcessDefinitionQuery().deploymentId("1").list();

        System.out.println(list);
        System.out.println(list1);

        return "success";
    }


}
