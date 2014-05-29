CourseManager.CourseController = Ember.ObjectController.extend
  isEditing: false

  submit: (e) ->
    e.preventDefault()
    console.log(e)

  actions:
    edit: ->
      @set('isEditing', true)

    update: ->
      @set 'isEditing', false
      @get('model').save()