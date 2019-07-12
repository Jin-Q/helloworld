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
//检验组信用历史的指标
			item=document.getElementsByName('G2012203818.ST001$ST00101');
		if(!judgeRadioChecked(item)){
			alert("指标[银行贷款资产状态（五级分类）]未选择，请选择后提交。");
			return false;
		}
				item=document.getElementsByName('G2012203818.ST001$ST00102');
		if(!judgeRadioChecked(item)){
			alert("指标[商业信用]未选择，请选择后提交。");
			return false;
		}
				item=document.getElementsByName('G2012203818.ST001$ST00103');
		if(!judgeRadioChecked(item)){
			alert("指标[个人信用（法人、实际负责人、主要股东）]未选择，请选择后提交。");
			return false;
		}
	//检验组规模实力的指标
							item=document.getElementsByName('G2012203833.ST001$ST00138');
		if(!judgeRadioChecked(item)){
			alert("指标[净利润纳税比率]未选择，请选择后提交。");
			return false;
		}
	//检验组偿债能力的指标
				item=document.getElementsByName('G2012203841.ST001$ST00110');
		if(!judgeRadioChecked(item)){
			alert("指标[或有负债比率]未选择，请选择后提交。");
			return false;
		}
				//检验组现金流量的指标
				item=document.getElementsByName('G2012203822.ST001$ST00114');
		if(!judgeRadioChecked(item)){
			alert("指标[现金净流量]未选择，请选择后提交。");
			return false;
		}
	//检验组盈利能力的指标
			//检验组营运能力的指标
			//检验组发展能力的指标
						item=document.getElementsByName('G2012203843.ST005$ST00510');
		if(!judgeRadioChecked(item)){
			alert("指标[纳税增长率]未选择，请选择后提交。");
			return false;
		}
	//检验组管理水平的指标
			item=document.getElementsByName('G2012203844.ST002$ST00227');
		if(!judgeRadioChecked(item)){
			alert("指标[公司组织结构情况]未选择，请选择后提交。");
			return false;
		}
				item=document.getElementsByName('G2012203844.ST002$ST00228');
		if(!judgeRadioChecked(item)){
			alert("指标[公司内部制度建设、制度实施情况]未选择，请选择后提交。");
			return false;
		}
				item=document.getElementsByName('G2012203844.ST002$ST00229');
		if(!judgeRadioChecked(item)){
			alert("指标[公司员工情况]未选择，请选择后提交。");
			return false;
		}
				item=document.getElementsByName('G2012203844.ST005$ST00501');
		if(!judgeRadioChecked(item)){
			alert("指标[房间入住率情况]未选择，请选择后提交。");
			return false;
		}
				item=document.getElementsByName('G2012203844.ST005$ST00503');
		if(!judgeRadioChecked(item)){
			alert("指标[企业地理环境]未选择，请选择后提交。");
			return false;
		}
	//检验组综合评价的指标
			item=document.getElementsByName('G2012203845.ST002$ST00231');
		if(!judgeRadioChecked(item)){
			alert("指标[领导者素质]未选择，请选择后提交。");
			return false;
		}
				item=document.getElementsByName('G2012203845.ST002$ST00233');
		if(!judgeRadioChecked(item)){
			alert("指标[我行行业信贷政策导向]未选择，请选择后提交。");
			return false;
		}
				item=document.getElementsByName('G2012203845.ST002$ST00234');
		if(!judgeRadioChecked(item)){
			alert("指标[经营的厂房、土地、店铺产权情况]未选择，请选择后提交。");
			return false;
		}
				item=document.getElementsByName('G2012203845.ST001$ST00135');
		if(!judgeRadioChecked(item)){
			alert("指标[生产经营期限]未选择，请选择后提交。");
			return false;
		}
				item=document.getElementsByName('G2012203845.ST005$ST00502');
		if(!judgeRadioChecked(item)){
			alert("指标[星级标准]未选择，请选择后提交。");
			return false;
		}
	//检验组修正/调整项的指标
			item=document.getElementsByName('G2012203826.ST002$ST00236');
		if(!judgeRadioChecked(item)){
			alert("指标[品牌情况]未选择，请选择后提交。");
			return false;
		}
				item=document.getElementsByName('G2012203826.ST002$ST00237');
		if(!judgeRadioChecked(item)){
			alert("指标[是否多营业执照经营]未选择，请选择后提交。");
			return false;
		}
				item=document.getElementsByName('G2012203826.ST002$ST00238');
		if(!judgeRadioChecked(item)){
			alert("指标[财务报表是否经过会计事务所审计]未选择，请选择后提交。");
			return false;
		}
			return true;
}	


</script>
<ind:IndTableLayout>

	<ind:IndGroup groupNo="G2012203818" groupName="信用历史" seqno="1">
				<ind:IndItemRadio indexNo="ST001$ST00101" indexName="银行贷款资产状态（五级分类）" readonly="false">
							<ind:IndItemRadioOption indValue="0" indDesc="无关注、次级、可疑、损失贷款"/>
	    						<ind:IndItemRadioOption indValue="1" indDesc="有关注贷款"/>
	    						<ind:IndItemRadioOption indValue="2" indDesc="有次级、可疑、损失之一"/>
	    						<ind:IndItemRadioOption indValue="3" indDesc="不得分"/>
	    			</ind:IndItemRadio>


				<ind:IndItemRadio indexNo="ST001$ST00102" indexName="商业信用" readonly="false">
							<ind:IndItemRadioOption indValue="0" indDesc="无不良记录"/>
	    						<ind:IndItemRadioOption indValue="1" indDesc="报告期内存在逾期1个月（含）以内的信用记录"/>
	    						<ind:IndItemRadioOption indValue="2" indDesc="存在逾期1个月以上的信用记录；或败诉商业纠纷的；或有拖欠员工工资记录"/>
	    						<ind:IndItemRadioOption indValue="3" indDesc="保全资产类借新还旧贷款"/>
	    						<ind:IndItemRadioOption indValue="4" indDesc="不得分"/>
	    			</ind:IndItemRadio>


				<ind:IndItemRadio indexNo="ST001$ST00103" indexName="个人信用（法人、实际负责人、主要股东）" readonly="false">
							<ind:IndItemRadioOption indValue="0" indDesc="无不良违约纪录"/>
	    						<ind:IndItemRadioOption indValue="1" indDesc="有违约记录6次（含）以下，但现无违约余额"/>
	    						<ind:IndItemRadioOption indValue="2" indDesc="有违约记录7-11次（含），现无违约余额"/>
	    						<ind:IndItemRadioOption indValue="3" indDesc="报告期内有违约记录超过12次（含）以上的或有违约记录且有违约余额在3个月以内"/>
	    						<ind:IndItemRadioOption indValue="4" indDesc="报告期尚有违约余额且逾期3个月以上"/>
	    						<ind:IndItemRadioOption indValue="5" indDesc="不得分"/>
	    			</ind:IndItemRadio>


	</ind:IndGroup>

	<ind:IndGroup groupNo="G2012203833" groupName="规模实力" seqno="2">
    			<ind:IndItemText indexNo="ST003$ST00302" indexName="销售收入(万元)" readonly="true" />
	

    			<ind:IndItemText indexNo="ST003$ST00303" indexName="利润总额(万元)" readonly="true" />
	

    			<ind:IndItemText indexNo="ST003$ST00304" indexName="纳税总额(万元)" readonly="false" />
	

    			<ind:IndItemText indexNo="ST003$ST00305" indexName="实有净资产(万元)" readonly="true" />
	

				<ind:IndItemRadio indexNo="ST001$ST00138" indexName="净利润纳税比率" readonly="false">
							<ind:IndItemRadioOption indValue="0" indDesc="20%(含）以上"/>
	    						<ind:IndItemRadioOption indValue="1" indDesc="15%（含）-20%"/>
	    						<ind:IndItemRadioOption indValue="2" indDesc="10%（含）-15%"/>
	    						<ind:IndItemRadioOption indValue="3" indDesc="5%（含）-10%"/>
	    						<ind:IndItemRadioOption indValue="4" indDesc="5%以下"/>
	    						<ind:IndItemRadioOption indValue="5" indDesc="不得分"/>
	    			</ind:IndItemRadio>


	</ind:IndGroup>

	<ind:IndGroup groupNo="G2012203841" groupName="偿债能力" seqno="3">
    			<ind:IndItemText indexNo="ST002$ST00210" indexName="流动比率" readonly="true" />
	

				<ind:IndItemRadio indexNo="ST001$ST00110" indexName="或有负债比率" readonly="false">
							<ind:IndItemRadioOption indValue="0" indDesc="没有或有负债"/>
	    						<ind:IndItemRadioOption indValue="1" indDesc="低于10%（含）"/>
	    						<ind:IndItemRadioOption indValue="2" indDesc="10%-20%（含）"/>
	    						<ind:IndItemRadioOption indValue="3" indDesc="20%-30%（含）"/>
	    						<ind:IndItemRadioOption indValue="4" indDesc="30%-40%（含）"/>
	    						<ind:IndItemRadioOption indValue="5" indDesc="高于40%"/>
	    						<ind:IndItemRadioOption indValue="6" indDesc="不得分"/>
	    			</ind:IndItemRadio>


    			<ind:IndItemText indexNo="ST001$ST00137" indexName="利息保障倍数" readonly="true" />
	

    			<ind:IndItemText indexNo="ST005$ST00504" indexName="资产负债率" readonly="true" />
	

    			<ind:IndItemText indexNo="ST005$ST00505" indexName="速动比率" readonly="true" />
	

	</ind:IndGroup>

	<ind:IndGroup groupNo="G2012203822" groupName="现金流量" seqno="4">
    			<ind:IndItemText indexNo="ST002$ST00215" indexName="现金流动比率" readonly="true" />
	

				<ind:IndItemRadio indexNo="ST001$ST00114" indexName="现金净流量" readonly="true">
							<ind:IndItemRadioOption indValue="0" indDesc="经营性现金净流量>0，现金净流量>0"/>
	    						<ind:IndItemRadioOption indValue="1" indDesc="经营性现金净流量>0，现金净流量≤0"/>
	    						<ind:IndItemRadioOption indValue="2" indDesc="经营性现金净流量≤0，现金净流量>0"/>
	    						<ind:IndItemRadioOption indValue="3" indDesc="经营性现金净流量≤0，现金净流量≤0"/>
	    						<ind:IndItemRadioOption indValue="4" indDesc="无财报"/>
	    			</ind:IndItemRadio>


	</ind:IndGroup>

	<ind:IndGroup groupNo="G2012203842" groupName="盈利能力" seqno="5">
    			<ind:IndItemText indexNo="ST002$ST00216" indexName="总资产报酬率" readonly="true" />
	

    			<ind:IndItemText indexNo="ST002$ST00218" indexName="净资产收益率" readonly="true" />
	

    			<ind:IndItemText indexNo="ST005$ST00506" indexName="销售利润率" readonly="true" />
	

	</ind:IndGroup>

	<ind:IndGroup groupNo="G2012203770" groupName="营运能力" seqno="6">
    			<ind:IndItemText indexNo="ST002$ST00219" indexName="存货周转率" readonly="true" />
	

    			<ind:IndItemText indexNo="ST002$ST00220" indexName="应收账款周转率" readonly="true" />
	

    			<ind:IndItemText indexNo="ST002$ST00221" indexName="总资产周转率" readonly="true" />
	

	</ind:IndGroup>

	<ind:IndGroup groupNo="G2012203843" groupName="发展能力" seqno="7">
    			<ind:IndItemText indexNo="ST005$ST00507" indexName="销售收入增长" readonly="true" />
	

    			<ind:IndItemText indexNo="ST005$ST00508" indexName="净利润增长率" readonly="true" />
	

    			<ind:IndItemText indexNo="ST005$ST00509" indexName="净资产增长率" readonly="true" />
	

				<ind:IndItemRadio indexNo="ST005$ST00510" indexName="纳税增长率" readonly="false">
							<ind:IndItemRadioOption indValue="0" indDesc="高于10%（含）"/>
	    						<ind:IndItemRadioOption indValue="1" indDesc="8%（含）-10%"/>
	    						<ind:IndItemRadioOption indValue="2" indDesc="5%（含）-8%"/>
	    						<ind:IndItemRadioOption indValue="3" indDesc="2%（含）-5%"/>
	    						<ind:IndItemRadioOption indValue="4" indDesc="低于2%"/>
	    						<ind:IndItemRadioOption indValue="5" indDesc="不得分"/>
	    			</ind:IndItemRadio>


	</ind:IndGroup>

	<ind:IndGroup groupNo="G2012203844" groupName="管理水平" seqno="8">
				<ind:IndItemRadio indexNo="ST002$ST00227" indexName="公司组织结构情况" readonly="false">
							<ind:IndItemRadioOption indValue="0" indDesc="各项均表现良好"/>
	    						<ind:IndItemRadioOption indValue="1" indDesc="有1项以上表现良好"/>
	    						<ind:IndItemRadioOption indValue="2" indDesc="表现一般"/>
	    						<ind:IndItemRadioOption indValue="3" indDesc="不得分"/>
	    			</ind:IndItemRadio>


				<ind:IndItemRadio indexNo="ST002$ST00228" indexName="公司内部制度建设、制度实施情况" readonly="false">
							<ind:IndItemRadioOption indValue="0" indDesc="各项均表现良好"/>
	    						<ind:IndItemRadioOption indValue="1" indDesc="有1项表现良好"/>
	    						<ind:IndItemRadioOption indValue="2" indDesc="表现一般"/>
	    						<ind:IndItemRadioOption indValue="3" indDesc="不得分"/>
	    			</ind:IndItemRadio>


				<ind:IndItemRadio indexNo="ST002$ST00229" indexName="公司员工情况" readonly="false">
							<ind:IndItemRadioOption indValue="0" indDesc="各项均表现良好"/>
	    						<ind:IndItemRadioOption indValue="1" indDesc="有2项以上表现良好"/>
	    						<ind:IndItemRadioOption indValue="2" indDesc="表现一般"/>
	    						<ind:IndItemRadioOption indValue="3" indDesc="不得分"/>
	    			</ind:IndItemRadio>


				<ind:IndItemRadio indexNo="ST005$ST00501" indexName="房间入住率情况" readonly="false">
							<ind:IndItemRadioOption indValue="0" indDesc="各项均表现良好得2分"/>
	    						<ind:IndItemRadioOption indValue="1" indDesc="有2项以上表现良好"/>
	    						<ind:IndItemRadioOption indValue="2" indDesc="表现一般"/>
	    						<ind:IndItemRadioOption indValue="3" indDesc="不得分"/>
	    			</ind:IndItemRadio>


				<ind:IndItemRadio indexNo="ST005$ST00503" indexName="企业地理环境" readonly="false">
							<ind:IndItemRadioOption indValue="0" indDesc="位置在繁华商业区且环境好"/>
	    						<ind:IndItemRadioOption indValue="1" indDesc="位置在一般商业区但经营有特色或位置不在商业区但有稳定的客户群"/>
	    						<ind:IndItemRadioOption indValue="2" indDesc="位置不在商业区且环境差"/>
	    						<ind:IndItemRadioOption indValue="3" indDesc="不得分"/>
	    			</ind:IndItemRadio>


	</ind:IndGroup>

	<ind:IndGroup groupNo="G2012203845" groupName="综合评价" seqno="9">
				<ind:IndItemRadio indexNo="ST002$ST00231" indexName="领导者素质" readonly="false">
							<ind:IndItemRadioOption indValue="0" indDesc="领导者有丰富的管理经验；近3年总资产或销售收入逐年扩大(即本表20项应连续3年取得正值，并超过行业水平）；业绩显著，有良好的社会声誉"/>
	    						<ind:IndItemRadioOption indValue="1" indDesc="一般"/>
	    						<ind:IndItemRadioOption indValue="2" indDesc="其他"/>
	    						<ind:IndItemRadioOption indValue="3" indDesc="不得分"/>
	    			</ind:IndItemRadio>


				<ind:IndItemRadio indexNo="ST002$ST00233" indexName="我行行业信贷政策导向" readonly="false">
							<ind:IndItemRadioOption indValue="0" indDesc="支持"/>
	    						<ind:IndItemRadioOption indValue="1" indDesc="一般"/>
	    						<ind:IndItemRadioOption indValue="2" indDesc="限制、禁止进入"/>
	    						<ind:IndItemRadioOption indValue="3" indDesc="不得分"/>
	    			</ind:IndItemRadio>


				<ind:IndItemRadio indexNo="ST002$ST00234" indexName="经营的厂房、土地、店铺产权情况" readonly="false">
							<ind:IndItemRadioOption indValue="0" indDesc="土地、厂房产权清晰完整，手续完备"/>
	    						<ind:IndItemRadioOption indValue="1" indDesc="产权部分完整"/>
	    						<ind:IndItemRadioOption indValue="2" indDesc="没有办理相关产权自有土地厂房"/>
	    						<ind:IndItemRadioOption indValue="3" indDesc="租用"/>
	    						<ind:IndItemRadioOption indValue="4" indDesc="不得分"/>
	    			</ind:IndItemRadio>


				<ind:IndItemRadio indexNo="ST001$ST00135" indexName="生产经营期限" readonly="false">
							<ind:IndItemRadioOption indValue="0" indDesc="生产经营时间超过3年(含）"/>
	    						<ind:IndItemRadioOption indValue="1" indDesc="低于3年"/>
	    						<ind:IndItemRadioOption indValue="2" indDesc="不得分"/>
	    			</ind:IndItemRadio>


				<ind:IndItemRadio indexNo="ST005$ST00502" indexName="星级标准" readonly="false">
							<ind:IndItemRadioOption indValue="0" indDesc="五星"/>
	    						<ind:IndItemRadioOption indValue="1" indDesc="四星"/>
	    						<ind:IndItemRadioOption indValue="2" indDesc="三星"/>
	    						<ind:IndItemRadioOption indValue="3" indDesc="其它"/>
	    						<ind:IndItemRadioOption indValue="4" indDesc="不得分"/>
	    			</ind:IndItemRadio>


	</ind:IndGroup>

	<ind:IndGroup groupNo="G2012203826" groupName="修正/调整项" seqno="10">
				<ind:IndItemRadio indexNo="ST002$ST00236" indexName="品牌情况" readonly="false">
							<ind:IndItemRadioOption indValue="0" indDesc="具有全国性品牌（如工商局认定的驰名商标，中国名牌等）及其以上"/>
	    						<ind:IndItemRadioOption indValue="1" indDesc="具有地区性品牌（如省级名牌）"/>
	    						<ind:IndItemRadioOption indValue="2" indDesc="具有地方性品牌（如泉州市名牌）"/>
	    						<ind:IndItemRadioOption indValue="3" indDesc="无知名品牌"/>
	    						<ind:IndItemRadioOption indValue="4" indDesc="不得分"/>
	    			</ind:IndItemRadio>


				<ind:IndItemRadio indexNo="ST002$ST00237" indexName="是否多营业执照经营" readonly="false">
							<ind:IndItemRadioOption indValue="0" indDesc="是"/>
	    						<ind:IndItemRadioOption indValue="1" indDesc="不是"/>
	    						<ind:IndItemRadioOption indValue="2" indDesc="不得分"/>
	    			</ind:IndItemRadio>


				<ind:IndItemRadio indexNo="ST002$ST00238" indexName="财务报表是否经过会计事务所审计" readonly="false">
							<ind:IndItemRadioOption indValue="0" indDesc="经会计事务所审计持无保留意见"/>
	    						<ind:IndItemRadioOption indValue="1" indDesc="经会计事务所审计却持保留意见"/>
	    						<ind:IndItemRadioOption indValue="2" indDesc="未经审计"/>
	    						<ind:IndItemRadioOption indValue="3" indDesc="不得分"/>
	    			</ind:IndItemRadio>


    			<ind:IndItemText indexNo="ST002$ST00239" indexName="其它可加减分因素（-3~3之间有效）" readonly="false" />
	

	</ind:IndGroup>


</ind:IndTableLayout>
