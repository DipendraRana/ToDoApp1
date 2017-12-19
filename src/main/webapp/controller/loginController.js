var ToDo = angular.module("ToDo");
ToDo.controller('loginController', function($scope, loginService, $location) {
	$scope.loginUser = function() {
		var message = loginService.loginUser($scope.user, $scope.error);
		message.then(function(response) {
			console.log(response.data.message);
			if(response.data.message=="succesfull"){
				localStorage.setItem('token', response.headers('token'));
				$location.path("/home");
			} else 
				document.getElementById("loginfailedmessage").innerHTML = "Login Failed";
		}, function() {
			$scope.error = response.data.message;
		});
	}
});