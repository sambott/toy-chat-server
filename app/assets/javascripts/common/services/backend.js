define(['angular', 'require', 'jsRoutes'], function (angular, require, jsRoutes) {
  'use strict';

  var mod = angular.module('common.backend', []);

  mod.service('backend', ['$http', function ($http) {

    var wrapHttp = function (playFunction) {
      return function (/*arguments*/) {
        var routeObject = playFunction.apply(this, arguments);
        var httpMethod = routeObject.method.toLowerCase();
        var url = routeObject.url;
        var res = {
          method: httpMethod,
          url: url,
          absoluteUrl: routeObject.absoluteURL,
          webSocketUrl: routeObject.webSocketURL
        };
        res[httpMethod] = function (obj) {
          return $http[httpMethod](url, obj);
        };
        return res;
      };
    };

    // Add package object, in most cases 'controllers'
    var addPackageObject = function (packageName, service) {
      if (!(packageName in service)) {
        service[packageName] = {};
      }
    };

    // Add controller object, e.g. Application
    var addControllerObject = function (controllerKey, service) {
      addPackageObject(controllerKey, service);
    };

    var backendRoutes = {};

    // checks if the controllerKey starts with a lower case letter
    var isControllerKey = function (controllerKey) {
      return (/^[A-Z].*/.test(controllerKey));
    };

    var addRoutes = function (routeObject, jsRoutesObject) {
      for (var key in jsRoutesObject) {
        if (isControllerKey(key)) {
          var controller = jsRoutesObject[key];
          addControllerObject(key, routeObject);
          for (var methodKey in controller) {
            routeObject[key][methodKey] = wrapHttp(controller[methodKey]);
          }
        } else {
          addPackageObject(key, routeObject);
          addRoutes(routeObject[key], jsRoutesObject[key]);
        }
      }
    };

    addRoutes(backendRoutes, jsRoutes);
    return backendRoutes;
  }]);

  return mod;
});
