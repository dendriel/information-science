package com.dendriel.reasoner;

import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyManager;

import aterm.ATermAppl;

import com.clarkparsia.pellet.owlapiv3.PelletReasoner;
import com.clarkparsia.pellet.owlapiv3.PelletReasonerFactory;


public class SemanticSearchCore
{
	private OWLOntologyManager ontoMan;
	private OWLOntology loadedOnto;
	private PelletReasoner reasoner;
	
	private static final String httpProtoStr = "http://";
	private static final String httpsProtoStr = "http://";
	
	public SemanticSearchCore()
	{
		ontoMan = OWLManager.createOWLOntologyManager();
	}

	/**
	 * Get all terms from the ontology.
	 * @return A list of ATerms.
	 */
	public Set<ATermAppl> GetAllTerms()
	{
		Set<ATermAppl> classList = reasoner.getKB().getAllClasses();

		Set<ATermAppl> classListFiltered = new HashSet<ATermAppl>();
		
		for (ATermAppl elem : classList) {
			String className = GetClassNameFromURI(elem.toString());
			// Filter off these class names.
			if (className.equals("_TOP_") || className.equals("not(_TOP_)") || className.equals("top")) {
				continue;
			}
			
			classListFiltered.add(elem);
		}
		
		return classListFiltered;
	}
	/**
	 * Get all terms from the ontology.
	 * @return A list of Strings.
	 */
	public List<String> GetAllTermsStr()
	{
		Set<ATermAppl> classList = reasoner.getKB().getAllClasses();

		List<String> classListStr = new ArrayList<String>();
		
		for (ATermAppl elem : classList) {
			String className = GetClassNameFromURI(elem.toString());
			// Filter off these class names.
			if (IsValidClassName(className) == false) {
				continue;
			}
			
			classListStr.add(GetTermName(className));
		}
		
		return classListStr;
	}
	
	/**
	 * Get all sub classes from the given term.
	 * @return A list of string with the sub class names.
	 */
	public List<String> GetAllSubClasses(ATermAppl term)
	{
		List<String> subClassesList =  new ArrayList<String>();
		Set<Set<ATermAppl>> subClasses = reasoner.getKB().getSubClasses(term);
		
		for (Set<ATermAppl> termList : subClasses) {
			
			for (ATermAppl termFromSet : termList) {

				String className = GetClassNameFromURI(termFromSet.toString());
				// Filter off these class names.
				if (IsValidClassName(className) == false) continue;
				
				subClassesList.add(GetTermName(className));
			}
		}		
		return subClassesList;
	}
	
	/**
	 * Get all individuals from the given term.
	 * 
	 * @param direct - get only direct individuals (true) or all individuals (false);
	 * 
	 * @return A list of string with the individuals names.
	 */
	public List<String> GetIndividuals(ATermAppl term, Boolean direct)
	{
		List<String> individualsList =  new ArrayList<String>();
		Set<ATermAppl> individuals = reasoner.getKB().getInstances(term, direct);
					
		for (ATermAppl termFromSet : individuals) {

			String individualName = GetClassNameFromURI(termFromSet.toString());
			// Filter off these class names.
			if (IsValidClassName(individualName) == false) continue;
			
			individualsList.add(GetTermName(individualName));
		}
		return individualsList;
	}
	
	/**
	 * Get all super classes from the given term.
	 * @return A list of string with the super class names.
	 */
	public List<String> GetAllSuperClasses(ATermAppl term)
	{
		List<String> superClassesList =  new ArrayList<String>();
		Set<Set<ATermAppl>> superClasses = reasoner.getKB().getSuperClasses(term);
		
		for (Set<ATermAppl> termList : superClasses) {
			
			for (ATermAppl termFromSet : termList) {

				String className = GetClassNameFromURI(termFromSet.toString());
				// Filter off these class names.
				if (IsValidClassName(className) == false) continue;
				
				superClassesList.add(GetTermName(className));
			}
		}
		return superClassesList;
	}
	
	/**
	 * Get all synonyms from the given term.
	 * @return A list of string with the synonym class names.
	 */
	public List<String> GetAllSynonyms(ATermAppl term)
	{
		List<String> synonymClassesList =  new ArrayList<String>();
		Set<ATermAppl> synonymClasses = reasoner.getKB().getAllEquivalentClasses(term);
				
		for (ATermAppl termFromSet : synonymClasses) {

			String className = GetClassNameFromURI(termFromSet.toString());
			
			// Filter off these class names.
			if (IsValidClassName(className) == false) continue;
			
			String termName = GetTermName(className);
			// Don't add the same term in the synonym list.
			if (termFromSet == term) continue;
			
			synonymClassesList.add(termName);
		}
		
		return synonymClassesList;
	}
	
	/**
	 * Load an ontology from a file or URL.
	 * @param path
	 * 
	 */
	public Boolean LoadOntology(String path)
	{		
		if (loadedOnto != null) {
			ontoMan.removeOntology(loadedOnto);
			loadedOnto = null;
		}
		
		// Decide which method it must use to load the ontology.
		if (path.contains(httpProtoStr) || path.contains(httpsProtoStr)) {
			loadedOnto = LoadOntologyURL(ontoMan, path);
			
		} else {
			loadedOnto = LoadOntologyFile(ontoMan, path);			
		}
				
		if (loadedOnto == null) return false;
		
		// Setup the Pellet reasoner.
		reasoner = PelletReasonerFactory.getInstance().createReasoner( loadedOnto );
		
		// consistency < classify < realize
		//reasoner.getKB().ensureConsistency();		
		// Classify also calls ensureConsistency().
		//reasoner.getKB().classify();
		
		// Realize also calls classify().
		reasoner.getKB().realize();
		
		//reasoner.getKB().printClassTree();

		return true;
	}
	
	/**
	 * Loads an ontology from an URI.
	 * @param owlUrl
	 * @return The ontology reference.
	 */
	private static OWLOntology LoadOntologyURL(OWLOntologyManager ontoMan, String owlUrl)
	{
		IRI owlIRI = IRI.create(owlUrl);
		
		try {
			return ontoMan.loadOntology(owlIRI);
		} catch (OWLOntologyCreationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * Loads an ontology from a file.
	 * @param owlUrl
	 * @return The ontology reference.
	 */
	private static OWLOntology LoadOntologyFile(OWLOntologyManager ontoMan, String owlUrl)
	{		
		try {
			System.out.println("Working Directory = " + System.getProperty("user.dir") +
					" - File to load: " + owlUrl);
			File file = new File(owlUrl);			
			return ontoMan.loadOntologyFromOntologyDocument(file);
		} catch (OWLOntologyCreationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * Split the URI to obtain the ATerm class name.
	 * @param term
	 * @return A non-formatted class name.
	 */
	public static String GetClassNameFromURI(String term)
	{
		String[] termSplitted = term.toString().split("#");
	
		// Split class name from URI.
		String className = termSplitted[(termSplitted.length - 1)];
	
		return className;
	}
		
	/**
	 * Transform a class name in a human readable word.
	 * 
	 * Retrieved from:
	 * http://stackoverflow.com/questions/2559759/how-do-i-convert-camelcase-into-human-readable-names-in-java
	 * 
	 * @param s The class name
	 * @return A human readable string.
	 */
	public static String GetTermName(String s)
	{
	   return s.replaceAll(
	      String.format("%s|%s|%s",
	         "(?<=[A-Z])(?=[A-Z][a-z])",
	         "(?<=[^A-Z])(?=[A-Z])",
	         "(?<=[A-Za-z])(?=[^A-Za-z])"
	      ),
	      " "
	   );
	}
	
	/**
	 * Checks if the class name is valid
	 * 
	 * @param name
	 * @return true if the name is valid; false if the name isn't valid.
	 */
	public static Boolean IsValidClassName(String className)
	{
		if (className.equals("_TOP_") || className.equals("not(_TOP_)") || className.equals("top")) {
			return false;
		} else {
			return true;
		}		
	}
}
