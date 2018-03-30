'use strict';
/* Filters */
// need load the moment.js to use this filter.
angular.module('hc.util').filter('resource', ['$rootScope',function($rootScope) {
	return function(resource, type) {
		if (!resource)
			return resource;
		if(resource.charAt(0) == "@"){
			resource = $rootScope.resourceServerUrl + resource.substring(1);
		}else{
			resource = ($rootScope.staticResourceServerUrl ? $rootScope.staticResourceServerUrl : "") + resource;
		}
		return resource;
	}
}]);