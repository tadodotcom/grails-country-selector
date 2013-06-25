def log = org.slf4j.LoggerFactory.getLogger('grails.plugins.countrySelector.CountrySelectorResources')

log.debug "CountrySelectorResources"

modules = {

   'select-to-autocomplete-js' {
      resource id: 'select-to-autocomplete-js', url:[plugin: 'country-selector', dir: 'js/thirdParty', file: 'jquery.select-to-autocomplete.js']
   }

   'country-selector-plugin-bootstrap-js' {
      dependsOn 'bootstrap-typeahead'
      resource id: 'country-selector-plugin-bootstrap-js', url:[plugin: 'country-selector', dir: 'js/plugin', file: 'country-selector-bootstrap.js']
   }

   'country-selector-plugin-jquery-js' {
      dependsOn 'jquery-ui'
      resource id: 'country-selector-plugin-jquery-js', url:[plugin: 'country-selector', dir: 'js/plugin', file: 'country-selector-jquery.js']
   }

   'country-selector-plugin-js' {
      dependsOn 'country-selector-plugin-jquery-js'
   }

   'country-selector-js' {
      dependsOn 'select-to-autocomplete-js', 'country-selector-plugin-js'
   }

   'country-selector' {
      dependsOn 'jquery', 'country-selector-js'
   }
}