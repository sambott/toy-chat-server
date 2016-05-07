/**
 * Home controllers.
 */
define([], function() {
  'use strict';

  /** Controls the index page */
  var HomeCtrl = function($scope, $rootScope, $location, helper, playRoutes) {

    $rootScope.pageTitle = 'Chat App';
    $scope.newRoom='';

    playRoutes.controllers.Chat.getActiveRooms().get().then(function(response) {
      $scope.rooms = response.data;
    });

    $scope.onNewRoom = function () {
      if ($scope.newRoom) {
        $location.path('/room/' + $scope.newRoom);
      }
    };
  };
  HomeCtrl.$inject = ['$scope', '$rootScope', '$location', 'helper', 'playRoutes'];

  /** Controls the header */
  var HeaderCtrl = function($scope) {
    $scope.title = 'Chat App';
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
