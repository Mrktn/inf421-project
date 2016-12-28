import java.util.List;

public class lineNode<F extends List<E>, E extends Comparable<E>> extends dtNode {

	public F set; //ensemble stocké

	/*taille de la classe d'équivalence. type custom por éviter de devoir enregistrer cette taille un grand nombre de fois en mémoire 
	 * (pertinent pour de grandes tailles)*/
	
	//La colone C est ici le wrapper dancingLinks
	
	public Size eQsize; 	
	public lineNode<F,E> nextEq;
	public lineNode<F,E> prevEq;
	
	public boolean coveredByColumnCover;

	lineNode(F set, dancingLinks<F,E> source) {

		super(null, null, null, null, source, null);
		this.set = set;
		this.size = 0;
		this.eQsize = new Size(1);
		this.Ln = this;
		
		this.L = this;
		this.R = this;
		
		this.nextEq = this;
		this.prevEq = this;
		
		coveredByColumnCover = false;

		source.addDNode(this);

	}
	
	lineNode(F set, dancingLinks<F,E> source, lineNode<F,E> eqClass) {

		super(null, null, null, null, source, null);
		this.set = set;
		this.size = 0;
		this.eQsize = eqClass.eQsize;
		eqClass.eQsize.add(1);
		this.Ln = this;
		
		this.L = this;
		this.R = this;
		
		this.nextEq = eqClass.nextEq;
		this.prevEq = eqClass;
		
		eqClass.nextEq.prevEq = this;
		eqClass.nextEq = this;

		source.addDNode(this);

	}

	public void addRNode(dtNode that) {

		this.size++;

		that.R = this;
		that.L = this.L;

		this.L.R = that;
		this.L = that;

	}

	public void addDNode(dtNode that) {

		that.U = this.U;
		that.D = this;

		this.U.D = that;
		this.U = that;

	}
	
	/*Opération de couverture/découverture de ligne. On retire tous les élements de leurs colonnes respectives*/

	public void coverLine() {
			
		dtNode buffer = this;

		for (int i = 0; i < this.size; i++) {

			buffer = buffer.R;
			buffer.U.D = buffer.D;
			buffer.D.U = buffer.U;
			buffer.C.size--;
		}
		
		this.U.D = this.D;
		this.D.U = this.U;

	}

	public void uncoverLine() {

		dtNode buffer = this;
		
		for (int i = 0; i < this.size; i++) {

			buffer = buffer.R;
				buffer.U.D = buffer;
				buffer.D.U = buffer;
				buffer.C.size++;
		}
		
		this.U.D = this;
		this.D.U = this;

	}

	/*
	 * le procédé qui permet de retirer les polyominos equivalents se fait de la
	 * manière suivante : partant d'un polyomino donné, on va parcourir la liste
	 * de ses equivalents, puis les retirer petit à petit, jusqu'a revenir au
	 * polyomino initial (lui meme étant déjà retiré car l'une de ses colones à
	 * été couverte)
	 */

	/*
	 * le procédé de remise est plus complexe : il faut éviter de remettre dans
	 * la structure des polyominos qui auraient été masqués par une précédente
	 * opération de couverture. Pour ce faire, on se sert de l'invariant suivant
	 * :
	 * "Si un polyomino a été couvert précédement, et ne doit donc pas etre replacé, son bit coveredByColumnCover vaut true". 
	 * Nous allons donc parcourir les équivalents du polyomino de départ, en
	 * n'effectuant aucune opération tant que le bit vaut true, puis une fois que
	 * l'invariant n'est plus vérifié, on va parcourir la liste en relpacant les
	 * polyominos
	 */

	/*
	 * par simplicité, les deux algorithmes utiliseront cet invariant, exploité
	 * à l'aide d'une méthode tierce. Flle retourne un element non masqué si il
	 * existe des éléments de la classe d'equivalence qui sont non masqués, null
	 * sinon. Le paramètre permet de dire si l'on prend le premier vers le bas
	 * ou vers le haut (nextEq ou prevEq)
	 */

	public lineNode<F,E> findVisibleEq(boolean dir){
		
		if(dir){
			lineNode<F,E> buffer = this.nextEq;
			
			while(buffer != this){
				if(!buffer.coveredByColumnCover)
					return buffer;
				buffer = buffer.nextEq;
			}
			return null;
		}
		else{
			lineNode<F,E> buffer = this.prevEq;
			
			while(buffer != this){
				if(!buffer.coveredByColumnCover)
					return buffer;
				buffer = buffer.prevEq;
			}
			return null;
		}
		
	}
	public void coverEq() {

		lineNode<F,E> buffer = this.findVisibleEq(true);
		
		if(buffer == null){
			return;
		}
		
		for (int i = 0; i < buffer.eQsize.s; i++) {
			buffer.coverLine();
			buffer = buffer.nextEq;
		}
	}

	public void uncoverEq(){
		
		lineNode<F,E> buffer = this.findVisibleEq(false);
		
		if(buffer == null){
			return;
		}
		
		for (int i = 0; i < buffer.eQsize.s; i++) {
			buffer.uncoverLine();
			buffer = buffer.prevEq;
		}
		
	}
	
	public String toString(){
		return " size : " + this.size + " elt : " + this.set;
	}

}

/*
 * La class Size permet d'éviter de stocker un grand nombre de fois la meme
 * valeur de eQsize
 */

class Size {

	public int s;

	Size(int s) {
		this.s = s;
	}

	public void add(int a) {
		this.s = this.s + a;
	}

	public void add(Size that) {
		this.s = this.s + that.s;
	}

}
