/**
 * Common functionality.
 */
define(['angular', './services/helper', './services/playRoutes', './filters', './directives'],
    function(angular) {
  'use strict';

  return angular.module('chatapp.common',
    ['common.helper', 'common.playRoutes', 'common.filters', 'common.directives']
  );
});
