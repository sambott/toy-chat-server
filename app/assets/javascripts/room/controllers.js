/**
 * Room controller.
 */
define(['jsRoutes'], function(jsRoutes) {
  'use strict';

  /** Controls the room picker page */
  var RoomPickerCtrl = function($scope, $rootScope, $location, helper, playRoutes) {

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
  RoomPickerCtrl.$inject = ['$scope', '$rootScope', '$location', 'helper', 'playRoutes'];

  /** Controls the header */
  var HeaderCtrl = function($scope) {
    $scope.title = 'Chat App';
  };
  HeaderCtrl.$inject = ['$scope'];

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
    RoomPickerCtrl: RoomPickerCtrl,
    RoomCtrl: RoomCtrl
  };

});
