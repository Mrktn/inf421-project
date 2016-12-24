import java.util.*;
public class dataNode<E extends Comparable<E>> extends dtNode{
	
	public Vector<E> set; 
	
	/*En plus du modèle de base, on stocke l'ensemble de base. Permet d'éviter de devoir le recontruire à chaque fois, 
	 * et dans le cadre de notre problème permet d'évier de gaspiller la mémoire car on 
	 * doit de toute façon créer les ensembles au début.
	 */
	
	dataNode(dtNode L, dtNode C, Vector<E> set){
		super(null,null,null,null,C);
		

		this.set = set;
		
		if(C != null){
			C.addDNode(this);
		}
		
		if(L == null){
			this.L = this;
			this.R = this;
			return;
		}
		
		this.L = L;
		this.R = L.R;
		
		L.R.L = this;
		L.R = this;
		
		
		
	}
	
	public void addDNode(dtNode that){
		if(this.C == this.D){
			this.D = that;
			that.U = this;
			
			that.C = this.C;
			
			that.D = this.C;
			this.C.U = that;
			
			return;
		}
		
		this.D.addDNode(that);
		
	}
	
	public void addRNode(dtNode that){
		
		that.L = this;
		that.R = this.R;
		
		this.R.L = that;
		this.R = that;

	}
	
	//Le code suivant permet la visualisation
	
	/*int height;
	
	dataNode(dtNode L, dtNode C, Vector<E> set, int height){
		super(null,null,null,null,C);
		

		this.set = set;
		
		this.height = height;
		
		if(C != null){
			C.addDNode(this);
		}
		
		if(L == null){
			this.L = this;
			this.R = this;
			return;
		}
		
		this.L = L;
		this.R = L.R;
		
		L.R.L = this;
		L.R = this;
		
		
		
	}
	
	public void visualRPZ(int index, int[][] matrix){
		
		matrix[this.height][index] = 1;
		
	}
	
	public String toString(){
		return "Contained in : " + this.C.toString() + " height is : " + this.height + " Contained set is : " + this.set.toString();
	}*/

}
