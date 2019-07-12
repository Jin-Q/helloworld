<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp"%>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<script type="text/javascript">
	rowIndex = 0;
	function doViewGrtGuarCont() {
		var paramStr = RLmtGuarContListYb._obj.getParamStr(['limit_code','guar_cont_no']);   
		var flag ="view";       
		if (paramStr != null) {   
			var url = '<emp:url action="getGrtGuarContViewPage.do"/>?op=view&menuIdTab=ybCount&flag=ybWh&rel=sxRel&'+paramStr+'&oper='+flag;
			url = EMPTools.encodeURI(url);
			window.open(url,'newwindow2','height=600,width=1200,top=100,left=100,toolbar=no,menubar=no,scrollbars=yes,resizable=yes,location=no,status=no');
		} else {
			alert('请先选择一条记录！'); 
		}   
	};
	
	/*一般担保合同引入 */
	function doGetAddGrtYBGuarPage(){
		var url = '<emp:url action="introYbGrtGuarContList.do"/>?rel=sxRel&limit_code=${context.org_limit_code}';
		url=EMPTools.encodeURI(url);    
      	window.open(url,'newwindow','height=538,width=1024,top=100,left=100,toolbar=no,menubar=no,scrollbars=yes,resizable=yes,location=no,status=no');
	};
	
    /*一般担保合同新增
     @ws添加参数 
     @serno 业务申请流水号
     @rel 担保新增后调用接口判断 （sxRel：来自授信模块)
    */
    function doGetAddGrtGuarContPage(){
		var url = '<emp:url action="getGrtYBContAddPage.do"/>?op=add&menuIdTab=ybCount&flag=ybWh&rel=sxRel&limit_code=${context.org_limit_code}&serno=${context.serno}&cus_id=${context.cus_id}';   
		url = EMPTools.encodeURI(url);
		window.open(url,'newwindow3','height=600,width=1200,top=100,left=100,toolbar=no,menubar=no,scrollbars=no,resizable=yes,location=no,status=no');
	};
	
	/*一般担保合同修改 
    @rel 担保新增后调用接口判断 （sxRel：来自授信模块)
   */  
	function doGetUpdateGrtGuarContPage() {
		var paramStr = RLmtGuarContListYb._obj.getParamStr(['limit_code','guar_cont_no']);
			if(paramStr != null) {         
				var flag ="update";
				/**只有登记状态的担保合同才能修改*/
				var guar_cont_state = RLmtGuarContListYb._obj.getParamValue(['guar_cont_state']); 
				if("00"!=guar_cont_state){
					alert("只有担保状态为【登记】的担保合同才能修改！");
					return false;
				}
				/** END */
				var url = '<emp:url action="getGrtGuarContUpdatePage.do"/>?op=update&flag=ybWh&menuIdTab=ybCount&'+paramStr+'&rel=sxRel&oper='+flag;
				url = EMPTools.encodeURI(url);
				window.open(url,'newwindow1','height=600,width=1200,top=100,left=100,toolbar=no,menubar=no,scrollbars=no,resizable=yes,location=no,status=no');
			}else{  
				alert('请先选择一条记录！');  
			}
	};       
	
	/*一般担保合同删除 */
	function doDeleteGrtLoanRGur() {
		var paramStr = RLmtGuarContListYb._obj.getParamStr(['limit_code','guar_cont_no','guar_cont_state','guar_lvl']);
		if (paramStr != null) {   
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
					var url = '<emp:url action="deleteRLmtGuarContRecord.do"/>?'+paramStr+'&serno=${context.serno}';
					url = EMPTools.encodeURI(url);     
					var obj1 = YAHOO.util.Connect.asyncRequest('POST',url, callback)
				}
		} else {
			alert('请先选择一条记录！');
		}
	};
	
    /*最高担保额合同引入*/
	function doGetAddRLmtGuarContZge() {
		var url = '<emp:url action="introGrtGuarContList.do"/>?action=zg&rel=sxRel&limit_code=${context.org_limit_code}';
		url=EMPTools.encodeURI(url);   
      	window.open(url,'newwindow','height=538,width=1024,top=100,left=100,toolbar=no,menubar=no,scrollbars=yes,resizable=yes,location=no,status=no');
	};

	/*最高额担保合同新增
    @ws添加参数 
    @serno 业务申请流水号
    @rel 担保新增后调用接口判断 （sxRel：来自授信模块)
   */
   function doGetHighGrtGuarContAddPage(){
		var url = '<emp:url action="getHighGrtGuarContAddPage.do"/>?op=add&menuIdTab=zge&flag=ybWh&rel=sxRel&limit_code=${context.org_limit_code}&serno=${context.serno}&cus_id=${context.cus_id}';   
		url = EMPTools.encodeURI(url);
		window.open(url,'newwindow3','height=600,width=1200,top=100,left=100,toolbar=no,menubar=no,scrollbars=no,resizable=yes,location=no,status=no');
	};
	
	/*最高担保额合同修改*/
	function doGetUpdateRLmtGuarContZge() {    
		var paramStr = RLmtGuarContListZge._obj.getParamStr(['limit_code','guar_cont_no']); 
		var flag ="update";        
			if (paramStr != null) {
				/**只有登记状态的担保合同才能修改*/
				var guar_cont_state = RLmtGuarContListZge._obj.getParamValue(['guar_cont_state']); 
				if("00"!=guar_cont_state){
					alert("只有担保状态为【登记】的担保合同才能修改！");
					return false;
				}
				/** END */
				var url = '<emp:url action="getGrtGuarContUpdatePage.do"/>?menuIdTab=zge&op=update&'+paramStr+'&rel=sxRel&flag=ybWh&oper='+flag;
				url = EMPTools.encodeURI(url);        
				window.open(url,'newwindow1','height=600,width=1200,top=100,left=100,toolbar=no,menubar=no,scrollbars=no,resizable=yes,location=no,status=no');
			} else {            
				alert('请先选择一条记录！');
			} 
	}; 
	/*最高额担保合同删除 */
	function doDeleteRLmtGuarContZge() {     
		var paramStr = RLmtGuarContListZge._obj.getParamStr(['limit_code','guar_cont_no','guar_lvl','guar_cont_state']);  
		if (paramStr != null) {      
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
				var url = '<emp:url action="deleteRLmtGuarContZGE.do"/>?'+paramStr+'&serno=${context.serno}';
				url = EMPTools.encodeURI(url);
				var obj1 = YAHOO.util.Connect.asyncRequest('POST',url, callback)
			}
		} else {
			alert('请先选择一条记录！');
		}
	};
	   
	/*最高额担保合同查看*/   
	function doViewRLmtGuarContZge() {      
		var paramStr = RLmtGuarContListZge._obj.getParamStr(['limit_code','guar_cont_no']);  
		var flag ="view"; 
		if (paramStr != null) {   
			var url = '<emp:url action="getGrtGuarContViewPage.do"/>?menuIdTab=zge&op=view&'+paramStr+'&flag=loan&rel=sxRel&oper='+flag;
			url = EMPTools.encodeURI(url);      
			window.open(url,'newwindow2','height=600,width=1200,top=200,left=200,toolbar=no,menubar=no,scrollbars=yes,resizable=yes,location=no,status=no'); 
		} else {   
			alert('请先选择一条记录！'); 
		}
	};   
	
    //提升等级 
	function doUpLvl(){   
		var dataRow = RLmtGuarContListYb._obj.getSelectedData()[0];
 		var limit_code = dataRow.limit_code._getValue();
 		var guar_cont_no = dataRow.guar_cont_no._getValue();   
 		var serno = dataRow.serno._getValue();   
		if (limit_code != null && guar_cont_no !=null) {      
			var row = dataRow.displayid._getValue();
			if(row==1){
               alert("已是本次担保合同中最高等级");
               return;  
			}else{  
			  var limit_code_up = RLmtGuarContListYb._obj.data[row-2].limit_code._getValue(); 
			  var guar_cont_no_up = RLmtGuarContListYb._obj.data[row-2].guar_cont_no._getValue(); 
			  var serno_up = RLmtGuarContListYb._obj.data[row-2].serno._getValue(); 
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
				var url = '<emp:url action="lmtLvl.do"/>?limit_code='+limit_code+'&guar_cont_no='+guar_cont_no+'&serno='+serno+'&limit_code_up='+limit_code_up+'&guar_cont_no_up='+guar_cont_no_up+'&serno_up='+serno_up;  
				url = EMPTools.encodeURI(url);
				var obj1 = YAHOO.util.Connect.asyncRequest('POST',url, callback)
		    }
		} else {  
			alert('请先选择一条记录！');
		}     
    };
    
    //最高额提升等级
	function doUpLvlZge(){
		var dataRow = RLmtGuarContListZge._obj.getSelectedData()[0];
		var limit_code = dataRow.limit_code._getValue();
 		var guar_cont_no = dataRow.guar_cont_no._getValue();
 		var serno = dataRow.serno._getValue();   
		if (limit_code != null && guar_cont_no !=null) {
			var row = dataRow.displayid._getValue();
			if(row==1){ 
               alert("已是本次担保合同中最高等级");
               return;  
			}else{
			  var limit_code_up = RLmtGuarContListZge._obj.data[row-2].limit_code._getValue(); 
			  var guar_cont_no_up = RLmtGuarContListZge._obj.data[row-2].guar_cont_no._getValue(); 
			  var serno_up = RLmtGuarContListZge._obj.data[row-2].serno._getValue(); 
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
				var url = '<emp:url action="lmtLvl.do"/>?limit_code='+limit_code+'&guar_cont_no='+guar_cont_no+'&serno='+serno+'&limit_code_up='+limit_code_up+'&guar_cont_no_up='+guar_cont_no_up+'&serno_up='+serno_up; 
				url = EMPTools.encodeURI(url);
				var obj1 = YAHOO.util.Connect.asyncRequest('POST',url, callback)
		    }
		} else {  
			alert('请先选择一条记录！');
		}     
    };
    
    //最高额降低等级
    function doDownLvlZge(){
    	var dataRow = RLmtGuarContListZge._obj.getSelectedData()[0];
    	var limit_code = dataRow.limit_code._getValue();
 		var guar_cont_no = dataRow.guar_cont_no._getValue();
 		var serno = dataRow.serno._getValue();
		if (limit_code != null && guar_cont_no !=null) {
			var row = dataRow.displayid._getValue();
			var recordCount = RLmtGuarContListZge._obj.recordCount;
			if(row==recordCount){
               alert("已是本次担保合同中最低等级");
               return;  
			}else{ 
				var limit_code_down = RLmtGuarContListZge._obj.data[row].limit_code._getValue();  
				var guar_cont_no_down = RLmtGuarContListZge._obj.data[row].guar_cont_no._getValue();  
				var serno_down = RLmtGuarContListZge._obj.data[row].serno._getValue();  
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
					var url = '<emp:url action="lmtLvl.do"/>?limit_code='+limit_code+'&guar_cont_no='+guar_cont_no+'&serno='+serno+'&limit_code_up='+limit_code_down+'&guar_cont_no_up='+guar_cont_no_down+'&serno_up='+serno_down;   
					url = EMPTools.encodeURI(url);
					var obj1 = YAHOO.util.Connect.asyncRequest('POST',url, callback)
		    }
		} else { 
			alert('请先选择一条记录！');
		} 
    }; 
    
    //降低等级
    function doDownLvl(){
    	var dataRow = RLmtGuarContListYb._obj.getSelectedData()[0];
    	var limit_code = dataRow.limit_code._getValue();
 		var guar_cont_no = dataRow.guar_cont_no._getValue();
 		var serno = dataRow.serno._getValue();
		if (limit_code != null && guar_cont_no !=null) {
			var row = dataRow.displayid._getValue();
			var recordCount = RLmtGuarContListYb._obj.recordCount;
			if(row==recordCount){
               alert("已是本次担保合同中最低等级");
               return;  
			}else{
				var limit_code_down = RLmtGuarContListYb._obj.data[row].limit_code._getValue();  
				var guar_cont_no_down = RLmtGuarContListYb._obj.data[row].guar_cont_no._getValue();  
				var serno_down = RLmtGuarContListYb._obj.data[row].serno._getValue();  
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
					var url = '<emp:url action="lmtLvl.do"/>?limit_code='+limit_code+'&guar_cont_no='+guar_cont_no+'&serno='+serno+'&limit_code_up='+limit_code_down+'&guar_cont_no_up='+guar_cont_no_down+'&serno_up='+serno_down;   
					url = EMPTools.encodeURI(url);
					var obj1 = YAHOO.util.Connect.asyncRequest('POST',url, callback)  
		    }  
		} else { 
			alert('请先选择一条记录！');
		} 
    };              

</script>