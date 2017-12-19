var ToDo=angular.module("ToDo");
ToDo.controller('registrationController',function($scope,registrationService,$location){
	$scope.registerUser=function(){
		var message=registrationService.registerUser($scope.user);
		message.then(function(response){
			console.log(response.data.message);
			if(response.data.message=='registration succesfull')
				$location.path("login");
			else
				document.getElementById("registrationfailedmessage").innerHTML = "Registration Failed";
		});
	}
});