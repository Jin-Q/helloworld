<%@page import="java.sql.Connection"%>
<%@page import="com.ecc.emp.core.EMPConstance"%>
<%@page import="com.ecc.emp.core.Context"%>
<%@page import="com.ecc.emp.dbmodel.service.TableModelDAO"%>
<%@page import="com.yucheng.cmis.base.CMISConstance"%>
<%@page import="com.yucheng.cmis.pub.CMISComponentFactory"%>
<%@page import="com.ecc.emp.data.IndexedCollection"%>
<%@page import="com.ecc.emp.data.KeyedCollection"%>
<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<%@page import="com.yucheng.cmis.platform.workflow.domain.WFIInstanceVO"%>
<%@page import="com.yucheng.cmis.pub.util.SystemTransUtils"%>
<%@page import="com.ecc.emp.jdbc.ConnectionManager"%>
<%@page import="com.yucheng.cmis.pub.CMISComponentFactory"%>
<%@page import="com.yucheng.cmis.biz01line.cus.cusbase.domain.CusBase"%>
<%@page import="com.yucheng.cmis.biz01line.cus.cusbase.component.CusBaseComponent"%>

<style type="text/css">
.emp_field_text_input1 {
	width: 150px;
	border-width: 1px;
	border-color: #b7b7b7;
	border-style: solid;
	text-align: left;
	background-color: #e3e3e3;
}

.emp_field_text_input2 {
	border: 1px solid #b7b7b7;
	text-align: left;
	width: 150px;
}
</style>

<%
String serno = request.getParameter("serno");
Context context = (Context)request.getAttribute(EMPConstance.ATTR_CONTEXT);
if(serno==null||"".equals(serno)){
	serno = (String)context.getDataValue("pkVal");
}
com.ecc.emp.jdbc.JNDIDataSource jndiDataSource = (com.ecc.emp.jdbc.JNDIDataSource)context.getService("dataSource");
Connection connection = jndiDataSource.getConnection();
TableModelDAO dao = (TableModelDAO)context.getService(CMISConstance.ATTR_TABLEMODELDAO);
KeyedCollection kCollApp = dao.queryDetail("LmtRediApply", serno, connection);
//String lmt_type = (String)kCollApp.getDataValue("lmt_type");//授信类别(区分条线)
String cus_id = (String)kCollApp.getDataValue("cus_id");
if(cus_id!=null&&!"".equals(cus_id)){
	CusBaseComponent cusComponent = (CusBaseComponent)CMISComponentFactory.getComponentFactoryInstance().getComponentInstance("CusBase", context,connection);
	CusBase cusBase = cusComponent.getCusBase(cus_id);
	String cus_name = cusBase.getCusName();
	kCollApp.put("cus_id_displayname",cus_name);
}
context.addDataElement(kCollApp);

IndexedCollection iCollDet = dao.queryList("LmtAppDetails","where serno='"+serno+"'",connection);
KeyedCollection kColl = (KeyedCollection)context.getDataElement("WfiBizVarRecord");
IndexedCollection iColl = new IndexedCollection("LmtAppDetailsList");
if(kColl!=null&&kColl.size()>0){
	for(int i=0;i<iCollDet.size();i++){
		KeyedCollection kCollTmp = (KeyedCollection)iCollDet.get(i);
	//	String lmtType_det = (String)kCollTmp.getDataValue("lmt_type");
	//	if(lmtType_det.equals(lmt_type)){
			kCollTmp.put("term_old",kColl.getDataValue("term@"+serno+"@"+kCollTmp.getDataValue("org_limit_code")+"_old"));
			kCollTmp.put("term_type_old",kColl.getDataValue("termType@"+serno+"@"+kCollTmp.getDataValue("org_limit_code")+"_old"));
			kCollTmp.put("crd_amt_old",kColl.getDataValue("crdAmt@"+serno+"@"+kCollTmp.getDataValue("org_limit_code")+"_old"));
			/**若该节点没有进行过保存操作，则修改后值默认取修改前值*/
			if(kColl.containsKey("term@"+serno+"@"+kCollTmp.getDataValue("org_limit_code"))){
				kCollTmp.put("term",kColl.getDataValue("term@"+serno+"@"+kCollTmp.getDataValue("org_limit_code")));
			}else{
				kCollTmp.put("term",kColl.getDataValue("term@"+serno+"@"+kCollTmp.getDataValue("org_limit_code")+"_old"));
			}
			if(kColl.containsKey("termType@"+serno+"@"+kCollTmp.getDataValue("org_limit_code"))){
				kCollTmp.put("term_type",kColl.getDataValue("termType@"+serno+"@"+kCollTmp.getDataValue("org_limit_code")));
			}else{
				kCollTmp.put("term_type",kColl.getDataValue("termType@"+serno+"@"+kCollTmp.getDataValue("org_limit_code")+"_old"));
			}
			if(kColl.containsKey("crdAmt@"+serno+"@"+kCollTmp.getDataValue("org_limit_code"))){
				kCollTmp.put("crd_amt",kColl.getDataValue("crdAmt@"+serno+"@"+kCollTmp.getDataValue("org_limit_code")));
			}else{
				kCollTmp.put("crd_amt",kColl.getDataValue("crdAmt@"+serno+"@"+kCollTmp.getDataValue("org_limit_code")+"_old"));
			}
			iColl.add(kCollTmp);
	//	}
	}
}else{
	for(int i=0;i<iCollDet.size();i++){
		KeyedCollection kCollTmp = (KeyedCollection)iCollDet.get(i);
	//	String lmtType_det = (String)kCollTmp.getDataValue("lmt_type");
	//	if(lmtType_det.equals(lmt_type)){
			kCollTmp.put("term_old",kCollTmp.getDataValue("term"));
			kCollTmp.put("term_type_old",kCollTmp.getDataValue("term_type"));
			kCollTmp.put("crd_amt_old",kCollTmp.getDataValue("crd_amt"));
			
			iColl.add(kCollTmp);
	//	}
	}
}
context.addDataElement(iColl);
//context.addDataElement(iCollDet);

String nodeId = "";
String nodeName = "";
String instanceId = "";
if(context.containsKey("wfiInstanceVO")){
	WFIInstanceVO instanceVO = (WFIInstanceVO)context.getDataValue("wfiInstanceVO");
	nodeId = instanceVO.getNodeId();
	nodeName = instanceVO.getNodeName();
	instanceId = instanceVO.getInstanceId();
}
if(connection!=null){
	ConnectionManager.releaseConnection(jndiDataSource, connection);
}
%>
<emp:page>

<script type="text/javascript">
	
	function onVarSubmit(formSub){
		var serno = '<%=serno%>';
		var instanceId = '<%=instanceId%>';
		var nodeId = '<%=nodeId%>';
		var nodeName = '<%=nodeName%>';

		var handleSuccess = function(o) {
			if (o.responseText !== undefined) {
				try {
					var jsonstr = eval("(" + o.responseText + ")");
				} catch (e) {
					alert("Parse jsonstr define error!" + e.message);
					return;
				}
				var flag = jsonstr.flag;
				if(flag!='success'){
		            alert('保存失败!');
				}else{
					var op = formSub.op.value;
					if(op=='submit'){
						doWorkFlowAgent(submitWorkFlow, formSub);
					}
				}
			}
		};
		var handleFailure = function(o) {
			alert("保存失败!");
		};
		var callback = {
			success :handleSuccess,
			failure :handleFailure
		};

		var form = document.getElementById("submitVarForm");
		LmtAppDetailsList._toForm(form);
		form.action="submitLmtVarRecord.do?serno="+serno+'&nodeId='+nodeId+'&instanceId='+instanceId+'&nodeName='+nodeName;
		var postData = YAHOO.util.Connect.setForm(form);
		var obj1 = YAHOO.util.Connect.asyncRequest('POST',form.action, callback,postData);
   }
</script>
	<emp:form id="submitVarForm" action="#" method="POST">
	<DIV>
		<emp:gridLayout id="bizGroup" title="业务概况" maxColumn="2" >
			<emp:select id="LmtRediApply.app_type" label="申请类型" dictname="STD_ZB_APP_TYPE" readonly="true" />
			<emp:select id="LmtRediApply.biz_type" label="授信业务类型" dictname="STD_ZB_BIZ_TYPE" defvalue="01" readonly="true"/>
			<emp:text id="LmtRediApply.cus_id" label="客户码" readonly="true"/>
			<emp:text id="LmtRediApply.cus_id_displayname" label="客户名称" cssElementClass="emp_field_text_long_readonly" colSpan="2" readonly="true" /> 
			<emp:select id="LmtRediApply.lrisk_type" label="低风险业务类型" dictname="STD_ZB_LRISK_TYPE" readonly="true" defvalue="20"/>
			<emp:select id="LmtRediApply.cur_type" label="授信币种" dictname="STD_ZX_CUR_TYPE" defvalue="CNY" readonly="true"/>
			<emp:text id="LmtRediApply.crd_totl_amt" label="授信总额" maxlength="18" dataType="Currency" readonly="true" defvalue="0" cssElementClass="emp_currency_text_readonly" />
			<emp:text id="LmtRediApply.crd_cir_amt" label="循环授信敞口" maxlength="18" dataType="Currency" defvalue="0.00" cssElementClass="emp_currency_text_readonly" readonly="true"/>
			<emp:text id="LmtRediApply.crd_one_amt" label="一次性授信敞口" maxlength="18" dataType="Currency" defvalue="0.00" cssElementClass="emp_currency_text_readonly" readonly="true"/>
		</emp:gridLayout>
	</DIV>
	</emp:form>
	<DIV style="margin: 0px 0px 0px 10px;width:100%;">
		<div class='emp_gridlayout_title'>审批调整&nbsp;</div>
 		<emp:table icollName="LmtAppDetailsList" editable="true" pageMode="false" url="">
			<emp:text id="org_limit_code" label="授信额度编号" readonly="true" cssElementClass="emp_field_text_input1"/>
			<emp:select id="term_type_old" label="期限类型(调整前)" readonly="true" dictname="STD_ZB_TERM_TYPE" cssFakeInputClass="emp_field_text_input1"/>
			<emp:text id="term_old" label="期限(调整前)" readonly="true" cssElementClass="emp_field_text_input1"/>
			<emp:text id="crd_amt_old" label="授信金额(调整前)" readonly="true" dataType="Currency" cssElementClass="emp_field_text_input1"/>
			<emp:select id="term_type" label="期限类型" dictname="STD_ZB_TERM_TYPE" cssElementClass="emp_field_text_input2"/>
			<emp:text id="term" label="期限" dataType="Int" cssElementClass="emp_field_text_input2"/>
			<emp:text id="crd_amt" label="授信金额" dataType="Currency" cssElementClass="emp_field_text_input2"/>
		</emp:table>
	</DIV>
 </emp:page>
