CourseManager.ApplicationView = Ember.View.extend
  didInsertElement: ->
    @$().foundation('topbar')
,
  willDestroyElement: ->
    @$().foundation('topbar', 'off')