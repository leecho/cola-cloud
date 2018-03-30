'use strict';

/* Filters */
// need load the moment.js to use this filter. 
angular.module('hc.util')
  .filter('fromNow', function() {
    return function(date) {
      return moment(date).fromNow();
    }
  });