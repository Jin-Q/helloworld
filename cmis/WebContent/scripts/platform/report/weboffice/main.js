<SCRIPT language=javascript event=NotifyCtrlReady for=WebOffice1>
/****************************************************
*
*	在装载完Weboffice(执行<object>...</object>)
*	控件后执行 "WebOffice1_NotifyCtrlReady"方法
*
****************************************************/
    WebOffice_Event_Flash("NotifyCtrlReady");
	WebOffice1_NotifyCtrlReady()
</SCRIPT>

<SCRIPT language=javascript event=NotifyWordEvent(eventname) for=WebOffice1>
<!--
WebOffice_Event_Flash("NotifyWordEvent");
 WebOffice1_NotifyWordEvent(eventname);
 
//-->
</SCRIPT>

<SCRIPT language=javascript event=NotifyToolBarClick(iIndex) for=WebOffice1>
<!--
  WebOffice_Event_Flash("NotifyToolBarClick");
 WebOffice1_NotifyToolBarClick(iIndex);
//-->
</SCRIPT>

<SCRIPT language=javascript>
/****************************************************
*
*		控件初始化WebOffice方法
*
****************************************************/
function WebOffice1_NotifyCtrlReady() {
	document.all.WebOffice1.SetWindowText("授权XX(可通过接口自定义)", 0);
	document.all.WebOffice1.OptionFlag |= 128;
	// 新建文档
	//document.all.WebOffice1.LoadOriginalFile("", "doc");
	spnWebOfficeInfo.innerText="----   您电脑上安装的WebOffice版本为:V" + document.all.WebOffice1.GetOcxVersion() +"\t\t\t本实例是根据版本V6044编写";
}
var flag=false;
function menuOnClick(id){
	var id=document.getElementById(id);
	var dis=id.style.display;
	if(dis!="none"){
		id.style.display="none";
		
	}else{
		id.style.display="block";
	}
}
/****************************************************
*
*		接收office事件处理方法
*
****************************************************/
var vNoCopy = 0;
var vNoPrint = 0;
var vNoSave = 0;
var vClose=0;
function no_copy(){
	vNoCopy = 1;
}
function yes_copy(){
	vNoCopy = 0;
}


function no_print(){
	vNoPrint = 1;
}
function yes_print(){
	vNoPrint = 0;
}


function no_save(){
	vNoSave = 1;
}
function yes_save(){
	vNoSave = 0;
}
function EnableClose(flag){
 vClose=flag;
}
function CloseWord(){
	
  document.all.WebOffice1.CloseDoc(0); 
}

function WebOffice1_NotifyWordEvent(eventname) {
	if(eventname=="DocumentBeforeSave"){
		if(vNoSave){
			document.all.WebOffice1.lContinue = 0;
			alert("此文档已经禁止保存");
		}else{
			document.all.WebOffice1.lContinue = 1;
		}
	}else if(eventname=="DocumentBeforePrint"){
		if(vNoPrint){
			document.all.WebOffice1.lContinue = 0;
			alert("此文档已经禁止打印");
		}else{
			document.all.WebOffice1.lContinue = 1;
		}
	}else if(eventname=="WindowSelectionChange"){
		if(vNoCopy){
			document.all.WebOffice1.lContinue = 0;
			//alert("此文档已经禁止复制");
		}else{
			document.all.WebOffice1.lContinue = 1;
		}
	}else   if(eventname =="DocumentBeforeClose"){
	    if(vClose==0){
	    	document.all.WebOffice1.lContinue=0;
	    } else{
	    	//alert("word");
		    document.all.WebOffice1.lContinue = 1;
		  }
 }
	//alert(eventname); 
}
function dd(){

	document.all.WebOffice1.FullScreen=0;

}

//------------------------------------------

/*  以下的JavaScript为采用ActiveDocument的方式直接操作VBA，向DOC文档的书签中加入一个图片。
function AddPicture(strMarkName,strBmpPath,vType)
在书签位置插入图片,
strMarkName 书签的名称
strBmpPath  图片的路径
vType       插入后的图片的样式，5为浮动在文字上面
AddPicture("test1","d:\\111.bmp",5);
AddPicture("test1","d:\\111.bmp",128);
AddPicture("YUHQ_T_1","C:\\image_1.png",128);

*/

function AddPicture(strMarkName,strBmpPath,vType)
{
	
//定义一个对象，用来存储ActiveDocument对象
         var obj;
         obj = new Object(document.all.WebOffice1.GetDocumentObject());
         if(obj !=null){
               var pBookMarks;
// VAB接口获取书签集合
               pBookMarks = obj.Bookmarks;
               alert("1:"+pBookMarks);
               var pBookM;
// VAB接口获取书签strMarkName
               pBookM = pBookMarks(strMarkName);
               alert("2:"+pBookM);
               var pRange;
// VAB接口获取书签strMarkName的Range对象
               pRange = pBookM.Range;
               var pRangeInlines; 
// VAB接口获取书签strMarkName的Range对象的InlineShapes对象
               pRangeInlines = pRange.InlineShapes;
               var pRangeInline; 
// VAB接口通过InlineShapes对象向文档中插入图片
               pRangeInline = pRangeInlines.AddPicture(strBmpPath);  
//设置图片的样式，5为浮动在文字上面
               pRangeInline.ConvertToShape().WrapFormat.TYPE = vType;
               delete obj; 
   }
}
/**

向指定的标签位置添加文本内容

strMarkName:书签内容
strText：文本内容

*/
function AddText(strMarkName,strText){
	
//定义一个对象，用来存储ActiveDocument对象
         var obj;
         obj = new Object(document.all.WebOffice1.GetDocumentObject());
         if(obj !=null){
                alert(strMarkName);
               var pBookMarks;
// VAB接口获取书签集合
               pBookMarks = obj.Bookmarks;
               //alert("1:"+pBookMarks);
               var pBookM;
// VAB接口获取书签strMarkName
               pBookM = pBookMarks(strMarkName);
               alert("2:"+pBookM);
               var pRange;
// VAB接口获取书签strMarkName的Range对象
               pRange = pBookM.Range;
               pRange.InsertAfter(strText)
   }
}




//-------------------------------------------
</SCRIPT>