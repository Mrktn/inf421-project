import java.util.*;
public class test {

	public static void main(String[] args) {
		
		Vector<Integer> X = new Vector<Integer>();
		for(int i = 0; i < 14; i++){
			X.add(i);
		}
		
		Vector<Vector<Integer>> C = new Vector<Vector<Integer>>();
		
		Vector<Integer> buffer;
		
		String bin;
		
		for(int n = 1; n < Math.pow(2, 14); n++){
			buffer = new Vector<Integer>();
			bin = Integer.toBinaryString(n);
			for(int i = bin.length()-1; i >= 0; i--){
				if(bin.charAt(i) == '1'){
					buffer.add(bin.length()-i-1);
				}
			}
			C.add(buffer);
		}
		
		/*Vector<Integer> buffer = new Vector<Integer>();
		
		buffer.add(1);
		buffer.addElement(2);
		buffer.add(4);
		
		C.add(buffer);
		
		buffer = new Vector<Integer>();
		
		buffer.add(0);
		buffer.addElement(3);
		buffer.add(5);
		
		C.add(buffer);
		
		buffer = new Vector<Integer>();
		
		buffer.add(0);
		buffer.addElement(4);
		buffer.add(5);
		
		C.add(buffer);
		
		buffer = new Vector<Integer>();
		
		buffer.add(6);
		buffer.addElement(7);
		buffer.add(8);
		
		C.add(buffer);
		
		buffer = new Vector<Integer>();
		
		buffer.add(9);
		buffer.addElement(2);
		buffer.add(7);
		
		C.add(buffer);
		
		buffer = new Vector<Integer>();
		
		buffer.add(9);
		
		C.add(buffer);*/
		
		long time = System.currentTimeMillis();
		exactCover<Integer> alg = new exactCover<Integer>(X,C);
		alg.runAlgDL();

		/*for(Vector<Vector<Integer>> i : alg.P){
			String prt = "";
			for(Vector<Integer> j : i){
				prt += "{";
				for(Integer k : j){
					prt += k;
					prt += ",";
				}
				prt = prt.substring(0, prt.length()-1);
				prt += "}";
			}
			System.out.println(prt);
		}*/
		System.out.println("Result size : " + alg.P.size());
		System.out.println("Exec time : " + String.valueOf(System.currentTimeMillis() - time) );
	}

}
