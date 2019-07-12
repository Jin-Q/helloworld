<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<emp:page>

<html>
<head>
<title>列表查询页面</title>

<jsp:include page="/include.jsp" flush="true"/>
<%
	String canWr = (String)request.getParameter("canWr");
	if("false".equals(canWr)){
		request.setAttribute("canwrite","");
	}
%>
<script type="text/javascript">
	
	function doGetAddLmtAppBizAreaSupmkPage11() {
		var serno = "${context.serno}";
		var url = '<emp:url action="addLmtAppBizAreaSupmkRecord.do"/>?serno=' + serno;
		url = EMPTools.encodeURI(url);
		var handleSuccess = function(o){
			if(o.responseText !== undefined) {
			  try {
				var jsonstr = eval("("+o.responseText+")");
			  } catch(e) {
				alert("数据库操作失败!");
				return;
			  }
			  var flag = jsonstr.flag;
			  if(flag == 'suc'){
				  window.location.reload();
			  }
			}
		};
	    var handleFailure = function(o){};
	    var callback = {
	    	success:handleSuccess,
	    	failure:handleFailure
	    };
		var obj1 = YAHOO.util.Connect.asyncRequest('POST', url, callback);		
	};

	//新增
	function doGetAddLmtAppBizAreaSupmkPage(){
		var serno = "${context.serno}";
		if(serno==null||serno==""||serno=="null"){
			alert("请先保存额度申请信息再新增目标客户群信息！");
			return;
		}
		LmtAppBizAreaSupmkList._obj._addRow();
		LmtAppBizAreaSupmkList._obj.recordCount +=1; 	//增加总记录数
		var recordCount = LmtAppBizAreaSupmkList._obj.recordCount;//取总记录数
		LmtAppBizAreaSupmkList._obj.data[recordCount-1].serno._setValue(serno);//设置业务编号的值
	}
	
	function doDeleteLmtAppBizAreaSupmk111() {
		var data = LmtAppBizAreaSupmkList._obj.getSelectedData();
		if (data != null) {
			if(confirm("是否确认要删除？")){
				//组装多记录选择返回参数
				var schemecodeArr = "";
				for(var i=0;i<data.length;i++){
					schemecodeArr += data[i].supmk_serno._getValue()+",";
				}
				var url = '<emp:url action="deleteLmtAppBizAreaSupmkRecord.do"/>?schemecodeArr='+schemecodeArr;
				url = EMPTools.encodeURI(url);
				var handleSuccess = function(o){
					if(o.responseText !== undefined) {
					  try {
						var jsonstr = eval("("+o.responseText+")");
					  } catch(e) {
						alert("数据库操作失败!");
						return;
					  }
					  var flag = jsonstr.flag;
					  if(flag == 'suc'){
						  window.location.reload();
					  }
					}
				};
			    var handleFailure = function(o){
			    };
			    var callback = {
			    	success:handleSuccess,
			    	failure:handleFailure
			    };
				var obj1 = YAHOO.util.Connect.asyncRequest('POST', url, callback);
			}
		} else {
			alert('请先选择一条记录！');
		}
	};

	//删除
	function doDeleteLmtAppBizAreaSupmk(){
		var dataRow =  LmtAppBizAreaSupmkList._obj.getSelectedData()[0];  
		if (dataRow != undefined) {
			if(confirm("是否确认要删除？")){
				var idx = LmtAppBizAreaSupmkList._obj.getSelectedIdx();  //得到选中行的下标
				LmtAppBizAreaSupmkList._obj._deleteRow(idx);   //删除选中行
				LmtAppBizAreaSupmkList._obj.recordCount -=1; 	//减少总记录数
			}
		} else {
			alert('请先选择一条记录！');
		}
	}
	
	function doReset(){
		page.dataGroups.LmtAppBizAreaSupmkGroup.reset();
	};
	
	/*--user code begin--*/
	function doSave(){
		var recrdCount = LmtAppBizAreaSupmkList._obj.recordCount;
		if(recrdCount==0){
			alert('目标客户群信息不能为空!');
			return;
		}
		var form = document.getElementById("submitForm");
		LmtAppBizAreaSupmkList._toForm(form);
		var handleSuccess = function(o){
			if(o.responseText !== undefined) {
			  try {
				var jsonstr = eval("("+o.responseText+")");
			  } catch(e) {
				alert("数据库操作失败!");
				return;
			  }
			  var flag = jsonstr.flag;
			  if(flag == 'success'){
				  window.location.reload();
			  }
			}
		};
	    var handleFailure = function(o){};
	    var callback = {
	    	success:handleSuccess,
	    	failure:handleFailure
	    };
	  	//设置form
	  	LmtAppBizAreaSupmkList._toForm(form);
		var postData = YAHOO.util.Connect.setForm(form);	 
		var obj1 = YAHOO.util.Connect.asyncRequest('POST',form.action, callback,postData);
	}
	
	function doOnload(){
		var view = "${context.canWr}";
		if( view == "false" ){
			document.getElementById("divBtns").style.display = "none";
		}else
			document.getElementById("divBtns").style.display = "";
	}
	/*--user code end--*/
	
</script>
</head>
<body class="page_content" onload="doOnload()">
	<emp:form  method="POST" action="updateLmtAppBizAreaSupmkRecord.do" id="submitForm">
	
		<div align="left" id="divBtns">
			<emp:button id="getAddLmtAppBizAreaSupmkPage" label="新增" />
			<emp:button id="deleteLmtAppBizAreaSupmk" label="删除" />
			<emp:button id="save" label="保存" />
		</div>
	
		<emp:table icollName="LmtAppBizAreaSupmkList" pageMode="false" selectType="2" url="pageLmtAppBizAreaSupmkQuery.do" editable="true">
			<emp:text id="supmk_serno" label="流水号" readonly="true" hidden="true"/>
			<emp:text id="serno" label="业务编号" hidden="true"/>
			<emp:select id="oper_trade" label="经营行业" dictname="STD_LMT_BIZ_INDUS" />
			<emp:select id="oper_model" label="经营规模" dictname="STD_LMT_BIZ_SIZE" />
			<emp:text id="trade_rank" label="行业排名" dataType="Ind"/>
			<emp:text id="provid_year" label="供货年限" />
			<emp:text id="net_asset" label=" 净资产" dataType="Currency" />
			<emp:text id="other_cond" label="其他准入条件" />
		</emp:table>
	</emp:form>
</body>
</html>
</emp:page>
    