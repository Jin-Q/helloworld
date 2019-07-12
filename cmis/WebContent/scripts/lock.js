//====================================锁屏函数===============================
function IsDocEle(layid) { 
return document.getElementById(layid) || false;
}

 
function lockScreen() {
 

var overlayID="overlay";
var msgID = "overlayMsg";
if (IsDocEle(overlayID)) document.removeChild(IsDocEle(overlayID));
if (IsDocEle(msgID)) document.removeChild(IsDocEle(msgID));

var scrolltop = window.pageYOffset || document.documentElement.scrollTop || document.body.scrollTop || 0;

var _clientheight=0;

//ie FF 在有DOCTYPE时各有区别 
_clientheight = Math.min(document.body.clientHeight , document.documentElement.clientHeight);
if(_clientheight==0)
_clientheight= Math.max(document.body.clientHeight , document.documentElement.clientHeight);

var _clientwidth= document.documentElement.clientWidth || document.body.clientWidth;
//整个页面的高度
var _pageheight = Math.max(document.body.scrollHeight,document.documentElement.scrollHeight);

var msgtop = (scrolltop+(_clientheight-100)/2)+"px";
var msgleft = (_clientwidth-300)/2+"px";
 
// 锁屏图层
 
var overlay = (document.all) ? document.createElement('<div id="overlay" >') : document.createElement('div');

overlay.id = overlayID;
overlay.style.position = "absolute";
overlay.style.zIndex = "998";
overlay.style.width = _clientwidth + "px";
overlay.style.height = _pageheight + "px";
overlay.style.top = "0px";
overlay.style.left = "0px";
overlay.style.background = "#777";
overlay.style.filter ="progid:DXImageTransform.Microsoft.Alpha(style=3,opacity=25,finishOpacity=75";
overlay.style.opacity = "0.40";

document.body.appendChild(overlay);



//消息显示
var newDiv = (document.all) ? document.createElement('<div id="overlayMsg" >') : document.createElement('div');
 
newDiv.id = msgID;
newDiv.style.position = "absolute";
newDiv.style.zIndex = "999";
newDiv.style.width = "215px";
newDiv.style.height = "48px";
newDiv.style.top = msgtop;
newDiv.style.left =msgleft; // 屏幕居中
newDiv.style.background = "#EFEFEF";
newDiv.style.border = "1px solid #0000FF";
newDiv.style.padding = "5px";
newDiv.innerHTML = "<img src='images/wait.gif'><b>\u8bf7\u7a0d\u7b49\uff0c\u7cfb\u7edf\u6b63\u5728\u5904\u7406\u4e2d......</b>";

document.body.appendChild(newDiv);


}
// 关闭锁屏
// 锁屏用的参数strlist：下拉框的名称组成的字符串。多个以“;”隔开，如：A;B;C
function closeLock()
{
var overlayID="overlay";
var msgID = "overlayMsg";

document.body.removeChild(IsDocEle(overlayID));
document.body.removeChild(IsDocEle(msgID));
 
return false;

}

//=====================锁屏结束===========================================================
