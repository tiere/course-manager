CourseManager = Ember.Application.create
  LOG_TRANSITIONS: true

CourseManager.Router.map ->
  @resource 'courses', ->
    @resource('course', path: '/:course_id')

CourseManager.CoursesRoute = Ember.Route.extend
  model: ->
    @store.find('course')

CourseManager.Course = DS.Model.extend
  name: DS.attr 'string'
  points: DS.attr 'number'