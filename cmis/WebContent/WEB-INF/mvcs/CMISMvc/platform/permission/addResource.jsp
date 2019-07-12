<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<emp:page>
<html>
<head>
<title>新增页面</title>
<jsp:include page="/include.jsp" />

<script type="text/javascript">
/*
    window.onload = function(){
       //加载的时候首先加载业务条线，也就是子系统
       var url = window.opener.bizLinecfgUrl;
    	var handleSuccess = function(o){
			if(o.responseText != undefined){
				try {
					var jsonstr = eval(o.responseText); 
					var selObj = s_resource.systempk._obj.element;
                    for(var i=0;i<jsonstr.length;++i){
                    	var hidValue = jsonstr[i][0];
                    	var showValue = jsonstr[i][1];
                        var opt = new Option(showValue,hidValue);
                        selObj.add(opt);
                    }
				} catch(e) {
					alert("Parse jsonstr define error!"+e.message);
					return;
				}				
			}
			
		};
		var handleFailure = function(o){	
		};
		var callback = {
				success:handleSuccess,
				failure:handleFailure
		};
		var obj1 = YAHOO.util.Connect.asyncRequest('post', url, callback);	
    }
*/
    /**
    *检查是否存在相同ID的资源
    */
	function doCheckSame() {
		var resourceid = s_resource.resourceid._obj.element.value;
//alert(resourceid);
		var url = '<emp:url action="resourceIdCheck.do?resourceid=' + resourceid + '"/>';
		var handleSuccess = function(o) {
			EMPTools.unmask();
			if(o.responseText != undefined) {
				try {
					var jsonstr = eval("("+o.responseText+")");
				} catch(e) {
					alert(o.responseText);
					return;
				}
				var flag = jsonstr.flag;
			//	alert(flag);
				if(flag == "fail") {
					alert("资源ID不能重复！");
					s_resource.resourceid._setValue('');
				}
			}

		};
		var handleFailure = function(o){ EMPTools.unmask();
		};
		var callback = {
			success:handleSuccess,
			failure:handleFailure
		};
		//var postData = YAHOO.util.Connect.setForm(form);
		var obj1 = YAHOO.util.Connect.asyncRequest('POST',url,callback);
	}
	
	function doClose(){
		//page.dataGroups.s_resourceGroup.reset();
		if('${param.closeFun}' != ''){
              eval('${param.closeFun}');
              
		}else{
			window.close();
	    }
	};

   
   /*
   * add  by wuming 2012.2.20
   *模块新增按照新产品的做法，需要异步进行提交处理
   */ 
	function doSubmit(){
		var url = "<emp:url action='addResourceSubmit.do'/>";
		EMPTools.doAjaxUpdateAndBack('submitForm',s_resource,url,null,addBackFun);
	}
    /*
    *添加模块成功之后 调用的函数
    */
	function addBackFun(){
      //
       window.parent.resTree.getRootNode().reload();
       doClose();
	}
</script>
</head>
<body  class="page_content">
	<form id="submitForm" action="<emp:url action='addResourceSubmit.do'/>" method="POST">
	</form>
		<emp:gridLayout id="s_resourceGroup" maxColumn="2" title="资源表">
							<emp:text id="s_resource.resourceid" label="资源ID" required="true" onchange="doCheckSame()"/>
							<emp:text id="s_resource.cnname" label="中文名" required="true" />
							<emp:select id="s_resource.systempk" label="所属子系统" dictname="STD_ZB_BUSILINE" defvalue="BL_DEFAULT" required="true" />
							<emp:text id="s_resource.url" label="资源URL" required="false" />
							<emp:text id="s_resource.parentid" label="上级资源ID" required="false" readonly="true" />
							<emp:text id="s_resource.tablename" label="资源表名" required="false" />
							<emp:text id="s_resource.procid" label="流程模板" required="false" />
							<emp:text id="s_resource.orderid" label="序号" required="false" />
							<emp:text id="s_resource.icon" label="资源图标" required="false" />
							<emp:text id="s_resource.orgid" label="组织号" required="false" />
							<emp:select id="s_resource.memo" dictname="STD_ZX_YES_NO" defvalue="2" label="是否记录操作日志"></emp:select>
					</emp:gridLayout>	
	<div align="center">
			<br>
		<emp:button id="submit" label="确定"/>
		<emp:button id="close" label="关闭"/>
	</div>
</body>
</html>
</emp:page>

