var ToDo = angular.module("ToDo");
ToDo.service('intermediateService', function($http) {
	var details = {};
	details.getToken = function() {
		return $http({
			url:'intermediateLogin',
			method: 'GET'
		})
	}
	return details;
});