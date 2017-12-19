var ToDo = angular.module('ToDo');
ToDo.factory('emailEnteringService', function($http) {
	var details = {};
	details.enterEmail = function(user) {
		return $http({
			method : 'PUT',
			url : 'forgotPassword',
			data : user
		})
	}
	return details;
});