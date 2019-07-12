<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>

<emp:page>
<html>
<head>
<title>新增页面</title>

<jsp:include page="/include.jsp" flush="true"/>

<script type="text/javascript">

/*--user code begin--*/
	//选择客户POP框返回方法
	function returnCus(data){
		LmtApply.cus_id._setValue(data.cus_id._getValue());
		cus_name_displayname._setValue(data.cus_name._getValue());
		LmtApply.main_br_id._setValue(data.main_br_id._getValue());
		LmtApply.manager_id._setValue(data.cust_mgr._getValue());
		LmtApply.manager_br_id._setValue(data.main_br_id._getValue());
	}

	//下一步
    function doExecuteAjax(){
   	 	var handleSuccess = function(o) {
  			if (o.responseText !== undefined) {
  				try {
  					var fistTest = eval("(" + o.responseText + ")");
  				} catch (e) {
  					alert("异步执行不成功：" + e.message);
  					return;
  				}
  				var result = fistTest.result;   
  				if(result != ""){
  	  				//alert(result);
				}else{
					LmtApply._toForm(form);
			    	form.submit();
				}

  				LmtApply._toForm(form);
		    	form.submit();
  			}
   		};
  		var handleFailure = function(o) {
  			alert("与服务器交互失败，请联系管理员！");
  		};
  		var callback = {
  			success :handleSuccess,
  			failure :handleFailure
  		};
  		var form = document.getElementById('submitForm');
		var result = LmtApply._checkAll();
	    if(result){
	    	var url = '<emp:url action="searchLmtInfo.do"/>?op=update&cus_id='+LmtApply.cus_id._getValue()+"&lrisk_type="+LmtApply.lrisk_type._getValue()+"&"+new Date();
	  		url = EMPTools.encodeURI(url);
	  		var obj1 = YAHOO.util.Connect.asyncRequest('GET', url, callback);
	    }else {
		    alert("请检查各标签页面中的必填信息是否遗漏！");
		}
	}
	
    function doReturn() {
		var url = '<emp:url action="queryLmtApplyList.do"/>?type=app';
		url = EMPTools.encodeURI(url);
		window.location=url;
	};
    /**modified by lisj 2015-4-24 需求编号：【XD150407025】分支机构授信审批权限配置 begin**/
	//加载事件
	function onLoad(){
		var options = LmtApply.lmt_type._obj.element.options;
		for ( var i = options.length - 1; i >= 0; i--) {
		//去除小微条线为严格/非严格的区分
			if(options[i].value == "05" || options[i].value == "06" || options[i].value == "03" || options[i].value == "04"){
				options.remove(i);
			}
		}
	}
	/**modified by lisj 2015-4-24 需求编号：【XD150407025】分支机构授信审批权限配置 end**/
	/**modified by lisj 2014-11-26 需求编号：【XD141107075】授信流程关联关系信息改造  begin**/
	//点击【下一步】按钮，校验客户关联关系信息
	function doCheckCusRelInfo(){
		var result = LmtApply._checkAll();
		if(result){
		var cusId  = LmtApply.cus_id._getValue();
		if(cusId != null && cusId != ""){
			var handleSuccess = function(o){
				if(o.responseText !== undefined) {
					try {
						var jsonstr = eval("("+o.responseText+")");
					} catch(e) {
						alert("Parse jsonstr1 define error!" + e.message);
						return;
					}
					var flag = jsonstr.flag;
					if(flag == "existence"){
						if(confirm("该客户存在我行存在关联信息，请点击【确定】进行查看！")){
							var url = '<emp:url action="GetCusRelTreeOp.do"/>?cus_id='+cusId;
							url = EMPTools.encodeURI(url);
							window.open(url,'newwindow','height=600,width=800,top=30,left=30,toolbar=no,menubar=no,scrollbars=no,resizable=yes,location=no,status=no');
						}
					}
					doExecuteAjax();
				}
			}; 
			var handleFailure = function(o){
				alert("异步请求出错！");	
			};
			var callback = {
				success:handleSuccess,
				failure:handleFailure
			};
			var url = "<emp:url action='ckeckCusRelInfoOp.do'/>?cus_id="+cusId;
			url = EMPTools.encodeURI(url);
			var obj1 = YAHOO.util.Connect.asyncRequest('POST',url, callback,null)
			}
		}else{
			alert("请检查各标签页面中的必填信息是否遗漏！");
			}	
	}
	/**modified by lisj 2014-11-26 需求编号：【XD141107075】授信流程关联关系信息改造  end**/
/*--user code end--*/
</script>
</head>
<body class="page_content" style="width:200px" onload="onLoad()">
	<emp:form id="submitForm" action="getLmtApplyAddPage.do?op=add" method="POST">
		<emp:gridLayout id="LmtApplyGroup" title="单一法人授信申请向导" maxColumn="2">
			<%//单一法人企业客户主动授信改造   modefied by zhaoxp 2015-02-08 start %>
			<emp:pop id="LmtApply.cus_id" label="客户码" url="queryAllCusPop.do?cusTypCondition=((BELG_LINE in ('BL100','BL200')and cus_status='20') or cus_id in (select cus_id from lmt_ent_self_cus_info))&returnMethod=returnCus" required="true"/>
			<%//单一法人企业客户主动授信改造   modefied by zhaoxp 2015-02-08 end %>
			<emp:text id="cus_name_displayname" label="客户名称" required="true" readonly="true" /> 
			<emp:select id="LmtApply.lrisk_type" label="低风险业务类型" required="true" dictname="STD_ZB_LRISK_TYPE"/>
			<emp:select id="LmtApply.lmt_type" label="授信类别" required="true" dictname="STD_ZX_LMT_PRD"/>
			<emp:text id="LmtApply.main_br_id" label="管理机构" maxlength="60" required="false" readonly="true" hidden="true"/>
			<emp:text id="LmtApply.manager_id" label="责任人" maxlength="60" required="false" readonly="true" hidden="true"/>
			<emp:text id="LmtApply.manager_br_id" label="责任机构" maxlength="60" required="false" readonly="true" hidden="true"/>
		</emp:gridLayout>
		<div align="center">
			<br>
			<emp:button id="checkCusRelInfo" label="下一步" op="add" />
			<emp:button id="reset" label="重置"/>
			<emp:button id="return" label="返回"/>
		</div>
	</emp:form>
	
</body>
</html>
</emp:page>

