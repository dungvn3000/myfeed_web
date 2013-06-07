module = angular.module('myFeed.admin.controllers', ['ui.bootstrap'])

module.controller 'HomeController', ($scope, $state) ->

module.controller 'ListController', ($scope, $state, $dialog, Entry) ->
  # this value will be init by view when it render please do not init it
  # $scope.field = ""
  $scope.value = ""
  $scope.sort = "_id"
  $scope.order = 1
  $scope.page = 1
  $scope.limit = 20

  $scope.add = (id) ->
    opts =
      backdrop: true
      keyboard: true
      dialogFade: true
      backdropClick: true
      templateUrl: $state.current.detailTemplateUrl
      controller: 'DetailController'
      id: id
      reload: $scope.reload
    d = $dialog.dialog(opts)
    d.open()

  $scope.delete = (_id) ->
    title = 'Delete message'
    msg = "Do you want to delete entry id #{ _id }"
    btns = [{result:'cancel', label: 'Cancel'}, {result:'ok', label: 'OK', cssClass: 'btn-primary'}]
    $dialog.messageBox(title, msg, btns).open().then (result) ->
      if result is 'ok'
        Entry.delete {entity: $state.current.entity, id: _id}, () -> $scope.reload()

  query = (field, value, sort, order, page, limit) ->
    Entry.query {entity: $state.current.entity, f: field, v: value, s: sort, o: order, p: page, l: limit}, (dataTabe) ->
      $scope.field = dataTabe.field
      $scope.value = dataTabe.value
      $scope.sort = dataTabe.sort
      $scope.order = dataTabe.order
      $scope.page = dataTabe.page
      $scope.limit = dataTabe.limit
      $scope.data = dataTabe.data
      $scope.totalPage = dataTabe.totalPage

  reload = () ->
    query($scope.field, $scope.value, $scope.sort, $scope.order, $scope.page, $scope.limit)

  $scope.query = query
  $scope.reload = reload

  reload()

module.controller 'DetailController', ($scope, $state, dialog, Entry) ->
  if dialog.options.id
    $scope.entry = Entry.query {entity: $state.current.entity, id: dialog.options.id}

  $scope.save = ->
    error = (result) ->
      $scope.errors = result.data
    success = ->
      #reload data in table
      dialog.options.reload()
      dialog.close()

    if dialog.options.id
      Entry.update {entity: $state.current.entity, id: $scope.entry._id}, $scope.entry, success, error
    else
      Entry.create {entity: $state.current.entity}, $scope.entry, success, error

  $scope.close = ->
    dialog.close()

  $scope.getMsg = (key) ->
    msg = ""
    if $scope.errors
      for error in $scope.errors
        if error.key is key
          msg += error.msg + ", "
    if msg.length > 0 then msg.substring(0, msg.length - 2) else msg

  $scope.getClass = (key) ->
    style = ""
    if $scope.errors
      for error in $scope.errors
        if error.key is key
          style = "error"
    style