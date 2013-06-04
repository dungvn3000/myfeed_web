module = angular.module('my-l10n-en', ['l10n'])

module.config ['l10nProvider', (l10n) ->
  l10n.add 'en-us', {
    myPage: {
      title: 'dung ne'
    }
  }
]
