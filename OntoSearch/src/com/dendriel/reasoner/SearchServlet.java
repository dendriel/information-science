package com.dendriel.reasoner;

import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Path;
import java.nio.file.Paths;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Servlet implementation class SearchServlet
 */
@WebServlet("/SearchServlet")
public class SearchServlet extends HttpServlet
{
	private static final long serialVersionUID = 1L;

	private static final String smtSearchCoreKey = "smtSearchCore";
	private static final String kwdSearchCoreKey = "kwdSearchCore";
	private static final String searchIfBuilderKey = "searchIfBuilder";
	
	// Keys and values from HTML forms.
	private static final String pageIdKey = "page_id";

	// Page names.
	private static final String emptyPage = "/empty.jsp";
	
	/**
     * @see HttpServlet#HttpServlet()
     */
    public SearchServlet()
    {
        super();
    }
    
    public void init(ServletConfig servletConfig) throws ServletException
    {
        super.init(servletConfig);
        //ServletContext ctx = getServletContext();//request.getSession(true);
        
        // Create the Keyword Search Core for handling the keyword search,
        //ctx.setAttribute(kwdSearchCoreKey, new KeywordSearchCore());
        // Create the Search Interface Builder.
        //ctx.setAttribute(searchIfBuilderKey, new SearchInterfaceBuilder());
    }


	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException
	{
		doPost(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException
	{
		//doGet(request, response);
		
		selectOperation(request, response);
	}

	/**
	 * Call the appropriated method to handle the request.
	 * @param request
	 * @param response
	 */
	void selectOperation(HttpServletRequest request, HttpServletResponse response)
	{
		String pageId = request.getParameter(pageIdKey);
		
		// If there is no page ID, is the first access. Load the default page.
		if (pageId == null) pageId = "";
		
		switch(pageId)
		{
			case SearchInterfaceBuilder.LoadPageId:
				try {
					handleLoadPage(request, response);
				} catch (IOException | ServletException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			break;
		
			case SearchInterfaceBuilder.SearchPageId:
				try {
					handleSearchPage(request, response);
				} catch (ServletException | IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			break;
			default:
				try {
					loadDefaultPage(request, response);
				} catch (IOException | ServletException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			break;
		}
	}
	
	/**
	 * It handles the ontology loading page interaction.
	 * @param request
	 * @param response
	 * @throws IOException 
	 * @throws ServletException 
	 */
	void handleLoadPage(HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException
	{
		log("At the load page.");
		HttpSession session = request.getSession();
		
		SemanticSearchCore smtCore = (SemanticSearchCore) session.getAttribute(smtSearchCoreKey);
		if (smtCore == null) {
			// 	Create the Semantic Search Core for handling the ontology.
			smtCore = new SemanticSearchCore();
			session.setAttribute(smtSearchCoreKey, smtCore);
		}
		
		smtCore.LoadOntology(request.getParameter(SearchInterfaceBuilder.LoadPageInputKey));

		// Display the seach page.
		handleSearchPage(request, response);
		
		log("At the load page exit.");
	}
	
	/**
	 * It handles the search page interaction.
	 * @param request
	 * @param response
	 * @throws IOException 
	 * @throws ServletException 
	 */
	void handleSearchPage(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException
	{
        RequestDispatcher rd = getServletContext().getRequestDispatcher(emptyPage);
        PrintWriter out = response.getWriter();
        

        request.setAttribute("vartest", "HELLO");
        
        out.println(SearchInterfaceBuilder.BuildSearchPage((SemanticSearchCore) request.getSession().getAttribute(smtSearchCoreKey)));        
        rd.include(request, response);
	}
	
	/**
	 * Load the default page.
	 * @param request
	 * @param response
	 * @throws IOException 
	 * @throws ServletException 
	 */
	void loadDefaultPage(HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException
	{
        RequestDispatcher rd = getServletContext().getRequestDispatcher(emptyPage);
        PrintWriter out = response.getWriter();
        
        out.println(SearchInterfaceBuilder.BuildLoadPage());        
        rd.include(request, response);
	}
}
