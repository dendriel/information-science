/**
 * Removes white spaces from the begin and end of a long string.
 * @param str
 * @returns
 */
function trimLong (str)
{
	str.value = str.value.replace(/^\s\s*/, '').replace(/\s\s*$/, '');
	return str.value;
}

/**
 * Go to the previous page.
 */
function goBack()
{
    window.location.href="index.jsp";
}

/**
 * Validates the ontology path from the load form.
 * @returns {Boolean}
 */
function validateLoadForm()
{
	var str = document.onto_load_input_form['onto_load_input_box'].value

	if (str.length == 0) {
		alert("Please, provide an ontology to be loaded.");
		return false;
	} else {
		return true;
	}	
}

/**
 * Validates the term from the load form.
 * @returns {Boolean}
 */
function validateSearchForm()
{
	var str = document.onto_search_input_form['onto_search_input_box'].value;
	
	if (str.length == 0) {
		alert("Please, provide one or more terms to be searched.");
		return false;
	} else {
		return true;
	}	
}

function loadRelated(str)
{
	var term = str.value.replace(/^\s\s*/, '').replace(/\s\s*$/, '');
	
	document.termsTable.specificTerms.options.length = 0;
	document.termsTable.genericTerms.options.length = 0;
	document.termsTable.synonymTerms.options.length = 0;
	
	// Load the specific terms.
	for (i = 0; i < specificTerms.length; i++) {
		
		if (specificTerms[i].term.toLowerCase() == term.toLowerCase()) {
			for (j = 0; j < specificTerms[i].values.length; j++) {
				document.termsTable.specificTerms.options[j] = new Option(specificTerms[i].values[j], specificTerms[i].values[j], false, false);		
			}
			break;
		}
	}

	// Load the generic terms.
	for (i = 0; i < genericTerms.length; i++) {
		if (genericTerms[i].term.toLowerCase() == term.toLowerCase()) {
			for (j = 0; j < genericTerms[i].values.length; j++) {
				document.termsTable.genericTerms.options[j] = new Option(genericTerms[i].values[j], genericTerms[i].values[j], false, false);		
			}
			break;
		}	
	}	
	
	// Load the synonym terms.
	for (i = 0; i < synonymTerms.length; i++) {
		if (synonymTerms[i].term.toLowerCase() == term.toLowerCase()) {
			for (j = 0; j < synonymTerms[i].values.length; j++) {
				document.termsTable.synonymTerms.options[j] = new Option(synonymTerms[i].values[j], synonymTerms[i].values[j], false, false);		
			}
			break;
		}	
	}	
}

/**
 * Check if the terms interaction is set to "surf".
 * 
 * @returns {Boolean} true if the interaction is "surf"; false if the interaction is "combine".
 */
function isSurfing ()
{
	if ((document.interactionForm.interaction[0].value == "surf") &&
		(document.interactionForm.interaction[0].checked == true)) {
		return true;
	} else if ((document.interactionForm.interaction[0].value == "combine") &&
			(document.interactionForm.interaction[0].checked == false)) {
		return true;		
	}
	
	return false;
}

/**
 * Update the search field with the selected term. Also, update the related terms.
 * @param selected_term
 */
function selectSpecificTerm(selected_term)
{	
	if (isSurfing()) {
		//document.onto_search_input_form['onto_search_input_box'].value = selected_term.value;
		loadRelated(selected_term);
	} else {
		var currentTerm = document.onto_search_input_form['onto_search_input_box'].value;
		document.onto_search_input_form['onto_search_input_box'].value = currentTerm + ", " + selected_term.value;	
	}	
}

/**
 * Build the search term and submit it to google.
 */
function searchTerm ()
{	
	if (!validateSearchForm()) return;

	
	var str = document.onto_search_input_form['onto_search_input_box'].value
	var terms = str.split(" ");
	var search_engine = "www.google.com.br";
	var default_params = "#safe=off&q=";

	var searchUrl = "http://" + search_engine + "/" + default_params;
	
	for (i = 0; i < terms.length; i++) {
		searchUrl = searchUrl + terms[i];
		
		if ((i + 1) < terms.length) {
			searchUrl = searchUrl + "+";
		}
	}
	
	var win = window.open(searchUrl, '_blank');
	if (win){
	    //Browser has allowed it to be opened
	    win.focus();
	} else{
	    //Broswer has blocked it
	    alert('Please allow popups for this site.');
	}
	
	// Don't allow the submission so the interface can keep the selected search terms.
	return false;
}