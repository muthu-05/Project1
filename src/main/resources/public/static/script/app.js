var app = angular.module('fileApp', ['ngRoute']);
app.config(function($routeProvider) {

	$routeProvider.when('/', {
		templateUrl : 'login.html',
		controller : 'userController'
	}).when('/storage/files', {
		templateUrl : 'dashboard.html',
		controller : 'fileController'
	}).when('/user', {
		templateUrl : 'signup.html',
		controller : 'userController'
	}).otherwise({
		redirectTo : "/"
	});
});
app.controller("userController", function($scope, $http, $location) {
	$scope.formLogin123 = function() {
		var queryParamTemplate = "email={email}&password={password}";
    	var queryString = $scope.fillQueryParamSSWeb(queryParamTemplate)
    	
		var req = {
			method : 'GET',
			url : "/user?"+queryString
		};
		console.log('muthu request' + JSON.stringify(req));
		$http(req).then(function(response) {
			console.log('muthu response from server' + response);	
			window.location = "https://localhost:8443/ssweb/dashboard.html";
		}, function(error) {		
			console.log('muthu error response from server' + error);
			window.location = "https://localhost:8443/ssweb/dashboard.html";
		});
	};
	$scope.fillQueryParamSSWeb = function(queryParamTemplate) {
		var queryParam = queryParamTemplate;
		if ($scope.email) {
			queryParam = queryParam.replace("{email}", $scope.email);
		}
		if ($scope.firstname) {
			queryParam = queryParam.replace("{firstname}", $scope.firstname);
		}
		if ($scope.lastname) {
			queryParam = queryParam.replace("{lastname}", $scope.lastname);
		}
		if ($scope.password) {
			queryParam = queryParam.replace("{password}", $scope.password);
		}
		return queryParam;
	}
    $scope.formSignUp123 = function() {
    	var queryParamTemplate = "firstname={firstname}&lastname={lastname}&email={email}&password={password}";
    	var queryString = $scope.fillQueryParamSSWeb(queryParamTemplate)
    	var req = {
    			method : 'POST',
    			url : "/user?"+queryString,
    			data : {},
    			headers : {
    				'Content-Type' : "application/json"
    			}
    		};
			console.log('muthu request' + JSON.stringify(req));
    		$http(req).then(function(response) {
    			console.log('muthu response from server' + response);	
    			window.location = "https://localhost:8443/ssweb/dashboard.html";
    		}, function(error) {
    			console.log('muthu response from server' + response);	
    			window.location = "https://localhost:8443/ssweb/dashboard.html";
    		});
    };
});
app.controller('fileController', function($scope, $http,$location) {
	$scope.uploadFile = function(file) {
				var req = {
                    url : "/file",
					type : "POST",
					data : {
						file : file,
						fileName : file.name,
						description : $scope.description,
						owner : $scope.email
					}
				};
				$http(req).then(function(response) {
					console.log('file upload success' + response);	
    			window.location = "https://localhost:8443/ssweb/dashboard.html";

				}, function(response) {
					console.log('file upload error' + response);	
    			window.location = "https://localhost:8443/ssweb/dashboard.html";
			});
			};
    $scope.deleteFile = function(file) {
				$http({
					method : 'DELETE',
					url : "/file",
					data : {
						id : file.id
					},
					headers : {
						'Content-Type' : "application/json"
					}
				}).then(function(response) {
							console.log('file delete success' + response);	
    			window.location = "https://localhost:8443/ssweb/dashboard.html";
						}, function(error) {
							console.log('file delete success' + response);	
    			window.location = "https://localhost:8443/ssweb/dashboard.html";
						});
			};
			$scope.displayFiles = function() {
				var req = {
					method : 'GET',
					url : "/files",
					params : {
						owner : $scope.email
					},
					headers : {
						'Content-Type' : "application/json"
					}
				}
				$http(req).then(function(response) {
					console.log('files displaying' + response);	
    			window.location = "https://localhost:8443/ssweb/dashboard.html";

				}, function(response) {
					console.log('files display error' + response);	
    			window.location = "https://localhost:8443/ssweb/dashboard.html";
			});
			};
		});
