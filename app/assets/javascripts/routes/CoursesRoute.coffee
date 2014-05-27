CourseManager.CoursesRoute = Ember.Route.extend
  model: ->
    @store.find('course')