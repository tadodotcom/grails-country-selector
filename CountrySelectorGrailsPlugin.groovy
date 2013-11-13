import org.grails.plugins.countrySelector.SearchResourceBundleMessageSource

class CountrySelectorGrailsPlugin {
   def version = "1.0"
   def grailsVersion = "2.0 > *"

   def title = "Country Selector Plugin"
   def author = "Matouš Kučera"
   def authorEmail = "matous.kucera@tado.com"
   def description = "Advanced country selector for forms."

   def documentation = "http://tadodotcom.github.io/grails-country-selector"

   def license = "GPL3"

   def organization = [name: "tado GmbH", url: "http://tado.com/"]

   def issueManagement = [system: "Github", url: "https://github.com/tadodotcom/grails-country-selector/issues"]

   def scm = [url: "https://github.com/tadodotcom/grails-country-selector"]

   def loadAfter = ['i18n']
}
