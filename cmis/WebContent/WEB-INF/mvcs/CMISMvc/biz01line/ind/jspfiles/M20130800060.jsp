<%@page language="java" contentType="text/html; charset=UTF-8"%> 
<%@taglib uri="/WEB-INF/c-rt.tld" prefix="c" %>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<%@taglib uri="/WEB-INF/ind.tld" prefix="ind" %>
<link rel="stylesheet" type="text/css" href="<emp:file fileName='styles/ccrTable.css'/>"/>

<script type="text/javascript">
//验证单选是否选中.
function judgeRadioChecked(obj)
{
   if (obj){
    if (obj.length!=undefined)
    {
     for(var i=0;i < obj.length;i++)
     {
     if (obj[i].checked) return true;
     }
    }
    else{
      if (obj.checked) return true;
    }
   }
   return false;
}
function checkRequired(){
//检查每个组中的指标是否有值.如果没有则警告并返回.
var item;
//检验组管理指标的指标
				item=document.getElementsByName('G2013203938.ST023$ST02302');
		if(!judgeRadioChecked(item)){
			alert("指标[资信记录和商业信誉]未选择，请选择后提交。");
			return false;
		}
				item=document.getElementsByName('G2013203938.ST023$ST02303');
		if(!judgeRadioChecked(item)){
			alert("指标[有无完善的管理制度]未选择，请选择后提交。");
			return false;
		}
				item=document.getElementsByName('G2013203938.ST023$ST02304');
		if(!judgeRadioChecked(item)){
			alert("指标[网络信息化管理水平]未选择，请选择后提交。");
			return false;
		}
				item=document.getElementsByName('G2013203938.ST023$ST02305');
		if(!judgeRadioChecked(item)){
			alert("指标[有无稳定的管理团队]未选择，请选择后提交。");
			return false;
		}
				item=document.getElementsByName('G2013203938.ST023$ST02306');
		if(!judgeRadioChecked(item)){
			alert("指标[有无完善的商品检、化验制度和设备]未选择，请选择后提交。");
			return false;
		}
				item=document.getElementsByName('G2013203938.ST023$ST02307');
		if(!judgeRadioChecked(item)){
			alert("指标[与其他银行的合作年限]未选择，请选择后提交。");
			return false;
		}
				item=document.getElementsByName('G2013203938.ST023$ST02308');
		if(!judgeRadioChecked(item)){
			alert("指标[与我行的合作年限]未选择，请选择后提交。");
			return false;
		}
				item=document.getElementsByName('G2013203938.ST023$ST02309');
		if(!judgeRadioChecked(item)){
			alert("指标[管理能力（重点指输出监管能力）]未选择，请选择后提交。");
			return false;
		}
				item=document.getElementsByName('G2013203938.ST023$ST02310');
		if(!judgeRadioChecked(item)){
			alert("指标[与银行合作经历（专指仓储监管）]未选择，请选择后提交。");
			return false;
		}
				item=document.getElementsByName('G2013203938.ST023$ST02311');
		if(!judgeRadioChecked(item)){
			alert("指标[年利润总额]未选择，请选择后提交。");
			return false;
		}
	//检验组规模指标的指标
				//检验组特殊加减分的指标
			item=document.getElementsByName('G2013203940.ST023$ST02316');
		if(!judgeRadioChecked(item)){
			alert("指标[海关保税监管仓、国家级大型港口]未选择，请选择后提交。");
			return false;
		}
				item=document.getElementsByName('G2013203940.ST023$ST02317');
		if(!judgeRadioChecked(item)){
			alert("指标[交易所指定交割仓库和地、市级以上国家物资储备仓库]未选择，请选择后提交。");
			return false;
		}
		return true;
}	


</script>
<ind:IndTableLayout>

	<ind:IndGroup groupNo="G2013203938" groupName="管理指标" seqno="1">
    			<ind:IndItemText indexNo="ST023$ST02301" indexName="仓储监管经营年限" readonly="true" />
	

				<ind:IndItemRadio indexNo="ST023$ST02302" indexName="资信记录和商业信誉" readonly="false">
							<ind:IndItemRadioOption indValue="0" indDesc="有不良资信纪录"/>
	    						<ind:IndItemRadioOption indValue="1" indDesc="不详"/>
	    						<ind:IndItemRadioOption indValue="2" indDesc="无记录"/>
	    						<ind:IndItemRadioOption indValue="3" indDesc="一般"/>
	    						<ind:IndItemRadioOption indValue="4" indDesc="信用良好、信誉佳"/>
	    						<ind:IndItemRadioOption indValue="5" indDesc="不得分"/>
	    			</ind:IndItemRadio>


				<ind:IndItemRadio indexNo="ST023$ST02303" indexName="有无完善的管理制度" readonly="false">
							<ind:IndItemRadioOption indValue="0" indDesc="管理制度不完整"/>
	    						<ind:IndItemRadioOption indValue="1" indDesc="管理制度比较完善、运作规范"/>
	    						<ind:IndItemRadioOption indValue="2" indDesc="管理严谨完善、运作高效规范"/>
	    						<ind:IndItemRadioOption indValue="3" indDesc="不得分"/>
	    			</ind:IndItemRadio>


				<ind:IndItemRadio indexNo="ST023$ST02304" indexName="网络信息化管理水平" readonly="false">
							<ind:IndItemRadioOption indValue="0" indDesc="高，信息化管理运行两年以上"/>
	    						<ind:IndItemRadioOption indValue="1" indDesc="功能一般，与手工操作结合"/>
	    						<ind:IndItemRadioOption indValue="2" indDesc="低或者无"/>
	    						<ind:IndItemRadioOption indValue="3" indDesc="不得分"/>
	    			</ind:IndItemRadio>


				<ind:IndItemRadio indexNo="ST023$ST02305" indexName="有无稳定的管理团队" readonly="false">
							<ind:IndItemRadioOption indValue="0" indDesc="专业团队、三年内基本稳定"/>
	    						<ind:IndItemRadioOption indValue="1" indDesc="比较专业，基本稳定"/>
	    						<ind:IndItemRadioOption indValue="2" indDesc="一般，特长不明显"/>
	    						<ind:IndItemRadioOption indValue="3" indDesc="不得分"/>
	    			</ind:IndItemRadio>


				<ind:IndItemRadio indexNo="ST023$ST02306" indexName="有无完善的商品检、化验制度和设备" readonly="false">
							<ind:IndItemRadioOption indValue="0" indDesc="完善"/>
	    						<ind:IndItemRadioOption indValue="1" indDesc="一般"/>
	    						<ind:IndItemRadioOption indValue="2" indDesc="无"/>
	    						<ind:IndItemRadioOption indValue="3" indDesc="不得分"/>
	    			</ind:IndItemRadio>


				<ind:IndItemRadio indexNo="ST023$ST02307" indexName="与其他银行的合作年限" readonly="false">
							<ind:IndItemRadioOption indValue="0" indDesc="一年以下"/>
	    						<ind:IndItemRadioOption indValue="1" indDesc="一年以上三年以下"/>
	    						<ind:IndItemRadioOption indValue="2" indDesc=" 三年以上"/>
	    						<ind:IndItemRadioOption indValue="3" indDesc="不得分"/>
	    			</ind:IndItemRadio>


				<ind:IndItemRadio indexNo="ST023$ST02308" indexName="与我行的合作年限" readonly="false">
							<ind:IndItemRadioOption indValue="0" indDesc="一年以下"/>
	    						<ind:IndItemRadioOption indValue="1" indDesc="一年以上三年以下"/>
	    						<ind:IndItemRadioOption indValue="2" indDesc=" 三年及以上"/>
	    						<ind:IndItemRadioOption indValue="3" indDesc="不得分"/>
	    			</ind:IndItemRadio>


				<ind:IndItemRadio indexNo="ST023$ST02309" indexName="管理能力（重点指输出监管能力）" readonly="false">
							<ind:IndItemRadioOption indValue="0" indDesc="强"/>
	    						<ind:IndItemRadioOption indValue="1" indDesc="较强"/>
	    						<ind:IndItemRadioOption indValue="2" indDesc="一般"/>
	    						<ind:IndItemRadioOption indValue="3" indDesc="未曾输出监管"/>
	    						<ind:IndItemRadioOption indValue="4" indDesc="较差"/>
	    						<ind:IndItemRadioOption indValue="5" indDesc="差"/>
	    						<ind:IndItemRadioOption indValue="6" indDesc="很差"/>
	    						<ind:IndItemRadioOption indValue="7" indDesc="不得分"/>
	    			</ind:IndItemRadio>


				<ind:IndItemRadio indexNo="ST023$ST02310" indexName="与银行合作经历（专指仓储监管）" readonly="false">
							<ind:IndItemRadioOption indValue="0" indDesc="积极配合银行质押监管，无投诉"/>
	    						<ind:IndItemRadioOption indValue="1" indDesc="比较积极配合银行监管"/>
	    						<ind:IndItemRadioOption indValue="2" indDesc="配合一般，偶有差错"/>
	    						<ind:IndItemRadioOption indValue="3" indDesc="未曾合作或差错较多"/>
	    						<ind:IndItemRadioOption indValue="4" indDesc="不愿配合银行监管或态度消极"/>
	    						<ind:IndItemRadioOption indValue="5" indDesc="不得分"/>
	    			</ind:IndItemRadio>


				<ind:IndItemRadio indexNo="ST023$ST02311" indexName="年利润总额" readonly="false">
							<ind:IndItemRadioOption indValue="0" indDesc="50万元以下"/>
	    						<ind:IndItemRadioOption indValue="1" indDesc="50-200万元"/>
	    						<ind:IndItemRadioOption indValue="2" indDesc="200-500万元"/>
	    						<ind:IndItemRadioOption indValue="3" indDesc="500万元以上"/>
	    						<ind:IndItemRadioOption indValue="4" indDesc="经营亏损"/>
	    						<ind:IndItemRadioOption indValue="5" indDesc="不得分"/>
	    			</ind:IndItemRadio>


	</ind:IndGroup>

	<ind:IndGroup groupNo="G2013203939" groupName="规模指标" seqno="2">
    			<ind:IndItemText indexNo="ST023$ST02312" indexName="年监管量（吨）" readonly="true" />
	

    			<ind:IndItemText indexNo="ST023$ST02313" indexName="自有仓储场地经营规模" readonly="true" />
	

    			<ind:IndItemText indexNo="ST023$ST02314" indexName="实收资本（万）" readonly="true" />
	

    			<ind:IndItemText indexNo="ST023$ST02315" indexName="企业规模" readonly="true" />
	

	</ind:IndGroup>

	<ind:IndGroup groupNo="G2013203940" groupName="特殊加减分" seqno="3">
				<ind:IndItemRadio indexNo="ST023$ST02316" indexName="海关保税监管仓、国家级大型港口" readonly="false">
							<ind:IndItemRadioOption indValue="0" indDesc="是"/>
	    						<ind:IndItemRadioOption indValue="1" indDesc="否"/>
	    						<ind:IndItemRadioOption indValue="2" indDesc="不得分"/>
	    			</ind:IndItemRadio>


				<ind:IndItemRadio indexNo="ST023$ST02317" indexName="交易所指定交割仓库和地、市级以上国家物资储备仓库" readonly="false">
							<ind:IndItemRadioOption indValue="0" indDesc="是"/>
	    						<ind:IndItemRadioOption indValue="1" indDesc="否"/>
	    						<ind:IndItemRadioOption indValue="2" indDesc="不得分"/>
	    			</ind:IndItemRadio>


	</ind:IndGroup>


</ind:IndTableLayout>
