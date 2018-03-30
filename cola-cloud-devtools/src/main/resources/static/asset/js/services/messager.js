angular.module('messager', [])
	.run(["$templateCache", function($templateCache) {
		$templateCache.put("template/modal/confirmModelTemplate.html",
			'<div class="m-c">\n' +
			'  <div class="modal-header bg-primary text-white">\n' +
			'    <h4 class="modal-title">{{title}}</h4>\n' +
			'  </div>\n' +
			'  <div class="modal-body">{{content}}</div>\n' +
			'  <div class="modal-footer" style="text-align: right;">\n' +
			'    <button type="button" class="btn btn-primary" ng-click="ok()">确定</button>\n' +
			'    <button type="button" class="btn btn-default" ng-click="cancel()">取消</button>\n' +
			'  </div>\n' +
			'</div>\n' +
			"");
		$templateCache.put("template/modal/alertModelTemplate.html",
				'<div class="m-c">\n' +
				'  <div class="modal-header bg-primary text-white">\n' +
				'    <h4 class="modal-title">{{title}}</h4>\n' +
				'  </div>\n' +
				'  <div class="modal-body">{{content}}</div>\n' +
				'  <div class="modal-footer" style="text-align: right;">\n' +
				'    <button type="button" class="btn btn-primary" ng-click="ok()">确定</button>\n' +
				'  </div>\n' +
				'</div>\n' +
				"");
		$templateCache.put("template/modal/promptModelTemplate.html",
				'<div class="m-c">\n' +
				'  <div class="modal-header">\n' +
				'    <h4 class="modal-title">{{title}}</h4>\n' +
				'  </div>\n' +
				'  <div class="modal-body">'+
						'<span>{{content}}</span>'+
						'<input class="form-control m-t-xs" ng-model="value" autofocus>'+
				'  </div>\n' +
				'  <div class="modal-footer" style="text-align: right;">\n' +
				'    <button type="button" class="btn btn-primary" ng-disabled="!value" ng-click="ok()">确定</button>\n' +
				'    <button type="button" class="btn btn-default" ng-click="cancel()">取消</button>\n' +
				'  </div>\n' +
				'</div>\n' +
				"");
	}]);

angular.module('messager').factory('Messager', ['$http', '$q',  '$location', '$modal',
			function($http, $q, $location, $modal) {
				return {
					confirm: function(title, content) {
						var deferred = $q.defer();
						/*
						 * modalInstance是在弹窗的基础上再弹出confirm确认框时从第一个弹窗中传进的$modalInstance,
						 * 若是直接在页面上弹出confirm确认框，则不能传$modalInstance,否则会报错
						 */
						var confirmModal = $modal.open({
							backdrop: 'static',
							templateUrl: 'template/modal/confirmModelTemplate.html', // 指向确认框模板
							controller: 'MessagerConfirmCtrl', // 初始化模态控制器
							windowClass: "confirmModal", // 自定义modal上级div的class
							resolve: {
								options: function() {
									return {
										title: title,
										content: content
									}; //surgeonSug: $scope.surgeonSug,
								}
							}
						});
						// 处理modal关闭后返回的数据
						confirmModal.result.then(function() {
							deferred.resolve();
						}, function() {
							deferred.reject();
						});
						return deferred.promise;
					 },
					 alert: function(title, content) {
							var deferred = $q.defer();
							/*
							 * modalInstance是在弹窗的基础上再弹出confirm确认框时从第一个弹窗中传进的$modalInstance,
							 * 若是直接在页面上弹出confirm确认框，则不能传$modalInstance,否则会报错
							 */
							var confirmModal = $modal.open({
								backdrop: 'static',
								templateUrl: 'template/modal/alertModelTemplate.html', // 指向确认框模板
								controller: 'MessagerAlertCtrl', // 初始化模态控制器
								windowClass: "alertModal", // 自定义modal上级div的class
								resolve: {
									options: function() {
										return {
											title: title,
											content: content
										}; //surgeonSug: $scope.surgeonSug,
									}
								}
							});
							// 处理modal关闭后返回的数据
							confirmModal.result.then(function() {
								deferred.resolve();
							}, function() {
								deferred.resolve();
							});
							return deferred.promise;
						 },
						 
					 prompt: function(title, content,value) {
						var deferred = $q.defer();
						/*
						 * modalInstance是在弹窗的基础上再弹出confirm确认框时从第一个弹窗中传进的$modalInstance,
						 * 若是直接在页面上弹出confirm确认框，则不能传$modalInstance,否则会报错
						 */
						var confirmModal = $modal.open({
							backdrop: 'static',
							templateUrl: 'template/modal/promptModelTemplate.html', // 指向确认框模板
							controller: 'MessagerPromptCtrl', // 初始化模态控制器
							windowClass: "promptModal", // 自定义modal上级div的class
							resolve: {
								options: function() {
									return {
										title: title,
										content: content,
										value : value
									}; 
								}
							}
						});
						// 处理modal关闭后返回的数据
						confirmModal.result.then(function(result) {
							deferred.resolve(result);
						}, function() {
							deferred.reject();
						});
						return deferred.promise;
					 }
    }
  }]);
  
angular.module('messager').controller("MessagerConfirmCtrl",['$scope', '$modalInstance','options',function($scope,$modalInstance,options){
	$scope.title = options.title;
	$scope.content = options.content;
    $scope.ok = function() {  
        $modalInstance.close();  
    };  
    $scope.cancel = function() {  
        $modalInstance.dismiss('cancel');  
    };  
}]);

angular.module('messager').controller("MessagerAlertCtrl",['$scope', '$modalInstance','options',function($scope,$modalInstance,options){
	$scope.title = options.title;
	$scope.content = options.content;
    $scope.ok = function() {  
        $modalInstance.close();  
    };  
    $scope.cancel = function() {  
        $modalInstance.dismiss('cancel');  
    };  
}]);

angular.module('messager').controller("MessagerPromptCtrl",['$scope', '$modalInstance','options',function($scope,$modalInstance,options){
	$scope.title = options.title;
	$scope.content = options.content;
	$scope.value = options.value;
    $scope.ok = function() {  
        $modalInstance.close($scope.value);  
    };  
    $scope.cancel = function() {  
        $modalInstance.dismiss('cancel');  
    };  
 }])

