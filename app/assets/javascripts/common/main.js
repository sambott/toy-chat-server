/**
 * Common functionality.
 */
define(['angular', './services/helper', './services/backend', './filters', './directives'],
    function(angular) {
  'use strict';

  return angular.module('chatapp.common',
    ['common.helper', 'common.backend', 'common.filters', 'common.directives']
  );
});
