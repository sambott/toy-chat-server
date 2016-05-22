/**
 * A common directive.
 * It would also be ok to put all directives into one file, or to define one RequireJS module
 * that references them all.
 */
define(['angular', 'jquery'], function(angular, $) {
  'use strict';

  var mod = angular.module('common.directives', []);

  mod.directive('example', ['$log', function($log) {
    return {
      restrict: 'AE',
      link: function(/*scope, el, attrs*/) {
        $log.info('Here prints the example directive from /common/directives.');
      }
    };
  }]);

  mod.directive('scrollToBottom', function () {
      return {
        scope: {
          scrollToBottom: "="
        },
        link: function (scope, element) {
          scope.$watchCollection('scrollToBottom', function (newValue) {
            if (newValue)
            {
              $(element).scrollTop($(element)[0].scrollHeight);
            }
          });
        }
      };
    });

  return mod;
});
