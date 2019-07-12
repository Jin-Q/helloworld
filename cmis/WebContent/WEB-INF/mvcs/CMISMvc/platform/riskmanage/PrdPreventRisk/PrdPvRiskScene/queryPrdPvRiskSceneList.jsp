<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<emp:page>
<html>
<head>
<title>列表查询页面</title>

<jsp:include page="/include.jsp" flush="true"/>

<script type="text/javascript">
	function doSavePrdPreventRiskScene(){
		PrdPvRiskSceneList._obj.selectAll();
		var data = PrdPvRiskSceneList._obj.getSelectedData();
		if(data == null || data=="" || data == "undefined"){
			alert("请先在风险拦截方案中配置拦截项！");
			return;
		}
		var prevent_id = data[0].prevent_id._getValue();
		var itemArr = "";
		var levelArr = "";
		for(var i=0;i<data.length;i++){
			if(data[i].risk_level._getValue() == null || data[i].risk_level._getValue() == ""){
				alert("第"+(i+1)+"条记录未选择拦截类型，请选择！");
				return;
			}
			itemArr += data[i].item_id._getValue()+",";
			levelArr += data[i].risk_level._getValue()+",";
		}
		var paramStr = "prevent_id="+prevent_id+"&itemArr="+itemArr+"&levelArr="+levelArr;
		if(confirm("是否确认保存？")){
			var handleSuccess = function(o){
				if(o.responseText !== undefined) {
					try {
						var jsonstr = eval("("+o.responseText+")");
					} catch(e) {
						alert("Parse jsonstr1 define error!" + e.message);
						return;
					}
					var flag = jsonstr.flag;
					if(flag == "success"){
						alert("保存成功！");
						window.location.reload();
					}else {
						alert("异步发生异常！");
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
			var url = '<emp:url action="savePrdPreventRiskScene.do"/>?'+paramStr;
			url = EMPTools.encodeURI(url);
			var obj1 = YAHOO.util.Connect.asyncRequest('POST',url, callback,null)
		}
	};
</script>
</head>
<body class="page_content">
	<div align="left">
		<emp:button id="savePrdPreventRiskScene" label="保存" op="add"/>
	</div>
	<emp:table icollName="PrdPvRiskSceneList" selectType="2" statisticType="2" pageMode="false" url="pagePrdPvRiskSceneQuery.do?prevent_id=${context.prevent_id}">
		<emp:text id="prevent_id" label="方案编号" />
		<emp:text id="item_id" label="项目编号" />
		<emp:text id="item_name" label="项目名称" />
		<emp:select id="risk_level" label="拦截类型" dictname="STD_ZB_RISK_LEVEL" flat="true"/>
	</emp:table>
</body>
</html>
</emp:page>