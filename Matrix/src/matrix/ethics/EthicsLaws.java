package matrix.ethics;

import java.util.HashMap;

public class EthicsLaws{
	
	HashMap<Integer,String> ethicsLaws = new HashMap<Integer, String>();
	
	int idOfLaws = 0;
	
	public void setEthicsLaws(String ethicsLaw) {
		ethicsLaws.put(++idOfLaws,ethicsLaw);
	}
	
	
}