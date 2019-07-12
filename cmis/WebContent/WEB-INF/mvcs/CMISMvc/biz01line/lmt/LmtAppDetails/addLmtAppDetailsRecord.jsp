<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>

<%@page import="com.ecc.emp.core.EMPConstance"%>
<%@page import="com.ecc.emp.core.Context"%>
<emp:page>
<html>
<%
	Context context = (Context)request.getAttribute(EMPConstance.ATTR_CONTEXT);
	String update_flag = "01";
	String belg_line = (String)context.getDataValue("BelgLine");
	//String lmt_type = request.getParameter("lmt_type");
	if(context.containsKey("LmtAppDetails.update_flag")){
		update_flag = context.getDataValue("LmtAppDetails.update_flag").toString();
	}
	
%>
<head>
<title>新增页面</title>
<jsp:include page="/include.jsp" flush="true"/>

<script type="text/javascript">
	/*--user code begin--*/
	function doload(){
		var form = document.getElementById("submitForm");
		form.action = "${context.action}";
		
		//清除担保方式中的全额保证金、准全额保证金 
		var lrisk_type = LmtAppDetails.lrisk_type._getValue();
		var options = LmtAppDetails.guar_type._obj.element.options;
		for ( var i = options.length - 1; i >= 0; i--) {
			if(options[i].value == "500" || options[i].value == "510"){
				options.remove(i);
			}else if("10"==lrisk_type && (options[i].value != "210" && options[i].value != "220" && options[i].value != "" )){  //低风险
				options.remove(i);
			}else if(("20"==lrisk_type) && (options[i].value == "210" || options[i].value == "220" )){
				options.remove(i);
			}
		}
		var belgLine = '<%=belg_line%>';
		var sub_type = '${context.sub_type}';
		if("01"==sub_type){//一般授信
			LmtAppDetails.core_corp_cus_id._obj._renderHidden(true);
			LmtAppDetails.core_corp_cus_id_displayname._obj._renderHidden(true);
			LmtAppDetails.core_corp_duty._obj._renderHidden(true);
			if(belgLine!=null&&belgLine=='BL300'){
				LmtAppDetails.prd_id._obj.config.url='<emp:url action="showPrdCheckTreeDetails.do"/>&bizline=BL300';
				LmtAppDetails.limit_name_displayname._obj.config.url='<emp:url action="showPrdTreeDetails.do"/>&bizline=BL300';
			}else{
				LmtAppDetails.prd_id._obj.config.url='<emp:url action="showPrdCheckTreeDetails.do"/>&bizline=BL100,BL200';
				LmtAppDetails.limit_name_displayname._obj.config.url='<emp:url action="showPrdTreeDetails.do"/>&bizline=BL100,BL200';
			}
			
		}else{
			LmtAppDetails.core_corp_cus_id._obj._renderHidden(false); 
			LmtAppDetails.core_corp_cus_id_displayname._obj._renderHidden(false);

			LmtAppDetails.core_corp_cus_id._obj._renderRequired(true);
			LmtAppDetails.core_corp_cus_id_displayname._obj._renderRequired(true);
			
			LmtAppDetails.core_corp_duty._obj._renderHidden(false);
			LmtAppDetails.prd_id._obj.config.url='<emp:url action="showPrdCheckTreeDetails.do"/>&bizline=BL500'; 
			LmtAppDetails.limit_name_displayname._obj.config.url='<emp:url action="showPrdTreeDetails.do"/>&bizline=BL500';
		}       

		//根据变更类型控制是否调整授信期限
		var update_flag = LmtAppDetails.update_flag._getValue();
		if("02" == update_flag){  //变更
			LmtAppDetails.is_adj_term._obj._renderHidden(false);

			//动态加载是否调整授信期限
			adjTerm(LmtAppDetails.is_adj_term._getValue());
		}else{  
			LmtAppDetails.is_adj_term._obj._renderHidden(true);
		}

		//根据条线设置绿色产业类型
		if("BL300"==LmtAppDetails.belg_line._getValue()){
			LmtAppDetails.green_indus_displayname._setValue("否");
			LmtAppDetails.green_indus._setValue("2");
		}
		var green_indus = LmtAppDetails.green_indus._getValue();
		if(green_indus != '2'&&green_indus!=''){
			LmtAppDetails.green_pro_type._obj._renderHidden(false);
			LmtAppDetails.green_pro_type._obj._renderRequired(true);
		}else{
			LmtAppDetails.green_pro_type._setValue('');
			LmtAppDetails.green_pro_type._obj._renderHidden(true);
			LmtAppDetails.green_pro_type._obj._renderRequired(false);
		} 
	}

	function doSave(){
		if(!LmtAppDetails._checkAll()){
			return false;
		}else{
			var term = LmtAppDetails.term._getValue();
			if(term < 0 || term == 0){
				alert("期限必须大于'0'！");
				return false;
			}else{
				/**add by lisj 2014-12-2 需求:XD140925064,生活贷需求开发  begin**/
				var prd_id  = LmtAppDetails.limit_name._getValue();
				var term_type = LmtAppDetails.term_type._getValue();
				var term = LmtAppDetails.term._getValue();
				var crd_amt = LmtAppDetails.crd_amt._getValue();
				if(prd_id == '100080' || prd_id == '100081' || prd_id == '100082' || prd_id == '100083'){		
					//校验生活贷期限
					if(term_type =="001" && parseInt(term) > 3){
						alert("生活贷授信期限最长不能超过3年，请重新输入！");
						return;
					}else if(term_type =="002" && parseInt(term) > 36){
						alert("生活贷授信期限最长不能超过36个月，请重新输入！");
						return;
					}
					//校验生活贷款金额
					if(parseInt(crd_amt)< 10000 ||parseInt(crd_amt)> 500000){
						alert("生活贷授信额度范围只能为1-50万元，请重新输入！");
						return;
					}
				}
				/**add by lisj 2014-12-2 需求:XD140925064,生活贷需求开发  end**/
				var form = document.getElementById("submitForm");
				LmtAppDetails._toForm(form);
				var handleSuccess = function(o){
					if(o.responseText !== undefined) {
						try {
							var jsonstr = eval("("+o.responseText+")");
						} catch(e) {
							alert("Parse jsonstr define error!" + e.message);
							return;
						}
						var flag = jsonstr.flag;
						if(flag == "success"){
							alert("操作成功！");
							//window.parent.location.reload();
							var url = "";
							
							var belgLine = '<%=belg_line%>';
							var limit_code = jsonstr.limit_code;
							if(belgLine!=null&&belgLine=='BL300'){
								//如果条线为个人返回个人详情页面
								//url = '<emp:url action="queryLmtAppIndivDetailsList.do"/>&serno=${context.serno}&cus_id=${context.cus_id}&app_type=${context.app_type}&op=${context.op}&subButtonId=${context.subButtonId}';
								//var url = '<emp:url action="getLmtAppDetailsUpdatePage.do"/>?limit_code='+limit_code+'&sub_type=${context.sub_type}&serno=${context.serno}&app_type=${context.app_type}&cus_id=${context.cus_id}&op=update&ogrMenuId=${context.menuId}&menuId=LmtAppDetails&grtOp=update&lrisk_type=${context.lrisk_type}&lmt_type='+lmt_type;
								var url = '<emp:url action="getLmtAppDetailsUpdatePage.do"/>?op=update&limit_code='+limit_code+'&sub_type=${context.sub_type}&serno=${context.serno}&app_type=${context.app_type}&cus_id=${context.cus_id}&op=update&ogrMenuId=${context.menuId}&menuIdTab=LmtAppDetails&grtOp=update&lrisk_type=${context.lrisk_type}';
								url = EMPTools.encodeURI(url);
								window.location = url;
							}else{
								//返回授信分项列表页面
								//url = '<emp:url action="queryLmtAppDetailsList.do"/>&serno=${context.serno}&cus_id=${context.cus_id}&app_type=${context.app_type}&op=${context.op}&subButtonId=${context.subButtonId}';
								//跳转到修改页面-录入担保合同跟担保品
								var limit_code = jsonstr.limit_code;
								//var url = '<emp:url action="getLmtAppDetailsUpdatePage.do"/>?limit_code='+limit_code+'&sub_type=${context.sub_type}&serno=${context.serno}&app_type=${context.app_type}&cus_id=${context.cus_id}&op=update&ogrMenuId=${context.menuId}&menuId=LmtAppDetails&grtOp=update&lrisk_type=${context.lrisk_type}&lmt_type='+lmt_type;
								var url = '<emp:url action="getLmtAppDetailsUpdatePage.do"/>?op=update&limit_code='+limit_code+'&sub_type=${context.sub_type}&serno=${context.serno}&app_type=${context.app_type}&cus_id=${context.cus_id}&op=update&ogrMenuId=${context.menuId}&menuIdTab=LmtAppDetails&grtOp=update&lrisk_type=${context.lrisk_type}';
								url = EMPTools.encodeURI(url);
								window.location = url;
							}
						}else if(flag=="fail_app"){
							alert("申请信息未保存不能新增分项信息！"); 
						}else if(flag=="fail"){
							alert(jsonstr.msg);
						}else{
							alert("保存分项抛出未知异常，请联系管理员！");
						}
					}
				}
			}
		}
		var handleFailure = function(o){
			alert("异步请求出错！");	
		};
		var callback = {
			success:handleSuccess,
			failure:handleFailure
		};
		var postData = YAHOO.util.Connect.setForm(form);	
		var obj1 = YAHOO.util.Connect.asyncRequest('POST',form.action, callback,postData);
	};

	//设置产品返回 
	function setProds(data){
		LmtAppDetails.prd_id._setValue(data[0]);
		LmtAppDetails.prd_id_displayname._setValue(data[1]);
		checkPrdConName('1');
	}

	//额度名称返回 
	function setLmtName(data){
		/**add by lisj 2014-12-2 需求:XD140925064,生活贷需求开发  begin**/
		var lrisk_type = LmtAppDetails.lrisk_type._getValue();//低风险类型
		if(lrisk_type == "10" && (data.id == '100080' || data.id == '100081' || data.id == '100082' || data.id == '100083')){
			alert("【生活贷】额度品种不属于低风险业务,请重新选择！");
			return;
		}else{
			LmtAppDetails.limit_name._setValue(data.id);
			LmtAppDetails.limit_name_displayname._setValue(data.label);
			/**add by lisj 2015-5-18 需求编号：XD150123005 小微自助循环贷款改造 begin**/
			/**added by wangj 2015/05/07 需求编号:XD141222087,法人账户透支需求变更  begin**/
			if(data.id == '100051' || data.id == '100088'){//如果是法人账户透支。默认授信类型为循环额度
				LmtAppDetails.limit_type._setValue('01');
				LmtAppDetails.limit_type._obj._renderReadonly(true);
				if(data.id == '100051'||data.id == '100088'){
					LmtAppDetails.prd_id._setValue(data.id);
					LmtAppDetails.prd_id_displayname._setValue(data.label);
					LmtAppDetails.prd_id._obj._renderReadonly(true);
					LmtAppDetails.prd_id_displayname._obj._renderReadonly(true);
				}
			}else{
				LmtAppDetails.limit_type._setValue('');
				LmtAppDetails.limit_type._obj._renderReadonly(false);
				LmtAppDetails.prd_id._setValue('');
				LmtAppDetails.prd_id_displayname._setValue('');
				LmtAppDetails.prd_id._obj._renderReadonly(false);
				LmtAppDetails.prd_id_displayname._obj._renderReadonly(false);
			}
			/**added by wangj 2015/05/07 需求编号:XD141222087,法人账户透支需求变更  end**/
			/**add by lisj 2015-5-18 需求编号：XD150123005 小微自助循环贷款改造 end**/
			checkPrdConName('2');
		}
		/**add by lisj 2014-12-2 需求:XD140925064,生活贷需求开发  end**/
		
		//增加泉水贷XD140715023  2014-09-01 zhaozq  start
		if(data.id=='100076'){//如果是泉水贷。默认授信期限为6个月，增加授信额度测算
			LmtAppDetails.term_type._setValue('002');
			LmtAppDetails.term_type._obj._renderReadonly(true);
			LmtAppDetails.term._setValue(6);
			LmtAppDetails.term._obj._renderReadonly(true);
			LmtAppDetails.crd_amt._setValue('0');
			LmtAppDetails.crd_amt._obj._renderReadonly(true);
			
			LmtAppDetails.crd_amt._obj.addOneButton('cal','测算',calCrdAmt);
			document.getElementById("cal").disabled="disabled";
		}else{
			LmtAppDetails.term_type._setValue('');
			LmtAppDetails.term_type._obj._renderReadonly(false);
			LmtAppDetails.term._setValue('');
			LmtAppDetails.term._obj._renderReadonly(false);
			LmtAppDetails.crd_amt._setValue('');
			LmtAppDetails.crd_amt._obj._renderReadonly(false);

			LmtAppDetails.crd_amt._obj.removeOneButton('cal');
		}
		//增加泉水贷XD140715023  2014-09-01 zhaozq  end

		/**add by lisj 2014-12-2 需求:XD140925064,生活贷需求开发  begin**/
		if(data.id == '100080' || data.id == '100081' || data.id == '100082' || data.id == '100083'){
			var options = LmtAppDetails.term_type._obj.element.options;
			LmtAppDetails.guar_type._setValue('400');
			LmtAppDetails.guar_type._obj._renderReadonly(true);
			//去除申请字典项为"日"
			for(var i=options.length-1;i>=0;i--){
				if(options[i].value == "003"){
					options.remove(i);
				}
			}
		}
		/**add by lisj 2014-12-2 需求:XD140925064,生活贷需求开发  end**/
		/**add by lisj 2015-5-18 需求编号：XD150123005 小微自助循环贷款改造 begin**/
		if(data.id == '100088'){
			var options = LmtAppDetails.term_type._obj.element.options;
			//去除申请字典项为"日"
			for(var i=options.length-1;i>=0;i--){
				if(options[i].value == "003"){
					options.remove(i);
				}
			}
		}
		/**add by lisj 2015-5-18 需求编号：XD150123005 小微自助循环贷款改造 end**/
	}
	//增加泉水贷XD140715023  2014-09-01 zhaozq  start
	function calCrdAmt(){
		var serno = LmtAppDetails.serno._getValue();
		var org_limit_code = LmtAppDetails.org_limit_code._getValue();
		var url = '<emp:url action="getLmtQsdInfoUpdatePage.do"/>&returnMethod=setCrdAmt&serno='+serno+'&org_limit_code='+org_limit_code+'&cus_id=${context.cus_id}';
		url = EMPTools.encodeURI(url);
		var param = 'height=700, width=1200, top=80, left=80, toolbar=no, menubar=no, scrollbars=yes, resizable=no, location=no, status=no';
		window.open(url,'newWindow',param);
	}

	function setCrdAmt(){
		LmtAppDetails.crd_amt._setValue(data);
	}
	//增加泉水贷XD140715023  2014-09-01 zhaozq end
	
	//校验适用产品是否包含额度名称
	function checkPrdConName(flag){
		var lmtName = LmtAppDetails.limit_name._getValue();
		var prdId = LmtAppDetails.prd_id._getValue();
		if(lmtName!=null&&lmtName!=''&&prdId!=null&&prdId!=''){
			if(prdId.indexOf(lmtName)<0){
				alert("适用产品必须包含额度品种名称！");
				if(flag=='1'){
					LmtAppDetails.prd_id._setValue('');
					LmtAppDetails.prd_id_displayname._setValue('');
				}else{
					LmtAppDetails.limit_name._setValue('');
					LmtAppDetails.limit_name_displayname._setValue('');
				}
			}
		}
	}

	//返回方法
	function doReturn(){
		var url = "";
		var belgLine = '<%=belg_line%>';
		
		if(belgLine!=null&&belgLine=='BL300'){
			//如果条线为个人返回个人详情页面
			//url = '<emp:url action="queryLmtAppIndivDetailsList.do"/>&serno=${context.serno}&cus_id=${context.cus_id}&app_type=${context.app_type}&op=${context.op}&subButtonId=${context.subButtonId}&lrisk_type=${context.lrisk_type}&lmt_type='+lmt_type;
			url = '<emp:url action="queryLmtAppIndivDetailsList.do"/>&serno=${context.serno}&cus_id=${context.cus_id}&app_type=${context.app_type}&op=${context.op}&subButtonId=${context.subButtonId}&lrisk_type=${context.lrisk_type}';
		}else{
			//返回授信分项列表页面
			url = '<emp:url action="queryLmtAppDetailsList.do"/>&serno=${context.serno}&cus_id=${context.cus_id}&app_type=${context.app_type}&op=${context.op}&subButtonId=${context.subButtonId}&lrisk_type=${context.lrisk_type}';
		}
		url = EMPTools.encodeURI(url);
		window.location = url;
	}

	//是否调整期限值改变事件
	function adjTerm(_value){
		if("1"==_value){
			LmtAppDetails.term_type._obj._renderHidden(false);
			LmtAppDetails.term._obj._renderHidden(false);
		}else{
			LmtAppDetails.term_type._obj._renderHidden(true);
			LmtAppDetails.term._obj._renderHidden(true);
		}
	}
	/**add by lisj 2015-2-9 需求编号【HS141110017】保理业务改造  begin**/
	//选择核心企业POP框返回方法
	function returnCoreCus(data){
		LmtAppDetails.core_corp_cus_id._setValue(data.cus_id._getValue());
		LmtAppDetails.core_corp_cus_id_displayname._setValue(data.cus_name._getValue());
	}
	/**add by lisj 2015-2-9  需求编号【HS141110017】保理业务改造  end**/
	/**modified by lisj 2015-6-23 需求编号：XD150407025 2015分支机构授权配置 begin**/
	//担保方式修改--不用了这个xjh
	function changeGuarType(){
		var guarType = LmtAppDetails.guar_type._getValue();
		var belgLine = '<%=belg_line%>';
		 
		if(guarType =="100"){
			LmtAppDetails.is_opn_mode._obj._renderHidden(false);
			LmtAppDetails.is_opn_mode._obj._renderRequired(true);
		}else{
			LmtAppDetails.is_opn_mode._obj._renderHidden(true);
			LmtAppDetails.is_opn_mode._obj._renderRequired(false);
		}
	}
	/**modified by lisj 2015-6-23 需求编号：XD150407025 2015分支机构授权配置 end**/
	
	function greenIndusReturn(date){
		LmtAppDetails.green_indus._obj.element.value=date.id;
		LmtAppDetails.green_indus_displayname._obj.element.value=date.label;
		if(date.id != '2'){
			LmtAppDetails.green_pro_type._obj._renderHidden(false);
			LmtAppDetails.green_pro_type._obj._renderRequired(true);
		}else{
			LmtAppDetails.green_pro_type._setValue('');
			LmtAppDetails.green_pro_type._obj._renderRequired(false);
			LmtAppDetails.green_pro_type._obj._renderHidden(true);
		}
	};
	/*--user code end--*/  
	
</script>
</head>
<body class="page_content" onload="doload()">
<emp:tabGroup mainTab="main_tabs" id="main_tab">
	<emp:tab label="授信分项基本信息" id="main_tabs" needFlush="true" initial="true">
	<emp:form id="submitForm" action="addLmtAppDetailsRecord.do" method="POST">  
		
		<emp:gridLayout id="LmtAppDetailsGroup" title="授信分项基本信息" maxColumn="2"> 
			<emp:text id="LmtAppDetails.serno" label="业务编号" maxlength="40" required="false" colSpan="2" readonly="true" defvalue="${context.serno}" cssElementClass="emp_field_text_readonly" hidden="true"/>
			<!-- add by lisj 2015-2-9  需求编号【HS141110017】保理业务改造 begin -->
			<emp:pop id="LmtAppDetails.core_corp_cus_id" label="核心企业客户码 " required="false" url="queryAllCusPop.do?returnMethod=returnCoreCus" hidden="true"/>
			<emp:select id="LmtAppDetails.core_corp_duty" label="核心企业责任" required="false" dictname="STD_ZB_CORP_DUTY" />
			<emp:text id="LmtAppDetails.core_corp_cus_id_displayname" label="核心企业客户名称" required="false" colSpan="2" cssElementClass="emp_field_text_long_readonly" hidden="true"/>
			 <!-- add by lisj 2015-2-9  需求编号【HS141110017】保理业务改造 end -->
			<emp:select id="LmtAppDetails.sub_type" label="分项类别" required="true" dictname="STD_LMT_PROJ_TYPE" readonly="true" defvalue="${context.sub_type}"/>
			<emp:select id="LmtAppDetails.limit_type" label="额度类型" required="true" dictname="STD_ZB_LIMIT_TYPE"/>
			<emp:text id="LmtAppDetails.limit_code" label="授信额度编号" maxlength="32" required="false" readonly="true" hidden="true"/>
			<emp:pop id="LmtAppDetails.limit_name_displayname" label="额度品种名称" url='' required="true" returnMethod="setLmtName"/>
			<emp:text id="LmtAppDetails.limit_name" label="额度品种名称" maxlength="60" required="true" hidden="true"/>
			<emp:pop id="LmtAppDetails.prd_id" label="适用产品编号" url='' returnMethod="setProds" required="true" colSpan="2" cssElementClass="emp_field_text_long"/> 
			<emp:textarea id="LmtAppDetails.prd_id_displayname" label="适用产品名称" required="true"  colSpan="2" cssElementClass="emp_field_textarea_readonly"/>
			<emp:select id="LmtAppDetails.cur_type" label="币种" required="true" dictname="STD_ZX_CUR_TYPE" defvalue="CNY" readonly="true"/>
			<emp:text id="LmtAppDetails.crd_amt" label="授信额度"  required="true" dataType="Currency" maxlength="18"/>
			<emp:select id="LmtAppDetails.guar_type" label="担保方式" required="true" dictname="STD_ZB_ASSURE_MEANS" />
			<!-- modified by FCL 2015-07-23 需求编号：XD150715055 新增个人N+1显示 begin -->
			<% if("BL300".equals(belg_line)){ %>
			<emp:select id="LmtAppDetails.is_opn_mode" label="是否为 N+1或1+N" dictname="STD_ZX_YES_NO" hidden="true" defvalue="2"/>
			<%}else{ %>
			<emp:select id="LmtAppDetails.is_opn_mode" label="是否为1+N模式" dictname="STD_ZX_YES_NO" hidden="true" defvalue="2"/>
			<%} %>
			<!-- modified by lisj 2015-6-23 需求编号：XD150407025 2015分支机构授权配置 end -->
			<emp:select id="LmtAppDetails.is_pre_crd" label="是否预授信" readonly="true" required="true"  dictname="STD_ZX_YES_NO" defvalue="1" />
			<emp:select id="LmtAppDetails.is_adj_term" label="是否调整期限" required="false" dictname="STD_ZX_YES_NO" colSpan="2" onchange="adjTerm(this.value)"/>
			<emp:select id="LmtAppDetails.term_type" label="授信期限类型" required="true" dictname="STD_ZB_TERM_TYPE" />
			<emp:text id="LmtAppDetails.term" label="授信期限" maxlength="5" required="true" dataType="Int" />
			<% if("02".equals(update_flag)){ %>
			<emp:date id="LmtAppDetails.start_date" label="授信起始日" required="false" readonly="true" />
			<emp:date id="LmtAppDetails.end_date" label="授信到期日" required="false" readonly="true" />
			<%} %>
			
			<!-- 绿色产业类型  2013-12-04 唐顺岩-->
			<emp:select id="LmtAppDetails.lmt_type" label="授信类别" dictname="STD_ZX_LMT_PRD" readonly="true" hidden="true"/>
			<emp:pop id="LmtAppDetails.green_indus_displayname" label="绿色产业类型" required="true" url="showDicTree.do?dicTreeTypeId=STD_ZB_GREEN_INDUS" returnMethod="greenIndusReturn" cssElementClass="emp_field_text_long" colSpan="2" />
			<emp:select id="LmtAppDetails.green_pro_type" label="绿色授信项目类型" required="false" dictname="STD_GREEN_PRO_TYPE"  cssElementClass="emp_field_text_long" colSpan="2"/>
			<emp:text id="LmtAppDetails.green_indus" label="绿色产业类型" required="true" hidden="true"/>
			<emp:select id="LmtAppDetails.update_flag" label="修改类型" required="false" defvalue="01" dictname="STD_ZB_APP_TYPE" readonly="true" hidden="true"/>
			
			<emp:text id="LmtAppDetails.ori_crd_amt" label="原有授信金额" maxlength="18" required="false" dataType="Currency" hidden="true"/>
			<emp:text id="LmtAppDetails.froze_amt" label="冻结金额" maxlength="18" required="false" dataType="Currency" hidden="true"/>
			<emp:text id="LmtAppDetails.unfroze_amt" label="解冻金额" maxlength="18" required="false" dataType="Currency" hidden="true"/>
			<emp:text id="LmtAppDetails.belg_line" label="所属条线" maxlength="16" defvalue="${context.BelgLine}" hidden="true"/>
			<emp:text id="LmtAppDetails.cus_id" label="客户码" maxlength="16" defvalue="${context.cus_id}" hidden="true"/>
			<emp:text id="LmtAppDetails.lrisk_type" label="低风险业务类型" maxlength="16" hidden="true" defvalue="${context.lrisk_type}" readonly="true"/>
		</emp:gridLayout>
		
		<div align="center">
			<br>
			<emp:button id="save" label="确定"/>
			<emp:button id="reset" label="重置" />
			<emp:button id="return" label="返回" />
		</div>
	</emp:form>
	</emp:tab>
</emp:tabGroup>
</body>
</html>
</emp:page>

