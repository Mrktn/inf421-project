import java.util.*;
public class dancingLinks<F extends List<E>, E extends Comparable<E>> extends dtNode {
	
	//size nombre de colones (éléments différents dans l'ensemble de base)
	
	HashMap<F,ArrayList<F>> Eq;
	
	//création à partir des arguments de base
		
	
	@SuppressWarnings("unchecked")
	dancingLinks(F S, ArrayList<F> C, HashMap<F,ArrayList<F>> Eq){ 
		
		super(null,null,null,null,null,null);
		
		this.L = this;
		this.R = this;
		this.Ln = this;
		
		this.U = this;
		this.D = this;
		
		this.Eq = Eq;
		
		/*L'un des ajouts que j'ai fait par rapport au cas "de base" consiste à trier les elements, pour que l'ajout des elements se fasse plus rapidement. 
		 * Il faut pour cela un type générique qui soir comparable*/
		
		Collections.sort(S); 
		this.size = S.size();
		
		if(S.isEmpty()){
			return;
		}
		
		columnNode<F,E> buffer;
		
		buffer = new columnNode<F,E>(this, S.get(0), this);
		
		for(int i = 1; i < S.size(); i++){
			buffer = new columnNode<F,E>(buffer, S.get(i), this); //ajout des colonnes
		}
		
		lineNode<F,E> lineBuffer = null;
		
		for(F T : C){ //ajout des ensembles dans la structure
			
			ArrayList<F> eqBuffer = null;
			
			if(Eq != null){
				eqBuffer = Eq.get(T);
			}
			
			Collections.sort(T); //encore une fois, le tri permet un ajout plus rapide
			
			buffer = (columnNode<F,E>) this.R;
			
			lineBuffer = new lineNode<F,E>(T, this);
			
			int i = 0;
			
			while(i < T.size())
			{
				
				if(T.get(i).compareTo(buffer.item) == 0){
					new dataNode<F,E>(lineBuffer, buffer);
					i++;
				}
				if(buffer.R == this)
					break;
				buffer = (columnNode<F,E>) buffer.R;
				
			}
			
			/*On va maintenant ajouter les elements de la classe d'équivalence de T, en liant les lignes entre elles avec lineNode. 
			 * Le procédé n'est pas utile si Eq = null ou si la classe d'équivalence est restreinte au seul élément
			 */
			
			if(eqBuffer == null)
				continue;
			
			for(F U : eqBuffer){
				
				if(U.equals(T)){
					continue;
				}
				
				Collections.sort(U); 
				
				buffer = (columnNode<F,E>) this.R;
				
				lineNode<F,E> lineBuffer2 = new lineNode<F,E>(U, this, lineBuffer);
				
				i = 0;
				
				while(i < U.size())
				{
					
					if(U.get(i).compareTo(buffer.item) == 0){
						new dataNode<F,E>(lineBuffer2, buffer);
						i++;
					}
					if(buffer.R == this)
						break;
					buffer = (columnNode<F,E>) buffer.R;
					
				}
			}
		}
		
	}
	
	public void addRNode(dtNode that){ //ajout d'une colonne. Fonctionnalité non utilisée, car les colonnes sont déjà ajoutées dans le constructeur
		
		this.size++;
		
		if(this.size == 1){
			
			
			that.L = this;
			that.R = this;
			this.R = that;
			this.L = that;
			
			return;
		}
		
		this.R.addRNode(that);
		
	}
	
	public void addDNode(dtNode that){
		
		that.U = this.U;
		that.D = this;
		
		this.U.D = that;
		this.U = that;
	}
	
	@SuppressWarnings("unchecked")
	public String toString(){
		
		String str = "\n";
		
		lineNode<F,E> lCast = null;
		columnNode<F,E> cCast = null;
		
		dtNode lbuffer = this;
		dtNode cbuffer = this; 
		
		byte size = 1;
		
		while(lbuffer.D != this){
			lbuffer = lbuffer.D;
			lCast = (lineNode<F,E>) lbuffer;
			cbuffer = this;
			size ++;
			while(cbuffer.R != this){
				cbuffer = cbuffer.R;
				
				cCast = (columnNode<F,E>) cbuffer;
				
				if(lCast.set.contains(cCast.item))
					str += " 1";
				else
					str += " 0";
			}
			
			str += " Line for set : " + lCast.set.toString() + " Class : " + findClass(lCast.set);
			
			str += "\n";
		}
		
		str += size;
				
		
		return str;
		
	}
	
	public F findClass(F elt){
		
		if(Eq == null)
			return elt;
		
		if(elt == null || Eq.isEmpty()){
			return null;
		}
		
		if(Eq.keySet().contains(elt)){
			return elt;
		}
		
		for(F T : Eq.keySet()){
			if(Eq.get(T) != null && Eq.get(T).contains(elt))
				return T;
		}
		return null;
	}
	
}
