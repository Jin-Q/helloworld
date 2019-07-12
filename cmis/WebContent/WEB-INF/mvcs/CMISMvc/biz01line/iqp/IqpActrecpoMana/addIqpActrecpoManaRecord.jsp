<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<%@page import="com.ecc.emp.core.EMPConstance"%>
<%@page import="com.ecc.emp.core.Context"%>
<emp:page>
<html>
<head>
<title>新增页面</title>
<style type="text/css">
.emp_input{
border:1px solid #b7b7b7;
width:160px;
}

.emp_input2{
border:1px solid #b7b7b7;
width:430px;
}
</style>
<jsp:include page="/include.jsp" flush="true"/>
<%	
	Context context = (Context)request.getAttribute(EMPConstance.ATTR_CONTEXT);
	String type = "";
	String poType = "";
	if(context.containsKey("type")){
		type = context.getDataValue("type").toString();
	}
	poType = context.getDataValue("PO_TYPE").toString();
	if(type.equals("view")){
		request.setAttribute("canwrite","");
	}
	String openType = "";
	if(context.containsKey("openType")){
		openType = context.getDataValue("openType").toString();
	}
	
	String cus_id = "";//关联其担保客户。
	if(context.containsKey("cus_id")){
		cus_id = (String)context.getDataValue("cus_id");
	}
	/**add by lisj 2015-1-30 需求编号【HS141110017】保理业务改造 begin**/
	String ggcbl ="";
	if(context.containsKey("ggcbl")){
		ggcbl = context.getDataValue("ggcbl").toString();
	}
	/**add by lisj 2015-1-30 需求编号【HS141110017】保理业务改造 end**/
%>
<script type="text/javascript">
	/**modified by yangzy 2015-06-01 需求编号【HS141110017】保理业务改造  start**/
	var acct_no_ori = "";
	/**modified by yangzy 2015-06-01 需求编号【HS141110017】保理业务改造  end**/
	function doOnLoad(){ 
		IqpActrecpoMana.po_type._setValue('<%=poType%>');
		/**modified by lisj 2015-1-30 需求编号【HS141110017】保理业务改造  begin**/
		//IqpActrecpoMana.bail_acc_no._obj.addOneButton("bail_acc_no","获取",getBailAccName);
		/**add by lisj 2014年11月10日 增加影像押品编号字段  begin**/
		if(<%=!type.equals("view")%>){
			IqpActrecpoMana.image_guaranty_no._obj.addOneButton("image_guaranty_no","获取",getBillForm);
		}
		/**add by lisj 2014年11月10日 增加影像押品编号字段  end**/
		var poType = <%=poType %>;
		//当池类型为【应收账款池】时，回款保证金默认为一个账户
		if(poType == "1" && <%=!type.equals("view")%>){
			doPoType1Init();
		}else if(poType == "2" && <%=!type.equals("view")%>){
			/** add by wangj 2015-5-20  需求编号【HS141110017】保理业务改造   begin */
			doPoType1Init();
			/** add by wangj 2015-5-20  需求编号【HS141110017】保理业务改造   end */
		}
		/**modified by lisj 2015-1-30 需求编号【HS141110017】保理业务改造  end**/
	};
	
	/**modified by lisj 2015-1-30 需求编号【HS141110017】保理业务改造  begin**/
	function doPoType1Init(){
		var recordCount = IqpBailaccDetailList._obj.recordCount;//取总记录数
		if(recordCount == "0"){
			//增加一条记录，并只读
			IqpBailaccDetailList._obj._addRow();
			IqpBailaccDetailList._obj.recordCount +=1; 	//增加总记录数
			var recordCount = IqpBailaccDetailList._obj.recordCount;//取总记录数
			IqpBailaccDetailList._obj.data[recordCount-1].optType._setValue("add");//判断操作方式
			var poNo = IqpActrecpoMana.po_no._getValue();
			IqpBailaccDetailList._obj.data[recordCount-1].po_no._setValue(poNo);//设置池编号
			var row = recordCount-1;
			var id1 = row + '_view1';//每一个按钮id都是固定的
			var id = id1;
			IqpBailaccDetailList._obj.data[recordCount-1].bail_acc_no._obj.addOneButton(id,'获取',getAccountInfo);
			var id2 = row + '_view2';//每一个按钮id都是固定的
			var id = id2;
			IqpBailaccDetailList._obj.data[recordCount-1].cus_id._obj.addOneButton(id,'选择',queryAllCusId);
		}else{
			IqpBailaccDetailList._obj.data[recordCount-1].optType._setValue("add");//判断操作方式
			var poNo = IqpActrecpoMana.po_no._getValue();
			IqpBailaccDetailList._obj.data[recordCount-1].po_no._setValue(poNo);//设置池编号
			var row = recordCount-1;
			var id1 = row + '_view1';//每一个按钮id都是固定的
			var id = id1;
			IqpBailaccDetailList._obj.data[recordCount-1].bail_acc_no._obj.addOneButton(id,'获取',getAccountInfo);
			var id2 = row + '_view2';//每一个按钮id都是固定的
			var id = id2;
			IqpBailaccDetailList._obj.data[recordCount-1].cus_id._obj.addOneButton(id,'选择',queryAllCusId);
		}
	};

	function doPoType2Init(){
		var recordCount = IqpBailaccDetailList._obj.recordCount;//取总记录数
		for(var idx=0;idx<recordCount;idx++){
			var id1 = idx + '_view1';//每一个按钮id都是固定的
			var id = id1;
			IqpBailaccDetailList._obj.data[idx].bail_acc_no._obj.addOneButton(id,'获取',getAccountInfo);
			var id2 = idx + '_view2';//每一个按钮id都是固定的
			var id = id2;
			IqpBailaccDetailList._obj.data[idx].cus_id._obj.addOneButton(id,'选择',queryAllCusId);
		}
    };
    /**modified by lisj 2015-1-30 需求编号【HS141110017】保理业务改造  end**/
    
	/**add by lisj 2014年11月10日 增加影像押品编号字段  begin**/
	function getBillForm(){
		var cus_id = IqpActrecpoMana.cus_id._getValue();
		if(cus_id==null||cus_id== ""){
			alert('请先录入客户码');
		}else{
			var url = "<emp:url action='delImageGuaranteeNoAction.do'/>&returnMethod=getRate";
	      	url=encodeURI(url); 
	      	window.open(url,'newwindow','height='+window.screen.availHeight*0.8+',width='+window.screen.availWidth*0.9+',top=70,left=0,toolbar=no,menubar=no,scrollbars=yes,resizable=yes,location=no,status=no');
		}
	};
	function getRate(data){
		IqpActrecpoMana.image_guaranty_no._setValue(data.PLEDGE_NO._getValue());
	};
	/**add by lisj 2014年11月10日 增加影像押品编号字段  end**/
	/**modified by lisj 2015-1-30 需求编号【HS141110017】保理业务改造 (注释原方法) begin**/
	/**function getBailAccName(){
		 var acctNo = IqpActrecpoMana.bail_acc_no._getValue();
	        if(acctNo == null || acctNo == ""){
				alert("请先输入保证金账号信息！");
				return;
	        }
			var handleSuccess = function(o){
				if(o.responseText !== undefined) {
					try {
						var jsonstr = eval("("+o.responseText+")");
					} catch(e) {
						alert("Parse jsonstr1 define error!" + e.message);
						return;
					}
					var flag = jsonstr.flag;
					var retMsg = jsonstr.retMsg;
					var GUARANTEE_ACCT_NO = jsonstr.BODY.GUARANTEE_ACCT_NO;
					var GUARANTEE_ACCT_NAME = jsonstr.BODY.GUARANTEE_ACCT_NAME;
					var CCY = jsonstr.BODY.CCY;
					var AMT = jsonstr.BODY.AMT;
					var GUARANTEE_TYPE = jsonstr.BODY.GUARANTEE_TYPE;
					var INT_RATE = jsonstr.BODY.INT_RATE;
					var INTER_FLT_RATE = jsonstr.BODY.INTER_FLT_RATE;
					var TERM = jsonstr.BODY.TERM;
					var OPEN_ACCT_BRANCH_ID = jsonstr.BODY.OPEN_ACCT_BRANCH_ID;
					if(flag == "success"){
						IqpActrecpoMana.bail_acc_name._setValue(GUARANTEE_ACCT_NAME);
						//保证金账户联机获取时提示余额  2014-10-15 唐顺岩
						alert("账户户名："+GUARANTEE_ACCT_NAME+"\n 账户余额："+AMT);
					}else {
						alert(retMsg); 
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
			var url = '<emp:url action="clientTrade4Esb.do"/>?bail_acct_no='+acctNo+'&service_code=11003000007&sence_code=16';	
			url = EMPTools.encodeURI(url);
			var obj1 = YAHOO.util.Connect.asyncRequest('POST',url, callback,null);
		}**/
		/**modified by lisj 2015-1-30 需求编号【HS141110017】保理业务改造 (注释原方法) end**/
	function doReturn() {
		var menuId = '${context.menuId}';
		if(menuId == 'fpdqtx_ywtx'){
			var url = '<emp:url action="queryAccActrecpoRemindList.do"/>?menuId='+menuId;
			url = EMPTools.encodeURI(url);
			window.parent.location=url;
		}else{
			var poType = '<%=poType%>';
			if(poType!=2){
				var menuId = '${context.menuId}';
			}else{
				var menuId = 'blcgl';
			}
			var url = '<emp:url action="queryIqpActrecpoManaList.do"/>?menuId='+menuId+'&PO_TYPE='+poType;
			url = EMPTools.encodeURI(url);
			if(<%=openType.equals("")%>){
				window.location=url;
			}else{
				window.parent.location=url;
			}
		}		
	}

	/**modified by lisj 2015-1-30 需求编号【HS141110017】保理业务改造 begin**/
	function doAdd(){
		var form = document.getElementById("submitForm");
		if(!IqpActrecpoMana._checkAll()){
			return;
		}
		//校验回款保证金明细
		var recordCount = IqpBailaccDetailList._obj.recordCount;//取总记录数
		var count = 0;
		var poType = <%=poType %> ;
		if(recordCount != 0){
			for(var i=0;i<recordCount;i++){
				var optType = IqpBailaccDetailList._obj.data[i].optType._getValue();
				if(optType == "" || optType == "add" ){
					count++;
					var bail_acc_no = IqpBailaccDetailList._obj.data[i].bail_acc_no._getValue();
					var cus_id = IqpBailaccDetailList._obj.data[i].cus_id._getValue();
					var bail_acc_name = IqpBailaccDetailList._obj.data[i].bail_acc_name._getValue();
					if(acct_no_ori!=bail_acc_no && poType == "2"){
						alert("待处理保理资金账号有变动，请重新获取户名");
						return;
				 	}
					if(acct_no_ori!=bail_acc_no && poType == "1"){
						alert("回款保证金账号有变动，请重新获取户名");
						return;
				 	}
					if( bail_acc_no == "" && poType == "2"){
						alert("待处理保理资金账号为空！");
						return;
					}else if(bail_acc_no == "" && poType == "1"){
						alert("回款保证金账号为空！");
						return;
					}
					if( cus_id == "" && poType == "2"){
						alert("买方客户号为空！");
						return;
					}else if(cus_id == "" && poType == "1"){
						alert("买方客户号为空！");
						return;
					}
					if( bail_acc_name == "" && poType == "2"){
						alert("待处理保理资金账户名为空！");
						return;
					}else if(bail_acc_name == "" && poType == "1"){
						alert("回款保证金账户名为空！");
						return;
					}
				}
			}
		}else{
			if(poType == "2"){
				alert("请录入待处理保理资金明细！");
			}else{
				alert("请录入回款保证金明细！");
			}
			return;
		}
		IqpActrecpoMana._toForm(form);
		IqpBailaccDetailList._toForm(form);
			var handleSuccess = function(o){
				if(o.responseText !== undefined) {
					try {
						var jsonstr = eval("("+o.responseText+")");
					} catch(e) {
						alert("Parse jsonstr1 define error!" + e.message);
						return;
					}
					var flag = jsonstr.flag;
					var po_no = jsonstr.po_no;
					if(flag == "success"){
						alert("保存成功！");
						if(<%=openType.equals("")%>){
							var url = '<emp:url action="getIqpActrecpoManaTabHelp.do"/>?po_no='+po_no+'&PO_TYPE=<%=poType%>&type=add';
							url = EMPTools.encodeURI(url);
							window.location=url;
						}
					}else {
						alert("保存失败！");
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
			var obj1 = YAHOO.util.Connect.asyncRequest('POST',form.action, callback,postData);
	}
	/**modified by lisj 2015-1-30 需求编号【HS141110017】保理业务改造 end**/
	
	function setManagerId(data){
		IqpActrecpoMana.manager_id._setValue(data.actorno._getValue());
		IqpActrecpoMana.manager_id_displayname._setValue(data.actorname._getValue());
		IqpActrecpoMana.manager_br_id._setValue(data.orgid._getValue());
		IqpActrecpoMana.manager_br_id_displayname._setValue(data.orgid_displayname._getValue());
		//IqpActrecpoMana.manager_br_id_displayname._obj._renderReadonly(true);
		doOrgCheck();
	}

	function doOrgCheck(){
		var handleSuccess = function(o) {
			if (o.responseText !== undefined) {
				try {
					var jsonstr = eval("(" + o.responseText + ")");
				} catch (e) {
					alert("Parse jsonstr define error!" + e.message);
					return;
				}
				var flag = jsonstr.flag;
				if("one" == flag){//客户经理只属于一个机构
					IqpActrecpoMana.manager_br_id._setValue(jsonstr.org);
					IqpActrecpoMana.manager_br_id_displayname._setValue(jsonstr.orgName);
				}else if("more" == flag){//客户经理属于多个机构
					IqpActrecpoMana.manager_br_id._setValue("");
					IqpActrecpoMana.manager_br_id_displayname._setValue("");
					IqpActrecpoMana.manager_br_id_displayname._obj._renderReadonly(false);
					var manager_id = IqpActrecpoMana.manager_id._getValue();
					IqpActrecpoMana.manager_br_id_displayname._obj.config.url="<emp:url action='querySOrgPop.do'/>&restrictUsed=false&manager_id="+manager_id;
				}else if("yteam"==flag){
					IqpActrecpoMana.manager_br_id._setValue("");
					IqpActrecpoMana.manager_br_id_displayname._setValue("");
					IqpActrecpoMana.manager_br_id_displayname._obj._renderReadonly(false);
					IqpActrecpoMana.manager_br_id_displayname._obj.config.url="<emp:url action='querySOrgPop.do'/>&restrictUsed=false&team=team";
				}
			}
		};
		var handleFailure = function(o) {
		};
		var callback = {
			success :handleSuccess,
			failure :handleFailure
		};
		var manager_id = IqpActrecpoMana.manager_id._getValue();
		var url = '<emp:url action="CheckSUserRecord.do"/>?manager_id='+manager_id;
		url = EMPTools.encodeURI(url);
 		var obj1 = YAHOO.util.Connect.asyncRequest('POST',url,callback);
	}
	
	//返回主管机构
	function getOrganName2(data){
		IqpActrecpoMana.manager_br_id._setValue(data.organno._getValue());
		IqpActrecpoMana.manager_br_id_displayname._setValue(data.organname._getValue());
	}
	function doUp(){
	}
	//选择客户POP框返回方法
	function returnCus(data){
		IqpActrecpoMana.cus_id._setValue(data.cus_id._getValue());
		IqpActrecpoMana.cus_id_displayname._setValue(data.cus_name._getValue());
	}
	
	/**modified by lisj 2015-1-30 需求编号【HS141110017】保理业务改造 begin**/
	//回款保证金明细记录控制（宏定义）
	rowIndex = 0;
	//增加一条记录
	function doAddIqpBailaccDetail(){
		var recordCount = IqpBailaccDetailList._obj.recordCount;//取总记录数
		IqpBailaccDetailList._obj._addRow();
		IqpBailaccDetailList._obj.recordCount +=1; 	//增加总记录数
		var recordCount = IqpBailaccDetailList._obj.recordCount;//取总记录数
		IqpBailaccDetailList._obj.data[recordCount-1].optType._setValue("add");//判断操作方式
		var poNo = IqpActrecpoMana.po_no._getValue();
		IqpBailaccDetailList._obj.data[recordCount-1].po_no._setValue(poNo);//设置池编号
		var row = recordCount-1;
		var id1 = row + '_view1';//每一个按钮id都是固定的
		var id = id1;
		IqpBailaccDetailList._obj.data[recordCount-1].bail_acc_no._obj.addOneButton(id,'获取',getAccountInfo);
		var id2 = row + '_view2';//每一个按钮id都是固定的
		var id = id2;
		IqpBailaccDetailList._obj.data[recordCount-1].cus_id._obj.addOneButton(id,'选择',queryAllCusId);
	}
	
	function queryAllCusId(){
		var id = this.id;
		rowIndex = parseInt(id.split('_')[0]);
		var url = '<emp:url action="queryAllCusPop.do"/>&returnMethod=returnBuyerId';
		url = EMPTools.encodeURI(url);
		var param = 'height=700, width=1200, top=80, left=80, toolbar=no, menubar=no, scrollbars=yes, resizable=no, location=no, status=no';
		window.open(url,'newWindow',param);
	}

	//返回买方客户号及买方客户名称
	function returnBuyerId(data){
		rowIndexStr=rowIndex;
		IqpBailaccDetailList._obj.data[rowIndexStr].cus_id._setValue(data.cus_id._getValue());
		IqpBailaccDetailList._obj.data[rowIndexStr].cus_id_displayname._setValue(data.cus_name._getValue());
	}

	//联机获取付息账号	
	function getAccountInfo(){
		var id = this.id;
		rowIndex = parseInt(id.split('_')[0]);
		var acctNo = IqpBailaccDetailList._obj.data[rowIndex].bail_acc_no._getValue();
		/* add by wangj 2015-5-20  需求编号【HS141110017】保理业务改造 begin*/
		var poType='<%=poType%>';
        if(acctNo == null || acctNo == ""){
        	if("1"==poType){
            	alert("请先输入保证金账号信息！");
            }else if("2"==poType){
            	alert("请先输入待处理保理资金账号信息！");
            }
            return;
        }
        if("1"==poType){
        	acct_no_ori = acctNo;
        	var handleSuccess = function(o){
    			if(o.responseText !== undefined) {
    				try {
    					var jsonstr = eval("("+o.responseText+")");
    				} catch(e) {
    					alert("Parse jsonstr1 define error!" + e.message);
    					return;
    				}
    				var flag = jsonstr.flag;
    				var retMsg = jsonstr.retMsg;
    				if(flag == "success"){
    					var GUARANTEE_ACCT_NO = jsonstr.BODY.GUARANTEE_ACCT_NO;
    					var GUARANTEE_ACCT_NAME = jsonstr.BODY.GUARANTEE_ACCT_NAME;
    					var CCY = jsonstr.BODY.CCY;
    					var AMT = jsonstr.BODY.AMT;
    					var GUARANTEE_TYPE = jsonstr.BODY.GUARANTEE_TYPE;
    					var INT_RATE = jsonstr.BODY.INT_RATE;
    					var INTER_FLT_RATE = jsonstr.BODY.INTER_FLT_RATE;
    					var TERM = jsonstr.BODY.TERM;
    					var OPEN_ACCT_BRANCH_ID = jsonstr.BODY.OPEN_ACCT_BRANCH_ID;

    					IqpBailaccDetailList._obj.data[rowIndex].bail_acc_name._setValue(GUARANTEE_ACCT_NAME);				
    					IqpBailaccDetailList._obj.data[rowIndex].bail_acc_amt._setValue(AMT);
    					IqpBailaccDetailList._obj.data[rowIndex].cur_type._setValue(CCY);
    					alert("账户户名：【"+GUARANTEE_ACCT_NAME+"】\n 账户余额："+AMT);
    				}else {
    					alert(retMsg); 
    					IqpBailaccDetailList._obj.data[rowIndex].bail_acc_no._setValue("");
    					IqpBailaccDetailList._obj.data[rowIndex].bail_acc_name._setValue("");
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
    		var url = '<emp:url action="clientTrade4Esb.do"/>?bail_acct_no='+acctNo+'&service_code=11003000007&sence_code=16';	
    		url = EMPTools.encodeURI(url);
    		var obj1 = YAHOO.util.Connect.asyncRequest('POST',url, callback,null)
        }else{
        	acct_no_ori = acctNo;
			var handleSuccess = function(o){
				if(o.responseText !== undefined) {
					try {
						var jsonstr = eval("("+o.responseText+")");
					} catch(e) {
						alert("Parse jsonstr1 define error!" + e.message);
						return;
					}
					var flag = jsonstr.flag;
					var retMsg = jsonstr.mes;
					var ACCT_NO = jsonstr.BODY.ACCT_NO;
					var ACCT_NAME = jsonstr.BODY.ACCT_NAME;
					var ACCT_TYPE = jsonstr.BODY.ACCT_TYPE;
					var BALANCE = jsonstr.BODY.BALANCE;
					var CCY=jsonstr.BODY.CCY;//增加币种
					if(flag == "success"){
						IqpBailaccDetailList._obj.data[rowIndex].bail_acc_name._setValue(ACCT_NAME);				
    					IqpBailaccDetailList._obj.data[rowIndex].bail_acc_amt._setValue(BALANCE);
    					IqpBailaccDetailList._obj.data[rowIndex].cur_type._setValue(CCY);
    					alert("账户户名：【"+ACCT_NAME+"】\n 账户余额："+Math.abs(parseFloat(BALANCE)));
					}else {
						alert(retMsg); 
						IqpBailaccDetailList._obj.data[rowIndex].bail_acc_no._setValue("");
    					IqpBailaccDetailList._obj.data[rowIndex].bail_acc_name._setValue("");
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
			var url = '<emp:url action="getIqpCusAcctForEsb.do"/>?acct_no='+acctNo;	
			url = EMPTools.encodeURI(url);
			var obj1 = YAHOO.util.Connect.asyncRequest('POST',url, callback,null)
        }
		/* add by wangj 2015-5-20  需求编号【HS141110017】保理业务改造 end*/
	};

	//删除一条记录
	function doDelIqpBailaccDetail(){
		var dataRow =  IqpBailaccDetailList._obj.getSelectedData()[0];
		if (dataRow != undefined) {
			if(confirm("是否确认要删除？")){
				var optType = dataRow.optType._getValue() ;
				dataRow.bail_acc_no._obj._renderHidden(true) ;
				dataRow.bail_acc_name._obj._renderHidden(true) ;
				dataRow.bail_acc_amt._obj._renderHidden(true) ;
				dataRow.cus_id._obj._renderHidden(true) ;
				dataRow.cus_id_displayname._obj._renderHidden(true) ;
				dataRow.displayid._obj._renderHidden(true) ;
				if(optType == 'add'){
					dataRow.optType._setValue("none") ;
				}else if(optType == 'del'){
					dataRow.optType._setValue("del") ;
				}else if(optType == ''){
					dataRow.optType._setValue("del");
				}
				var bail_acc_no = dataRow.bail_acc_no._getValue() ;
				if(bail_acc_no!=""){   
					rowIndexStr=rowIndex;
					var recordCount = IqpBailaccDetailList._obj.recordCount;//取总记录数 
					/*  检查有效记录的字段否为空 */
					var count = 0;  
					var form = document.getElementById('submitForm');
			    	if(form){ 
			    		IqpBailaccDetailList._toForm(form);
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
									alert("删除成功！");
									window.location.reload();
								}else{
									alert("删除异常！"); 
								}
							}
						};

						var url = '<emp:url action="addIqpActrecpoManaRecord.do"/>?del4BailAccNoFlag=1';
						url = EMPTools.encodeURI(url);
						var handleFailure = function(o){
							alert("异步请求出错！");	
						};
						var callback = {
							success:handleSuccess,
							failure:handleFailure
						};
						var postData = YAHOO.util.Connect.setForm(form);
						var obj1 = YAHOO.util.Connect.asyncRequest('POST', url,callback, postData);
			    	}
				}
			}
		} else {
			alert('请先选择一条记录！');
		}
	}
	/**modified by lisj 2015-1-30 需求编号【HS141110017】保理业务改造  end**/
</script>
</head>
<body class="page_content" onload="doOnLoad()">
	
	<emp:form id="submitForm" action="addIqpActrecpoManaRecord.do" method="POST">
			<emp:gridLayout id="IqpActrecpoManaGroup" title="基本信息" maxColumn="2">
			<emp:select id="IqpActrecpoMana.po_type" label="池类别"  required="true"  hidden="false" readonly="true" dictname="STD_ACTRECPO_TYPE"/>
			<emp:text id="IqpActrecpoMana.image_guaranty_no" label="影像押品编号" maxlength="40" required="true" hidden="false" readonly="true"/>
			<emp:pop id="IqpActrecpoMana.cus_id" label="客户码" required="true"  url="queryAllCusPop.do?restrictUsed=false&cusTypCondition=cus_status='20' and BELG_LINE IN('BL100','BL200')&returnMethod=returnCus"/>
			<emp:text id="IqpActrecpoMana.cus_id_displayname" label="客户名称" required="true" readonly="true" />
			<%if(poType.equals("2")){ %>
			<emp:select id="IqpActrecpoMana.factor_mode" label="保理方式" dictname = "STD_FACTORING_MODE"  required="true" hidden="false"/>
			<emp:select id="IqpActrecpoMana.is_rgt_res" label="是否有追索权"  dictname = "STD_IS_RGT_RES"  required="true" hidden="false"/>
			<!-- add by lisj 2014-12-12 应收账款方式BUG修复，于2014-12-18上线-->
			<%}else{%>
			<emp:select id="IqpActrecpoMana.factor_mode" label="应收账款方式" dictname = "STD_FACTORING_MODE"  required="true" hidden="false"/>
			<%} %>
			<emp:text id="IqpActrecpoMana.invc_quant" label="在池发票数量" maxlength="16" required="false" hidden="false" readonly="true"/>
			<emp:text id="IqpActrecpoMana.invc_amt" label="在池发票总金额" maxlength="18" required="false" readonly="true" dataType="Currency" cssElementClass="emp_currency_text_readonly"/>
			<emp:text id="IqpActrecpoMana.crd_rgtchg_amt" label="债权总金额" maxlength="18" required="false" readonly="true" dataType="Currency" cssElementClass="emp_currency_text_readonly"/>
			<emp:text id="IqpActrecpoMana.pledge_rate" label="质押率" maxlength="10" dataType="Percent" required="true" />
			<emp:text id="IqpActrecpoMana.period_grace" label="宽限期" maxlength="5" required="true" />
			<emp:textarea id="IqpActrecpoMana.memo" label="备注" maxlength="250" required="false" colSpan="2"/>
			
			<emp:pop id="IqpActrecpoMana.manager_id_displayname" label="责任人" url="getValueQuerySUserPopListOp.do?restrictUsed=false" returnMethod="setManagerId" required="true"/>
			<emp:pop id="IqpActrecpoMana.manager_br_id_displayname" label="责任机构" required="true" url="querySOrgPop.do?restrictUsed=false" returnMethod="getOrganName2" />
			
			<emp:pop id="IqpActrecpoMana.manager_id" label="责任人" url="getValueQuerySUserPopListOp.do?restrictUsed=false" returnMethod="setManagerId" hidden="true" required="true"/>
			<emp:text id="IqpActrecpoMana.manager_br_id" label="责任机构" maxlength="20" required="false" readonly="true" hidden="true"/>
			
			<emp:text id="IqpActrecpoMana.input_id" label="登记人" maxlength="30" required="true" readonly="true" hidden="true"/>
			<emp:text id="IqpActrecpoMana.input_br_id" label="登记机构" maxlength="30" required="true" readonly="true" hidden="true"/>
			<emp:text id="IqpActrecpoMana.input_id_displayname" label="登记人" required="true" readonly="true"/>
			<emp:text id="IqpActrecpoMana.input_br_id_displayname" label="登记机构" required="true" readonly="true"/>
			<emp:date id="IqpActrecpoMana.input_date" label="登记日期"  required="true" readonly="true"/>			
			
			<emp:text id="IqpActrecpoMana.po_mode" label="池模式" maxlength="5" required="false" hidden="true"/>
			
			<emp:text id="IqpActrecpoMana.status" label="状态" maxlength="5" required="false" hidden="true"/>
			<emp:text id="IqpActrecpoMana.po_no" label="池编号" maxlength="30" required="false" hidden="true"/>				
			</emp:gridLayout>
			<!-- add by lisj 2015-1-30 需求编号【HS141110017】保理业务改造 begin -->
			<div id="bailAccDetail">
		 	<!-- add by wangj 2015-5-20  需求编号【HS141110017】保理业务改造   begin -->
		    <div class='emp_gridlayout_title' id="commen">
		    <%
		    	String str="回款保证金";
				if(poType.equals("2")){
					str="待处理保理资金";
				} 
			%> <%=str%>明细&nbsp;
		   	</div>
		    <!-- 当池类型为【应收账款池】时，回款保证金只需一个账户 -->
		   <%-- <%if(!type.equals("view") && poType.equals("2")){ %> --%> 
		    <!-- 当池类型为【保理池】时，待处理保理资金账号 也只需一个账户-->
			 <!-- <div align="left">
				<emp:button id="addIqpBailaccDetail" label="新增" />
				<emp:button id="delIqpBailaccDetail" label="删除" />
			</div>-->
			<%--<%} %>--%> 
			<!-- add by wangj 2015-5-20  需求编号【HS141110017】保理业务改造 end -->
				<emp:table icollName="IqpBailaccDetailList" pageMode="false" editable="true" url="">
				    <emp:text id="optType" label="操作方式" hidden="true" />
					<emp:text id="po_no" label="池编号" readonly="true" hidden="true"/>
					<!-- add by wangj 2015-5-20  需求编号【HS141110017】保理业务改造 begin -->
					<emp:text id="bail_acc_no" label='<%=str+"账号" %>'/>
					<emp:text id="bail_acc_name" label='<%=str+"账户名"%>' readonly="true"/>
					<emp:text id="bail_acc_amt" label='<%=str+"额"%>' readonly="true" dataType="Currency" cssElementClass="emp_currency_text_readonly" hidden="true" required="false"/>
					<!-- add by wangj 2015-5-20  需求编号【HS141110017】保理业务改造 end -->
					<emp:text id="cus_id" label="买方客户号" readonly="true"/>
					<emp:text id="cus_id_displayname" label="买方客户名称" readonly="true"/>
					<emp:text id="cur_type" label="币种" readonly="true" hidden="true"/>
				    <emp:text id="pk" label="物理主键" hidden="true"/>
				</emp:table>
			</div>
			<!-- add by lisj 2015-1-30 需求编号【HS141110017】保理业务改造 end -->
			<div align="center">
				<br>
				<%if(!type.equals("view")){ %>
				<emp:button id="add" label="保存" op="add"/>
				<% } %>
				<%if(!ggcbl.equals("y")){ %>
				<emp:button id="return" label="返回"/>
				<%} %>
			</div>
	</emp:form>
	
</body>
</html>
</emp:page>

