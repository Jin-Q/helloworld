package com.yucheng.cmis.biz01line.ind;

import java.io.File;
import java.util.HashMap;
import java.util.ResourceBundle;

import com.yucheng.cmis.base.CMISConstance;

public class IndPubConstant {
	
    
	/**
	 * 指标管理组件id
	 */
	public static final String IND_COMPONENT="ind";
	
	public static final String IND_GET_MODNO_CCR="IndGetModNoForCcr";
	/**
	 * 
	 * 模型
	 * 
	 */
	public static final String IND_MODEL="IndModel";
	
	public static final String IND_MODEL_GROUP="IndModelGroup";
	
	public static final String IND_GROUP_INDEX ="IndGroupIndex";
	
	public static final String IND_GROUP ="IndGroup";
	
	public static final String IND_LIB="IndLib";
	
	public static final String IND_AGENT = "IndAgent";
	
	public static final String IND_OPT="IndOpt";
	
	/**
	 * 
	 * velocity 模板根目录
	 */
	public static final String VM_PATH="/com/yucheng/cmis/config/biz01line/ccr";
	
	/**
	 * velocity 生成jsp文件的根目录
	 */
	private static String VM_JSP_PATH=null;
	public static String getVM_JSP_PATH(){
	    if(VM_JSP_PATH==null){
	        ResourceBundle res = ResourceBundle.getBundle("cmis");
	        String dir = res.getString("ind.file.path");   //模板文件生成路径改为
//			String path=ResourceUtils.getFile(dir).getAbsolutePath();
	        //String dir = CMISConstance.PERMISSIONFILE_PATH.replace("permissions", "");  
	       // File file=new File(dir+"mvcs/CMISMvc/biz01line/ind/jspfiles");
	        File file=new File(dir);
	        if(!file.exists())
	            file.mkdirs();
	        VM_JSP_PATH=file.getAbsolutePath()+File.separator;
	    }
	        return VM_JSP_PATH;
	    
	}
	/**
	 * 风险分类企事业velocity模板文件
	 */
	public static final String RSC_VM_FILE="RscCusComIndex.jsp.vm";
	
	/**
	 * 客户评级velocity模板文件
	 */
	public static final String CCR_VM_FILE="CcrIndex.jsp.vm";
	
	/**
	 *  组定义表的DOMAIN路径
	 */
	public static final String INDGROUP_className="com.yucheng.cmis.biz01line.ind.domain.IndGroupDomain";
	
	/**
	 * 指标表的DOMAIN路径
	 */
	public static final String INDLIB_className="com.yucheng.cmis.biz01line.ind.domain.IndLibDomain";
	/**
	 * 模型组指标接口
	 */
	public static final String IND_MOD_GRP_INDEX_IFACE_ID="IndModGrpIndIface";
	/**
	 * 指标评分接口
	 */
	public static final String IND_CAL_SCORE_IFACE_ID="IndCalScoreIface";
	
	/**
	 * 插入指标结果表接口
	 */
	public static final String IND_INSERT_RESULT_VAL="IndInsertResultface";
	
	
	/**
	 * 获取指标选项描述接口
	 */
	public static final String GET_IND_OPTION_DESC="GetIndOptionDescIface";
	
	/**
	 * 获取指标结果删除接口
	 */
	public static final String DELETE_IND_RESULT_VAL="DeleteIndResultVal";
	
	public static final String IND_RSCNOTCUSCOM_IFACE_ID="";
	
	
	/**
	 * 自然人其他贷款和微型企业贷款的分类标准接口
	 */
	public static final String IND_OTHER_MODEL_SCORE_IFACE="IndOtherModelScoreIface";
	
	/**
	 * 自然人一般农户贷款分类接口
	 */
	public static final String IND_NATUCOMON_MODEL_IFACEe="IndNatuComonModelScoreIface";
	
	/**
	 * 指标显示类型  文本框
	 */
	public static final String IND_DISPLAY_TYPE_TEXT="1";
	/**
	 * 指标显示类型  文本域
	 */
	public static final String IND_DISPLAY_TYPE_TEXTAREA="2";
	/**
	 * 指标显示类型  单选框
	 */
	public static final String IND_DISPLAY_TYPE_RADIO="3";
	/**
	 * 指标显示类型  复选框
	 */
	public static final String IND_DISPLAY_TYPE_CHECKBOX="4";
	/**
	 * 指标显示类型  下拉框
	 */
	public static final String IND_DISPLAY_TYPE_SELECTONE="5";
	/**
	 * 复选框值分隔符
	 */
	public static final String IND_CHECKBOX_DELIMITER=",";
	/**
	 * 多选得分实现类路径
	 */
	public static final String IND_MUTISEL_CAL_SC_CP="com.yucheng.cmis.biz01line.ind.interfaces.impl.MultiSelectIndexScoreImpl";
	
	/**
	 *  评分中较好的取值
	 */
	public static final int BETTER = 10;
	
	/**
	 *  评分中较差的取值
	 */
	public static final int INFERIOR = 30;
	
	/**
	 *  评分中跳动一级变化的分值
	 */
	public static final int SKIP = 10;
	
	/**
	 *  风险分类中正常的取值
	 */
	public static final int RSC_NORMAL = 10;
	
	/**
	 *  风险分类中损失的取值
	 */
	public static final int RSC_LOSE = 50;
	
	/**
	 *  担保方式中的信用
	 */
	public static final String CREDIT = "00";
	
	/**
	 *  担保方式中的抵押
	 */
	public static final String GUARANTY = "10";
	
	/**
	 *  担保方式中的质押
	 */
	public static final String IMPAWN = "20";
	
	/**
	 *  担保方式中的保证
	 */
	public static final String ASSURE = "30";
	
	/**
	 *  五级分类中的正常
	 */
	public static final String NORMAL = "10";
	
	/**
	 *  五级分类中的关注
	 */
	public static final String ATTENTION = "20";
	/**
	 *  五级分类中的次级
	 */
	public static final String SUB = "30";
	/**
	 *  五级分类中的可疑
	 */
	public static final String DOUBT = "40";
	
	/**
	 *  逾期天数为0天
	 */
	public static final int OVERDUEDAYS_0 = 0;
	
	/**
	 *  逾期天数为30天
	 */
	public static final int OVERDUEDAYS_30 = 30;
	
	/**
	 *  逾期天数为60天
	 */
	public static final int OVERDUEDAYS_60 = 60;
	
	/**
	 *  逾期天数为90天
	 */
	public static final int OVERDUEDAYS_90 = 90;
	
	/**
	 *  逾期天数为120天
	 */
	public static final int OVERDUEDAYS_120 = 120;
	
	/**
	 *  逾期天数为180天
	 */
	public static final int OVERDUEDAYS_180 = 180;
	
	
	/**
	 *  逾期天数为270天
	 */
	public static final int OVERDUEDAYS_270 = 270;
	
	/**
	 *  逾期天数为360天
	 */
	public static final int OVERDUEDAYS_360 = 360;
	
	/**
	 * 事业单位财务类型
	 */
	public static String CAREER_FAN_TYPE= "PB0003,";
	
	/**
	 * 取值方式
	 */
	public static   HashMap<String,String> CCRINPUTTYPE=new HashMap<String,String>();
	static{
    	/*
    	 * '1':'系统判定', '2':'手工录入'
    	 * */
		CCRINPUTTYPE.put("自动计算","1");
		CCRINPUTTYPE.put("手工录入","2");
    	
    }
	/**
	 * 显示类型
	 */
	public static   HashMap<String,String> CCRINDDISTYPE=new HashMap<String,String>();
	static{
    	/*
    	 * 显示类型
    	 * '1':'TEXT', '2':'TEXTAREA', '3':'RADIO', '4':'CHECKBOX', '5':'SELECTONE', '6':'SELECTMULITE', '7':'DATE'
    	 * */
		CCRINDDISTYPE.put("输入框","1");
    	CCRINDDISTYPE.put("2","2");
    	CCRINDDISTYPE.put("单选","3");
    	CCRINDDISTYPE.put("多选","4");
    	CCRINDDISTYPE.put("5","5");
    	CCRINDDISTYPE.put("6","6");
    	CCRINDDISTYPE.put("7","7");
    	
    }
	
}
