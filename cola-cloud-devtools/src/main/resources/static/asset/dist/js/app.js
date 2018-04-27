var app = angular.module('app', [
       	'ngAnimate',
        'ngResource',
        'ui.router',
        'ui.bootstrap',
        'ui.validate',
        'pascalprecht.translate',
        'messager',
        'toaster',
        'hc.util',
        'hc.ui',
        'hc.common'
    ]);
app.config(
	    [        '$controllerProvider', '$compileProvider', '$filterProvider', '$provide',
	    function ($controllerProvider,   $compileProvider,   $filterProvider,   $provide) {
	        // lazy controller, directive and service
	        app.controller = $controllerProvider.register;
	        app.directive  = $compileProvider.directive;
	        app.filter     = $filterProvider.register;
	        app.factory    = $provide.factory;
	        app.service    = $provide.service;
	        app.constant   = $provide.constant;
	        app.value      = $provide.value;
	    }
	  ])
	  .config(['$translateProvider', function($translateProvider){
	    $translateProvider.useStaticFilesLoader({
	      prefix: 'asset/l10n/',
	      suffix: '.js'
	    });
	    $translateProvider.preferredLanguage('zh_CN');
	  }]).factory('responseObserver', ['$q','$window',function responseObserver($q, $window) {
		    return {
		        'responseError': function(errorResponse) {
		            switch (errorResponse.status) {
		            case 401:
		                $window.location.href = '/login';
		                break;
		            }
		            return $q.reject(errorResponse);
		        }
		    };
		}]).config(['$httpProvider',function($httpProvider){  
		  $httpProvider.interceptors.push('responseObserver');
		  /*$httpProvider.defaults.transformRequest = function(obj){  
			     var params = [];  
			     for(var p in obj){  
			    	 var value = obj[p];
			    	 if(angular.isObject(value)){
			    		 transformObject(value,p,params);
			    	 }else{
			    		 params.push(encodeURIComponent(p) + "=" + encodeURIComponent(angular.isUndefined(value) ? "" : value));   
			    	 }
			     }  
			     return params.join("&");  
			   }; 
		   function transformObject(obj,name,params){
			   for(var pro in obj){
				   var value = obj[pro];
				   var paramName = name + "." + pro;
				   if(angular.isObject(value)){
					   transformObject(value,paramName,params);
				   }else{
					   params.push(encodeURIComponent(paramName) + "=" + encodeURIComponent(angular.isUndefined(value) ? "" : value));  
				   }
			   }
		   }*/
		  
		   $httpProvider.defaults.headers.post = {  
		       // 'Content-Type': 'application/x-www-form-urlencoded',
				'Content-Type': 'application/json',
		        'X-Requested-With' : 'XMLHttpRequest'
		   };  
		   $httpProvider.defaults.headers.get = {  
		        'X-Requested-With' : 'XMLHttpRequest'
		   }; 
	  }]);
app.run(['$rootScope', '$state', '$stateParams','$http','Messager',
	 function ($rootScope,   $state,   $stateParams,$http,Messager) {
	    $rootScope.$state = $state;
	    $rootScope.$stateParams = $stateParams;        
}])
.config(['$stateProvider', '$urlRouterProvider','$locationProvider',
	function ($stateProvider,   $urlRouterProvider,$locationProvider) {
		  //$locationProvider.html5Mode(true);
      $urlRouterProvider.otherwise('/index');
      $stateProvider.state('/index', {
              url: '/index',
              templateUrl: 'asset/tpl/index.html',
              controller:'IndexCtrl'
          }).state('changePassword', {
	            url: '/index/changePassword',
	            templateUrl: 'asset/tpl/change_password.html',
	            controller : 'ChangePasswordCtrl'
	        }).state('profile', {
	            url: '/index/profile',
	            templateUrl: 'asset/tpl/profile.html',
	            controller : 'ProfileCtrl'
	        })
	}
]);
app.controller('AppCtrl', ['$rootScope','$scope', 'toaster','Messager','$http','$modal','$location','$state','userData','setting','menus',
           function( $rootScope,$scope,toaster,Messager,$http,$modal,$location,$state,userData,setting,menus) {
	$scope.ie9 = /msie 9/.test(window.navigator.userAgent.toLowerCase());
	
    $scope.supportWebkitDirectory = ('webkitdirectory' in HTMLInputElement.prototype);
    $rootScope.toaster = toaster;
	$rootScope.resourceServerUrl = setting.resourceServceUrl + "/";
    $rootScope.staticResourceServerUrl = setting.staticResourceServerUrl + "/";
    //设置用户信息
    $rootScope.user = userData.user;
	$rootScope.organization = userData.organization;
	$rootScope.menus = menus;
	
    //更新用户设置
    $scope.updateUser = function(){
    	$http.post("/organization/user/set",{
    		name:$scope.user.name,
    		mode:$scope.user.mode,
    		sortField:$scope.user.sortField,
    		sortDesc:$scope.user.sortDesc
    	});
    };
	$rootScope.started = true;
	
}]);

angular.element(document).ready(function() {
	
	loadEnv();
	
	 //加载用户信息
    function loadEnv(){
    	$.get("/env").success(function(result){
			if(result.success){
				delete result.success;
				app.value("userData",result.data);
				app.value("setting",{
					staticResourceServerUrl : '',
					resourceServceUrl : ''
				});
				bootstrap();
			}else{
				alert("加载用户信息失败，请重新刷新页面再试");
			}
		}).error(function(){
			alert("加载用户信息失败，请重新刷新页面再试");
		});
    }
    
    var menus = [{
    	code : 'organization',
    	name : '组织架构',
    	icon : 'fa fa-group',
    	url : 'asset/tpl/organization/organization.html'
    },{
    	code : 'role',
    	name : '角色管理',
    	icon : 'fa fa-user',
    	url : 'asset/tpl/role/role.html'
    },{
    	code : 'resource',
    	name : '资源管理',
    	icon : 'fa fa-folder',
    	url : 'asset/tpl/resource/resource.html'
    }];
    
    app.config(['$stateProvider', function ($stateProvider, ) {
        menus.forEach(function(menu){
        	$stateProvider.state(menu.code, {
                url: '/' + menu.code,
                templateUrl: menu.url
            });
        });   
    }]);
    
    app.value("menus",menus);
    
    //加载配置
    /*function loadSetting(state,stateParam){
    	$.get("/setting").success(function(result){
    		if(result.success){
    			delete result.success ;
				app.value("setting",result);
				bootstrap();
    		}else{
    			alert("加载系统配置信息失败，请重新刷新页面再试");
    		}
    	}).error(function(){
			alert("加载系统配置信息失败，请重新刷新页面再试");
		});
    }*/
    
    
    function bootstrap(){
    	angular.bootstrap(document, ['app']);
    	window.clearInterval(window.preloadInterval);
    }
});
app.controller('SettingsCtrl', [
		'$scope',
		'$http',
		'$state',
		'toaster',
		function($scope, $http, $state, toaster) {
			$http.get("setting/app").success(function(result) {
				if (result.success) {
					/*result.setting.appLogo = app.resourceServceUrl + "/" + result.setting.appLogo;
					result.setting.appIcon = app.resourceServceUrl + "/" + result.setting.appIcon;*/
					$scope.setting = result.setting;
				} else {
					$scope.toaster.pop('error', '提示', "获取配置失败：" + data.message);
				}

			}).error(function() {
				$scope.toaster.pop('error', '提示', '获取配置失败：' + status.statusCode);
			});

			$scope.logo = {
				operation : "选择LOGO",
				uploading : false,
				uploadError : false,
				uploadErrorText : ""
			};

			$scope.icon = {
				operation : "选择图标",
				uploading : false,
				uploadError : false,
				uploadErrorText : ""
			};

			$scope.resource = {
				logo : '',
				icon : ''
			};
			$scope.save = function() {
				$http.post("setting/app", $scope.setting).success(
						function(data, status, headers, config) {
							if (data.success) {
								$scope.settingForm.$setPristine();
								$scope.settingForm.$setUntouched();
								$scope.toaster.pop('success', '提示', "保存成功");
							} else {
								$scope.toaster.pop('error', '提示', "保存失败："
										+ data.message);
							}
						}).error(function() {
							$scope.toaster.pop('error', '提示', '保存设置失败：' + status.statusCode);
				});
			}
			$scope.selectLogo = function() {
				angular.element("#logoFileInput").click();
			}
			
			$scope.selectImage = function(type) {
				$scope.uploadType = type;
				angular.element("#imageFileInput").click();
			}
			
			$scope.uploadContext = {};
			
			$scope.upload = function() {
				var files = angular.element("#imageFileInput")[0].files;
				var fd = new FormData();
				fd.append("image", files[0]);
				fd.append("field",$scope.uploadType);
				var xhr = new XMLHttpRequest();
				xhr.addEventListener("load", function(event) {
					$scope.uploading = false;
					if (xhr.status != 200) {
						$scope.uploadResult = {
							success : false,
							message : "上传失败：请稍候再试，" + xhr.status
						}
					}else{
						var result = angular.fromJson(xhr.response);
						$scope.uploadResult = result;
						if (!result.success) {
							$scope.uploadResult.message = "上传失败：" + result.message
						} else {
							$scope.setting[$scope.uploadType] = result.resource;
							var img = angular.element("#" + $scope.uploadType + "Image")[0];
							img.src = img.src + "?_dc=" + new Date().getTime();
						}
					}
					$scope.$apply();
				}, false);
				xhr.addEventListener("error", function(response) {
					$scope.uploading = false;
					$scope.uploadResult = {
						success : false,
						message : "上传失败：请稍候再试."
					}
				}, false);
				xhr.addEventListener("abort", function() {

				}, false);
				xhr.open("POST", "/setting/app/upload");
				$scope.uploading = true;
				xhr.setRequestHeader('X-Requested-With','XMLHttpRequest');
				xhr.send(fd);
			}

			$scope.uploadLogo = function() {
				var files = angular.element("#logoFileInput")[0].files;
				var fd = new FormData();
				fd.append("logo", files[0]);
				var xhr = new XMLHttpRequest();
				xhr.addEventListener("load", function(event) {
					$scope.logo.uploading = false;
					if (xhr.status != 200) {
						$scope.logo.result = {
							success : false,
							message : "上传失败：请稍候再试，" + xhr.status
						}
						return;
					}
					var result = angular.fromJson(xhr.response);
					$scope.logo.result = result;
					if (!result.success) {
						$scope.logo.message = "上传失败：" + result.message
					} else {
						var img = angular.element("#appLogo")[0];
						img.src = img.src + "?_dc=" + new Date().getTime();
					}
				}, false);
				xhr.addEventListener("error", function(response) {
					$scope.logo.uploading = false;
					$scope.logo.result = {
						success : false,
						message : "上传失败：请稍候再试."
					}
				}, false);
				xhr.addEventListener("abort", function() {

				}, false);
				xhr.open("POST", "/setting/app/upload/logo");
				$scope.logo.uploading = true;
				xhr.send(fd);
			}

			$scope.selectIcon = function() {
				angular.element("#iconFileInput").click();
			}

			$scope.uploadIcon = function() {
				var files = angular.element("#iconFileInput")[0].files;
				var fd = new FormData();
				fd.append("icon", files[0]);
				var xhr = new XMLHttpRequest();
				xhr.addEventListener("load", function(event) {
					$scope.icon.uploading = false;
					$scope.icon.operation = "选择图片";
					if (xhr.status != 200) {
						$scope.icon.uploadError = true;
						$scope.logo.uploadErrorText = "上传失败：请稍候再试，"
								+ xhr.status;
						return;
					}
					var result = angular.fromJson(xhr.response);
					if (!result.success) {

						$scope.icon.uploadError = true;
						$scope.icon.uploadErrorText = "上传失败：" + result.message
					} else {
						$scope.icon.uploadError = false;
						$scope.setting.appIcon = result.icon;
						var img = angular.element("#appIcon")[0];
						img.src = img.src + "?_dc=" + new Date().getTime();
					}
				}, false);
				xhr.addEventListener("error", function(response) {
					$scope.icon.uploading = false;
					$scope.icon.operation = "选择图片"
					$scope.icon.uploadError = true;
					$scope.icon.uploadErrorText = "上传失败：请稍候再试.";
				}, false);
				xhr.addEventListener("abort", function() {

				}, false);
				xhr.open("POST", "/setting/app/upload/icon");
				$scope.icon.uploading = true;
				$scope.icon.operation = "正在上传...";
				xhr.send(fd);
			}
		} ]);

app.controller('MappingConfigCtrl', [
		'$scope',
		'$http',
		'Messager',
		'$modal',
		'toaster',
		function($scope, $http, Messager, $modal, toaster) {

			$scope.status = {
				isopen : false,
				loading : false
			};

			function load() {
				$scope.status.loading = true;
				$http.get("conf/mapping").success(
						function(data, status, headers, config) {
							$scope.status.loading = false;
							if (data.success) {
								$scope.mappings = data.mappings;
							} else {
								$scope.status.loadError = true;
								$scope.status.loadErrorText = "获取文件配置列表失败："
										+ data.message;
								toaster.pop('error', '提示', data.message);
							}
						}).error(function() {
					$scope.status.loading = false;
					$scope.status.loadError = true;
					$scope.status.loadErrorText = "获取文件配置列表失败";
					toaster.pop('error', '提示', '加载失败：' + status.statusCode);
				});
			}

			load();

			$http.get("conf/category").success(function(result) {
				$scope.categories = result;
				$scope.categories.splice(0,0,{
					name : '全部',
					value : ''
				});
			});
			
			$scope.clearSelected = function() {
				if ($scope.selected) {
					$scope.selected.selected = false;
					$scope.selected = null;
				}
			}

			$scope.setSelected = function(storage) {
				if ($scope.selected && $scope.selected != storage) {
					$scope.selected.selected = false;
				}
				if (storage.selected !== true) {
					storage.selected = true;
					$scope.selected = storage;
				} else {
					storage.selected = false;
					$scope.selected = null;
				}
			}

			$scope.remove = function() {
				if (!$scope.selected)
					return;
				var url = "conf/mapping/delete";
				Messager.confirm("提示", "确定要删除?").then(
						function(result) {
							$http.post(url, {
								id : $scope.selected.id
							}).success(
									function(result, status, headers, config) {
										if (result.success) {
											load();
										} else {
											$scope.toaster.pop('error', '提示', '刪除失败：'
													+ result.message);
										}
									}).error(function(result) {
										$scope.toaster.pop('error', '提示', '刪除失败，稍候再试');
							});
						});
			}

			$scope.edit = function() {
				if (!$scope.selected)
					return;
				open({
					tpl : 'asset/tpl/app/mapping_form.html',
					ctrl : 'MappingConfigFormCtrl',
					id : $scope.selected.id
				});
			}

			$scope.add = function() {
				open({
					tpl : 'asset/tpl/app/mapping_form.html',
					ctrl : 'MappingConfigFormCtrl'
				});
			}

			function open(config) {
				var modalInstance = $modal.open({
					templateUrl : config.tpl,
					controller : config.ctrl,
					backdrop : 'static',
					resolve : {
						id : function() {
							return config.id;
						}
					}
				});

				modalInstance.result.then(function() {
					load();
				}, function() {

				});
			}
			
			$scope.condition = {
				extensions : '',
				category : undefined
			}
			
			$scope.search = function(mapping){
				if($scope.condition.extensions.length == 0 && !$scope.condition.category){
					return true;
				}
				
				var matchExt = false;
				if($scope.condition.extensions.length > 0 ){
					var extensions = $scope.condition.extensions.split(",") , len = extensions.length;
					for(var i = 0;i < len ; i ++){
						if(extensions[i].length > 0 && mapping.extensions.indexOf(extensions[i]) >= 0){
							matchExt = true;
						} 
					}
				}else{
					matchExt = true;
				}
				var matchCate = (!$scope.condition.category || mapping.category == $scope.condition.category );
				
				return matchCate && matchExt;
			}
		} ]);

app.controller('MappingConfigFormCtrl', [
		'$scope',
		'$http',
		'id',
		'$modalInstance',
		'Messager',
		function($scope, $http, id, $modalInstance, Messager) {
			$scope.icon = {
				operation : "选择图标",
				uploading : false,
				uploadError : false,
				uploadErrorText : ""
			};
			
			$http.get("conf/category").success(function(result) {
				$scope.categories = result;
			});
			
			$scope.mapping = {};
			$scope.operation = '添加'
			if (id != null) {
				$http.get("conf/mapping/" + id).success(function(result) {
					$scope.mapping = result.mapping;
				});
				$scope.operation = '编辑';
			}

			$scope.selectIcon = function() {
				$("#iconFileInput").click();
			}
			
			$scope.save = function() {
				var url = 'conf/mapping/save';
				if (id != null) {
					url = 'conf/mapping/update';
				}
				$http.post(url, $scope.mapping).success(function(result) {
					if (result.success) {
						$modalInstance.close();
					} else {
						$scope.result = result;
					}
				}).error(function(data,status) {
					$scope.result = {
						success : false,
						message : '保存失败:' + status
					};
				});
			}
			
			$scope.uploadIcon = function() {
				var files = angular.element("#iconFileInput")[0].files;
				var fd = new FormData();
				fd.append("icon", files[0]);
				if($scope.mapping.icon){
					fd.append("name", $scope.mapping.icon);
				}
				var xhr = new XMLHttpRequest();
				xhr.addEventListener("load", function(event) {
					$scope.icon.uploading = false;
					$scope.icon.operation = "选择图片";
					if (xhr.status != 200) {
						$scope.icon.uploadError = true;
						$scope.icon.uploadErrorText = "上传失败：请稍候再试，" + xhr.status;
						return;
					}else{
						var result = angular.fromJson(xhr.response);
						if (!result.success) {
							$scope.icon.uploadError = true;
							$scope.icon.uploadErrorText = "上传失败：" + result.message;
						} else {
							$scope.icon.uploadError = false;
							$scope.mapping.icon = result.icon;
							var img = angular.element("#mappingIcon")[0];
							img.src = img.src + "?_dc=" + new Date().getTime();
							$scope.mappingConfigForm.$dirty = true;
						}
					}
					$scope.$apply();
				}, false);
				xhr.addEventListener("error", function(response) {
					$scope.icon.uploading = false;
					$scope.icon.operation = "选择图片"
					$scope.icon.uploadError = true;
					$scope.icon.uploadErrorText = "上传失败：请稍候再试.";
					$scope.$apply();
				}, false);
				xhr.addEventListener("abort", function() {

				}, false);
				xhr.open("POST", "/conf/mapping/upload/icon");
				$scope.icon.uploading = true;
				$scope.icon.uploadError = false;
				$scope.icon.operation = "正在上传...";
				$scope.$apply();
				xhr.send(fd);
			}

			$scope.cancel = function() {
				if ($scope.mappingConfigForm.$dirty) {
					Messager.confirm("提示", "您已经进行了修改，是否要关闭?").then(
							function(result) {
								$modalInstance.dismiss('cancel');
							}, function() {
							});
				} else {
					$modalInstance.dismiss('cancel');
				}
			}
		} ]);

app.controller('ChangePasswordCtrl', ['$scope','$http', function ($scope,$http) {
      $scope.check = function(){
		    var modes = 0;
		    //正则表达式验证符合要求的
		    if ($scope.password.length < 1) return modes;
		    if (/\d/.test($scope.password)) modes++; //数字
		    if (/[a-z]/.test($scope.password)) modes++; //小写
		    if (/[A-Z]/.test($scope.password)) modes++; //大写  
		    if (/\W/.test($scope.password)) modes++; //特殊字符
		   $scope.strength = modes;
      };
      
      $scope.submit = function(){
    	  $http.post('/organization/user/change/password',{
    		  password : $scope.password,
    		  oldPassword:$scope.oldPassword
    	  }).success(function(result){
    		  if(result.success){
    			  $scope.password = null;
    			  $scope.oldPassword = null;
    			  $scope.confirmPassword = null;
    			  $scope.success = true;
    			  $scope.cause = null;
    			  $scope.user.changePassword = false;
    		  }else{
    			  $scope.cause = result.message;
    			  $scope.success = false;
    		  }
    	  }).error(function(result,status){
    		  $scope.error  = result.message || status;
    		  $scope.success = false;
    	  });
      };
  }]);
app.controller('IndexCtrl', [
		'$scope',
		'$http',
		'$state',
		'toaster',function($scope, $http, $state, toaster) {
			/*$scope.report = {
				sumOfSize : '-',
				countOfFile : '-'
			}
			$http.get("/statictis/report").success(function(result){
				if(result.success){
					$scope.report = result.report
				}else{
					toaster.error("获取报表数据失败");
				}
			}).error(function(){
				toaster.error("获取报表数据失败");
			});*/
		}]);
app.controller('OrganizationCtrl', [
		'$scope',
		'$http',
		'Messager',
		'$modal',
		function($scope, $http, Messager, $modal) {
			
			$scope.tree = {};
			
			$scope.status = {};
			
			$scope.onSelectionChange = function(selection){
				$scope.current = selection;
				$scope.loadUser();
			}
			
			$scope.onTreeInit = function(){
				var root = $scope.tree.getRoot();
				$scope.tree.select(root.children[0]);
			}
			
			$scope.onTreeOptionClick = function(node,event){
				event.stopPropagation();
				var menu = angular.element("#departmentMenu");
				menu.css({
					top : event.pageY,
					left : event.pageX
				})
				menu.show();
				$scope.selected = node;
			}	
			
			function hideMenu(event){
				//var menu = $(event.target).closest(".dropdown-menu");
				var contextmenu = angular.element("#departmentMenu");
				//if(menu.length == 0){
					contextmenu.hide();
					//delete $scope.selected;
				//}else if(menu[0] != contextmenu[0]){
					//contextmenu.hide();
				//}
			}
				
			$(window).on("click",function(event){
				hideMenu(event);
			});
			
			$scope.$on("$destroy",function(){
				$(window).unbind("click",hideMenu);
			});
			
			
			$scope.addDepartment = function() {
				var selected = $scope.selected;
				$scope.open({
					tpl : 'asset/tpl/organization/department_form.html',
					ctrl : 'DepartmentCtrl',
					data : {
						parent : selected
					},
					success : function(){
						$scope.tree.reload(selected);
					}
				});
			}
			
			$scope.editDepartment = function() {
				var selected = $scope.selected;
				$scope.open({
					tpl : 'asset/tpl/organization/department_form.html',
					ctrl : 'DepartmentCtrl',
					data : {
						id : selected.id,
						parent : selected.$parent
					},
					success : function(department){
						var node = $scope.tree.get(department.id);
						console.log(node);
						node.name = department.name;
					}
				});
			}
			
			$scope.moveDepartment = function() {
				var selected = $scope.selected;
				$scope.open({
					tpl : 'asset/tpl/organization/department_move.html',
					ctrl : 'DepartmentMoveCtrl',
					data : {
						department : selected,
						newParent : selected.$parent
					},
					success : function(result){
						var oldNode = $scope.tree.get(result.oldParent.id);
						$scope.tree.reload(oldNode);
						var newNode = $scope.tree.get(result.newParent);
						$scope.tree.reload(newNode);
						//var node = $scope.tree.get(department.id);
						//node.name = department.name;
					}
				});
			}
			
			$scope.deleteDepartment = function(){
				var selected = $scope.selected;
				var url = "/tenant/organization/delete?id=" + selected.id;
				Messager.confirm("提示", "确定要删除?").then(
					function(result) {
						$http.post(url).success(
							function(result, status, headers, config) {
								if (result.success) {
									$scope.tree.remove(selected);
								} else {
									$scope.toaster.error('刪除失败：'+ result.msg);
								}
							}).error(function(result) {
								$scope.toaster.error('刪除失败，稍候再试');
						});
					});
			}
			
			
			$scope.loadUser = function(){
				$scope.status.loading = true;
				$scope.$broadcast("beforeLoadUsers");
				$scope.users = null;
				$http.get("/tenant/user/listByDept",{
					params : {
						deptId : $scope.current.id
					}
				}).success(
						function(data, status, headers, config) {
							$scope.status.loading = false;
							if (data.success) {
								$scope.users = data.data;
							} else {
								$scope.status.error =  "获取用户列表失败：" + data.message;
							}
						}).error(function(data,status) {
						$scope.status.loading = false;
						$scope.status.error =  "获取用户列表失败：" + (data.message || status);
				});
			}
			
			$scope.open = function(config) {
				var modalInstance = $modal.open({
					templateUrl : config.tpl,
					controller : config.ctrl,
					backdrop : 'static',
					resolve : {
						data : function() {
							return config.data;
						}
					}
				});

				modalInstance.result.then(function(result) {
					if(angular.isFunction(config.success)){
						config.success(result);
					}
					//load();
				}, function() {

				});
			}
}]);

app.controller('DepartmentCtrl', [
		'$scope',
		'$http',
		'data',
		'$modalInstance',
		'Messager',
		function($scope, $http, data, $modalInstance, Messager) {
			$scope.showResult = false;
			$scope.operation = '添加';
			$scope.department = {};
			if (data.id != null) {
				$http.get("/tenant/organization/get?id=" + data.id).success(function(result) {
					$scope.department = result.data;
				});
				$scope.operation = '编辑';
			}
			$scope.parent = data.parent;
			
			$scope.save = function() {
				var url = '/tenant/organization/save';
				/*if ($scope.department.id) {
					url = '/organization/department/update';
				}*/
				/*if($scope.department.parent){
					$scope.department.parent = $scope.department.parent.id;
				}*/
				if($scope.parent){
					$scope.department.pid = $scope.parent.id;
				}
				$http.post(url, $scope.department).success(function(result) {
					if (result.success) {
						$modalInstance.close($scope.department);
					} else {
						$scope.result = result;
					}
				}).error(function (data, status, headers, config, statusText) {
					$scope.result = {
						success : false,
						message : "保存失败:" + data.msg || status
					}
				});
			}

			$scope.cancel = function() {
				if ($scope.departmentForm.$dirty) {
					Messager.confirm("提示", "您已经进行了修改，是否要关闭?").then(
							function(result) {
								$modalInstance.dismiss('cancel');
							}, function() {
							});
				} else {
					$modalInstance.dismiss('cancel');
				}
			}
		} ]);
//移动部门
app.controller('DepartmentMoveCtrl', [
		'$scope',
		'$http',
		'data',
		'$modalInstance',
		'Messager',
		function($scope, $http, data, $modalInstance, Messager) {
			$scope.operation = '移动';
			$scope.invalid = false;
			$scope.changed = false;
			$scope.treeConfig = {
				'load-url' : '/tenant/organization/list',
				'load-asyn' : true,
				'open-folder-icon':'fa fa-users',
			    'folder-icon':'fa fa-users',
			    'leaf-icon':'fa fa-users',
			}
			
			$scope.department = data.department;
			$scope.newParent = data.newParent;
			/*if (data.id != null) {
				$http.get("/tenant/organization/get?id=" + data.id).success(function(result) {
					$scope.department = result.data;
					$scope.newParent = result.data.parent;
				});
			}*/
			
			$scope.onChange = function(node){
				$scope.changed = true;
				var id = $scope.department.id;
				var current = node;
				$scope.invalid = false;
				while(current && !current.$root){
					if(current.id == $scope.department.id){
						$scope.invalid = true;
						break;
					}
					current = current.$parent;
				}
				/*if(node.path.indexOf(id) >= 0){
					$scope.invalid = true;
				}else{
					$scope.invalid = false;
				}*/
			}
			
			$scope.save = function() {
				var url = '/tenant/organization/move';
				$http.post(url, {
					id : $scope.department.id,
					f : $scope.id
				}).success(function(result) {
					if (result.success) {
						$modalInstance.close({
							oldParent : $scope.department.pid,
							newParent : $scope.newParent
						});
					} else {
						$scope.result = result;
					}
				}).error(function (data, status, headers, config, statusText) {
					$scope.result = {
						success : false,
						message : "保存失败:" + status
					}
				});
			}

			$scope.cancel = function() {
				if ($scope.changed) {
					Messager.confirm("提示", "您已经进行了修改，是否要关闭?").then(
							function(result) {
								$modalInstance.dismiss('cancel');
							}, function() {
							});
				} else {
					$modalInstance.dismiss('cancel');
				}
			}
} ]);
app.controller('ProfileCtrl', ['$scope','$http', function ($scope,$http) {
	  //$scope.sortType = $scope.user.sortDesc === true ? 'desc' : 'asc';
      $scope.save = function(){
    	  //$scope.user.sortDesc = $scope.sortType == 'desc' ? true : false;
    	  var toast = $scope.toaster.wait("正在保存");
    	  $http.post("/organization/user/set",{
    		  	avatar:$scope.user.avatar,
				name:$scope.user.name,
				mode:$scope.user.mode,
				sortField:$scope.user.sortField,
				sortDesc:$scope.user.sortDesc
			}).success(function(result){
				if(result.success){
					$scope.userSettingForm.$setPristine();
     				$scope.userSettingForm.$setUntouched();
					toast.doSuccess("个人设置已保存")
				}else{
					toast.doError("保存失败：" + result.message);
				}
			}).error(function(result,status){
				toast.doError("保存失败：" + (result.message || status));
			});
      }
      
      $scope.selectAvatar = function() {
			angular.element("#avatarInput").click();
		}
      
      $scope.upload = function() {
			var files = angular.element("#avatarInput")[0].files;
			var fd = new FormData();
			fd.append("avatar", files[0]);
			var xhr = new XMLHttpRequest();
			xhr.onload = function(event) {
				$scope.uploading = false;
				if (xhr.status != 200) {
					$scope.result = {
						success : false,
						message : "上传失败：请稍候再试：" + xhr.status
					}
				}else{
					var result = angular.fromJson(xhr.response);
					$scope.result = result;
					if (!result.success) {
						$scope.message = "上传失败：" + result.message
					} else {
						$scope.user.avatar = result.avatar;
						var avatarImg = angular.element(".avatar-img");
						avatarImg.attr("src",avatarImg.attr("src") + "?_dc=" +  new Date().getTime());
					}
				}
				$scope.$apply();
			};
			xhr.onerror = function(response) {
				$scope.uploading = false;
				$scope.result = {
					success : false,
					message : "上传失败：请稍候再试：" + xhr.status
				}
			};
			xhr.open("POST", "/organization/user/upload/avatar");
			xhr.setRequestHeader('X-Requested-With','XMLHttpRequest');
			$scope.uploading = true;
			xhr.send(fd);
		}
  }]);
app.controller('ResourceCtrl', [
		'$scope',
		'$http',
		'Messager',
		'$modal',
		function($scope, $http, Messager, $modal) {
			
			$scope.tree = {};
			
			$scope.status = {};
			
			$scope.onSelectionChange = function(selection){
				if(!selection.$root){
					$scope.parent = {};
					$scope.selected = selection;
					$scope.current = {
						id : selection.id,
						code : selection.code,
						name : selection.name,
						url : selection.url,
						loadType : selection.loadType,
						isMenu : selection.isMenu,
						description : selection.description,
						icon : selection.icon
					};
					if(selection.$parent && !selection.$parent.$root){
						$scope.parent = selection.$parent;
					}
					$scope.origin = angular.copy($scope.current);
				}
				//$scope.loadUser();
			}
			
			$scope.onTreeInit = function(){
				var root = $scope.tree.getRoot();
				//$scope.tree.select(root.children[0]);
			}
			
			$scope.onTreeOptionClick = function(node,event){
				event.stopPropagation();
				var menu = angular.element("#contextmenu");
				menu.css({
					top : event.pageY,
					left : event.pageX
				})
				menu.show();
				$scope.selected = node;
			}	
			
			function hideMenu(event){
				//var menu = $(event.target).closest(".dropdown-menu");
				var contextmenu = angular.element("#contextmenu");
				//if(menu.length == 0){
					contextmenu.hide();
					//delete $scope.selected;
				//}else if(menu[0] != contextmenu[0]){
					//contextmenu.hide();
				//}
			}
				
			$(window).on("click",function(event){
				hideMenu(event);
			});
			
			$scope.$on("$destroy",function(){
				$(window).unbind("click",hideMenu);
			});
			
			$scope.reset = function(){
				$scope.current = angular.copy($scope.origin);
				$scope.resourceForm.$setPristine();
				$scope.resourceForm.$setUntouched();
			}
			
			$scope.addResouce = function() {
				var selected = $scope.selected;
				$scope.current = {
						loadType:"2",
						isMenu:"1",
						name : "<新建资源>"
				};
				$scope.parent = {};
				if(selected && !selected.$root){
					$scope.parent = selected;
				}
				/*$scope.open({
					tpl : 'asset/tpl/organization/department_form.html',
					ctrl : 'DepartmentCtrl',
					data : {
						parent : selected
					},
					success : function(){
						$scope.tree.reload(selected);
					}
				});*/
			}
			
			$scope.save = function(){
				var toast = $scope.toaster.wait("正在保存...");
				if($scope.parent){
					$scope.current.pid = $scope.parent.id;
				}
				$http.post("/tenant/resource/save",$scope.current).success(
						function(data, status, headers, config) {
							if (data.success) {
								//修改
								if($scope.current.id){
									$scope.selected.icon = $scope.current.icon;
									$scope.selected.url = $scope.current.url;
									$scope.selected.loadType = $scope.current.loadType;
									$scope.selected.isMenu = $scope.current.isMenu;
									$scope.selected.description = $scope.current.description;
									$scope.selected.name = $scope.current.name;
								}else{
									//新增
									$scope.tree.reload($scope.selected);
									$scope.tree.select($scope.selected);
								}
								
								$scope.resourceForm.$setPristine();
			     				$scope.resourceForm.$setUntouched();
			     				toast.doSuccess("保存成功");
							} else {
								toast.doError("保存失败：" + (result.message || status));
							}
						}).error(function(result,status) {
							toast.doError("保存失败：" + (result.message || status));
					});
			}
			
			$scope.moveResource = function() {
				var selected = $scope.selected;
				$scope.open({
					tpl : 'asset/tpl/resource/resource_move.html',
					ctrl : 'ResourceMoveCtrl',
					data : {
						department : selected,
						newParent : selected.$parent
					},
					success : function(result){
						console.log(result);
						var oldNode = $scope.tree.get(result.oldParent);
						$scope.tree.reload(oldNode);
						var newNode = $scope.tree.get(result.newParent);
						$scope.tree.reload(newNode);
						//var node = $scope.tree.get(department.id);
						//node.name = department.name;
					}
				});
			}
			
			$scope.deleteResource = function(){
				var selected = $scope.selected;
				var url = "/tenant/resource/delete?id=" + selected.id;
				Messager.confirm("提示", "删除资源将会删除授权，确定要删除?").then(
					function(result) {
						var toast = $scope.toaster.wait("正在删除...");
						$http.post(url).success(
							function(result, status, headers, config) {
								if (result.success) {
									$scope.tree.remove($scope.selected);
								} else {
									toast.doError('刪除失败：'+ result.msg);
								}
							}).error(function(result,status) {
								toast.doError('刪除失败:' + (result.msg || status));
						});
					});
			}
			
			
			$scope.loadUser = function(){
				$scope.status.loading = true;
				$scope.$broadcast("beforeLoadUsers");
				$scope.users = null;
				$http.get("/tenant/user/listByDept",{
					params : {
						deptId : $scope.current.id
					}
				}).success(
						function(data, status, headers, config) {
							$scope.status.loading = false;
							if (data.success) {
								$scope.users = data.data;
							} else {
								$scope.status.error =  "获取用户列表失败：" + data.message;
							}
						}).error(function(data,status) {
						$scope.status.loading = false;
						$scope.status.error =  "获取用户列表失败：" + (data.message || status);
				});
			}
			
			$scope.open = function(config) {
				var modalInstance = $modal.open({
					templateUrl : config.tpl,
					controller : config.ctrl,
					backdrop : 'static',
					resolve : {
						data : function() {
							return config.data;
						}
					}
				});

				modalInstance.result.then(function(result) {
					if(angular.isFunction(config.success)){
						config.success(result);
					}
					//load();
				}, function() {

				});
			}
}]);
//移动部门
app.controller('ResourceMoveCtrl', [
		'$scope',
		'$http',
		'data',
		'$modalInstance',
		'Messager',
		function($scope, $http, data, $modalInstance, Messager) {
			$scope.operation = '移动';
			$scope.invalid = false;
			$scope.changed = false;
			$scope.treeConfig = {
				'load-url' : '/tenant/resource/list',
				'load-asyn' : true,
				'open-folder-icon':'fa fa-users',
			    'folder-icon':'fa fa-users',
			    'leaf-icon':'fa fa-users',
			}
			
			$scope.resource = data.department;
			$scope.newParent = data.newParent;
			/*if (data.id != null) {
				$http.get("/tenant/organization/get?id=" + data.id).success(function(result) {
					$scope.department = result.data;
					$scope.newParent = result.data.parent;
				});
			}*/
			
			$scope.onChange = function(node){
				$scope.changed = true;
				var id = $scope.resource.id;
				var current = node;
				$scope.invalid = false;
				while(current && !current.$root){
					if(current.id == $scope.resource.id || current.id == $scope.resource.pid){
						$scope.invalid = true;
						break;
					}
					current = current.$parent;
				}
				/*if(node.path.indexOf(id) >= 0){
					$scope.invalid = true;
				}else{
					$scope.invalid = false;
				}*/
			}
			
			$scope.save = function() {
				var url = '/tenant/resource/move';
				$http.post(url, {
					id : $scope.resource.id,
					pid : $scope.newParent.id
				}).success(function(result) {
					if (result.success) {
						$modalInstance.close({
							oldParent : $scope.resource.pid,
							newParent : $scope.newParent.id
						});
					} else {
						$scope.result = result;
					}
				}).error(function (data, status, headers, config, statusText) {
					$scope.result = {
						success : false,
						message : "保存失败:" + status
					}
				});
			}

			$scope.cancel = function() {
				if ($scope.changed) {
					Messager.confirm("提示", "您已经进行了修改，是否要关闭?").then(
							function(result) {
								$modalInstance.dismiss('cancel');
							}, function() {
							});
				} else {
					$modalInstance.dismiss('cancel');
				}
			}
} ]);

app.controller('RoleCtrl', [
       		'$scope',
       		'$http',
       		'Messager',
       		'$modal',
       		'toaster',
       		function($scope, $http, Messager, $modal, toaster) {
       			function load() {
       				$scope.loading = true;
       				$http.get("tenant/role/list").success(
       						function(result, status, headers, config) {
       							$scope.loading = false;
       							if (result.success) {
       								$scope.roles = result.data;
       							} else {
       								$scope.error =  result.message;
       							}
       						}).error(function(result,status) {
       						$scope.loading = false;
       						$scope.error =  result.message || status;
       				});
       			}

       			load();

       			$scope.remove = function(role) {
       				var url = "/tenant/role/delete";
       				Messager.confirm("提示", "删除之后已配置的文件权限将会失效，确定要删除?").then(function(result) {
       					var toast = $scope.toaster.wait("正在删除...");
       					$http.post(url, {
       						id : role.id
       					}).success(function(result, status, headers, config) {
       						if(result.success){
       							toast.doSuccess("权限配置已删除");
       							load();
       						}else{
       							toast.doError("删除失败：" + result.message);
       						}
       					}).error(function(result,status){
       						toast.doError("删除失败：" + (result.message || status));
       					});
       				});
       			}

       			$scope.edit = function(role) {
       				open({
       					tpl : 'asset/tpl/role/role_form.html',
       					ctrl : 'RoleFormCtrl',
       					role : role
       				});
       			}

       			$scope.add = function() {
       				open({
       					tpl : 'asset/tpl/role/role_form.html',
       					ctrl : 'RoleFormCtrl'
       				});
       			}

       			function open(config) {
       				var modalInstance = $modal.open({
       					templateUrl : config.tpl,
       					controller : config.ctrl,
       					backdrop : 'static',
       					resolve : {
       						role : function() {
       							return config.role;
       						}
       					}
       				});

       				modalInstance.result.then(function() {
       					load();
       				}, function() {
       					
       				});
       			}
       		} ]);

app.controller('RoleFormCtrl', [
		'$scope',
		'$http',
		'role',
		'$modalInstance',
		'Messager',
		function($scope, $http, role, $modalInstance, Messager) {
			
			
			$scope.role = role;
			$scope.operation = role ? '编辑' : '新建'
			$scope.save = function() {
				var url = '/tenant/role/save';
				if(role){
					url = '/tenant/role/update';
				}
				var toast = $scope.toaster.wait("正在保存...");
				$http.post(url, $scope.role).success(function(result) {
					$scope.saving = false;
					if (result.success) {
						toast.doSuccess("保存成功");
						$modalInstance.close();
					} else {
						toast.doError("保存失败：" + result.message);
					}
				}).error(function(result,status) {
					$scope.saving = false;
					toast.doError("保存失败：" + (result.message || status) );
				});
			}
			
			$scope.cancel = function() {
				if ($scope.roleForm.$dirty) {
					Messager.confirm("提示", "您已经进行了修改，是否要关闭?").then(
							function(result) {
								$modalInstance.dismiss('cancel');
							}, function() {
							});
				} else {
					$modalInstance.dismiss('cancel');
				}
			}
		} ]);

app.controller('UserCtrl', [
      		'$scope',
      		'$http',
      		'Messager',
      		'$modal',
      		function($scope, $http, Messager, $modal) {
      			
      			$scope.$on('beforeLoadUsers',function(){
      				$scope.selected = null;
      			});
      			
      			$scope.select = function(user) {
    				if ($scope.selected && $scope.selected != user) {
    					$scope.selected.selected = false;
    				}
    				if (user.selected !== true) {
    					user.selected = true;
    					$scope.selected = user;
    				} else {
    					user.selected = false;
    					$scope.selected = null;
    				}
    			}
      			
      			$scope.addUser = function() {
      				$scope.open({
      					tpl : 'asset/tpl/organization/user_form.html',
      					ctrl : 'UserFormCtrl',
      					data : {
      						department : $scope.current
      					},
      					success : function(){
      						$scope.loadUser();
      					}
      				});
      			}
      			
      			$scope.operation = function(type,user,event){
      				event.stopPropagation();
      				user.executing = true;
    				var url = '/tenant/user/' + type + "?id=" + user.id;
    				var toast = $scope.toaster.wait("正在执行...");
    				$http.post(url).success(function(result) {
    					user.executing = false;
    					if (result.success) {
    						toast.doSuccess("操作成功");
    						user.status = result.msg;
    					} else {
    						toast.doError('操作失败：' + result.message);
    					}
    				}).error(function (data, status) {
    					user.executing = false;
    					toast.doError('操作失败：' + (data.message || status));
    				});
      			}
      			
      			$scope.reset = function(event){
      				Messager.confirm("提示","确定将该用户的密码重置为系统默认密码?").then(function(){
      					var user = $scope.selected;
      					user.executing = true;
        				var url = '/tenant/user/resetPassword';
        				var toast = $scope.toaster.wait("正在重置密码...");
        				$http.post(url + '?id=' + user.id).success(function(result) {
        					user.executing = false;
        					if (result.success) {
        						toast.doSuccess("重置密码成功");
        					} else {
        						toast.doError("重置密码失败：" + result.message);
        					}
        				}).error(function (data, status, headers, config, statusText) {
        					user.executing = false;
        					toast.doError("重置密码失败：" + (data.message || status));
        				});
      				});
      			}
      			
      			$scope.disable = function(user,event){
      				Messager.confirm("提示","确定禁用该用户?").then(function(){
      					$scope.operation('disable',user,event);
      				});
      			}
      			
      			$scope.enable = function(user,event){
      				Messager.confirm("提示","确定启用该用户?").then(function(){
      					$scope.operation('enable',user,event);
      				});
      			}
      			
      			/*$scope.lock = function(user,event){
      				Messager.confirm("提示","确定锁定该用户?").then(function(){
      					$scope.operation('lock',user,event);
      				});
      			}
      			$scope.unlock = function(user,event){
      				Messager.confirm("提示","确定解锁该用户?").then(function(){
      					$scope.operation('unlock',user,event);
      				});
      			}*/
      			
      			$scope.move = function() {
    				var selected = $scope.selected;
    				$scope.open({
    					tpl : 'asset/tpl/organization/user_move.html',
    					ctrl : 'UserMoveCtrl',
    					data : {
    						user : $scope.selected,
    						department : $scope.current
    					},
    					success : function(result){
    						$scope.toaster.pop('success','移动部门成功');
    						$scope.loadUser();
    					}
    				});
    			}   
      			
      			
      			
      			$scope.transferAdmin = function(user) {
      				  if($scope.selected.id == $scope.user.id){
      					  Messager.alert("提示","不能移交给自己");
      					  return ;
      				  }
              		  Messager.confirm("提示","管理员权限移交后将会自动退出。确定要移交管理员权限？").then(function(){
              			  if(!$scope.selected) return;
                  		  var params = {
                  			 id:$scope.selected.id
                  		  }
                  		  var toast = $scope.toaster.wait('正在移交权限...');
                  		  $scope.saving = true;
                  		  $http.post('/organization/user/admin/transfer',params).success(function(result){
                  			  $scope.saving = false;
                  			  if(result.success){
                  				toast.doClose();
                  				Messager.alert("提示","权限移交成功").then(function(){
                  					window.location.href = "/logout";
                  				});
                  			  }else{
                  				  toast.doError("权限移交失败：" + result.message);
                  				  //$scope.toaster.error('提交失败：' + result.message);
                  			  }
                  		  }).error(function(result,status){
                  			  $scope.saving = false;
                  			  toast.doError("权限移交失败：" + (result.message || status));
                  		  });
              		  });
    			}
      			
      			
      			$scope.editUser = function() {
      				var selected = $scope.selected;
      				$scope.open({
      					tpl : 'asset/tpl/organization/user_form.html',
      					ctrl : 'UserFormCtrl',
      					data : {
      						id : selected.id,
      						department : $scope.current
      					},
      					success : function(department){
      						$scope.loadUser();
      					}
      				});
      			}
      }]);


//移动用户
app.controller('UserMoveCtrl', [
          		'$scope',
          		'$http',
          		'data',
          		'$modalInstance',
          		'Messager',
          		function($scope, $http, data, $modalInstance, Messager) {
          			$scope.operation = '移动';
          			$scope.treeConfig = {
          				'load-url' : '/tenant/organization/list',
          				'load-asyn' : true,
          				'open-folder-icon':'fa fa-users',
          			    'folder-icon':'fa fa-users',
          			    'leaf-icon':'fa fa-users',
          			}
          			
          			$scope.user = data.user;
          			$scope.department = data.department;
          			
          			$scope.onChange = function(node){
          				$scope.changed = true;
          			}
          			
          			$scope.save = function() {
          				var url = '/tenant/user/setDept';
          				$http.post(url, {
          					id : $scope.user.id,
          					deptId : $scope.department.id
          				}).success(function(result) {
          					if (result.success) {
          						$modalInstance.close();
          					} else {
          						$scope.result = result;
          					}
          				}).error(function (data, status, headers, config, statusText) {
          					$scope.result = {
          						success : false,
          						message : "保存失败:" + status
          					}
          				});
          			}

          			$scope.cancel = function() {
          				if ($scope.changed) {
          					Messager.confirm("提示", "您已经进行了修改，是否要关闭?").then(
          							function(result) {
          								$modalInstance.dismiss('cancel');
          							}, function() {
          							});
          				} else {
          					$modalInstance.dismiss('cancel');
          				}
          			}
          } ]);


//添加用户
app.controller('UserFormCtrl', [
  		'$scope',
  		'$http',
  		'data',
  		'$modalInstance',
  		'Messager',
  		function($scope, $http, data, $modalInstance, Messager) {
  			$scope.operation = '添加';
  			$scope.user = {};
  			if (data.id != null) {
  				$http.get("/tenant/user/get?id=" + data.id).success(function(result) {
  					$scope.user = result.data;
  					/*if(!$scope.user.department){
  						delete $scope.user.department;
  					}*/
  				});
  				$scope.operation = '编辑';
  			}
  			$scope.department = data.department;
  			$scope.user.deptId = $scope.department.id;
  			$scope.save = function() {
  				$scope.saving = true;
  				var url = '/tenant/user/save';
  				if ($scope.user.id) {
  					url = '/tenant/user/update';
  				}
  				$http.post(url, $scope.user).success(function(result) {
  					$scope.saving = false;
  					if (result.success) {
  						$modalInstance.close($scope.user);
  					} else {
  						$scope.result = result;
  					}
  				}).error(function (data, status, headers, config, statusText) {
  					$scope.saving = false;
  					$scope.result = {
  						success : false,
  						message : "保存失败:" + status
  					}
  				});
  			}

  			$scope.cancel = function() {
  				if ($scope.userForm.$dirty) {
  					Messager.confirm("提示", "您已经进行了修改，是否要关闭?").then(
  							function(result) {
  								$modalInstance.dismiss('cancel');
  							}, function() {
  							});
  				} else {
  					$modalInstance.dismiss('cancel');
  				}
  			}
  		} ]);