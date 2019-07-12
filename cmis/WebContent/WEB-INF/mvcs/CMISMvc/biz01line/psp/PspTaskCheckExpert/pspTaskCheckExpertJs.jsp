<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp"%>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<script type="text/javascript">
//-----------------------专项检查设置JS----------------------------
	//根据任务生成方式的不同来显示或者隐藏不同的字段
	function doCheck(){
	    var task_create_mode = PspTaskCheckExpert.task_create_mode._getValue();
		if(task_create_mode=="00"){//按单一客户
			singleCus();
		}else if(task_create_mode=="01"){//按集团客户
			grpCus();
		}else if(task_create_mode=="02"){//按担保方式
			guarMode();
		}else if(task_create_mode=="03"){//按业务品种
			bizVariet();
		}else if(task_create_mode=="04"){//按支行机构
			branchOrg();
		}else if(task_create_mode=="05"){//按客户经理
			cusManager();
		}else if(task_create_mode=="06"){//按金额区间
			amtSection();
		}else if(task_create_mode=="07"){//按圈商类型
			bizAreaType();
		}else if(task_create_mode=="08"){//按合作项目
			coopPro();
		}else if(task_create_mode=="09"){//按联保小组
			jointGuarantee();
		}else if(task_create_mode=="10"){//按行业投向
			tradeTer();
		}else if(task_create_mode=="11"){//按政府融资平台
			goverFinPlatForm();
		}
	}
	//单一客户时Js校验
	function singleCus(){
		PspTaskCheckExpert.cus_id._obj._renderRequired(true);
		PspTaskCheckExpert.cus_id_displayname._obj._renderRequired(true);
		PspTaskCheckExpert.cus_id._obj._renderHidden(false);
		PspTaskCheckExpert.cus_id_displayname._obj._renderHidden(false);
		PspTaskCheckExpert.grp_no._obj._renderHidden(true);
		PspTaskCheckExpert.grp_no_displayname._obj._renderHidden(true);
		PspTaskCheckExpert.assure_main._obj._renderHidden(true);
		PspTaskCheckExpert.biz_type._obj._renderHidden(true);
		PspTaskCheckExpert.biz_type_displayname._obj._renderHidden(true);
		PspTaskCheckExpert.belg_branch._obj._renderHidden(true);
		PspTaskCheckExpert.belg_branch_displayname._obj._renderHidden(true);
		PspTaskCheckExpert.belg_manager_id._obj._renderHidden(true);
		PspTaskCheckExpert.belg_manager_id_displayname._obj._renderHidden(true);
		PspTaskCheckExpert.loan_amt_begin._obj._renderHidden(true);
		PspTaskCheckExpert.loan_amt_end._obj._renderHidden(true);
		PspTaskCheckExpert.biz_circle_no._obj._renderHidden(true);
		PspTaskCheckExpert.biz_area_name._obj._renderHidden(true);
		PspTaskCheckExpert.biz_area_type._obj._renderHidden(true);
		PspTaskCheckExpert.agr_no._obj._renderHidden(true);
		PspTaskCheckExpert.agr_no_displayname._obj._renderHidden(true);
		PspTaskCheckExpert.coop_type._obj._renderHidden(true);
		PspTaskCheckExpert.biz_area_no._obj._renderHidden(true);
		PspTaskCheckExpert.indus_type._obj._renderHidden(true);
		PspTaskCheckExpert.indus_type_displayname._obj._renderHidden(true);
		PspTaskCheckExpert.gover_fin_ter._obj._renderHidden(true);
		PspTaskCheckExpert.gover_fin_ter_displayname._obj._renderHidden(true);
	}
	//集团客户时Js校验
	function grpCus(){
		PspTaskCheckExpert.grp_no._obj._renderRequired(true);
		PspTaskCheckExpert.grp_no_displayname._obj._renderRequired(true);
		PspTaskCheckExpert.cus_id._obj._renderHidden(true);
		PspTaskCheckExpert.cus_id_displayname._obj._renderHidden(true);
		PspTaskCheckExpert.grp_no._obj._renderHidden(false);
		PspTaskCheckExpert.grp_no_displayname._obj._renderHidden(false);
		PspTaskCheckExpert.assure_main._obj._renderHidden(true);
		PspTaskCheckExpert.biz_type._obj._renderHidden(true);
		PspTaskCheckExpert.biz_type_displayname._obj._renderHidden(true);
		PspTaskCheckExpert.belg_branch._obj._renderHidden(true);
		PspTaskCheckExpert.belg_branch_displayname._obj._renderHidden(true);
		PspTaskCheckExpert.belg_manager_id._obj._renderHidden(true);
		PspTaskCheckExpert.belg_manager_id_displayname._obj._renderHidden(true);
		PspTaskCheckExpert.loan_amt_begin._obj._renderHidden(true);
		PspTaskCheckExpert.loan_amt_end._obj._renderHidden(true);
		PspTaskCheckExpert.biz_circle_no._obj._renderHidden(true);
		PspTaskCheckExpert.biz_area_name._obj._renderHidden(true);
		PspTaskCheckExpert.biz_area_type._obj._renderHidden(true);
		PspTaskCheckExpert.agr_no._obj._renderHidden(true);
		PspTaskCheckExpert.agr_no_displayname._obj._renderHidden(true);
		PspTaskCheckExpert.coop_type._obj._renderHidden(true);
		PspTaskCheckExpert.biz_area_no._obj._renderHidden(true);
		PspTaskCheckExpert.indus_type._obj._renderHidden(true);
		PspTaskCheckExpert.indus_type_displayname._obj._renderHidden(true);
		PspTaskCheckExpert.gover_fin_ter._obj._renderHidden(true);
		PspTaskCheckExpert.gover_fin_ter_displayname._obj._renderHidden(true);
	}
	//担保方式时Js校验
	function guarMode(){
		PspTaskCheckExpert.assure_main._obj._renderRequired(true);
		PspTaskCheckExpert.cus_id._obj._renderHidden(true);
		PspTaskCheckExpert.cus_id_displayname._obj._renderHidden(true);
		PspTaskCheckExpert.grp_no._obj._renderHidden(true);
		PspTaskCheckExpert.grp_no_displayname._obj._renderHidden(true);
		PspTaskCheckExpert.assure_main._obj._renderHidden(false);
		PspTaskCheckExpert.biz_type._obj._renderHidden(true);
		PspTaskCheckExpert.biz_type_displayname._obj._renderHidden(true);
		PspTaskCheckExpert.belg_branch._obj._renderHidden(true);
		PspTaskCheckExpert.belg_branch_displayname._obj._renderHidden(true);
		PspTaskCheckExpert.belg_manager_id._obj._renderHidden(true);
		PspTaskCheckExpert.belg_manager_id_displayname._obj._renderHidden(true);
		PspTaskCheckExpert.loan_amt_begin._obj._renderHidden(true);
		PspTaskCheckExpert.loan_amt_end._obj._renderHidden(true);
		PspTaskCheckExpert.biz_circle_no._obj._renderHidden(true);
		PspTaskCheckExpert.biz_area_name._obj._renderHidden(true);
		PspTaskCheckExpert.biz_area_type._obj._renderHidden(true);
		PspTaskCheckExpert.agr_no._obj._renderHidden(true);
		PspTaskCheckExpert.agr_no_displayname._obj._renderHidden(true);
		PspTaskCheckExpert.coop_type._obj._renderHidden(true);
		PspTaskCheckExpert.biz_area_no._obj._renderHidden(true);
		PspTaskCheckExpert.indus_type._obj._renderHidden(true);
		PspTaskCheckExpert.indus_type_displayname._obj._renderHidden(true);
		PspTaskCheckExpert.gover_fin_ter._obj._renderHidden(true);
		PspTaskCheckExpert.gover_fin_ter_displayname._obj._renderHidden(true);
	}
	//业务品种时Js校验
	function bizVariet(){
		PspTaskCheckExpert.biz_type._obj._renderRequired(true);
		PspTaskCheckExpert.biz_type_displayname._obj._renderRequired(true);
		PspTaskCheckExpert.cus_id._obj._renderHidden(true);
		PspTaskCheckExpert.cus_id_displayname._obj._renderHidden(true);
		PspTaskCheckExpert.grp_no._obj._renderHidden(true);
		PspTaskCheckExpert.grp_no_displayname._obj._renderHidden(true);
		PspTaskCheckExpert.assure_main._obj._renderHidden(true);
		PspTaskCheckExpert.biz_type._obj._renderHidden(true);
		PspTaskCheckExpert.biz_type_displayname._obj._renderHidden(false);
		PspTaskCheckExpert.belg_branch._obj._renderHidden(true);
		PspTaskCheckExpert.belg_branch_displayname._obj._renderHidden(true);
		PspTaskCheckExpert.belg_manager_id._obj._renderHidden(true);
		PspTaskCheckExpert.belg_manager_id_displayname._obj._renderHidden(true);
		PspTaskCheckExpert.loan_amt_begin._obj._renderHidden(true);
		PspTaskCheckExpert.loan_amt_end._obj._renderHidden(true);
		PspTaskCheckExpert.biz_circle_no._obj._renderHidden(true);
		PspTaskCheckExpert.biz_area_name._obj._renderHidden(true);
		PspTaskCheckExpert.biz_area_type._obj._renderHidden(true);
		PspTaskCheckExpert.agr_no._obj._renderHidden(true);
		PspTaskCheckExpert.agr_no_displayname._obj._renderHidden(true);
		PspTaskCheckExpert.coop_type._obj._renderHidden(true);
		PspTaskCheckExpert.biz_area_no._obj._renderHidden(true);
		PspTaskCheckExpert.indus_type._obj._renderHidden(true);
		PspTaskCheckExpert.indus_type_displayname._obj._renderHidden(true);
		PspTaskCheckExpert.gover_fin_ter._obj._renderHidden(true);
		PspTaskCheckExpert.gover_fin_ter_displayname._obj._renderHidden(true);
	}
	//支行机构时Js校验
	function branchOrg(){
		PspTaskCheckExpert.belg_branch._obj._renderRequired(true);
		PspTaskCheckExpert.belg_branch_displayname._obj._renderRequired(true);
		PspTaskCheckExpert.cus_id._obj._renderHidden(true);
		PspTaskCheckExpert.cus_id_displayname._obj._renderHidden(true);
		PspTaskCheckExpert.grp_no._obj._renderHidden(true);
		PspTaskCheckExpert.grp_no_displayname._obj._renderHidden(true);
		PspTaskCheckExpert.assure_main._obj._renderHidden(true);
		PspTaskCheckExpert.biz_type._obj._renderHidden(true);
		PspTaskCheckExpert.biz_type_displayname._obj._renderHidden(true);
		PspTaskCheckExpert.belg_branch._obj._renderHidden(true);
		PspTaskCheckExpert.belg_branch_displayname._obj._renderHidden(false);
		PspTaskCheckExpert.belg_manager_id._obj._renderHidden(true);
		PspTaskCheckExpert.belg_manager_id_displayname._obj._renderHidden(true);
		PspTaskCheckExpert.loan_amt_begin._obj._renderHidden(true);
		PspTaskCheckExpert.loan_amt_end._obj._renderHidden(true);
		PspTaskCheckExpert.biz_circle_no._obj._renderHidden(true);
		PspTaskCheckExpert.biz_area_name._obj._renderHidden(true);
		PspTaskCheckExpert.biz_area_type._obj._renderHidden(true);
		PspTaskCheckExpert.agr_no._obj._renderHidden(true);
		PspTaskCheckExpert.agr_no_displayname._obj._renderHidden(true);
		PspTaskCheckExpert.coop_type._obj._renderHidden(true);
		PspTaskCheckExpert.biz_area_no._obj._renderHidden(true);
		PspTaskCheckExpert.indus_type._obj._renderHidden(true);
		PspTaskCheckExpert.indus_type_displayname._obj._renderHidden(true);
		PspTaskCheckExpert.gover_fin_ter._obj._renderHidden(true);
		PspTaskCheckExpert.gover_fin_ter_displayname._obj._renderHidden(true);
	}
	
	//客户经理时Js校验
	function cusManager(){
		PspTaskCheckExpert.belg_manager_id._obj._renderRequired(true);
		PspTaskCheckExpert.belg_manager_id_displayname._obj._renderRequired(true);
		PspTaskCheckExpert.cus_id._obj._renderHidden(true);
		PspTaskCheckExpert.cus_id_displayname._obj._renderHidden(true);
		PspTaskCheckExpert.grp_no._obj._renderHidden(true);
		PspTaskCheckExpert.grp_no_displayname._obj._renderHidden(true);
		PspTaskCheckExpert.assure_main._obj._renderHidden(true);
		PspTaskCheckExpert.biz_type._obj._renderHidden(true);
		PspTaskCheckExpert.biz_type_displayname._obj._renderHidden(true);
		PspTaskCheckExpert.belg_branch._obj._renderHidden(true);
		PspTaskCheckExpert.belg_branch_displayname._obj._renderHidden(true);
		PspTaskCheckExpert.belg_manager_id._obj._renderHidden(true);
		PspTaskCheckExpert.belg_manager_id_displayname._obj._renderHidden(false);
		PspTaskCheckExpert.loan_amt_begin._obj._renderHidden(true);
		PspTaskCheckExpert.loan_amt_end._obj._renderHidden(true);
		PspTaskCheckExpert.biz_circle_no._obj._renderHidden(true);
		PspTaskCheckExpert.biz_area_name._obj._renderHidden(true);
		PspTaskCheckExpert.biz_area_type._obj._renderHidden(true);
		PspTaskCheckExpert.agr_no._obj._renderHidden(true);
		PspTaskCheckExpert.agr_no_displayname._obj._renderHidden(true);
		PspTaskCheckExpert.coop_type._obj._renderHidden(true);
		PspTaskCheckExpert.biz_area_no._obj._renderHidden(true);
		PspTaskCheckExpert.indus_type._obj._renderHidden(true);
		PspTaskCheckExpert.indus_type_displayname._obj._renderHidden(true);
		PspTaskCheckExpert.gover_fin_ter._obj._renderHidden(true);
		PspTaskCheckExpert.gover_fin_ter_displayname._obj._renderHidden(true);
	}
	//金额区间时Js校验
	function amtSection(){
		PspTaskCheckExpert.loan_amt_begin._obj._renderRequired(true);
		PspTaskCheckExpert.loan_amt_end._obj._renderRequired(true);
		PspTaskCheckExpert.cus_id._obj._renderHidden(true);
		PspTaskCheckExpert.cus_id_displayname._obj._renderHidden(true);
		PspTaskCheckExpert.grp_no._obj._renderHidden(true);
		PspTaskCheckExpert.grp_no_displayname._obj._renderHidden(true);
		PspTaskCheckExpert.assure_main._obj._renderHidden(true);
		PspTaskCheckExpert.biz_type._obj._renderHidden(true);
		PspTaskCheckExpert.biz_type_displayname._obj._renderHidden(true);
		PspTaskCheckExpert.belg_branch._obj._renderHidden(true);
		PspTaskCheckExpert.belg_branch_displayname._obj._renderHidden(true);
		PspTaskCheckExpert.belg_manager_id._obj._renderHidden(true);
		PspTaskCheckExpert.belg_manager_id_displayname._obj._renderHidden(true);
		PspTaskCheckExpert.loan_amt_begin._obj._renderHidden(false);
		PspTaskCheckExpert.loan_amt_end._obj._renderHidden(false);
		PspTaskCheckExpert.biz_circle_no._obj._renderHidden(true);
		PspTaskCheckExpert.biz_area_name._obj._renderHidden(true);
		PspTaskCheckExpert.biz_area_type._obj._renderHidden(true);
		PspTaskCheckExpert.agr_no._obj._renderHidden(true);
		PspTaskCheckExpert.agr_no_displayname._obj._renderHidden(true);
		PspTaskCheckExpert.coop_type._obj._renderHidden(true);
		PspTaskCheckExpert.biz_area_no._obj._renderHidden(true);
		PspTaskCheckExpert.indus_type._obj._renderHidden(true);
		PspTaskCheckExpert.indus_type_displayname._obj._renderHidden(true);
		PspTaskCheckExpert.gover_fin_ter._obj._renderHidden(true);
		PspTaskCheckExpert.gover_fin_ter_displayname._obj._renderHidden(true);
	}
	//圈商类型时Js校验
	function bizAreaType(){
		PspTaskCheckExpert.biz_circle_no._obj._renderRequired(true);
		PspTaskCheckExpert.biz_area_name._obj._renderRequired(true);
		PspTaskCheckExpert.biz_area_type._obj._renderRequired(true);
		PspTaskCheckExpert.cus_id._obj._renderHidden(true);
		PspTaskCheckExpert.cus_id_displayname._obj._renderHidden(true);
		PspTaskCheckExpert.grp_no._obj._renderHidden(true);
		PspTaskCheckExpert.grp_no_displayname._obj._renderHidden(true);
		PspTaskCheckExpert.assure_main._obj._renderHidden(true);
		PspTaskCheckExpert.biz_type._obj._renderHidden(true);
		PspTaskCheckExpert.biz_type_displayname._obj._renderHidden(true);
		PspTaskCheckExpert.belg_branch._obj._renderHidden(true);
		PspTaskCheckExpert.belg_branch_displayname._obj._renderHidden(true);
		PspTaskCheckExpert.belg_manager_id._obj._renderHidden(true);
		PspTaskCheckExpert.belg_manager_id_displayname._obj._renderHidden(true);
		PspTaskCheckExpert.loan_amt_begin._obj._renderHidden(true);
		PspTaskCheckExpert.loan_amt_end._obj._renderHidden(true);
		PspTaskCheckExpert.biz_circle_no._obj._renderHidden(false);
		PspTaskCheckExpert.biz_area_name._obj._renderHidden(false);
		PspTaskCheckExpert.biz_area_type._obj._renderHidden(false);
		PspTaskCheckExpert.agr_no._obj._renderHidden(true);
		PspTaskCheckExpert.agr_no_displayname._obj._renderHidden(true);
		PspTaskCheckExpert.coop_type._obj._renderHidden(true);
		PspTaskCheckExpert.biz_area_no._obj._renderHidden(true);
		PspTaskCheckExpert.indus_type._obj._renderHidden(true);
		PspTaskCheckExpert.indus_type_displayname._obj._renderHidden(true);
		PspTaskCheckExpert.gover_fin_ter._obj._renderHidden(true);
		PspTaskCheckExpert.gover_fin_ter_displayname._obj._renderHidden(true);
	}
	//合作项目时Js校验
	function coopPro(){
		PspTaskCheckExpert.agr_no._obj._renderRequired(true);
		PspTaskCheckExpert.agr_no_displayname._obj._renderRequired(true);
		PspTaskCheckExpert.coop_type._obj._renderRequired(true);
		PspTaskCheckExpert.cus_id._obj._renderHidden(true);
		PspTaskCheckExpert.cus_id_displayname._obj._renderHidden(true);
		PspTaskCheckExpert.grp_no._obj._renderHidden(true);
		PspTaskCheckExpert.grp_no_displayname._obj._renderHidden(true);
		PspTaskCheckExpert.assure_main._obj._renderHidden(true);
		PspTaskCheckExpert.biz_type._obj._renderHidden(true);
		PspTaskCheckExpert.biz_type_displayname._obj._renderHidden(true);
		PspTaskCheckExpert.belg_branch._obj._renderHidden(true);
		PspTaskCheckExpert.belg_branch_displayname._obj._renderHidden(true);
		PspTaskCheckExpert.belg_manager_id._obj._renderHidden(true);
		PspTaskCheckExpert.belg_manager_id_displayname._obj._renderHidden(true);
		PspTaskCheckExpert.loan_amt_begin._obj._renderHidden(true);
		PspTaskCheckExpert.loan_amt_end._obj._renderHidden(true);
		PspTaskCheckExpert.biz_circle_no._obj._renderHidden(true);
		PspTaskCheckExpert.biz_area_name._obj._renderHidden(true);
		PspTaskCheckExpert.biz_area_type._obj._renderHidden(true);
		PspTaskCheckExpert.agr_no._obj._renderHidden(false);
		PspTaskCheckExpert.agr_no_displayname._obj._renderHidden(false);
		PspTaskCheckExpert.coop_type._obj._renderHidden(false);
		PspTaskCheckExpert.biz_area_no._obj._renderHidden(true);
		PspTaskCheckExpert.indus_type._obj._renderHidden(true);
		PspTaskCheckExpert.indus_type_displayname._obj._renderHidden(true);
		PspTaskCheckExpert.gover_fin_ter._obj._renderHidden(true);
		PspTaskCheckExpert.gover_fin_ter_displayname._obj._renderHidden(true);
	}
	//联保小组时Js校验
	function jointGuarantee(){
		PspTaskCheckExpert.biz_area_no._obj._renderRequired(true);
		PspTaskCheckExpert.cus_id._obj._renderHidden(true);
		PspTaskCheckExpert.cus_id_displayname._obj._renderHidden(true);
		PspTaskCheckExpert.grp_no._obj._renderHidden(true);
		PspTaskCheckExpert.grp_no_displayname._obj._renderHidden(true);
		PspTaskCheckExpert.assure_main._obj._renderHidden(true);
		PspTaskCheckExpert.biz_type._obj._renderHidden(true);
		PspTaskCheckExpert.biz_type_displayname._obj._renderHidden(true);
		PspTaskCheckExpert.belg_branch._obj._renderHidden(true);
		PspTaskCheckExpert.belg_branch_displayname._obj._renderHidden(true);
		PspTaskCheckExpert.belg_manager_id._obj._renderHidden(true);
		PspTaskCheckExpert.belg_manager_id_displayname._obj._renderHidden(true);
		PspTaskCheckExpert.loan_amt_begin._obj._renderHidden(true);
		PspTaskCheckExpert.loan_amt_end._obj._renderHidden(true);
		PspTaskCheckExpert.biz_circle_no._obj._renderHidden(true);
		PspTaskCheckExpert.biz_area_name._obj._renderHidden(true);
		PspTaskCheckExpert.biz_area_type._obj._renderHidden(true);
		PspTaskCheckExpert.agr_no._obj._renderHidden(true);
		PspTaskCheckExpert.agr_no_displayname._obj._renderHidden(true);
		PspTaskCheckExpert.coop_type._obj._renderHidden(true);
		PspTaskCheckExpert.biz_area_no._obj._renderHidden(false);
		PspTaskCheckExpert.indus_type._obj._renderHidden(true);
		PspTaskCheckExpert.indus_type_displayname._obj._renderHidden(true);
		PspTaskCheckExpert.gover_fin_ter._obj._renderHidden(true);
		PspTaskCheckExpert.gover_fin_ter_displayname._obj._renderHidden(true);
	}
	//行业投向时Js校验
	function tradeTer(){
		PspTaskCheckExpert.indus_type_displayname._obj._renderRequired(true);
		PspTaskCheckExpert.cus_id._obj._renderHidden(true);
		PspTaskCheckExpert.cus_id_displayname._obj._renderHidden(true);
		PspTaskCheckExpert.grp_no._obj._renderHidden(true);
		PspTaskCheckExpert.grp_no_displayname._obj._renderHidden(true);
		PspTaskCheckExpert.assure_main._obj._renderHidden(true);
		PspTaskCheckExpert.biz_type._obj._renderHidden(true);
		PspTaskCheckExpert.biz_type_displayname._obj._renderHidden(true);
		PspTaskCheckExpert.belg_branch._obj._renderHidden(true);
		PspTaskCheckExpert.belg_branch_displayname._obj._renderHidden(true);
		PspTaskCheckExpert.belg_manager_id._obj._renderHidden(true);
		PspTaskCheckExpert.belg_manager_id_displayname._obj._renderHidden(true);
		PspTaskCheckExpert.loan_amt_begin._obj._renderHidden(true);
		PspTaskCheckExpert.loan_amt_end._obj._renderHidden(true);
		PspTaskCheckExpert.biz_circle_no._obj._renderHidden(true);
		PspTaskCheckExpert.biz_area_name._obj._renderHidden(true);
		PspTaskCheckExpert.biz_area_type._obj._renderHidden(true);
		PspTaskCheckExpert.agr_no._obj._renderHidden(true);
		PspTaskCheckExpert.agr_no_displayname._obj._renderHidden(true);
		PspTaskCheckExpert.coop_type._obj._renderHidden(true);
		PspTaskCheckExpert.biz_area_no._obj._renderHidden(true);
		PspTaskCheckExpert.indus_type._obj._renderHidden(true);
		PspTaskCheckExpert.indus_type_displayname._obj._renderHidden(false);
		PspTaskCheckExpert.gover_fin_ter._obj._renderHidden(true);
		PspTaskCheckExpert.gover_fin_ter_displayname._obj._renderHidden(true);
	}
	//政府融资平台时Js校验
	function goverFinPlatForm(){
		PspTaskCheckExpert.gover_fin_ter._obj._renderRequired(true);
		PspTaskCheckExpert.gover_fin_ter_displayname._obj._renderRequired(true);
		PspTaskCheckExpert.cus_id._obj._renderHidden(true);
		PspTaskCheckExpert.cus_id_displayname._obj._renderHidden(true);
		PspTaskCheckExpert.grp_no._obj._renderHidden(true);
		PspTaskCheckExpert.grp_no_displayname._obj._renderHidden(true);
		PspTaskCheckExpert.assure_main._obj._renderHidden(true);
		PspTaskCheckExpert.biz_type._obj._renderHidden(true);
		PspTaskCheckExpert.biz_type_displayname._obj._renderHidden(true);
		PspTaskCheckExpert.belg_branch._obj._renderHidden(true);
		PspTaskCheckExpert.belg_branch_displayname._obj._renderHidden(true);
		PspTaskCheckExpert.belg_manager_id._obj._renderHidden(true);
		PspTaskCheckExpert.belg_manager_id_displayname._obj._renderHidden(true);
		PspTaskCheckExpert.loan_amt_begin._obj._renderHidden(true);
		PspTaskCheckExpert.loan_amt_end._obj._renderHidden(true);
		PspTaskCheckExpert.biz_circle_no._obj._renderHidden(true);
		PspTaskCheckExpert.biz_area_name._obj._renderHidden(true);
		PspTaskCheckExpert.biz_area_type._obj._renderHidden(true);
		PspTaskCheckExpert.agr_no._obj._renderHidden(true);
		PspTaskCheckExpert.agr_no_displayname._obj._renderHidden(true);
		PspTaskCheckExpert.coop_type._obj._renderHidden(true);
		PspTaskCheckExpert.biz_area_no._obj._renderHidden(true);
		PspTaskCheckExpert.indus_type._obj._renderHidden(true);
		PspTaskCheckExpert.indus_type_displayname._obj._renderHidden(true);
		PspTaskCheckExpert.gover_fin_ter._obj._renderHidden(false);
		PspTaskCheckExpert.gover_fin_ter_displayname._obj._renderHidden(false);
	}
	//日期校验
	function checkDt(){
		var occur_date = PspTaskCheckExpert.need_finish_date._getValue();
		var openDay='${context.OPENDAY}';
		if(occur_date!=''){
			if(CheckDate1BeforeDate2(occur_date,openDay)){
	    		alert('要求完成时间需大于当前日期！');
	    		PspTaskCheckExpert.need_finish_date._obj.element.value="";
	    		return;
	    	}
    	}
	}
	//单一客户选择pop返回方法
	function returnCus(data){
		PspTaskCheckExpert.cus_id._setValue(data.cus_id._getValue());
		PspTaskCheckExpert.cus_id_displayname._setValue(data.cus_name._getValue());
	}
	//客户经理pop返回方法
	function setconId(data){
		PspTaskCheckExpert.task_exe_id_displayname._setValue(data.actorname._getValue());
		PspTaskCheckExpert.task_exe_id._setValue(data.actorno._getValue());
	}
	//机构pop返回方法
	function getOrgID(data){
		PspTaskCheckExpert.task_exe_br_id._setValue(data.organno._getValue());
		PspTaskCheckExpert.task_exe_br_id_displayname._setValue(data.organname._getValue());
	}
	//集团客户选择pop返回方法
	function returnCusGrp(data){
		PspTaskCheckExpert.grp_no._setValue(data.grp_no._getValue());
		PspTaskCheckExpert.grp_no_displayname._setValue(data.grp_name._getValue());
	}
	//产品树返回方法
	function returnPrdId(data){
		PspTaskCheckExpert.biz_type._setValue(data.id);
		PspTaskCheckExpert.biz_type_displayname._setValue(data.label);
	};
	//所属支行返回方法
	function getOrgId1(data){
		PspTaskCheckExpert.belg_branch._setValue(data.organno._getValue());
		PspTaskCheckExpert.belg_branch_displayname._setValue(data.organname._getValue());
	}
	//所属客户经理返回方法
	function setconId1(data){
		PspTaskCheckExpert.belg_manager_id._setValue(data.actorno._getValue());
		PspTaskCheckExpert.belg_manager_id_displayname._setValue(data.actorname._getValue());
	}
	//行业投向返回方法
	function onReturnComCllName(date){
		PspTaskCheckExpert.indus_type._obj.element.value=date.One+date.id;
		PspTaskCheckExpert.indus_type_displayname._obj.element.value=date.label;
	}
	//圈商返回方法
	function setBizAreaNo(data){
		PspTaskCheckExpert.biz_circle_no._setValue(data.agr_no._getValue());
		//PspTaskCheckExpert.biz_area_name._setValue(data.biz_area_name._getValue());
		//PspTaskCheckExpert.biz_area_type._setValue(data.biz_area_type._getValue());
	}
	//联保协议编号返回方法
	function returnAgrNo(data){
		PspTaskCheckExpert.biz_area_no._setValue(data.agr_no._getValue());
	}
	//政府融资性平台返回方法
	function getGoverFinTer(data){
		PspTaskCheckExpert.gover_fin_ter._setValue(data.cus_id._getValue());
		PspTaskCheckExpert.gover_fin_ter_displayname._setValue(data.cus_id_displayname._getValue());
	}
	//合作方返回方法
	function returnCoopAgrNo(data){
		PspTaskCheckExpert.agr_no._setValue(data.cus_id._getValue());
		PspTaskCheckExpert.agr_no_displayname._setValue(data.cus_id_displayname._getValue());
		PspTaskCheckExpert.coop_type._setValue(data.coop_type._getValue());
	}
	//贷款金额校验（低值区间）
	function checkBegin(){
		var loan_amt_begin = PspTaskCheckExpert.loan_amt_begin._getValue();
		var loan_amt_end = PspTaskCheckExpert.loan_amt_end._getValue();
		if(loan_amt_begin!=''&&loan_amt_end!=''){
 			if(parseFloat(loan_amt_begin)>=parseFloat(loan_amt_end)){
 	 			alert("贷款金额低值区间的值需要小于高值区间的值");
 				PspTaskCheckExpert.loan_amt_begin._setValue('');
 	 		}
		}
	}
	//贷款金额校验（高值区间）
	function checkEnd(){
		var loan_amt_begin = PspTaskCheckExpert.loan_amt_begin._getValue();
		var loan_amt_end = PspTaskCheckExpert.loan_amt_end._getValue();
		if(loan_amt_begin!=''&&loan_amt_end!=''){
 			if(parseFloat(loan_amt_begin)>=parseFloat(loan_amt_end)){
 	 			alert("贷款金额高值区间的值需要大于低值区间的值");
 				PspTaskCheckExpert.loan_amt_end._setValue('');
 	 		}
		}
	}
//-----------------------------------------------------------

</script>