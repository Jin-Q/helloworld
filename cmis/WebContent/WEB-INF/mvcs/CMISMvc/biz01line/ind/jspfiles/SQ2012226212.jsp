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
//检验组经营者素质与资信的指标
			item=document.getElementsByName('G2012203895.ST014$ST01401');
		if(!judgeRadioChecked(item)){
			alert("指标[从业经验]未选择，请选择后提交。");
			return false;
		}
				item=document.getElementsByName('G2012203895.ST014$ST01402');
		if(!judgeRadioChecked(item)){
			alert("指标[经营者素质]未选择，请选择后提交。");
			return false;
		}
				item=document.getElementsByName('G2012203895.ST014$ST01403');
		if(!judgeRadioChecked(item)){
			alert("指标[资产实力]未选择，请选择后提交。");
			return false;
		}
	//检验组购销渠道的指标
			item=document.getElementsByName('G2012203896.ST014$ST01409');
		if(!judgeRadioChecked(item)){
			alert("指标[购销渠道]未选择，请选择后提交。");
			return false;
		}
	//检验组企业财务状况的指标
					item=document.getElementsByName('G2012203897.ST014$ST01413');
		if(!judgeRadioChecked(item)){
			alert("指标[短期融资余额与销售收入比]未选择，请选择后提交。");
			return false;
		}
	//检验组信用状况的指标
			item=document.getElementsByName('G2012203898.ST014$ST01417');
		if(!judgeRadioChecked(item)){
			alert("指标[小微企业业主（或主要股东）信用]未选择，请选择后提交。");
			return false;
		}
				item=document.getElementsByName('G2012203898.ST014$ST01418');
		if(!judgeRadioChecked(item)){
			alert("指标[企业银行信用]未选择，请选择后提交。");
			return false;
		}
				item=document.getElementsByName('G2012203898.ST014$ST01419');
		if(!judgeRadioChecked(item)){
			alert("指标[信用环境]未选择，请选择后提交。");
			return false;
		}
		return true;
}	


</script>
<ind:IndTableLayout>

	<ind:IndGroup groupNo="G2012203895" groupName="经营者素质与资信" seqno="1">
				<ind:IndItemRadio indexNo="ST014$ST01401" indexName="从业经验" readonly="false">
							<ind:IndItemRadioOption indValue="0" indDesc="经营者行业经验≥6年"/>
	    						<ind:IndItemRadioOption indValue="1" indDesc="经营者行业经验≥3年，<6年"/>
	    						<ind:IndItemRadioOption indValue="2" indDesc="经营者行业经验≥1，<3年"/>
	    						<ind:IndItemRadioOption indValue="3" indDesc="不得分"/>
	    			</ind:IndItemRadio>


				<ind:IndItemRadio indexNo="ST014$ST01402" indexName="经营者素质" readonly="false">
							<ind:IndItemRadioOption indValue="0" indDesc="经营者谈吐诚实，思路清晰"/>
	    						<ind:IndItemRadioOption indValue="1" indDesc="经营者谈吐本分，思路一般"/>
	    						<ind:IndItemRadioOption indValue="2" indDesc="经营者谈吐与实际明显不符，思路混乱，对企业未来发展无规划或规划混乱"/>
	    						<ind:IndItemRadioOption indValue="3" indDesc="不得分"/>
	    			</ind:IndItemRadio>


				<ind:IndItemRadio indexNo="ST014$ST01403" indexName="资产实力" readonly="false">
							<ind:IndItemRadioOption indValue="0" indDesc="≥5"/>
	    						<ind:IndItemRadioOption indValue="1" indDesc="≥4，<5"/>
	    						<ind:IndItemRadioOption indValue="2" indDesc="≥3，<4"/>
	    						<ind:IndItemRadioOption indValue="3" indDesc="≥2，<3"/>
	    						<ind:IndItemRadioOption indValue="4" indDesc="<2"/>
	    						<ind:IndItemRadioOption indValue="5" indDesc="不得分"/>
	    			</ind:IndItemRadio>


	</ind:IndGroup>

	<ind:IndGroup groupNo="G2012203896" groupName="购销渠道" seqno="2">
				<ind:IndItemRadio indexNo="ST014$ST01409" indexName="购销渠道" readonly="false">
							<ind:IndItemRadioOption indValue="0" indDesc="供货商实力强，质量信誉好，与供货商签订有长期购货合同；有固定的客源，客户满意度高"/>
	    						<ind:IndItemRadioOption indValue="1" indDesc="无明显购销渠道优势"/>
	    						<ind:IndItemRadioOption indValue="2" indDesc="供货商实力一般，无长期购货合同；无固定的客源，客户满意度差"/>
	    						<ind:IndItemRadioOption indValue="3" indDesc="不得分"/>
	    			</ind:IndItemRadio>


	</ind:IndGroup>

	<ind:IndGroup groupNo="G2012203897" groupName="企业财务状况" seqno="3">
    			<ind:IndItemText indexNo="ST014$ST01411" indexName="资产负债率" readonly="true" />
	

    			<ind:IndItemText indexNo="ST014$ST01412" indexName="利息保障倍数" readonly="true" />
	

				<ind:IndItemRadio indexNo="ST014$ST01413" indexName="短期融资余额与销售收入比" readonly="false">
							<ind:IndItemRadioOption indValue="0" indDesc="≤50%"/>
	    						<ind:IndItemRadioOption indValue="1" indDesc=">50%，≤55%"/>
	    						<ind:IndItemRadioOption indValue="2" indDesc=">55%，≤60%"/>
	    						<ind:IndItemRadioOption indValue="3" indDesc=">60%，≤70%"/>
	    						<ind:IndItemRadioOption indValue="4" indDesc=">70%"/>
	    						<ind:IndItemRadioOption indValue="5" indDesc="不得分"/>
	    			</ind:IndItemRadio>


	</ind:IndGroup>

	<ind:IndGroup groupNo="G2012203898" groupName="信用状况" seqno="4">
				<ind:IndItemRadio indexNo="ST014$ST01417" indexName="小微企业业主（或主要股东）信用" readonly="false">
							<ind:IndItemRadioOption indValue="0" indDesc="小微企业业主或主要股东个人偿债意愿强，信用记录良好"/>
	    						<ind:IndItemRadioOption indValue="1" indDesc="小微企业业主或主要股东个人偿债意愿一般，非主观恶意造成逾期信用记录"/>
	    						<ind:IndItemRadioOption indValue="2" indDesc="小微企业业主或主要股东个人偿债意愿低，有不良信用记录"/>
	    						<ind:IndItemRadioOption indValue="3" indDesc="不得分"/>
	    			</ind:IndItemRadio>


				<ind:IndItemRadio indexNo="ST014$ST01418" indexName="企业银行信用" readonly="false">
							<ind:IndItemRadioOption indValue="0" indDesc="企业信用良好，无不良记录，银行贷款均为正常类"/>
	    						<ind:IndItemRadioOption indValue="1" indDesc="企业银行贷款中有关注类贷款，且企业无不良记录，本息无逾期记录"/>
	    						<ind:IndItemRadioOption indValue="2" indDesc="企业曾有本息逾期记录、不良信用记录"/>
	    						<ind:IndItemRadioOption indValue="3" indDesc="不得分"/>
	    			</ind:IndItemRadio>


				<ind:IndItemRadio indexNo="ST014$ST01419" indexName="信用环境" readonly="false">
							<ind:IndItemRadioOption indValue="0" indDesc="符合所在县市级区域经济发展战略，当地小微企业总体信用状况良好"/>
	    						<ind:IndItemRadioOption indValue="1" indDesc="符合所在县市级区域经济发展战略，当地小微企业总体信用状况较好"/>
	    						<ind:IndItemRadioOption indValue="2" indDesc="符合所在县市级区域经济发展战略，当地小微企业总体信用状况尚可"/>
	    						<ind:IndItemRadioOption indValue="3" indDesc="符合所在县市级区域经济发展战略，当地小微企业总体信用状况一般"/>
	    						<ind:IndItemRadioOption indValue="4" indDesc="符合所在县市级区域经济发展战略，当地小微企业总体信用状况较差"/>
	    						<ind:IndItemRadioOption indValue="5" indDesc="不得分"/>
	    			</ind:IndItemRadio>


	</ind:IndGroup>


</ind:IndTableLayout>
