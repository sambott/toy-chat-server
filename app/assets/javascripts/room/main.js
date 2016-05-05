define(['angular', './routes', './controller'], function(angular) {
  'use strict';

  return angular.module('yourprefix.room', ['ngRoute', 'room.routes']);
});
