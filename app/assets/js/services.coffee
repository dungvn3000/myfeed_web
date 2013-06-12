module = angular.module('myFeed.services', ['ngResource'])

module.factory "Feed", ($resource) ->
  actions =
    all:
      method: 'GET'
      isArray: true

    getNews:
      method: 'GET'
      params:
        method: 'getNews'
      isArray: true

    mark:
      method: 'POST'
      params:
        method: 'mark'

    create:
      method: 'POST'
      isArray: true

  $resource '/feed/:feedId/:method', {}, actions


module.factory "News", ($resource) ->
  actions =
    query:
      method: 'GET'

  $resource '/news/:newsId/:method', {}, actions
