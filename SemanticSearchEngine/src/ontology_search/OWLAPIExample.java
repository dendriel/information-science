/*    */ package ontology_search;
/*    */ 
/*    */ import com.clarkparsia.pellet.owlapiv3.PelletReasoner;
/*    */ import com.clarkparsia.pellet.owlapiv3.PelletReasonerFactory;
/*    */ import java.io.PrintStream;
/*    */ import java.util.Iterator;
/*    */ import java.util.Set;
/*    */ import org.mindswap.pellet.KnowledgeBase;
/*    */ import org.semanticweb.owlapi.apibinding.OWLManager;
/*    */ import org.semanticweb.owlapi.model.IRI;
/*    */ import org.semanticweb.owlapi.model.OWLClass;
/*    */ import org.semanticweb.owlapi.model.OWLDataFactory;
/*    */ import org.semanticweb.owlapi.model.OWLDataProperty;
/*    */ import org.semanticweb.owlapi.model.OWLLiteral;
/*    */ import org.semanticweb.owlapi.model.OWLNamedIndividual;
/*    */ import org.semanticweb.owlapi.model.OWLObjectProperty;
/*    */ import org.semanticweb.owlapi.model.OWLOntology;
/*    */ import org.semanticweb.owlapi.model.OWLOntologyManager;
/*    */ import org.semanticweb.owlapi.reasoner.Node;
/*    */ import org.semanticweb.owlapi.reasoner.NodeSet;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class OWLAPIExample
/*    */ {
/*    */   public static final void main(String[] args)
/*    */     throws Exception
/*    */   {
/* 36 */     String file = "http://owl.man.ac.uk/2006/07/sssw/people.owl";
/*    */     
/* 38 */     System.out.print("Reading file " + file + "...");
/* 39 */     OWLOntologyManager manager = OWLManager.createOWLOntologyManager();
/* 40 */     OWLOntology ontology = manager.loadOntology(IRI.create(file));
/*    */     
/* 42 */     PelletReasoner reasoner = PelletReasonerFactory.getInstance().createReasoner(ontology);
/* 43 */     System.out.println("done.");
/*    */     
/* 45 */     reasoner.getKB().realize();
/* 46 */     reasoner.getKB().printClassTree();
/*    */     
/*    */ 
/* 49 */     OWLClass Person = manager.getOWLDataFactory().getOWLClass(IRI.create("http://xmlns.com/foaf/0.1/Person"));
/* 50 */     OWLObjectProperty workHomepage = manager.getOWLDataFactory().getOWLObjectProperty(IRI.create("http://xmlns.com/foaf/0.1/workInfoHomepage"));
/* 51 */     OWLDataProperty foafName = manager.getOWLDataFactory().getOWLDataProperty(IRI.create("http://xmlns.com/foaf/0.1/name"));
/*    */     
/*    */ 
/* 54 */     NodeSet<OWLNamedIndividual> individuals = reasoner.getInstances(Person, false);
/* 55 */     for (Node<OWLNamedIndividual> sameInd : individuals)
/*    */     {
/* 57 */       OWLNamedIndividual ind = (OWLNamedIndividual)sameInd.getRepresentativeElement();
/*    */       
/*    */ 
/* 60 */       Set<OWLLiteral> names = reasoner.getDataPropertyValues(ind, foafName);
/* 61 */       NodeSet<OWLClass> types = reasoner.getTypes(ind, true);
/* 62 */       NodeSet<OWLNamedIndividual> homepages = reasoner.getObjectPropertyValues(ind, workHomepage);
/*    */       
/*    */ 
/* 65 */       String name = ((OWLLiteral)names.iterator().next()).getLiteral();
/* 66 */       System.out.println("Name: " + name);
/*    */       
/*    */ 
/* 69 */       OWLClass type = (OWLClass)((Node)types.iterator().next()).getRepresentativeElement();
/* 70 */       System.out.println("Type:" + type);
/*    */       
/*    */ 
/* 73 */       if (homepages.isEmpty()) {
/* 74 */         System.out.print("Homepage: Unknown");
/*    */       }
/*    */       else {
/* 77 */         System.out.print("Homepage:");
/* 78 */         for (Node<OWLNamedIndividual> homepage : homepages) {
/* 79 */           System.out.print(" " + ((OWLNamedIndividual)homepage.getRepresentativeElement()).getIRI());
/*    */         }
/*    */       }
/* 82 */       System.out.println();
/* 83 */       System.out.println();
/*    */     }
/*    */   }
/*    */ }


/* Location:              D:\workspace\Programming\java\ontologia_marta\bin\!\ontology_search\OWLAPIExample.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */