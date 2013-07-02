package org.grails.plugins.countrySelector

import org.codehaus.groovy.grails.web.util.WebUtils
import org.springframework.web.servlet.support.RequestContextUtils

class CountrySelectorService {

   def messageSource
   def grailsApplication

   private final MESSAGE_SOURCE_KEY_START = "org.grails.plugin.countrySelector."

   static transactional = false

   /**
    * Resolves all countries specified in plugin and/or in custom specification.
    *
    * @return Structured map of all countries recognized in following format:
    * [countries: [
    *    AUT: [
    *       label: "Austria",
    *       alternatives: ["AUT", "AT", "Österreich", "Osterreich", "Oesterreich"],
    *       relevancy: 1
    *       ]
    *    ]
    * ]
    */
   def resolveCountries(def locale) {
      def useOnlyCustom = grailsApplication.config.grails.plugin.countrySelector.messageSource.useOnlyCustom ?: false
      // load all related messages
      def messageSourceMap = SearchResourceBundleMessageSource.listMessageCodes(messageSource, locale, MESSAGE_SOURCE_KEY_START)
      // build the result map
      def countrySelectorMap = CountryMapBuilder.buildFromMessageSources(messageSourceMap, useOnlyCustom)

      return countrySelectorMap
   }

   /**
    * Resolves country codes specified in the plugin and/or in the custom specification. Useful for domain class
    * constraints resolution.
    *
    * @return List of allowed country codes
    */
   def allowedCountryCodes() {
      def useOnlyCustom = grailsApplication.config.grails.plugin.countrySelector.messageSource.useOnlyCustom ?: false
      // load all related messages
      def messageSourceMap = SearchResourceBundleMessageSource.listMessageCodes(messageSource, Locale.getDefault(), MESSAGE_SOURCE_KEY_START)
      // build the result list
      def countryCodesMap = CountryMapBuilder.buildFromMessageSources(messageSourceMap, useOnlyCustom)
      def countryCodes = []
      countryCodesMap.countries.each {
         countryCodes.add(it.key)
      }

      return countryCodes
   }
}
