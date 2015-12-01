/*    */ package ontology_search;
/*    */ import org.semanticweb.owlapi.model.IRI;
/*    */ import org.semanticweb.owlapi.model.OWLAnnotation;
/*    */ import org.semanticweb.owlapi.model.OWLAnnotationAssertionAxiom;
/*    */ import org.semanticweb.owlapi.model.OWLAnnotationObjectVisitor;
/*    */ import org.semanticweb.owlapi.model.OWLAnnotationProperty;
/*    */ import org.semanticweb.owlapi.model.OWLAnnotationPropertyDomainAxiom;
/*    */ import org.semanticweb.owlapi.model.OWLAnnotationPropertyRangeAxiom;
/*    */ import org.semanticweb.owlapi.model.OWLAnnotationValue;
/*    */ import org.semanticweb.owlapi.model.OWLAnonymousIndividual;
/*    */ import org.semanticweb.owlapi.model.OWLLiteral;
/*    */ import org.semanticweb.owlapi.model.OWLSubAnnotationPropertyOfAxiom;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class LabelExtractor
/*    */   implements OWLAnnotationObjectVisitor
/*    */ {
/*    */   String result;
/*    */   
/*    */   public LabelExtractor()
/*    */   {
/* 27 */     this.result = null;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */   public void visit(OWLAnonymousIndividual individual) {}
/*    */   
/*    */ 
/*    */ 
/*    */   public void visit(IRI iri) {}
/*    */   
/*    */ 
/*    */ 
/*    */   public void visit(OWLLiteral literal) {}
/*    */   
/*    */ 
/*    */   public void visit(OWLAnnotation annotation)
/*    */   {
/* 45 */     if (annotation.getProperty().isLabel()) {
/* 46 */       OWLLiteral c = (OWLLiteral)annotation.getValue();
/* 47 */       this.result = c.getLiteral();
/*    */     }
/*    */   }
/*    */   
/*    */ 
/*    */   public void visit(OWLAnnotationAssertionAxiom axiom) {}
/*    */   
/*    */ 
/*    */   public void visit(OWLAnnotationPropertyDomainAxiom axiom) {}
/*    */   
/*    */ 
/*    */   public void visit(OWLAnnotationPropertyRangeAxiom axiom) {}
/*    */   
/*    */   public void visit(OWLSubAnnotationPropertyOfAxiom axiom) {}
/*    */   
/*    */   public void visit(OWLAnnotationProperty property) {}
/*    */   
/*    */   public void visit(OWLAnnotationValue value) {}
/*    */   
/*    */   public String getResult()
/*    */   {
/* 68 */     return this.result;
/*    */   }
/*    */ }


/* Location:              D:\workspace\Programming\java\ontologia_marta\bin\!\ontology_search\LabelExtractor.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */