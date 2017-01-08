import java.util.*;
public class testOld {

	public static void main(String[] args) {
		
		int m = 11;
		
		if(args.length != 0 && Integer.valueOf(args[0]) <= 13){
			m = Integer.valueOf(args[0]);
		}
		
		// Cas donné dans l'énoncé
		
		ArrayList<Integer> xs = new ArrayList<Integer>();
		for(int i = 1; i < 8; i++){
			xs.add(i);
		}
		
		ArrayList<ArrayList<Integer>> cs = new ArrayList<ArrayList<Integer>>();
		
		ArrayList<Integer> buffer = new ArrayList<Integer>();
		
		buffer.add(3);
		buffer.add(5);
		buffer.add(6);
		
		cs.add(buffer);
		
		buffer = new ArrayList<Integer>();
		
		buffer.add(1);
		buffer.add(4);
		buffer.add(7);
		
		cs.add(buffer);
		
		buffer = new ArrayList<Integer>();
		
		buffer.add(3);
		buffer.add(2);
		buffer.add(6);
		
		cs.add(buffer);
		
		buffer = new ArrayList<Integer>();
		
		buffer.add(1);
		buffer.add(4);
		
		cs.add(buffer);
		
		buffer = new ArrayList<Integer>();
		
		buffer.add(2);
		buffer.add(7);
		
		cs.add(buffer);
		
		buffer = new ArrayList<Integer>();
		
		buffer.add(4);
		buffer.add(5);
		buffer.add(7);
		
		cs.add(buffer);
		
		//test de toutes les possibilités de 1 à 10
		
		ArrayList<Integer> X = new ArrayList<Integer>();
		for(int i = 0; i < m; i++){
			X.add(i);
		}
		
		ArrayList<ArrayList<Integer>> C = new ArrayList<ArrayList<Integer>>();
		
		buffer = new ArrayList<Integer>();
		
		String bin;
		
		for(int n = 1; n < Math.pow(2, m); n++){
			buffer = new ArrayList<Integer>();
			bin = Integer.toBinaryString(n);
			for(int i = bin.length()-1; i >= 0; i--){
				if(bin.charAt(i) == '1'){
					buffer.add(bin.length()-i-1);
				}
			}
			C.add(buffer);
		}
		
		//test
		exactCoverOld<Integer> alg = new exactCoverOld<Integer>(xs,cs);
		alg.runAlgNaive();

		for(ArrayList<ArrayList<Integer>> i : alg.P){
			String prt = "For example in task 4 : ";
			for(ArrayList<Integer> j : i){
				prt += "{";
				for(Integer k : j){
					prt += k;
					prt += ",";
				}
				prt = prt.substring(0, prt.length()-1);
				prt += "}";
			}
			System.out.println(prt);
		}
		
		long time = System.currentTimeMillis();
		alg = new exactCoverOld<Integer>(X,C);
		alg.runAlgNaive();
		
		System.out.println("Number of results for all subsets of [0," + m + "[ : " + alg.P.size());
		System.out.println("Exec time : " + String.valueOf(System.currentTimeMillis() - time) );
	}

}
