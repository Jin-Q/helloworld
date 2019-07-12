<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<emp:page>
<html>
<head>
<title>新增页面</title>

<jsp:include page="/include.jsp" flush="true"/>
<script type="text/javascript">

	/*--user code begin--*/
	function doReturn() {
		var url = '<emp:url action="queryCusOrgAppList.do?resourceType=app"/>';
		url = EMPTools.encodeURI(url);
		window.location=url;
	};

	/**
	*	通过组织机构代码查询评估机构信息，若存在则返回信息
	*/
	function checkEvalOrgCode(){
		var extr_eval_org = CusOrgApp.extr_eval_org._getValue();//组织机构代码
		if(extr_eval_org!=null&&extr_eval_org!=""){
			if(!CheckOrganFormat(extr_eval_org)){
				CusOrgApp.extr_eval_org._setValue('');
				return false;
			}else{
				var handleSuccess = function(o){
					var jsonstr = eval("(" + o.responseText + ")");
					var cusList=jsonstr.cusOrgList;
					var cusObj;
					if (cusList != null && cusList.length > 0) {
						cusObj=cusList[0];
						returnCus(cusObj);
					} else {
						alert("记录为空！");
					}
				}
				var handleFailure = function(o){
			        alert("校验组织机构代码失败！");	
				};
				var callback = {
					success:handleSuccess,
					failure:handleFailure
				};
				var url = '<emp:url action="chekCusOrgIsExistByNo.do"/>&extr_eval_org='+extr_eval_org;
				url = EMPTools.encodeURI(url);
				var obj1 = YAHOO.util.Connect.asyncRequest('POST', url,callback);
			}
		}
	};
	//查询返回信息
	function returnCus(cusObj){
		var flag = cusObj.flag;
		if(flag=='exists'){//已经存在该评估机构
			alert("该评估机构已经存在！");
			//客户码
			var cus_id =cusObj.cus_id;
			CusOrgApp.cus_id._setValue(cus_id);
			//评估机构资质等级
	        var extr_eval_quali=cusObj.extr_eval_quali;
	        if (extr_eval_quali != null) {
	        	CusOrgApp.extr_eval_quali._setValue(extr_eval_quali);
	        }
	      	//评估机构地址代码
	        var extr_eval_addr=cusObj.extr_eval_addr;
	        if (extr_eval_addr != null) {
	        	CusOrgApp.extr_eval_addr._setValue(extr_eval_addr);
	        }
	      	//评估机构地址
	        var extr_eval_addr_displayname=cusObj.extr_eval_addr_displayname;
	        if (extr_eval_addr_displayname != null) {
	        	CusOrgApp.extr_eval_addr_displayname._setValue(extr_eval_addr_displayname);
	        }
	        //评估范围
	        var extr_eval_rng=cusObj.extr_eval_rng;
	        if (extr_eval_rng != null) {
	        	CusOrgApp.extr_eval_rng._setValue(extr_eval_rng);
	        }
	        //法定代表人
	        var fic_per = cusObj.fic_per;
	        if (fic_per != null) {
	        	CusOrgApp.fic_per._setValue(fic_per);
	        }
	        //实际经营人
	        var real_oper_per=cusObj.real_oper_per;
	        if (real_oper_per != null) {
	        	CusOrgApp.real_oper_per._setValue(real_oper_per);
	        }
	        //成立日期
	        var founded_date=cusObj.founded_date;
	        if (founded_date != null) {
	        	CusOrgApp.founded_date._setValue(founded_date);
	        }
	      	//注册资金
	        var reg_cap_amt = cusObj.reg_cap_amt;
	        if (reg_cap_amt != null) {
	        	CusOrgApp.reg_cap_amt._setValue(reg_cap_amt);
	        }
	        //联系电话
	        var phone=cusObj.phone;
	        if (phone != null) {
	        	CusOrgApp.phone._setValue(phone);
	        }
	        //房地产价格评估资质证书号
	        var realty_validation_lice_no=cusObj.realty_validation_lice_no;
	        if (realty_validation_lice_no != null) {
	        	CusOrgApp.realty_validation_lice_no._setValue(realty_validation_lice_no);
	        }
	      	//房地产价格评估资质证书名称
	        var realty_validation_lice_name = cusObj.realty_validation_lice_name;
	        if (realty_validation_lice_name != null) {
	        	CusOrgApp.realty_validation_lice_name._setValue(realty_validation_lice_name);
	        }
	        //土地评估中介机构注册证书号
	        var land_extr_eval_reg_lice_no=cusObj.land_extr_eval_reg_lice_no;
	        if (land_extr_eval_reg_lice_no != null) {
	        	CusOrgApp.land_extr_eval_reg_lice_no._setValue(land_extr_eval_reg_lice_no);
	        }
	        //土地评估中介机构注册证书名称
	        var land_extr_eval_reg_lice_name=cusObj.land_extr_eval_reg_lice_name;
	        if (land_extr_eval_reg_lice_name != null) {
	        	CusOrgApp.land_extr_eval_reg_lice_name._setValue(land_extr_eval_reg_lice_name);
	        }
	      	//资产评估资格证书号
	        var cap_eval_quali_lice_no=cusObj.cap_eval_quali_lice_no;
	        if (cap_eval_quali_lice_no != null) {
	        	CusOrgApp.cap_eval_quali_lice_no._setValue(cap_eval_quali_lice_no);
	        }
	        //资产评估资格证书名称
	        var cap_eval_quali_lice_name=cusObj.cap_eval_quali_lice_name;
	        if (cap_eval_quali_lice_name != null) {
	        	CusOrgApp.cap_eval_quali_lice_name._setValue(cap_eval_quali_lice_name);
	        }
	      	//注册房地产估价师
	        var reg_realty_estimater = cusObj.reg_realty_estimater;
	        if (reg_realty_estimater != null) {
	        	CusOrgApp.reg_realty_estimater._setValue(reg_realty_estimater);
	        }
	        //注册土地估价师
	        var reg_land_estimater=cusObj.reg_land_estimater;
	        if (reg_land_estimater != null) {
	        	CusOrgApp.reg_land_estimater._setValue(reg_land_estimater);
	        }
	        //注册资产评估师
	        var cap_realty_estimater=cusObj.cap_realty_estimater;
	        if (cap_realty_estimater != null) {
	        	CusOrgApp.cap_realty_estimater._setValue(cap_realty_estimater);
	        }
	      	//地址
	        var street=cusObj.street;
	        if (street != null) {
	        	CusOrgApp.street._setValue(street);
	        }
	        //客户名称
	        var cus_name=cusObj.cus_name;
	        if (cus_name != null) {
	        	CusOrgApp.cus_name._setValue(cus_name);
	        }
			//置为只读
	        CusOrgApp.cus_name._obj._renderReadonly(true);
	        CusOrgApp.extr_eval_org._obj._renderReadonly(true);
		}else if(flag=='fail'){
			CusOrgApp.extr_eval_org._setValue('');
		}
	}
	
	function getOrgID(data){
		CusOrgApp.manager_br_id._setValue(data.organno._getValue());
		CusOrgApp.manager_br_id_displayname._setValue(data.organname._getValue());
	};

	function setconId(data){
		CusOrgApp.manager_id_displayname._setValue(data.actorname._getValue());
		CusOrgApp.manager_id._setValue(data.actorno._getValue());
		CusOrgApp.manager_br_id._setValue(data.orgid._getValue());
		CusOrgApp.manager_br_id_displayname._setValue(data.orgid_displayname._getValue());
		//CusOrgApp.manager_br_id_displayname._obj._renderReadonly(true);
		doOrgCheck();
	};

	function doOrgCheck(){
		var handleSuccess = function(o) {
			if (o.responseText !== undefined) {
				try {
					var jsonstr = eval("(" + o.responseText + ")");
				} catch (e) {
					alert("Parse jsonstr define error!" + e.message);
					return;
				}
				/** modified by lisj 2015-9-21 需求:XD150918069 丰泽鲤城区域团队业务流程改造  begin**/
				var flag = jsonstr.flag;
				if("one" == flag){//客户经理只属于一个机构
					CusOrgApp.manager_br_id._setValue(jsonstr.org);
					CusOrgApp.manager_br_id_displayname._setValue(jsonstr.orgName);
				}else if("more" == flag || "belg2team" == flag){//客户经理属于多个机构
					CusOrgApp.manager_br_id._setValue("");
					CusOrgApp.manager_br_id_displayname._setValue("");
					CusOrgApp.manager_br_id_displayname._obj._renderReadonly(false);
					var manager_id = CusOrgApp.manager_id._getValue();
					CusOrgApp.manager_br_id_displayname._obj.config.url="<emp:url action='querySOrgPop.do'/>&restrictUsed=false&manager_id="+manager_id;
				}else if("yteam"==flag){
					CusOrgApp.manager_br_id._setValue("");
					CusOrgApp.manager_br_id_displayname._setValue("");
					CusOrgApp.manager_br_id_displayname._obj._renderReadonly(false);
					CusOrgApp.manager_br_id_displayname._obj.config.url="<emp:url action='querySOrgPop.do'/>&restrictUsed=false&team=team";
				}
				/** modified by lisj 2015-9-21 需求:XD150918069 丰泽鲤城区域团队业务流程改造  end**/
			}
		};
		var handleFailure = function(o) {
		};
		var callback = {
			success :handleSuccess,
			failure :handleFailure
		};
		var manager_id = CusOrgApp.manager_id._getValue();
		var url = '<emp:url action="CheckSUserRecord.do"/>?manager_id='+manager_id;
		url = EMPTools.encodeURI(url);
 		var obj1 = YAHOO.util.Connect.asyncRequest('POST',url,callback);
	}
	
    function onReturnRegStateCode(date){
    	CusOrgApp.extr_eval_addr._obj.element.value=date.id;
    	CusOrgApp.extr_eval_addr_displayname._obj.element.value=date.label;
	};
	
	//异步提交申请数据
	function doAddCusOrgApp(){
		var form = document.getElementById("submitForm");
		CusOrgApp._checkAll();
		if(CusOrgApp._checkAll()){
			CusOrgApp._toForm(form);
			var handleSuccess = function(o){
				if(o.responseText !== undefined) {
					try {
						var jsonstr = eval("("+o.responseText+")");
					} catch(e) {
						alert("Parse jsonstr define error!" + e.message);
						return;
					}
					var flag = jsonstr.flag;
					if(flag != "false"){
						alert("保存成功！");
						var url = '<emp:url action="getCusOrgAppUpdatePage.do"/>?serno='+flag;
						url = EMPTools.encodeURI(url);
						window.location = url;
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
		}else {
			return false;
		}
	};
	function selCusId(data){		
		CusOrgApp.cus_id._setValue(data.cus_id._getValue());
		CusOrgApp.cus_name._setValue(data.cus_name._getValue());
		CusOrgApp.extr_eval_org._setValue(data.cert_code._getValue());
		CusOrgApp.extr_eval_addr._setValue(data.extr_eval_addr._getValue());
		CusOrgApp.extr_eval_addr_displayname._setValue(data.extr_eval_addr_displayname._getValue());
		CusOrgApp.street._setValue(data.street._getValue());
		CusOrgApp.fic_per._setValue(data.fic_per._getValue());
		CusOrgApp.real_oper_per._setValue(data.real_oper_per._getValue());
		CusOrgApp.founded_date._setValue(data.com_str_date._getValue());
		CusOrgApp.reg_cap_amt._setValue(data.reg_cap_amt._getValue());
		CusOrgApp.phone._setValue(data.legal_phone._getValue());
		/**add by lisj 2015-10-14 需求编号：XD150918069 丰泽鲤城区域团队业务流程改造 begin**/
		CusOrgApp.manager_id_displayname._setValue(data.cust_mgr_displayname._getValue());
		CusOrgApp.manager_br_id_displayname._setValue(data.main_br_id_displayname._getValue());
		CusOrgApp.manager_id._setValue(data.cust_mgr._getValue());
		CusOrgApp.manager_br_id._setValue(data.main_br_id._getValue());
		doChangeOrgUrl();
		/**add by lisj 2015-10-14 需求编号：XD150918069 丰泽鲤城区域团队业务流程改造 end**/		
		checkCusid(data.cus_id._getValue());
		//checkEvalOrgCode();
	};
	/**add by lisj 2015-10-14 需求编号：XD150918069 丰泽鲤城区域团队业务流程改造 begin**/
	function doChangeOrgUrl(){
		var handleSuccess = function(o) {
   			if (o.responseText !== undefined) {
   				try {
   					var jsonstr = eval("(" + o.responseText + ")");
   				} catch (e) {
   					alert("Parse jsonstr define error!" + e.message);
   					return;
   				}
   				var flag = jsonstr.flag;
				if("belg2team" == flag){//客户经理只属于团队
					var manager_id = CusOrgApp.manager_id._getValue();
					CusOrgApp.manager_br_id_displayname._obj.config.url="<emp:url action='querySOrgPop.do'/>&restrictUsed=false&manager_id="+manager_id;
   				}
   			}
   		};
   		var handleFailure = function(o) {
   		};
   		var callback = {
   			success :handleSuccess,
   			failure :handleFailure
   		};

   		var manager_id = CusOrgApp.manager_id._getValue();
   		var url = '<emp:url action="CheckSUserRecord.do"/>?manager_id='+manager_id;
   		url = EMPTools.encodeURI(url);
   		var obj1 = YAHOO.util.Connect.asyncRequest('POST',url,callback);
	};
	/**add by lisj 2015-10-14 需求编号：XD150918069 丰泽鲤城区域团队业务流程改造 end**/
	function checkCusid(cus_id){
 		var handleSuccess = function(o){
			var jsonstr = eval("(" + o.responseText + ")");
			var flag = jsonstr.flag;
			if(flag == "false" ){
				CusOrgApp.cus_id._setValue("");
				CusOrgApp.cus_name._setValue("");
				CusOrgApp.extr_eval_org._setValue("");
				CusOrgApp.extr_eval_addr._setValue("");
				CusOrgApp.extr_eval_addr_displayname._setValue("");
				CusOrgApp.street._setValue("");
				CusOrgApp.fic_per._setValue("");
				CusOrgApp.real_oper_per._setValue("");
				CusOrgApp.founded_date._setValue("");
				CusOrgApp.reg_cap_amt._setValue("");
				CusOrgApp.phone._setValue("");		
				alert("该客户已经存在于申请中，请重新选择：");	
			}else{
			}
		}
		var handleFailure = function(o){
		        alert("异步回调失败！");	
		};
		var callback = {
				success:handleSuccess,
				failure:handleFailure
		};
		var url = '<emp:url action="checkCusidApplyed.do"/>?cus_id='+cus_id;
		url = EMPTools.encodeURI(url);
		var obj1 = YAHOO.util.Connect.asyncRequest('POST', url,callback);
	};
	/*--user code end--*/
	
</script>
</head>
<body class="page_content">
	
	<emp:form id="submitForm" action="addCusOrgAppRecord.do" method="POST">		
		<emp:gridLayout id="CusOrgAppGroup" title="评估机构认定" maxColumn="2">
			<emp:text id="CusOrgApp.serno" label="申请流水号" maxlength="40" hidden="true" readonly="true" />
			<!-- modified by yangzy 2015/04/16 需求：XD150325024，集中作业扫描岗权限改造 start -->
			<emp:pop id="CusOrgApp.cus_id" label="评估机构客户码"  required="true" colSpan="2" 
			url="queryCusOrgPop.do?cusTypCondition=cust_mgr = '${context.currentUserId}'&returnMethod=selCusId" />
			<!-- modified by yangzy 2015/04/16 需求：XD150325024，集中作业扫描岗权限改造 end -->
			<emp:text id="CusOrgApp.cus_name" label="评估机构名称" maxlength="60" required="true" 
			colSpan="2" readonly="true" cssElementClass="emp_field_text_input2"/>
			<emp:text id="CusOrgApp.extr_eval_org" label="评估机构组织机构代码" maxlength="30" required="false" readonly="true" />
			<emp:text id="CusOrgApp.extr_eval_addr" label="评估机构地址" required="false" hidden="true" />
			<emp:pop id="CusOrgApp.extr_eval_addr_displayname" label="评估机构地址" url="showDicTree.do?dicTreeTypeId=STD_GB_AREA_ALL" 
			returnMethod="onReturnRegStateCode" colSpan="2" required="true" readonly="true" cssElementClass="emp_field_text_input2"/>
			<emp:text id="CusOrgApp.street" label="街道"  required="false" readonly="true" cssElementClass="emp_field_text_input2" colSpan="2"/>
			<emp:text id="CusOrgApp.fic_per" label="法定代表人" maxlength="60" required="false" hidden="true" />
			<emp:text id="CusOrgApp.real_oper_per" label="实际经营人" maxlength="60" required="false" hidden="true"/>
			<emp:date id="CusOrgApp.founded_date" label="成立日期"  required="false"  hidden="true" />
			<emp:text id="CusOrgApp.reg_cap_amt" label="注册资金" maxlength="18" required="false" dataType="Currency" hidden="true"/>
			<emp:text id="CusOrgApp.phone" label="联系电话" maxlength="35" required="false" dataType="Phone" colSpan="2" hidden="true"/>
			<emp:select id="CusOrgApp.extr_eval_quali" label="评估机构资质等级" required="true" dictname="STD_ZB_EXTR_EVAL_QUALI" />
			<emp:text id="CusOrgApp.extr_eval_rng" label="评估范围" maxlength="80" required="true" />
			<emp:select id="CusOrgApp.extr_eval_exp_type" label="评估机构有效期类型" required="true" dictname="STD_ZB_TERM_TYPE" />
			<emp:text id="CusOrgApp.extr_eval_exp_term" label="评估机构有效期期限" maxlength="3" required="true" dataType="Int" />
			<emp:text id="CusOrgApp.realty_validation_lice_no" label="房地产价格评估资质证书号" maxlength="40" required="true" />
			<emp:text id="CusOrgApp.realty_validation_lice_name" label="房地产价格评估资质证书名称" maxlength="60" required="true" />
			<emp:text id="CusOrgApp.land_extr_eval_reg_lice_no" label="土地评估中介机构注册证书号" maxlength="40" required="true" />
			<emp:text id="CusOrgApp.land_extr_eval_reg_lice_name" label="土地评估中介机构注册证书名称" maxlength="60" required="true" />
			<emp:text id="CusOrgApp.cap_eval_quali_lice_no" label="资产评估资格证书号" maxlength="40" required="true" />
			<emp:text id="CusOrgApp.cap_eval_quali_lice_name" label="资产评估资格证书名称" maxlength="60" required="true" />
			<emp:text id="CusOrgApp.reg_realty_estimater" label="注册房地产估价师" maxlength="60" required="true" />
			<emp:text id="CusOrgApp.reg_land_estimater" label="注册土地估价师" maxlength="60" required="true" />
			<emp:text id="CusOrgApp.cap_realty_estimater" label="注册资产评估师" maxlength="60" required="true" />
			<emp:textarea id="CusOrgApp.remarks" label="备注" maxlength="250" required="false" colSpan="2" onblur="this.value = this.value.substring(0, 250)"  />
		</emp:gridLayout>
		<emp:gridLayout id="CusOrgAppGroup" maxColumn="2" title="登记信息">
			<emp:pop id="CusOrgApp.manager_id_displayname" label="责任人" url="getValueQuerySUserPopListOp.do?restrictUsed=false" returnMethod="setconId" required="true"/>
			<emp:pop id="CusOrgApp.manager_br_id_displayname" label="管理机构"  required="true" url="querySOrgPop.do?restrictUsed=false" returnMethod="getOrgID" />
			<emp:text id="CusOrgApp.input_id_displayname" label="登记人"  readonly="true" required="true"  defvalue="$currentUserName" />
			<emp:text id="CusOrgApp.input_br_id_displayname" label="登记机构" readonly="true" required="true"  defvalue="$organName" />
			<emp:text id="CusOrgApp.input_date" label="登记日期" required="true" readonly="true" colSpan="2" defvalue="$OPENDAY" />
			<emp:text id="CusOrgApp.input_id" label="登记人" maxlength="20" readonly="true" required="true"  defvalue="$currentUserId" hidden="true"/>
			<emp:text id="CusOrgApp.input_br_id" label="登记机构"  maxlength="20" readonly="true" required="true"  defvalue="$organNo" hidden="true"/>			
			<emp:text id="CusOrgApp.approve_status" label="审批状态" maxlength="3" required="false"  hidden="true" defvalue="000"/>
			<emp:text id="CusOrgApp.manager_id" label="责任人" hidden="true"/>
			<emp:text id="CusOrgApp.manager_br_id" label="管理机构" hidden="true"/>
		</emp:gridLayout>
		
		<div align="center">
			<br>
			<emp:button id="addCusOrgApp" label="确定" op="add"/>
			<emp:button id="reset" label="重置"/>
			<emp:button id="return" label="返回"/>
		</div>
	</emp:form>
	
</body>
</html>
</emp:page>