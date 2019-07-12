<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp"%>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<script type="text/javascript">
//-----------------------对公客户调用JS----------------------------
	//是否有贷款卡
	function changeCardFlg() {
		if (CusBase.loan_card_flg._obj.element.value == "1") {//有贷款卡
			CusBase.loan_card_id._obj._renderRequired(true);
			CusBase.loan_card_id._obj._renderHidden(false);
			/* modify by wangj 2015-05-20  中征码修改 begin*//*
			CusBase.loan_card_pwd._obj._renderRequired(false);
			CusBase.loan_card_pwd._obj._renderHidden(false);
			CusBase.loan_card_eff_flg._obj._renderRequired(false);//edited by FCL 2014-12-17 贷款卡状态修改为非必输项 
			CusBase.loan_card_eff_flg._obj._renderHidden(false);
			CusBase.loan_card_audit_dt._obj._renderRequired(false);//modified by yangzy 2014/12/15 贷款卡年检到期日非必输
			CusBase.loan_card_audit_dt._obj._renderHidden(false);
			*//* modify by wangj 2015-05-20  中征码修改 end*/
		} else if (CusBase.loan_card_flg._obj.element.value == "2") {
			CusBase.loan_card_id._obj.element.value = "";
			CusBase.loan_card_id._obj._renderRequired(false);
			CusBase.loan_card_id._obj._renderHidden(true);
			/* modify by wangj 2015-05-20  中征码修改 begin*//*
			CusBase.loan_card_pwd._obj.element.value = "";
			CusBase.loan_card_pwd._obj._renderRequired(false);
			CusBase.loan_card_pwd._obj._renderHidden(true);
			CusBase.loan_card_eff_flg._setValue("");
			CusBase.loan_card_eff_flg._obj._renderRequired(false);
			CusBase.loan_card_eff_flg._obj._renderHidden(true);
			CusBase.loan_card_audit_dt._setValue("");
			CusBase.loan_card_audit_dt._obj._renderRequired(false);
			CusBase.loan_card_audit_dt._obj._renderHidden(true);
			*//* modify by wangj 2015-05-20  中征码修改 end*/
		}
		/* modify by yangzy 2015-05-21  中征码修改 begin*/
		var loanCardId= CusBase.loan_card_id._getValue();
		if(loanCardId!=tmpLoanCardId){
			alert("请提供客户征信异议处理申请书");
		}
		/* modify by yangzy 2015-05-21  中征码修改 end*/
	};

	function cheakMrk(){
		var com_mrk_flg = CusCom.com_mrk_flg._obj.element.value;
		if(com_mrk_flg=="1"){
			 CusCom.com_mrk_area._obj._renderRequired(true);
			 CusCom.com_mrk_area._obj._renderReadonly(false);
			 CusCom.com_mrk_area._obj._renderHidden(false);
	
		     CusCom.com_stock_id._obj._renderRequired(true);
		     CusCom.com_stock_id._obj._renderReadonly(false);
		     CusCom.com_stock_id._obj._renderHidden(false);
		}else if(com_mrk_flg=="2"){
			 CusCom.com_mrk_area._setValue("");
			 CusCom.com_mrk_area._obj._renderRequired(false);
			 CusCom.com_mrk_area._obj._renderReadonly(false);
			 CusCom.com_mrk_area._obj._renderHidden(true);
	
			 CusCom.com_stock_id._obj.element.value="";
		     CusCom.com_stock_id._obj._renderRequired(false);
		     CusCom.com_stock_id._obj._renderReadonly(false);
		     CusCom.com_stock_id._obj._renderHidden(true);
		}
	}
	
	//检查特种经营标识
	function cheakSpBusiness(){
		 if(CusCom.com_sp_business._obj.element.value=="1"){
			   CusCom.com_sp_lic_no._obj._renderRequired(true);
			   CusCom.com_sp_lic_no._obj._renderReadonly(false);
			   CusCom.com_sp_lic_no._obj._renderHidden(false);
	
			   CusCom.com_sp_detail._obj._renderRequired(true);
			   CusCom.com_sp_detail._obj._renderReadonly(false);
			   CusCom.com_sp_detail._obj._renderHidden(false);
	
			   CusCom.com_sp_lic_org._obj._renderRequired(false);
			   CusCom.com_sp_lic_org._obj._renderReadonly(false);
			   CusCom.com_sp_lic_org._obj._renderHidden(false);
	
			   CusCom.com_sp_str_date._obj._renderRequired(true);
			   CusCom.com_sp_str_date._obj._renderReadonly(false);
			   CusCom.com_sp_str_date._obj._renderHidden(false);
	
			   CusCom.com_sp_end_date._obj._renderRequired(true);
			   CusCom.com_sp_end_date._obj._renderReadonly(false);
			   CusCom.com_sp_end_date._obj._renderHidden(false);
		   }else if(CusCom.com_sp_business._obj.element.value=="2"){
			   CusCom.com_sp_lic_no._obj.element.value="";
			   CusCom.com_sp_lic_no._obj._renderRequired(false);
			   CusCom.com_sp_lic_no._obj._renderReadonly(false);
			   CusCom.com_sp_lic_no._obj._renderHidden(true);
	
			   CusCom.com_sp_detail._obj.element.value="";
			   CusCom.com_sp_detail._obj._renderRequired(false);
			   CusCom.com_sp_detail._obj._renderReadonly(false);
			   CusCom.com_sp_detail._obj._renderHidden(true);
	
			   CusCom.com_sp_lic_org._obj.element.value="";
			   CusCom.com_sp_lic_org._obj._renderRequired(false);
			   CusCom.com_sp_lic_org._obj._renderReadonly(false);
			   CusCom.com_sp_lic_org._obj._renderHidden(true);
	
			   CusCom.com_sp_str_date._obj.element.value="";
			   CusCom.com_sp_str_date._obj._renderRequired(false);
			   CusCom.com_sp_str_date._obj._renderReadonly(false);
			   CusCom.com_sp_str_date._obj._renderHidden(true);
	
			   CusCom.com_sp_end_date._obj.element.value="";
			   CusCom.com_sp_end_date._obj._renderRequired(false);
			   CusCom.com_sp_end_date._obj._renderReadonly(false);
			   CusCom.com_sp_end_date._obj._renderHidden(true);
		}
	}
	
	//集团客户类型字段的联动
	function linkChangeComGrpMode(){
		var comGrpMode_value= CusCom.com_grp_mode._getValue();
		var grp_no= CusCom.grp_no._getValue();
		if(grp_no==null||grp_no==""){
			CusCom.grp_no._obj._renderHidden(true);
			CusCom.grp_no._obj._renderRequired(false);
			if(comGrpMode_value=="4"){
				CusCom.parent_cus_name._obj._renderHidden(false);
				CusCom.parent_cert_code._obj._renderHidden(false);
				CusCom.parent_loan_card._obj._renderHidden(false);

				CusCom.parent_cus_name._obj._renderRequired(true);
				CusCom.parent_cert_code._obj._renderRequired(true);
				CusCom.parent_loan_card._obj._renderRequired(true);
			}else{
				CusCom.parent_cus_name._obj._renderHidden(true);
				CusCom.parent_cert_code._obj._renderHidden(true);
				CusCom.parent_loan_card._obj._renderHidden(true);

				CusCom.parent_cus_name._obj._renderRequired(false);
				CusCom.parent_cert_code._obj._renderRequired(false);
				CusCom.parent_loan_card._obj._renderRequired(false);

				CusCom.parent_cus_name._setValue('');
				CusCom.parent_cert_code._setValue('');
				CusCom.parent_loan_card._setValue('');
			}
		}else{
			CusCom.grp_no._obj._renderReadonly(true);
			CusCom.com_grp_mode._obj._renderReadonly(true);
		}
	}

	//集团客户LINK
	function viewGrpCusInfo(){
		var grp_no = CusCom.grp_no._getValue();
		if(grp_no!=null && grp_no!=""){
			var url = "<emp:url action='queryCusGrpInfoPopDetial.do'/>&grp_no="+grp_no;
	      	url=encodeURI(url);
	      	window.open(url,'newwindow','height='+window.screen.availHeight*0.7+',width='+window.screen.availWidth*0.7+',top=70,left=0,toolbar=no,menubar=no,scrollbars=no,resizable=yes,location=no,status=no');
		}
	}
	
	//高环境风险高污染企业  取消必输项控制 edited by FCL
	function doChange(){
		var com_dhgh_flg = CusCom.com_dhgh_flg._getValue();
		if(com_dhgh_flg == 2){
			CusCom.row_lice._obj._renderRequired(false);
			CusCom.row_lice_end_dt._obj._renderRequired(false);
		}else if(com_dhgh_flg == 1){
			CusCom.row_lice._obj._renderRequired(false);
			CusCom.row_lice_end_dt._obj._renderRequired(false);
		}
	}

	//战略性客户
	function changeLine(){
		var strCus = CusCom.str_cus._getValue();
		if(strCus == 1){
			CusCom.str_cus_end_dt._obj._renderHidden(false);
		}else if(strCus == 2){
			CusCom.str_cus_end_dt._obj._renderHidden(true);	
		}
	}

	//是否为我行关联客户
	function changeRelaCust(){
		var IsOursRelaCust = CusCom.is_ours_rela_cust._getValue();
		if(IsOursRelaCust == 03){//否
			CusCom.bank_duty._obj._renderHidden(true);
			CusCom.equi_no._obj._renderHidden(true);
			CusCom.bank_equi_amt._obj._renderHidden(true);
			CusCom.bank_duty._obj._renderRequired(false);
			CusCom.cus_bank_rel._obj._renderRequired(false);
			var options = CusCom.cus_bank_rel._obj.element.options;
	    	var option1 = new Option('普通客户关系','B1');
	    	options.add(option1);
			CusCom.cus_bank_rel._setValue("B1");
			CusCom.cus_bank_rel._obj._renderHidden(true);
			CusCom.cus_bank_rel_fake._obj._renderHidden(false);
		}else if(IsOursRelaCust == 01 || IsOursRelaCust == 02){
			CusCom.bank_duty._obj._renderHidden(false);
			CusCom.equi_no._obj._renderHidden(false);
			CusCom.bank_equi_amt._obj._renderHidden(false);
			CusCom.bank_duty._obj._renderRequired(true);
			CusCom.cus_bank_rel._obj._renderRequired(true);
			var options = CusCom.cus_bank_rel._obj.element.options;
			for ( var i = options.length - 1; i >= 0; i--) {
				if(options[i].value == 'B1'){
					options.remove(i);
				}
			}
			CusCom.cus_bank_rel._obj._renderHidden(false);
			CusCom.cus_bank_rel_fake._obj._renderHidden(true);
		}
	}

	//税务信息的校验
	function checkRegCode(){
		var LocTaxRegCode = CusCom.loc_tax_reg_code._getValue();//地税税务登记代码
		var loc_tax_reg_dt = CusCom.loc_tax_reg_dt._getValue();//地税税务登记日期
		var loc_tax_reg_end_dt = CusCom.loc_tax_reg_end_dt._getValue();//地税税务登记有效期
		var loc_tax_ann_date = CusCom.loc_tax_ann_date._getValue();//地税年检到期日
		var loc_tax_reg_org = CusCom.loc_tax_reg_org._getValue();//地税登记机关
		var NatTaxRegcode = CusCom.nat_tax_reg_code._getValue();//国税税务登记代码
		var nat_tax_reg_dt = CusCom.nat_tax_reg_dt._getValue();//国税税务登记日期
		var nat_tax_reg_end_dt = CusCom.nat_tax_reg_end_dt._getValue();//国税税务登记有效期
		var nat_tax_ann_date = CusCom.nat_tax_ann_date._getValue();//国税年检到期日
		var nat_tax_reg_org = CusCom.nat_tax_reg_org._getValue();//国税登记机关
		if(LocTaxRegCode !== "" && LocTaxRegCode !== null || loc_tax_reg_dt !== "" && loc_tax_reg_dt !== null
				|| loc_tax_reg_end_dt !== "" && loc_tax_reg_end_dt !== null || loc_tax_ann_date !== "" && loc_tax_ann_date !== null 
				|| loc_tax_reg_org !== "" && loc_tax_reg_org !== null){
			CusCom.loc_tax_reg_code._obj._renderRequired(true);
			CusCom.loc_tax_reg_dt._obj._renderRequired(true);
			CusCom.loc_tax_reg_org._obj._renderRequired(true);
		}else{
			CusCom.loc_tax_reg_code._obj._renderRequired(false);
			CusCom.loc_tax_reg_dt._obj._renderRequired(false);
			CusCom.loc_tax_reg_org._obj._renderRequired(false);
		}
		if(NatTaxRegcode !== "" && NatTaxRegcode !== null || nat_tax_reg_dt !== "" && nat_tax_reg_dt !== null
				|| nat_tax_reg_end_dt !== "" && nat_tax_reg_end_dt !== null || nat_tax_ann_date !== "" && nat_tax_ann_date !== null 
				|| nat_tax_reg_org !== "" && nat_tax_reg_org !== null){
			CusCom.nat_tax_reg_code._obj._renderRequired(true);
			CusCom.nat_tax_reg_dt._obj._renderRequired(true);
			CusCom.nat_tax_reg_org._obj._renderRequired(true);
		}else{
			CusCom.nat_tax_reg_code._obj._renderRequired(false);
			CusCom.nat_tax_reg_dt._obj._renderRequired(false);
			CusCom.nat_tax_reg_org._obj._renderRequired(false);
		}
	}

	function checkCoopInfo(){
		var cusType = CusBase.cus_type._getValue();
		if(cusType == 'A2' ){
			CusBase.guar_crd_grade._obj._renderHidden(false);
			CusCom.guar_crd_grade2._obj._renderHidden(false);
			CusCom.com_coop_exp._obj._renderHidden(false);
			CusCom.guar_cls._obj._renderHidden(false);
			CusCom.guar_bail_multiple._obj._renderHidden(false);
			CusBase.cus_crd_grade._obj._renderHidden(true);
			CusCom.com_out_crd_grade._obj._renderHidden(true);
			CusCom.com_out_crd_grade._obj._renderRequired(false);
		}else{
			CusBase.guar_crd_grade._obj._renderHidden(true);
			CusCom.guar_crd_grade2._obj._renderHidden(true);
			CusCom.com_coop_exp._obj._renderHidden(true);
			CusCom.guar_cls._obj._renderHidden(true);
			CusCom.guar_bail_multiple._obj._renderHidden(true);
			CusBase.cus_crd_grade._obj._renderHidden(false);
			CusCom.com_out_crd_grade._obj._renderHidden(false);
			CusCom.com_out_crd_grade._obj._renderRequired(true);
		}
	}

	function changeGrade(){
		var cusType = CusBase.cus_type._getValue();
		if(cusType == 'A2' ){
			var com_out_crd_grade = CusCom.guar_crd_grade2._getValue();
			if(com_out_crd_grade == "Z"){
				CusCom.com_out_crd_dt._obj._renderRequired(false);
				CusCom.com_out_crd_org._obj._renderRequired(false);
				CusCom.com_out_crd_dt._setValue('');
				CusCom.com_out_crd_org._setValue('');
			}else{
				CusCom.com_out_crd_dt._obj._renderRequired(true);
				CusCom.com_out_crd_org._obj._renderRequired(true);
			}
		}else{
			var com_out_crd_grade = CusCom.com_out_crd_grade._getValue();
			if(com_out_crd_grade == "00"){
				CusCom.com_out_crd_dt._obj._renderRequired(false);
				CusCom.com_out_crd_org._obj._renderRequired(false);
				CusCom.com_out_crd_dt._setValue('');
				CusCom.com_out_crd_org._setValue('');
			}else{
				CusCom.com_out_crd_dt._obj._renderRequired(true);
				CusCom.com_out_crd_org._obj._renderRequired(true);
			}
		}
	}

	//进出口权标志
	function checkIeFlag(){
		var ie_flag = CusCom.ie_flag._getValue();
		if(ie_flag=="1"){
			CusCom.ie_con_code._obj._renderHidden(false);
		}else{
			CusCom.ie_con_code._obj._renderHidden(true);
			CusCom.ie_con_code._setValue("");
		}
	}
</script>