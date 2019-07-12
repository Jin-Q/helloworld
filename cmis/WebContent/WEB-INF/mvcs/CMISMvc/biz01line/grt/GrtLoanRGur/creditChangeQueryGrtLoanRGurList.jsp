<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<%@page import="com.ecc.emp.core.Context"%>
<%@page import="com.ecc.emp.core.EMPConstance"%>
<% 
	//request = (HttpServletRequest) pageContext.getRequest();
	Context context = (Context) request.getAttribute(EMPConstance.ATTR_CONTEXT);
	String serno="";
	String cus_id="";
	String limit_acc_no="";
	String op = "";
	if(context.containsKey("serno")){
		serno =(String)context.getDataValue("serno");
	}
	if(context.containsKey("cus_id")){
		cus_id =(String)context.getDataValue("cus_id");
	}
	if(context.containsKey("limit_acc_no")){
		limit_acc_no =(String)context.getDataValue("limit_acc_no");
	}  
	if(context.containsKey("op")){
		op =(String)context.getDataValue("op");
	}   
%>
<emp:page>

<html>
<head>
<title>列表查询页面</title>
<jsp:include page="/include.jsp" flush="true"/>
<style type="text/css">
.emp_field_readonly .emp_field_select_input {
	display: inline;
	width: 60px;
	border-color: #b7b7b7;
	background-color: #e3e3e3;
}
.emp_field_select_select { /***** 固定长度 *****/
	width: 60px;
	border-width: 1px;
	border-color: #BCD7E2;
	border-style: solid;
	text-align: left;
}
</style>  

<script type="text/javascript"> 
   rowIndex = 0;
	/*一般担保合同查看
	@flag=loan 业务担保合同关联页面标识，可以查看到业务担保合同关联信息Tab页
	@oper=view 查看按钮标识
	@menuId=ybCount  当Tab页页面在别处调用时，需加上原有的menuId
	 */
	function doViewGrtGuarCont() {
		var paramStr = GrtLoanRGurListYb._obj.getParamStr(['pk_id','guar_cont_no']);   
		var flag ="view";
		if (paramStr != null) {
			var url = '<emp:url action="getGrtGuarContViewPage.do"/>?op=view&menuIdTab=ybCount&flag=ybWh&rel=ywRel&'+paramStr+'&oper='+flag;
			url = EMPTools.encodeURI(url);
			window.open(url,'newwindow2','height=600,width=1200,top=200,left=200,toolbar=no,menubar=no,scrollbars=yes,resizable=yes,location=no,status=no');
		} else {   
			alert('请先选择一条记录！');
		}   
	};

	      
	/*一般担保合同引入 */
	function doGetAddGrtYBGuarPage(){
		if('<%=limit_acc_no%>' == "" || '<%=limit_acc_no%>' == "null"){  
			var url = '<emp:url action="introYbGrtGuarContList.do"/>?isCreditChange=is&serno='+'<%=serno%>'+'&cus_id='+'<%=cus_id%>'+'&cont_no=${context.cont_no}';  
		}else{    
			var url = '<emp:url action="grtGuarYbList.do"/>?cus_id='+'<%=cus_id%>'+'&limit_acc_no='+'<%=limit_acc_no%>'+'&serno='+'<%=serno%>';
		}       
		
		url=EMPTools.encodeURI(url);  
      	window.open(url,'newwindow','height=538,width=1024,top=170,left=200,toolbar=no,menubar=no,scrollbars=yes,resizable=yes,location=no,status=no');
	};
    /*一般担保合同新增
     @ws添加参数 
     @serno 业务申请流水号
     @rel 担保新增后调用接口判断 （ywRel：来自业务模块)
    */
    function doGetAddGrtGuarContPage(){
		var url = '<emp:url action="getGrtYBContAddPage.do"/>?isCreditChange=is&cont_no=${context.cont_no}&op=add&cus_id=${context.cus_id}&menuIdTab=ybCount&flag=ybWh&rel=ywRel&serno='+'<%=serno%>';   
		url = EMPTools.encodeURI(url);    
		window.open(url,'newwindow3','height=600,width=1200,top=200,left=200,toolbar=no,menubar=no,scrollbars=no,resizable=yes,location=no,status=no');
	}; 
	/*一般担保合同修改 
    @ws添加参数 
    @pk_id 业务申请流水号
    @rel 担保新增后调用接口判断 （ywRel：来自业务模块)
   */  
	function doGetUpdateGrtGuarContPage() {
	   
		var paramStr = GrtLoanRGurListYb._obj.getParamStr(['pk_id','guar_cont_no']);  
			if(paramStr != null) {           
				var dataRow = GrtLoanRGurListYb._obj.getSelectedData()[0];
				if(dataRow.corre_rel._getValue() != 2 && dataRow.corre_rel._getValue() != 4){
				   alert("变更类型非新增/续作不能修改!");             
		          return;
				}
				var flag ="update";
				var url = '<emp:url action="getGrtGuarContUpdatePage.do"/>?isCreditChange=is&cont_no=${context.cont_no}&serno=${context.serno}&op=update&flag=ybWh&menuIdTab=ybCount&'+paramStr+'&rel=ywRel&oper='+flag;
				url = EMPTools.encodeURI(url);
				window.open(url,'newwindow1','height=600,width=1200,top=200,left=200,toolbar=no,menubar=no,scrollbars=no,resizable=yes,location=no,status=no');
			}else{     
				alert('请先选择一条记录！');   
			}
	};       
	
	/*一般担保合同删除 */
	function doDeleteGrtLoanRGur() {
		
		var paramStr = GrtLoanRGurListYb._obj.getParamStr(['pk_id','guar_cont_no','guar_cont_state','guar_lvl','serno']);
		if (paramStr != null) { 
			var dataRow = GrtLoanRGurListYb._obj.getSelectedData()[0];
			if(dataRow.corre_rel._getValue() != 2){   
			   alert("变更类型非新增不能删除!");      
	          return;      
			}          
			if(confirm("是否确认要删除？")){   
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
								alert("删除成功!");
								window.location.reload();  
							}else {
								alert("发生异常!");
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
					var url = '<emp:url action="deleteGrtLoanRGurRecord.do"/>?'+paramStr;
					url = EMPTools.encodeURI(url);  
					var obj1 = YAHOO.util.Connect.asyncRequest('POST',url, callback)
				}
		} else {
			alert('请先选择一条记录！');
		}
	};
    /*最高担保额合同引入*/
	function doGetAddGrtLoanRGurZge() { 
		if('<%=limit_acc_no%>' == "" || '<%=limit_acc_no%>' == "null"){ 
			var url = '<emp:url action="introGrtGuarContList.do"/>?action=zg&isCreditChange=is&cus_id='+'<%=cus_id%>'+'&serno='+'<%=serno%>'+'&cont_no=${context.cont_no}';
		}else{
			var url = '<emp:url action="grtGuarZgeList.do"/>?cus_id='+'<%=cus_id%>'+'&limit_acc_no='+'<%=limit_acc_no%>'+'&serno='+'<%=serno%>'; 
		} 
		url=EMPTools.encodeURI(url);    
      	window.open(url,'newwindow','height=538,width=1024,top=170,left=200,toolbar=no,menubar=no,scrollbars=yes,resizable=yes,location=no,status=no');
	};    
	/*最高担保额合同修改*/
	function doGetUpdateGrtLoanRGurZge() {   
		
		var paramStr = GrtLoanRGurListZge._obj.getParamStr(['pk_id','guar_cont_no']); 
		var flag ="update";       
			if (paramStr != null) {
				var dataRow = GrtLoanRGurListZge._obj.getSelectedData()[0];
				if(dataRow.corre_rel._getValue() != 2 && dataRow.corre_rel._getValue() != 4){
				   alert("变更类型非新增/续作不能修改!");      
		           return;
				}
				var url = '<emp:url action="getGrtGuarContUpdatePage.do"/>?isCreditChange=is&cont_no=${context.cont_no}&serno=${context.serno}&menuIdTab=zge&op=view&'+paramStr+'&rel=ywRel&flag=ybWh&oper='+flag;
				url = EMPTools.encodeURI(url);        
				window.open(url,'newwindow1','height=600,width=1200,top=200,left=200,toolbar=no,menubar=no,scrollbars=no,resizable=yes,location=no,status=no');
			} else {            
				alert('请先选择一条记录！');  
			}
	};
	/*最高额担保合同删除 */
	function doDeleteGrtLoanRGurZge() {  
		var paramStr = GrtLoanRGurListZge._obj.getParamStr(['pk_id','guar_lvl','serno']);  
		if (paramStr != null) {     
			var dataRow = GrtLoanRGurListZge._obj.getSelectedData()[0];
			if(dataRow.corre_rel._getValue() != 2){
			   alert("变更类型非新增不能删除!");      
	           return;     
			}   
			if(confirm("是否确认要删除？")){ 
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
								alert("删除成功!");
								window.location.reload(); 
							}else {
								alert("发生异常!");
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
					var url = '<emp:url action="deleteGrtLoanRGurZGE.do"/>?'+paramStr;  
					url = EMPTools.encodeURI(url);
					var obj1 = YAHOO.util.Connect.asyncRequest('POST',url, callback)
				}
			 
		} else {
			alert('请先选择一条记录！');
		}
	};
	   
	/*最高额担保合同查看*/   
	function doViewGrtLoanRGurZge() { 
		var paramStr = GrtLoanRGurListZge._obj.getParamStr(['pk_id','guar_cont_no']);  
		var flag ="view";   
		if (paramStr != null) {  
			var url = '<emp:url action="getGrtGuarContViewPage.do"/>?menuIdTab=zge&op=view&'+paramStr+'&flag=loan&rel=ywRel&oper='+flag;
			url = EMPTools.encodeURI(url);  
			window.open(url,'newwindow2','height=600,width=1200,top=200,left=200,toolbar=no,menubar=no,scrollbars=yes,resizable=yes,location=no,status=no'); 
		} else {
			alert('请先选择一条记录！');     
		}
	};   
	
	function doReset(){
		page.dataGroups.GrtLoanRGurGroup.reset();
	};
    //提升等级
	function doUpLvl(){   
		var dataRow = GrtLoanRGurListYb._obj.getSelectedData()[0];
 		var paramStr = dataRow.pk_id._getValue();
		if (paramStr != null) {      
			var row = dataRow.displayid._getValue();
			if(row==1){ 
               alert("已是本次担保合同中最高等级");
               return;  
			}else{ 
			  var pk = GrtLoanRGurListYb._obj.data[row-2].pk_id._getValue(); 
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
							alert("提升等级成功!");
							window.location.reload(); 
						}else {
							alert("发生异常!");
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
				var url = '<emp:url action="upLvl.do"/>?pk_id='+paramStr+'&pk_up='+pk;  
				url = EMPTools.encodeURI(url);
				var obj1 = YAHOO.util.Connect.asyncRequest('POST',url, callback)
		    }
		} else {  
			alert('请先选择一条记录！');
		}     
    };
    //最高额提升等级
	function doUpLvlZge(){
		var dataRow = GrtLoanRGurListZge._obj.getSelectedData()[0];
		var paramStr = dataRow.pk_id._getValue();
		if (paramStr != null) {
			var row = dataRow.displayid._getValue();
			if(row==1){ 
               alert("已是本次担保合同中最高等级");
               return;  
			}else{
			  var pk = GrtLoanRGurListZge._obj.data[row-2].pk_id._getValue(); 
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
							alert("提升等级成功!");
							window.location.reload(); 
						}else {
							alert("发生异常!");
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
				var url = '<emp:url action="upLvl.do"/>?pk_id='+paramStr+'&pk_up='+pk; 
				url = EMPTools.encodeURI(url);
				var obj1 = YAHOO.util.Connect.asyncRequest('POST',url, callback)
		    }
		} else {  
			alert('请先选择一条记录！');
		}     
    };
    //最高额降低等级
    function doDownLvlZge(){
    	var dataRow = GrtLoanRGurListZge._obj.getSelectedData()[0];
    	var paramStr = dataRow.pk_id._getValue();
		if (paramStr != null) {
			var row = dataRow.displayid._getValue();
			var recordCount = GrtLoanRGurListZge._obj.recordCount;
			if(row==recordCount){
               alert("已是本次担保合同中最低等级");
               return;  
			}else{ 
				var pk = GrtLoanRGurListZge._obj.data[row].pk_id._getValue();  
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
								alert("降低等级成功!");  
								window.location.reload(); 
							}else {
								alert("发生异常!");
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
					var url = '<emp:url action="upLvl.do"/>?pk_id='+paramStr+'&pk_up='+pk; 
					url = EMPTools.encodeURI(url);
					var obj1 = YAHOO.util.Connect.asyncRequest('POST',url, callback)
		    }
		} else { 
			alert('请先选择一条记录！');
		} 
    };  
    //降低等级
    function doDownLvl(){
    	var dataRow = GrtLoanRGurListYb._obj.getSelectedData()[0];
    	var paramStr = dataRow.pk_id._getValue();
		if (paramStr != null) {
			var row = dataRow.displayid._getValue();
			var recordCount = GrtLoanRGurListYb._obj.recordCount;
			if(row==recordCount){
               alert("已是本次担保合同中最低等级");
               return;  
			}else{
				var pk = GrtLoanRGurListYb._obj.data[row].pk_id._getValue();  
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
								alert("降低等级成功!");  
								window.location.reload(); 
							}else {
								alert("发生异常!");
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
					var url = '<emp:url action="upLvl.do"/>?pk_id='+paramStr+'&pk_up='+pk; 
					url = EMPTools.encodeURI(url);
					var obj1 = YAHOO.util.Connect.asyncRequest('POST',url, callback)  
		    }
		} else { 
			alert('请先选择一条记录！');
		} 
    };   
    /****初始化页面，去除变更类型中的请选择、新增、正常的字典项***/
    function load(){   
        var recordCountZGE = GrtLoanRGurListZge._obj.recordCount;//取最高额总记录数
        //如果记录数为0则不处理
        if(recordCountZGE>0){
        	for(var m=0;m<recordCountZGE;m++){
        		var options = GrtLoanRGurListZge._obj.data[m].corre_rel._obj.element.options;
        		var corre_rel = GrtLoanRGurListZge._obj.data[m].corre_rel._getValue();
        		if(corre_rel != 2){
        			for(var i=options.length-1;i>=0;i--){	
                		if(options[i].value=="" || options[i].value=="1" || options[i].value=="2" || options[i].value=="5" || options[i].value=="6"){
                			options.remove(i);            
                		}
                	}
                	//如果是查看则全部只读
        			if('${context.op}'=="view"){
        				GrtLoanRGurListZge._obj.data[m].corre_rel._obj._renderReadonly(true); 
            		}
            	}else{
            		GrtLoanRGurListZge._obj.data[m].corre_rel._obj._renderReadonly(true);   
                }
            } 
        }  
    	
        var recordCountYB = GrtLoanRGurListYb._obj.recordCount;//取一般担保总记录数
        //如果记录数为0则不处理    
        if(recordCountYB>0){ 
        	for(var m=0;m<recordCountYB;m++){ 
        		var options = GrtLoanRGurListYb._obj.data[m].corre_rel._obj.element.options;
        		var corre_rel = GrtLoanRGurListYb._obj.data[m].corre_rel._getValue();
        		if(corre_rel != 2){
        			for(var i=options.length-1;i>=0;i--){	
                		if(options[i].value=="" || options[i].value=="1" || options[i].value=="2" || options[i].value=="5" || options[i].value=="6"){
                			options.remove(i);                 
                		} 
                	}  
        			//如果是查看则全部只读  
        			
        			if('${context.op}'=="view"){
            			GrtLoanRGurListYb._obj.data[m].corre_rel._obj._renderReadonly(true); 
            		}  
        		}else{ 
        			GrtLoanRGurListYb._obj.data[m].corre_rel._obj._renderReadonly(true);    
            	}
            }
        }
    };
    /**变更类型选择删除和续作时改变对应数据关联关系状态**/
    function changeCorreRelZGE(){
        var data = GrtLoanRGurListZge._obj.getSelectedData()[0]; 
        if (data == null) {
            alert("请选中需要做变更的担保合同，再调整变更类型!");
            window.location.reload();
        	return;
    	}     
        var corre_rel = data.corre_rel._getValue();
        var pk_id = data.pk_id._getValue();
        if(corre_rel == 3 || corre_rel == 4){   
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
    					if(corre_rel == "3"){
        					alert("解除成功!");
            			}else if(corre_rel == "4"){
            				alert("续作成功!");
                		}
    					window.location.reload(); 
    				}else {
    					alert("发生异常!");
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
    		var url = '<emp:url action="updateCorreRelRecord.do"/>?pk_id='+pk_id+'&corre_rel='+corre_rel;  
    		url = EMPTools.encodeURI(url);   
    		var obj1 = YAHOO.util.Connect.asyncRequest('POST',url, callback)
        }else{
            alert("变更类型非解除和续作,请检查!");
            window.location.reload();  
        } 
    }; 

    function changeCorreRelYB(){
    	var data = GrtLoanRGurListYb._obj.getSelectedData()[0]; 
    	if (data == null) {
    		alert("请选中需要做变更的担保合同，再调整变更类型!");
            window.location.reload();
        	return;
    	}
        var corre_rel = data.corre_rel._getValue();   
        var pk_id = data.pk_id._getValue();
        if(corre_rel == 3 || corre_rel == 4){   
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
        				if(corre_rel == "3"){
        					alert("解除成功!");
            			}else if(corre_rel == "4"){
            				alert("续作成功!");
                		}
    					window.location.reload(); 
    				}else {
    					alert("发生异常!");
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
    		var url = '<emp:url action="updateCorreRelRecord.do"/>?pk_id='+pk_id+'&corre_rel='+corre_rel;
    		url = EMPTools.encodeURI(url);
    		var obj1 = YAHOO.util.Connect.asyncRequest('POST',url, callback)
        }else{
            alert("变更类型非解除和续作,请检查!");  
            window.location.reload();
        } 
    }; 
   
	/*--user code begin--*/

	/*--user code end--*/ 
	
</script>
</head>
<body class="page_content" onload="load()">
	<form  method="POST" action="#" id="queryForm">
	</form>       
    
	<b>最高额担保</b>
	<div align="left">
		<emp:actButton id="getAddGrtLoanRGurZge" label="引入" op="add"/>
		<emp:actButton id="getUpdateGrtLoanRGurZge" label="修改" op="update"/>  
		<emp:actButton id="deleteGrtLoanRGurZge" label="重置引入" op="remove"/>
		<emp:actButton id="viewGrtLoanRGurZge" label="查看" op="view"/>   
	</div>     

	<emp:table icollName="GrtLoanRGurListZge" pageMode="false" url="pageGrtLoanRGurQueryZGE.do">
	    <emp:text id="pk_id" label="pk_id" hidden="true"/>
	     <emp:text id="serno" label="业务流水号" hidden="true"/>     
		<emp:text id="guar_cont_no" label="担保合同编号" />
		<emp:text id="guar_cont_type" label="担保合同类型" dictname="STD_GUAR_CONT_TYPE"/>
		<emp:select id="guar_way" label="担保方式"  dictname="STD_GUAR_TYPE" />
		<emp:text id="guar_amt" label="担保合同金额" dataType="Currency"/>
		<emp:text id="this_guar_amt" label="本次担保金额" dataType="Currency"/>
		<emp:text id="is_per_gur" label="是否阶段性担保" dictname="STD_ZX_YES_NO"/>
		<emp:select id="guar_cont_state" label="担保状态"  dictname="STD_CONT_STATUS"/>
		<emp:select id="is_add_guar" label="是否追加担保" dictname="STD_ZX_YES_NO"/>
		<emp:select id="corre_rel" label="变更类型" dictname="STD_BIZ_CORRE_REL" flat="false" onchange="changeCorreRelZGE()" />          
		<emp:text id="guar_lvl" label="等级 " />
		<%if(!"view".equals(op)){%>
		<emp:link id="upZge" label="提升" imageFile="images/default/arrow_up.gif" opName="提升" hidden="false" operation="upLvlZge"/>  
		<emp:link id="downZge" label="降低 " imageFile="images/default/arrow_down.gif" opName="降低" operation="downLvlZge" hidden="false"/> 
	    <%} %>
	</emp:table>      
	   
	<br><br>
	<b>一般担保合同</b>
	<div align="left">   
		<emp:actButton id="getAddGrtYBGuarPage" label="引入" op="add"/>
		<emp:actButton id="getAddGrtGuarContPage" label="新增" op="add"/> 
		<emp:actButton id="getUpdateGrtGuarContPage" label="修改" op="update"/>
		<emp:actButton id="deleteGrtLoanRGur" label="删除" op="remove"/>
		<emp:actButton id="viewGrtGuarCont" label="查看" op="view"/>   
	</div>
	   
	<emp:table icollName="GrtLoanRGurListYb" pageMode="false" url="pageGrtLoanRGurQueryYB.do"> 
	    <emp:text id="pk_id" label="pk_id" hidden="true"/>   
	    <emp:text id="serno" label="业务流水号" hidden="true"/>     
		<emp:text id="guar_cont_no" label="担保合同编号" />
		<emp:text id="guar_cont_type" label="担保合同类型" dictname="STD_GUAR_CONT_TYPE"/>
		<emp:select id="guar_way" label="担保方式"  dictname="STD_GUAR_TYPE" />
		<emp:text id="guar_amt" label="担保合同金额" dataType="Currency"/>
		<emp:text id="this_guar_amt" label="本次担保金额" dataType="Currency"/>   
		<emp:text id="is_per_gur" label="是否阶段性担保" dictname="STD_ZX_YES_NO"/>
		<emp:select id="guar_cont_state" label="担保状态"  dictname="STD_CONT_STATUS"/>   
		<emp:select id="is_add_guar" label="是否追加担保" dictname="STD_ZX_YES_NO"/>
		<emp:select id="corre_rel" label="变更类型" dictname="STD_BIZ_CORRE_REL" flat="false"  onchange="changeCorreRelYB()" />
		<emp:text id="guar_lvl" label="等级 "/>
		<%if(!"view".equals(op)){%>
		    <emp:link id="up" label="提升" imageFile="images/default/arrow_up.gif" opName="提升" hidden="false" operation="upLvl"/>  
		    <emp:link id="down" label="降低 " imageFile="images/default/arrow_down.gif" opName="降低" operation="downLvl" hidden="false"/>
	   <%} %>
	</emp:table>   
	  
</body>
</html>
</emp:page>
    