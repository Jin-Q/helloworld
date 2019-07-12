<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<emp:page>
<html>
<head>
<title>列表查询页面</title>

<jsp:include page="/include.jsp" flush="true"/>


<script type="text/javascript">

function addItem(icoll,kcoll){
	icoll._obj._addRow();
	icoll._obj.recordCount = icoll._getSize();


	
	var obj = eval("icoll[(icoll._getSize()-1)].cnname");
	if (obj) {
					obj._setValue(kcoll.cnname);
				}
		obj = eval("icoll[(icoll._getSize()-1)].enname");
				if (obj) {
					obj._setValue(kcoll.enname);
				}				
	}


	function doOnLoad() {
		//topPage.checkPermissions('<ctp:text dataName="menuId" />', document);
		for(var j=1;j<6;j++){
			addItem(orgLevelList,{enname:j,cnname:j+"级"});
		}
		orgLevelList._obj.setMessage(null);	
		
	}
	
	function doSelect(methodName){
	//由于这个页面只有查询分析一个模块用到所以不做公共的接口
		if(window.opener){
				var data = orgLevelList._obj.getSelectedData();
				var organLevel=""//适用机构等级
				for (var i=0;i<data.length;i++){
					organLevel=organLevel+","+data[i].enname._getValue();
				}
				organLevel=organLevel.substring(1);
				window.opener.returnOrganLevel(organLevel);
				window.close();
		}else{
			alert("找不到父窗口");
		}
	};		
	
</script>
</head>
<body onload="doOnLoad()">
<div id="tableListDiv">
	<emp:table icollName="orgLevelList" pageMode="false" url="" selectType="2">
		<emp:text id="enname" label="查询编号" />
		<emp:text id="cnname" label="级别名称" />
	</emp:table>
</div>
<emp:button label="选取并返回" id="select"></emp:button>
</body>
</html>

</emp:page>
