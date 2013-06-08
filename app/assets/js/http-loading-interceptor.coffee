angular.module('http-loading-interceptor', [])
  .config(['$httpProvider', ($httpProvider) ->
    interceptor = (data, headersGetter) ->
      if !$("#infscr-loading").is(":visible")
        $("#infscr-loading").show()
      data
    $httpProvider.defaults.transformRequest.push(interceptor)
  ])

  .config(['$httpProvider', ($httpProvider) ->
    interceptor = ['$q', '$window', ($q, $window) ->
      success = (response) ->
        $("#infscr-loading").delay("slow").fadeOut();
        response
      error = (response) ->
        $("#infscr-loading").delay("slow").fadeOut();
        $q.reject(response)
      return (promise) -> promise.then(success, error)
    ]
    $httpProvider.responseInterceptors.push(interceptor)
  ])