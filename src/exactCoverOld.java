import java.util.*;

public class exactCoverOld<E extends Comparable<E>> {
	
	/*Utilisation de vector peut etre remise en question, en fonction des perfs. j'ai eu la flemme pour le moment de réfléchir à ça*/
	
	ArrayList<E> X; //ensemble de base X
	ArrayList<ArrayList<E>> C; //ensemble d'ensembles
	ArrayList<ArrayList<ArrayList<E>>> P; //ensembles des partitions solution
	
	//dancingLinks<E> H; //pour le Task 6
	
	exactCoverOld (ArrayList<E> X, ArrayList<ArrayList<E>> C){ //constructeur de base
		this.X = X;
		this.C = C;
		this.P = new ArrayList<ArrayList<ArrayList<E>>>();
		//this.H = new dancingLinks<E>(X,C);
	}
	
	/*exactCoverOld (dancingLinks<E> H){ // permet de construire à partir d'un dancinglinks. Seulement pour Task 6
		this.H = H;
		this.P = new ArrayList<ArrayList<ArrayList<E>>>();
	}*/
	
	exactCoverOld (ArrayList<E> X, ArrayList<ArrayList<E>> C, boolean copy){ //permet de recopier les ensembles. Nécessaire pour la récurrence de l'algorithme naif
		
		this.X = new ArrayList<E>();
		this.C = new ArrayList<ArrayList<E>>();
		this.P = new ArrayList<ArrayList<ArrayList<E>>>();
		
		for(E i : X){
			this.X.add(i);
		}
		
		ArrayList<E> buffer;
		for(List<E> i : C){
			buffer = new ArrayList<E>();
			for(E j : i){
				buffer.add(j);
			}
			this.C.add(buffer);
		}
		
		//this.H = new dancingLinks<E>(X,C);
		
	}
	
	public void runAlgNaive(){ //algorithme naif
		if(X.isEmpty()){
			P.add(new ArrayList<ArrayList<E>>());
			return;
		}
		
		//Cas de base. Randomnisé pour rester dans le bain du cours d'INF421
		
		//E x = X.get((int) Math.random()*X.size());
		
		//Optimisation 1. Pour n = 10, pas de différence, pour n = 11, gain de temps (2min au lieu de 3)
		
		E x = X.get(0);
		
		int numRef = Integer.MAX_VALUE;
		int numBuf = 0;
		
		for(E i : X){
			for(ArrayList<E> j : C){
				if(j.contains(i)){
					numBuf++;
				}
			}
			if(numBuf < numRef){
				x = i;
				numRef = numBuf;
			}
		}
		
		/*une fois l'élement x choisi, on va le traiter, en prenant un par un les ensembles qui le contiennent. 
		 * Une structure différente pourrait eventuellement permettre de regarder directements les ensembles contenant l'élement, 
		 * mais étant donné que la question suivante permet une optimisation en changant de structure de donnée, 
		 * il me parrassait pas pertienent d'optimiser le cas de base plus que ce qui n'est indiqué*/
		
		for(ArrayList<E> i : C){ //on
			if(!i.contains(x)){
				continue;
			}
			exactCoverOld<E> nextStep = new exactCoverOld<E>(X,C,true);
			for(E y : i){ //on enlève les ensembles dont l'intersection avec i est non nulle
				nextStep.X.remove(y);
				for(ArrayList<E> j : C){
					if(!j.contains(y)){
						continue;
					}
					nextStep.C.remove(j);
				}
			}
			
			nextStep.runAlgNaive(); //récurrence
			for(ArrayList<ArrayList<E>> p : nextStep.P){ //mise à jour de l'ensemble des solutions
				p.add(i);
				this.P.add(p); 
			}
			
		}
	}
	/*
	@SuppressWarnings("unchecked") //évite les avertissements sur le typecasting
	public void runAlgDL(){
		
		if(H.R == H){ 								//Si la structure est vide
			this.P.add(new ArrayList<ArrayList<E>>()); 	//ajout de l'ensemble vide
			return;	
		}
		
		//Tirage de la valeur x telle que x.size soit minimale
		
		int minSize = Integer.MAX_VALUE;
		
		columnNode<E> x = (columnNode<E>) H.R; //initialisation de x
		
		dtNode buffer = H; //buffer permet de circuler dans la liste des colones
		
		columnNode<E> tCast; //permettra de caster les variables, pour acceder aux varialbes propres à une colone.
		
		lineNode<E> eCast; //idem pour une ligne
		
		for(int i = 0; i < H.size; i++){
			
			buffer = buffer.R;
			
			if(buffer.size < minSize){
				x = (columnNode<E>) buffer;
				minSize = x.size;
			}
		}
		
		
		x.coverColumn(); //couvertude de la colonne
		
		//boucle sur les ensembles contenant x
		
		buffer = x; //réutilisation de buffer
		
		dtNode buffer2 = null; //second buffer spécifique à la circulation sur un ensemble particulier
		
		for(int i = 0; i < x.size; i++){
			
			buffer = buffer.D; //passage au prochain ensemble
			
			//je n'ai pas mi de size sur les elements simples, meme si ça aurait permi de circuler plus simplement sur un set, car cela prendrait n*2**n mémoire supplémentaire

			buffer2 = buffer; //initialisation pour circuler dans l'ensemble
			
			while(true){ //boucle infinie, pour gérer la condition d'arret plus précisément au sein du code. Permet de couvrir les autres éléments de l'ensemble considéré.
				
				if(buffer2.R == buffer){ //si la valeur suivante revient au début, on a fini
					break;
				}
				if(buffer2.R.C == H){
					if(buffer2.R.R == buffer){
						break;
					}
					buffer2 = buffer2.R;
				}
				
				buffer2 = buffer2.R; //passage à la valeur suivante
			
				tCast = (columnNode<E>) buffer2.C; //récupération de la colone associée à l'element courent
				
				tCast.coverColumn(); //couvertude de ladite colone
				
			}
			
			//maintenant que tout est couvert, on récupère la solution du problème réduit
			
			exactCoverOld<E> nextStep = new exactCoverOld<E>(H);
			nextStep.runAlgDL();
			
			//pour chaque element de la solution réduite, on ajoute l'ensemble courent et on l'ajoute à la fin de la solution complète
			
			//dans chaque element est stocké l'ensemble de base, ce qui permet d'éviter d'avoir à reconstruire l'ensemble et évite le gaspillage de mémoire
			
			for(ArrayList<ArrayList<E>> p : nextStep.P){
				eCast = (lineNode<E>) buffer.Ln;
				p.add(eCast.set);
				this.P.add(p);
			}
			
			//une fois que ce cas est traité, il faut découvrir les colones qui on servi à la faire : on reprend la boucle inf précédente
			
			buffer2 = buffer; //réinitialisation pour circuler dans l'ensemble. Attention, pour que la découverture ne détruit pas des noeuds existants, il faut le faire dans le sens inverse de la couverture (m'a couté 4h de débuggage)
			
			while(true){ 
				
				if(buffer2.L == buffer){ 
					break;
				}
				if(buffer2.L.C == H){
					if(buffer2.L.L == buffer){
						break;
					}
					buffer2 = buffer2.L;
				}
				
				buffer2 = buffer2.L; 
				
				tCast = (columnNode<E>) buffer2.C;
				
				tCast.uncoverColumn(); //découvertude de ladite colone 
			}
		}
		
		x.uncoverColumn(); //ne pas oublier de découvrir la colone à la fin, sinon on s'en sort pas
	}
	*/
	
}
