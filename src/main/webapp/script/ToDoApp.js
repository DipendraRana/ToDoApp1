var ToDo = angular.module('ToDo', [ 'ui.router', 'ngResource', 'ngSanitize',
		'ui.bootstrap', 'toastr', 'ngFileUpload' , 'base64' , 'ngImgCrop']);
ToDo.config([ '$stateProvider', '$urlRouterProvider',

function($stateProvider, $urlRouterProvider) {

	$stateProvider.state('login', {
		url : '/login',
		templateUrl : 'template/login.html',
		controller : 'loginController'
	}).state('register', {
		url : '/registration',
		templateUrl : 'template/registration.html',
		controller : 'registrationController'
	}).state('home', {
		url : '/home',
		templateUrl : 'template/home.html',
		controller : 'homeController'
	}).state('socialtohome', {
		url : '/home/',
		templateUrl : 'template/home.html',
		controller : 'homeController'
	}).state('enterEmail', {
		url : '/enterEmail',
		templateUrl : 'template/emailEnter.html',
		controller : 'enterEmailController'
	}).state('resetPassword', {
		url : '/resetPassword/',
		templateUrl : 'template/resetPassword.html',
		controller : 'resetPasswordController'
	}).state('intermediate', {
		url : '/intermediate',
		controller : 'intermediateController'
	}).state('trash', {
		url : '/trash',
		templateUrl : 'template/trash.html',
		controller : 'homeController'
	}).state('archive', {
		url : '/archive',
		templateUrl : 'template/archive.html',
		controller : 'homeController'
	}).state('labels', {
		url : '/{labelNames}',
		templateUrl : 'template/labels.html',
		controller : 'homeController'
	}).state('reminders', {
		url : '/reminders',
		templateUrl : 'template/reminders.html',
		controller : 'homeController'
	}).state('search',{
		url : '/search',
		templateUrl : 'template/searchpage.html',
		controller : 'homeController'
	});
	$urlRouterProvider.otherwise('login');
} ]);