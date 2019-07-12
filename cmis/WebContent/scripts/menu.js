/*图片预先家载，参数为多个图片文件，使用","隔开，比如：loadImages('/images/1.gif','/images/2.gif')*/

var imgsURLArray=new Array();

imgsURLArray[1]='../images/default/menu1_off_left.gif';

imgsURLArray[2]='../images/default/menu1_off_right.gif';

imgsURLArray[3]='../images/default/menu1_on_left.gif';

imgsURLArray[4]='../images/default/menu1_on_right.gif';


function preloadimages(imgsURLArray){

    var d=document; 

    if(d.images)

    { 

    	if(!d.MM_p) 

    		d.MM_p=new Array();

    	var i,j=d.MM_p.length,a=imgsURLArray; 

    	for(i=0; i<a.length; i++){

    		if (a[i].indexOf("#")!=0){ 

    			d.MM_p[j]=new Image; d.MM_p[j++].src=a[i];

    		}

    	}

    }

}


//update frame height,the minimum heigth is 600 px

function sizeChange() {

var bodyScrollHeight = document.getElementById('infoFrame').contentWindow.document.body.offsetHeight;

var bodyScrollHeight2 = document.getElementById('tranFrame').contentWindow.document.body.offsetHeight;

bodyScrollHeight = (bodyScrollHeight > 600 || bodyScrollHeight2 > 600) ? (bodyScrollHeight > bodyScrollHeight2 ? bodyScrollHeight:bodyScrollHeight2) : 600;

document.getElementById('tranFrame').height = bodyScrollHeight; 

document.getElementById('infoFrame').height = bodyScrollHeight; 

document.getElementById('Page_middle').style.height = bodyScrollHeight; 


//下面的代码是为了调节firefox下不正常显示的问题

//var bodyScrollWidth= document.getElementById("Page_content").offsetWidth- 

//document.getElementById("Page_left").offsetWidth - 50;

//document.getElementById("tranFrame") .width = bodyScrollWidth;

}


function sizeChangeUpper() {//上下结构框架专用

var bodyScrollHeight = document.getElementById('tranFrame').contentWindow.document.body.offsetHeight;

bodyScrollHeight = (bodyScrollHeight > 600) ? bodyScrollHeight : 600;

document.getElementById('tranFrame').height = bodyScrollHeight;  


//下面的代码是为了调节firefox下不正常显示的问题

//var bodyScrollWidth= document.getElementById("Page_content").offsetWidth- 

//document.getElementById("Page_left").offsetWidth - 50;

//document.getElementById("tranFrame") .width = bodyScrollWidth;

}


function menu1Css(aId){

	sElements = document.all.tags("span");

	for (i = 0; i < sElements.length; i++) {

		sElm = sElements(i);

		if (sElm.className == "menu1"||sElm.className=="menu1_on") {

			if(sElm.id == aId){

				sElm.className="menu1_on";

			}

			else{

				sElm.className="menu1";

			}

		}

	}

	aElements = document.all.tags("a");

	for(j = 0; j < aElements.length; j++) {

		aElm = aElements(j);

		if (aElm.className == "menu_lv2_off"||aElm.className=="menu_lv2_on") {			

			aElm.className="menu_lv2_off";			

		}

		if (aElm.className == ""||aElm.className=="menu3_on") {			

			aElm.className="";			

		}

	}				

}


function menu2Css(aId){

	aElements = document.all.tags("a");

	for (i = 0; i < aElements.length; i++) {

		aElm = aElements(i);

		if (aElm.className == "menu_lv2_off"||aElm.className=="menu_lv2_on") {

			if(aElm.id == aId){

				aElm.className="menu_lv2_on";

			}

			else{

				aElm.className="menu_lv2_off";

			}

		}

		if (aElm.className == ""||aElm.className=="menu3_on") {			

			aElm.className="";			

		}		

	}

}


function menu3Css(aId){

	aElements = document.all.tags("a");

	for (i = 0; i < aElements.length; i++) {

		aElm = aElements(i);

		if (aElm.className == ""||aElm.className=="menu3_on") {

			if(aElm.id == aId){

				aElm.className="menu3_on";

			}

			else{

				aElm.className="";

			}

		}	

	}

}


function expandit(aMenuCode) {

	var tdElements = document.all.tags("div");

	for (var i = 0; i < tdElements.length; i++) {

		var tdElmt = tdElements(i);

		if (tdElmt.id != '') {

			var tdArray = tdElmt.id.split('-');

			if (tdArray[1] == aMenuCode) {

				tdElmt.style.display = 'block';

			} else {

				tdElmt.style.display = 'none';

			}

		}

	}

	//parent.mainframe.location.href = 'form.htm';

}


function expandit2(aMenuCode, aParentCode) {

	var tdElements = document.all.tags("a");

	for (var i = 0; i < tdElements.length; i++) {

		var tdElmt = tdElements(i);

		if (tdElmt.id != '') {

			var tdArray = tdElmt.id.split('-');

			if (tdArray[2] > '2') {

				if (tdArray[1] == aMenuCode) {

					menu3.style.display = 'block';

					tdElmt.style.display = 'inline';

				} else {

					menu3.style.display = 'none';

					tdElmt.style.display = 'none';

				}

			}

		}

	}

	//parent.mainframe.location.href = 'form.htm';

}


function submitTran1(aBusinessCode, aLevel,aId) {

	//先显示选中的菜单效果.

	menu1Css(aId);

	var tdElements = document.all.tags("a");

	//for (var i = 0; i < tdElements.length; i++) {

	//	var tdElmt = tdElements(i);

	//	if (tdElmt.id != '') {

	//		var tdArray = tdElmt.id.split('-');

	//		if (tdArray[2] > aLevel) {

	//			tdElmt.style.display = 'none';

	//		}

	//	}

	//}

	

	frmTranDispatch.businessCode.value = aBusinessCode;

	frmTranDispatch.target = "mainframe";

	frmTranDispatch.submit();

}

function submitTran2(aBusinessCode, aLevel,aId) {

	//先显示选中的菜单效果.

	menu2Css(aId);

	//var tdElements = document.all.tags("a");

	//for (var i = 0; i < tdElements.length; i++) {

	//	var tdElmt = tdElements(i);

	//	if (tdElmt.id != '') {

	//		var tdArray = tdElmt.id.split('-');

	//		if (tdArray[2] > aLevel) {

	//			tdElmt.style.display = 'none';

	//		}

	//	}

	//}

	

	frmTranDispatch.businessCode.value = aBusinessCode;

	frmTranDispatch.target = "mainframe";

	frmTranDispatch.submit();

}

function submitTran3(aBusinessCode, aLevel,aId) {

	//先显示选中的菜单效果.

	menu3Css(aId);

	//var tdElements = document.all.tags("a");

	//for (var i = 0; i < tdElements.length; i++) {

	//	var tdElmt = tdElements(i);

	//	if (tdElmt.id != '') {

	//		var tdArray = tdElmt.id.split('-');

	//		if (tdArray[2] > aLevel) {

	//			tdElmt.style.display = 'none';

	//		}

	//	}

	//}

	frmTranDispatch.businessCode.value = aBusinessCode;

	frmTranDispatch.target = "mainframe";

	frmTranDispatch.submit();

}

function quit() {

	if (confirm("是否确定退出！")) {

		top.opener = null;	// IE5.5+

		top.close();

	}

}


//下面的方法只在静态页面中使用


//菜单事件。通过点击更换class实现。


//一级菜单事件，只控制二级菜单的显示

function m1(i) {

	for (n=1;n<29;n++) {

		var menu2a = document.getElementById('m2_'+n);

		menu2a.className='menu2_off';

	}

	for(m=1;m<7;m++) {

		var menu1 = document.getElementById('m1_'+m);

		var menu2 = document.getElementById('menu2_'+m);

		if(i==null){

		menu1.className='menu1_off';

		document.getElementById('menu2_7').style.display='';

		menu2.style.display='none';

		}

		else{

		if (m!=i) {

			menu1.className='menu1_off';

			menu2.style.display='none';

		document.getElementById('menu2_7').style.display='none';

		} else {

			menu1.className='menu1_on';

			menu2.style.display='';

		}}

	}

}


//二级菜单事件。

function m2(i) {

	for (n=1;n<29;n++) {

		var menu2a = document.getElementById('m2_'+n);

		if (n!=i) {

			menu2a.className='menu2_off';

		} else {

			menu2a.className='menu2_on';		

		}

	}

}


//三级菜单事件

function m3(i) {

	for (n=1;n<8;n++) {

		var menu3a = document.getElementById('m3_'+n);

		if (n!=i) {

			menu3a.className='';

		} else {

			menu3a.className='menu3_on';

		}

	}

}


//页面链接去掉点击的虚框

function linkblur() {

	var linka = document.getElementsByTagName("a");

	if(linka!=null) {

		for(i=0;i<linka.length;i++){

			linka[i].onfocus=ablur;

		}

	}

}


function ablur(evt) {

	evt = evt?evt:(window.event?window.event:null);

	var srcElement = evt.srcElement? evt.srcElement : evt.target;

	srcElement.blur();

}


function jump(pagename){

	document.jumpform.action=pagename;

	jumpform.submit();

}


function quickJump(){

    if(document.getElementById('qj').value)

	{document.getElementById('tranFrame').contentWindow.document.location=document.getElementById('qj').value;}

}