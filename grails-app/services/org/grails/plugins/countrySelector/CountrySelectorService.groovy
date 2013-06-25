package org.grails.plugins.countrySelector

import org.codehaus.groovy.grails.web.util.WebUtils
import org.springframework.web.servlet.support.RequestContextUtils

class CountrySelectorService {

   def messageSource
   def grailsApplication

   private static final MESSAGE_SOURCE_KEY_START = "org.grails.plugin.countrySelector."

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
   def resolveCountries() {
      def useOnluyCustom = grailsApplication.config.grails.plugin.countrySelector.messageSource.useOnlyCustom ?: false
      def request = WebUtils.retrieveGrailsWebRequest().currentRequest
      def locale = RequestContextUtils.getLocale(request)

      // load all related messages
      def messageSourceMap = SearchResourceBundleMessageSource.listMessageCodes(messageSource, locale, MESSAGE_SOURCE_KEY_START)
      // build the result map
      def countrySelectorMap = CountryMapBuilder.buildFromMessageSources(messageSourceMap, useOnluyCustom)

      return countrySelectorMap
   }

   /**
    * Resolves country codes specified in the plugin and/or in the custom specification. Useful for domain class
    * constraints resolution.
    *
    * @return List of allowed country codes
    */
   def allowedCountryCodes() {
      // load all related messages
      def messageSourceMap = SearchResourceBundleMessageSource.listMessageCodes(messageSource, locale, MESSAGE_SOURCE_KEY_START)
      // build the result list
      def countryCodes = CountryMapBuilder.buildFromMessageSources(messageSourceMap, useOnluyCustom)

      return countryCodes
   }
}
