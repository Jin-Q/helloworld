package com.yucheng.cmis.platform.workflow;

/**
 * 流程引擎接入常量类
 * @author liuhw 2013-7-1
 */

public class WorkFlowConstance {
	
	/**
	 * 信贷工作流接入模块ID
	 */
	public final static String WORKFLOW_MODUAL_ID = "workflow";
	
	/**
	 * 信贷工作流接入对外服务ID
	 */
	public final static String WORKFLOW_SERVICE_ID = "workflowService";
	
	/**
	 * 信贷流程组件ID
	 */
    public static final String WFI_COMPONENTID = "wfiComponent";
    
    /**
     * 信贷流程扩展会签组件ID
     */
    public static final String WFI_COMPONENTID4SIGN = "wfiSignComponent";
	
	/**
	 * 返回人员范围表示所有用户时的标识，一般将WFIUser的userId设置为${alluser}
	 */
	public final static String ALL_USER = "${alluser}";
	
	/**
	 * 流程接入使用的登录用户ID域名
	 */
	public final static String ATTR_USERID = "s_userid";
	
	/**
     * 流程接入使用的登录组织机构ID域名
     */
    public static final String ATTR_ORGID = "s_orgid";
    
    /**
     * 流程接入使用的系统ID参数名
     */
    public static final String ATTR_SYSID = "sysId";
    
    /**
	 * 流程接入信贷系统缺省机构ID标识
	 */
	public static final String WFI_CMIS_ORGID = "cmis";
	
	/**
	 * 流程接入信贷系统默认系统ID标识
	 */
	public static final String WFI_CMIS_SYSID = "cmis";
	
    /**
     * 流程开始节点ID
     */
	public static final String ATTR_BEGIN_NODEID = "WFBEGIN";
	
	/**
	 * 流程接入中间表模型ID
	 */
	public static final String ATTR_WFIJOIN_MODELID = "WfiJoin";
	
	/**
	 * 流程节点扩展属性——场景
	 */
	public static final String NODE_EXT_SCENE = "scene";
	
	/**
	 * 流程节点扩展属性——节点处理业务逻辑 0.不处理;1.处理
	 */
	public static final String NODE_EXT_IS_PROCESS = "isHandleBiz";
	
	/**
	 * 流程业务审批状态， 000——初始待发起状态
	 */
	public static final String WFI_STATUS_INIT = "000";
	
	/**
	 * 流程业务审批状态， 111——审批中状态
	 */
	public static final String WFI_STATUS_APPROVE = "111";
	
	/**
	 * 流程业务审批状态， 990——取消状态
	 */
	public static final String WFI_STATUS_CANCEL = "990" ;
	
	/**
	 * 流程业务审批状态， 991——拿回重办状态；当前办理环节是流程的第一个节点
	 */
	public static final String WFI_STATUS_AGAIN = "991" ;
	
	/**
	 * 流程业务审批状态， 992——打回状态；；当前办理环节是流程的第一个节点，包括退回，打回，跳转到第一个节点
	 */
	public static final String WFI_STATUS_BACK = "992" ;
	
	/**
	 * 流程业务审批状态， 993——追回状态；；流程发起人主动将流程追回到第一个节点
	 */
	public static final String WFI_STATUS_AGAIN_FIRST = "993" ;
	
	/**
	 * 流程业务审批状态， 997——审批通过状态
	 */
	public static final String WFI_STATUS_PASS = "997" ;
	
	/**
	 * 流程业务审批状态， 998——审批否决（不同意）状态
	 */
	public static final String WFI_STATUS_DENIAL = "998" ;
	
	
	/**
	 * 流程关联业务配置适用审批范围，111——审批中
	 */
	public static final String WFI_2BIZ_SCOPE_APPROVE = "111";
	
	/**
	 * 流程关联业务配置适用审批范围，997——同意通过
	 */
	public static final String WFI_2BIZ_SCOPE_PASS = "997";
	
	/**
	 * 流程关联业务配置适用审批范围，998——否决（不同意）状态
	 */
	public static final String WFI_2BIZ_SCOPE_DENIAL = "998";
	
	/**
	 * 流程关联业务配置适用审批范围，999——所有
	 */
	public static final String WFI_2BIZ_SCOPE_ALL = "999";
	
	

	/**
	 * 审查审批结论(对应审批意见标识)——提交（同意）
	 */
	public static final String WFI_RESULT_AGREE = "10";			//
	
	/**
	 * 审查审批结论(对应审批意见标识)——否决
	 */
	public static final String WFI_RESULT_DISAGREE = "20";		//否决
	
	/**
	 * 审查审批结论(对应审批意见标识)——打回
	 */
	public static final String WFI_RESULT_CALLBACK = "30";		//打回
	
	/**
	 * 审查审批结论(对应审批意见标识)——退回
	 */
	public static final String WFI_RESULT_RETURNBACK = "40";	//退回
	
	/**
	 * 审查审批结论(对应审批意见标识)——跳转
	 */
	public static final String WFI_RESULT_JUMP = "50";			//跳转
	
	/**
	 * 审查审批结论(对应审批意见标识)——拿回
	 */
	public static final String WFI_RESULT_AGAIN = "60";			//拿回
	
	/**
	 * 审查审批结论(对应审批意见标识)——发起人追回
	 */
	public static final String WFI_RESULT_AGAIN_FIRST = "61";   //发起人追回
	
	/**
	 * 审查审批结论(对应审批意见标识)——撤办
	 */
	public static final String WFI_RESULT_CANCEL = "70";		//取消
	
	/**
	 * 审查审批结论(对应审批意见标识)——挂起
	 */
	public static final String WFI_RESULT_HANG = "80";		//挂起
	
	/**
	 * 审查审批结论(对应审批意见标识)——唤醒
	 */
	public static final String WFI_RESULT_WAKE = "90";
	
	/**
	 * 审查审批结论(对应审批意见标识)——转办
	 */
	public static final String WFI_RESULT_CHANGE = "11";
	
	/**
	 * 审查审批结论(对应审批意见标识)——协办
	 */
	public static final String WFI_RESULT_ASSIST = "21";
	
	/**
	 * 审查审批结论(对应审批意见标识)——催办
	 */
	public static final String WFI_RESULT_URGE = "31";
	
	/** 消息处理状态  初始尚未处理*/
	public static final String WFI_MSG_OPSTATUS_INIT = "00";
	/** 消息处理状态  处理中*/
	public static final String WFI_MSG_OPSTATUS_DOING = "10";
	/**消息处理状态  回滚取消（引擎与业务处理同一事务，发生异常时产生）*/
	public static final String WFI_MSG_OPSTATUS_CANCEL = "70";
	/** 消息处理状态  异常*/
	public static final String WFI_MSG_OPSTATUS_ERROR = "90";
	/** 消息处理状态  处理完毕*/
	public static final String WFI_MSG_OPSTATUS_END = "99";
    
	/**
	 * EMPContext设置在流程表单中的key名称
	 */
	public static final String ATTR_EMPCONTEXT= "_emp_context";
	
	/**
	 * 流程审批的业务流程处理接口配置XML文件名
	 */
	public static final String WFI_BIZ_XML = "cmis-wfi-biz.xml";
	
	/**
	 * 流程审批的业务流程处理接口为空时(业务无处理接口)系统默认的接口ID
	 */
	public static final String WFI_BIZIF_BLANK = "blankProc";
	
	/**
	 * 流程节点属性——节点流向类型，对应值 0.一般处理;1.单选处理;2.多选处理;3.条件处理
	 */
	public static final String NODE_PROPERTY_ROUTERTYPE = "noderoutertype";
	
	/**
     * 获取流程下一节点操作后，存放context中的key名称
     */
    public static final String WF_NEXT_NODE_LIST = "NextNodeList";
    
    /**
     * 获取流程节点办理人操作后，存放context中的key名称
     */
    public static final String WF_NEXT_USER_LIST = "NextUserList";
    
    /**
	 * 流程节点属性——办理类型，对应值 0.单人签收办理;1.单人竞争办理;2.多人顺序办理;3.多人并行办理;4.按转移条件;5.多人顺序可结束;6.多人并行可结束
	 */
	public static final String NODE_PROPERTY_TRANSACTTYPE = "nodetransacttype";

	/**
	 * 流程节点属性——人员计算模式，对应值0.并集;1.交集;2.组间交集;3.原始配置
	 */
	public static final String NODE_PROPERTY_USERSCOMPUTE = "nodeuserscompute";
	
	/**
	 * 流程节点扩展属性——会签策略(独立会签模块)，对应值 0.非会签;1.一票否决;2.牵头人一票否决;3.三分之二通过;4.半数通过（1；2；3；4只为示例，值对应实际的会签策略id）
	 */
	public static final String NODE_EXT_PROPERTY_SIGNCONFIG = "signConfig";
	
	/**
	 * 信贷流程扩展会签的匿名办理人 signUser
	 */
	public static final String SIGN_USER = "signUser";
	
	/**
	 * 节点的办理人员指定设置为:[2.系统指定]时，系统指定的系统用户名称
	 */
	public static final String WFI_SYS_NODEUSER = "wfiSysNodeUser";

	/**
	 * 流程实例号参数名，用于传参取值
	 */
	public static final String ATTR_INSTANCEID = "instanceId";
	
	/**
	 * 流程申请类型参数名，用于传参取值
	 */
	public static final String ATTR_APPLTYPE = "applType";
	
	/**
     * 流程节点ID参数名，用于传参取值
     */
    public static final String ATTR_NODEID = "nodeId";
    
    /**
     * 流程下一节点ID参数名，用于传参取值
     */
    public static final String ATTR_NEXTNODEID = "nextNodeId";
    
    /**
     * 流程下一办理人参数名，用于传参取值
     */
    public static final String ATTR_NEXTNODEUSER = "nextNodeUser";
    
    /**
     * 流程抄送人员参数名，用于传参取值
     */
    public static final String ATTR_NEXTANNOUCEUSER = "nextAnnouceUser";
    
    /**
     * 审批结论（意见标识），用于传参取值
     */
    public static final String ATTR_WFI_RESULT = "commentSign";
	
    /**
     * 流程操作返回WFVO，存放在context中的key名称
     */
    public static final String WFVO_RET_NAME = "_WFVO_retObj";
    
    
    /**
     * 会签投票任务状态——同意110
     */
    public static final String AGREE_VOTE = "110";
    /**
     * 会签投票任务状态——否决111
     */
    public static final String REJECT_VOTE = "111";
    /**
     * 会签投票任务状态——复议112
     */
    public static final String NOIDEA_VOTE = "112";
    
    /**
     * 会签任务状态——等待开始210
     */
    public static final String WAIT_BEGIN = "210";
    /**
     * 会签任务状态——等待投票211
     */
    public static final String WAIT_VOTE = "211";
    /**
     * 会签任务状态——正在投票212
     */
    public static final String NOW_VOTE = "212";
    /**
     * 会签任务状态——投票结束213
     */
    public static final String FINISH_VOTE = "213";
    /**
     * 会签任务状态——会议重开214
     */
    public static final String RESTART_VOTE = "214";
    /**
     * 会签任务状态——自动弃权215
     */
    public static final String AUTO_FINISH = "215";
    /**
     * 会签任务状态——取消会议216
     */
    public static final String CANCEL_TASK = "216";
    /**
     * 会签任务状态——会议结束217
     */
    public static final String FINISH_TASK = "217";
    
    
}
