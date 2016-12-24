
public class columnNode<E extends Comparable<E>> extends dtNode {
	
	public int size; //nombre d'ensembles contenant l'élement "item"
	public E item; 
	public dancingLinks<E> source; //pointeur vers la source de la structure : permet de mettre à jour ses valeurs
	
	columnNode(E item, dancingLinks<E> source){
		
		super(null,null,null,null,null);
		this.item = item;
		this.size = 0;
		this.C = this;
		this.D = this;
		this.U = this;
		
		this.source = source;
		
	}
	
	columnNode(dtNode L, E item, dancingLinks<E> source){
		
		super(null,null,L,null,null);
		this.item = item;
		this.size = 0;
		this.C = this;
		
		this.D = this;
		this.U = this;
		
		this.source = source;
		
		if(L != null){
			this.R = L.R;
			L.R.L = this;
			L.R = this;
		}
		
	}
	
	public void addDNode(dtNode that){ //ajout d'un ensemble à la liste des ensembles contenant l'élement item
		
		this.size++;
		
		if(this.U == this){
			this.D = that;
			that.D = this;
			that.U = this;
			that.C = this;
			this.U = that;
			return;
		}
		this.D.addDNode(that);
		
	}
	
	public void addRNode(dtNode that){ //ajout d'une nouvelle colone
		
		if(this.R.C == null){
			
			that.L = this;
			that.R = this.R;
			
			this.R.L = that;
			this.R = that;
			
			return;
			
		}
		
		this.R.addRNode(that);
	}
	
	@SuppressWarnings("unchecked")
	public void coverColumn(){ //couvrir colone (voir énoncé)
		
		
		this.source.size--;
		this.R.L = this.L;
		this.L.R = this.R;
		
		dtNode buffer = this;
		dtNode buffer2 = null;
		
		columnNode<E> tCast = null;
		
		for(int i = 0; i < this.size; i++){
			
			buffer = buffer.D;
			
			buffer2 = buffer;
			
			while(true){ 
				
				if(buffer2.R == buffer){ 
					break;
				}
				
				buffer2 = buffer2.R; 
				
				tCast = (columnNode<E>) buffer2.C;
				
				tCast.size--;
				
			
				buffer2.D.U = buffer2.U;
				buffer2.U.D = buffer2.D;
			}
			
		}
		
	}
	
	@SuppressWarnings("unchecked")
	public void uncoverColumn(){ //découvrir colone
		
		this.source.size++;
		this.R.L = this;
		this.L.R = this;
		
		dtNode buffer = this;
		dtNode buffer2 = null;
		
		columnNode<E> tCast = null;
		
		for(int i = 0; i < this.size; i++){
			
			buffer = buffer.D;
			
			buffer2 = buffer;
			
			while(true){ 
				
				if(buffer2.R == buffer){ 
					break;
				}
				
				buffer2 = buffer2.R; 
				
				tCast = (columnNode<E>) buffer2.C;
				
				tCast.size++;
			
				buffer2.D.U = buffer2;
				buffer2.U.D = buffer2;
			}
			
		}
		
	}
	//Le code suivant permet la visualisation
	
	/*
	public String toString(){
		
		return "Column " + this.item + " with size " + this.size;
	}
	
	public void visualRPZ(int index, int[][] matrix){
		
		dtNode buffer = this;
		
		for(int i = 0; i < this.size; i++){
			buffer = buffer.D;
			buffer.visualRPZ(index, matrix);
		}
	}*/
}
