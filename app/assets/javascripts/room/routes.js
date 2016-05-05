/**
 * Configure routes of user module.
 */
define(['angular', './controller', 'common'], function(angular, controller) {
  'use strict';

  var mod = angular.module('room.routes', ['yourprefix.common']);
  mod.config(['$routeProvider', function($routeProvider) {
    $routeProvider
      .when('/room/:room', {templateUrl:'/assets/javascripts/room/room.html', controller:controller.RoomCtrl});
  }]);
  return mod;
});
