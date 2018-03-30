angular.module('hc.common',[]);

angular.module('hc.ui',[]);

angular.module('hc.util',[]);
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


'use strict';

/**
 * 0.1.1
 * Deferred load js/css file, used for ui-jq.js and Lazy Loading.
 * 
 * @ flatfull.com All Rights Reserved.
 * Author url: http://themeforest.net/user/flatfull
 */

angular.module('ui.load', [])
	.service('uiLoad', ['$document', '$q', '$timeout', function($document, $q, $timeout) {

		var loaded = [];
		var promise = false;
		var deferred = $q.defer();

		/**
		 * Chain loads the given sources
		 * @param srcs array, script or css
		 * @returns {*} Promise that will be resolved once the sources has been loaded.
		 */
		this.load = function(srcs) {
			srcs = angular.isArray(srcs) ? srcs : srcs.split(/\s+/);
			var self = this;
			if(!promise) {
				promise = deferred.promise;
			}
			angular.forEach(srcs, function(src) {
				promise = promise.then(function() {
					return src.indexOf('.css') >= 0 ? self.loadCSS(src) : self.loadScript(src);
				});
			});
			deferred.resolve();
			return promise;
		}

		/**
		 * Dynamically loads the given script
		 * @param src The url of the script to load dynamically
		 * @returns {*} Promise that will be resolved once the script has been loaded.
		 */
		this.loadScript = function(src) {
			if(loaded[src]) return loaded[src].promise;

			var deferred = $q.defer();
			var script = $document[0].createElement('script');
			script.src = src;
			script.onload = function(e) {
				$timeout(function() {
					deferred.resolve(e);
				});
			};
			script.onerror = function(e) {
				$timeout(function() {
					deferred.reject(e);
				});
			};
			$document[0].body.appendChild(script);
			loaded[src] = deferred;

			return deferred.promise;
		};

		/**
		 * Dynamically loads the given CSS file
		 * @param href The url of the CSS to load dynamically
		 * @returns {*} Promise that will be resolved once the CSS file has been loaded.
		 */
		this.loadCSS = function(href) {
			if(loaded[href]) return loaded[href].promise;

			var deferred = $q.defer();
			var style = $document[0].createElement('link');
			style.rel = 'stylesheet';
			style.type = 'text/css';
			style.href = href;
			style.onload = function(e) {
				$timeout(function() {
					deferred.resolve(e);
				});
			};
			style.onerror = function(e) {
				$timeout(function() {
					deferred.reject(e);
				});
			};
			$document[0].head.appendChild(style);
			loaded[href] = deferred;

			return deferred.promise;
		};
	}]);
angular.module('hc.util')
  .directive('setNgAnimate', ['$animate', function ($animate) {
    return {
        link: function ($scope, $element, $attrs) {
            $scope.$watch( function() {
                return $scope.$eval($attrs.setNgAnimate, $scope);
            }, function(valnew, valold){
                $animate.enabled(!!valnew, $element);
            });
        }
    };
  }]);
angular.module('hc.ui')
  .directive('uiButterbar', ['$rootScope', '$anchorScroll', function($rootScope, $anchorScroll) {
     return {
      restrict: 'AC',
      template:'<span class="bar"></span>',
      link: function(scope, el, attrs) {        
        el.addClass('butterbar hide');
        scope.$on('$stateChangeStart', function(event) {
          $anchorScroll();
          el.removeClass('hide').addClass('active');
        });
        scope.$on('$stateChangeSuccess', function( event, toState, toParams, fromState ) {
          event.targetScope.$watch('$viewContentLoaded', function(){
            el.addClass('hide').removeClass('active');
          })
        });
        scope.$on('$stateChangeError', function( event, toState, toParams, fromState ) {
           el.addClass('hide').removeClass('active');
        });
      }
     };
  }]);
angular.module('hc.ui').directive('hcComboTree', ['$compile',function($compile) {
     return {
      restrict: 'E',
      scope : {
			selectedItem: "=ngModel",
			options: "=",
			placeholder: "=",
			disabled: "=",
			multiSelect : "@",
			onChange : '&',
			textField : '@',
			treeConfig : "=",
			onBeforeLoad : '&'
	  },
	  controllerAs : "combotreeCtrl",
	  link: function(scope, element, attrs) {
      	var config = angular.fromJson(scope.treeConfig);
      	var treeAttrs = "";
      	for(var option in config){
      		treeAttrs += (option + "=\"" + config[option]) + "\" ";
      	}
	  	var treeTpl = '<hc-tree ' + treeAttrs + ' multi-select="{{multiSelect}}" on-before-load="combotreeCtrl.onBeforeLoad(node,params);" on-selection-change="combotreeCtrl.onSelectionChange(selection);"></hc-tree>'; 
	  	
	  	element.find(".dropdown-menu").html(treeTpl);
	  	$compile(element.contents())(scope);
	  	
	  	function hideDropdown(e){
            if (!element[0].contains(e.target)) {
                scope.combotreeCtrl.pop = false;
				scope.$digest();
            }
        }
	  	
		scope.combotreeCtrl.placeholder = scope.combotreeCtrl.placeholder || "请选择";
        var closeEvent = $(window).on('click',hideDropdown);
		scope.$on('$destroy', function(){ 
			$(window).unbind('click',hideDropdown);
		});
	  },
      template: function(element,scope){
	    var tpl = '<div class="hc-combotree dropdown" ng-class="{\'open\':combotreeCtrl.pop}">' +
					'<button class="btn btn-default dropdown-toggle" type="button" ng-click="combotreeCtrl.showPop()" style="width: 100%;text-align: left;" ng-disabled="combotreeCtrl.disabled">'+ 
						'{{combotreeCtrl.getText()}}'+ 
						'<span class="caret"></span>'+ 
					'</button>'+ 
				'<div class="dropdown-menu" style="max-height:300px;overflow:auto;min-width:200px;width:100%" >' + 
				'</div>'+
			'</div>';
		return tpl;
		},
	      
      controller : ['$scope','$element',function($scope,$element){
    	  
      	this.onSelectionChange = function(selection){
      		this.select(selection);
      	}
      	
      	this.onBeforeLoad = function(node,params){
      		if(angular.isFunction($scope.onBeforeLoad)) {
				$scope.onBeforeLoad({
					node : node,
					params : params
				});
			}
      	}
      	
		this.showPop = function() {
			if (this.disabled) {
				return;
			}
			this.pop = true;
		}
		
		this.getText = function(){
			if($scope.selectedItem){
				return $scope.selectedItem[$scope.textField || 'name'];
			}else{
				return $scope.placeholder;
			}
		}
	
		this.select = function(item) {
			var fireEvent = false;
			if(item != $scope.selectedItem){
				fireEvent = true;
			}
			$scope.selectedItem = item;
			if(fireEvent === true){
				$scope.onChange({
					node : $scope.selectedItem
				});
			}
			if($scope.multiSelect != 'true'){
				this.pop = false;
			}
		}
      }]
     };
}]);
angular.module('hc.ui')
  .directive('uiFocus', ['$timeout','$parse',function($timeout, $parse) {
    return {
      link: function(scope, element, attr) {
        var model = $parse(attr.uiFocus);
        scope.$watch(model, function(value) {
          if(value === true) {
            $timeout(function() {
              element[0].focus();
            });
          }
        });
        element.bind('blur', function() {
           scope.$apply(model.assign(scope, false));
        });
      }
    };
  }]);
 angular.module('hc.ui')
  .directive('uiFullscreen', ['uiLoad', '$document', '$window', function(uiLoad, $document, $window) {
    return {
      restrict: 'AC',
      template:'<i class="fa fa-expand fa-fw text"></i><i class="fa fa-compress fa-fw text-active"></i>',
      link: function(scope, el, attr) {
        el.addClass('hide');
        uiLoad.load('vendor/libs/screenfull.min.js').then(function(){
          // disable on ie11
          if (screenfull.enabled && !navigator.userAgent.match(/Trident.*rv:11\./)) {
            el.removeClass('hide');
          }
          el.on('click', function(){
            var target;
            attr.target && ( target = $(attr.target)[0] );            
            screenfull.toggle(target);
          });
          $document.on(screenfull.raw.fullscreenchange, function () {
            if(screenfull.isFullscreen){
              el.addClass('active');
            }else{
              el.removeClass('active');
            }
          });
        });
      }
    };
  }]);
'use strict';

/**
 * 0.1.1
 * General-purpose jQuery wrapper. Simply pass the plugin name as the expression.
 *
 * It is possible to specify a default set of parameters for each jQuery plugin.
 * Under the jq key, namespace each plugin by that which will be passed to ui-jq.
 * Unfortunately, at this time you can only pre-define the first parameter.
 * @example { jq : { datepicker : { showOn:'click' } } }
 *
 * @param ui-jq {string} The $elm.[pluginName]() to call.
 * @param [ui-options] {mixed} Expression to be evaluated and passed as options to the function
 *     Multiple parameters can be separated by commas
 * @param [ui-refresh] {expression} Watch expression and refire plugin on changes
 *
 * @example <input ui-jq="datepicker" ui-options="{showOn:'click'},secondParameter,thirdParameter" ui-refresh="iChange">
 */
angular.module('ui.jq', ['ui.load']).
  value('uiJqConfig', {}).
  directive('uiJq', ['uiJqConfig', 'JQ_CONFIG', 'uiLoad', '$timeout', function uiJqInjectingFunction(uiJqConfig, JQ_CONFIG, uiLoad, $timeout) {

  return {
    restrict: 'A',
    compile: function uiJqCompilingFunction(tElm, tAttrs) {

      if (!angular.isFunction(tElm[tAttrs.uiJq]) && !JQ_CONFIG[tAttrs.uiJq]) {
        throw new Error('ui-jq: The "' + tAttrs.uiJq + '" function does not exist');
      }
      var options = uiJqConfig && uiJqConfig[tAttrs.uiJq];

      return function uiJqLinkingFunction(scope, elm, attrs) {
        function getOptions(){
          var linkOptions = [];

          // If ui-options are passed, merge (or override) them onto global defaults and pass to the jQuery method
          if (attrs.uiOptions) {
            linkOptions = scope.$eval('[' + attrs.uiOptions + ']');
            if (angular.isObject(options) && angular.isObject(linkOptions[0])) {
              linkOptions[0] = angular.extend({}, options, linkOptions[0]);
            }
          } else if (options) {
            linkOptions = [options];
          }
          return linkOptions;
        }

        // If change compatibility is enabled, the form input's "change" event will trigger an "input" event
        if (attrs.ngModel && elm.is('select,input,textarea')) {
          elm.bind('change', function() {
            elm.trigger('input');
          });
        }

        // Call jQuery method and pass relevant options
        function callPlugin() {
          $timeout(function() {
            elm[attrs.uiJq].apply(elm, getOptions());
          }, 0, false);
        }

        function refresh(){
          // If ui-refresh is used, re-fire the the method upon every change
          if (attrs.uiRefresh) {
            scope.$watch(attrs.uiRefresh, function() {
              callPlugin();
            });
          }
        }

        if ( JQ_CONFIG[attrs.uiJq] ) {
          uiLoad.load(JQ_CONFIG[attrs.uiJq]).then(function() {
            callPlugin();
            refresh();
          }).catch(function() {
            
          });
        } else {
          callPlugin();
          refresh();
        }
      };
    }
  };
}]);
angular.module('hc.ui')
  .directive('uiModule', ['MODULE_CONFIG','uiLoad', '$compile', function(MODULE_CONFIG, uiLoad, $compile) {
    return {
      restrict: 'A',
      compile: function (el, attrs) {
        var contents = el.contents().clone();
        return function(scope, el, attrs){
          el.contents().remove();
          uiLoad.load(MODULE_CONFIG[attrs.uiModule])
          .then(function(){
            $compile(contents)(scope, function(clonedElement, scope) {
              el.append(clonedElement);
            });
          });
        }
      }
    };
  }]);
angular.module('hc.ui')
  .directive('uiNav', ['$timeout', function($timeout) {
    return {
      restrict: 'AC',
      link: function(scope, el, attr) {
        var _window = $(window), 
        _mb = 768, 
        wrap = $('.app-aside'), 
        next, 
        backdrop = '.dropdown-backdrop';
        // unfolded
        el.on('click', 'a', function(e) {
          next && next.trigger('mouseleave.nav');
          var _this = $(this);
          _this.parent().siblings( ".active" ).toggleClass('active');
          _this.next().is('ul') &&  _this.parent().toggleClass('active') &&  e.preventDefault();
          // mobile
          _this.next().is('ul') || ( ( _window.width() < _mb ) && $('.app-aside').removeClass('show off-screen') );
        });

        // folded & fixed
        el.on('mouseenter', 'a', function(e){
          next && next.trigger('mouseleave.nav');
          $('> .nav', wrap).remove();
          if ( !$('.app-aside-fixed.app-aside-folded').length || ( _window.width() < _mb ) || $('.app-aside-dock').length) return;
          var _this = $(e.target)
          , top
          , w_h = $(window).height()
          , offset = 50
          , min = 150;

          !_this.is('a') && (_this = _this.closest('a'));
          if( _this.next().is('ul') ){
             next = _this.next();
          }else{
            return;
          }
         
          _this.parent().addClass('active');
          top = _this.parent().position().top + offset;
          next.css('top', top);
          if( top + next.height() > w_h ){
            next.css('bottom', 0);
          }
          if(top + min > w_h){
            next.css('bottom', w_h - top - offset).css('top', 'auto');
          }
          next.appendTo(wrap);

          next.on('mouseleave.nav', function(e){
            $(backdrop).remove();
            next.appendTo(_this.parent());
            next.off('mouseleave.nav').css('top', 'auto').css('bottom', 'auto');
            _this.parent().removeClass('active');
          });

          $('.smart').length && $('<div class="dropdown-backdrop"/>').insertAfter('.app-aside').on('click', function(next){
            next && next.trigger('mouseleave.nav');
          });

        });

        wrap.on('mouseleave', function(e){
          next && next.trigger('mouseleave.nav');
          $('> .nav', wrap).remove();
        });
      }
    };
  }]);
angular.module('hc.util').directive('ngRightClick', ['$parse',function($parse) {
    return function(scope, element, attrs) {
        var fn = $parse(attrs.ngRightClick);
        element.bind('contextmenu', function(event) {
            scope.$apply(function() {
                event.preventDefault();
                fn(scope, {$event:event});
            });
        });
    };
}]);
angular.module('hc.ui')
  .directive('uiScroll', ['$location', '$anchorScroll', function($location, $anchorScroll) {
    return {
      restrict: 'AC',
      link: function(scope, el, attr) {
        el.on('click', function(e) {
          $location.hash(attr.uiScroll);
          $anchorScroll();
        });
      }
    };
  }]);
angular.module('hc.ui')
  .directive('uiShift', ['$timeout', function($timeout) {
    return {
      restrict: 'A',
      link: function(scope, el, attr) {
        // get the $prev or $parent of this el
        var _el = $(el),
            _window = $(window),
            prev = _el.prev(),
            parent,
            width = _window.width()
            ;

        !prev.length && (parent = _el.parent());
        
        function sm(){
          $timeout(function () {
            var method = attr.uiShift;
            var target = attr.target;
            _el.hasClass('in') || _el[method](target).addClass('in');
          });
        }
        
        function md(){
          parent && parent['prepend'](el);
          !parent && _el['insertAfter'](prev);
          _el.removeClass('in');
        }

        (width < 768 && sm()) || md();

        _window.resize(function() {
          if(width !== _window.width()){
            $timeout(function(){
              (_window.width() < 768 && sm()) || md();
              width = _window.width();
            });
          }
        });
      }
    };
  }]);
angular.module('hc.ui')
  .directive('uiToggleClass', ['$timeout', '$document', function($timeout, $document) {
    return {
      restrict: 'AC',
      link: function(scope, el, attr) {
        el.on('click', function(e) {
          e.preventDefault();
          e.stopPropagation();
          var classes = attr.uiToggleClass.split(','),
              targets = (attr.target && attr.target.split(',')) || Array(el),
              key = 0;
          angular.forEach(classes, function( _class ) {
            var target = targets[(targets.length && key)];            
            ( _class.indexOf( '*' ) !== -1 ) && magic(_class, target);
            $( target ).toggleClass(_class);
            key ++;
          });
          $(el).toggleClass('active');

          function magic(_class, target){
            var patt = new RegExp( '\\s' + 
                _class.
                  replace( /\*/g, '[A-Za-z0-9-_]+' ).
                  split( ' ' ).
                  join( '\\s|\\s' ) + 
                '\\s', 'g' );
            var cn = ' ' + $(target)[0].className + ' ';
            while ( patt.test( cn ) ) {
              cn = cn.replace( patt, ' ' );
            }
            $(target)[0].className = $.trim( cn );
          }
        });
      }
    };
  }]);
angular.module('hc.ui').directive('hcTree', ['$http', function($http) {
	return {
		restrict: 'E',
		scope: {
			treeData: "=",
			loadUrl: "@",
			onBeforeLoad: "&",
			loadAsyn: "@",
			multiSelect : "@",
			clickToDiselect : "@",
			showCheckbox: "@",
			treeControl: "=",
			onSelectionChange :"&",
			onOptionClick : "&",
			onNodeDblClick : '&',
			showOptionIcon : "&",
			onInit : "&",
			cascadCheck : "@",
			determineSelected : '&',
			leafIcon : '@',
			folderIcon : '@',
			openFolderIcon : '@',
			root : "@",
			rootVisiable:"@"
		},
		transclude: true,
		controllerAs: 'treeCtrl',
		link: function(scope, element, attrs,treeCtrl) {
			
		},
		//ng-class="{true:\'selected\', false:\'\'}[node.$selected]"
		replace: true,
		template: function(element, scope) {
			var tpl = '<ul class="hc-tree">' +
				'<li sf-treepeat="node in children of root" >' +
				'<a href="javascript:;" class="hc-lvl-{{node.$level}}" ng-class="{\'selected\':node.$selected || node.$checked, \'hidden\':node.$hide}" ng-dblclick="treeCtrl.nodeDblClick(node,$event)" ng-click="treeCtrl.nodeClick(node)">' ;
			if(scope.showOptionIcon == 'true'){
				tpl += '<i class="hc-tree-icon hc-tree-option-icon" ng-click="treeCtrl.optionClick(node,$event)" ng-class="treeCtrl.getOptionIcon(node)"></i>';
			} 
			tpl += '<i class="hc-tree-icon " ng-click="treeCtrl.toggleNode(node,$event)" ng-class="treeCtrl.getArrowClass(node)"></i>' + 
				   '<i class="hc-tree-icon " ng-class="treeCtrl.getNodeIcon(node)"></i>' ;
			if(scope.showCheckbox == 'true') {
				//tpl += '<input type="checkbox" ng-model="node.$checked" ng-change="itemCheck(node)"></input>';
				tpl+= '<label ng-click="treeCtrl.nodeCheck(node,$event)" class="i-checks i-checks-sm" ><input type="checkbox" ng-model="node.$selected"  checked=""><i></i></label>';
			}
			tpl += '<span >' +
				'{{ node.name }}' +
				'</span>' +
				'</a>' + 
				'<ul ng-show=\'node.$expanded\'><li sf-treecurse></li></ul>' +
				'</li>' +
				'</ul>';
			return tpl;
		},

		controller: ['$scope','$element',function($scope, $element) {
			/*$scope.loadAsyn = Util.Boolean.parse($scope.loadAsyn);
			$scope.checkbox = Util.Boolean.parse($scope.checkbox);*/
			$scope.multiSelect = Util.Boolean.parse($scope.multiSelect);
			$scope.cascadCheck = Util.Boolean.parse($scope.cascadCheck);
			$scope.clickToDiselect = Util.Boolean.parse($scope.clickToDiselect);
			this.cache = {};
			var ctrl = this;
			if($scope.loadUrl) {
				$http.get($scope.loadUrl).success(function(result) {
					var root;
					if(angular.isArray(result)){
						root = {
							name : $scope.root || 'ROOT',
							children : result,
							$level : -1,
							$node : true,
							$loaded:true,
							$root : true,
							$expanded : true
						};
					}else{
						root = result;
						root.$node = true;
						root.$level = 0;
						root.$root = true;
					}
					root.id = '$root';
					root.$hide = true;
					if($scope.rootVisiable == "true"){
						root.$hide = false;
					}
					
					ctrl.collect(root);
					$scope.root = root;
					//初始化函数
					if(angular.isFunction($scope.onInit)){
						$scope.onInit();
					}
				});
			}else{
				//初始化函数
				$scope.root = scope.treeData;
				if(angular.isFunction($scope.onInit)){
					$scope.onInit();
				}
			}
			
			if($scope.showCheckbox == 'true'){
				$scope.multiSelect = true;
			}
			if($scope.multiSelect === true)	{
				this.selection = [];
			}
			
			$scope.$isTreeNode = true;
			
			this.levelClass = function(node){
				return "hc-lvl-" + node.$level;
			}
			
			this.getOptionIcon = function(node){
				return $scope.optionIcon || 'fa fa-ellipsis-h';
			}
			
			this.getNodeIcon = function(node){
				if(!node) return;
				
				if(node.icon) return node.icon;
				
				if(node.leaf !== true) {
					if(node.$expanded) {
						return $scope.openFolderIcon || "fa fa-folder-open";
					} else {
						return $scope.folderIcon || "fa fa-folder";
					}
				} else {
					return $scope.leafIcon || "glyphicon glyphicon-file";
				}
			}

			this.getArrowClass = function(node) {
				if(!node) return;
				if(node.leaf !== true) {
					if(node.$expanded) {
						return "glyphicon glyphicon-triangle-bottom";
					} else {
						return "glyphicon glyphicon-triangle-right";
					}
				} else {
					return "glyphicon glyphicon-triangle-bottom hc-tree-arraw-hidden";
				}
			};

			this.select = function(node,fireEvent) {
				if($scope.multiSelect == true){
					this.selection.push(node);
				}else{
					if(this.selection){
						this.selection.$selected = false;
					}
					node.$selected = true;
					this.selection = node;
				}
				
				if(angular.isDefined($scope.cascadCheck)){
					cascadHaflCheckParent(node);
				}
				
				if(fireEvent !== false){
					this.fireSelectionChangeEvent();
				}
			};
			
			this.diselect = function(node,fireEvent){
				if($scope.multiSelect == true){
					this.selection.splice(this.selection.indexOf(node),1);
				}else{
					node.$selected = false;
					delete this.selection;
				}
				
				if(angular.isDefined($scope.cascadCheck)){
					cascadHaflCheckParent(node);
				}
				
				if(fireEvent !== false){
					this.fireSelectionChangeEvent();
				}
			}
			
			this.nodeDblClick = function(node,event){
				this.toggleNode(node,event)
				if(angular.isDefined($scope.onNodeDblClick) ){
					$scope.onNodeDblClick({
						node : node
					});
				}
			}
			
			this.optionClick = function(node,event){
				if(angular.isFunction($scope.onOptionClick) ){
					$scope.onOptionClick({
						node : node,
						event : event
					});
				}
			}

			this.nodeClick = function(node) {
				if(node.$selected !== true){
					this.select(node);
				}else if($scope.clickToDiselect === true){
					this.diselect(node);
				}
			};
			
			function cascadHaflCheckParent(node){
				var parent = node.$parent;
				if(parent){
					var halfChecked,len = parent.children.length;
					
					for(var i = 0; i < len; i ++){
						var child = parent.children[i];
						if(child.$selected || child.$halfChecked){
							halfChecked = true;
							break ;
						}
					}
					
					if(halfChecked !== true){
						parent.$halfChecked = false;
					}else{
						parent.$halfChecked = true;
					}
					
					cascadHaflCheckParent(parent);
				}
			}
			
			this.fireSelectionChangeEvent = function(){
				var selection = this.selection;
				if(angular.isDefined($scope.onSelectionChange) ){
					$scope.onSelectionChange({
						selection : this.selection
					});
				}
			}

			this.nodeCheck = function(node,event) {
				event.stopPropagation();
				if(event.target.tagName != 'INPUT'){
					return ;
				}
				node.$halfChecked = false;
				if($scope.cascadCheck == true){
					checkChildren(node);
				}
				
				if(node.$selected == true){
					this.select(node);
				}else{
					this.diselect(node);
				}
			};

			function checkChildren(node) {
				if(node.children) {
					node.children.forEach(function(it) {
						it.$selected = node.$selected;
						
						if(node.$selected == true){
							$scope.treeCtrl.select(it,false);
						}else{
							$scope.treeCtrl.diselect(it,false);
						}
						
						if(node.$selected === false){
							it.$halfChecked = false;
						}
						checkChildren(it);
					});
				}
			}
			
			this.collect = function(node){
				var me = this;
				if(node.children){
					node.children.forEach(function(n){
						n.$level = node.$level + 1;
						n.$parent = node;
						n.$node = true;
						if(n.children){
							me.collect(n);
						}else{
							me.cache[n.id] = n;
						}
					});
					if(node.$checked && $scope.cascadCheck){
						checkChildren(node);
						this.fireSelectionChangeEvent();
					}
				}
				this.cache[node.id] = node;
			}
			
			this.append = function(node,children){
				if(!node.children || remove === true) {
					node.children = [];
				}
				
				//不是数组构造成数组
				if(!angular.isArray(children)){
					children = [children];
				}
				
				node.children = node.children.concat(children);
				ctrl.collect(node);
			}
			
			this.remove = function(node){
				var ctrl = this;
				var children = node.$parent.children;
				delete node.$parent;
				children.splice(children.indexOf(node),1);
				
				delete ctrl.cache[node.id];  
				clearCache(node);
				
				function clearCache(node){
					if(!node.children) return ;
					node.children.forEach(function(n){
						delete ctrl.map[n.id]; 
						if(node.children){
							clearCache(node);
						}
					});
				}
				
			}
			
			this.load = function(node,remove){
				var ctrl = this;
				var params = {
						id: node.id
					};
				if(angular.isFunction($scope.onBeforeLoad)) {
					$scope.onBeforeLoad({
						node : node,
						params : params
					});
				}
				$http.get($scope.loadUrl, {
					params : params
				}).success(function(data) {
					if(data.length > 0){
						if(!node.children || remove === true) {
							node.children = [];
						}
						node.children = node.children.concat(data);
						node.leaf = false;
						ctrl.collect(node);
					}else{
						if(remove === true){
							node.children = [];
						}
						node.leaf = true;
					}
					node.$loaded = true;
					node.$expanded = true;
					
				}).error(function() {
					node.$loaded = true;
					node.$expanded = true;
				});
			}
			
			this.expand = function(node){
				if(node.$expanded !== true && node.leaf !== true && node.$loaded !== true && angular.isDefined($scope.loadAsyn) === true) {
					this.load(node);
				}else{
					node.$expanded = true;
				}
			};

			this.toggleNode = function(node,event) {
				event.stopPropagation();
				if(node.$expanded !== true){
					this.expand(node);
				}else{
					this.collapse(node);
				}
				
			};
			
			this.collapse = function(node){
				node.$expanded = false;
			}
			
			if(angular.isObject($scope.treeControl)){
				var tree = $scope.treeControl;
				tree.reload = function(node){
					ctrl.load(node,true);
				}
				tree.append = function(node,children){
					ctrl.append(node,children);
				}
				tree.remove = function(node){
					ctrl.remove(node);
				},
				tree.expand = function(node){
					ctrl.expand(node);
				},
				tree.collapse = function(node){
					ctrl.collapse(node);
				},
				tree.select = function(node){
					ctrl.select(node);
				},
				tree.diselect = function(node){
					ctrl.diselect(node);
				},
				tree.get = function(id){
					return ctrl.cache[id];
				},
				tree.getSelection = function(){
					return ctrl.selection;
				},
				tree.getRoot = function(){
					return $scope.root;
				}
			}
		}]
	};
}]);
// angular-tree-repeat - v0.0.1

// Include this first to define the module that the directives etc. hang off.
//

// sf-tree-repeat directive
// ========================
// Like `ng-repeat` but recursive
//
(function(){
  'use strict';
  // (part of the sf.treeRepeat module).
  var mod = angular.module('hc.ui');

  // Utility to turn the expression supplied to the directive:
  //
  //     a in b of c
  //
  // into `{ value: "a", collection: "b", root: "c" }`
  //
  function parseRepeatExpression(expression){
    var match = expression.match(/^\s*([\$\w]+)\s+in\s+([\S\s]*)\s+of\s+([\S\s]*)$/);
    if (! match) {
      throw new Error("Expected sfTreepeat in form of"+
                      " '_item_ in _collection_ of _root_' but got '" +
                      expression + "'.");
    }
    return {
      value: match[1],
      collection: match[2],
      root: match[3]
    };
  }

  // The `sf-treepeat` directive is the main and outer directive. Use this to
  // define your tree structure in the form `varName in collection of root`
  // where:
  //  - varName is the scope variable used for each node in the tree.
  //  - collection is the collection of children within each node.
  //  - root is the root node.
  //
  mod.directive('sfTreepeat', ['$log', function($log) {
    return {
      restrict: 'A',
      // Use a scope to attach the node model
      scope: true,
      // and a controller to communicate the template params to `sf-treecurse`
      controller: ['$scope', '$attrs',
        function TreepeatController($scope, $attrs){
          var ident = this.ident = parseRepeatExpression($attrs.sfTreepeat);
          $log.info("Parsed '%s' as %s", $attrs.sfTreepeat, JSON.stringify(this.ident));
          // Keep the root node up to date.
          $scope.$watch(this.ident.root, function(v){
            $scope[ident.value] = v;
          });
        }
      ],
      // Get the original element content HTML to use as the recursive template
      compile: function sfTreecurseCompile(element){
        var template = element.html();
        return {
          // set it in the pre-link so we can use it lower down
          pre: function sfTreepeatPreLink(scope, iterStartElement, attrs, controller){
            controller.template = template;
          }
        };
      }
    };
  }]);

  // The `sf-treecurse` directive is a little like `ng-transclude` in that it
  // signals where to insert our recursive template
  mod.directive('sfTreecurse', ['$compile', function($compile){
    return {
      // which must come from a parent `sf-treepeat`.
      require: "^sfTreepeat",
      link: function sfTreecursePostLink(scope, iterStartElement, attrs, controller) {
        // Now we stitch together an element containing a vanila repeater using
        // the values from the controller.
        var build = [
          '<', iterStartElement.context.tagName, ' ng-repeat="',
          controller.ident.value, ' in ',
          controller.ident.value, '.', controller.ident.collection,
          '">',
          controller.template,
          '</', iterStartElement.context.tagName, '>'];
        var el = angular.element(build.join(''));
        // We swap out the element for our new one and tell angular to do its
        // thing with that.
        iterStartElement.replaceWith(el);
        $compile(el)(scope);
      }
    };
  }]);

}());


'use strict';

/**
 * General-purpose validator for ngModel.
 * angular.js comes with several built-in validation mechanism for input fields (ngRequired, ngPattern etc.) but using
 * an arbitrary validation function requires creation of a custom formatters and / or parsers.
 * The ui-validate directive makes it easy to use any function(s) defined in scope as a validator function(s).
 * A validator function will trigger validation on both model and input changes.
 *
 * @example <input ui-validate=" 'myValidatorFunction($value)' ">
 * @example <input ui-validate="{ foo : '$value > anotherModel', bar : 'validateFoo($value)' }">
 * @example <input ui-validate="{ foo : '$value > anotherModel' }" ui-validate-watch=" 'anotherModel' ">
 * @example <input ui-validate="{ foo : '$value > anotherModel', bar : 'validateFoo($value)' }" ui-validate-watch=" { foo : 'anotherModel' } ">
 *
 * @param ui-validate {string|object literal} If strings is passed it should be a scope's function to be used as a validator.
 * If an object literal is passed a key denotes a validation error key while a value should be a validator function.
 * In both cases validator function should take a value to validate as its argument and should return true/false indicating a validation result.
 */
angular.module('ui.validate',[]).directive('uiValidate', function () {

  return {
    restrict: 'A',
    require: 'ngModel',
    link: function (scope, elm, attrs, ctrl) {
      var validateFn, validators = {},
          validateExpr = scope.$eval(attrs.uiValidate);

      if (!validateExpr){ return;}

      if (angular.isString(validateExpr)) {
        validateExpr = { validator: validateExpr };
      }

      angular.forEach(validateExpr, function (exprssn, key) {
        validateFn = function (valueToValidate) {
          var expression = scope.$eval(exprssn, { '$value' : valueToValidate });
          if (angular.isObject(expression) && angular.isFunction(expression.then)) {
            // expression is a promise
            expression.then(function(){
              ctrl.$setValidity(key, true);
            }, function(){
              ctrl.$setValidity(key, false);
            });
            return valueToValidate;
          } else if (expression) {
            // expression is true
            ctrl.$setValidity(key, true);
            return valueToValidate;
          } else {
            // expression is false
            ctrl.$setValidity(key, false);
            return valueToValidate;
          }
        };
        validators[key] = validateFn;
        ctrl.$formatters.push(validateFn);
        ctrl.$parsers.push(validateFn);
      });

      function apply_watch(watch)
      {
          //string - update all validators on expression change
          if (angular.isString(watch))
          {
              scope.$watch(watch, function(){
                  angular.forEach(validators, function(validatorFn){
                      validatorFn(ctrl.$modelValue);
                  });
              });
              return;
          }

          //array - update all validators on change of any expression
          if (angular.isArray(watch))
          {
              angular.forEach(watch, function(expression){
                  scope.$watch(expression, function()
                  {
                      angular.forEach(validators, function(validatorFn){
                          validatorFn(ctrl.$modelValue);
                      });
                  });
              });
              return;
          }

          //object - update appropriate validator
          if (angular.isObject(watch))
          {
              angular.forEach(watch, function(expression, validatorKey)
              {
                  //value is string - look after one expression
                  if (angular.isString(expression))
                  {
                      scope.$watch(expression, function(){
                          validators[validatorKey](ctrl.$modelValue);
                      });
                  }

                  //value is array - look after all expressions in array
                  if (angular.isArray(expression))
                  {
                      angular.forEach(expression, function(intExpression)
                      {
                          scope.$watch(intExpression, function(){
                              validators[validatorKey](ctrl.$modelValue);
                          });
                      });
                  }
              });
          }
      }
      // Support for ui-validate-watch
      if (attrs.uiValidateWatch){
          apply_watch( scope.$eval(attrs.uiValidateWatch) );
      }
    }
  };
});

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
'use strict';

/* Filters */
// need load the moment.js to use this filter. 
angular.module('hc.util')
  .filter('fromNow', function() {
    return function(date) {
      return moment(date).fromNow();
    }
  });
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
'use strict';
/* Filters */
// need load the moment.js to use this filter.
angular.module('hc.util').filter('trustHtml', ['$sce',function($sce) {
	return function (input){
	       return $sce.trustAsHtml(input); 
	   } 
}]);
angular.module('hc.common',[]).controller('SelectUserCtrl', [
          '$rootScope',
   		'$scope',
   		'$http',
   		'$modal',
   		'$modalInstance',
   		'options',
   		function($rootScope,$scope,$http,$modal,$modalInstance,options) {
    	  $scope.loading = false;
    	  $scope.tree = {};
    	  $scope.onTreeInit = function(){
				var root = $scope.tree.getRoot();
				$scope.tree.select(root);
		  }
    	  $scope.multiple = options.multiple;
    	  
    	  $scope.clear = function(){
    		  $scope.searching = false;
    		  $scope.condition = null;
    	  }
    	  
    	  $scope.selected = [];
    	  $scope.selectedIds = [];
    	  $scope.select = function(user){
    		  if($scope.selectedIds.indexOf(user.id) >= 0){
    			  return ;
    		  }
    		  if($scope.multiple === false){
    			  $scope.selected = [];
    	    	  $scope.selectedIds = [];
    		  }
    		  $scope.selected.push(user);
    		  $scope.selectedIds.push(user.id);
    	  }
    	  
    	  $scope.remove = function(user){
    		  if($scope.selectedIds.indexOf(user.id) < 0){
    			  return ;
    		  }
    		  $scope.selected.splice($scope.selected.indexOf(user),1);
    		  $scope.selectedIds.splice( $scope.selectedIds.indexOf(user.id),1);
    	  }
    	  
    	  $scope.search = function(){
    		  	if(!$scope.condition){
    		  		return ;
    		  	}
    		    $scope.searching = true;
    		    $scope.loading = true;
	  			$http.get("/organization/user/search",{
	  				params : {
	  					condition : $scope.condition
	  				}
	  			}).success(function(data, status, headers, config) {
	  				$scope.loading = false;
	  				if (data.success) {
	  					$scope.result = data.users;
	  				} else {
	  					$scope.toaster.pop('error', '提示', data.message);
	  				}
	  			}).error(function(data,status) {
	  				$scope.loading = false;
	  				$scope.toaster.pop('error', '提示', '加载失败：' + status);
	  			});
    	  }
    	  
    	  $scope.ok = function(){
    		  $modalInstance.close($scope.selected);
    	  }
    	  
          $scope.loadUser = function(dept){
	          	$scope.loading = true;
	  			$http.get("/organization/user/list",{
	  				params : {
	  					departmentId : dept.id
	  				}
	  			}).success(function(data, status, headers, config) {
	  				$scope.loading = false;
	  				if (data.success) {
	  					$scope.users = data.users;
	  				} else {
	  					$scope.toaster.pop('error',  data.message);
	  				}
	  			}).error(function(data,status) {
	  				$scope.loading = false;
	  				$scope.toaster.pop('error', '加载失败：' + status);
	  			});
	  		}
          $scope.close = function(){
       		$modalInstance.dismiss('cancel');
         }
   }]);

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
