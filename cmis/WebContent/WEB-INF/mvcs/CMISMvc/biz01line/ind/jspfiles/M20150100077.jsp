<%@page language="java" contentType="text/html; charset=GBK"%> 
<%@taglib uri="/WEB-INF/c-rt.tld" prefix="c" %>
<%@taglib uri="/WEB-INF/emp.tld" prefix="emp" %>
<%@taglib uri="/WEB-INF/ind.tld" prefix="ind" %>
<link rel="stylesheet" type="text/css" href="<emp:file fileName='styles/ccrTable.css'/>"/>

<script type="text/javascript">
//��֤��ѡ�Ƿ�ѡ��.
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
//���ÿ�����е�ָ���Ƿ���ֵ.���û���򾯸沢����.
var item;
//������������ʷ(ũ)��ָ��
			item=document.getElementsByName('G2015203967.ST025$ST02501');
		if(!judgeRadioChecked(item)){
			alert("ָ��[���д����ʲ�״̬]δѡ����ѡ����ύ��");
			return false;
		}
				item=document.getElementsByName('G2015203967.ST025$ST02502');
		if(!judgeRadioChecked(item)){
			alert("ָ��[��ҵ����]δѡ����ѡ����ύ��");
			return false;
		}
				item=document.getElementsByName('G2015203967.ST025$ST02503');
		if(!judgeRadioChecked(item)){
			alert("ָ��[��������]δѡ����ѡ����ύ��");
			return false;
		}
	//�������ģʵ��(ũ)��ָ��
		//�����鳥ծ����(ũ)��ָ��
							item=document.getElementsByName('G2015203969.ST025$ST025010');
		if(!judgeRadioChecked(item)){
			alert("ָ��[���и�ծ����]δѡ����ѡ����ύ��");
			return false;
		}
	//�������ֽ�����(ũ)��ָ��
			item=document.getElementsByName('G2015203970.ST025$ST02511');
		if(!judgeRadioChecked(item)){
			alert("ָ��[�ֽ�����]δѡ����ѡ����ύ��");
			return false;
		}
		//������ӯ������(ũ)��ָ��
			//������Ӫ������(ũ)��ָ��
			//�����鷢չ����(ũ)��ָ��
			item=document.getElementsByName('G2015203973.ST025$ST02519');
		if(!judgeRadioChecked(item)){
			alert("ָ��[��������������]δѡ����ѡ����ύ��");
			return false;
		}
				item=document.getElementsByName('G2015203973.ST025$ST02520');
		if(!judgeRadioChecked(item)){
			alert("ָ��[������������]δѡ����ѡ����ύ��");
			return false;
		}
				item=document.getElementsByName('G2015203973.ST025$ST02521');
		if(!judgeRadioChecked(item)){
			alert("ָ��[���ʲ�������]δѡ����ѡ����ύ��");
			return false;
		}
	//�������ۺ�����(ũ)��ָ��
			item=document.getElementsByName('G2015203974.ST025$ST02522');
		if(!judgeRadioChecked(item)){
			alert("ָ��[�쵼������]δѡ����ѡ����ύ��");
			return false;
		}
				item=document.getElementsByName('G2015203974.ST025$ST02523');
		if(!judgeRadioChecked(item)){
			alert("ָ��[����ˮƽ]δѡ����ѡ����ύ��");
			return false;
		}
				item=document.getElementsByName('G2015203974.ST025$ST02524');
		if(!judgeRadioChecked(item)){
			alert("ָ��[�������]δѡ����ѡ����ύ��");
			return false;
		}
				item=document.getElementsByName('G2015203974.ST025$ST02525');
		if(!judgeRadioChecked(item)){
			alert("ָ��[������ҵ�Ŵ����ߵ���]δѡ����ѡ����ύ��");
			return false;
		}
				item=document.getElementsByName('G2015203974.ST025$ST02526');
		if(!judgeRadioChecked(item)){
			alert("ָ��[��Ӫ�ĳ��������ء��������]δѡ����ѡ����ύ��");
			return false;
		}
				item=document.getElementsByName('G2015203974.ST025$ST02527');
		if(!judgeRadioChecked(item)){
			alert("ָ��[��Ӫ����]δѡ����ѡ����ύ��");
			return false;
		}
	//����������������(ũ)��ָ��
			item=document.getElementsByName('G2015203975.ST025$ST02528');
		if(!judgeRadioChecked(item)){
			alert("ָ��[������Ȩ��]δѡ����ѡ����ύ��");
			return false;
		}
				item=document.getElementsByName('G2015203975.ST025$ST02529');
		if(!judgeRadioChecked(item)){
			alert("ָ��[����]δѡ����ѡ����ύ��");
			return false;
		}
				item=document.getElementsByName('G2015203975.ST025$ST02530');
		if(!judgeRadioChecked(item)){
			alert("ָ��[��������������ܶ����������µ�����>=10%]δѡ����ѡ����ύ��");
			return false;
		}
				item=document.getElementsByName('G2015203975.ST025$ST02531');
		if(!judgeRadioChecked(item)){
			alert("ָ��[���񱨱�δ�����ʦ���������]δѡ����ѡ����ύ��");
			return false;
		}
		return true;
}	


</script>
<ind:IndTableLayout>

	<ind:IndGroup groupNo="G2015203967" groupName="������ʷ(ũ)" seqno="1">
				<ind:IndItemRadio indexNo="ST025$ST02501" indexName="���д����ʲ�״̬" readonly="false">
							<ind:IndItemRadioOption indValue="0" indDesc="�޹�ע���μ������ɡ���ʧ����"/>
	    						<ind:IndItemRadioOption indValue="1" indDesc="�й�ע����"/>
	    						<ind:IndItemRadioOption indValue="2" indDesc="�дμ������ɡ���ʧ֮һ"/>
	    						<ind:IndItemRadioOption indValue="3" indDesc="���÷�"/>
	    			</ind:IndItemRadio>


				<ind:IndItemRadio indexNo="ST025$ST02502" indexName="��ҵ����" readonly="false">
							<ind:IndItemRadioOption indValue="0" indDesc="�޲�����¼"/>
	    						<ind:IndItemRadioOption indValue="1" indDesc="�������ڴ�������1����(��)���ڵ����ü�¼"/>
	    						<ind:IndItemRadioOption indValue="2" indDesc="��������1�������ϵ����ü�¼;�������ҵ���׵�;������ǷԱ�����ʼ�¼��"/>
	    						<ind:IndItemRadioOption indValue="3" indDesc="��ȫ�ʲ�����»��ɴ���"/>
	    						<ind:IndItemRadioOption indValue="4" indDesc="���÷�"/>
	    			</ind:IndItemRadio>


				<ind:IndItemRadio indexNo="ST025$ST02503" indexName="��������" readonly="false">
							<ind:IndItemRadioOption indValue="0" indDesc="�޲���ΥԼ��¼"/>
	    						<ind:IndItemRadioOption indValue="1" indDesc="��ΥԼ��¼6��(��)����,������ΥԼ���"/>
	    						<ind:IndItemRadioOption indValue="2" indDesc="��ΥԼ��¼7-11��(��),����ΥԼ���"/>
	    						<ind:IndItemRadioOption indValue="3" indDesc="����������ΥԼ��¼����12��(��)���ϵĻ���ΥԼ��¼����ΥԼ�����3��������"/>
	    						<ind:IndItemRadioOption indValue="4" indDesc="����������ΥԼ���������3��������"/>
	    						<ind:IndItemRadioOption indValue="5" indDesc="���÷�"/>
	    			</ind:IndItemRadio>


	</ind:IndGroup>

	<ind:IndGroup groupNo="G2015203968" groupName="��ģʵ��(ũ)" seqno="2">
    			<ind:IndItemText indexNo="ST025$ST02504" indexName="��������(��Ԫ)" readonly="true" />
	

    			<ind:IndItemText indexNo="ST025$ST02505" indexName="��˰�ܶ�(��Ԫ)" readonly="false" />
	

	</ind:IndGroup>

	<ind:IndGroup groupNo="G2015203969" groupName="��ծ����(ũ)" seqno="3">
    			<ind:IndItemText indexNo="ST025$ST02506" indexName="�ʲ���ծ��" readonly="true" />
	

    			<ind:IndItemText indexNo="ST025$ST02507" indexName="��������" readonly="true" />
	

    			<ind:IndItemText indexNo="ST025$ST02508" indexName="�ٶ�����" readonly="true" />
	

    			<ind:IndItemText indexNo="ST025$ST02509" indexName="��Ϣ���ϱ���" readonly="true" />
	

				<ind:IndItemRadio indexNo="ST025$ST025010" indexName="���и�ծ����" readonly="false">
							<ind:IndItemRadioOption indValue="0" indDesc="û�л��и�ծ"/>
	    						<ind:IndItemRadioOption indValue="1" indDesc="����10%(��)"/>
	    						<ind:IndItemRadioOption indValue="2" indDesc="10%-20%(��)"/>
	    						<ind:IndItemRadioOption indValue="3" indDesc="20%-30%(��)"/>
	    						<ind:IndItemRadioOption indValue="4" indDesc="30%-40%(��)"/>
	    						<ind:IndItemRadioOption indValue="5" indDesc="����40%"/>
	    						<ind:IndItemRadioOption indValue="6" indDesc="���÷�"/>
	    			</ind:IndItemRadio>


	</ind:IndGroup>

	<ind:IndGroup groupNo="G2015203970" groupName="�ֽ�����(ũ)" seqno="4">
				<ind:IndItemRadio indexNo="ST025$ST02511" indexName="�ֽ�����" readonly="true">
							<ind:IndItemRadioOption indValue="0" indDesc="��Ӫ���ֽ�����>0,�ֽ�����>0"/>
	    						<ind:IndItemRadioOption indValue="1" indDesc="��Ӫ���ֽ�����>0,�ֽ�������0"/>
	    						<ind:IndItemRadioOption indValue="2" indDesc="��Ӫ���ֽ�������0,�ֽ�����>0"/>
	    						<ind:IndItemRadioOption indValue="3" indDesc="��Ӫ���ֽ�������0,�ֽ�������0"/>
	    						<ind:IndItemRadioOption indValue="4" indDesc="��"/>
	    			</ind:IndItemRadio>


    			<ind:IndItemText indexNo="ST025$ST02512" indexName="�ֽ���������" readonly="true" />
	

	</ind:IndGroup>

	<ind:IndGroup groupNo="G2015203971" groupName="ӯ������(ũ)" seqno="5">
    			<ind:IndItemText indexNo="ST025$ST02513" indexName="���ʲ�������" readonly="true" />
	

    			<ind:IndItemText indexNo="ST025$ST02514" indexName="����������" readonly="true" />
	

    			<ind:IndItemText indexNo="ST025$ST02515" indexName="���ʲ�������" readonly="true" />
	

	</ind:IndGroup>

	<ind:IndGroup groupNo="G2015203972" groupName="Ӫ������(ũ)" seqno="6">
    			<ind:IndItemText indexNo="ST025$ST02516" indexName="�����ת��" readonly="true" />
	

    			<ind:IndItemText indexNo="ST025$ST02517" indexName="Ӧ���˿���ת��" readonly="true" />
	

    			<ind:IndItemText indexNo="ST025$ST02518" indexName="���ʲ���ת��" readonly="true" />
	

	</ind:IndGroup>

	<ind:IndGroup groupNo="G2015203973" groupName="��չ����(ũ)" seqno="7">
				<ind:IndItemRadio indexNo="ST025$ST02519" indexName="��������������" readonly="false">
							<ind:IndItemRadioOption indValue="0" indDesc="����20%(��)"/>
	    						<ind:IndItemRadioOption indValue="1" indDesc="15%(��)-20%"/>
	    						<ind:IndItemRadioOption indValue="2" indDesc="10%(��)-15%"/>
	    						<ind:IndItemRadioOption indValue="3" indDesc="5%(��)-10%"/>
	    						<ind:IndItemRadioOption indValue="4" indDesc="����5%"/>
	    						<ind:IndItemRadioOption indValue="5" indDesc="���÷�"/>
	    			</ind:IndItemRadio>


				<ind:IndItemRadio indexNo="ST025$ST02520" indexName="������������" readonly="false">
							<ind:IndItemRadioOption indValue="0" indDesc="����15%(��)"/>
	    						<ind:IndItemRadioOption indValue="1" indDesc="10%(��)-15%"/>
	    						<ind:IndItemRadioOption indValue="2" indDesc="5%(��)-10%"/>
	    						<ind:IndItemRadioOption indValue="3" indDesc="3%(��)-5%"/>
	    						<ind:IndItemRadioOption indValue="4" indDesc="����3%"/>
	    						<ind:IndItemRadioOption indValue="5" indDesc="���÷�"/>
	    			</ind:IndItemRadio>


				<ind:IndItemRadio indexNo="ST025$ST02521" indexName="���ʲ�������" readonly="false">
							<ind:IndItemRadioOption indValue="0" indDesc="����20%(��)"/>
	    						<ind:IndItemRadioOption indValue="1" indDesc="15%(��)-20%"/>
	    						<ind:IndItemRadioOption indValue="2" indDesc="10%(��)-15%"/>
	    						<ind:IndItemRadioOption indValue="3" indDesc="5%(��)-10%"/>
	    						<ind:IndItemRadioOption indValue="4" indDesc="����5%"/>
	    						<ind:IndItemRadioOption indValue="5" indDesc="���÷�"/>
	    			</ind:IndItemRadio>


	</ind:IndGroup>

	<ind:IndGroup groupNo="G2015203974" groupName="�ۺ�����(ũ)" seqno="8">
				<ind:IndItemRadio indexNo="ST025$ST02522" indexName="�쵼������" readonly="false">
							<ind:IndItemRadioOption indValue="0" indDesc="�쵼���зḻ�Ĺ�����;��3�����ʲ�������������������;ҵ������,�����õ��������"/>
	    						<ind:IndItemRadioOption indValue="1" indDesc="һ��"/>
	    						<ind:IndItemRadioOption indValue="2" indDesc="����"/>
	    						<ind:IndItemRadioOption indValue="3" indDesc="���÷�"/>
	    			</ind:IndItemRadio>


				<ind:IndItemRadio indexNo="ST025$ST02523" indexName="����ˮƽ" readonly="false">
							<ind:IndItemRadioOption indValue="0" indDesc="�ͻ���Ȩ��������֯�ṹ���ơ������ƶȽ�ȫ�����񱨱����"/>
	    						<ind:IndItemRadioOption indValue="1" indDesc="һ��"/>
	    						<ind:IndItemRadioOption indValue="2" indDesc="��"/>
	    						<ind:IndItemRadioOption indValue="3" indDesc="���÷�"/>
	    			</ind:IndItemRadio>


				<ind:IndItemRadio indexNo="ST025$ST02524" indexName="�������" readonly="false">
							<ind:IndItemRadioOption indValue="0" indDesc="�����к�����������,�н����¿ͻ�"/>
	    						<ind:IndItemRadioOption indValue="1" indDesc="�����к���3�����ϻ��н����¿ͻ�֮һ"/>
	    						<ind:IndItemRadioOption indValue="2" indDesc="��"/>
	    						<ind:IndItemRadioOption indValue="3" indDesc="���÷�"/>
	    			</ind:IndItemRadio>


				<ind:IndItemRadio indexNo="ST025$ST02525" indexName="������ҵ�Ŵ����ߵ���" readonly="false">
							<ind:IndItemRadioOption indValue="0" indDesc="֧��"/>
	    						<ind:IndItemRadioOption indValue="1" indDesc="һ��"/>
	    						<ind:IndItemRadioOption indValue="2" indDesc="���ơ���ֹ"/>
	    						<ind:IndItemRadioOption indValue="3" indDesc="���÷�"/>
	    			</ind:IndItemRadio>


				<ind:IndItemRadio indexNo="ST025$ST02526" indexName="��Ӫ�ĳ��������ء��������" readonly="false">
							<ind:IndItemRadioOption indValue="0" indDesc="���ء�������Ȩ���������������걸"/>
	    						<ind:IndItemRadioOption indValue="1" indDesc="��Ȩ��������"/>
	    						<ind:IndItemRadioOption indValue="2" indDesc="û�а�����ز�Ȩ�������س���"/>
	    						<ind:IndItemRadioOption indValue="3" indDesc="����"/>
	    						<ind:IndItemRadioOption indValue="4" indDesc="���÷�"/>
	    			</ind:IndItemRadio>


				<ind:IndItemRadio indexNo="ST025$ST02527" indexName="��Ӫ����" readonly="false">
							<ind:IndItemRadioOption indValue="0" indDesc=" ������Ӫʱ�䳬��5��(��)"/>
	    						<ind:IndItemRadioOption indValue="1" indDesc="����3��(��)δ��5��"/>
	    						<ind:IndItemRadioOption indValue="2" indDesc="����3��"/>
	    						<ind:IndItemRadioOption indValue="3" indDesc="���÷�"/>
	    			</ind:IndItemRadio>


	</ind:IndGroup>

	<ind:IndGroup groupNo="G2015203975" groupName="����������(ũ)" seqno="9">
				<ind:IndItemRadio indexNo="ST025$ST02528" indexName="������Ȩ��" readonly="false">
							<ind:IndItemRadioOption indValue="0" indDesc="���ڵ���6��Ԫ"/>
	    						<ind:IndItemRadioOption indValue="1" indDesc="3(��)-6��Ԫ"/>
	    						<ind:IndItemRadioOption indValue="2" indDesc="1-3��Ԫ"/>
	    						<ind:IndItemRadioOption indValue="3" indDesc="С�ڵ���1��Ԫ"/>
	    						<ind:IndItemRadioOption indValue="4" indDesc="���÷�"/>
	    			</ind:IndItemRadio>


				<ind:IndItemRadio indexNo="ST025$ST02529" indexName="����" readonly="false">
							<ind:IndItemRadioOption indValue="0" indDesc="�����ܶ� ũҵ>=3��Ԫ"/>
	    						<ind:IndItemRadioOption indValue="1" indDesc="��"/>
	    						<ind:IndItemRadioOption indValue="2" indDesc="���÷�"/>
	    			</ind:IndItemRadio>


				<ind:IndItemRadio indexNo="ST025$ST02530" indexName="��������������ܶ����������µ�����>=10%" readonly="false">
							<ind:IndItemRadioOption indValue="0" indDesc="��"/>
	    						<ind:IndItemRadioOption indValue="1" indDesc="��"/>
	    						<ind:IndItemRadioOption indValue="2" indDesc="���÷�"/>
	    			</ind:IndItemRadio>


				<ind:IndItemRadio indexNo="ST025$ST02531" indexName="���񱨱�δ�����ʦ���������" readonly="false">
							<ind:IndItemRadioOption indValue="0" indDesc="��"/>
	    						<ind:IndItemRadioOption indValue="1" indDesc="��"/>
	    						<ind:IndItemRadioOption indValue="2" indDesc="���÷�"/>
	    			</ind:IndItemRadio>


	</ind:IndGroup>


</ind:IndTableLayout>
