<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<emp:page>

<html>
<head>
<title>列表查询页面</title>

<jsp:include page="/include.jsp" flush="true"/>

<script type="text/javascript">
	
	function doQuery(){
		var form = document.getElementById('queryForm');
		SUser._toForm(form);
		SUserList._obj.ajaxQuery(null,form);
	};
	function doReset() {
		page.dataGroups.SUserGroup.reset();
	}
	
	function doAddActorno(){
		var data = SUserList._obj.getSelectedData();	
		if (data != null && data.length !=0) {
		/*	var actornos=document.getElementById('actornos').value;
			var actornames=document.all.actornames.value;
			var actorno=data[0].actorno._getValue();
			var actorname = data[0].actorname._getValue();
			if(actornos.indexOf(actorno)!=-1){
				alert("已经选中用户:"+actorname+'('+actorno+')');
			}else{
				if(actornos==''){
					document.getElementById('actornos').value='U.'+actorno;
					document.all.actornames.value=actorname+'('+actorno+')';
				}else{
					document.getElementById('actornos').value=actornos+";"+'U.'+actorno;
					document.all.actornames.value=actornames+";"+actorname+'('+actorno+')';
				} 
			} */
			for(var i=0;i<data.length;i++){
				var actornames=document.all.actornames.value;
				var actornos=document.getElementById('actornos').value;
				var actorno=data[i].actorno._getValue();
				var actorname = data[i].actorname._getValue();
				if(actornos.indexOf(actorno)!=-1){
					alert("已经选中用户:"+actorname+'('+actorno+')');
				}else{
					if(actornos==''){
						document.getElementById('actornos').value='U.'+actorno;
						document.all.actornames.value=actorname+'('+actorno+')';
					}else{
						document.getElementById('actornos').value=actornos+";"+'U.'+actorno;
						document.all.actornames.value=actornames+";"+actorname+'('+actorno+')';
					} 
				}
			}
		} else {
			alert('请先选择一条记录！');
		}
	}
	
	function doRemoveActorno() {
		var data = SUserList._obj.getSelectedData();	
		if (data != null && data.length !=0) {
			var actornos=document.all.actornos.value;
			var actornames=document.all.actornames.value;
			var actorno=data[0].actorno._getValue();
			var actorname = data[0].actorname._getValue();
			if(actornos.indexOf('U.'+actorno)==-1){
				alert("没有选择用户:"+actorname+'('+actorno+')');
			}else{
				document.all.actornos.value=actornos.replace('U.'+actorno+';','').replace(';U.'+actorno,'').replace('U.'+actorno,'');
				document.all.actornames.value=actornames.replace(actorname+'('+actorno+');','').replace(';'+actorname+'('+actorno+')','').replace(actorname+'('+actorno+')','');
			}
		} else {
			alert('请先选择一条记录！');
		}
	}
	
	function doReturnMethod(methodName){
		var methodName = "${context.returnMethod}";
		if (methodName) {
			if(document.all.actornos.value==null || document.all.actornos.value=='') {
				alert('请先选择会签委员！');
				return;
			}
			var data = new Array(2);
			data[0] = document.all.actornos.value;
			data[1] = document.all.actornames.value;
			window.opener[methodName](data);
			window.close();
			
		}else{
			alert("未定义返回的函数，请检查弹出按钮的设置!");
		}
	};
	
	/*--user code end--*/

</script>
</head>
<body class="page_content">
	<form  method="POST" action="#" id="queryForm">
	</form>

	<%-- <emp:gridLayout id="SUserGroup" title="输入查询条件" maxColumn="2">
			<emp:text id="SUser.actorno" label="用户码" />
			<emp:text id="SUser.actorname" label="姓名" />
			<emp:text id="SUser.orgid" label="机构码" hidden="true"/>

	</emp:gridLayout>
	
	<jsp:include page="/queryInclude.jsp" flush="true" /> --%>

	<emp:table icollName="SUserList" pageMode="false" url="pageWfiSignTaskUserPopQuery.do" selectType="2" reqParams="duty=${context.duty}">
		<emp:text id="actorno" label="用户码" />
		<emp:text id="actorname" label="姓名" />
		<emp:text id="telnum" label="联系电话" />
		<emp:text id="idcardno" label="身份证号码" />
	</emp:table>
	
	<div align="center"><br>
		<textarea rows="5" cols="85" name="actornames" readonly="readonly"></textarea>
		<input type="hidden" id="actornos" name="actornos" /><br> 
		<emp:button label="加入" id="addActorno"></emp:button><emp:button label="移除" id="removeActorno"></emp:button>
		<emp:returnButton label="选择返回"/>
		<br>
	</div>
	
</body>
</html>
</emp:page>
    