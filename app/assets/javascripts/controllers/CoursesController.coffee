CourseManager.CoursesController = Ember.ArrayController.extend
  numberOfCourses: ( ->
    @get('length')
  ).property('@each')

  numberOfPoints: ( ->
    sum = 0
    @forEach (course) ->
      sum += course.get('points')
    sum
  ).property('@each.points')
  actions:
    submitCourse: ->
      name = this.get('newCourseName')
      points = this.get('newCoursePoints')

      course = this.store.createRecord 'course',
        name: name
        points: points
      course.save()
      @set('newCourseName', '')
      @set('newCoursePoints', '')

    deleteCourse: (course) ->
      course.destroyRecord()