Ajax.History = function(opt) {
	this.options = {
			currentHash: '',
            interval: 500,
            iframeSrc: '/blank.html'	
	};
	this.init = function() {
		jQuery.extend(this.options, opt);
		this.callback = this.options.callback || jQuery.noop();
		if (jQuery.browser.msie) {
			var frameOpt = {id:'ajaxHistoryHandler', src:this.options.iframeSrc};
			this.locator = new Ajax.History.Iframe(frameOpt);
		} else {
			this.locator = new Ajax.History.Hash();
		}
		this.currentHash = '';
		if (this.options.currentHash) this.add(this.options.currentHash);
        this.locked = false;
	};
	this.add = function(hash) {
		this.locked = true;
		clearTimeout(this.timer);
        this.currentHash = hash;
        this.locator.setHash(hash);
        this.timer = setTimeout(this.checkHash.bind(this), this.options.interval);

	};
	this.checkHash = function(){
        if(!this.locked){
            var check = this.locator.getHash();
            if(check != this.currentHash){      
                this.callback(check);
                this.currentHash = check;
            }
        } else {
        	this.locked = false;
        }
        this.timer = setTimeout(this.checkHash.bind(this), this.options.interval);
    };
    this.getBookmark = function(){
        return this.locator.getBookmark();
    };
};

Ajax.History.Iframe = function(opt) {
	this.options = {
			id: 'ajaxHistoryHandler',
            src: '/blank.html'
	};
	jQuery.extend(this.options, opt);
	jQuery('body').prepend('<iframe src="'+this.options.src+'" id="'+this.options.id+'"name="'+this.options.id+'" style="display: none;" ></iframe>');
	this.setHash = function(hash){
        try {
        	jQuery("#"+this.options.id).attr("src",this.options.src + '?' + hash);
        }catch(e) {}
    };
    this.getHash = function(){
        try {
            return (document.frames[this.options.id].location.href||'?').split('?')[1];
        }catch(e){ return ''; }
    };
    this.getBookmark = function(){
        try{
            return window.location.href.split('#')[1]||'';
        }catch(e){ return ''; }
    };
};

Ajax.History.Hash = function() {
	this.setHash = function(hash){
        window.location.hash = hash;
    };
    this.getHash = function(){
        return window.location.hash.substring(1)||'';
    };
    this.getBookmark = function(){
        try{
            return window.location.hash.substring(1)||'';
        }catch(e){ return ''; }
    };
};

var ParamParse = {
		valueArray:[],
		makeStackParam:function(str){		
			var param = {};
			var values = str.split("&");
			var elems,val;
			for ( var i = 0; i < values.length; i++) {
				elems = values[i].split("=");
				val = decodeURI(elems[1]);
				val = replaceAll(val,"+"," ");
				val = replaceAll(val,"%40","@");
				val = replaceAll(val,"%3A",":");
				if(elems && elems.length == 2){				
					eval("param."+elems[0]+"= '"+val+"';");
				}
			}
			try{
				return param;
			}finally{
				param = values = elems = null;
			}	
		},
		addParam:function( key, value ){
			ParamParse.valueArray[ParamParse.valueArray.length ] = encodeURIComponent(key) + '=' + encodeURIComponent(value);
		},
		parseStackParam:function(obj){		
			try{
				return Object.toQueryString(obj);
			}finally{
				ParamParse.valueArray = [];
			}
		}
	};

/* TUCSTOM-2244 20161118 S */ 
var PageNum = 0;
function addToHistory(num){
	if(isMsie7 || isMsie8 || isMsie9){
		document.getElementById( "history" ).src = "/history.html?num=" + num;
	}else{
		window.location.hash = "num=" + num;
	}
}
function updateByHistory(num){
	PageNum = num;
	var info = HistoryManager.historyDataStack.get(num);			
	var isNextLoad = true;
	if(info){
		if(HistoryManager.preWorkFunc){				
			isNextLoad = HistoryManager.preWorkFunc(info.url,info.mode,info.param);
		}
		if(isNextLoad   ){
			if(info.mode == "readSub"){
			}else{
				jQuery("#"+info.id).load(info.url,info.param);
			}
		}
	}
	if(isMsie7 || isMsie8 || isMsie9){
		window.location.hash = "num=" + num;
	}
}
function parseNumFromHash(){
	var ret = 0;
	var num = /\d+/.exec( window.location.hash );
	if( num ){
		if(parseInt(num) > 0){
			ret = parseInt(num);
		}else{
			ret = 0;
		}
	}
	return ret;
}
function onTimer(){
	var num = parseNumFromHash();
	if(PageNum != num){
		updateByHistory(num);
	}
}
var HistoryManager = {
		preHistoryCount:-1,
		historyCount:0,
		preWorkFunc:null,
		backProcessFlag:false,
		historyDataStack:new HashMap(),
		loadHistoryManager:function(preFunc){
			HistoryManager.preWorkFunc = preFunc;
			if(isMsie7 || isMsie8 || isMsie9){
				jQuery("body").remove("#history");
				jQuery("body").append(jQuery("<iframe id=\"history\" src=\"\" style=\"display:none;\"></iframe>"));
				var num = parseNumFromHash();
				addToHistory( num );
			}else{
				setInterval(onTimer, 100);
			}
		},
		historyManagerPush:function(id,url,param,mode){		
			var nparam,nmode;
			if(param){			
				nparam = param;
			}else{
				nparam = {};
			}
			if(mode){
				nmode = mode;
			} else {
				nmode = "none";
			}
			PageNum++;
			addToHistory(PageNum);
			
			HistoryManager.historyDataStack.put(
				(PageNum), {"id":id,"url":url,"param":nparam,"mode":nmode}
			);
		}
	};
/* TUCSTOM-2244 20161118 E */