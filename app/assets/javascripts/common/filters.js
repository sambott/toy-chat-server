/** Common filters. */
define(['angular'], function(angular) {
  'use strict';

  function timeConverter(UNIX_timestamp){
    var a = new Date(UNIX_timestamp * 1000);
    var months = ['Jan','Feb','Mar','Apr','May','Jun','Jul','Aug','Sep','Oct','Nov','Dec'];
    var year = a.getFullYear();
    var month = months[a.getMonth()];
    var date = a.getDate();
    var hour = a.getHours();
    var min = a.getMinutes();
    var sec = a.getSeconds();
    return date + ' ' + month + ' ' + year + ' ' + hour + ':' + min + ':' + sec;
  }

  var mod = angular.module('common.filters', []);
  /**
   * Extracts a given property from the value it is applied to.
   * {{{
   * (user | property:'name')
   * }}}
   */
  mod.filter('property', function(value, property) {
    if (angular.isObject(value)) {
      if (value.hasOwnProperty(property)) {
        return value[property];
      }
    }
  });

  /**
   * Formats a Chat Message
   */
  mod.filter('chatFormatter', function () {
    return function(value) {
      var timestr = timeConverter(value.dateTime);
      return '<p>' + timestr + '<br><b>' + value.user + '</b>:: ' + value.message + '</p>';
    };
  });

  return mod;
});
