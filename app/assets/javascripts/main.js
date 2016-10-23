// `main.js` is the file that sbt-web will use as an entry point
(function (requirejs) {
  'use strict';

  // -- RequireJS config --
  requirejs.config({
    // Packages = top-level folders; loads a contained file named 'main.js"
    packages: ['common', 'home', 'room'],
    shim: {
      'jsRoutes': {
        deps: [],
        // it's not a RequireJS module, so we have to tell it what var is returned
        exports: 'jsRoutes'
      },
      // Hopefully this all will not be necessary but can be fetched from WebJars in the future
      'angular': {
        deps: ['jquery'],
        exports: 'angular'
      },
      'angular-route': ['angular'],
      'angular-cookies': ['angular'],
      'bootstrap': ['jquery'],
      'angular-websocket': ['angular']
    },
    paths: { // CDN replacement([]) only for classic webjars
      'requirejs': ['../lib/requirejs/require'],
      'jquery': '../lib/jquery/dist/jquery',
      'angular': '../lib/angular/angular',
      'angular-route': '../lib/angular-route/angular-route',
      'angular-cookies': '../lib/angular-cookies/angular-cookies',
      'bootstrap': '../lib/bootstrap/dist/js/bootstrap',
      'jsRoutes': ['/jsroutes'],
      'angular-websocket': '../lib/angular-websocket/angular-websocket'
    }
  });

  requirejs.onError = function (err) {
    console.log(err);
  };

  // Load the app. This is kept minimal so it doesn't need much updating.
  require(['angular', 'angular-cookies', 'angular-route', 'jquery', 'bootstrap', './app'],
    function (angular) {
      angular.bootstrap(document, ['app']);
    }
  );
})(requirejs);
