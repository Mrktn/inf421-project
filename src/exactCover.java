import java.util.*;

public class exactCover<E extends Comparable<E>> {
	
	/*Utilisation de vector peut etre remise en question, en fonction des perfs. j'ai eu la flemme pour le moment de réfléchir à ça*/
	
	Vector<E> X; //ensemble de base X
	Vector<Vector<E>> C; //ensemble d'ensembles
	Vector<Vector<Vector<E>>> P; //ensembles des partitions solution
	
	dancingLinks<E> H; //pour le Task 6
	
	exactCover (Vector<E> X, Vector<Vector<E>> C){ //constructeur de base
		this.X = X;
		this.C = C;
		this.P = new Vector<Vector<Vector<E>>>();
		this.H = new dancingLinks<E>(X,C);
	}
	
	exactCover (dancingLinks<E> H){ // permet de construire à partir d'un dancinglinks. Seulement pour Task 6
		this.H = H;
		this.P = new Vector<Vector<Vector<E>>>();
	}
	
	exactCover (Vector<E> X, Vector<Vector<E>> C, boolean copy){ //permet de recopier les ensembles. Nécessaire pour la récurrence de l'algorithme naif
		
		this.X = new Vector<E>();
		this.C = new Vector<Vector<E>>();
		this.P = new Vector<Vector<Vector<E>>>();
		
		for(E i : X){
			this.X.addElement(i);
		}
		
		Vector<E> buffer;
		for(List<E> i : C){
			buffer = new Vector<E>();
			for(E j : i){
				buffer.add(j);
			}
			this.C.add(buffer);
		}
		
		this.H = new dancingLinks<E>(X,C);
		
	}
	
	public void runAlgNaive(){ //algorithme naif
		if(X.isEmpty()){
			P.addElement(new Vector<Vector<E>>());
			return;
		}
		
		//Cas de base. Randomnisé pour rester dans le bain du cours d'INF421
		
		E x = X.get((int) Math.random()*X.size());
		
		//Optimisation 1. Pour n = 10, pas de différence, pour n = 11, gain de temps (2min au lieu de 3)
		
		/*E x = X.firstElement();
		
		int numRef = Integer.MAX_VALUE;
		int numBuf = 0;
		
		for(E i : X){
			for(Vector<E> j : C){
				if(j.contains(i)){
					numBuf++;
				}
			}
			if(numBuf < numRef){
				x = i;
				numRef = numBuf;
			}
		}*/
		
		/*une fois l'élement x choisi, on va le traiter, en prenant un par un les ensembles qui le contiennent. 
		 * Une structure différente pourrait eventuellement permettre de regarder directements les ensembles contenant l'élement, 
		 * mais étant donné que la question suivante permet une optimisation en changant de structure de donnée, 
		 * il me parrassait pas pertienent d'optimiser le cas de base plus que ce qui n'est indiqué*/
		
		for(Vector<E> i : C){ //on
			if(!i.contains(x)){
				continue;
			}
			exactCover<E> nextStep = new exactCover<E>(X,C,true);
			for(E y : i){ //on enlève les ensembles dont l'intersection avec i est non nulle
				nextStep.X.remove(y);
				for(Vector<E> j : C){
					if(!j.contains(y)){
						continue;
					}
					nextStep.C.remove(j);
				}
			}
			
			nextStep.runAlgNaive(); //récurrence
			for(Vector<Vector<E>> p : nextStep.P){ //mise à jour de l'ensemble des solutions
				p.addElement(i);
				this.P.addElement(p); 
			}
			
		}
	}
	
	@SuppressWarnings("unchecked") //évite les avertissements sur le typecasting
	public void runAlgDL(){
		
		if(H.R == H){ 								//Si la structure est vide
			this.P.addElement(new Vector<Vector<E>>()); 	//ajout de l'ensemble vide
			return;	
		}
		
		//Tirage de la valeur x telle que x.size soit minimale
		
		int minSize = Integer.MAX_VALUE;
		
		columnNode<E> x = (columnNode<E>) H.R; //initialisation de x
		
		dtNode buffer = H; //buffer permet de circuler dans la liste des colones
		
		columnNode<E> tCast; //permettra de caster les variables, pour acceder aux varialbes propres à une colone.
		
		dataNode<E> dCast; //permettra (plus tard) de caster des variable en le type simple
		
		for(int i = 0; i < H.size; i++){
			
			buffer = buffer.R;
			
			tCast = (columnNode<E>) buffer;
			
			if(tCast.size < minSize){
				x = tCast;
				minSize = x.size;
			}
		}
		
		
		x.coverColumn(); //couvertude de la colone
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
				
				buffer2 = buffer2.R; //passage à la valeur suivante
			
				tCast = (columnNode<E>) buffer2.C; //récupération de la colone associée à l'element courent
				
				tCast.coverColumn(); //couvertude de ladite colone
				
			}
			
			//maintenant que tout est couvert, on récupère la solution du problème réduit
			
			exactCover<E> nextStep = new exactCover<E>(H);
			nextStep.runAlgDL();
			
			//pour chaque element de la solution réduite, on ajoute l'ensemble courent et on l'ajoute à la fin de la solution complète
			
			//dans chaque element est stocké l'ensemble de base, ce qui permet d'éviter d'avoir à reconstruire l'ensemble et évite le gaspillage de mémoire
			
			for(Vector<Vector<E>> p : nextStep.P){
				dCast = (dataNode<E>) buffer;
				p.add(dCast.set);
				this.P.add(p);
			}
			
			//une fois que ce cas est traité, il faut découvrir les colones qui on servi à la faire : on reprend la boucle inf précédente
			
			buffer2 = buffer; //réinitialisation pour circuler dans l'ensemble. Attention, pour que la découverture ne détruit pas des noeuds existants, il faut le faire dans le sens inverse de la couverture (m'a couté 4h de débuggage)
			
			while(true){ 
				
				if(buffer2.L == buffer){ 
					break;
				}
				
				buffer2 = buffer2.L; 
				
				tCast = (columnNode<E>) buffer2.C;
				
				tCast.uncoverColumn(); //découvertude de ladite colone 
			}
		}
		
		x.uncoverColumn(); //ne pas oublier de découvrir la colone à la fin, sinon on s'en sort pas
	}
	
	
}
