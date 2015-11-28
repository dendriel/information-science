/*     */ package ontology_search;
/*     */ 
/*     */ import com.clarkparsia.pellet.owlapiv3.PelletReasoner;
/*     */ import com.clarkparsia.pellet.owlapiv3.PelletReasonerFactory;
/*     */ import java.io.PrintStream;
/*     */ import java.util.Iterator;
/*     */ import java.util.Set;
/*     */ import org.semanticweb.owlapi.apibinding.OWLManager;
/*     */ import org.semanticweb.owlapi.model.IRI;
/*     */ import org.semanticweb.owlapi.model.OWLAnnotation;
/*     */ import org.semanticweb.owlapi.model.OWLClass;
/*     */ import org.semanticweb.owlapi.model.OWLDataFactory;
/*     */ import org.semanticweb.owlapi.model.OWLException;
/*     */ import org.semanticweb.owlapi.model.OWLOntology;
/*     */ import org.semanticweb.owlapi.model.OWLOntologyManager;
/*     */ import org.semanticweb.owlapi.reasoner.NodeSet;
/*     */ import org.semanticweb.owlapi.reasoner.OWLReasoner;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class SimpleHierarchyExample
/*     */ {
/*  32 */   private static int INDENT = 4;
/*     */   private final PelletReasonerFactory reasonerFactory;
/*     */   private final OWLOntology ontology;
/*     */   private final PrintStream out;
/*     */   
/*     */   private SimpleHierarchyExample(PelletReasonerFactory reasonerFactory, OWLOntology _ontology)
/*     */   {
/*  39 */     this.reasonerFactory = reasonerFactory;
/*  40 */     this.ontology = _ontology;
/*  41 */     this.out = System.out;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private void printHierarchy(OWLClass clazz)
/*     */     throws OWLException
/*     */   {
/*  51 */     PelletReasoner reasoner = this.reasonerFactory.createReasoner(this.ontology);
/*  52 */     printHierarchy(reasoner, clazz, 0);
/*     */     
/*  54 */     for (OWLClass cl : this.ontology.getClassesInSignature()) {
/*  55 */       if (!reasoner.isSatisfiable(cl)) {
/*  56 */         this.out.println("XXX: " + labelFor(cl));
/*     */       }
/*     */     }
/*  59 */     reasoner.dispose();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   private String labelFor(OWLClass clazz)
/*     */   {
/*  66 */     LabelExtractor le = new LabelExtractor();
/*  67 */     Set<OWLAnnotation> annotations = clazz.getAnnotations(this.ontology);
/*  68 */     for (OWLAnnotation anno : annotations) {
/*  69 */       anno.accept(le);
/*     */     }
/*     */     
/*  72 */     if (le.getResult() != null) {
/*  73 */       return le.getResult().toString();
/*     */     }
/*  75 */     return clazz.getIRI().toString();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private void printHierarchy(OWLReasoner reasoner, OWLClass clazz, int level)
/*     */     throws OWLException
/*     */   {
/*  91 */     if (reasoner.isSatisfiable(clazz)) {
/*  92 */       for (int i = 0; i < level * INDENT; i++) {
/*  93 */         this.out.print(" ");
/*     */       }
/*  95 */       this.out.println(labelFor(clazz));
/*     */       
/*     */ 
/*  98 */       Iterator localIterator = reasoner.getSubClasses(clazz, true).getFlattened().iterator();
/*  97 */       while (localIterator.hasNext()) {
/*  98 */         OWLClass child = (OWLClass)localIterator.next();
/*  99 */         if (!child.equals(clazz)) {
/* 100 */           printHierarchy(reasoner, child, level + 1);
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   public static void main(String[] args)
/*     */     throws OWLException, InstantiationException, IllegalAccessException, ClassNotFoundException
/*     */   {
/* 110 */     String onto_path = "http://protege.cim3.net/file/pub/ontologies/travel/travel.owl";
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 130 */     PelletReasonerFactory reasonerFactory = PelletReasonerFactory.getInstance();
/*     */     
/*     */ 
/*     */ 
/*     */ 
/* 135 */     OWLOntologyManager manager = OWLManager.createOWLOntologyManager();
/*     */     
/*     */ 
/* 138 */     System.out.println(onto_path);
/* 139 */     IRI documentIRI = IRI.create(onto_path);
/*     */     
/*     */ 
/* 142 */     OWLOntology ontology = manager.loadOntology(documentIRI);
/*     */     
/* 144 */     System.out.println("Ontology Loaded...");
/* 145 */     System.out.println("Document IRI: " + documentIRI);
/* 146 */     System.out.println("Ontology : " + ontology.getOntologyID());
/* 147 */     System.out.println("Format      : " + manager.getOntologyFormat(ontology));
/*     */     
/*     */ 
/* 150 */     SimpleHierarchyExample simpleHierarchy = new SimpleHierarchyExample(reasonerFactory, ontology);
/* 151 */     System.out.println("[0]");
/*     */     
/* 153 */     OWLClass clazz = manager.getOWLDataFactory().getOWLThing();
/* 154 */     System.out.println("Class       : " + clazz);
/*     */     
/* 156 */     simpleHierarchy.printHierarchy(clazz);
/*     */   }
/*     */ }


/* Location:              D:\workspace\Programming\java\ontologia_marta\bin\!\ontology_search\SimpleHierarchyExample.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */