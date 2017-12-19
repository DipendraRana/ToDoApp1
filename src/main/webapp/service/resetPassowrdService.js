var ToDo = angular.module('ToDo');
ToDo.factory('resetPasswordService', function($resource) {
	var data = $resource(
			'http://localhost:8080/ToDo/forgotPassword/resetPassword/:Token', {
				Token : '@token'
			}, {
				update : {
					method : 'PUT',
					transformResponse : function(data) {
						response = {}
						response.data = angular.fromJson(data);
						return response;
					}
				}
			});
	return data;
});