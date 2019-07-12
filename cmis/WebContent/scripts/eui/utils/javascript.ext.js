/**
 * javascript通用扩展方法
 **/

//***************数组扩展********start*******************************
// 数组中删除重复值
Array.prototype.unique = function(){
	this.sort();
	var re=[this[0]];
	for(var i = 1; i < this.length; i++){
		if( this[i] !== re[re.length-1]){
			re.push(this[i]);
		}
	}
	return re;
};
// 数组中删除某值
Array.prototype.remove = function(val) {
	var index = this.indexOf(val);
	if (index > -1) {
		this.splice(index, 1);
	}
};
//***************数组扩展********end***************************************

//***************字符串扩展*******start************************************
//全局替换
String.prototype.replaceAll = function(s1, s2){ 
	return this.replace(new RegExp(s1,"gm"),s2); 
};
//***************字符串扩展*******end**************************************