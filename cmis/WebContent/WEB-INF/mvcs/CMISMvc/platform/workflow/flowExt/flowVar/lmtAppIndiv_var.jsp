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
<%@page import="com.yucheng.cmis.biz01line.lmt.component.LmtPubComponent"%>
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
//业务概况信息
KeyedCollection kCollApp = dao.queryDetail("LmtAppIndiv", serno, connection);
String cus_id = (String)kCollApp.getDataValue("cus_id");
if(cus_id!=null&&!"".equals(cus_id)){
	CusBaseComponent cusComponent = (CusBaseComponent)CMISComponentFactory.getComponentFactoryInstance().getComponentInstance("CusBase", context,connection);
	CusBase cusBase = cusComponent.getCusBase(cus_id);
	String cus_name = cusBase.getCusName();
	kCollApp.put("cus_id_displayname",cus_name);
}
//汇总循环额度、一次性额度
LmtPubComponent lmtComponent = (LmtPubComponent)CMISComponentFactory.getComponentFactoryInstance().getComponentInstance("LmtPubComponent", context,connection);
KeyedCollection kColl_details = lmtComponent.selectLmtAppIndivAmt(serno,"LMT_APP_DETAILS");
if(null!=kColl_details){
	kCollApp.put("totl_amt", kColl_details.getDataValue("total_amt"));
	kCollApp.put("crd_cir_amt", kColl_details.getDataValue("crd_cir_amt"));
	kCollApp.put("crd_one_amt", kColl_details.getDataValue("crd_one_amt"));
}
context.addDataElement(kCollApp);
String is_self_rev = (String)kCollApp.getDataValue("is_self_revolv");//是否开通自助循环
IndexedCollection iCollDet = dao.queryList("LmtAppDetails","where serno='"+serno+"'",connection);
//审批变更要素
KeyedCollection kColl = (KeyedCollection)context.getDataElement("WfiBizVarRecord");
IndexedCollection iColl = new IndexedCollection("LmtAppDetailsList");
if(kColl!=null&&kColl.size()>0){
	for(int i=0;i<iCollDet.size();i++){
		KeyedCollection kCollTmp = (KeyedCollection)iCollDet.get(i);
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
	}
	//自助额度
	KeyedCollection kCollAppTmp = new KeyedCollection("LmtAppIndivTmp");
	kCollAppTmp.put("self_amt_old",kColl.getDataValue("self_amt_old"));
	if(kColl.containsKey("self_amt")){
		kCollAppTmp.put("self_amt",kColl.getDataValue("self_amt"));
	}else{
		kCollAppTmp.put("self_amt",kColl.getDataValue("self_amt_old"));
	}
	context.addDataElement(kCollAppTmp);
}else{
	for(int i=0;i<iCollDet.size();i++){
		KeyedCollection kCollTmp = (KeyedCollection)iCollDet.get(i);
		kCollTmp.put("term_old",kCollTmp.getDataValue("term"));
		kCollTmp.put("term_type_old",kCollTmp.getDataValue("term_type"));
		kCollTmp.put("crd_amt_old",kCollTmp.getDataValue("crd_amt"));
		
		iColl.add(kCollTmp);
	}
	//自助额度
	KeyedCollection kCollAppTmp = new KeyedCollection("LmtAppIndivTmp");
	kCollAppTmp.put("self_amt_old",kCollApp.getDataValue("self_amt"));
	kCollAppTmp.put("self_amt",kCollApp.getDataValue("self_amt"));
	context.addDataElement(kCollAppTmp);
}
context.addDataElement(iColl);

//流程要素
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
		/* modified by yangzy 2014/12/01  需求:XD140925064,生活贷需求开发  start */
		var recordCount = LmtAppDetailsList._obj.recordCount;//取总记录数
		var count = 0;
		for(var i=0;i<recordCount;i++){
			var limit_name = LmtAppDetailsList._obj.data[i].limit_name._getValue();
			if(limit_name == "100080" || limit_name == "100081" || limit_name == "100082" || limit_name == "100083"){
				count++;
				var term_type = LmtAppDetailsList._obj.data[i].term_type._getValue();
				var term = LmtAppDetailsList._obj.data[i].term._getValue();
				var crd_amt = LmtAppDetailsList._obj.data[i].crd_amt._getValue();
				var org_limit_code = LmtAppDetailsList._obj.data[i].org_limit_code._getValue();
				if( term_type == "003" ){
					alert("授信额度["+ org_limit_code + "]生活贷授信期限类型不能为[日]！");
					updateFlag = 'fail';
					return;
				}
				if( term_type == "002" && parseInt(term)>36){
					alert("授信额度["+ org_limit_code + "]生活贷授信期最长不能超过3年！");
					updateFlag = 'fail';
					return;
				}
				if( term_type == "001" && parseInt(term)>3){
					alert("授信额度["+ org_limit_code + "]生活贷授信期最长不能超过3年！");
					updateFlag = 'fail';
					return;
				}
				if( parseInt(crd_amt)>500000 || parseInt(crd_amt)<10000 ){
					alert("授信额度["+ org_limit_code + "]生活贷授信金额需在[1W-50W]之间！");
					updateFlag = 'fail';
					return;
				}
			}
		}
		
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
		            updateFlag = 'fail';
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
			updateFlag = 'fail';
		};
		var callback = {
			success :handleSuccess,
			failure :handleFailure
		};
		/* modified by yangzy 2014/12/01  需求:XD140925064,生活贷需求开发  end */
		var form = document.getElementById("submitVarForm");
		LmtAppDetailsList._toForm(form);
		LmtAppIndivTmp._toForm(form);
		form.action="submitLmtVarRecord.do?serno="+serno+'&nodeId='+nodeId+'&instanceId='+instanceId+'&nodeName='+nodeName;
		var postData = YAHOO.util.Connect.setForm(form);
		var obj1 = YAHOO.util.Connect.asyncRequest('POST',form.action, callback,postData);
   }
   /* modified by yangzy 2014/12/01  需求:XD140925064,生活贷需求开发  start */
	function onVarLoad(){
		var is_self_rev = '<%=is_self_rev%>';
		if(is_self_rev=='2'){//未开通自助循环
			LmtAppIndivTmp.self_amt._obj._renderReadonly(true);
			LmtAppIndivTmp.self_amt_old._setValue('0');
			LmtAppIndivTmp.self_amt._setValue('0');
		}
   }
   /* modified by yangzy 2014/12/01  需求:XD140925064,生活贷需求开发  end */
</script>
	<emp:form id="submitVarForm" action="#" method="POST">
	</emp:form>
	<DIV>
		<emp:gridLayout id="bizGroup" title="业务概况" maxColumn="2" >
			<emp:text id="LmtAppIndiv.serno" label="业务编号" maxlength="40" required="false" readonly="true" hidden="true"/>
			<emp:select id="LmtAppIndiv.biz_type" label="授信业务类型 ：内部授信/公开授信" dictname="STD_ZB_BIZ_TYPE" defvalue="01" hidden="true"/>
			<emp:text id="LmtAppIndiv.cus_id" label="客户码" required="true" readonly="true"/>
			<emp:text id="LmtAppIndiv.cus_id_displayname" label="客户名称" readonly="true"/>
			<emp:select id="LmtAppIndiv.app_type" label="申请类型" defvalue="01" dictname="STD_ZB_APP_TYPE" readonly="true"/>
			<emp:select id="LmtAppIndiv.cur_type" label="币种" dictname="STD_ZX_CUR_TYPE" defvalue="CNY" readonly="true"/>
			<emp:text id="LmtAppIndiv.crd_totl_amt" label="授信总额" maxlength="18" dataType="Currency" cssElementClass="emp_currency_text_readonly" readonly="true"/>
			<emp:text id="LmtAppIndiv.totl_amt" label="非自助总额" maxlength="18" dataType="Currency" cssElementClass="emp_currency_text_readonly" readonly="true"/>
			<emp:text id="LmtAppIndiv.self_amt" label="自助总额" maxlength="18" dataType="Currency" cssElementClass="emp_currency_text_readonly" defvalue="0.00" readonly="true"/>
			<emp:text id="LmtAppIndiv.crd_cir_amt" label="非自助循环额度" maxlength="18" dataType="Currency" cssElementClass="emp_currency_text_readonly" readonly="true"/>
			<emp:text id="LmtAppIndiv.crd_one_amt" label="非自助一次性额度" maxlength="18" dataType="Currency" cssElementClass="emp_currency_text_readonly" readonly="true"/>
			<emp:select id="LmtAppIndiv.lrisk_type" label="低风险业务类型" dictname="STD_ZB_LRISK_TYPE" readonly="true"/> 
		</emp:gridLayout>
	</DIV>
	<DIV style="margin: 0px 0px 0px 10px;width:98%;">
		<emp:gridLayout id="bizGroup" title="审批调整" maxColumn="2" >
			<emp:text id="LmtAppIndivTmp.self_amt_old" label="自助金额(调整前)" readonly="true" dataType="Currency" cssElementClass="emp_currency_text_readonly"/>
			<emp:text id="LmtAppIndivTmp.self_amt" label="自助金额" dataType="Currency" />
		</emp:gridLayout>
 		<emp:table icollName="LmtAppDetailsList" editable="true" pageMode="false" url="">
			<emp:text id="org_limit_code" label="授信额度编号" readonly="true" cssElementClass="emp_field_text_input1"/>
			<emp:text id="limit_name" label="授信额度品种" readonly="true" hidden="true"/>
			<emp:select id="term_type_old" label="期限类型(调整前)" readonly="true" dictname="STD_ZB_TERM_TYPE" cssFakeInputClass="emp_field_text_input1"/>
			<emp:text id="term_old" label="期限(调整前)" readonly="true" cssElementClass="emp_field_text_input1"/>
			<emp:text id="crd_amt_old" label="授信金额(调整前)" readonly="true" dataType="Currency" cssElementClass="emp_field_text_input1"/>
			<emp:select id="term_type" label="期限类型" dictname="STD_ZB_TERM_TYPE" cssElementClass="emp_field_text_input2"/>
			<emp:text id="term" label="期限" dataType="Int" cssElementClass="emp_field_text_input2"/>
			<emp:text id="crd_amt" label="授信金额" dataType="Currency" cssElementClass="emp_field_text_input2"/>
		</emp:table>
	</DIV>
 </emp:page>
