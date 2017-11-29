package server;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class WordData implements Serializable {
	ArrayList<String> word = new ArrayList<String>();
	ArrayList<String> mean = new ArrayList<String>();
	
	public WordData(ArrayList<String> word, ArrayList<String> mean) {
		this.word = word;
		this.mean = mean;
	}
	
	public ArrayList<String> getWordData() {
		return word;
	}
	
	public ArrayList<String> getMeanData() {
		return mean;
	}
}
