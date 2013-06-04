angular.module('http-loading-interceptor', [])
  .config(['$httpProvider', ($httpProvider) ->
    interceptor = (data, headersGetter) ->
      $('#infscr-loading').fadeIn('normal')
      data
    $httpProvider.defaults.transformRequest.push(interceptor)
  ])

  .config(['$httpProvider', ($httpProvider) ->
    interceptor = ['$q', '$window', ($q, $window) ->
      success = (response) ->
        $('#infscr-loading').fadeOut('normal')
        response
      error = (response) ->
        $('#infscr-loading').fadeOut('normal')
        $q.reject(response)
      return (promise) -> promise.then(success, error)
    ]
    $httpProvider.responseInterceptors.push(interceptor)
  ])