CourseManager = Ember.Application.create
  LOG_TRANSITIONS: true

CourseManager.ApplicationView = Ember.View.extend
  didInsertElement: ->
    @$().foundation('topbar')
  ,
  willDestroyElement: ->
    @$().foundation('topbar', 'off')

CourseManager.Router.map ->
  @resource 'courses', ->
    @resource('course', path: '/:course_id')

CourseManager.CoursesRoute = Ember.Route.extend
  model: ->
    @store.find('course')

CourseManager.Course = DS.Model.extend
  name: DS.attr 'string'
  points: DS.attr 'number'

CourseManager.CoursesController = Ember.ArrayController.extend
  actions:
    submitCourse: ->
      name = this.get('newCourseName')
      points = this.get('newCoursePoints')

      if name? && name.length > 0 && name.length <=30 && points? && points > 0 && points <= 30
        course = this.store.createRecord 'course',
          name: name
          points: points

        course.save()
      else
        unless $('#errorPanel').length > 0
          $("fieldset").before("<div id='errorPanel' class='panel callout'>Error, name and points are required</div>")