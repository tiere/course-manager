CourseManager.CourseForm = Ember.View.extend
  templateName: 'courses/form'

  submit: (e) ->
    e.preventDefault()
    @get('controller').send('submitCourse')