<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<%@page import="com.ecc.emp.core.Context"%>
<%@page import="com.ecc.emp.core.EMPConstance"%>
<% 
	//request = (HttpServletRequest) pageContext.getRequest();
	Context context = (Context) request.getAttribute(EMPConstance.ATTR_CONTEXT);
	String prdId = "";
	if(context.containsKey("prdId")){
		prdId = (String)context.getDataValue("prdId");
	}
	String query = "";
	if(context.containsKey("query")){
		query = (String)context.getDataValue("query");
	}
%>
<emp:page>
<html>
<head>
<title>列表查询页面</title>

<jsp:include page="/include.jsp" flush="true"/>

<script type="text/javascript">
	rowIndex = 0;
	//增加产品适用机构记录
	function doAddPrdOrgApply(){
		var recordCount = PrdOrgApplyList._obj.recordCount;//取总记录数
		PrdOrgApplyList._obj._addRow();
		PrdOrgApplyList._obj.recordCount +=1; 	//增加总记录数
		var recordCount = PrdOrgApplyList._obj.recordCount;//取总记录数
		PrdOrgApplyList._obj.data[recordCount-1].displayid._setValue(recordCount);//添加显示序号
		PrdOrgApplyList._obj.data[recordCount-1].optType._setValue("add");//判断操作方式
		var row = recordCount-1;
		var id = row + '_view';
		PrdOrgApplyList._obj.data[recordCount-1].org_id._obj.addOneButton(id,'选择',querySOrg);
	}

	//加载机构选择页面
	function querySOrg(){
		var id = this.id;
		rowIndex = parseInt(id.split('_')[0]);
		var url = '<emp:url action="queryPrdOrgPopList.do"/>&returnMethod=selFeeTyp';
		url = EMPTools.encodeURI(url);
		var param = 'height=500, width=800, top=80, left=80, toolbar=no, menubar=no, scrollbars=yes, resizable=no, location=no, status=no';
		window.open(url,'newWindow',param);
	}

	function selFeeTyp(data){
		rowIndexStr=rowIndex;
		PrdOrgApplyList._obj.data[rowIndexStr].org_id._setValue(data.organno._getValue());
		PrdOrgApplyList._obj.data[rowIndexStr].organname._setValue(data.organname._getValue());
	}
	
	//删除产品适用机构记录
	function doDelPrdOrgApply(){
		var dataRow =  PrdOrgApplyList._obj.getSelectedData()[0];
		if (dataRow != undefined) {
			if(confirm("是否确认要删除？")){
				var optType = dataRow.optType._getValue() ;

				dataRow.org_id._obj._renderHidden(true) ;
				dataRow.organname._obj._renderHidden(true) ;
				dataRow.apply_range._obj._renderHidden(true) ;
				dataRow.apply_type._obj._renderHidden(true) ;
				
				if(optType == 'add'){
					dataRow.optType._setValue("none") ;
				}else if(optType == 'del'){
					dataRow.optType._setValue("del") ;
				}else if(optType == ''){
					dataRow.optType._setValue("del");
				}
			}
		} else {
			alert('请先选择一条记录！');
		}
	}
	
	
	// 异步保存产品适用机构列表
	function confirmSubmit(){
		var recordCount = PrdOrgApplyList._obj.recordCount;//取总记录数
		/*  检查有效记录的字段否为空 */
		var count = 0;
		for(var i=0;i<recordCount;i++){
			var optType = PrdOrgApplyList._obj.data[i].optType._getValue();
			if(optType == "" || optType == "add" ){
				count++;
				var org_id = PrdOrgApplyList._obj.data[i].org_id._getValue();
				var org_name = PrdOrgApplyList._obj.data[i].organname._getValue();
				var apply_range = PrdOrgApplyList._obj.data[i].apply_range._getValue();
				var apply_type = PrdOrgApplyList._obj.data[i].apply_type._getValue();
				if( org_id == "" ){
					alert("第"+ count + "条记录机构编码为空！");
					return;
				}
				if( org_name == "" ){
					alert("第"+ count +"条记录机构名称为空！");
					return;
				}
				if( apply_range == "" ){
					alert("第"+ count +"条记录适用范围为空！");
					return;
				}
				if( apply_type == "" ){
					alert("第"+ count +"条记录适用类型为空！");
					return;
				}
			}
		}
		
		var form = document.getElementById('submitForm');
    	if(form){
    		PrdOrgApplyList._toForm(form);
    		var handleSuccess = function(o){
				if(o.responseText != undefined){
					try {
						var jsonstr = eval("("+o.responseText+")");
					} catch(e) {
						alert("Parse jsonstr define error!"+e.message);
						return;
					}
					var flag = jsonstr.flag;
					if(flag == 'success'){
						alert("产品适用机构保存成功！");
						window.location.reload();
					}else{
						alert("产品适用机构保存失败！");
					}
				}
			};

			var url = '<emp:url action="savePrdOrgApply.do"/>?prdId=<%=prdId %>';
			url = EMPTools.encodeURI(url);
			var data = YAHOO.util.Connect.setForm(form);
			var callback = {
					success:handleSuccess,
					failure:function(){alert("保存失败！");}
			};
			var obj1 = YAHOO.util.Connect.asyncRequest('post', url,callback, data);
    	}
	}

</script>
</head>
<body class="page_content" >
	
	
	<!-- 增加产品适用机构列表 -->
		<BR><div  class='emp_gridlayout_title'>产品适用机构设置</div>
		<div id="tempButton"  align="left" style="display:${param.optype}">
			<% 
				if(!"view".equals(query)){
			%>
			<emp:button id="addPrdOrgApply" label="新增" op="add"/>
			<emp:button id="delPrdOrgApply" label="删除" op="remove" locked="false"/>
			<%		
				}
			%>
		  	
		</div>
		<emp:table icollName="PrdOrgApplyList" pageMode="false" url="">
			<emp:text id="optType" label="操作方式" hidden="true" />
			<emp:text id="org_id" label="机构编码"  flat="false" required="true" />
			<emp:text id="organname" label="机构名称" readonly="true" flat="false" required="true"/>
			<emp:select id="apply_range" label="适用范围"  dictname="STD_ZB_APPLY_RANGE" flat="false" required="true"/>
			<emp:select id="apply_type" label="适用类型" dictname="STD_ZB_APPLY_TYPE" flat="false" required="true"/> 
		</emp:table>
		<emp:form id="submitForm" action="" method="POST"></emp:form> 
		<div align="center" style="display:${param.optype}">
			<br>
			<% 
				if(!"view".equals(query)){
			%>
			<button onclick="confirmSubmit()">保存列表</button>
			<%		
				}
			%>
		</div>
		
</body>
</html>
</emp:page>