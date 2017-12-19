var ToDo = angular.module('ToDo');
ToDo.controller('enterEmailController', function($scope, emailEnteringService) {
	$scope.enterEmail = function() {
		var message = emailEnteringService.enterEmail($scope.user);
		message.then(function(response) {
			console.log(response.data.message);
			if (response.data.message == "Email-Sent")
				$scope.message = response.data.message + " to "
						+ $scope.user.emailId;
			else
				$scope.message = response.data.message;
			/* localStorage.setItem('token', response.headers('token')); */
		});
	}

});