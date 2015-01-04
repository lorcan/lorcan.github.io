import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.util.Hashtable;
import java.util.List;

import org.xml.sax.InputSource;

import cbml.CBMLReader;
import cbml.CaseReader;
import cbml.cbr.CaseStruct;
import cbml.cbr.SimilarityProfile;
import fionn.Case;
import fionn.cbr.nn.Casenode;
import fionn.cbr.nn.NN;

/**
 * @author Lorcan Coyle
 */
public class GetBestKCases {

	public static void main(String[] args) {
		try {
			if (args.length != 5) {
				System.out.println("This script works as follows: ");
				System.out.println("java GetBestKCases structure_file similarity_profile_file casebase_file target_case_file k ");
				System.out.println("e.g. java GetBestKCases c:/iris/structure.xml c:/iris/similarity.xml c:/iris/casebase.xml c:/iris/target.xml 5 ");
				return;
			}
			new GetBestKCases(args[0], args[1], args[2], args[3], Integer.parseInt(args[4]));

		} catch (Exception e) {
			System.out.println("This script works as follows: ");
			System.out.println("java GetBestKCases structure_file casebase_file target_case_file k ");
			System.out.println("e.g. java GetBestKCases structure.xml casebase.xml target.xml 5 ");
			e.printStackTrace();
		}
	}

	private List casebase;
	private CaseStruct caseStruct;
	private SimilarityProfile profile;
	private Case target;
	private String username = "default";
	
	public GetBestKCases(String caseStructFileName, String similarityProfileFileName, String casebaseFileName, String targetFileName, int k) throws Exception {
		try {
			
			// sets up the case structure object, the similarity profile object, the casebase and the target cases
			initialiseVariables(caseStructFileName, similarityProfileFileName, casebaseFileName, targetFileName);
			
			// initialises the NN 
			NN nn = new NN(casebase, caseStruct, profile);

			// presents the target case to the NN algorithm
			nn.presentTarget((Casenode) target);

			// gets a list of the k most similar cases 
			List bestCases = nn.getBestKCases(k);

			// prints out the target case
			System.out.println("Target case ->" + target.toString());

			// k is changed here in case there were fewer than k cases in the casebase
			int casebaseSize = bestCases.size();
			if (k > casebaseSize)
				k = casebaseSize;

			// prints out the k most similar cases form the casebase
			System.out.println("top " + k + " cases ->");
			for (int i = 0; i < k; i++) {
				Casenode current = (Casenode) bestCases.get(i);
				if (current != null) {
					double score = current.getActivation();
					System.out.print("Score=" + score);
					System.out.println(current.toString());
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void initialiseVariables(String caseStructFileName, String similarityProfileFileName, String casebaseFileName, String targetFileName) throws Exception {
		
		// checks that the files exist
		if (!(new File(caseStructFileName)).exists())
			throw new Exception("Error - Case Structure File " + caseStructFileName + " doesn't exist");
		if (!(new File(similarityProfileFileName)).exists())
			throw new Exception("Error - Similarity Profile File " + similarityProfileFileName + " doesn't exist");
		if (!(new File(casebaseFileName)).exists())
			throw new Exception("Error - Case-base File " + casebaseFileName + " doesn't exist");
		if (!(new File(targetFileName)).exists())
			throw new Exception("Error - Target Case File " + targetFileName + " doesn't exist");

		// this is the case class. Any class that implements CBMLCase is suitable
		Class caseClass = Class.forName("fionn.cbr.nn.Casenode");
		
		// cbmlReader reads the case structure document and the similarity profile document		
		CBMLReader cbmlReader = new CBMLReader();
		InputSource source = new InputSource(new BufferedInputStream(new FileInputStream(caseStructFileName)));

		// Loads Case Structure
		if (cbmlReader.readCBMLDocument(source)) {
			// gets case structure from the reader
			caseStruct = cbmlReader.getCaseStruct();
			//System.out.println("Case Structure      " + caseStruct.toString());		
			{
				// Loads case-base
				CaseReader caseReader = new CaseReader(caseClass, caseStruct, new Boolean(true));
				source = new InputSource(new BufferedInputStream(new FileInputStream(casebaseFileName)));
				casebase = caseReader.readCasebase(source);
			}
			{
				// Loads target case
				CaseReader caseReader = new CaseReader(caseClass, caseStruct, new Boolean(false));
				source = new InputSource(new BufferedInputStream(new FileInputStream(targetFileName)));
				target = (Case) caseReader.readSkeletalCase(source);
			}
			source = new InputSource(new BufferedInputStream(new FileInputStream(similarityProfileFileName)));
			// Loads similarity profile
			if (cbmlReader.readCBMLDocument(source)) {
				
				// gets the similarity profile for the user username. If this does not exist it tries to load the default profile
				Hashtable profiles = cbmlReader.getSimilarities();
				profile = (SimilarityProfile) profiles.get(username);
				if (profile == null)
					profile = (SimilarityProfile) profiles.get("default");
				//System.out.println("Similarity Profile  " + profile.toString());

			} else {
				throw new Exception("unable to load similarity profile document");
			}
		} else {
			throw new Exception("unable to load case structure document");
		}
	}
}
