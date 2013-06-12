module = angular.module('myFeed.admin.services', ['ngResource'])

# This service using for common curd task so i used a common name is "Entry"
module.factory "Entry", ($resource, $state) ->
  actions =
    query:
      method: 'GET'

    update:
      method: 'PUT'
      isArray: true

    create:
      method: 'POST'
      isArray: true

    delete:
      method: 'DELETE'

  $resource '/admin/:entity/:id/:method', {}, actions