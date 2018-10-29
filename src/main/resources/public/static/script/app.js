angular.module('fileApp', ['ngRoute']);

angular.module("fileApp").controller("userController", function($scope, $http, $location, $interval) {
  $scope.interval = undefined;

  $scope.serverdomain = "http://mboxstorage.com";
  var USER_Endpoint = $scope.serverdomain + "/user";
  var DASHBOARD_Endpoint = $scope.serverdomain + "/dashboard.html";

  $scope.oauth2url = $scope.serverdomain + "/authentication/{enterprise}/login?redirecturl="+ window.location.origin + "/ssweb/launcher.html";

  $scope.launchGoogleOAuth2Flow123 = function(enterprise) {
   $scope.launchOAuth2Flow123("google");
  }

  $scope.launchFacebookOAuth2Flow123 = function(enterprise) {
     $scope.launchOAuth2Flow123("facebook");
  }

  $scope.launchOAuth2Flow123 = function(enterprise) {
    url = $scope.oauth2url.replace("{enterprise}", enterprise);
    if (localStorage.token) {
      localStorage.removeItem("token");
    }
    var height = window.screen.availHeight;
    var config = "width=600px,height="+height + 'px';
    window.open(url, "mbox", config);

    $scope.interval = $interval(function(){
                            $scope.checkLoginStatus();
                        }, 2000);
  }

  $scope.checkLoginStatus = function() {
      if (localStorage.token) {
        $interval.cancel($scope.interval);
        window.location = $scope.serverdomain + "/dashboard.html?token="+localStorage.token;
      }
    }

	$scope.formLogin123 = function() {
		var queryParamTemplate = "email={email}&password={password}";
    	var queryString = $scope.fillQueryParamSSWeb(queryParamTemplate)
    	
		var req = {
			method : 'GET',
			url : USER_Endpoint + "?"+queryString,
			headers : {
            'Content-Type' : "application/json"
          }
		};
		console.log('muthu request' + JSON.stringify(req));
		$http(req).then(function(response) {
			console.log('muthu response from server' + JSON.stringify(response));
			var accesstoken = response.data.token;
			var admin = response.data.admin;
			console.log('muthu response from server' + accesstoken);
			var redirecturl = DASHBOARD_Endpoint + "?token="+accesstoken;
			if (admin) {
			  redirecturl = redirecturl + "&admin=true";
			}
			window.location = redirecturl;
		}, function(error) {		
			console.log('muthu error response from server' + error);
			//window.location = "https://localhost:8443/ssweb/dashboard.html";
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
    			url : USER_Endpoint + "?"+queryString,
    			data : {},
    			headers : {
    				'Content-Type' : "application/json"
    			}
    		};
			console.log('muthu request' + JSON.stringify(req));
    		$http(req).then(function(response) {
    		  var access = response.data;
          console.log('muthu response from server' + access.token);
          window.location = DASHBOARD_Endpoint + "?token="+access.token;
    		}, function(error) {
    			console.log('muthu response from server' + response);
    		});
    };
});

angular.module("fileApp").directive('fileModel', ['$parse', function ($parse) {
  return {
     restrict: 'A',
     link: function(scope, element, attrs) {
        var model = $parse(attrs.fileModel);
        var modelSetter = model.assign;

        element.bind('change', function(){
           scope.$apply(function(){
              modelSetter(scope, element[0].files[0]);
           });
        });
     }
  };
}]);

angular.module("fileApp").controller('fileController', function($scope, $http) {
  $scope.serverdomain = "http://mboxstorage.com"
  
  var FILE_Endpoint = $scope.serverdomain + "/storage/file";
  var FILES_Endpoint = $scope.serverdomain + "/storage/files";
  var DASHBOARD_Endpoint = $scope.serverdomain + "/dashboard.html";

	$scope.uploadFile = function() {
    console.log('uploadFile called');
    var payload = new FormData();

    payload.append('description', $scope.description);
    payload.append('file', $scope.myFile);

    console.log('forming request');
    var req = {
      url : FILE_Endpoint,
      method : "POST",
      data : payload,
      headers : {
        'Content-Type': undefined,
        'Authorization': "Bearer " + $scope.getTokenQueryParameter()
      }
    };
    console.log('http call');
    $http(req).then(function(response) {
      console.log('file upload success' + response);
      window.location = DASHBOARD_Endpoint + "?token=" + $scope.getTokenQueryParameter()

    }, function(response) {
      console.log('file upload error' + JSON.stringify(response));
		});
	};

  $scope.deleteFile = function(file) {
    $http({
      method : 'DELETE',
      url : FILE_Endpoint + "?id=" + file.id,
      headers : {
        'Content-Type' : "application/json",
        'Authorization': "Bearer " + $scope.getTokenQueryParameter()
      }
    }).then(function(response) {
      console.log('file delete success' + JSON.stringify(response));
      window.location = DASHBOARD_Endpoint + "?token=" + $scope.getTokenQueryParameter()
    }, function(error) {
      console.log('file delete failed' + error);
    });
  };

  $scope.getQueryParameters = function(){
    var url = location.search;
    var qs = url.substring(url.indexOf('?') + 1).split('&');
    for(var i = 0, result = {}; i < qs.length; i++){
        qs[i] = qs[i].split('=');
        result[qs[i][0]] = decodeURIComponent(qs[i][1]);
    }
    return result;
  };

  $scope.getTokenQueryParameter = function(){
    var result = $scope.getQueryParameters();
    return result['token'];
  };

  $scope.getAdminQueryParameter = function(){
    var result = $scope.getQueryParameters();
    return result['admin'];
  };

  $scope.displayFiles = function() {
    $scope.admin = $scope.getAdminQueryParameter();
    var req = {
      method : 'GET',
      url : FILES_Endpoint,
      params : {
        owner : $scope.email
      },
      headers : {
        'Content-Type' : "application/json",
        'Authorization': "Bearer " + $scope.getTokenQueryParameter()
      }
    }
    $http(req).then(function(response) {
      console.log('received response:' + JSON.stringify(response.data));
      $scope.files = response.data;
      console.log('received response:' + JSON.stringify($scope.files));
    }, function(response) {
      console.log('received error' + response);
      console.log($scope);
    });
  };
});