/**
 * Room controller.
 */
define(['jsRoutes'], function(jsRoutes) {
  'use strict';

  /** Controls the index page */
  var RoomCtrl = function($scope, $routeParams, helper, playRoutes, $websocket) {

    $scope.roomName = $routeParams.room;
    $scope.userName = 'guest';
    $scope.newMessage = '';

    var wsUrl = jsRoutes.controllers.Chat.ws($scope.roomName).webSocketURL();

    var dataStream = $websocket(wsUrl);
    $scope.$on("$destroy", function handler() {
      dataStream.close();
    });

    playRoutes.controllers.Chat.getLatest($scope.roomName).get().then(function(response) {
      $scope.latestMsgs = response.data;
    }).then(function () {
      dataStream.onMessage(function(message) {
        $scope.latestMsgs.push(JSON.parse(message.data));
      });
    });

    $scope.onNewMessage = function () {
      if ($scope.userName && $scope.newMessage) {
        var msg = {user: $scope.userName, message: $scope.newMessage};
        playRoutes.controllers.Chat.postMessage($scope.roomName).post(msg);
      }
    };
  };
  RoomCtrl.$inject = ['$scope', '$routeParams', 'helper', 'playRoutes', '$websocket'];

  return {
    RoomCtrl: RoomCtrl
  };

});
