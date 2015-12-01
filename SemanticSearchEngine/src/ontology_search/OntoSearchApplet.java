/*     */ package ontology_search;
/*     */ 
/*     */ import java.applet.Applet;
/*     */ import java.applet.AppletContext;
/*     */ import java.awt.Button;
/*     */ import java.awt.Color;
/*     */ import java.awt.Dimension;
/*     */ import java.awt.Font;
/*     */ import java.awt.Graphics;
/*     */ import java.awt.Rectangle;
/*     */ import java.awt.TextField;
/*     */ import java.awt.event.ActionEvent;
/*     */ import java.awt.event.ActionListener;
/*     */ import java.io.PrintStream;
/*     */ import java.net.MalformedURLException;
/*     */ import java.net.URL;
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
/*     */ 
/*     */ public class OntoSearchApplet
/*     */   extends Applet
/*     */   implements ActionListener
/*     */ {
/*     */   OntoSearch engine;
/*  42 */   Dimension windowSize = new Dimension(800, 600);
/*  43 */   private final int windowCenter_x = this.windowSize.width / 2;
/*  44 */   private final int elementsAlign_x = this.windowCenter_x - 300;
/*  45 */   private final Font generalFont = new Font("TimesRoman", 0, 14);
/*  46 */   private final Font failFont = new Font("TimesRoman", 2, 14);
/*  47 */   private final Color failFontColor = Color.RED;
/*     */   
/*     */ 
/*     */ 
/*  51 */   private final String titleLabel = "OntoApi";
/*  52 */   private final Font titleLabelFont = new Font("TimesRoman", 1, 24);
/*  53 */   private final int titleLabel_x = this.windowCenter_x - 70;
/*  54 */   private final int titleLabel_y = 20;
/*     */   
/*     */ 
/*  57 */   private final String loadLabel = "Load an Ontology (use \"http://\" as prefix to load from a URL):";
/*  58 */   private final int loadLabel_x = this.elementsAlign_x;
/*  59 */   private final int loadLabel_y = 60;
/*     */   TextField loadInput;
/*  61 */   private final int loadInputLenght = 50;
/*  62 */   private final Rectangle loadInputRec = new Rectangle(this.loadLabel_x, 70, 500, 25);
/*     */   Button loadButton;
/*  64 */   private final String loadButtonLabel = "Load";
/*  65 */   private final Rectangle loadButtonRec = new Rectangle(this.loadInputRec.width + 120, this.loadInputRec.y, 50, 25);
/*     */   boolean loadButtonPressed;
/*  67 */   String loadInputMessage = "";
/*  68 */   private final int loadInputMessage_x = this.elementsAlign_x;
/*  69 */   private final int loadInputMessage_y = this.loadInputRec.y + this.loadInputRec.height + 15;
/*     */   
/*     */ 
/*  72 */   private final String searchLabel = "Search Term:";
/*  73 */   private final int searchLabel_x = this.elementsAlign_x;
/*  74 */   private final int searchLabel_y = 140;
/*     */   TextField searchInput;
/*  76 */   private final Rectangle searchInputRec = new Rectangle(this.searchLabel_x, 150, 500, 25);
/*  77 */   private final int searchInputLenght = 50;
/*     */   Button searchButton;
/*  79 */   private final String searchButtonLabel = "Submit";
/*  80 */   private final Rectangle searchButtonRec = new Rectangle(this.searchInputRec.width + 120, this.searchInputRec.y, 50, 25);
/*     */   boolean searchButtonPressed;
/*  82 */   String searchInputMessage = "";
/*  83 */   private final int searchInputMessage_x = this.elementsAlign_x;
/*  84 */   private final int searchInputMessage_y = this.searchButtonRec.y + this.searchButtonRec.height + 15;
/*     */   
/*     */ 
/*  87 */   private final String relatedLabel = "Related Terms:";
/*  88 */   private final int relatedLabel_x = this.elementsAlign_x;
/*  89 */   private final int relatedLabel_y = 220;
/*     */   java.awt.List relatedBox;
/*  91 */   private final Rectangle relatedBoxRec = new Rectangle(this.relatedLabel_x, 230, 500, 300);
/*     */   boolean relatedButtonPressed;
/*     */   Button relatedButton;
/*  94 */   private final String relatedButtonLabel = "Search Related";
/*  95 */   private final Rectangle relatedButtonRec = new Rectangle(this.relatedBoxRec.x, this.relatedBoxRec.height + 235, 100, 25);
/*  96 */   String relatedBoxMessage = "";
/*  97 */   private final int relatedBoxMessage_x = this.elementsAlign_x + 105;
/*  98 */   private final int relatedBoxMessage_y = this.relatedButtonRec.y + 15;
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
/* 111 */   private final String authorsLabel = "Developed by Marta Denisczwicz and Vitor Rozsa";
/* 112 */   private final int authorsLabel_x = this.elementsAlign_x + 200;
/* 113 */   private final int authorsLabel_y = this.relatedButtonRec.y + this.relatedButtonRec.height + 30;
/* 114 */   private final Font authorsLabelFont = new Font("TimesRoman", 1, 14);
/*     */   
/*     */   public void init()
/*     */   {
/* 118 */     setSize(this.windowSize);
/*     */     
/* 120 */     setLayout(null);
/*     */     
/* 122 */     this.loadButton = new Button("Load");
/* 123 */     this.searchButton = new Button("Submit");
/* 124 */     this.relatedButton = new Button("Search Related");
/* 125 */     this.loadInput = new TextField("", 50);
/* 126 */     this.searchInput = new TextField("", 50);
/* 127 */     this.relatedBox = new java.awt.List(0, true);
/*     */     
/* 129 */     this.loadButton.setBounds(this.loadButtonRec);
/* 130 */     this.searchButton.setBounds(this.searchButtonRec);
/* 131 */     this.relatedButton.setBounds(this.relatedButtonRec);
/* 132 */     this.loadInput.setBounds(this.loadInputRec);
/* 133 */     this.searchInput.setBounds(this.searchInputRec);
/* 134 */     this.relatedBox.setBounds(this.relatedBoxRec);
/*     */     
/* 136 */     add(this.loadButton);
/* 137 */     add(this.searchButton);
/* 138 */     add(this.relatedButton);
/* 139 */     add(this.loadInput);
/* 140 */     add(this.searchInput);
/* 141 */     add(this.relatedBox);
/*     */     
/* 143 */     this.loadButtonPressed = false;
/* 144 */     this.searchButtonPressed = false;
/* 145 */     this.relatedButtonPressed = false;
/*     */     
/* 147 */     this.loadButton.addActionListener(this);
/* 148 */     this.searchButton.addActionListener(this);
/* 149 */     this.relatedButton.addActionListener(this);
/*     */     
/* 151 */     this.engine = new OntoSearch();
/*     */   }
/*     */   
/*     */ 
/*     */   private void load_ontology(Graphics g)
/*     */   {
/* 157 */     int ret = 0;
/*     */     
/* 159 */     String ontology_path = this.loadInput.getText();
/* 160 */     if (ontology_path.compareTo("") == 0)
/*     */     {
/* 162 */       return;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/* 167 */     if (this.engine.loaded()) {
/* 168 */       System.out.println("Removing ontology before loading another.");
/* 169 */       this.engine.clear();
/*     */     }
/*     */     
/* 172 */     ret = this.engine.load(ontology_path);
/*     */     
/* 174 */     if (ret == 0) {
/* 175 */       this.loadInputMessage = "Ontology loaded!";
/*     */     }
/*     */     else {
/* 178 */       this.loadInputMessage = "Failed to load the ontology. May be an invalid path.";
/* 179 */       g.setColor(this.failFontColor);
/*     */     }
/*     */     
/* 182 */     g.drawString(this.loadInputMessage, this.loadInputMessage_x, this.loadInputMessage_y);
/*     */   }
/*     */   
/*     */   private void search_term(Graphics g)
/*     */   {
/* 187 */     if (!this.engine.loaded()) {
/* 188 */       this.searchInputMessage = "There is no ontology loaded to search in.";
/* 189 */       g.setColor(this.failFontColor);
/* 190 */       g.drawString(this.searchInputMessage, this.searchInputMessage_x, this.searchInputMessage_y);
/* 191 */       return;
/*     */     }
/*     */     
/* 194 */     this.relatedBox.removeAll();
/*     */     
/* 196 */     String term = this.searchInput.getText();
/* 197 */     if (term.compareTo("") == 0)
/*     */     {
/* 199 */       return;
/*     */     }
/*     */     
/* 202 */     java.util.List<String> related = this.engine.get_all_related(term);
/*     */     
/* 204 */     if (related.isEmpty()) {
/* 205 */       this.searchInputMessage = "Term not found.";
/*     */     }
/*     */     else {
/* 208 */       fill_relatedBox(related);
/* 209 */       this.searchInputMessage = "Term found!";
/*     */     }
/* 211 */     g.drawString(this.searchInputMessage, this.searchInputMessage_x, this.searchInputMessage_y);
/*     */   }
/*     */   
/*     */ 
/*     */   private void fill_relatedBox(java.util.List<String> elements)
/*     */   {
/* 217 */     for (String class_name : elements) {
/* 218 */       this.relatedBox.add(class_name);
/*     */     }
/*     */   }
/*     */   
/*     */   private void search_related(Graphics g)
/*     */   {
/* 224 */     if (!this.engine.loaded()) {
/* 225 */       this.searchInputMessage = "There is no ontology loaded to search in.";
/* 226 */       g.setColor(this.failFontColor);
/* 227 */       g.drawString(this.searchInputMessage, this.searchInputMessage_x, this.searchInputMessage_y);
/* 228 */       return;
/*     */     }
/*     */     
/* 231 */     String term = this.searchInput.getText();
/* 232 */     if (term.compareTo("") == 0) {
/* 233 */       this.searchInputMessage = "The search term's field is empty!";
/* 234 */       g.setColor(this.failFontColor);
/* 235 */       g.drawString(this.searchInputMessage, this.searchInputMessage_x, this.searchInputMessage_y);
/* 236 */       return;
/*     */     }
/*     */     
/* 239 */     String[] related_elements = this.relatedBox.getSelectedItems();
/*     */     
/* 241 */     if (related_elements.length == 0) {
/* 242 */       this.relatedBoxMessage = "No related terms were selected!";
/* 243 */       g.setColor(this.failFontColor);
/* 244 */       g.drawString(this.relatedBoxMessage, this.relatedBoxMessage_x, this.relatedBoxMessage_y);
/* 245 */       return;
/*     */     }
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
/* 260 */     String search_engine = "www.google.com.br";
/* 261 */     String default_params = "#safe=off&q=";
/*     */     
/* 263 */     String searchUrl = "http://" + search_engine + "/" + default_params + "%22" + term + "%22";
/* 264 */     for (int i = 0; i < related_elements.length; i++) {
/* 265 */       searchUrl = searchUrl + "+%22" + related_elements[i] + "\"";
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     try
/*     */     {
/* 276 */       getAppletContext().showDocument(new URL(searchUrl), "_blank");
/*     */       
/* 278 */       System.out.println("finalString: " + searchUrl);
/*     */     } catch (MalformedURLException e1) {
/* 280 */       e1.printStackTrace();
/*     */     }
/*     */   }
/*     */   
/*     */   public void paint(Graphics g)
/*     */   {
/* 286 */     g.setFont(this.titleLabelFont);
/* 287 */     g.drawString("OntoApi", this.titleLabel_x, 20);
/* 288 */     g.setFont(this.generalFont);
/* 289 */     g.drawString("Load an Ontology (use \"http://\" as prefix to load from a URL):", this.loadLabel_x, 60);
/* 290 */     g.drawString("Search Term:", this.searchLabel_x, 140);
/* 291 */     g.drawString("Related Terms:", this.relatedLabel_x, 220);
/*     */     
/* 293 */     g.setFont(this.authorsLabelFont);
/* 294 */     g.drawString("Developed by Marta Denisczwicz and Vitor Rozsa", this.authorsLabel_x, this.authorsLabel_y);
/* 295 */     g.setFont(this.generalFont);
/*     */     
/*     */ 
/* 298 */     if (this.loadButtonPressed) {
/* 299 */       this.loadButtonPressed = false;
/* 300 */       load_ontology(g);
/*     */     }
/* 302 */     else if (this.searchButtonPressed) {
/* 303 */       this.searchButtonPressed = false;
/* 304 */       search_term(g);
/*     */     }
/* 306 */     else if (this.relatedButtonPressed) {
/* 307 */       this.relatedButtonPressed = false;
/* 308 */       search_related(g);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void actionPerformed(ActionEvent evt)
/*     */   {
/* 317 */     Object event = evt.getSource();
/* 318 */     if (event == this.loadButton) {
/* 319 */       this.loadButtonPressed = true;
/*     */     }
/* 321 */     else if (event == this.searchButton) {
/* 322 */       this.searchButtonPressed = true;
/*     */     }
/* 324 */     else if (event == this.relatedButton) {
/* 325 */       this.relatedButtonPressed = true;
/*     */     }
/*     */     else
/*     */     {
/* 329 */       return;
/*     */     }
/*     */     
/* 332 */     repaint();
/*     */   }
/*     */ }


/* Location:              D:\workspace\Programming\java\ontologia_marta\bin\!\ontology_search\OntoSearchApplet.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */