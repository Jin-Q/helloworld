<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<%@page import="java.sql.Connection"%>
<%@page import="com.ecc.emp.core.EMPConstance"%>
<%@page import="com.ecc.emp.core.Context"%>
<%@page import="com.ecc.emp.dbmodel.service.TableModelDAO"%>
<%@page import="com.yucheng.cmis.base.CMISConstance"%>
<%@page import="com.ecc.emp.data.KeyedCollection"%>
<%@page import="com.yucheng.cmis.pub.util.SInfoUtils"%>
<%@page import="com.yucheng.cmis.pub.util.SystemTransUtils"%>
<%@page import="com.ecc.emp.jdbc.ConnectionManager"%>
<%@page import="com.yucheng.cmis.pub.CMISComponentFactory"%>
<%@page import="com.yucheng.cmis.biz01line.cus.cusbase.domain.CusBase"%>
<%@page import="com.yucheng.cmis.biz01line.cus.cusbase.component.CusBaseComponent"%>
<%@page import="com.yucheng.cmis.platform.workflow.domain.WFIInstanceVO"%>

<%
String serno = request.getParameter("serno");
Context context = (Context)request.getAttribute(EMPConstance.ATTR_CONTEXT);
if(serno==null||"".equals(serno)){
	serno = (String)context.getDataValue("pkVal");
}
String adjusted_grade = "";
if(serno!=null&&!"".equals(serno)){
	com.ecc.emp.jdbc.JNDIDataSource jndiDataSource = (com.ecc.emp.jdbc.JNDIDataSource)context.getService("dataSource");
	Connection connection = jndiDataSource.getConnection();
	TableModelDAO dao = (TableModelDAO)context.getService(CMISConstance.ATTR_TABLEMODELDAO);
	KeyedCollection kCollInfo = dao.queryDetail("CcrAppInfo", serno, connection);
	KeyedCollection kCollDet = dao.queryDetail("CcrAppDetail", serno, connection);
	String model_no = (String)kCollDet.getDataValue("model_no");
	KeyedCollection modelKcoll = dao.queryDetail("IndModel",model_no,connection);
	kCollDet.put("model_no_displayname",modelKcoll.getDataValue("model_name"));
	context.addDataElement(kCollInfo);
	context.addDataElement(kCollDet);
	String cus_id = (String)kCollInfo.getDataValue("cus_id");
	if(cus_id!=null&&!"".equals(cus_id)){
		CusBaseComponent cusComponent = (CusBaseComponent)CMISComponentFactory.getComponentFactoryInstance().getComponentInstance("CusBase", context,connection);
		CusBase cusBase = cusComponent.getCusBase(cus_id);
		String cus_name = cusBase.getCusName();
		kCollInfo.put("cus_id_displayname",cus_name);
	}
	SInfoUtils.addSOrgName(kCollInfo, new String[] { "manager_br_id" });
	SInfoUtils.addUSerName(kCollInfo, new String[] { "manager_id" });
	//如果上一节点是起始节点则变更前值取申请表中的值
	KeyedCollection kColl = (KeyedCollection)context.getDataElement("WfiBizVarRecord");
	adjusted_grade = (String)kCollDet.getDataValue("adjusted_grade");
	if(context.containsKey("isFirstNode")&&(Boolean)context.getDataValue("isFirstNode")==true){
		kColl.put("adjusted_grade_old",adjusted_grade);
	}
	//释放数据库连接
	if(connection!=null){
		ConnectionManager.releaseConnection(jndiDataSource, connection);
	}
}
%>

<emp:page>
<script type="text/javascript">
   function onVarSubmit(){
      /*将金额放入对应的 WfiVarShow 之中*/
      WfiVarDisp.adjusted_grade._setValue(WfiBizVarRecord.adjusted_grade._obj.getDisplayValue());
   }

   window.onload = function(){//将未评级移除
		var options = WfiBizVarRecord.adjusted_grade._obj.element.options;
		for ( var i = options.length - 1; i >= 0; i--) {
		    if(options[i].value=='00'){
				options.remove(i);
		    }
		}
		var serno = '<%=serno%>';
		if(serno==null||serno==''||serno=='null'){
			document.getElementById("bizinfo_div").style.display="none";
		}
   }
</script>
	<DIV id="bizinfo_div">
		<emp:gridLayout id="bizGroup" title="业务概况" maxColumn="2" >
			<emp:text id="CcrAppInfo.serno" label="业务编号" maxlength="40" readonly="true"/>
			<emp:text id="CcrAppDetail.model_no_displayname" label="评级模型" readonly="true" />
			<emp:text id="CcrAppInfo.cus_id" label="客户码" maxlength="30" readonly="true"/>
			<emp:text id="CcrAppInfo.cus_id_displayname" label="客户名称"  readonly="true" />
			<emp:text id="CcrAppInfo.app_begin_date" label="申请日期" maxlength="10" readonly="true"/>
			<emp:date id="CcrAppInfo.expiring_date" label="到期日期" required="false" readonly="true" colSpan="2" hidden="true"/>
			<emp:select id="CcrAppDetail.auto_grade" label="机评信用等级"  required="false" readonly="true" dictname="STD_ZB_CREDIT_GRADE" />
			<emp:select id="CcrAppDetail.adjusted_grade" label="客户经理调整信用等级" readonly="true" dictname="STD_ZB_CREDIT_GRADE" />
			<emp:text id="CcrAppInfo.manager_id_displayname" label="主管客户经理" readonly="true" />
			<emp:text id="CcrAppInfo.manager_br_id_displayname" label="主管机构" readonly="true"/>
		</emp:gridLayout>
	</DIV>	
	<DIV>
		<emp:gridLayout id="bizGroup" title="审批调整" maxColumn="2" >
			<emp:select id="WfiBizVarRecord.adjusted_grade_old" label="调整前等级" dictname="STD_ZB_CREDIT_GRADE" readonly="true" defvalue="<%=adjusted_grade%>"/>
			<emp:select id="WfiBizVarRecord.adjusted_grade" label="调整等级" dictname="STD_ZB_CREDIT_GRADE" />
			<emp:checkbox id="WfiVarFlag.adjusted_grade" label="是否修改" hidden="true"/> <!-- 暂时不用,只做保留 -->
		</emp:gridLayout>
		
		<!-- 字段名称-->
		<emp:text id="WfiVarName.adjusted_grade" label="字段名称" hidden="true" defvalue="最终等级"/>
		
		<!-- 类型 -->
		<emp:text id="WfiVarType.adjusted_grade" label="类型" hidden="true" defvalue="string"/>

		<!-- 显示值 -->
		<emp:text id="WfiVarDisp.adjusted_grade" label="显示值" hidden="true" />
	</DIV>
	
 </emp:page>