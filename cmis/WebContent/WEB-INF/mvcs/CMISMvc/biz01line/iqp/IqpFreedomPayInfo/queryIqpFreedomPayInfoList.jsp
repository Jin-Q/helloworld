<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<%@page import="com.ecc.emp.core.Context"%>
<%@page import="com.ecc.emp.core.EMPConstance"%>
<emp:page>

<% 
	//request = (HttpServletRequest) pageContext.getRequest();
	Context context = (Context) request.getAttribute(EMPConstance.ATTR_CONTEXT);
	String op = "";
	String pvp = "";
	String iqpFlowHis = "";
	if(context.containsKey("op")){
		op = (String)context.getDataValue("op");		
		if(context.containsKey("pvp")){
			pvp = (String)context.getDataValue("pvp");
		}
		if(context.containsKey("iqpFlowHis")){
			iqpFlowHis = (String)context.getDataValue("iqpFlowHis");
		}
		if("view".equals(op) && !(!pvp.equals("")&& iqpFlowHis.equals(""))){
			request.setAttribute("canwrite","");
		}
	}
	/**modified by lisj 2015-8-17 需求编号：【XD150303015】 关于增加对被放款审查岗打回的业务申请信息进行修改流程需求 begin**/
	String modiflg = "";
	String modify_rel_serno = "";
	String wf_flag="";
	if(context.containsKey("modiflg")){
	   modiflg = (String)context.getDataValue("modiflg");
	}
	if(context.containsKey("modify_rel_serno")){
		modify_rel_serno = (String) context.getDataValue("modify_rel_serno");
	}
	if(context.containsKey("wf_flag")){
		wf_flag = (String) context.getDataValue("wf_flag");
	}
	/**modified by lisj 2015-8-17 需求编号：【XD150303015】 关于增加对被放款审查岗打回的业务申请信息进行修改流程需求 end**/
	     
%> 

<html>
<head>
<title>列表查询页面</title>

<jsp:include page="/include.jsp" flush="true"/>
<jsp:include page="iqpPubAction.jsp" flush="true"/>
<script type="text/javascript">
	
	function doGetAddIqpFreedomPayInfoPage() {
		var recordCount = IqpFreedomPayInfoList._obj.recordCount;//取总记录数
		IqpFreedomPayInfoList._obj._addRow();
		IqpFreedomPayInfoList._obj.recordCount +=1; 	//增加总记录数
		var recordCount = IqpFreedomPayInfoList._obj.recordCount;//取总记录数
		IqpFreedomPayInfoList._obj.data[recordCount-1].displayid._setValue(recordCount);//添加显示序号
		IqpFreedomPayInfoList._obj.data[recordCount-1].dateno._setValue(recordCount);
		IqpFreedomPayInfoList._obj.data[recordCount-1].flag._setValue(true);
		var row = recordCount-1;
		var id = row + '_view';
		IqpFreedomPayInfoList._obj.data[recordCount-1].serno._setValue(serno);
	};

	function doGetUpdateIqpFreedomPayInfoPage(){
		var recordCount = IqpFreedomPayInfoList._obj.recordCount;//取总记录数 
		if(recordCount==0){
			alert("请添加有效数据!");
            return;   
		}
		/**modified by lisj 2015-8-17 需求编号：【XD150303015】 关于增加对被放款审查岗打回的业务申请信息进行修改流程需求 begin**/
		url = '<emp:url action="updateIqpFreedomPayInfoRecord.do"/>'+postStr+'&modiflg='+'<%=modiflg%>'+'&modify_rel_serno='+'<%=modify_rel_serno%>';
		/**modified by lisj 2015-8-17 需求编号：【XD150303015】 关于增加对被放款审查岗打回的业务申请信息进行修改流程需求 end**/
		doPubSubmit(url,true);
	};	
	
	function doDeleteIqpFreedomPayInfo() {
		var paramStr = IqpFreedomPayInfoList._obj.getParamStr(['dateno']);
		if (paramStr != null) {
			var dataRow =  IqpFreedomPayInfoList._obj.getSelectedData()[0];
			var displayid = dataRow.displayid._getValue();
			var recordCount = IqpFreedomPayInfoList._obj.recordCount;
			//if(displayid != recordCount){
				//alert("请从期号最大的记录开始删除!");
			//}else{
				if(confirm("是否确认要删除？")){
					var flag = dataRow.flag._getValue();
					if(flag == true){
						var displayid2 = dataRow.displayid._getValue();
						IqpFreedomPayInfoList._obj._deleteRow(displayid-1);   //删除行
						IqpFreedomPayInfoList._obj.recordCount -=1;
					}else{
						/**modified by lisj 2015-8-17 需求编号：【XD150303015】 关于增加对被放款审查岗打回的业务申请信息进行修改流程需求 begin**/
						var url = '<emp:url action="deleteIqpFreedomPayInfoRecord.do"/>?'+paramStr+postStr+'&modiflg='+'<%=modiflg%>'+'&modify_rel_serno='+'<%=modify_rel_serno%>';
						/**modified by lisj 2015-8-17 需求编号：【XD150303015】 关于增加对被放款审查岗打回的业务申请信息进行修改流程需求 end**/
						doPubDelete(url);
					}
				}
			//}
		} else {
			alert('请先选择一条记录！');
		}
	};

	function doInitIqpFreedomPayInfo() {
		if(confirm("是否初始化覆盖原有数据？")){
			var handleSuccess = function(o){
				if(o.responseText !== undefined) {
					try {
						var jsonstr = eval("("+o.responseText+")");
					} catch(e) {
						alert("Parse jsonstr1 define error!" + e.message);
						return;
					}
					var flag = jsonstr.flag;
					var msg = jsonstr.msg;
					if(flag == "success"){
						alert("初始化成功!");
						window.location.reload();
					}else {
						alert(msg);
						return;
					}
				}
			};
			var handleFailure = function(o){
				alert("异步请求出错！");	
			};
			var callback = {
				success:handleSuccess,
				failure:handleFailure
			};
			/**modified by lisj 2015-8-17 需求编号：【XD150303015】 关于增加对被放款审查岗打回的业务申请信息进行修改流程需求 begin**/
			var url = '<emp:url action="initIqpFreedomPayInfoRecord.do"/>?serno='+serno+'&modiflg='+'<%=modiflg%>'+'&modify_rel_serno='+'<%=modify_rel_serno%>';
			/**modified by lisj 2015-8-17 需求编号：【XD150303015】 关于增加对被放款审查岗打回的业务申请信息进行修改流程需求 end**/
			url = EMPTools.encodeURI(url);
			var obj1 = YAHOO.util.Connect.asyncRequest('POST',url, callback,null)
		}
	};
	
	/*--user code begin--*/
	function doLoad(){
		serno = "${context.serno}";
		postStr = "&serno="+serno;
		openday = "${context.OPENDAY}";
		pvp = "${context.pvp}";
		iqpFlowHis = "${context.iqpFlowHis}";

		if(pvp != '' && iqpFlowHis == ''){
			document.getElementById('button_addInfo').style.display = '';
			document.getElementById('button_deleteInfo').style.display = '';
			document.getElementById('button_updateInfo').style.display = '';
		}else{
			document.getElementById('button_addInfo').style.display = 'none';
			document.getElementById('button_deleteInfo').style.display = 'none';
			document.getElementById('button_updateInfo').style.display = 'none';
		}
	};

	function doAddInfo(){
		doGetAddIqpFreedomPayInfoPage();
	};
	function doDeleteInfo(){
		doDeleteIqpFreedomPayInfo();
	};
	function doUpdateInfo(){
		doGetUpdateIqpFreedomPayInfoPage();
	};
	
	function doPubSubmit(url,check){
		var handleSuccess = function(o){
			if(o.responseText !== undefined) {
				try {
					var jsonstr = eval("("+o.responseText+")");
				} catch(e) {
					alert("Parse jsonstr1 define error!" + e.message);
					return;
				}
				var flag = jsonstr.flag;
				var serno = jsonstr.serno;
				if(flag == "success"){
					alert('保存成功!');
					window.location.reload();
				}else{
					alert("保存失败！");
				}
			}
		};
		var handleFailure = function(o){
			alert("异步请求出错！");	
		};
		var callback = {
			success:handleSuccess,
			failure:handleFailure
		};

		url = EMPTools.encodeURI(url);
		
		var form = document.getElementById('submitForm');
		if(check){
			 result = IqpFreedomPayInfoList._checkAll();
			 if(!result){
				return;
			 }
		}
		IqpFreedomPayInfoList._toForm(form);
		var postData = YAHOO.util.Connect.setForm(form);
		var obj1 = YAHOO.util.Connect.asyncRequest('POST',url, callback,postData);
	};

	function fuckDate(obj){
		if(obj.value){
			if(obj.value <= openday){
				alert('还款日期应大于当前日期');
				obj.value = '';
			}
		}
	};
	
	/*--user code end--*/
	
</script>
</head>
<body class="page_content" onload="doLoad()">
	<form  method="POST" action="#" id="queryForm">
	</form>
	
	<div align="left">
		<emp:actButton id="getAddIqpFreedomPayInfoPage" label="新增" op="add"/>
		<emp:actButton id="deleteIqpFreedomPayInfo" label="删除" op="remove"/>
		<emp:actButton id="initIqpFreedomPayInfo" label="初始化" op="add"/>
		<emp:button id="addInfo" label="新增" />
		<emp:button id="deleteInfo" label="删除" />
	</div>
	
	<table  class='emp_table'>
		<tr class='emp_table_title'>
			<th id='title1' width="26.5 xp">序号</th>
			<th id='title2' width="27 xp">期号</th>
			<th id='title3' width="513 xp">还款日期</th>
			<th id='title4'>应还款本金</th>
		</tr>
	</table>
	<emp:table icollName="IqpFreedomPayInfoList" pageMode="false" needTableTitle="false" url="pageIqpFreedomPayInfoQuery.do?serno=${context.serno}">
		<emp:text id="serno" label="业务流水号"  hidden="true"/>
		<emp:text id="dateno" label="期号" />
		<emp:date id="pay_date" label="还款日期" flat="false" required="true" onblur="fuckDate(this)"/>
		<emp:text id="suppose_pay_cap" label="应还款本金" dataType="Currency" flat="false" required="true"/>
		<emp:text id="flag" label="标志" hidden="true"/>
	</emp:table>
	
	<emp:form id="submitForm" action="" method="POST"></emp:form>
	<div align="center" >
	<br>
		<emp:actButton id="getUpdateIqpFreedomPayInfoPage" label="保存列表" op="update"/>
		<emp:button id="updateInfo" label="保存列表" />
	</div>
</body>
</html>
</emp:page>
    