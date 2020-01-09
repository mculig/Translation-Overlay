package org.translationoverlay.translation;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.translationoverlay.config.Config;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;

import net.ricecode.similarity.LevenshteinDistanceStrategy;
import net.ricecode.similarity.SimilarityStrategy;
import net.ricecode.similarity.StringSimilarityService;
import net.ricecode.similarity.StringSimilarityServiceImpl;

public class CrowdTranslator {
	
	private String language;
	private double minTranslateScore;
	
	private Gson gson = new Gson();
	private TranslationManifest manifest=null;
	private Dictionary dictionary=null;
	
	private String regexSpecialCharacters = "[.!?\\-,\\:]+";
	private String regexMultiSpace = "[ ]+";
	private String placeholderTag = "<ph>";
	
	public CrowdTranslator(Config config) {
		this.language = config.language;
		this.minTranslateScore = config.minTranslateScore;
		
		try {
			//Get all paths in folder
			Stream<Path> walk = Files.walk(Paths.get("./translations"));
			//Get only files from all paths
			List<String> folders = walk.filter(Files::isDirectory)
				.map(path->path.toString()).collect(Collectors.toList());
			walk.close();
			for (String folder: folders){
				JsonReader jsonReader;
				FileReader fileReader;
				//Check if the manifest file is there. If not, print a message and continue loop
				File manifestFile = new File(folder+"/manifest.json");
				if(!manifestFile.exists()) {
					System.out.println("No manifest file found in folder " + folder);
					continue;
				}
				//If manifest is there, open and read it
				fileReader = new FileReader(folder+"/manifest.json");
				jsonReader = new JsonReader(fileReader);
				manifest=gson.fromJson(jsonReader, TranslationManifest.class);
				jsonReader.close();
				fileReader.close();
				//If manifest language doesn't match, continue trucking
				if(!manifest.language.equals(this.language)) {
					System.out.println("Manifest language " + manifest.language + " didn't match language " + this.language + ", continuing search.");
					continue;
				}
				//If manifest language matches, find file mentioned in manifest
				File translationFile = new File(folder+"/"+manifest.filename);
				//If it doesn't exist, break. We found the manifest but no matching file
				if(!translationFile.exists()) {
					System.out.println("No matching translation file found for manifest " + folder + "/manifest.json");
					break;
				}
				//If the file exists, we open and parse it, then exit the loop
				fileReader = new FileReader(folder+"/"+manifest.filename);
				jsonReader = new JsonReader(fileReader);
				dictionary = gson.fromJson(jsonReader, Dictionary.class);
				jsonReader.close();
				fileReader.close();
				break;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	public String translate(String inputString){
		if(dictionary!=null) {
			double maxScore=0.0;
			int maxIndex=0;
			SimilarityStrategy strategy = new LevenshteinDistanceStrategy();
			StringSimilarityService service = new StringSimilarityServiceImpl(strategy);
			for(DictionaryEntry entry : dictionary.translations) {
				double score = service.score(inputString, entry.original);
				if (score>maxScore) {
					maxScore=score;
					maxIndex = dictionary.translations.indexOf(entry);
				}
			}
			if(maxScore<minTranslateScore){
				//System.out.println("Insufficient match score: " + maxScore);
				return inputString;
			}
			else {
				//Get original correct text from the dictionary
				String dictionaryValue = dictionary.translations.get(maxIndex).original;
				//Clean up correct text and match candidate
				dictionaryValue = regexClean(dictionaryValue);
				inputString = regexClean(inputString);
				//Separate them into arrays of words
				String[] dictionaryWords = dictionaryValue.split(" ");
				String[] inputWords = inputString.split(" ");
				
				//For just replacing one placeholder we'll test this first
				String placeholderValue="";
				
				//Ternary puts the shortest string into shortest
				int shortest = dictionaryWords.length < inputWords.length ? dictionaryWords.length : inputWords.length;
				//Run until the end of the shortest. Ideally these will always just match length
				//Naive solution
				for(int i=0;i<shortest;i++)
				{
					if(service.score(inputWords[i], dictionaryWords[i])<0.1) {
						//Where the word doesn't match at all
						if(dictionaryWords[i].equals(placeholderTag)) {
							//If we have a placeholder
							placeholderValue=inputWords[i];
						}
					}
				}
				//Get the translation
				String translation = dictionary.translations.get(maxIndex).translation;
				//Replace the placeholder tag with the actual value from the input string
				translation = translation.replace(placeholderTag, placeholderValue);
				
				//System.out.println("Sufficient match score: " + maxScore);
				return translation;
			}
			
		}
		else {
			return "No translation available due to failed initialization!";
		}
	}
	
	private String regexClean(String text) {
		/* Replace all special characters with spaces
		 * This can lead to multiple spaces so replace all 
		 * sequences of 1 or more spaces with a single space
		 */
		return text.replaceAll(regexSpecialCharacters, " ")
				.replaceAll(regexMultiSpace, " ");
	}

}
