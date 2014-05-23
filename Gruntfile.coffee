bower_components = "bower_components"

# Javascripts
modernizr = "#{bower_components}/modernizr/modernizr.js"
handlebars = "#{bower_components}/handlebars/handlebars.js"
jquery = "#{bower_components}/jquery/dist/jquery.js"
ember = "#{bower_components}/ember/ember.js"
ember_data = "#{bower_components}/ember-data/ember-data.js"

# CSS
foundation = "#{bower_components}/foundation/scss"

module.exports = (grunt) ->
  grunt.initConfig
    pkg: grunt.file.readJSON('package.json'),
    sass:
      options:
        includePaths: [foundation]
      dist:
        options:
          outputStyle: 'compressed'
        files:
          'public/stylesheet.css': 'scss/app.scss'
    concat:
      options:
        separator: ';'
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
  grunt.loadNpmTasks 'grunt-sass'

  grunt.registerTask 'default', ['sass', 'concat', 'uglify']