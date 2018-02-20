import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GenerateTDMMatrix {

	@SuppressWarnings("rawtypes")
	static List<Map> genericList = new ArrayList<Map>();
	static List<String> list = new ArrayList<String>();
	static Map<Integer, String> wordIndex = new HashMap<Integer, String>();
	static Map<String, Integer> wordIndexTemp = new HashMap<String, Integer>();
	static int count = 1;

	public static void main(String[] args) {

		GenerateTDMMatrix matrixObj = new GenerateTDMMatrix();
		matrixObj.generateTDMMatrix();
	}

	private void generateTDMMatrix() {

		BufferedReader br;
		boolean addToTheList = false;
		for (int i = 1; i <= 45; i++) {

			Map<String, Integer> wordFrequency = new HashMap<String, Integer>();
			try {
				br = new BufferedReader(new FileReader(new File(Integer.toString(i))));
				String st = "";
				while ((st = br.readLine()) != null) {
					String[] wordArray = st.trim().split("\\s+");
					for (String str : wordArray) {
						String word = str.trim();
						addToTheList = checkStopWordsAndSpecialCharacters(word);
						if (addToTheList) {
							if (wordFrequency.containsKey(word.toLowerCase())) {
								wordFrequency.put(word.toLowerCase(), wordFrequency.get(word.toLowerCase()) + 1);
							} else {
								wordFrequency.put(word.toLowerCase(), 1);
								if (!wordIndexTemp.containsKey(word.toLowerCase())) {
									wordIndex.put(count, word.toLowerCase());
									wordIndexTemp.put(word.toLowerCase(), count);
									count++;
								}
							}
						}
					}
				}
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			genericList.add(wordFrequency);
		}
		String TDM[][] = new String[genericList.size()][wordIndex.size()];

		for (int i = 0; i < genericList.size(); i++) {
			@SuppressWarnings("unchecked")
			Map<String, Integer> tempMap = genericList.get(i);
			for (Map.Entry<String, Integer> entry : tempMap.entrySet()) {
				TDM[i][wordIndexTemp.get(entry.getKey()) - 1] = Integer.toString(entry.getValue());
			}
		}
		writeTDMToFile(TDM);
		createAndPrintWordFrequenceyTables();
		System.out.println("Output files generated. "
				+ "Filenames: TDMMatrix.csv, "
				+ "WordFrequency_File_1.csv, "
				+ "WordFrequency_File_2.csv, ..., "
				+ "WordFrequency_File_45.csv");
	}

	private void createAndPrintWordFrequenceyTables() {
		try {
			BufferedWriter outputWriter = null;

			for (int i = 0; i < genericList.size(); i++) {
				outputWriter = new BufferedWriter(
						new FileWriter("WordFrequency_File_" + Integer.toString(i + 1) + ".csv"));
				@SuppressWarnings("unchecked")
				Map<String, Integer> tempMap = genericList.get(i);
				outputWriter.write("Word,Frequency");
				outputWriter.write("\n");
				for (Map.Entry<String, Integer> entry : tempMap.entrySet()) {
					outputWriter.write(entry.getKey() + "," + entry.getValue());
					outputWriter.write("\n");
				}
				outputWriter.flush();
				outputWriter.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void writeTDMToFile(String[][] TDM) {
		try {
			BufferedWriter outputWriter = null;
			outputWriter = new BufferedWriter(new FileWriter("TDMatrix.csv"));
			outputWriter.write(" ,");
			for (int i = 1; i <= wordIndex.size(); i++) {
				outputWriter.write(wordIndex.get(i) + ",");
			}
			outputWriter.write("\n");
			for (int i = 0; i < TDM.length; i++) {
				outputWriter.write("File # " + (i + 1) + ",");
				for (int j = 0; j < TDM[i].length; j++) {
					if (null == TDM[i][j]) {
						TDM[i][j] = "0";
						outputWriter.write(0 + ",");
					} else {
						outputWriter.write(TDM[i][j] + ",");
					}
				}
				outputWriter.write("\n");
			}

			outputWriter.flush();
			outputWriter.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private boolean checkStopWordsAndSpecialCharacters(String word) {
		File file = new File("stopWords.txt");
		String stringArr[] = null;
		BufferedReader br;
		boolean flag = true;
		try {
			br = new BufferedReader(new FileReader(file));
			String st = "";
			while ((st = br.readLine()) != null) {
				stringArr = st.split("::");
			}
			List<String> wordList = Arrays.asList(stringArr);
			if (isAlpha(word)) {
				if (wordList.contains(word.toLowerCase())) {
					flag = false;
				}
			} else {
				flag = false;
			}
			br.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return flag;
	}

	public boolean isAlpha(String name) {
		return name.matches("[a-zA-Z]+");
	}
}
