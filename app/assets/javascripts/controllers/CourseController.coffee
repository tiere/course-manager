CourseManager.CourseController = Ember.ObjectController.extend
  isEditing: false

  actions:
    edit: ->
      @set('isEditing', true)

    update: ->
      @set 'isEditing', false
      @get('model').save()