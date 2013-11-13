package org.grails.plugins.countrySelector

import org.springframework.web.servlet.support.RequestContextUtils as RCU

/**
 * Tag library with country selector.
 *
 * @author Matous Kucera
 */
class CountrySelectorTagLib {

   private static final COUNTRY_SELECTOR_RESOURCE = "country-selector"

   static namespace = 'cs'

   def countrySelectorService

   /**
    * Country selector
    *
    * @attr name  Unique name of the country selector
    * @attr value Optional. Value of the selected country - country code
    * @attr placeholder Optional. The placeholder for the input
    * @attr locale Optional. Locale which should be used for country names. The request locale are taken by default.
    */
   def countrySelector = { attrs ->
      if (!attrs.name)
         throwTagError("Tag [countrySelector] is missing required attribute [name]")

      def value = attrs.remove('value')
      def placeholder = attrs.remove('placeholder')
      def locale = attrs.remove('locale') ?: RCU.getLocale(request)
      def countryMissing = value ? true : false

      // load required resource
      out << r.require(module: COUNTRY_SELECTOR_RESOURCE)

      // add required class
      // TODO check if it is not there
      attrs.class = attrs.class + " country-selector"

      out << "<select "
      // process remaining attributes
      outputAttributes(attrs, out, true)

      out << '>'
      out << "\n"

      // build the options
      def countrySelectorConfig = countrySelectorService.resolveCountries(locale)
      if(countrySelectorConfig.countries) {
         countrySelectorConfig.countries.each {entryName, countryProperties ->
            out << "<option value='$entryName' "

            if (value == entryName) {
               out << "selected='selected' "
               countryMissing = false
            }

            if (countryProperties.alternatives) {
               out << "data-alternative-spellings='"
               for (def alternativeSpelling : countryProperties.alternatives) {
                  out << "$alternativeSpelling "
               }
               out << "' "
            }

            if (countryProperties.relevancy)
               out << "data-relevancy-booster='${countryProperties.relevancy}'"

            out << ">"
            out << countryProperties.label?.encodeAsHTML()
            out << "</option>"
            out << "\n"
         }
      }

      // fill the value if there is not a country in config
      if (countryMissing)
         out << "<option value='${value}' selected='selected'>$value</option>"
      else if(value)
         out << "<option value=''>$placeholder</option>"
      else
         out << "<option value='' selected='selected'>$placeholder</option>"

      // close tag
      out << '</select>'
   }

   /**
    * Country displays the country name for given country code. It doesn't check if the country code is valid, if not
    * it prints the given code.
    *
    * @attr code Unique country code
    * @attr locale Optional. Locale which should be used for country names. The request locale are taken by default.
    */
   def country = { attrs ->
      def countryCode = attrs.remove('code') ?: RCU.getLocale(request)
      def locale = attrs.remove('locale') ?: RCU.getLocale(request)

      out << g.message(code: "org.grails.plugin.countrySelector.default.$countryCode", default: countryCode, locale: locale)
   }

   /**
    * Dump out attributes in HTML compliant fashion.
    */
   void outputAttributes(attrs, writer, boolean useNameAsIdIfIdDoesNotExist = false) {
      attrs.remove('tagName') // Just in case one is left
      attrs.each { k, v ->
         if(v != null) {
            writer << k
            writer << '="'
            writer << v.encodeAsHTML()
            writer << '" '
         }
      }
      if (useNameAsIdIfIdDoesNotExist) {
         outputNameAsIdIfIdDoesNotExist(attrs, writer)
      }
   }

   private outputNameAsIdIfIdDoesNotExist(attrs, out) {
      if (!attrs.containsKey('id') && attrs.containsKey('name')) {
         out << 'id="'
         out << attrs.name?.encodeAsHTML()
         out << '" '
      }
   }
}
