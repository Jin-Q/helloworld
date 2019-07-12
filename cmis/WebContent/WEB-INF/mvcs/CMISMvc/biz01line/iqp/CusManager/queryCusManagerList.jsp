<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<%@page import="com.ecc.emp.core.Context"%>
<%@page import="com.ecc.emp.core.EMPConstance"%>
<% 
	//request = (HttpServletRequest) pageContext.getRequest();
	Context context = (Context) request.getAttribute(EMPConstance.ATTR_CONTEXT);
	String op = "";
	String serno = "";
	String cont="";
	String cont_no ="";
	if(context.containsKey("serno")){
		serno = (String)context.getDataValue("serno");
	}
	if(context.containsKey("cont_no")){
		cont_no = (String)context.getDataValue("cont_no");
	}
	if(context.containsKey("op")){
		op = (String)context.getDataValue("op");
		if(op.equals("view")){   
			request.setAttribute("canwrite","");
		}
	}
	if(context.containsKey("cont")){
		cont = (String)context.getDataValue("cont");
		if("cont".equals(cont)){   
			request.setAttribute("canwrite","");
		}
	}
	     
%> 
<emp:page>

<html>
<head>
<title>列表查询页面</title>

<jsp:include page="/include.jsp" flush="true"/>

<script type="text/javascript">
rowIndex = 0;
	function doQuery(){
		var form = document.getElementById('queryForm');
		CusManager._toForm(form);
		CusManagerList._obj.ajaxQuery(null,form);
	};
	
	function doGetUpdateCusManagerPage() {
		var paramStr = CusManagerList._obj.getParamStr(['serno']);
		if (paramStr != null) {
			var url = '<emp:url action="getCusManagerUpdatePage.do"/>?'+paramStr;
			url = EMPTools.encodeURI(url);
			window.location = url;
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doViewCusManager() {
		var paramStr = CusManagerList._obj.getParamStr(['serno']);
		if (paramStr != null) {
			var url = '<emp:url action="getCusManagerViewPage.do"/>?'+paramStr;
			url = EMPTools.encodeURI(url);
			window.location = url;
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	function doGetAddCusManagerPage() {
		var recordCount = CusManagerList._obj.recordCount;//取总记录数
		CusManagerList._obj._addRow();
		CusManagerList._obj.recordCount +=1; 	//增加总记录数
		var recordCount = CusManagerList._obj.recordCount;//取总记录数
		CusManagerList._obj.data[recordCount-1].displayid._setValue(recordCount);//添加显示序号
		CusManagerList._obj.data[recordCount-1].optType._setValue("add");//判断操作方式
		var row = recordCount-1;
		var id = row + '_view';
		CusManagerList._obj.data[recordCount-1].serno._setValue('<%=serno%>');
		CusManagerList._obj.data[recordCount-1].manager_id._obj.addOneButton(id,'选择',querySOrg);
	};
 
	//加载机构选择页面
	function querySOrg(){
		var id = this.id;
		rowIndex = parseInt(id.split('_')[0]);
		var url = '<emp:url action="getValueQuerySUserPopListOp.do"/>&popReturnMethod=returnManager&restrictUsed=false';
		url = EMPTools.encodeURI(url);    
		var param = 'height=700, width=1200, top=80, left=80, toolbar=no, menubar=no, scrollbars=yes, resizable=yes, location=no, status=no';
		window.open(url,'newWindow',param);   
	};
	function returnManager(data){
		var actorno = data.actorno._getValue();  
		rowIndexStr=rowIndex;
		CusManagerList._obj.data[rowIndexStr].manager_id._setValue(data.actorno._getValue()); 
		CusManagerList._obj.data[rowIndexStr].manager_id_displayname._setValue(data.actorname._getValue()); 
		var form = document.getElementById('submitForm');
    		var handleSuccess = function(o){
				if(o.responseText != undefined){
					try {
						var jsonstr = eval("("+o.responseText+")");
					} catch(e) {
						alert("Parse jsonstr define error!"+e.message);
						return;
					}
					var flag = jsonstr.flag;
					if(flag == 'error'){
						alert("存在客户经理，请重新选择！");
						CusManagerList._obj.data[rowIndexStr].manager_id._setValue("");
						CusManagerList._obj.data[rowIndexStr].manager_id_displayname._setValue("")
					}  
				}
			};
 
			var url = '<emp:url action="checkCusManagerRecord.do"/>&manager_id='+actorno+'&serno='+'<%=serno%>'; 
			url = EMPTools.encodeURI(url);
			var data = YAHOO.util.Connect.setForm(form);
			var callback = {
					success:handleSuccess,
					failure:function(){alert("保存失败！");}
			};
			var obj1 = YAHOO.util.Connect.asyncRequest('post', url,callback, data);
    };
	
	
	//删除
	function doDeleteCusManager(){
		var dataRow =  CusManagerList._obj.getSelectedData()[0];
		if (dataRow != undefined) {
			if(confirm("是否确认要删除？")){
				var optType = dataRow.optType._getValue() ;
				dataRow.displayid._obj._renderHidden(true) ;
				dataRow.serno._obj._renderHidden(true) ;
				dataRow.manager_id._obj._renderHidden(true) ;
				dataRow.is_main_manager._obj._renderHidden(true) ;
				dataRow.ser_rate._obj._renderHidden(true) ;
				dataRow.manager_id_displayname._obj._renderHidden(true) ;
				var manager = dataRow.manager_id._getValue();
				if(optType == 'add'){   
					dataRow.optType._setValue("none") ;  
				}else if(optType == 'del'){
					dataRow.optType._setValue("del") ;
				}else if(optType == ''){
					dataRow.optType._setValue("del");
				}
				if(manager!=""){   
				rowIndexStr=rowIndex;
				var recordCount = CusManagerList._obj.recordCount;//取总记录数 
				/*  检查有效记录的字段否为空 */
				var count = 0;  
				var form = document.getElementById('submitForm');
		    	if(form){ 
		    		CusManagerList._toForm(form);
		    		var handleSuccess = function(o){
						if(o.responseText != undefined){
							try {
								var jsonstr = eval("("+o.responseText+")");
							} catch(e) {
								alert("Parse jsonstr define error!"+e.message);
								return;
							}
							var flag = jsonstr.flag;
							if(flag == 'error'){
								alert("保存失败！");
								window.location.reload();
							}else{
								alert("删除成功！");
								window.location.reload();     
							}
						}
					};

					var url;
		            if('<%=cont_no%>' != null && '<%=cont_no%>' != ""){
		                url = '<emp:url action="addCusManagerRecord.do"/>&serno='+'<%=serno%>'+'&cont_no='+'<%=cont_no%>';
		            }else{
		            	url = '<emp:url action="addCusManagerRecord.do"/>&serno='+'<%=serno%>';
		            }   
					url = EMPTools.encodeURI(url);
					var data = YAHOO.util.Connect.setForm(form);
					var callback = {
							success:handleSuccess,
							failure:function(){alert("保存失败！");}
					};
					var obj1 = YAHOO.util.Connect.asyncRequest('post', url,callback, data);
		    	}
			}
		}
		} else {
			alert('请先选择一条记录！');
		}
	};
	
	// 异步保存客户经理
	function doConfirmSubmit(){
		rowIndexStr=rowIndex;
		var recordCount = CusManagerList._obj.recordCount;//取总记录数 
		/*  检查有效记录的字段否为空 */
		var count = 0;
		var mainManagerNum = 0;//主管客户经理数量
		var totalRate = 0;//业务比例总和
		for(var i=0;i<recordCount;i++){
			var optType = CusManagerList._obj.data[i].optType._getValue();
			if(optType == "" || optType == "add" ){
				count++; 
				var serno = CusManagerList._obj.data[i].serno._getValue();
				var manager_id = CusManagerList._obj.data[i].manager_id._getValue();
				var is_main_manager = CusManagerList._obj.data[i].is_main_manager._getValue();				
				var ser_rate = CusManagerList._obj.data[i].ser_rate._getValue();	
				for(var k=i+1;k<recordCount;k++){
					if(optType == "" || optType == "add" ){
						var manager_id_other = CusManagerList._obj.data[k].manager_id._getValue();
						if(manager_id == manager_id_other){
							alert("第"+(i+1)+"条客户经理与第"+(k+1)+"条客户经理重复！请修改后重新保存！");
							
							return;
						}
					}
				}		
				if( manager_id == "" ){
					alert("第"+ count +"条记录客户经理为空！");
					return;
				}
			    
				if( is_main_manager == "" ){
					alert("第"+ count +"条记录是否主管客户经理为空！");
					return;
				}else if(is_main_manager == "1"){
					mainManagerNum +=1;
			    }
			    
				if( ser_rate == "" ){
					alert("第"+ count +"条记录业务比例为空！");
					return;
				}
				totalRate += parseFloat(ser_rate);
			}
		}
		if(recordCount==0){
			alert("请添加有效数据!");
            return;   
		}
		if(mainManagerNum >1){//主管客户经理只能有一个
			alert("主管客户经理只能有一个，请调整后保存。");
		    return;
		}
		if(totalRate >1){//业务比例总和不能超过100%
			alert("业务比例总和不能超过100%，请调整后保存。");
		    return;
		}
		var form = document.getElementById('submitForm');
    	if(form){ 
    		CusManagerList._toForm(form);
    		var handleSuccess = function(o){
				if(o.responseText != undefined){
					try {
						var jsonstr = eval("("+o.responseText+")");
					} catch(e) {
						alert("Parse jsonstr define error!"+e.message);
						return;
					}
					var flag = jsonstr.flag;
					if(flag == 'error'){
						alert("保存失败！");
						window.location.reload();
					}else if(flag=="del"){
						alert("删除成功！"); 
						window.location.reload();
					}else{
						alert("保存成功！");
						window.location.reload();     
					}
				}
			};
			var url;
            if('<%=cont_no%>' != null && '<%=cont_no%>' != ""){
                url = '<emp:url action="addCusManagerRecord.do"/>&serno='+serno+'&cont_no='+'<%=cont_no%>';
            }else{
            	url = '<emp:url action="addCusManagerRecord.do"/>&serno='+serno; 
            }
			
			url = EMPTools.encodeURI(url);
			var data = YAHOO.util.Connect.setForm(form);
			var callback = {
					success:handleSuccess,
					failure:function(){alert("保存失败！");}
			};
			var obj1 = YAHOO.util.Connect.asyncRequest('post', url,callback, data);
    	}
	}
	 
	function doReset(){
		page.dataGroups.CusManagerGroup.reset();
	};

	
	/*--user code begin--*/
			
	/*--user code end--*/
	
</script>
</head>
<body class="page_content" >

	<div align="left">
		<emp:actButton id="getAddCusManagerPage" label="新增" op="add"/>
		<emp:actButton id="deleteCusManager" label="删除" op="remove"/>
	</div>
	<emp:table icollName="CusManagerList" pageMode="false" url="" >
	    <emp:text id="optType" label="操作方式" hidden="true"  />
		<emp:text id="serno" label="业务编号" hidden="true" flat="false"/>
		<emp:text id="manager_id" label="客户经理编号" flat="false"  readonly="true"/>   
		<emp:text id="manager_id_displayname" label="客户经理名称" />   
		<emp:select id="is_main_manager" label="是否主管客户经理"  dictname="STD_ZX_YES_NO" flat="false"/>
		<emp:text id="ser_rate" label="业务比例" flat="false" dataType="Percent" />
	</emp:table>
	<emp:form id="submitForm" action="" method="POST"></emp:form>
	<div align="center" style="display:${param.optype}" >
		<br>
			<emp:actButton id="confirmSubmit" label="保存列表" op="update"/>
		</div>
</body>
</html>
</emp:page>
    