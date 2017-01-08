import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

import javax.swing.JFrame;

public class test {

	public static void main(String[] args) {

		ArrayList<ArrayList<Pair>> C = null;
		ArrayList<Pair> X = null;
		HashMap<ArrayList<Pair>, ArrayList<ArrayList<Pair>>> Eq = null;

		if (args.length == 0) {
			System.out.println("Invalid arguments");
			return;
		} else {
			if (Integer.valueOf(args[0]) == 0) {
				// Cas génération de free pentaminoes sur surface : args[1]
				// donne
				// l'exemple (0, 1 ou 2)

				if (args.length < 1) {
					System.out.println("Invalid arguments");
					return;
				}

				String filename = "fig1";

				if (Integer.valueOf(args[1]) == 1) {
					filename = "fig2";
				} else if (Integer.valueOf(args[1]) == 2) {
					filename = "fig3";
				}

				X = new ArrayList<Pair>();
				int[] ret = fromFile(X, filename);

				ArrayList<short[]> all = RedelmeierFreePolyominoGenerator.generateFreePolyominoes(5);

				C = new ArrayList<ArrayList<Pair>>();

				Eq = new HashMap<ArrayList<Pair>, ArrayList<ArrayList<Pair>>>();

				for (short[] T : all) {

					boolean addedToC = false;

					ArrayList<Pair> U = RedelmeierFreePolyominoGenerator.codage2ListOfPairs(5, T);

					Collections.sort(U);

					ArrayList<Pair> buffer = null;

					ArrayList<ArrayList<Pair>> normEq = RedelmeierFreePolyominoGenerator.classeEquivalence(T).stream()
							.map((short[] l) -> RedelmeierFreePolyominoGenerator.codage2ListOfPairs(5, l))
							.collect(Collectors.toCollection(ArrayList::new));
					normEq.add(U);

					for (ArrayList<Pair> V : normEq) {

						Collections.sort(V);

						for (int i = 0; i < ret[0]; i++) {
							for (int j = 0; j < ret[1]; j++) {
								buffer = new ArrayList<Pair>();

								for (Pair q : V) {

									Pair r = q.translate(i, j);
									if (!X.contains(r)) {
										buffer = null;
										break;
									}

									buffer.add(r);
								}

								if (buffer != null) {

									Collections.sort(buffer);

									if (!addedToC) {
										C.add(buffer);
										Eq.put(buffer, new ArrayList<ArrayList<Pair>>());
										addedToC = true;
										U = buffer;
									} else {
										if (!Eq.get(U).contains(buffer))
											Eq.get(U).add(buffer);
									}

								}
							}
						}

					}

				}

			} else if (Integer.valueOf(args[0]) == 1) {
				// Cas rectangle : args[2] est l'aire des polyominoes, args[1]
				// le
				// type (0 pour fixed, 1 pour free), args[3] et args[4] la
				// taille du
				// rectangle
				if (args.length < 5) {
					System.out.println("Invalid arguments");
					return;
				}

				if (Integer.valueOf(args[1]) != 0) {

					int n = Integer.valueOf(args[3]);
					int m = Integer.valueOf(args[4]);
					int s = Integer.valueOf(args[2]);

					X = new ArrayList<Pair>();

					for (int i = 0; i < n; i++) {
						for (int j = 0; j < m; j++) {
							X.add(new Pair(i, j));
						}
					}

					ArrayList<short[]> all = RedelmeierFreePolyominoGenerator.generateFreePolyominoes(s);

					C = new ArrayList<ArrayList<Pair>>();

					Eq = new HashMap<ArrayList<Pair>, ArrayList<ArrayList<Pair>>>();

					for (short[] T : all) {

						ArrayList<Pair> U = RedelmeierFreePolyominoGenerator.codage2ListOfPairs(s, T);

						Collections.sort(U);

						C.add(U);

						Eq.put(U, new ArrayList<ArrayList<Pair>>());

						ArrayList<Pair> buffer = null;

						ArrayList<ArrayList<Pair>> normEq = RedelmeierFreePolyominoGenerator.classeEquivalence(T)
								.stream().map((short[] l) -> RedelmeierFreePolyominoGenerator.codage2ListOfPairs(s, l))
								.collect(Collectors.toCollection(ArrayList::new));

						normEq.add(U);

						for (ArrayList<Pair> V : normEq) {

							Collections.sort(V);

							for (int i = 0; i < n; i++) {
								for (int j = 0; j < m; j++) {

									if (i == 0 && j == 0 && V.equals(U))
										continue;

									buffer = new ArrayList<Pair>();

									for (Pair q : V) {

										Pair r = q.translate(i, j);
										if (!X.contains(r)) {
											buffer = null;
											break;
										}

										buffer.add(q.translate(i, j));
									}

									if (buffer != null) {
										Collections.sort(buffer);
										if (!Eq.get(U).contains(buffer))
											Eq.get(U).add(buffer);
									}
								}

							}

						}

					}

				} else {

					int n = Integer.valueOf(args[3]);
					int m = Integer.valueOf(args[4]);
					int s = Integer.valueOf(args[2]);

					X = new ArrayList<Pair>();

					for (int i = 0; i < n; i++) {
						for (int j = 0; j < m; j++) {
							X.add(new Pair(i, j));
						}
					}

					C = RedelmeierFixedPolyominoGenerator.generateFixedPolyominoesAsCoord(s);

					Eq = new HashMap<ArrayList<Pair>, ArrayList<ArrayList<Pair>>>();

					for (ArrayList<Pair> T : C) {
						Collections.sort(T);
					}

					for (ArrayList<Pair> T : C) {

						ArrayList<Pair> buffer;

						Eq.put(T, new ArrayList<ArrayList<Pair>>());

						for (int i = 0; i < n; i++) {
							for (int j = 0; j < m; j++) {

								if (i == 0 && j == 0)
									continue;

								buffer = new ArrayList<Pair>();

								for (Pair q : T) {

									Pair r = q.translate(i, j);
									if (!X.contains(r)) {
										buffer = null;
										break;
									}

									buffer.add(q.translate(i, j));
								}

								if (buffer != null) {
									Eq.get(T).add(buffer);
								}
							}
						}

					}

				}

			} else {
				System.out.println("Invalid arguments");
				return;
			}
		}

		/*
		 * TODO : -> Methodes de calcul des classes d'equivalence pour les free
		 * et les one-sided (pour l'instant j'ai fait que fixed). Elles peuvent
		 * s'appliquer à des ColoredPolygon : l'algo est capable de tourner
		 * directement sur ça, meme si c'est pas top en complexité
		 * 
		 * -> Les trucs spécifiques de la question 8 (il faut en gros créer les
		 * polyominos considérés, pas bien long à faire)
		 * 
		 * -> Créer une classe "Triplet" qui gère les points en 3D, et adapter
		 * les algos générateurs de polyominos, pour appliquer l'algo avec E =
		 * Triplet et F = ArrayList<Triplet> (Q9)
		 * 
		 * -> Idem avec les grilles non orthogonales (hexa, penta, etc) (Q10)
		 * 
		 * -> sudoku ? je suis d'avis de pas se faire chier, faire 2 question
		 * sup sur 3 c'est dejà bien. Après on peut aussi choisir de ne pas
		 * faire la 9 ou la 10 et à la place faire la 11, si elle t'inspire
		 */

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

		// Exemple 1 : Remplir le rectangle 6*5 par des polyominos "fixed" de
		// taille 5 */

		/*
		*/

		/*
		 * Exmeple 2 : exact cover avec l'ensemble [| 0,n |] avec toutes les
		 * parties de cet ensemble. Finit en temps inférieur à l'heure pour n <
		 * 14
		 */

		/*
		 * ArrayList<Integer> buffer; ArrayList<Integer> X = new
		 * ArrayList<Integer>(); ArrayList<ArrayList<Integer>> C = new
		 * ArrayList<ArrayList<Integer>>();
		 * 
		 * int n = 8
		 * 
		 * for(int i = 0; i < n; i++){
		 * 
		 * X.add(i);
		 * 
		 * }
		 * 
		 * String bin;
		 * 
		 * for(int n = 1; n < Math.pow(2, n); n++){ buffer = new
		 * ArrayList<Integer>(); bin = Integer.toBinaryString(n); for(int i =
		 * bin.length()-1; i >= 0; i--){ if(bin.charAt(i) == '1'){
		 * buffer.add(bin.length()-i-1); } } C.add(buffer); }
		 * 
		 * Eq = null;
		 */

		long time = System.currentTimeMillis();

		@SuppressWarnings("unchecked")
		exactCover<ArrayList<Pair>, Pair> alg = new exactCover<ArrayList<Pair>, Pair>(X, C, Eq,
				(Class<ArrayList<Pair>>) C.getClass());
		alg.runAlgDL();

		int stop = 0;

		System.out.println("Result size : " + alg.P.size());
		System.out.println("Exec time : " + String.valueOf(System.currentTimeMillis() - time) + "ms");

		/*
		 * for (ArrayList<Pair> T : Eq.get(C.get(11))) { Image2d im = new
		 * Image2d(800, 600);
		 * 
		 * im.addColoredPolygon(new ColoredPolygon(X, ColoredPolygon.rndCol()));
		 * im.addColoredPolygon(new ColoredPolygon(T, ColoredPolygon.rndCol()));
		 * Image2dViewer frame = new Image2dViewer(im);
		 * frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		 * frame.setExtendedState(frame.getExtendedState() |
		 * JFrame.MAXIMIZED_BOTH); frame.setVisible(true); }
		 */

		/*
		 * for(ArrayList<ArrayList<Pair>> T : alg.P) {
		 * 
		 * Image2d im = new Image2d(800, 600);
		 * 
		 * for (ArrayList<Pair> j : T) { im.addColoredPolygon(new
		 * ColoredPolygon(j, ColoredPolygon.rndCol())); }
		 * 
		 * Image2dViewer frame = new Image2dViewer(im);
		 * frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		 * frame.setExtendedState(frame.getExtendedState() |
		 * JFrame.MAXIMIZED_BOTH); frame.setVisible(true);
		 * 
		 * }
		 */

		while (alg.P.size() != 0 && stop < 3) { // System.out.println(i);

			stop++;

			ArrayList<ArrayList<Pair>> i = alg.P.get((int) (Math.random() * alg.P.size()));

			Image2d im = new Image2d(800, 600);

			for (ArrayList<Pair> j : i) {
				im.addColoredPolygon(new ColoredPolygon(j, ColoredPolygon.rndCol()));
			}

			Image2dViewer frame = new Image2dViewer(im);
			frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			frame.setExtendedState(frame.getExtendedState() | JFrame.MAXIMIZED_BOTH);
			frame.setVisible(true);

		}

		/* teste si il y a des erreurs de classes d'equivalence */

		/*
		 * for(ArrayList<ArrayList<Pair>> p : alg.P){ for(ArrayList<Pair> q :
		 * p){ for(ArrayList<Pair> f : p){ if(q == f) continue;
		 * if(alg.H.findClass(q) == alg.H.findClass(f))
		 * System.err.println("Error"); } } }
		 * 
		 */

	}

	public static int[] fromFile(ArrayList<Pair> ret, String filename) {

		try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
			String line;
			int n = Integer.valueOf(br.readLine());
			int m = Integer.valueOf(br.readLine());
			for (int i = 0; i < m; i++) {
				line = br.readLine();
				for (int j = 0; j < n; j++) {

					if (Character.getNumericValue(line.charAt(2 * j)) == 1) {
						ret.add(new Pair(j, i));
					}
				}
			}

			return new int[] { n, m };

		} catch (IOException e) {
			e.printStackTrace();
		}

		return null;
	}
}
