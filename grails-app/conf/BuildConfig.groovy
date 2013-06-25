grails.project.work.dir = 'target'

grails.project.dependency.resolution = {
   inherits 'global'
   log 'warn'

   repositories {
      grailsCentral()
   }

   plugins {
      build ':release:2.2.1' { export = false }
      build ':rest-client-builder:1.0.3' { export = false }
      runtime ':resources:1.1.6'
      runtime ":jquery:1.8.3"
      runtime ":jquery-ui:1.8.24"
   }
}
