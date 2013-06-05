module = angular.module('myFeed.admin.controllers', [])

module.controller 'HomeController', ($scope, $state) ->

module.controller 'FeedController', ($scope, $state, Feed) ->
  $scope.field = "name"
  $scope.value = ""
  $scope.sort = "_id"
  $scope.order = 1
  $scope.page = 1
  $scope.limit = 20

  query = (field, value, sort, order, page, limit) ->
    Feed.query {f: field, v: value, s: sort, o: order, p: page, l: limit}, (dataTabe) ->
      $scope.field = dataTabe.field
      $scope.value = dataTabe.value
      $scope.sort = dataTabe.sort
      $scope.order = dataTabe.order
      $scope.page = dataTabe.page
      $scope.limit = dataTabe.limit
      $scope.data = dataTabe.data
      $scope.totalPage = dataTabe.totalPage

  $scope.query = query

  query($scope.field, $scope.value, $scope.sort, $scope.order, $scope.page, $scope.limit)
