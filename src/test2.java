
import java.util.*;

public class test2 {

	public static void main(String[] args) {

		ArrayList<ArrayList<Integer>> C = null;
		ArrayList<Integer> X = null;
		HashMap<ArrayList<Integer>, ArrayList<ArrayList<Integer>>> Eq = null;

		/*
		 * 2 algorithmes : runAlgNaive (premiere implémentation) et runAlgDL
		 * (implémentation dancing links). Les deux sont génériques. il convient
		 * d'initialiser l'objet exactCover avec les ensembles X,C et Eq. La
		 * version naive ne supporte pas les classes d'équivalence (non demandé
		 * dans le sujet)
		 * 
		 * 4 éléments : X - la zone à couvrir. N'importe quelle class qui soit
		 * comparable C - l'ensemble des polyominos non équivalents disponibles.
		 * N'importe quelle classe qui extend List<E>, E étant le type de X, et
		 * qui soit clonable Eq - les classes d'equivalence de chaque polyomino.
		 * HashMap<F, ArrayList<F>>, F type de C type - la classe de C. Permet
		 * la duplication (problème de duplication de classes génériques) -
		 * Utile pour le naif
		 * 
		 * Attention, il convient de tirer les ensembles dans C avant l'ajout à
		 * Eq, afin de la clef ne change pas lors d'un tri futur Pour un calcul
		 * avec repetition des polyominos, il convient d'envoyer null à la place
		 * de Eq. Les valeurs de sortie : P : ArrayList<ArrayList<F>> Correspond
		 * à l'ensemble des partitions solution du problème
		 */

		ArrayList<Integer> buffer;
		X = new ArrayList<Integer>();
		C = new ArrayList<ArrayList<Integer>>();

		int m = 8;
		
		if(args.length != 0){
			m = Integer.valueOf(args[0]);
		}

		for (int i = 0; i < m; i++) {

			X.add(i);

		}

		String bin;

		for (int n = 1; n < Math.pow(2, m); n++) {
			buffer = new ArrayList<Integer>();
			bin = Integer.toBinaryString(n);
			for (int i = bin.length() - 1; i >= 0; i--) {
				if (bin.charAt(i) == '1') {

					buffer.add(bin.length() - i - 1);
				}
			}

			C.add(buffer);
		}

		Eq = null;

		long time = System.currentTimeMillis();

		@SuppressWarnings("unchecked")
		exactCover<ArrayList<Integer>, Integer> alg = new exactCover<ArrayList<Integer>, Integer>(X, C, Eq,
				(Class<ArrayList<Integer>>) C.getClass());
		alg.runAlgDL();

		System.out.println("Nombre de partitions de [0," + m + "[ : " + alg.P.size());
		System.out.println("Temps d'execution : " + String.valueOf(System.currentTimeMillis() - time) + "ms");

	}
}
