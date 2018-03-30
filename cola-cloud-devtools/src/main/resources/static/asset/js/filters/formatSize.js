'use strict';
/* Filters */
// need load the moment.js to use this filter.
angular.module('hc.util').filter('formatSize', ['$rootScope',function($rootScope) {
	return function(size) {
		
		if(!size) return ;
		
		size = parseInt(size);
		var G = 1024 * 1024 * 1024;
		var M = 1024 * 1024;
		var K = 1025;
		if(size > G){
			return (size/G).toFixed(1) + 'GB';
		}else if(size > M){
			return (size/M).toFixed(1) + 'MB';
		}else if(size > K){
			return (size/K).toFixed(1) + 'KB';
		}
		return size + "B";
	}
}]);