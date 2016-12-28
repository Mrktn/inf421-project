import java.util.List;
public class columnNode<F extends List<E>, E extends Comparable<E>> extends dtNode {
	
	// size  : nombre d'ensembles contenant l'élement "item", càs nombre d'elements dans la colone
	public E item; 
	
	//ici, Ln sera une réfence vers le wrapper dancingLinks
	
	columnNode(E item, dancingLinks<F,E> source){
		
		super(null,null,null,null,null,source);
		this.item = item;
		this.size = 0;
		this.C = this;
		this.D = this;
		this.U = this;
		
	}
	
	columnNode(dtNode L, E item, dancingLinks<F,E> source){
		
		super(null,null,L,null,null,source);
		this.item = item;
		this.size = 0;
		this.C = this;
		
		this.D = this;
		this.U = this;
		
		if(L != null){
			this.R = L.R;
			L.R.L = this;
			L.R = this;
		}
		
	}
	
	public void addDNode(dtNode that){ //ajout d'un ensemble à la liste des ensembles contenant l'élement item
		
		this.size++;
		
		that.C = this;
		
		that.U = this.U;
		that.D = this;
		this.U.D = that;
		this.U = that;
		
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
	public void coverColumn(){ //couvrir colone
		
		
		if(this.R.L != this)
			return;
		
		/*On retire la colone de la liste chainée*/
		
		this.Ln.size--;
		this.R.L = this.L;
		this.L.R = this.R;
		
		dtNode buffer = this;
		dtNode buffer2 = null;
		
		lineNode<F,E> tCast;
		
		/*Pour chaque elements dans la colone, on va retirer le restant des elements de sa lignes de leurs colones correspondantes, 
		 * afin que l'ensemble ne puisse plus etre considéré. On va ensuite prendre la ligne de cet élément, et la retirer de sa classe d'équivalence en 
		 * changeant son bit "covered" pour par que l'opération de couverture de ligne ne perturbe l'opération */
		
		for(int i = 0; i < this.size; i++){
			
			buffer = buffer.D;
			
			tCast = (lineNode<F,E>) buffer.Ln;
			
			buffer2 = buffer.Ln;
			
			for(int j = 0; j < buffer.Ln.size; j++){
				
				buffer2 = buffer2.R;
				if(buffer2.C == this)
					continue;
				else{
				buffer2.C.size--;
				buffer2.D.U = buffer2.U;
				buffer2.U.D = buffer2.D;
				}
			}
			
			
			tCast = (lineNode<F,E>) buffer.Ln;
			tCast.U.D = tCast.D;
			tCast.D.U = tCast.U;
			
			tCast.nextEq.prevEq = tCast.prevEq;
			tCast.prevEq.nextEq = tCast.nextEq;
			if(tCast.nextEq != tCast)
				tCast.eQsize.add(-1);
			
			tCast.coveredByColumnCover = true;
			
			
		}
		
	}
	
	@SuppressWarnings("unchecked")
	public void uncoverColumn(){ //découvrir colone
		
		if(this.R.L == this)
			return;
		
		this.Ln.size++;
		this.R.L = this;
		this.L.R = this;
		
		dtNode buffer = this;
		dtNode buffer2 = null;
		
		for(int i = 0; i < this.size; i++){
			
			buffer = buffer.U;
			
			buffer2 = buffer.Ln;
			
			for(int j = 0; j < buffer2.Ln.size; j++){
								
				buffer2 = buffer2.R;
				
				if(buffer2 == buffer)
					continue;
				else{
				
				if(buffer2.getClass() != dataNode.class){
					System.out.println(buffer2.getClass());
				}
				
				buffer2.C.size++;
				buffer2.D.U = buffer2;
				buffer2.U.D = buffer2;
				}
			}
			
			lineNode<F,E> tCast; 
			
			tCast = (lineNode<F,E>) buffer.Ln;
			tCast.U.D = tCast;
			tCast.D.U = tCast;
			
			tCast.nextEq.prevEq = tCast;
			tCast.prevEq.nextEq = tCast;
			if(tCast.nextEq != tCast)
				tCast.eQsize.add(1);
			
			tCast.coveredByColumnCover = false;
			
		}
		
	}
	
	public String toString(){
		
		return "Item : " + this.item.toString() + " Size : " + this.size;
		
	}
}
