package com.dendriel.reasoner;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyManager;

import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.rdf.model.ModelFactory;

import aterm.ATermAppl;

import com.clarkparsia.pellet.owlapiv3.PelletReasoner;
import com.clarkparsia.pellet.owlapiv3.PelletReasonerFactory;


public class SemanticSearchCore
{
	private OWLOntologyManager ontoMan;
	private OWLOntology loadedOnto;
	private PelletReasoner reasoner;
	private String loadedOntologyPath;
	
	private static final String httpProtoStr = "http://";
	private static final String httpsProtoStr = "http://";
	
	public SemanticSearchCore()
	{
		ontoMan = OWLManager.createOWLOntologyManager();
		//reasoner = ModelFactory.createOntologyModel(PelletReasonerFactory.THE_SPEC);
	}

	/**
	 * Get all terms from the ontology.
	 * @return A list of ATerms.
	 */
	public Set<ATermAppl> GetAllTerms()
	{
		return reasoner.getKB().getAllClasses();
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
			if (className.equals("_TOP_") || className.equals("not(_TOP_)") || className.equals("top")) {
				continue;
			}
			
			classListStr.add(GetTermName(className));
		}
		
		return classListStr;
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
		
		loadedOntologyPath = path;
		
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
	private String GetClassNameFromURI(String term)
	{
		String[] termSplitted = term.toString().split("#");
	
		// Split class name from URI.
		String className = termSplitted[(termSplitted.length - 1)];
		
		//className = className.replaceAll("_", " ");	
		//className = className.toLowerCase();	
		//className = className.replaceAll("\\s", "");
	
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
	private static String GetTermName(String s)
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
}
