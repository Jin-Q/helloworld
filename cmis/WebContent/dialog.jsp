<%@page language="java" contentType="text/html; charset=UTF-8"%>
<div id='maskDiv' style="position:absolute;left:0;top:0;width:1000;height:500;display:none;background:#FFFFFF;">
<iframe style="position:absolute;width:100%;height:100%;_filter:alpha(opacity=0);opacity=0;border-style:none;"></iframe> 
</div> 
<div id='dialogDiv' style="position:absolute;left:200;top:50;width:523;height:407;display:none;" >
  <div id='dialogShadowDiv' style="left:8;top:10;width:495;height:405;background:#CCCCCC;position:absolute;" >
  </div>
 <div id='dialogMainDiv' style="left:0;top:0;width:510;height:390;background:#FFFFFF;position:absolute;">
   <table id='dialogMainTable' height="400" width="510" class="emp_table" >
    <tr height='16' class="emp_table_title">
      <td id='dialogTitle' height='16' width='95%' >对话框</td>
      <td width='5%' onclick='javascript:EDialog.prototype.closeDialog();' style='cursor:hand' align='left'>
        <img src="scripts/yui/container/assets/close12_1.gif"></img></td>
    </tr>
    <tr  height='6'><td colspan='2'></td></tr>
    <tr id='dialogMainTr' height='378'>
      <td id ='dialogMainTd' height='378' colspan='2'><iframe id="dialogiF" name="dialogiF" src=""  height='100%' width="510" Frameborder=No Border=0 Marginwidth=0 Marginheight=0  ></iframe></td>
    </tr>
   </table>
  </div>
</div>

<script language='javascript'>

function EDialog() {
	
	this.width = 500;
	this.height = 400;
	this.top = 80;
	this.left = 200;
	this.title = '对话框';
	this.src = '#';
}

EDialog.prototype.resizeDialog = function(_top, _left, _width, _heigh){
	
	document.all("dialogDiv").style.top = _top;
	document.all("dialogDiv").style.left = _left;
	document.all("dialogDiv").style.width = _width;
	document.all("dialogDiv").style.heigh = _heigh ;
    
    document.all("dialogMainDiv").style.width = parseInt(_width) - 10 ;
    document.all("dialogMainDiv").style.heigh = parseInt(_heigh) - 10 ;

    document.all("dialogShadowDiv").style.width = parseInt(_width) - 5 ;
    document.all("dialogShadowDiv").style.heigh = parseInt(_heigh) + 5  ;

    document.all("dialogMainTable").style.width = parseInt(_width) - 10 ;
    document.all("dialogMainTable").style.heigh = parseInt(_heigh) - 10 ;

    document.all("dialogiF").style.width = parseInt(_width) - 10 ;
    
    document.all("dialogMainTr").style.heigh = parseInt(_heigh) - 10 - 22;
    document.all("dialogMainTd").style.heigh = parseInt(_heigh) - 10 - 22;
    
	document.all("dialogDiv").style.display='block';
}
/**
 * _title 对话框标题
 * _url 对话框内显示页面的URL
 * _top 对话框位置 上
 * _left 对话框位置 左
 * _width 对话框宽
 * _heigh 对话框高
 */
EDialog.prototype.showDialog = function(_title, _url, _top, _left, _width, _heigh){

	this.width = _width;
	this.height = _heigh;
	this.top = _top;
	this.left = _left;
	this.title = _title;
	this.src = _url;
	
	this.resizeDialog(_top, _left, _width, _heigh);
	document.all("dialogTitle").innerText = _title;
	document.all("dialogiF").src = _url;
	document.all("dialogShadowDiv").style.filter = "alpha(opacity=50)";
	
	document.all("maskDiv").style.display='block';
	document.all("maskDiv").style.filter = "alpha(opacity=50)";

	ECDrag.initbind('dialogDiv','dialogTitle');
}

/**
 * 关闭对话框
 * 注：其将自动触发对话框页面的onunload事件
 */
EDialog.prototype.closeDialog = function(){

	document.all("dialogDiv").style.display='none';
	document.all("dialogiF").src = '#';
	document.all("maskDiv").style.display='none';
	document.all("maskDiv").style.filter = "";
}

function ECDrag(){
}

  ECDrag.iex = 0;
  ECDrag.iey = 0;
  ECDrag.tempx = 0;
  ECDrag.tempy = 0;
  ECDrag.dragapproved=false ;
  ECDrag.dropobj = '';

/**
 * 将待移动的对象绑定事件
 *  _dragobj  待移动对象名
 *  _handle   用于移动对象的对象（可选）
 */
ECDrag.initbind = function(_dragobj,_handle){
  
  if(_handle == null){
		 _handle = _dragobj;
	 }
   
	 document.all[_handle].onmousedown = ECDrag.initdrag; 
	 document.all[_dragobj].style.position = 'absolute';
	 document.all[_handle].uiid = _dragobj;

	 document.all[_handle].onmousemove =  function (){
	   this.style.cursor = 'hand';
	 };	
}

/**
 * 初始移动
 */
ECDrag.initdrag = function(){

  ECDrag.dropobj = this.uiid; 
  ECDrag.dragapproved=true;
	  
  ECDrag.iex=event.clientX ;
  ECDrag.iey=event.clientY ; 	  
	  
  ECDrag.tempx=document.all[ECDrag.dropobj].style.pixelLeft ;
  ECDrag.tempy=document.all[ECDrag.dropobj].style.pixelTop ;
   	  
  document.onmousemove=ECDrag.startmove ;
  document.onmouseup=ECDrag.stopmove;
   
  return false;
}

/**
 * 开始移动
 */
ECDrag.startmove = function(){
window.status='move ...';
 if (ECDrag.dragapproved==true){ 

   var _x = ECDrag.tempx+event.clientX-ECDrag.iex ; 
   var _y = ECDrag.tempy+event.clientY-ECDrag.iey ;

   if(_x < 0) _x=0;
	if(_y < 0) _y=0;

   document.all[ECDrag.dropobj].style.pixelLeft = _x;
   document.all[ECDrag.dropobj].style.pixelTop = _y;

   window.status= 'start ' + ECDrag.dropobj + ' move (X:' + _x+ ' Y:' + _y + ')... ';
 	return false ;
 } 
}

/**
 * 停止移动
 */
ECDrag.stopmove =function(){ 
 window.status= 'stop move ... ' ;
 dragapproved = false;

 document.onmousemove = null;
 document.onmouseup= null;
} 
 

</script>