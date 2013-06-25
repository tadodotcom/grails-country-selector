package org.grails.plugins.countrySelector

import org.codehaus.groovy.grails.context.support.PluginAwareResourceBundleMessageSource

/**
 * Util class which allows search in message source wia partial key and return all results in key/value map.
 *
 * User: Matouš Kučera
 * Email: matous.kucera@tado.cz
 * Created: 24.06.13 10:00
 */
class SearchResourceBundleMessageSource {

   /**
    * Search for all message codes starting with key defined.
    *
    * @param locale to search for
    * @param keyStart start of keys which should be searched for
    * @param searchOnlyInBaseApp if true searches just in the base application if false search also in plugins. False
    * by default
    * @return Map of all relevant message sources. Key is the whole message key and the value is the real message
    * value
    */
   public static Map<String, String> listMessageCodes(PluginAwareResourceBundleMessageSource messageSource, Locale locale, String keyStart) {
      // NOTE: Access protected methods of messageSource implementation which is allowed in Groovy. Extending the PluginAwareResourceBundleMessageSource
      // class and registering this class as a messageSource bean is possible but requires setting of all bean properties which are set
      // by the i18n plugin
      Properties properties = messageSource.getMergedProperties(locale).getProperties()
      properties.plus(messageSource.getMergedPluginProperties(locale).getProperties())

      return properties.findAll { it.key.startsWith(keyStart) }
   }
}
