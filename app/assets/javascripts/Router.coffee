CourseManager.Router.map ->
  @resource 'courses', ->
    @resource('course', path: '/:course_id')