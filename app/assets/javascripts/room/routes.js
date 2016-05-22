/**
 * Configure routes of user module.
 */
define(['angular', './controllers', 'common'], function(angular, controllers) {
  'use strict';

  var mod = angular.module('room.routes', ['chatapp.common']);
  mod.config(['$routeProvider', function($routeProvider) {
    $routeProvider
      .when('/room', {templateUrl:'/assets/javascripts/room/room-picker.html', controller:controllers.RoomPickerCtrl})
      .when('/room/:room', {templateUrl:'/assets/javascripts/room/room.html', controller:controllers.RoomCtrl});
  }]);
  return mod;
});
