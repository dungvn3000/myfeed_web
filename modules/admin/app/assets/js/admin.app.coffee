app = angular.module('myFeed.admin', ['myFeed.admin.config', 'myFeed.admin.filters', 'myFeed.admin.services', 'myFeed.admin.directives',
   'myFeed.admin.controllers', 'ui.utils', 'ui.bootstrap', 'ui.compat', 'http-loading-interceptor'])

app.config ($routeProvider, $stateProvider, $urlRouterProvider, configs) ->

  #Curd module config
  for config in configs
    $stateProvider.state config.entity, {
      url: '/' + config.entity
      'abstract': true
      templateUrl: "/admin/#{ config.entity }/partials/index"
    }

    $stateProvider.state config.entity + '.list', {
      url: ''
      templateUrl: "/admin/#{ config.entity }/partials/list"
      detailTemplateUrl: "/admin/#{ config.entity }/partials/detail"
      entity: config.entity
      controller: 'ListController'
    }


  $stateProvider.state 'maintenance', {
    url: '/maintenance'
    templateUrl: '/admin/maintenance/partials/index'
  }

  $urlRouterProvider.otherwise("/feed")

app.run ($rootScope, $state, $stateParams) ->
  $rootScope.$state = $state
  $rootScope.$stateParams = $stateParams