app = angular.module('myFeed', ['myFeed.filters', 'myFeed.services', 'myFeed.directives', 'myFeed.controllers',
                                'ui.utils', 'infinite-scroll', 'ui.bootstrap','ui.compat'])

app.config ($routeProvider, $stateProvider, $urlRouterProvider) ->
  $stateProvider.state 'feed', {
    url: '/feed'
    views: {
      'feed.nav@' : {
        templateUrl: '/feed/partials/nav'
      }
      'feed.list@': {
        templateUrl: '/feed/partials/index'
      }
    }
  }

  $stateProvider.state 'feed.list', {
    url: '/:feedId'
    views: {
      'feed.list@': {
        templateUrl: '/feed/partials/list'
        controller: 'FeedController'
      }
    }
  }

  $stateProvider.state 'feed.list.detail', {
    url: '/:newsId'
    views: {
      'feed.detail@': {
        templateUrl: '/feed/partials/detail'
        controller: 'FeedController'
      }
    }
  }

  $urlRouterProvider.otherwise("/feed")

app.run ($rootScope, $state, $stateParams) ->
  $rootScope.$state = $state
  $rootScope.$stateParams = $stateParams