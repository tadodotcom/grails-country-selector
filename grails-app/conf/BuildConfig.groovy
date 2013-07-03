grails.project.work.dir = 'target'

grails.project.dependency.resolution = {
   inherits 'global'
   log 'warn'

   repositories {
      grailsCentral()

      mavenLocal()
      mavenCentral()
   }

   plugins {
      build(':release:2.2.1', ':rest-client-builder:1.0.3') {
         export = false
      }
      compile ":resources:1.1.6" {
         export = false
      }
      compile ":jquery:1.8.3"
      compile ":jquery-ui:1.8.24"
   }
}
