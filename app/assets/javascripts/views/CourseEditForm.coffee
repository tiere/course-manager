CourseManager.CourseEditForm = Ember.View.extend
  templateName: 'courses/editForm'

  submit: (e) ->
    e.preventDefault()
    @get('controller').send('update')