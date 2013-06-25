jQuery(document).ready(function () {
   jQuery("select.country-selector").selectToAutocomplete({
      'autocomplete-plugin': function (context) {
         // loose matching of search terms
         var filter_options = function (term) {
            var split_term = term.split(' ');
            var matchers = [];
            for (var i = 0; i < split_term.length; i++) {
               if (split_term[i].length > 0) {
                  var matcher = {};
                  matcher['partial'] = new RegExp(split_term[i].replace(/[-[\]{}()*+?.,\\^$|#\s]/g, "\\$&"), "i");
                  if (context.settings['relevancy-sorting']) {
                     matcher['strict'] = new RegExp("^" + split_term[i].replace(/[-[\]{}()*+?.,\\^$|#\s]/g, "\\$&"), "i");
                  }
                  matchers.push(matcher);
               }
            }
            ;

            return $.grep(context.options, function (option) {
               var partial_matches = 0;
               if (context.settings['relevancy-sorting']) {
                  var strict_match = false;
                  var split_option_matches = option.matches.split(' ');
               }
               for (var i = 0; i < matchers.length; i++) {
                  if (matchers[i]['partial'].test(option.matches)) {
                     partial_matches++;
                  }
                  if (context.settings['relevancy-sorting']) {
                     for (var q = 0; q < split_option_matches.length; q++) {
                        if (matchers[i]['strict'].test(split_option_matches[q])) {
                           strict_match = true;
                           break;
                        }
                     }
                     ;
                  }
               }
               ;
               if (context.settings['relevancy-sorting']) {
                  var option_score = 0;
                  option_score += partial_matches * context.settings['relevancy-sorting-partial-match-value'];
                  if (strict_match) {
                     option_score += context.settings['relevancy-sorting-strict-match-value'];
                  }
                  option_score = option_score * option['relevancy-score-booster'];
                  option['relevancy-score'] = option_score;
               }
               return (!term || matchers.length === partial_matches );
            });
         }

         // update the select field value using either selected option or current input in the text field
         var update_select_value = function (option) {
            if (option) {
               if (context.$select_field.val() !== option['real-value']) {
                  context.$select_field.val(option['real-value']);
                  context.$select_field.change();
               }
            } else {
               var option_name = context.$text_field.val().toLowerCase();
               var matching_option = { 'real-value': false };
               for (var i = 0; i < context.options.length; i++) {
                  if (option_name === context.options[i]['label'].toLowerCase()) {
                     matching_option = context.options[i];
                     break;
                  }
               }
               ;
               if (context.$select_field.val() !== matching_option['real-value']) {
                  context.$select_field.val(matching_option['real-value'] || '');
                  context.$select_field.change();
               }
               if (matching_option['real-value']) {
                  context.$text_field.val(matching_option['label']);
               }
               if (typeof context.settings['handle_invalid_input'] === 'function' && context.$select_field.val() === '') {
                  context.settings['handle_invalid_input'](context);
               }
            }
         }

         // Twitter Bootstrap autocomplete
         context.$text_field.typeahead({
            source: function (query, process) {
               var filtered_options = filter_options(query);

               return filtered_options;
            },
            updater: function (item) {
               var matching_item;
               for (var i = 0; i < context.options.length; i++) {
                  if (item === context.options[i]['label']) {
                     matching_item = context.options[i];
                     break;
                  }
               }
               ;
               update_select_value(matching_item);
               return item;
            },
            matcher: function (item) {
               return true;
            },
            highlighter: function (item) {
               return item;
            },
            sorter: function (items) {
               var sortedItems = [];
               var resultItems = [];
               // sort by relevancy
               if (context.settings['relevancy-sorting']) {
                  sortedItems = items.sort(function (a, b) {
                     return b['relevancy-score'] - a['relevancy-score'];
                  });
               }
               // get the label value
               for (var i = 0; i < sortedItems.length; i++) {
                  resultItems.push(sortedItems[i]['label']);
               }
               return resultItems;
            }
         });

         // force refresh value of select field when form is submitted
         context.$text_field.parents('form:first').submit(function () {
            update_select_value();
         });
         // select current value
         update_select_value();
      },
      handle_invalid_input: function (context) {
         context.$text_field.val("");
      }
   });
});