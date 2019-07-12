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
//检验组股东的指标
			item=document.getElementsByName('G2012203899.ST011$ST01101');
		if(!judgeRadioChecked(item)){
			alert("指标[经济实力]未选择，请选择后提交。");
			return false;
		}
				item=document.getElementsByName('G2012203899.ST011$ST01102');
		if(!judgeRadioChecked(item)){
			alert("指标[信用情况]未选择，请选择后提交。");
			return false;
		}
	//检验组管理层的指标
			item=document.getElementsByName('G2012203900.ST011$ST01103');
		if(!judgeRadioChecked(item)){
			alert("指标[品质]未选择，请选择后提交。");
			return false;
		}
				item=document.getElementsByName('G2012203900.ST011$ST01104');
		if(!judgeRadioChecked(item)){
			alert("指标[从业经验]未选择，请选择后提交。");
			return false;
		}
				item=document.getElementsByName('G2012203900.ST011$ST01105');
		if(!judgeRadioChecked(item)){
			alert("指标[经营能力]未选择，请选择后提交。");
			return false;
		}
	//检验组条件 的指标
			item=document.getElementsByName('G2012203904.ST011$ST01106');
		if(!judgeRadioChecked(item)){
			alert("指标[政策支持]未选择，请选择后提交。");
			return false;
		}
				item=document.getElementsByName('G2012203904.ST011$ST01107');
		if(!judgeRadioChecked(item)){
			alert("指标[信用环境]未选择，请选择后提交。");
			return false;
		}
	//检验组发展前景的指标
			item=document.getElementsByName('G2012203917.ST020$ST02001');
		if(!judgeRadioChecked(item)){
			alert("指标[盈利能力]未选择，请选择后提交。");
			return false;
		}
				item=document.getElementsByName('G2012203917.ST020$ST02002');
		if(!judgeRadioChecked(item)){
			alert("指标[客户群体]未选择，请选择后提交。");
			return false;
		}
				item=document.getElementsByName('G2012203917.ST011$ST01108');
		if(!judgeRadioChecked(item)){
			alert("指标[行业投向]未选择，请选择后提交。");
			return false;
		}
	//检验组实际资产的指标
		//检验组其它可加减分因素的指标
		return true;
}	


</script>
<ind:IndTableLayout>

	<ind:IndGroup groupNo="G2012203899" groupName="股东" seqno="1">
				<ind:IndItemRadio indexNo="ST011$ST01101" indexName="经济实力" readonly="false">
							<ind:IndItemRadioOption indValue="0" indDesc="母公司或控制投资个人为连续两年盈利的上市、拟上市企业、行业龙头企业或上述企业主要控制人，或母公司、控制公司为国资背景"/>
	    						<ind:IndItemRadioOption indValue="1" indDesc="母公司或控制投资个人为本地有影响力的企业或个人的，或控股在50%以上的控制企业在我行或全国性银行信用等级评定AA级（含）以上"/>
	    						<ind:IndItemRadioOption indValue="2" indDesc="母公司、实际控制人影响力一般"/>
	    						<ind:IndItemRadioOption indValue="3" indDesc="母公司、实际控制人有不良商业纪录的或行业口碑不佳"/>
	    						<ind:IndItemRadioOption indValue="4" indDesc="不得分"/>
	    			</ind:IndItemRadio>


				<ind:IndItemRadio indexNo="ST011$ST01102" indexName="信用情况" readonly="false">
							<ind:IndItemRadioOption indValue="0" indDesc="无不良（含银行信业、商业信用）记录，业界反映良好"/>
	    						<ind:IndItemRadioOption indValue="1" indDesc="无不良记录，但业界口碑不从考证"/>
	    						<ind:IndItemRadioOption indValue="2" indDesc="有不良记录，当前已无余额"/>
	    						<ind:IndItemRadioOption indValue="3" indDesc="有不良记录，当前尚有余额或未决商业"/>
	    						<ind:IndItemRadioOption indValue="4" indDesc="不得分"/>
	    			</ind:IndItemRadio>


	</ind:IndGroup>

	<ind:IndGroup groupNo="G2012203900" groupName="管理层" seqno="2">
				<ind:IndItemRadio indexNo="ST011$ST01103" indexName="品质" readonly="false">
							<ind:IndItemRadioOption indValue="0" indDesc="艰苦创业、谈吐诚实、社会反映良好"/>
	    						<ind:IndItemRadioOption indValue="1" indDesc="一般"/>
	    						<ind:IndItemRadioOption indValue="2" indDesc="行踪不定、背景繁杂"/>
	    						<ind:IndItemRadioOption indValue="3" indDesc="不得分"/>
	    			</ind:IndItemRadio>


				<ind:IndItemRadio indexNo="ST011$ST01104" indexName="从业经验" readonly="false">
							<ind:IndItemRadioOption indValue="0" indDesc="从业经验>=10年"/>
	    						<ind:IndItemRadioOption indValue="1" indDesc="从业经验>=5年"/>
	    						<ind:IndItemRadioOption indValue="2" indDesc="从业经验>=4年"/>
	    						<ind:IndItemRadioOption indValue="3" indDesc="4年>从业经验>= 3年"/>
	    						<ind:IndItemRadioOption indValue="4" indDesc="3年>从业经验>= 2年"/>
	    						<ind:IndItemRadioOption indValue="5" indDesc="2年>从业经验"/>
	    						<ind:IndItemRadioOption indValue="6" indDesc="曾经经营的企业发生关、停、并、破产"/>
	    						<ind:IndItemRadioOption indValue="7" indDesc="不得分"/>
	    			</ind:IndItemRadio>


				<ind:IndItemRadio indexNo="ST011$ST01105" indexName="经营能力" readonly="false">
							<ind:IndItemRadioOption indValue="0" indDesc="管理规范、经营稳健、思路清晰"/>
	    						<ind:IndItemRadioOption indValue="1" indDesc="管理一般、经营一般"/>
	    						<ind:IndItemRadioOption indValue="2" indDesc="管理混乱、领导层经营思想不统一"/>
	    						<ind:IndItemRadioOption indValue="3" indDesc="不得分"/>
	    			</ind:IndItemRadio>


	</ind:IndGroup>

	<ind:IndGroup groupNo="G2012203904" groupName="条件 " seqno="3">
				<ind:IndItemRadio indexNo="ST011$ST01106" indexName="政策支持" readonly="false">
							<ind:IndItemRadioOption indValue="0" indDesc="支持力度大"/>
	    						<ind:IndItemRadioOption indValue="1" indDesc="一般"/>
	    						<ind:IndItemRadioOption indValue="2" indDesc="较差"/>
	    						<ind:IndItemRadioOption indValue="3" indDesc="如果受到政策限制"/>
	    						<ind:IndItemRadioOption indValue="4" indDesc="不得分"/>
	    			</ind:IndItemRadio>


				<ind:IndItemRadio indexNo="ST011$ST01107" indexName="信用环境" readonly="false">
							<ind:IndItemRadioOption indValue="0" indDesc="贷款不良率小于5%（不含）"/>
	    						<ind:IndItemRadioOption indValue="1" indDesc="贷款不良率5%（含）-10%（不含）"/>
	    						<ind:IndItemRadioOption indValue="2" indDesc="贷款不良率10%（含）-20%（不含）"/>
	    						<ind:IndItemRadioOption indValue="3" indDesc="20%（含）及其以上"/>
	    						<ind:IndItemRadioOption indValue="4" indDesc="不得分"/>
	    			</ind:IndItemRadio>


	</ind:IndGroup>

	<ind:IndGroup groupNo="G2012203917" groupName="发展前景" seqno="4">
				<ind:IndItemRadio indexNo="ST020$ST02001" indexName="盈利能力" readonly="false">
							<ind:IndItemRadioOption indValue="0" indDesc="很好"/>
	    						<ind:IndItemRadioOption indValue="1" indDesc="较好"/>
	    						<ind:IndItemRadioOption indValue="2" indDesc="一般"/>
	    						<ind:IndItemRadioOption indValue="3" indDesc="较差"/>
	    						<ind:IndItemRadioOption indValue="4" indDesc="不得分"/>
	    			</ind:IndItemRadio>


				<ind:IndItemRadio indexNo="ST020$ST02002" indexName="客户群体" readonly="false">
							<ind:IndItemRadioOption indValue="0" indDesc="消费能力强、集中度不大"/>
	    						<ind:IndItemRadioOption indValue="1" indDesc="消费能力强、集中度大"/>
	    						<ind:IndItemRadioOption indValue="2" indDesc="其他"/>
	    						<ind:IndItemRadioOption indValue="3" indDesc="不得分"/>
	    			</ind:IndItemRadio>


				<ind:IndItemRadio indexNo="ST011$ST01108" indexName="行业投向" readonly="false">
							<ind:IndItemRadioOption indValue="0" indDesc="积极支持"/>
	    						<ind:IndItemRadioOption indValue="1" indDesc="支持"/>
	    						<ind:IndItemRadioOption indValue="2" indDesc="维持"/>
	    						<ind:IndItemRadioOption indValue="3" indDesc="限制进入或不得进入"/>
	    						<ind:IndItemRadioOption indValue="4" indDesc="不得分"/>
	    			</ind:IndItemRadio>


	</ind:IndGroup>

	<ind:IndGroup groupNo="G2012203902" groupName="实际资产" seqno="5">
    			<ind:IndItemText indexNo="ST011$ST01111" indexName="实收资本（万）" readonly="true" />
	

    			<ind:IndItemText indexNo="ST011$ST01112" indexName="固定资产购置情况" readonly="true" />
	

	</ind:IndGroup>

	<ind:IndGroup groupNo="G2012203903" groupName="其它可加减分因素" seqno="6">
    			<ind:IndItemText indexNo="ST011$ST01113" indexName="其它可减分因素" readonly="false" />
	

	</ind:IndGroup>


</ind:IndTableLayout>
