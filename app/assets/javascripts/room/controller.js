/**
 * Room controller.
 */
define([], function() {
  'use strict';

  /** Controls the index page */
  var RoomCtrl = function($scope, $routeParams, helper, playRoutes) {
    $scope.roomName = $routeParams.room;
    playRoutes.controllers.Chat.getLatest($scope.roomName).get().then(function(response) {
      $scope.latestMsgs = response.data;
    });
  };
  RoomCtrl.$inject = ['$scope', '$routeParams', 'helper', 'playRoutes'];

  return {
    RoomCtrl: RoomCtrl
  };

});
