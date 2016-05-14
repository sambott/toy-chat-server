/**
 * Home controllers.
 */
define([], function() {
  'use strict';

  /** Controls the index page */
  var HomeCtrl = function($scope, $rootScope) {

    $rootScope.pageTitle = 'Chat App';

  };
  HomeCtrl.$inject = ['$scope', '$rootScope'];

  /** Controls the header */
  var HeaderCtrl = function($scope) {
    $scope.title = 'Chat App';
  };
  HeaderCtrl.$inject = ['$scope'];

  /** Controls the footer */
  var FooterCtrl = function(/*$scope*/) {
  };
  //FooterCtrl.$inject = ['$scope'];

  return {
    HeaderCtrl: HeaderCtrl,
    FooterCtrl: FooterCtrl,
    HomeCtrl: HomeCtrl
  };

});
