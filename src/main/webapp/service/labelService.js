var ToDo = angular.module('ToDo');
ToDo.factory('labelService', function($http) {
	var labels={};
	labels.service = function(url, method, token,label) {
		return $http({
			method : method,
			url : url,
			data : label,
			headers : {
				'token' : token
			}

		});
	}
	return labels;
});