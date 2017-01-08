import java.util.*;

public class exactCover<F extends List<E>, E extends Comparable<E>> {
	
	F X;
	ArrayList<F> C;
	ArrayList<ArrayList<F>> P;
	
	dancingLinks<F,E> H;	
	
	Class<F> type;
	
	exactCover (F X, ArrayList<F> C, HashMap<F,ArrayList<F>> Eq, Class<F> type){
		this.X = X;
		this.C = C;
		this.P = new ArrayList<ArrayList<F>>();
		this.H = new dancingLinks<F,E>(X,C,Eq);
		this.type = type;
	}
	
	exactCover (dancingLinks<F,E> H){
		this.H = H;
		this.P = new ArrayList<ArrayList<F>>();
	}
	
	exactCover (F X, ArrayList<F> C, HashMap<F,ArrayList<F>> Eq, boolean copy, Class<F> type) throws InstantiationException, IllegalAccessException{ //permet de recopier les ensembles. Nécessaire pour la récurrence de l'algorithme naif
		
		try {
			this.X = type.newInstance();
		} catch (InstantiationException e1) {
			e1.printStackTrace();
		} catch (IllegalAccessException e1) {
			e1.printStackTrace();
		}
		this.C = new ArrayList<F>();
		this.P = new ArrayList<ArrayList<F>>();
		
		for(E i : X){
			this.X.add(i);
		}
		
		F buffer = null;
		for(List<E> i : C){
			buffer = type.newInstance();
			for(E j : i){
				buffer.add(j);
			}
			this.C.add(buffer);
		}
		
		this.H = new dancingLinks<F,E>(X,C,Eq);
		
	}
	
	public void runAlgNaive() throws InstantiationException, IllegalAccessException{
		if(X.isEmpty()){
			P.add(new ArrayList<F>());
			return;
		}
		
		//Cas de base. Randomnisé pour rester dans le bain du cours d'INF421
		
		//E x = X.get((int) Math.random()*X.size());
		
		//Optimisation 1. Pour n = 10, pas de différence, pour n = 11, gain de temps (2min au lieu de 3)
		
		E x = X.get(0);
		
		int numRef = Integer.MAX_VALUE;
		int numBuf = 0;
		
		for(E i : X){
			for(F j : C){
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
		
		for(F i : C){ //on
			if(!i.contains(x)){
				continue;
			}
			exactCover<F,E> nextStep = new exactCover<F,E>(X , C, null, true, this.type);
			for(E y : i){ //on enlève les ensembles dont l'intersection avec i est non nulle
				nextStep.X.remove(y);
				for(F j : C){
					if(!j.contains(y)){
						continue;
					}
					nextStep.C.remove(j);
				}
			}
			
			nextStep.runAlgNaive(); //récurrence
			for(ArrayList<F> p : nextStep.P){ //mise à jour de l'ensemble des solutions
				p.add(i);
				this.P.add(p); 
			}
			
		}
	}
	
	@SuppressWarnings("unchecked") //évite les avertissements sur le typecasting
	public void runAlgDL(){
		
		//System.out.println(H);
		
		if(H.R == H){ 								//Si la structure est vide
			this.P.add(new ArrayList<F>()); 	//ajout de l'ensemble vide
			return;	
		}
		
		//Tirage de la valeur x telle que x.size soit minimale
		
		int minSize = Integer.MAX_VALUE;
		
		columnNode<F,E> x = (columnNode<F,E>) H.R; //initialisation de x
		
		dtNode buffer = H; //buffer permet de circuler dans la liste des colones
		
		columnNode<F,E> tCast; //permettra de caster les variables, pour acceder aux varialbes propres à une colone.
		
		lineNode<F,E> eCast; //idem pour une ligne
		
		for(int i = 0; i < H.size; i++){
			
			buffer = buffer.R;
			
			if(buffer.size < minSize){
				x = (columnNode<F,E>) buffer;
				minSize = x.size;
			}
		}
		
		x.coverColumn();
		
		//System.out.println("After covering first column : " + H);
		
		/*boucle sur les ensembles contenant x, pour traiter les cas de manière dynamique*/
		
		buffer = x; 
		
		dtNode buffer2 = null; 
		
		for(int i = 0; i < x.size; i++){
			
			buffer = buffer.D;
			
			//eCast = (lineNode<F,E>) buffer.Ln;
			
			//System.out.println("Chose set : " + eCast.set);
			
			/*je n'ai pas mi de size sur les elements simples, meme si ça aurait permi de circuler plus simplement sur un set, 
			 * car cela prendrait n*2**n mémoire supplémentaire. Le procédé est le suivant : on prend chaque element de l'ensemble
			 * selecitionné, et on retire sa colone des possibles pour qu'il ne reste plus que les polyominos qui ne sont pas en
			 * intersection avec lui. On va ensuite retirer les elements de sa classe d'équivalence, puis on récurse*/

			eCast = (lineNode<F,E>) buffer.Ln;
			
			//System.out.println(eCast);
			eCast.coverEq();
			
			//System.out.println("Covered Eq");
			
			buffer2 = buffer.Ln; 
			
			for(int j = 0; j < buffer.Ln.size; j++){
				
				buffer2 = buffer2.R;
				
				if(buffer2 == buffer)
					continue;
				
				tCast = (columnNode<F,E>) buffer2.C; 
				
				tCast.coverColumn(); 
				
			}
			
			//System.out.println("Covered Column");
			
			exactCover<F,E> nextStep = new exactCover<F,E>(H);
			nextStep.runAlgDL();
			
			//System.out.println("Back");
			
			for(ArrayList<F> p : nextStep.P){
				eCast = (lineNode<F,E>) buffer.Ln;
				p.add(eCast.set);
				this.P.add(p);
			}
			
			buffer2 = buffer.Ln;  
			
			/*Attention, pour que la découverture ne détruit pas des noeuds existants, 
			 * il faut le faire dans le sens inverse de la couverture (m'a couté 4h de débuggage)*/
			
			for(int j = 0; j < buffer2.Ln.size; j++){
				
				
				buffer2 = buffer2.L;
				
				if(buffer2 == buffer)
					continue;
				
				tCast = (columnNode<F,E>) buffer2.C; 
				tCast.uncoverColumn();
			}
			
			eCast = (lineNode<F,E>) buffer.Ln;
			eCast.uncoverEq();
			
		}
		
		x.uncoverColumn(); //ne pas oublier de découvrir la colone à la fin, sinon on s'en sort pas
		
	}
	
	
	
}
