CourseManager.TextFieldWithValidation = Ember.TextField.extend
  focusOut: ->
    @removeErrors()

    if @required and @type == 'text' and not @value
      @addErrors("This field is required")
    else if @type == 'number' and not @value
      @addErrors("This field is required and must be a number")

  removeErrors: ->
    @$().next('.error').remove()
    @$().parent().removeClass('error')

  addErrors: (message) ->
    @$().parent().addClass('error')
    @$().after("<small class='error'>#{message}</small>")
