module = angular.module('myFeed.admin.services', ['ngResource'])

module.factory "Feed", ($resource) ->
  actions =
    query:
      method: 'GET'

  $resource '/admin/feed/:feedId/:method', {}, actions