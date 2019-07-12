<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<emp:page>
<html>
<head>
<title>新增页面</title>
<style type="text/css">
.emp_input2{
border: 1px solid #b7b7b7;
width:600px;
}
</style>
<jsp:include page="/include.jsp" flush="true"/>

<script type="text/javascript">
	
	/*--user code begin--*/
	function doReturn(){
		window.location= '<emp:url action="queryQryTempletList.do"/>';
	}
	function doPreSubmit(){
		//var sql=QryTemplet.query_sql._getValue();
	//	if(sql.length>0){
	//		if(!/select(\s|\n)+.*(\s|\n)from(\s|\n)+.*/ig.test(sql)){
	//			alert(" 请输入正确的sql语句!");
	//			return ;
	//		}
	//	}
		//doSubmit();

		var form = document.getElementById("submitForm");
		QryTemplet._checkAll();
		if(QryTemplet._checkAll()){
			QryTemplet._toForm(form);
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
						alert("操作成功！");
						tempNo = jsonstr.tempNo;
						var url = '<emp:url action="getQryTempletUpdatePage.do"/>?temp_no='+tempNo+'&op=update';
						url = EMPTools.encodeURI(url);
						window.location = url;
					}else {
						alert("操作失败！"+jsonstr.message);
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
			var postData = YAHOO.util.Connect.setForm(form);	
			var obj1 = YAHOO.util.Connect.asyncRequest('POST',form.action, callback,postData)
		}else {
			return false;
		}
	}

	/**查询模式值改变事件
	当模式为“1-使用SQL语句查询”时隐藏[扩展类路径] 否则隐藏[查询SQL语句]
	*/		
	function change_TempPattern(_value){
		if("1"==_value){   //使用SQL语句查询
			QryTemplet.classpath._obj._renderHidden(true);
			QryTemplet.classpath._obj._renderRequired(false);

			QryTemplet.query_sql._obj._renderHidden(false);
			QryTemplet.query_sql._obj._renderRequired(true);
		}else{     //使用类查询
			QryTemplet.query_sql._obj._renderHidden(true);
			QryTemplet.query_sql._obj._renderRequired(false);

			QryTemplet.classpath._obj._renderHidden(false);
			QryTemplet.classpath._obj._renderRequired(true);
		}
	}

	//设置适用机构
	function getOrganlevel(data){
		QryTemplet.organlevel._setValue(data[0]);
		QryTemplet.organlevel_displayname._setValue(data[1]);
	}
	/*--user code end--*/
	
</script>

</head>
<body class="page_content">
	
	<emp:form id="submitForm" action="addQryTempletRecord.do" method="POST">
		<emp:gridLayout id="QryTempletGroup" title="查询信息配置模板" maxColumn="2">
			<emp:text id="QryTemplet.temp_no" label="查询模板编号" maxlength="20" required="true" hidden="true" defvalue="系统自动生成"/>
			<emp:text id="QryTemplet.temp_name" label="查询名称" maxlength="200" required="true" cssElementClass="emp_input2"/>
			<emp:pop id="QryTemplet.organlevel" label="适用机构"  cssElementClass="emp_field_text_long" colSpan="2" url="queryMultiSOrgPop.do" returnMethod="getOrganlevel" required="false" />
			<emp:textarea id="QryTemplet.organlevel_displayname" label="适用机构" required="false" readonly="true" colSpan="2" cssElementClass="emp_field_textarea_organlevel" />
			<emp:select id="QryTemplet.templet_type" label="查询类型" required="true" dictname="STD_ZB_TEMPLET_TYPE" readonly="false"/>
			<emp:select id="QryTemplet.temp_pattern" label="查询模式" required="true" dictname="STD_ZB_TEMP_PATTERN" defvalue="1" onchange="change_TempPattern(this.value)"/>
			<emp:text id="QryTemplet.classpath" label="扩展类路径" maxlength="250" required="false" colSpan="2" hidden="true" cssElementClass="emp_field_text_long"/>
			<emp:select id="QryTemplet.temp_enable" label="是否启用" required="true" dictname="STD_ZX_YES_NO"/>
			<emp:textarea id="QryTemplet.query_sql" label="查询SQL语句" maxlength="4000" required="true" colSpan="2" />
			<emp:text id="QryTemplet.jsp_file_name" label="查询页面文件夹名" maxlength="250" required="true" />
			<emp:text id="QryTemplet.order_id" label="排序字段" maxlength="20" required="false" dataType="Long" />
		</emp:gridLayout>
		<div class="note" style="text-algin:left;color:blue"> 注意:SQL关键字请使用小写形式</div>
		
		<emp:gridLayout id="SRowrightGroup" maxColumn="1" title="系统内置参数">
		<emp:label text="对于内置参数，替换方式为：'$变量名称$' 1-7参数为单个值，8-9参数为多值" /><br> 
		<emp:label text="示例如下：当前登录人员：|manager_id:'$currentUserId$'|；当前登录人角色：|角色字段 :$roles$|"/> 
		<emp:textarea id="desc" label="内置参数" cssElementClass="emp_field_textarea_textarea1" defvalue="1.	当前登录人员：currentUserId
2.	当前登录人员名称：currentUserName
3.	当前登录机构：organNo
4.	当前登录机构名称：organName
5.	系统当前登录机构法人ID：artiOrganNo
6.	本机构以及下级机构：S_orgchilds（适用于分行）
7.	系统当前登录日期：OPENDAY
8.	系统当前登录人岗位：dutys(可能存在多个岗位信息)
9.	系统当前登录人角色ID：roles（可能存在多个角色信息） " required="false" readonly="true"/>
		</emp:gridLayout>	
		<div align="center">
			<br>
			<emp:button id="preSubmit" label="确定" op="add"/>
			<emp:button id="reset" label="重置"/>
			<emp:button id="return" label="返回"/>
		</div>
	</emp:form>
</body>
</html>
</emp:page>
