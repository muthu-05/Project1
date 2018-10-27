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
				if(file.size > 10485760) {
					$scope.error = true;
					$scope.errorMessage = "File Upload Failed! File Size is more than 10MB!";
					return;
				}
				file.upload = Upload.upload({
					url : "/file",
					type : "POST",
					data : {
						file : file,
						title : file.name,
					}
				});
				file.upload.then(function(response) {
					$scope.success = true;
					$scope.successMessage = "File Uploaded Successfully";

				}, function(response) {
					$scope.error = true;
					$scope.errorMessage = "File Upload Failed!";
				});
			};
	$scope.boolConvert = function (value) {
        $scope[value] = !$scope[value];
    };
    $scope.deleteFile = function(file) {
				$http({
					method : 'DELETE',
					url : "/file",
					data : {
						id : file.id,
					},
					headers : {
						'Content-Type' : "application/json"
					}
				}).then(function(response) {
							$scope.success = true;
							$scope.successMessage = "File deleted successfully!";

						}, function(error) {
							$scope.error = true;
							$scope.errorMessage = "File delete failed! Please try again after sometime!";
						});
			};
			
});