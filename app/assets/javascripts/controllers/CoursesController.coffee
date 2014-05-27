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