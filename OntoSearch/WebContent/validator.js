/**
 * Removes white spaces from the begin and end of a long string.
 * @param str
 * @returns
 */
function trimLong (str)
{	
	str.value = str.value.replace(/^\s\s*/, '').replace(/\s\s*$/, '');
	return;
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
	var str = document.onto_search_input_form['onto_search_input_box'].value

	if (str.length == 0) {
		alert("Please, provide one or more terms to be searched.");
		return false;
	} else {
		return true;
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
	
	return;
}