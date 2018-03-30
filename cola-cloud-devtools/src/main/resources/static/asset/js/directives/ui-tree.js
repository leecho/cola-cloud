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
				'<a href="javascript:;" class="hc-lvl-{{node.$level}}" ng-class="{\'selected\':node.$selected || node.$checked, \'hidden\':node.$root && !node.rootVisiable}" ng-dblclick="treeCtrl.nodeDblClick(node,$event)" ng-click="treeCtrl.nodeClick(node)">' ;
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
					console.log(ctrl.cache);
					
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