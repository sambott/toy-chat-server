define(['angular', './routes', './controllers'], function(angular) {
  'use strict';

  return angular.module('chatapp.room', ['ngRoute', 'room.routes']);
});
