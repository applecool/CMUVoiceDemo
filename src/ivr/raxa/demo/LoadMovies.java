package ivr.raxa.demo;

import java.io.IOException;
import java.net.URL;

import javax.speech.EngineException;
import javax.speech.EngineStateError;
import javax.speech.recognition.GrammarException;
import javax.speech.recognition.Rule;
import javax.speech.recognition.RuleGrammar;
import javax.speech.recognition.RuleParse;

import com.sun.speech.engine.recognition.BaseRecognizer;
import com.sun.speech.engine.recognition.BaseRuleGrammar;


import edu.cmu.sphinx.frontend.util.Microphone;
import edu.cmu.sphinx.jsgf.JSGFGrammar;
import edu.cmu.sphinx.recognizer.Recognizer;
import edu.cmu.sphinx.result.Result;
import edu.cmu.sphinx.util.props.ConfigurationManager;

public class LoadMovies {

	  private Recognizer recognizer;
	    private JSGFGrammar jsgfGrammar;
	    private Microphone microphone;
	    private BaseRecognizer jsapiRecognizer;

	public LoadMovies() throws Exception, EngineStateError {
			super();
			// TODO Auto-generated constructor stub
			 URL url = LoadMovies.class.getResource("movies.config.xml");
		        ConfigurationManager cm = new ConfigurationManager(url);

		        // retrive the recognizer, jsgfGrammar and the microphone from
		        // the configuration file.

		        recognizer = (Recognizer) cm.lookup("recognizer");
		        jsgfGrammar = (JSGFGrammar) cm.lookup("jsgfGrammar");
		        microphone = (Microphone) cm.lookup("microphone");
		        
		        jsapiRecognizer = new BaseRecognizer(jsgfGrammar.getGrammarManager());
		        jsapiRecognizer.allocate();
		}
	
	
	
	
	public static void main(String[] args) throws Exception, EngineStateError {
		// TODO Auto-generated method stub
			LoadMovies l = new LoadMovies();
	       
	        l.recognizer.allocate();
	       
	        if (!l.microphone.startRecording()) {
	            System.out.println("Cannot start microphone.");
	            l.recognizer.deallocate();
	            System.exit(1);
	        }
	     
	        l.jsgfGrammar.loadJSGF("books");
	        System.out.println(" ====== Books======");
            System.out.println("Speak one of: \n");
            l.jsgfGrammar.dumpRandomSentences(200);
        
            l.recognizeAndReport();
            l.jsgfGrammar.loadJSGF("songs");
     	   System.out.println(" ====== Songs ======");
            System.out.println("Speak one of: \n");
            l.jsgfGrammar.dumpRandomSentences(200);
            System.out.println(" ============================");
             l.recognizeAndReport();
	        l.jsgfGrammar.loadJSGF("category");
	        	   System.out.println(" ====== Category ======");
	               System.out.println("Speak one of: \n");
	               l.jsgfGrammar.dumpRandomSentences(200);
	               System.out.println(" ============================");
	               String value = l.recongnizeAndReportCategory();
	                    
	             l.loadAndRecognizeCategory(value);
	        		           
	        

	          
	        
	} 
	        private String recongnizeAndReportCategory() throws GrammarException {
		// TODO Auto-generated method stub
	        	boolean done = false;
	            String value = null;

	         //   while (!done)  {
	                Result result = recognizer.recognize();
	                String bestResult = result.getBestFinalResultNoFiller();
	                RuleGrammar ruleGrammar = new BaseRuleGrammar (jsapiRecognizer, jsgfGrammar.getRuleGrammar());
	                RuleParse ruleParse = ruleGrammar.parse(bestResult, null);
	                if (ruleParse != null) {
	                    System.out.println("\n  " + bestResult + '\n');
	                    //done = isExit(ruleParse);
	                    value = ruleParse.getTags()[0];
	          //      } 
	            }
	            return value;
	}
			private static boolean isExit(RuleParse ruleParse) {
	            String[] tags = ruleParse.getTags();

	            for (int i = 0; tags != null && i < tags.length; i++) {
	                if (tags[i].trim().equals("exit")) {
	                    return true;
	                }
	            }
	            return  false;
	        }
		
	        private void loadAndRecognizeCategory(String category) throws IOException, GrammarException {
	            try {
	                jsgfGrammar.loadJSGF("selectedcategory");
	                RuleGrammar ruleGrammar = new BaseRuleGrammar(jsapiRecognizer, jsgfGrammar.getRuleGrammar());
	                if(category.equalsIgnoreCase("romantic"))
	                {
	                addRule(ruleGrammar, "mr1", "A walk in the clouds");
	                addRule(ruleGrammar, "mr2", "Titanic");
	                addRule(ruleGrammar, "mr3", "Sleeping with the enemy");
	                }
	                else if(category.equalsIgnoreCase("action"))
	                {
	                	addRule(ruleGrammar, "ma1", "Speed");
		                addRule(ruleGrammar, "ma2", "terminator");
		                addRule(ruleGrammar, "ma3", "Fast and the Furious");
		                	
	                }else if (category.equalsIgnoreCase("thriller"))
	                {
	                	addRule(ruleGrammar, "mt1", "Black Swan");
		                addRule(ruleGrammar, "mt2", "Cold Fish");
		                addRule(ruleGrammar, "mt3", "From Paris with love");
		            }
	                jsgfGrammar.commitChanges();
	                System.out.println(" ====== Your favourite category ======");
	                System.out.println("Speak one of: \n");
	                jsgfGrammar.dumpRandomSentences(200);
	                System.out.println(" ============================");
	                recognizeAndReport();
	            } catch (Exception e) {
	                throw new IOException(e);
	            }
	        }
	        private  String  recognizeAndReport() throws GrammarException {
	            boolean done = false;
	            String value = null;

	            while (!done)  {
	                Result result = recognizer.recognize();
	                String bestResult = result.getBestFinalResultNoFiller();
	                RuleGrammar ruleGrammar = new BaseRuleGrammar (jsapiRecognizer, jsgfGrammar.getRuleGrammar());
	                RuleParse ruleParse = ruleGrammar.parse(bestResult, null);
	                if (ruleParse != null) {
	                    System.out.println("\n  " + bestResult + '\n');
	                    done = isExit(ruleParse);
	                    value = ruleParse.getTags()[0];
	                } 
	            }
	            return value;
	        }
	        private void addRule(RuleGrammar ruleGrammar, 
	                String ruleName, String jsgf) throws GrammarException {
	        Rule newRule = ruleGrammar.ruleForJSGF(jsgf);
	        ruleGrammar.setRule(ruleName, newRule, true);
	        ruleGrammar.setEnabled(ruleName, true);
	    }
}
