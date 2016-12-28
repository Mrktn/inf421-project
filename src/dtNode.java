
abstract class dtNode {
	
	/*Classe abstraite comprennant les elements de base de toute neoud de la structure dancing links. 
	 * Permet de linker entre eux des noeuds de type différent sans se soucier de leur type. 
	 * Pour récupérer des variables propres à un type donné, 
	 * il faudra néamoins typecaster le type de base en l'une des classes enfant*/
	
	/*Les classes enfant sont génériques, paramétrées par le type E et F.
	 *  
	 * E représente le type de nos items (par exemple, une paire de coordonnées). Elle doit extend Comparable<E>, afin d'effectuer les tris necessaires
	 * pour réduire la compléxité, et tester les cas d'égalité. 
	 * 
	 * F représente des ensembles de points, par exemple un polyomino. Elle doit extend List<E>, afin de pouvoir la parcourir, récupérer de elements, etc...
	 * 
	 * Il peut etre interessant d'extend F à ce qui nous interesse exclusivement (get, iterable) en créant une interface, mais il faut
	 * s'assurer que les types de base du style ArrayList<E> puissent etre admissibles*/
	
	/*Solution retenue pour la question 7 : création de pointeurs sur les lignes en plus que sur les colones, contenant l'ensemble modélisé par la ligne 
	 * (pour éviter de le stocker sur chaque dataNode) et qui en plus implémente une structure de double liste chainée entre les 
	 * elements d'une meme classe d'équivalence. Les opérations de couverture parcourent cette liste et cassent les liens de chaque dataNode
	 * de chaque ligne avec les autres elements de la ligne, à la manière d'une methode coverColumn. Une subtilité néanmoins : pour assurer que
	 * la découverture d'une classe d'équivalence, c'est à dire la remise dans la structure des ensembles d'une meme classe d'équivalence,
	 * ne viennent pas découvrir des lignes couvertes par un coverColumn, un bit 'coveredByCoverColumn' a été ajouté à chaque ligne, pour 
	 * signaler qu'un ligne a déjà été couverte par un coverColumn, afin de ne pas la découvrir. */	
	
	dtNode U;
	dtNode D;
	dtNode L;
	dtNode R;
	
	dtNode C;
	dtNode Ln; //ligne
	
	int size;
	
	dtNode(dtNode U, dtNode D, dtNode L, dtNode R, dtNode C, dtNode Ln){
		this.U = U;
		this.D = D;
		this.L = L;
		this.R = R;
		
		this.C = C;
		this.Ln = Ln;
		this.size = 0;
	}
	
	public void addDNode(dtNode that){}
	
	public void addRNode(dtNode that){}

}
