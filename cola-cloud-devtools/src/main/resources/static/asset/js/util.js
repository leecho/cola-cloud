window.Util = {
	Boolean : {
		parse : function(value){
			if(value == 'true'){
				return true;
			}
			return false;
		}
	},
	Array : {
		copyProperty : function(array,property,filter){
			if(!angular.isArray(array)) return ;
			if(!property) return ;
			
			var result = [];
			if(angular.isFunction(array.forEach)){
				array.forEach(function(item,index){
					if(angular.isFunction(filter)){
						if(filter(item) === true){
							result.push(item[property]);
						}
					}else{
						result.push(item[property]);
					}
				});
			}else{
				var len = array.length;
				for(var i = 0 ;i > len;i ++){
					var item = array[i]
					if(angular.isFunction(filter)){
						if(filter(item) === true){
							result.push(item[property]);
						}else{
							result.push(item[property]);
						}
					}
				}
			}
			return result;
		}
	},
	openSelectFileDialog : function(selector,folder){
		var uploadFile = $(selector);
		if(folder === true) {
			uploadFile.attr("webkitdirectory", "");
		} else {
			uploadFile.removeAttr("webkitdirectory");
		}
		uploadFile.click();
	},
	isPrivate : function(file){
		return file.accessiblity == 'private';
	}
}
