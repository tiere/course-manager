CourseManager.ApplicationView = Ember.View.extend
  didInsertElement: ->
    @$().foundation('topbar')
    @$().foundation('abide')
,
  willDestroyElement: ->
    @$().foundation('topbar', 'off')
    @$().foundation('abide', 'off')