app = angular.module('myFeed.admin', ['myFeed.admin.filters', 'myFeed.admin.services', 'myFeed.admin.directives',
                                      'myFeed.admin.controllers', 'ui.utils', 'ui.bootstrap', 'ui.compat'])

app.config ($routeProvider, $stateProvider, $urlRouterProvider) ->
  $stateProvider.state 'feed', {
    'abstract': true
    url: '/feed'
    templateUrl: 'admin/feed/partials/index'
  }

  $stateProvider.state 'feed.list', {
    url: ''
    templateUrl: 'admin/feed/partials/list'
    controller: 'FeedController'
  }

  $stateProvider.state 'feed.detail', {
    url: '/:feedId'
    templateUrl: 'admin/feed/partials/detail'
    controller: 'FeedController'
  }

  $stateProvider.state 'maintenance', {
    url: '/maintenance'
    templateUrl: 'admin/maintenance/partials/index'
  }

  $urlRouterProvider.otherwise("/feed")

app.run ($rootScope, $state, $stateParams) ->
  $rootScope.$state = $state
  $rootScope.$stateParams = $stateParams