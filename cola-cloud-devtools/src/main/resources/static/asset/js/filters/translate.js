'use strict';

/* 通用代码 */
//  将值转换成文本
angular.module('hc.util')
  .filter('translate', ['$translate',function($translate) {
    return function(value,key) {
    	if(value){
            return $translate.instant(key+"."+value);
    	}
    }
  }]);