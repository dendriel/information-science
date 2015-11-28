/*     */ package ontology_search;
/*     */ 
/*     */ import aterm.ATermAppl;
/*     */ import com.clarkparsia.pellet.owlapiv3.PelletReasoner;
/*     */ import com.clarkparsia.pellet.owlapiv3.PelletReasonerFactory;
/*     */ import java.io.File;
/*     */ import java.io.PrintStream;
/*     */ import java.util.ArrayList;
/*     */ import java.util.HashMap;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import org.mindswap.pellet.KnowledgeBase;
/*     */ import org.semanticweb.owlapi.apibinding.OWLManager;
/*     */ import org.semanticweb.owlapi.model.IRI;
/*     */ import org.semanticweb.owlapi.model.OWLOntology;
/*     */ import org.semanticweb.owlapi.model.OWLOntologyCreationException;
/*     */ import org.semanticweb.owlapi.model.OWLOntologyManager;
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
/*     */ public class OntoSearch
/*     */ {
/*     */   public static final String m_URLPrefix = "http://";
/*     */   private Map<String, ATermAppl> m_OntoClassArr;
/*     */   private String m_OntoRef;
/*     */   private OWLOntologyManager m_OntoManager;
/*     */   private OWLOntology m_Ontology;
/*     */   private PelletReasoner m_Reasoner;
/*     */   private KnowledgeBase m_KnowledgeBase;
/*     */   private boolean m_OntologyLoaded;
/*     */   
/*     */   OntoSearch()
/*     */   {
/*  44 */     this.m_OntoManager = OWLManager.createOWLOntologyManager();
/*  45 */     this.m_OntoClassArr = new HashMap();
/*  46 */     this.m_OntologyLoaded = false;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public int load(String onto_ref)
/*     */   {
/*     */     try
/*     */     {
/*  58 */       this.m_OntoRef = onto_ref;
/*     */       
/*  60 */       debug("Loading ontology from \"" + onto_ref + "\"...");
/*     */       
/*     */ 
/*  63 */       debug("Will load the ontology from source...");
/*  64 */       if (this.m_OntoRef.toLowerCase().contains("http://")) {
/*  65 */         this.m_Ontology = this.m_OntoManager.loadOntology(IRI.create(this.m_OntoRef));
/*  66 */         debug("Ontology loaded from URL!");
/*     */       }
/*     */       else
/*     */       {
/*  70 */         File ontoFile = new File(this.m_OntoRef);
/*  71 */         this.m_Ontology = this.m_OntoManager.loadOntologyFromOntologyDocument(ontoFile);
/*  72 */         debug("Ontology loaded from file!");
/*     */       }
/*     */       
/*  75 */       debug("Creating the Pellet reasoner...");
/*  76 */       this.m_Reasoner = PelletReasonerFactory.getInstance().createReasoner(this.m_Ontology);
/*     */       
/*  78 */       debug("Reasoning over the ontology...");
/*  79 */       this.m_KnowledgeBase = this.m_Reasoner.getKB();
/*  80 */       this.m_KnowledgeBase.realize();
/*     */       
/*  82 */       debug("Storing class names...");
/*  83 */       get_class_names();
/*     */       
/*  85 */       this.m_OntologyLoaded = true;
/*  86 */       debug("Done loading the ontology!");
/*     */     }
/*     */     catch (Exception e) {
/*  89 */       this.m_OntologyLoaded = false;
/*  90 */       this.m_OntoRef = "";
/*  91 */       this.m_Reasoner = null;
/*  92 */       this.m_KnowledgeBase = null;
/*  93 */       this.m_OntoClassArr.clear();
/*     */       
/*  95 */       System.out.println("Failed to load the ontology. " + e);
/*  96 */       return -1;
/*     */     }
/*     */     
/*  99 */     return 0;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private static void appendToFile(Exception e)
/*     */   {
/*     */     try
/*     */     {
/* 113 */       e.printStackTrace();
/*     */     }
/*     */     catch (Exception ie) {
/* 116 */       throw new RuntimeException("Could not write Exception to file", ie);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private void debug(String msg)
/*     */   {
/* 128 */     System.out.println(msg);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void clear()
/*     */   {
/* 136 */     this.m_OntologyLoaded = false;
/* 137 */     this.m_OntoRef = "";
/* 138 */     this.m_Reasoner = null;
/* 139 */     this.m_KnowledgeBase = null;
/* 140 */     this.m_OntoClassArr.clear();
/* 141 */     this.m_OntoManager.removeOntology(this.m_Ontology);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean loaded()
/*     */   {
/* 150 */     return this.m_OntologyLoaded;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private void get_class_names()
/*     */   {
/* 158 */     Set<ATermAppl> class_list = this.m_KnowledgeBase.getClasses();
/*     */     
/* 160 */     System.out.println("There is " + class_list.size() + " in this ontology.");
/*     */     
/*     */ 
/* 163 */     for (ATermAppl elem : class_list)
/*     */     {
/* 165 */       this.m_OntoClassArr.put(get_name_part(elem.toString()), elem);
/*     */     }
/*     */   }
/*     */   
/*     */   private String get_name_part(String term)
/*     */   {
/* 171 */     String[] term_splitted = term.toString().split("#");
/*     */     
/* 173 */     String class_name = term_splitted[(term_splitted.length - 1)];
/*     */     
/* 175 */     class_name = class_name.replaceAll("_", "");
/*     */     
/* 177 */     class_name = class_name.toLowerCase();
/*     */     
/* 179 */     class_name = class_name.replaceAll("\\s", "");
/*     */     
/* 181 */     return class_name;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void print_tree()
/*     */   {
/* 189 */     this.m_KnowledgeBase.printClassTree();
/*     */   }
/*     */   
/*     */   public void do_stuff()
/*     */   {
/* 194 */     for (String elem : this.m_OntoClassArr.keySet()) {
/* 195 */       System.out.println(elem);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean is_class_present(String name)
/*     */   {
/* 207 */     for (String c_elem : this.m_OntoClassArr.keySet())
/*     */     {
/* 209 */       if (c_elem.contains(name)) {
/* 210 */         System.out.println("Term found in the ontology. \"" + c_elem + "\" [" + this.m_OntoClassArr.get(c_elem) + "]");
/* 211 */         return true;
/*     */       }
/*     */     }
/*     */     
/* 215 */     System.out.println("Term no found in the ontology. \"" + name + "\"");
/* 216 */     return false;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void print_if_found(String term)
/*     */   {
/* 227 */     String[] term_arr = term.toLowerCase().split(" ");
/*     */     
/*     */     String[] arrayOfString1;
/* 230 */     int j = (arrayOfString1 = term_arr).length; for (int i = 0; i < j; i++) { String s_elem = arrayOfString1[i];
/*     */       
/*     */ 
/* 233 */       for (String c_elem : this.m_OntoClassArr.keySet())
/*     */       {
/* 235 */         if (c_elem.contains(s_elem)) {
/* 236 */           System.out.println("Term found in the ontology. \"" + c_elem + "\" [" + this.m_OntoClassArr.get(c_elem) + "]");
/* 237 */           print_related((ATermAppl)this.m_OntoClassArr.get(c_elem));
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public List<String> get_all_related(String term)
/*     */   {
/* 250 */     String[] term_arr = term.toLowerCase().split(" ");
/* 251 */     List<String> terms_list = new ArrayList();
/*     */     
/*     */     String[] arrayOfString1;
/* 254 */     int j = (arrayOfString1 = term_arr).length; for (int i = 0; i < j; i++) { String s_elem = arrayOfString1[i];
/*     */       
/*     */ 
/* 257 */       for (String c_elem : this.m_OntoClassArr.keySet())
/*     */       {
/* 259 */         if (c_elem.contains(s_elem)) {
/* 260 */           get_related((ATermAppl)this.m_OntoClassArr.get(c_elem), terms_list);
/*     */         }
/*     */       }
/*     */     }
/*     */     
/* 265 */     return terms_list;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private void get_related(ATermAppl ref, List<String> terms_list)
/*     */   {
/* 276 */     Set<Set<ATermAppl>> class_list = this.m_KnowledgeBase.getSuperClasses(ref);
/* 277 */     Iterator localIterator2; for (Iterator localIterator1 = class_list.iterator(); localIterator1.hasNext(); 
/*     */         
/* 279 */         localIterator2.hasNext())
/*     */     {
/* 277 */       Set<ATermAppl> class_ref = (Set)localIterator1.next();
/*     */       
/* 279 */       localIterator2 = class_ref.iterator(); continue;ATermAppl class_iri = (ATermAppl)localIterator2.next();
/* 280 */       String name_iri = class_iri.toString();
/*     */       
/* 282 */       if ((!name_iri.equals("_TOP_")) && (!name_iri.equals("not(_TOP_)")) && (!name_iri.equals("top")))
/*     */       {
/*     */ 
/* 285 */         String name = get_name_part(name_iri);
/* 286 */         if (!terms_list.contains(name)) {
/* 287 */           terms_list.add(name);
/*     */         }
/*     */       }
/*     */     }
/*     */     
/*     */ 
/* 293 */     class_list = this.m_KnowledgeBase.getSubClasses(ref);
/* 294 */     for (localIterator1 = class_list.iterator(); localIterator1.hasNext(); 
/*     */         
/* 296 */         localIterator2.hasNext())
/*     */     {
/* 294 */       Set<ATermAppl> class_ref = (Set)localIterator1.next();
/*     */       
/* 296 */       localIterator2 = class_ref.iterator(); continue;ATermAppl class_iri = (ATermAppl)localIterator2.next();
/* 297 */       String name_iri = class_iri.toString();
/*     */       
/* 299 */       if ((!name_iri.equals("_TOP_")) && (!name_iri.equals("not(_TOP_)")) && (!name_iri.equals("top")))
/*     */       {
/*     */ 
/* 302 */         String name = get_name_part(name_iri);
/* 303 */         if (!terms_list.contains(name)) {
/* 304 */           terms_list.add(name);
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private void print_related(ATermAppl ref)
/*     */   {
/* 316 */     System.out.println("Superclasses: ");
/* 317 */     Set<Set<ATermAppl>> class_list = this.m_KnowledgeBase.getSuperClasses(ref);
/* 318 */     Iterator localIterator2; for (Iterator localIterator1 = class_list.iterator(); localIterator1.hasNext(); 
/*     */         
/* 320 */         localIterator2.hasNext())
/*     */     {
/* 318 */       Set<ATermAppl> class_ref = (Set)localIterator1.next();
/*     */       
/* 320 */       localIterator2 = class_ref.iterator(); continue;ATermAppl class_iri = (ATermAppl)localIterator2.next();
/* 321 */       System.out.println(get_name_part(class_iri.toString()));
/*     */     }
/*     */     
/*     */ 
/* 325 */     System.out.println("Subclasses: ");
/* 326 */     class_list = this.m_KnowledgeBase.getSubClasses(ref);
/* 327 */     for (localIterator1 = class_list.iterator(); localIterator1.hasNext(); 
/*     */         
/* 329 */         localIterator2.hasNext())
/*     */     {
/* 327 */       Set<ATermAppl> class_ref = (Set)localIterator1.next();
/*     */       
/* 329 */       localIterator2 = class_ref.iterator(); continue;ATermAppl class_iri = (ATermAppl)localIterator2.next();
/* 330 */       System.out.println(get_name_part(class_iri.toString()));
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   public static void main(String[] args)
/*     */     throws OWLOntologyCreationException
/*     */   {
/* 338 */     String onto_url = "http://owl.man.ac.uk/2006/07/sssw/people.owl";
/* 339 */     OntoSearch tool = new OntoSearch();
/*     */     
/* 341 */     tool.load(onto_url);
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 347 */     List<String> related = tool.get_all_related("animal tiger");
/*     */     
/* 349 */     for (String class_name : related) {
/* 350 */       System.out.println(class_name);
/*     */     }
/*     */   }
/*     */ }


/* Location:              D:\workspace\Programming\java\ontologia_marta\bin\!\ontology_search\OntoSearch.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */