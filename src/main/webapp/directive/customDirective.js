var ToDo = angular.module('ToDo');
ToDo.directive('topNavBar',function(){
	return{
		templateUrl : 'template/topNavBar.html'
	}
});

ToDo.directive('sideNavBar', function() {
	return{
		templateUrl : 'template/sideNavBar.html'
	}
});

ToDo.directive('noteExtras', function() {
	return{
		templateUrl : 'template/noteExtras.html'
	}
});

ToDo.directive('noteIcons', function() {
	return{
		templateUrl : 'template/noteIcons.html'
	}
});

ToDo.directive('urlMetaData', function() {
	return{
		templateUrl : 'template/urlMetaData.html'
	}
});

ToDo.directive('pinned', function() {
	return{
		templateUrl : 'template/pinnedTemplate.html'
	}
});

ToDo.directive('notPinned', function() {
	return{
		templateUrl : 'template/notPinnedTemplate.html'
	}
});

ToDo.directive('contenteditable', [ '$sce', function($sce) {
	
	return {
		  restrict: 'A', // only activate on element attribute
		  require: '?ngModel', // get a hold of NgModelController
		  link: function(scope, element, attrs, ngModel) {
		      if (!ngModel) return; // do nothing if no ng-model

		    // Specify how UI should be updated
		      ngModel.$render = function() {
		        element.html($sce.getTrustedHtml(ngModel.$viewValue || ''));
		        read(); // initialize
		      };

		      // Listen for change events to enable binding
		      element.on('blur keyup change', function() {
		        scope.$evalAsync(read);
		      });

		      // Write data to the model
		      function read() {
		        var html = element.html();
		        // When we clear the content editable the browser leaves a <br> behind
		        // If strip-br attribute is provided then we strip this out
		        if ( attrs.stripBr && html == '<br>' ) {
		          html = '';
		        }
		        ngModel.$setViewValue(html);
		      }
		    }
		  };
} ]);

ToDo.directive("ngFileSelect", function(fileReader, $timeout) {
	return {
		scope : {
			ngModel : '='
		},
		link : function($scope, el) {
			function getFile(file) {
				fileReader.readAsDataUrl(file, $scope).then(function(result) {
					$timeout(function() {
						$scope.ngModel = result;
					});
				});
			}

			el.bind("change", function(e) {
				var file = (e.srcElement || e.target).files[0];
				getFile(file);
			});
		}
	};
});