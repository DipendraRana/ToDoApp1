var ToDo = angular.module('ToDo');
ToDo.controller('resetPasswordController', [ '$scope', 'resetPasswordService',
		'$location', function($scope, resetPasswordService, $location) {
			$scope.token = $location.hash();
			$scope.resetPassword = function() {
				if ($scope.token != null) {
					var userPassword = {
						newPassword : $scope.user.newPassword,
						reEnterNewPassword : $scope.user.reEnterNewPassword
					};
					var message = resetPasswordService.update({
						Token : $scope.token
					}, userPassword).$promise.then(function(response){
						  if(response.data.message=="password is reset")
							  $location.path("/login");
						  else
							  $location.path("/resetPassword");
					})
					localStorage.removeItem("token");
				} else {
					$location.path("/login");
				}
			}

		} ]);