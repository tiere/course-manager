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
        files:
          'public/stylesheet.css': 'scss/app.scss'
    coffee:
      compile:
        files:
          'public/app.js': 'app/assets/**/*.coffee'
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
    watch:
      sass:
        files: 'scss/**/*.scss',
        tasks: ['sass']
      coffee:
        files: 'app/assets/**/*.coffee',
        tasks: ['coffee']
      emberTemplates:
        files: 'handlebars_templates/**/*.hbs',
        tasks: ['emberTemplates']
    clean:
      js: ['public/application.js']
    emberTemplates:
      compile:
        options:
          templateBasePath: 'templates'
        files:
          'public/templates.js': 'templates/**/*.hbs'

  grunt.loadNpmTasks 'grunt-contrib-concat'
  grunt.loadNpmTasks 'grunt-contrib-uglify'
  grunt.loadNpmTasks 'grunt-sass'
  grunt.loadNpmTasks 'grunt-contrib-watch'
  grunt.loadNpmTasks 'grunt-contrib-coffee'
  grunt.loadNpmTasks 'grunt-contrib-clean'
  grunt.loadNpmTasks 'grunt-ember-templates'

  grunt.registerTask 'build', ['emberTemplates', 'coffee', 'sass', 'concat', 'uglify', 'clean']
  grunt.registerTask 'default', ['build', 'watch']