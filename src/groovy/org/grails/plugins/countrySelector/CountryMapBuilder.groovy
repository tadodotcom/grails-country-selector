package org.grails.plugins.countrySelector

/**
 * Util class for building a map of countries to be used for the selector
 *
 * User: Matouš Kučera
 * Email: matous.kucera@envelon.cz
 * Created: 24.06.13 11:17
 */
class CountryMapBuilder {

   private static final MESSAGE_SOURCE_KEY_START = "org.grails.plugin.countrySelector."
   private static final MESSAGE_SOURCE_PLUGIN_DEFAULT = "default"
   private static final MESSAGE_SOURCE_ALTERNATIVES = "alternatives"
   private static final MESSAGE_SOURCE_RELEVANCY = "relevancy"

   /**
    * Builds a map of countries with their names, alternatives and relevancy
    *
    * @param messageSources Map of message sources starting with 'org.grails.plugin.countrySelector.'. For each country
    * a code, code.alternatives, code.relevancy is expected. Example of one country message source:
    * org.grails.plugin.countrySelector.DEU=Germany   // country code is DEU and the name is Germany
    * org.grails.plugin.countrySelector.DEU.alternatives=Bundes Republik Deutchland    // comma separated alternatives
    * org.grails.plugin.countrySelector.DEU.relevancy=1  // number relevancy
    * @param searchOnlyInBaseApp if true searches just in the base application if false search also in country selector
    * plugin and merge it with application specified (application specified has more relevancy)
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
   public static Map buildFromMessageSources(Map<String,String> messageSources, boolean searchOnlyInBaseApp) {
      def filteredMessageSources = resolveCustomMessageSources(messageSources, searchOnlyInBaseApp)

      // build result maps
      def resultMap
      if (!searchOnlyInBaseApp)
         resultMap = _buildFromMessageSources(filteredMessageSources.messageSourcesPluginDefault, MESSAGE_SOURCE_KEY_START + MESSAGE_SOURCE_PLUGIN_DEFAULT + ".", resultMap)

      resultMap = _buildFromMessageSources(filteredMessageSources.messageSourcesPluginCustom, MESSAGE_SOURCE_KEY_START, resultMap)

      return resultMap
   }

   /**
    * List all countries codes
    *
    * @param messageSources Map of message sources starting with 'org.grails.plugin.countrySelector.'. For each country
    * a code, code.alternatives, code.relevancy is expected. Example of one country message source:
    * org.grails.plugin.countrySelector.DEU=Germany   // country code is DEU and the name is Germany
    * org.grails.plugin.countrySelector.DEU.alternatives=Bundes Republik Deutchland    // comma separated alternatives
    * org.grails.plugin.countrySelector.DEU.relevancy=1  // number relevancy
    * @param searchOnlyInBaseApp if true searches just in the base application if false search also in country selector
    * plugin and merge it with application specified (application specified has more relevancy)
    *
    * @return List of relevant country codes
    */
   public static List listCountryCodes(Map<String,String> messageSources, boolean searchOnlyInBaseApp) {
      def filteredMessageSources = resolveCustomMessageSources(messageSources, searchOnlyInBaseApp)

      def resultList = _listCountryCodes(filteredMessageSources.messageSourcesPluginCustom, MESSAGE_SOURCE_KEY_START)
      if (!searchOnlyInBaseApp) {
         def pluginResultList = _listCountryCodes(filteredMessageSources.messageSourcesPluginDefault, MESSAGE_SOURCE_KEY_START + MESSAGE_SOURCE_PLUGIN_DEFAULT + ".")
         resultList = pluginResultList << resultList
      }

      return resultList
   }

   private static Map resolveCustomMessageSources(Map<String,String> messageSources, boolean searchOnlyInBaseApp) {
      def resultMap = [messageSourcesPluginDefault: [:], messageSourcesPluginCustom: [:]]

      // filter interesting message sources
      messageSources.each { key, value ->
         if (key.startsWith(MESSAGE_SOURCE_KEY_START + MESSAGE_SOURCE_PLUGIN_DEFAULT) &&
                 key.length() > (MESSAGE_SOURCE_KEY_START + MESSAGE_SOURCE_PLUGIN_DEFAULT).length()) {
            resultMap.messageSourcesPluginDefault."$key"= value
         }
         else if (key.startsWith(MESSAGE_SOURCE_KEY_START) && key.length() > MESSAGE_SOURCE_KEY_START.length()) {
            resultMap.messageSourcesPluginCustom."$key"= value
         }
      }

      return resultMap
   }

   private static Map _buildFromMessageSources(Map<String,String> messageSources, String messagePrefix, Map resultMap = null) {
      resultMap = resultMap ?: [countries: [:]]

      messageSources.each { key, value ->
         // relevant key
         if (key.startsWith(messagePrefix) && key.length() > messagePrefix.length()) {
            def messageRest = key.substring(messagePrefix.length(), key.length())
            def messageRestTokens = messageRest.split("\\.")
            if (messageRestTokens.length > 0) {
               def countryCode = messageRestTokens[0]
               // if country isn't in the result map, create one if exists in message source
               if (!resultMap.countries."$countryCode") {
                  def countryLabel = messageSources.get(messagePrefix + countryCode)
                  if (countryLabel) {
                     resultMap.countries."$countryCode" = [label: countryLabel]
                  }
               }

               // if exists or was created in previous step
               // and key has following label
               if (resultMap.countries."$countryCode" && messageRestTokens.length == 2) {
                  // alternatives
                  if (messageRestTokens[1] == MESSAGE_SOURCE_ALTERNATIVES) {
                     // add all relevancies
                     resultMap.countries."$countryCode".alternatives = value.split("\\ ")
                  }
                  else if (messageRestTokens[1] == MESSAGE_SOURCE_RELEVANCY) {
                     resultMap.countries."$countryCode".relevancy = value
                  }
               }
            }
         }
      }

      return resultMap
   }

   private static List _listCountryCodes(Map<String,String> messageSources, String messagePrefix) {
      def resultList = []

      messageSources.each { key, value ->
         // relevant key
         if (key.startsWith(messagePrefix) && key.length() > messagePrefix.length()) {
            def messageRest = key.substring(messagePrefix.length(), key.length())
            def messageRestTokens = messageRest.split("\\.")
            if (messageRestTokens.length > 0) {
               def countryCode = messageRestTokens[0]
               if (!resultList.contains(countryCode))
                  resultList.add(countryCode)
            }
         }
      }

      return resultList
   }
}
