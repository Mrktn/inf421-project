import java.util.*;
import javax.swing.JFrame;

public class test {

	public static void main(String[] args) {
		
		/* TODO :
		 * 			-> Methodes de calcul des classes d'equivalence pour les free et les one-sided (pour l'instant j'ai fait que fixed).
		 * Elles peuvent s'appliquer à des ColoredPolygon : l'algo est capable de tourner directement sur ça, meme si c'est pas top en
		 * complexité
		 * 
		 * 			-> Les trucs spécifiques de la question 8 (il faut en gros créer les polyominos considérés, pas bien long à faire)
		 * 
		 * 			-> Créer une classe "Triplet" qui gère les points en 3D, et adapter les algos générateurs de polyominos, pour 
		 * appliquer l'algo avec E = Triplet et F = ArrayList<Triplet> (Q9)
		 * 
		 * 			-> Idem avec les grilles non orthogonales (hexa, penta, etc) (Q10)
		 * 
		 * 			-> sudoku ? je suis d'avis de pas se faire chier, faire 2 question sup sur 3 c'est dejà bien. Après on peut
		 * aussi choisir de ne pas faire la 9 ou la 10 et à la place faire la 11, si elle t'inspire
		 */
		
		/* 2 algorithmes : runAlgNaive (premiere implémentation) et runAlgDL (implémentation dancing links). Les deux sont génériques. il convient
		 * d'initialiser l'objet exactCover avec les ensembles X,C et Eq. La version naive ne supporte pas les classes d'équivalence (non demandé dans le sujet)
		 * 
		 * 4 éléments : X - la zone à couvrir. N'importe quelle class qui soit comparable
		 * 				C - l'ensemble des polyominos non équivalents disponibles. N'importe quelle classe qui extend List<E>, E étant le type de X, et qui soit clonable
		 * 				Eq - les classes d'equivalence de chaque polyomino. HashMap<F, ArrayList<F>>, F type de C
		 * 				type - la classe de C. Permet la duplication (problème de duplication de classes génériques) - Utile pour le naif
		 * 
		 * Attention, il convient de tirer les ensembles dans C avant l'ajout à Eq, afin de la clef ne change pas lors d'un tri futur
		 * Pour un calcul avec repetition des polyominos, il convient d'envoyer null à la place de Eq. Les valeurs de sortie :
		 * 				P : ArrayList<ArrayList<F>> Correspond à l'ensemble des partitions solution du problème*/
		
		/* Exemple 1 : Remplir le rectangle 6*5 par des polyominos "fixed" de taille 5 */
		
		ArrayList<Pair> X = new ArrayList<Pair>();	
		
		for(int i = 0; i < 6; i++){
			for(int j = 0; j < 5; j++){
				X.add(new Pair(i,j));
			}
		}
				
		ArrayList<ArrayList<Pair>> C = RedelmeierFixedPolyominoGenerator.generateFixedPolyominoesAsCoord(5);
		
		HashMap<ArrayList<Pair>, ArrayList<ArrayList<Pair>>> Eq = new HashMap<ArrayList<Pair>, ArrayList<ArrayList<Pair>>>();
				
		for(ArrayList<Pair> T : C){
			Collections.sort(T);
		}
		
		for(ArrayList<Pair> T : C){
			
			ArrayList<Pair> buffer;
			
			Eq.put(T, new ArrayList<ArrayList<Pair>>());
			
			for(int i = 0; i < 6; i++){
				for(int j = 0; j < 5; j++){
					
					if(i == 0 && j == 0)
						continue;
					
					buffer = new ArrayList<Pair>();
					
					for(Pair q : T){
						
						Pair r = q.translate(i,j);
						if(!X.contains(r)){
							buffer = null;
							break;
						}
							
						buffer.add(q.translate(i,j));
					}
					
					if(buffer != null){
						Eq.get(T).add(buffer);
					}
				}
			}
			
		}
		
		/*Exmeple 2 : exact cover avec l'ensemble [| 0,n |] avec toutes les parties de cet ensemble. Finit en temps inférieur à l'heure pour n < 14 */
		
		/*
		ArrayList<Integer> buffer;
		ArrayList<Integer> X = new ArrayList<Integer>();
		ArrayList<ArrayList<Integer>> C = new ArrayList<ArrayList<Integer>>();
		
		int n = 8
		
		for(int i = 0; i < n; i++){
		
			X.add(i);
		
		}
		
		String bin;
		
		for(int n = 1; n < Math.pow(2, n); n++){
			buffer = new ArrayList<Integer>();
			bin = Integer.toBinaryString(n);
			for(int i = bin.length()-1; i >= 0; i--){
				if(bin.charAt(i) == '1'){
					buffer.add(bin.length()-i-1);
				}
			}
			C.add(buffer);
		}
		
		Eq = null;
		*/

		long time = System.currentTimeMillis();
		
		@SuppressWarnings("unchecked")
		exactCover<ArrayList<Pair>, Pair> alg = new exactCover<ArrayList<Pair>, Pair>(X,C,Eq, (Class<ArrayList<Pair>>) C.getClass());
		alg.runAlgDL();

		/*for(ArrayList<ArrayList<Pair>> i : alg.P){
			String prt = "";
			for(ArrayList<Pair> j : i){
				prt += "{";
				for(Pair k : j){
					prt += k;
					prt += ",";
				}
				prt = prt.substring(0, prt.length()-1);
				prt += "}";
			}
			System.out.println(prt);
		}*/
		
		//System.out.println(C2.size());
		
		/*Image2d im = new Image2d(800, 600);
		
		for(ArrayList<Pair> j : C2){
			im.addColoredPolygon(new ColoredPolygon(j,ColoredPolygon.rndCol()));
		}
		Image2dViewer frame = new Image2dViewer(im);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setExtendedState(frame.getExtendedState() | JFrame.MAXIMIZED_BOTH);
        frame.setVisible(true);*/
		
		/*for(ArrayList<ArrayList<Pair>> i : alg.P){
			//System.out.println(i);
			
			Image2d im = new Image2d(800, 600);
			
			for(ArrayList<Pair> j : i){
				im.addColoredPolygon(new ColoredPolygon(j,ColoredPolygon.rndCol()));
			}
			
			Image2dViewer frame = new Image2dViewer(im);
	        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	        frame.setExtendedState(frame.getExtendedState() | JFrame.MAXIMIZED_BOTH);
	        frame.setVisible(true);
			
		}*/
		
		/* teste si il y a des erreurs de classes d'equivalence */
		
		/*for(ArrayList<ArrayList<Pair>> p : alg.P){
			for(ArrayList<Pair> q : p){
				for(ArrayList<Pair> f : p){
					if(q == f)
						continue;
					if(alg.H.findClass(q) == alg.H.findClass(f))
						System.err.println("Error");
				}
			}
		}
		*/
		System.out.println("Result size : " + alg.P.size());
		System.out.println("Exec time : " + String.valueOf(System.currentTimeMillis() - time) );
		
		
		
	}

}
