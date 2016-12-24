
abstract class dtNode {
	
	/*Classe abstraite comprennant les elements de base de toute neoud de la structure dancing links. 
	 * Permet de linker entre eux des noeuds de type différent sans se soucier de leur type. 
	 * Pour récupérer des variables propres à un type donné (par exemple size dans le cas des colonnes), 
	 * il faudra néamoins typecaster le type de base en l'une des classes enfant*/
	
	/*Les classes enfant sont génériques, paramétrées par le type E. 
	 * Cela permet de l'appliquer directement à une structure de donnée autre que les entiers (par exmeple les polyomino). 
	 * Il faut néamnoins que la classe E soit comparable. Pour le moment, les ensemble sont de type Vector<E>, 
	 * mais pour l'implementation en polyomino, on peut le changer en un type personnalisé, 
	 * tant qu'il puisse etre parcouru par une boucle for*/
	
	/* Début d'idée pour la question 7 : En plus de ce qui existe pour le moment, 
	 * il faudrait créer un pointeur supplémentaire pour les types dataNode, 
	 * qui pointe vers les elements de sa classe d'équivalence. 
	 * En plus de celà, il faudra une nouvelle methode de couverture qui permet lors de la couverture du x selectionné au début, 
	 * d'enlever de la structure les elements de la classe d'équivalence de x.*/
	
	/*Une autre solution serait d'avoir en parallèle une structure d'union-find contenant les classes d'équivalences. 
	 * Au moment de la selection d'un element x, il faudra parcourir la classe d'équivalence et enlever les noeuds concernés.
	 *  Au moment de la découverture, il faudra aussi la parcourir et replacer ces noeuds. Attention, l'ordre des opérations compte : 
	 *  il faut d'abord commencer par enlever ces elements-là, puis couvrir, et ensuite découvrir puis enfin remettre ces éléments*/
	
	
	dtNode U;
	dtNode D;
	dtNode L;
	dtNode R;
	
	dtNode C;
	
	dtNode(dtNode U, dtNode D, dtNode L, dtNode R, dtNode C){
		this.U = U;
		this.D = D;
		this.L = L;
		this.R = R;
		
		this.C = C;
	}
	
	public void addDNode(dtNode that){}
	
	public void addRNode(dtNode that){}
	
	//public void visualRPZ(int index, int[][] matrix){}

}
