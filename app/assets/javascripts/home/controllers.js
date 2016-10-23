/**
 * Home controllers.
 */
define([], function() {
  'use strict';

  /** Controls the index page */
  var HomeCtrl = function($scope, $rootScope, $location) {

    $rootScope.pageTitle = 'Chat Client Mini-Hackathon';

    $scope.backendProtocol = $location.protocol() + '://';
    $scope.backendWsProtocol = $scope.backendProtocol.replace('http', 'ws');
    $scope.backendRoot = $location.host();
    var port = $location.port();
    if (
      ($scope.backendProtocol === 'http://' && port !== 80)||
      ($scope.backendProtocol === 'https://' && port !== 443)
    ) {
      $scope.backendRoot += ':' + String(port);
    }

  };
  HomeCtrl.$inject = ['$scope', '$rootScope', '$location'];

  /** Controls the header */
  var HeaderCtrl = function($scope) {
    $scope.title = 'Chat Client Mini-Hackathon';
  };
  HeaderCtrl.$inject = ['$scope'];

  /** Controls the footer */
  var FooterCtrl = function(/*$scope*/) {
  };
  //FooterCtrl.$inject = ['$scope'];

  return {
    HeaderCtrl: HeaderCtrl,
    FooterCtrl: FooterCtrl,
    HomeCtrl: HomeCtrl
  };

});
