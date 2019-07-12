
if(!EMP.widget.SoftKeyboard){
	
	EMP.widget.SoftKeyboard  = function(obj){
		
		this.isCaps = false;
		this.keyBoard = null;
		this.header = null;
		this.width = null;
		this.height = null;
		this.dataObj = null;
		this.bodyDiv = "empSoftKeyboard";
		this.obj = obj;
		
		this.init(obj);
	};
	
	EMP.widget.SoftKeyboard.prototype.init = function(obj){
		try{
			this.width = obj.config.keyBoardWidth;
			this.height = obj.config.keyBoardHeight;
			this.header = obj.config.keyBoardTitle;
			
			this.keyBoard = new YAHOO.widget.Panel("keyBoard", { width:this.width, height:this.height, visible:false, draggable:true, close:true });
			if( this.header != null )
				this.keyBoard.setHeader( this.header );
			else
				this.keyBoard.setHeader("EMP Soft keyboard");
			
			document.getElementById(this.bodyDiv ).style.display="";
			this.keyBoard.setBody(document.getElementById(this.bodyDiv ));
			
			this.keyBoard.render(document.body);
		}catch(e)
		{
			alert(e);
		}

	};


	EMP.widget.SoftKeyboard.prototype.showKeyboard = function(){
		var th = this.obj.element;
		var ttop  = th.offsetTop;
		var thei  = th.clientHeight;
		var tleft = th.offsetLeft;
		var ttyp  = th.type;

		while (th = th.offsetParent)
		{
			ttop+=th.offsetTop; 
			tleft+=th.offsetLeft;
		}

		this.keyBoard.moveTo(ttop, tleft);
		this.keyBoard.show();
	};
	
	EMP.widget.SoftKeyboard.prototype.closeKeyboard = function(){
		if( this.keyBoard != null )
			this.keyBoard.hide();
		this.obj.focus();
	};

	EMP.widget.SoftKeyboard.prototype.setKeyValue = function( idx ){
		var keyValue = "`1234567890-=qwertyuiop[]\asdfghjkl;'zxcvbnm,./";
		var keyValue2 = "~~!@#$%^&*()_+QWERTYUIOP{}|ASDFHJKL:\"ZXCVBNM<>?";
		
		var value = this.obj.getValue();
		var length = 0;
		if(value != null){
			length = value.length;
		}
		var maxlength = this.obj.config.maxlength;
		if(!maxlength || length < maxlength ){
			if( this.isCaps ){
				this.obj.setValue(value + keyValue2.charAt(idx));
			}else{
				var nValue = value + keyValue.charAt(idx);
				this.obj.setValue(value + keyValue.charAt(idx));
			}
		}
	};

	EMP.widget.SoftKeyboard.prototype.setNumKeyValue = function( idx ){
		var keyValue = "0123456789";
		var value = this.obj.getValue();
		var length = 0;
		if(value != null){
			length = value.length;
		}
		var maxlength = this.obj.config.maxlength;
		if(!maxlength || length < maxlength ){
			this.obj.setValue(value + keyValue.charAt(idx));
		}
	};


	EMP.widget.SoftKeyboard.prototype.backspace = function(){
		var value = this.obj.getValue();
		var length = 0;
		if(value != null)
			length = value.length;
		if( length > 0){
			this.obj.setValue(value.substring(0, length-1));
		}
	};

	EMP.widget.SoftKeyboard.prototype.setCapsLock = function(srcUrl, capSrcUrl ){
		this.isCaps = !this.isCaps;
		if( this.isCaps ){
			document.getElementById('EMPKeyBoardImg').src=capSrcUrl;
		}else{
			document.getElementById('EMPKeyBoardImg').src=srcUrl;
		}
	};

};



	