'use strict';
/* Filters */
// need load the moment.js to use this filter.
angular.module('hc.util').filter('trustHtml', ['$sce',function($sce) {
	return function (input){
	       return $sce.trustAsHtml(input); 
	   } 
}]);