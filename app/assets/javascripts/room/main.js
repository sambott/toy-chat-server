define(['angular', './routes', './controller'], function(angular) {
  'use strict';

  return angular.module('chatapp.room', ['ngRoute', 'room.routes']);
});
