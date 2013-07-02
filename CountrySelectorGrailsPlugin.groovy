import org.grails.plugins.countrySelector.SearchResourceBundleMessageSource

class CountrySelectorGrailsPlugin {
   def version = "0.2"
   def grailsVersion = "2.0 > *"

   def title = "Country Selector Plugin"
   def author = "Matouš Kučera"
   def authorEmail = "matous.kucera@tado.com"
   def description = """
Provides an advanced country selector in your grails application.

Tag for rendering an input with smart autocomplete feature based on country names, name alternatives and relevance. Full localization and custom settings supported.
"""

   def documentation = "http://energybob.github.io/grails-country-selector"

   def license = "GPL3"

   def organization = [name: "tado GmbH", url: "http://tado.com/"]

   def issueManagement = [system: "Github", url: "https://github.com/EnergyBob/grails-country-selector/issues"]

   def scm = [url: "https://github.com/EnergyBob/grails-country-selector"]

   def dependsOn = [i18n: "* > 1.0"]

}
