grails.project.work.dir = 'target'

grails.project.dependency.resolution = {
   inherits 'global'
   log 'warn'

   repositories {
      grailsCentral()
      grailsRepo "http://grails.org/plugins"
   }

   plugins {
      build ':release:2.2.1' { export = false }
      build ':rest-client-builder:1.0.3' { export = false }
      runtime ':resources:1.1.6' { export = false }
      runtime ":jquery:1.8.3" { export = false }
      runtime ":jquery-ui:1.8.24" { export = false }
   }
}
