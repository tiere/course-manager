bower_components = "bower_components"

# Javascripts
modernizr = "#{bower_components}/modernizr/modernizr.js"
handlebars = "#{bower_components}/handlebars/handlebars.js"
jquery = "#{bower_components}/jquery/dist/jquery.js"
ember = "#{bower_components}/ember/ember.js"
ember_data = "#{bower_components}/ember-data/ember-data.js"

# CSS
foundation = "#{bower_components}/foundation/css/foundation.css"
normalize = "#{bower_components}/foundation/css/normalize.css"

module.exports = (grunt) ->
  grunt.initConfig
    pkg: grunt.file.readJSON('package.json'),
    concat:
      options:
        separator: ';'
      stylesheet:
        src: [
          normalize
          foundation
        ],
        dest: 'public/stylesheet.css'
      application:
        src: [
          jquery
          handlebars
          ember
          ember_data
        ],
        dest: 'public/application.js'
    uglify:
      modernizr:
        src: modernizr,
        dest: 'public/modernizr.min.js'
      application:
        src: '<%= concat.application.dest %>',
        dest: 'public/application.min.js'

  grunt.loadNpmTasks 'grunt-contrib-concat'
  grunt.loadNpmTasks 'grunt-contrib-uglify'

  grunt.registerTask 'default', ['concat', 'uglify']