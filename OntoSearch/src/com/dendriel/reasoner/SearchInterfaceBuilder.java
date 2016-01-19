package com.dendriel.reasoner;

import java.util.List;
import java.util.Set;

import aterm.ATermAppl;

public class SearchInterfaceBuilder
{
	public static final String LoadPageId = "load_page";
	public static final String LoadPageFormKey = "onto_load_input_form";
	public static final String LoadPageInputKey = "onto_load_input_box";
	public static final String SearchPageId = "search_page";
	public static final String SearchPageFormKey = "onto_search_input_form";
	public static final String SearchPageInputKey = "onto_search_input_box";
	
	private final static String title = "OntoSearch";
	
	private final static String loadPageTitle = "Load Page";
	private final static String loadPageTip = "<b>" + loadPageTitle + "</b> - Load an ontology from file system or remote host.";
	private final static String loadPageExample = "e.g: <i>http://protege.stanford.edu/ontologies/pizza/pizza.owl</i>";
	private final static String loadPageSubmit = "Load Ontology";
	private final static String loadPageFooter = "<br /><i>Developed by Vitor Rozsa and Marta Denisczwicz.</i>";
	private final static String loadPageColor = "#7BB5FF";
	private final static String loadPageExtraData = "";
	
	private final static String searchPageTitle = "Search Page";
	private final static String searchPageGoBackLink = "<button style=\"height:32px;width:128px\" onclick=\"goBack()\">Back to Load Page</button>";
	private final static String searchPageTip = "<b>" + searchPageTitle + "</b> - Use the ontology terms to improve your search.";
	private final static String searchPageSubmit = "Search";
	private final static String searchPageFooter = loadPageFooter;
	private final static String searchPageColor = "#ff837b";
	
	private final static String pageHeadStr =
"<!DOCTYPE html>\n"																+
"<html>\n"																		+ 
"	<head>\n"																	+
"		<meta http-equiv=\"Content-Type\" content=\"text/html; charset=utf-8\">\n"+
"      	<script type=\"text/javascript\" src=\"validator.js\" ></script>\n"		+
"      	<script type=\"text/javascript\" src=\"awesomeplete.js\" async></script>\n"	+
"		<link rel=\"stylesheet\" type=\"text/css\" href=\"mystyle.css\">\n"		+
"		<link rel=\"stylesheet\" type=\"text/css\" href=\"awesomplete.css\">\n"	+
"		<title>%s</title>\n"													+
"	</head>\n";
	
	private final static String bodyHeaderStr =
"	<body>\n"										+
"	<div id=\"container\">\n"						+
"	    <div id=\"header\">\n"					+
"			<header>\n"							+
"			<div class=\"header_image\"></div>\n" +
"			<div class=\"header_link\" style=\"\" >%s</div>\n" +
"			<div class=\"header_tip\" style=\"background:%s\">%s</div>\n" +
"			</header>\n"							+
"		</div>\n";
	
	private final static String loadBodyInputStr =
"		    <main>\n"													+
"				<div id=\"load_form\">\n"								+
"					<form name=\"" + LoadPageFormKey + "\" action=\"SearchServlet\" method=\"post\" onsubmit=\"return validateLoadForm()\">\n"+
"						<input type=\"text\" size=\"96\" name=\"" + LoadPageInputKey + "\" style=\"border:solid %s\" onchange=\"return trimLong(this)\"/><br />\n"+
"						<input type=\"submit\" value=\"" + loadPageSubmit + "\" />\n"		+
"						<input type=\"hidden\" name=\"page_id\" value=\"" + LoadPageId + "\">\n"+
"					</form>\n"											+
"				</div>\n"												+
"				<br /> " + loadPageExample + "\n"						+ 
"			</main>\n";

	private final static String searchBodyInputStr =
"		    <main>\n"												+	
"				<div name=\"load_form\">\n"						+
"					<form name=\"" + SearchPageFormKey + "\" onsubmit=\"return searchTerm()\">\n"+
"						<input type=\"text\" size=\"96\" name=\"" + SearchPageInputKey + "\" style=\"border:solid %s\" onsubmit=\"trimLong(this);\" oninput=\"loadRelated(this)\" class=\"awesomplete\" list=\"terms\"/><br />\n"+
"						<datalist id=\"terms\">\n"				+
"							%s"									+
"						</datalist>\n"							+
"						<input type=\"submit\" value=\"" + searchPageSubmit + "\" />\n"		+
"						<input type=\"hidden\" name=\"page_id\" value=\"" + SearchPageId + "\">\n"+
"					</form>\n"									+
"				</div>\n"										+
"				<br />\n"										+
"			<form  name=\"termsTable\"><table>" 					+
"				<tr>\n" 											+
"					<th><i>Specifc terms</i></th>\n" 				+	
"					<th><i>Generic terms</i></th>\n" 				+	
"					<th><i>Synonyms terms</i></th>\n" 				+		
"				</tr>\n" 											+
"				<tr>\n" 											+
"					<td>\n" 										+
"						<select name=\"specificTerms\" size=\"18\" multiple=\"multiple\">\n" +
"						</select>\n" 								+
"					</td>\n" 										+	
"					<td>\n" 										+
"						<select name=\"genericTerms\" size=\"18\" multiple=\"multiple\">\n" +
"						</select>\n" 								+
"					</td>\n" 										+	
"					<td>\n" 										+
"						<select name=\"synonymTerms\" size=\"18\" multiple=\"multiple\">\n" +
"						</select>\n" 								+
"					</td>\n" 										+
"				</tr>" 												+
"			</table></form>\n" 										+
"			</main>\n";
	
	private final static String pageFooterStr =
"		<div id=\"footer\">"+
"		    <footer style=\"background:%s\">%s</footer>"+
"		</div>"+
"	</div>"+
"	</body>"+
"</html>";
	
	/**
	 * Build the load ontology page.
	 * @return
	 */
	public static String BuildLoadPage()
	{
		String bodyInputFormated = String.format(loadBodyInputStr, loadPageColor);
		
		return BuildPage(
				loadPageTitle,
				"",
				loadPageColor,
				loadPageTip,
				bodyInputFormated,
				loadPageFooter,
				loadPageExtraData);
	}
	
	/**
	 * Build the search ontology page.
	 * @return
	 */
	public static String BuildSearchPage(SemanticSearchCore smtCore)
	{
		String bodyInputFormated = String.format(searchBodyInputStr, searchPageColor, BuildSearchTermsList(smtCore.GetAllTermsStr()));
		String relatedTerms = BuildSpecificRelatedTerms(smtCore);
		relatedTerms += BuildGenericRelatedTerms(smtCore);
		relatedTerms += BuildSynonymRelatedTerms(smtCore);
		
		return BuildPage(
				searchPageTitle,
				searchPageGoBackLink,
				searchPageColor,
				searchPageTip,
				bodyInputFormated,
				searchPageFooter,
				relatedTerms);
	}
	
	
	/**
	 * Generic function to build the OntoSearch page.
	 * 
	 * @param title
	 * @param link
	 * @param color
	 * @param tip
	 * @param bodyInputFormat
	 * @param footer
	 * @param extraData Append an extra string at the end of the page (for instance, append js data).
	 * @return
	 */
	private static String BuildPage(
			String title,
			String link,
			String color,
			String tip,
			String bodyInputFormat,
			String footer,
			String extraData)
	{
		String page = String.format(pageHeadStr + bodyHeaderStr + bodyInputFormat + pageFooterStr + "%s",
				title,
				link,
				color,
				tip,
				color,
				footer,
				extraData
				);
		
		return page;
	}
	
	/**
	 * Build a list of search terms for the auto-complete.
	 * @return
	 */
	private static String BuildSearchTermsList(List<String> termsList)
	{
		String optionsList = "";
		
		for (String term : termsList) {
			optionsList += String.format("<option>%s</option>\n", term);
		}
		return optionsList;
	}
	
	/**
	 * Make all the specific related terms available for javascript processing.
	 * @param smtCore
	 * @return
	 */
	private static String BuildSpecificRelatedTerms(SemanticSearchCore smtCore)
	{
		Set<ATermAppl> termsList = smtCore.GetAllTerms();

		String termsStr = "\n<script>var specificTerms = [\n";
		for (ATermAppl term : termsList) {
			
			termsStr += String.format("{ term: \"%s\", values: [",
					SemanticSearchCore.GetTermName(SemanticSearchCore.GetClassNameFromURI(term.toString())));
			
			List<String> subClassesList = smtCore.GetAllSubClasses(term);
			
			for (String related : subClassesList) {
				termsStr += "\"" + related + "\", ";
			}
			termsStr += "] },\n";
		}
		termsStr += "];</script>\n";
		
		return termsStr;
	}
	
	/**
	 * Make all the generic related terms available for javascript processing.
	 * @param smtCore
	 * @return
	 */
	private static String BuildGenericRelatedTerms(SemanticSearchCore smtCore)
	{
		Set<ATermAppl> termsList = smtCore.GetAllTerms();

		String termsStr = "\n<script>var genericTerms = [\n";
		for (ATermAppl term : termsList) {
			
			termsStr += String.format("{ term: \"%s\", values: [",
					SemanticSearchCore.GetTermName(SemanticSearchCore.GetClassNameFromURI(term.toString())));
			
			List<String> superClassesList = smtCore.GetAllSuperClasses(term);
			
			for (String related : superClassesList) {
				termsStr += "\"" + related + "\", ";
			}
			termsStr += "] },\n";
		}
		termsStr += "];</script>\n";
		
		return termsStr;
	}
	
	/**
	 * Make all the synonym terms available for javascript processing.
	 * @param smtCore
	 * @return
	 */
	private static String BuildSynonymRelatedTerms(SemanticSearchCore smtCore)
	{
		Set<ATermAppl> termsList = smtCore.GetAllTerms();

		String termsStr = "\n<script>var synonymTerms = [\n";
		for (ATermAppl term : termsList) {
			
			termsStr += String.format("{ term: \"%s\", values: [",
					SemanticSearchCore.GetTermName(SemanticSearchCore.GetClassNameFromURI(term.toString())));
			
			List<String> synonymClassesList = smtCore.GetAllSynonyms(term);
			
			for (String related : synonymClassesList) {
				termsStr += "\"" + related + "\", ";
			}
			termsStr += "] },\n";
		}
		termsStr += "];</script>\n";
		
		return termsStr;
	}
}
