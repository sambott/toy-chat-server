/**
 * Home controllers.
 */
define([], function() {
  'use strict';

  /** Controls the index page */
  var HomeCtrl = function($scope, $rootScope, $location, helper, playRoutes) {
    console.log(helper.sayHi());
    $rootScope.pageTitle = 'Welcome';
    playRoutes.controllers.Chat.getActiveRooms().get().then(function(response) {
      $scope.rooms = response;
    });
  };
  HomeCtrl.$inject = ['$scope', '$rootScope', '$location', 'helper', 'playRoutes'];

  /** Controls the header */
  var HeaderCtrl = function($scope) {
    $scope.title = 'Chat ui';
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
